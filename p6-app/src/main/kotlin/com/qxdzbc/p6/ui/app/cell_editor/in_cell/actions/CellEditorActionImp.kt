package com.qxdzbc.p6.ui.app.cell_editor.in_cell.actions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.p6.app.command.Commands
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.qxdzbc.p6.app.action.worksheet.make_cell_editor_display_text.MakeCellEditorDisplayText
import com.qxdzbc.p6.app.common.utils.key_event.PKeyEvent
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.translator.jvm_translator.CellLiteralParser
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.document.cell.action.CellViewAction
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.qxdzbc.p6.ui.document.worksheet.cursor.actions.CursorAction
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import javax.inject.Inject

class CellEditorActionImp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>,
    private val cellLiteralParser: CellLiteralParser,
    private val cellViewAction: CellViewAction,
    private val cursorAction: CursorAction,
    private val makeDisplayText: MakeCellEditorDisplayText,
    private val open: OpenCellEditorAction,
) : CellEditorAction,
    MakeCellEditorDisplayText by makeDisplayText,
    OpenCellEditorAction by open
{

    private var appState by appStateMs
    private var cellEditorState by appState.cellEditorStateMs

    private fun isFormula(formula: String): Boolean {
        val script: String = formula.trim()
        val isFormula: Boolean = script.startsWith("=")
        return isFormula
    }

    override fun focus() {
        var editorState by appState.cellEditorStateMs
        val fcsMs = editorState.targetWbKey?.let { appState.getFocusStateMsByWbKey(it) }
        if (fcsMs != null) {
            fcsMs.value = fcsMs.value.focusOnEditor()
        }
    }

    private fun getWsState(editorState: CellEditorState): Ms<WorksheetState>? {
        val wbKey = editorState.targetWbKey
        val wsName = editorState.targetWsName
        if (wbKey != null && wsName != null) {
            return appState.getWsStateMs(wbKey, wsName)
        } else {
            return null
        }
    }

    override fun runFormula() {
        val editorState by appState.cellEditorStateMs
        val wsStateMs = getWsState(editorState)
        val ws = wsStateMs?.value?.worksheet
        val wbKey = editorState.targetWbKey
        val wsName = editorState.targetWsName
        val editTarget = editorState.targetCell
        if (ws != null && wbKey != null && wsName != null && editTarget != null) {
            // x: execute the formula in the editor
            val cell = ws.getCellOrNull(editTarget)
            val codeText = editorState.rangeSelectorTextField?.text ?: editorState.currentText

            val reverseRequest = if (cell?.formula != null) {
                CellUpdateRequest(
                    wbKey = wbKey,
                    wsName = wsName,
                    cellAddress = editTarget,
                    valueAsStr = cell.displayValue,
                    formula = cell.formula,
                )
            } else {
                CellUpdateRequest(
                    wbKey = wbKey,
                    wsName = wsName,
                    cellAddress = editTarget,
                    valueAsStr = cell?.displayValue,
                    cellValue = cell?.currentValue,
                )
            }
            var value: String? = null
            var formula: String? = null
            if (isFormula(codeText)) {
                formula = codeText
            } else {
                value = codeText
            }
            val request = CellUpdateRequest(
                wbKey = wbKey,
                wsName = wsName,
                cellAddress = editTarget,
                valueAsStr = value,
                formula = formula,
                cellValue = cellLiteralParser.parse(value)
            )
            val command = Commands.makeCommand(
                run = { cellViewAction.updateCell(request) },
                undo = { cellViewAction.updateCell(reverseRequest) }
            )
            appState.queryStateByWorkbookKey(wbKey).ifOk {
                val cMs = it.workbookState.commandStackMs
                cMs.value = cMs.value.add(command)
            }
            command.run()
            closeEditor()
        }
    }

    override fun closeEditor() {
        var editorState by appState.cellEditorStateMs
        val fcsMs = editorState.targetWbKey?.let { appState.getFocusStateMsByWbKey(it) }
        if (fcsMs != null) {
            fcsMs.value = fcsMs.value.focusOnCursor()
        }
        editorState = editorState.clearAllText().close()
    }

    override fun updateText(newText: String, ) {
        var editorState by appState.cellEditorStateMs
        if (editorState.isActive) {
            editorState = editorState
                .setCurrentText(newText)
        }
    }

    override fun updateTextField(newTextField: TextFieldValue) {
        var editorState by appState.cellEditorStateMs
        if (editorState.isActive) {
            val oldAllowRangeSelector = editorState.allowRangeSelector
            val newEditorState = editorState
                .setCurrentTextField(newTextField)
                .apply {
                    /*
                    when editor switch allow-range-selector flag
                    from true to false, move the respective cursor
                    to the currently edited cell
                    */
                    val isFromAllow_To_Disallow: Boolean = oldAllowRangeSelector && !this.allowRangeSelector
                    if (editorState.rangeSelectorIsSameAsTargetCursor) {
                        if (isFromAllow_To_Disallow) {
                            editorState.targetWbWs?.let { wbws ->
                                appState.getCursorStateMs(wbws)?.let { cursorStateMs ->
                                    editorState.targetCell?.let { editTarget ->
                                        cursorStateMs.value = cursorStateMs.value
                                            .removeAllExceptAnchorCell()
                                            .setMainCell(editTarget)
                                    }
                                }
                            }
                        }
                    }
                }
            editorState = newEditorState
        }
    }

    fun passKeyEventToRangeSelector(keyEvent: PKeyEvent, editorState: CellEditorState): Boolean {
        val wbws = editorState.rangeSelectorCursorId
        if (wbws != null) {
            val rt = appState.getCursorState(wbws)?.let { cs: CursorState ->
                cursorAction.handleKeyboardEvent(keyEvent, cs)
            } ?: false
            return rt
        } else {
            return false
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun handleKeyboardEvent(keyEvent: PKeyEvent): Boolean {
        var editorState by appState.cellEditorStateMs
        if (editorState.isActive) {
            when (keyEvent.key) {
                Key.Enter -> {
                    runFormula()
                    return true
                }
                Key.Escape -> {
                    closeEditor()
                    return true
                }
                else -> {
                    if (editorState.allowRangeSelector) {
                        if (keyEvent.isRangeSelectorToleratedKey()) {
                            val rt = passKeyEventToRangeSelector(keyEvent, editorState)
                            if(keyEvent.isRangeSelectorNavKey()){
                                // x: generate rs text
                                val rsText = makeDisplayText
                                    .makeRangeSelectorText(appState.cellEditorState)
                                // x: update range selector text
                                editorState = editorState.setRangeSelectorText(rsText)
                            }
                            return  rt
                        }else{
                            editorState = editorState.stopGettingRangeAddress()
                            return false //propagate the key event to the editor text field
                        }
                    } else {
                        return false //propagate the key event to the editor text field
                    }
                }
            }
        } else {
            return false
        }
    }
}