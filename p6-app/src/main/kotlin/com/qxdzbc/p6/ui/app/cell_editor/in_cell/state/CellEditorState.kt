package com.qxdzbc.p6.ui.app.cell_editor.in_cell.state

import androidx.compose.ui.text.input.TextFieldValue
import com.qxdzbc.p6.app.action.common_data_structure.WbWsImp
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.common.compose.St
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorStateId

interface CellEditorState {

    /**
     * move [rangeSelectorTextField]'s content to [currentTextField], then nullify [rangeSelectorTextField]
     */
    fun stopGettingRangeAddress():CellEditorState

    val allowRangeSelector: Boolean

    val rangeSelectorCursorIdSt: St<CursorStateId>?
    val rangeSelectorCursorId: CursorStateId?
    fun setRangeSelectorCursorId(i: St<CursorStateId>?): CellEditorState

    val rangeSelectorIsSameAsTargetCursor: Boolean
        get() = rangeSelectorCursorId?.let { rs ->
            targetCursorId?.let {
                it.isSame(rs)
            }
        } ?: false

    /**
     * This is for locating the edit target.
     * Which workbook, worksheet does the target cell belong to?
     */
    val targetCursorIdSt: St<CursorStateId>?
    val targetCursorId: CursorStateId?

    /**
     * This is the address of the cell being edited by this cell editor
     */
    val targetCell: CellAddress?
    fun setEditTarget(newCellAddress: CellAddress?): CellEditorState


    val targetWbKey: WorkbookKey? get() = targetCursorId?.wbKey
    val targetWsName: String? get() = targetCursorId?.wsName
    val targetWbWs: WbWs?
        get() {
            if (targetWbKey != null && targetWsName != null) {
                return WbWsImp(targetWbKey!!, targetWsName!!)
            } else {
                return null
            }
        }

    val displayTextField: TextFieldValue
    val displayText: String

    /**
     * [rangeSelectorTextField] = [currentText] + range address that produced by the cursor denoted by [rangeSelectorCursorId]. The content of [rangeSelectorTextField] will be moved into [currentText] when the range selector is done with its work
     */
    val rangeSelectorTextField: TextFieldValue?
    val rangeSelectorText: String?
    fun setRangeSelectorText(newTextField: TextFieldValue?): CellEditorState

    /**
     * current text is the one that hold the formula that will be run at the end
     */
    val currentText: String
    val currentTextField: TextFieldValue
    fun setCurrentText(newText: String): CellEditorState
    fun setCurrentTextField(newTextField: TextFieldValue): CellEditorState

    /**
     * clear both current text and range selector text
     */
    fun clearAllText(): CellEditorState

    val isActiveMs: Ms<Boolean>
    val isActive: Boolean

    val isActiveAndAllowRangeSelector:Boolean

    /**
     * open cell editor at cursor denoted by [cursorIdMs]
     */
    fun open(cursorIdMs: St<CursorStateId>): CellEditorState

    /**
     * switch this state to close state
     */
    fun close(): CellEditorState
}