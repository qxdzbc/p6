package com.qxdzbc.p6.ui.app.cell_editor.state

import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.action.worksheet.check_range_selector_state.CheckRangeSelectorStateAction
import com.qxdzbc.p6.app.action.worksheet.check_range_selector_state.CheckRangeSelectorStateActionImp
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.di.state.app_state.InitCellEditorInitCursorIdSt
import com.qxdzbc.p6.ui.app.cell_editor.RangeSelectorAllowState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId
import com.squareup.anvil.annotations.ContributesBinding
import org.antlr.v4.runtime.tree.ParseTree
import javax.inject.Inject

@ContributesBinding(P6AnvilScope::class)
data class CellEditorStateImp constructor(
    override val targetCursorIdSt: St< CursorStateId>? = null,
    override val isOpenMs: Ms<Boolean> = ms(false),
    override val currentTextField: TextFieldValue = TextFieldValue(""),
    override val targetCell: CellAddress? = null,
    override val rangeSelectorCursorIdSt: St< CursorStateId>? = null,
    override val rangeSelectorTextField: TextFieldValue? = null,
    override val parseTreeMs: Ms<ParseTree?> = ms(null),
    private val checkRangeSelector:CheckRangeSelectorStateAction,
    override val rangeSelectorAllowState: RangeSelectorAllowState = RangeSelectorAllowState.NOT_AVAILABLE,
    ) : CellEditorState {

    @Inject constructor(
         checkRangeSelector:CheckRangeSelectorStateAction,
    ):this(
        targetCursorIdSt=null,
        isOpenMs=ms(false),
        currentTextField=TextFieldValue(""),
        targetCell=null,
        rangeSelectorCursorIdSt=null,
        rangeSelectorTextField=null,
        parseTreeMs=ms(null),
        checkRangeSelector=checkRangeSelector,
        rangeSelectorAllowState=RangeSelectorAllowState.NOT_AVAILABLE
    )

    companion object {
        fun defaultForTest(): CellEditorStateImp {
            return CellEditorStateImp(checkRangeSelector = CheckRangeSelectorStateActionImp())
        }
    }

    override val parseTree: ParseTree? get() = parseTreeMs.value
    override fun setParseTree(i: ParseTree?): CellEditorState {
        this.parseTreeMs.value = i
        return this
    }

    override fun clearAll(): CellEditorState {
        return this.clearAllText().setParseTree(null)
    }

    override fun stopGettingRangeAddress(): CellEditorState {
        val rt = this.moveTextFromRangeSelectorTextToCurrentText()
        return rt
    }

    private fun moveTextFromRangeSelectorTextToCurrentText(): CellEditorState {
        if (rangeSelectorTextField != null) {
            return this.setCurrentText(rangeSelectorTextField.text).setRangeSelectorTextField(null)
        } else {
            return this
        }
    }

    override val allowRangeSelector: Boolean
        get() {
            return checkRangeSelector.check(this.currentText,this.currentTextField.selection.end)
        }

    override val isOpen: Boolean by isOpenMs
    override val isNotOpen: Boolean
        get() = !isOpen
    override val isActiveAndAllowRangeSelector: Boolean
        get() = isOpen && allowRangeSelector
    override val currentText: String get() = currentTextField.text

    override val rangeSelectorCursorId: CursorStateId?
        get() = rangeSelectorCursorIdSt?.value

    override fun setRangeSelectorCursorId(i: St<CursorStateId>?): CellEditorState {
        return this.copy(rangeSelectorCursorIdSt = i)
    }

    override val targetCursorId: CursorStateId?
        get() = targetCursorIdSt?.value

    override fun setTargetCell(newCellAddress: CellAddress?): CellEditorState {
        return this.copy(targetCell = newCellAddress)
    }

    override val displayTextField: TextFieldValue
        get() {
            if (this.isOpen) {
                val rst: TextFieldValue = this.rangeSelectorTextField ?: this.currentTextField
                return rst
            } else {
                return this.currentTextField
            }
        }
    override val displayText: String
        get() = displayTextField.text
    override val rangeSelectorText: String?
        get() = rangeSelectorTextField?.text

    override fun setRangeSelectorTextField(newTextField: TextFieldValue?): CellEditorState {
        return this.copy(rangeSelectorTextField = newTextField)
    }

    override fun setCurrentText(newText: String): CellEditorState {
        val tf = this.currentTextField
        return this.copy(
            currentTextField = tf
                .copy(text = newText, selection = TextRange(newText.length))
        )
    }

    override fun setCurrentTextField(newTextField: TextFieldValue): CellEditorState {
        return this.copy(currentTextField = newTextField)
    }

    override fun setDisplayTextField(newTextField: TextFieldValue): CellEditorState {
        if (this.isActiveAndAllowRangeSelector) {
            return this.setRangeSelectorTextField(newTextField)
        } else {
            return this.setCurrentTextField(newTextField)
        }
    }

    override fun clearAllText(): CellEditorState {
        return this.setCurrentText("").setRangeSelectorTextField(null)
    }

    /**
     * Open this cell editor at the cursor whose id is [cursorIdMs]
     */
    override fun open(cursorIdMs: St<CursorStateId>): CellEditorState {
        isOpenMs.value = true
        return this.copy(targetCursorIdSt = cursorIdMs, rangeSelectorCursorIdSt = cursorIdMs)
    }

    override fun close(): CellEditorState {
        isOpenMs.value = false
        return this.copy(targetCursorIdSt = null, targetCell = null)
            .stopGettingRangeAddress()
            .setRangeSelectorTextField(null)
    }
}
