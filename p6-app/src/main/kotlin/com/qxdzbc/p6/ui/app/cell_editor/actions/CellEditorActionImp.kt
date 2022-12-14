package com.qxdzbc.p6.ui.app.cell_editor.actions

import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.app.action.cell_editor.close_cell_editor.CloseCellEditorAction
import com.qxdzbc.p6.app.action.cell_editor.color_formula.ColorFormulaInCellEditorAction
import com.qxdzbc.p6.app.action.cell_editor.cycle_formula_lock_state.CycleFormulaLockStateAction
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.qxdzbc.p6.app.action.cell_editor.run_formula.RunFormulaOrSaveValueToCellAction
import com.qxdzbc.p6.app.action.cursor.handle_cursor_keyboard_event.HandleCursorKeyboardEventAction
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorTextAction
import com.qxdzbc.p6.app.common.key_event.P6KeyEvent
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.PartialTreeExtractor
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.translator.jvm_translator.CellLiteralParser
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import com.qxdzbc.p6.ui.app.cell_editor.RangeSelectorAllowState
import com.qxdzbc.p6.ui.app.cell_editor.actions.differ.TextDiffer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class, boundType = CellEditorAction::class)
class CellEditorActionImp @Inject constructor(
    private val cellLiteralParser: CellLiteralParser,
    private val updateCellAction: UpdateCellAction,
    private val handleCursorKeyboardEventAct: HandleCursorKeyboardEventAction,
    private val makeDisplayText: MakeCellEditorTextAction,
    private val openCellEditor: OpenCellEditorAction,
    private val stateContMs: Ms<StateContainer>,
    private val textDiffer: TextDiffer,
    val cycleLockStateAct: CycleFormulaLockStateAction,
    @PartialTreeExtractor
    val treeExtractor: TreeExtractor,
    val colorFormulaAction: ColorFormulaInCellEditorAction,
    val closeCellEditorAction: CloseCellEditorAction,
    val runFormulaOrSaveValueToCellAction: RunFormulaOrSaveValueToCellAction,
) : CellEditorAction,
    RunFormulaOrSaveValueToCellAction by runFormulaOrSaveValueToCellAction,
    CloseCellEditorAction by closeCellEditorAction,
    CycleFormulaLockStateAction by cycleLockStateAct,
    MakeCellEditorTextAction by makeDisplayText,
    OpenCellEditorAction by openCellEditor {

    private val stateCont by stateContMs
    private val editorStateMs = stateCont.cellEditorStateMs
    private val editorState by editorStateMs

//    private fun isFormula(formula: String): Boolean {
//        val script: String = formula.trim()
//        val isFormula: Boolean = script.startsWith("=")
//        return isFormula
//    }

    override fun focusOnCellEditor() {
        val fcsMs = editorState.targetWbKey?.let { stateCont.getFocusStateMsByWbKey(it) }
        if (fcsMs != null) {
            fcsMs.value = fcsMs.value.focusOnEditor()
        }
    }

    override fun freeFocusOnCellEditor() {
        val fcsMs = editorState.targetWbKey?.let { stateCont.getFocusStateMsByWbKey(it) }
        if (fcsMs != null) {
            fcsMs.value = fcsMs.value.freeFocusOnEditor()
        }
    }

    override fun setCellEditorFocus(i: Boolean) {
        val fcsMs = editorState.targetWbKey?.let { stateCont.getFocusStateMsByWbKey(it) }
        if (fcsMs != null) {
            fcsMs.value = fcsMs.value.setCellEditorFocus(i)
        }
    }

    /**
     * **For testing only.**
     * Be careful when using this function. It directly updates the text content and will erase all the text formats. Should be use for testing only. Even so, be extra careful when use this in tests. Use [changeText] in the app instead.
     */
    override fun changeText(newText: String) {
        val editorState by stateCont.cellEditorStateMs
        if (editorState.isOpen) {
            val newTextField = TextFieldValue(text = newText, selection = TextRange(newText.length))
            this.changeTextField(newTextField)
        }
    }

    /**
     * This function is called when users type with their keyboards into the editor. It does the following:
     *  - update the text field displayed by the cell editor
     *  - point the new text to the correct location which could be either the current text, or the temp text, depending on the current state of the state editor. If the range selector is activated, the new text will be stored in the temp text, and only when the range selector is deactivated, the temp text is moved to the current text.
     *  - update the internal parse tree of cell editor
     *
     */
    override fun changeTextField(newTextField: TextFieldValue) {
        val editorState by stateCont.cellEditorStateMs
        val ntf = newTextField
        if (editorState.isOpen) {
            val oldRSAState = editorState.rangeSelectorAllowState
            val autoCompletedTf = autoCompleteBracesIfPossible(editorState.displayTextField, ntf)

            val newEditorState = editorState
                // x: because the new text is input by user, it is stored directly into current text
                .setCurrentTextField(autoCompletedTf)
                .let {
                    /*
                    when the cell editor switches off allow-range-selector flag, if the range-selector cell cursor and target cell cursor are the same, reset the cell cursor state and point it to the currently edited cell so that on the UI it moves back to the currently edited cell. This does not change the content of the cell editor state.
                    */
                    val from_Allow_To_Disallow: Boolean =
                        oldRSAState.isAllow() && it.rangeSelectorAllowState.isAllow().not()
                    if (from_Allow_To_Disallow) {
                        if (it.rangeSelectorIsSameAsTargetCursor) {
                            it.targetCursorId?.let { cursorId ->
                                stateCont.getCursorStateMs(cursorId)
                            }?.let { targetCursorMs ->
                                it.targetCell?.let { targetCell ->
                                    targetCursorMs.value = targetCursorMs.value
                                        .removeAllExceptMainCell()
                                        .setMainCell(targetCell)
                                }
                            }
                        }
                    }
                    it
                }
            stateCont.cellEditorStateMs.value = newEditorState
            colorFormulaAction.colorCurrentTextInCellEditor()
        }
    }

    fun autoCompleteBracesIfPossible(oldTf: TextFieldValue, newTextField: TextFieldValue): TextFieldValue {
        val ntf = newTextField
        if (oldTf == ntf) {
            return ntf
        }

        val diffRs = textDiffer.extractTextAddition(oldTf, newTextField)
        val rt = diffRs?.let { tnr ->
            val braces = when (tnr.text) {
                "(" -> "()"
                "[" -> "[]"
                "{" -> "{}"
                else -> null
            }
            val cursorPosition = tnr.range.end - 1
            val newText: String = braces?.let {
                ntf.text.substring(0, cursorPosition) + it + ntf.text.substring(cursorPosition + 1, ntf.text.length)
            } ?: ntf.text
            val newRange: TextRange = braces?.let {
                ntf.selection
            } ?: ntf.selection
            ntf.copy(text = newText, selection = newRange)
        } ?: ntf
        return rt
    }

    /**
     * pass keyboard event caught by a cell editor to its range selector (which is another cell cursor).
     */
    private fun passKeyEventToRangeSelector(keyEvent: P6KeyEvent, rangeSelectorId: CursorId?): Boolean {
        val rt: Boolean = rangeSelectorId?.let {
            handleCursorKeyboardEventAct.handleKeyboardEvent(keyEvent, it)
        } ?: false
        return rt
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun handleKeyboardEvent(keyEvent: P6KeyEvent): Boolean {
        if (editorState.isOpen) {
            if (keyEvent.type == KeyEventType.KeyDown) {
                when (keyEvent.key) {
                    Key.F4 -> {
                        cycleFormulaLockState()
                        return true
                    }

                    Key.Enter -> {
                        val targetCursorId = editorState.targetCursorId
                        if (keyEvent.isAltPressedAlone) {
                            val newText = editorState.currentTextField
                                .let { ctf ->
                                    ctf.copy(
                                        text = ctf.text + "\n",
                                        selection = TextRange(ctf.selection.end + 1)
                                    )
                                }
                            changeTextField(newText)
                        } else {
                            runFormulaOrSaveValueToCell(true)
                        }
                        // x: move the target cursor 1 row down
                        targetCursorId?.also {
                            stateCont.getCursorStateMs(it)?.also { csMs ->
                                csMs.value = csMs.value.removeAllExceptMainCell().let{cs->
                                    if(keyEvent.isShiftPressedInCombination){
                                        cs.up()
                                    }else{
                                        cs.down()
                                    }
                                }
                            }
                        }
                        return true
                    }

                    Key.Escape -> {
                        closeEditor()
                        return true
                    }

                    else -> {
                        if (editorState.rangeSelectorAllowState == RangeSelectorAllowState.ALLOW) {
                            if (keyEvent.isAcceptedByRangeSelector()) {
                                val rt = this.passKeyEventToRangeSelector(keyEvent, editorState.rangeSelectorCursorId)
                                if (keyEvent.isRangeSelectorNavKey()) {
                                    // x: generate range selector text
                                    val rsText = makeDisplayText
                                        .makeRangeSelectorText(stateCont.cellEditorState)
                                    // x: update range selector text
                                    editorStateMs.value =
                                        colorFormulaAction.colorDisplayTextInCellEditor(
                                            editorState.setRangeSelectorTextField(rsText)
                                        )
                                }
                                return rt
                            } else {
                                editorStateMs.value = editorState.stopGettingRangeAddress()
                                // propagate the key event further
                                return false
                            }
                        } else {
                            // propagate the key event further
                            return false
                        }
                    }
                }

            } else {
                return false
            }
        } else {
            return false
        }
    }
}
