package com.qxdzbc.p6.app.action.cursor.handle_cursor_keyboard_event

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.cell_editor.open_cell_editor.OpenCellEditorAction
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.cursor.copy_cursor_range_to_clipboard.CopyCursorRangeToClipboardAction
import com.qxdzbc.p6.app.action.cursor.paste_range_to_cursor.PasteRangeToCursor
import com.qxdzbc.p6.app.action.cursor.undo_on_cursor.UndoOnCursorAction
import com.qxdzbc.p6.app.action.worksheet.WorksheetAction
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiAtCursorRequest
import com.qxdzbc.p6.app.action.worksheet.make_slider_follow_cell.MakeSliderFollowCellAction
import com.qxdzbc.p6.app.common.key_event.P6KeyEvent
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerState
import com.qxdzbc.p6.ui.document.worksheet.select_whole_col_for_selected_cell.SelectWholeColumnForAllSelectedCellAction
import com.qxdzbc.p6.ui.document.worksheet.select_whole_row_for_selected_cells.SelectWholeRowForAllSelectedCellAction
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@OptIn(ExperimentalComposeUiApi::class)
@P6Singleton
@ContributesBinding(scope= P6AnvilScope::class)
class HandleCursorKeyboardEventActionImp  @Inject constructor(

    private val wsAction: WorksheetAction,
    private val openCellEditor: OpenCellEditorAction,
    private val stateContSt:St<@JvmSuppressWildcards StateContainer>,
    private val pasteRangeToCursorAction: PasteRangeToCursor,
    private val selectWholeCol: SelectWholeColumnForAllSelectedCellAction,
    private val selectWholeRow: SelectWholeRowForAllSelectedCellAction,
    private val makeSliderFollowCellAct: MakeSliderFollowCellAction,
    private val copyCursorRangeToClipboardAction: CopyCursorRangeToClipboardAction,
    private val undoOnCursorAct: UndoOnCursorAction,

    ) : HandleCursorKeyboardEventAction {

    private val sc by stateContSt

    @OptIn(ExperimentalComposeUiApi::class)
    override fun handleKeyboardEvent(
        keyEvent: P6KeyEvent,
        wbws: WbWs,
    ): Boolean {
        val wsState = sc.getWsState(wbws)
        if (wsState != null) {
            val cursorState by wsState.cursorStateMs
            return if (keyEvent.type == KeyEventType.KeyDown) {
                if (keyEvent.isCtrlPressedAlone) {
                    handleKeyWithCtrlDown(keyEvent, cursorState)
                } else if (keyEvent.isShiftPressedAlone) {
                    handleKeyboardEventWhenShiftDown(keyEvent, cursorState)
                } else if (keyEvent.isCtrlShiftPressed) {
                    handleKeyWithCtrlShift(keyEvent, wbws)
                } else {

                    val wsStateMs =
                        sc.getWsStateMs(cursorState.id.wbKey, cursorState.id.wsName)
                    val cursorStateMs =
                        sc.getCursorStateMs(cursorState.id.wbKey, cursorState.id.wsName)
                    val colRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.colRulerStateMs
                    val rowRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.rowRulerStateMs

                    if (wsStateMs != null && cursorStateMs != null && colRulerStateMs != null && rowRulerStateMs != null) {
                        when (keyEvent.key) {
                            Key.Escape -> {
                                wsStateMs.value.cursorStateMs.value = cursorState.removeClipboardRange()
                                true
                            }
                            Key.Delete -> {
                                _onDeleteKey(cursorState)
                                true
                            }
                            Key.F2 -> {
                                f2(wsStateMs.value.id)
                                true
                            }
                            Key.DirectionUp -> {
                                up(wbws)
                                true
                            }
                            Key.DirectionDown -> {
                                down(wbws)
                                true
                            }
                            Key.DirectionLeft -> {
                                left(wbws)
                                true
                            }
                            Key.DirectionRight -> {
                                right(wbws)
                                true
                            }
                            Key.Home -> {
                                this.home(wbws)
                                true
                            }
                            Key.MoveEnd -> {
                                this.end(wbws)
                                true
                            }
                            else -> false
                        }
                    } else {
                        false
                    }
                }
            } else {
                false
            }

        } else {
            return false
        }
    }

    private fun handleKeyboardEventWhenShiftDown(
        keyEvent: P6KeyEvent,
        wbws: WbWs
    ): Boolean {
        if (keyEvent.isShiftPressedAlone) {

            val wsStateMs = sc.getWsStateMs(wbws)
            val cursorStateMs = sc.getCursorStateMs(wbws)
            val colRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.colRulerStateMs
            val rowRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.rowRulerStateMs
            if (wsStateMs != null && cursorStateMs != null && colRulerStateMs != null && rowRulerStateMs != null) {
                return when (keyEvent.key) {
                    Key.DirectionUp -> {
                        shiftUp(wbws)
                        true
                    }
                    Key.DirectionDown -> {
                        shiftDown(wbws)
                        true
                    }
                    Key.DirectionLeft -> {
                        shiftLeft(wbws)
                        true
                    }
                    Key.DirectionRight -> {
                        shiftRight(wbws)
                        true
                    }
                    Key.Spacebar -> {
                        selectWholeRow.selectWholeRowForAllSelectedCells(wbws)
                        true
                    }
                    else -> false
                }

            } else {
                return false
            }
        } else {
            return false
        }
    }

    private fun handleKeyWithCtrlShift(keyEvent: P6KeyEvent, wbws: WbWs): Boolean {
        if (keyEvent.isCtrlShiftPressed) {
            return when (keyEvent.key) {
                Key.DirectionUp -> {
                    ctrlShiftUp(wbws)
                    true
                }
                Key.DirectionDown -> {
                    ctrlShiftDown(wbws)
                    true
                }
                Key.DirectionLeft -> {
                    ctrlShiftLeft(wbws)
                    true
                }
                Key.DirectionRight -> {
                    ctrlShiftRight(wbws)
                    true
                }
                else -> false
            }
        } else {
            return false
        }
    }

    private fun handleKeyWithCtrlDown(keyEvent: P6KeyEvent, wbws: WbWs): Boolean {
        val cursorState = sc.getCursorState(wbws)
        if (cursorState != null) {
            if (keyEvent.isCtrlPressedAlone) {
                val wsStateMs = sc.getWsStateMs(cursorState.id.wbKey, cursorState.id.wsName)
                val cursorStateMs = sc.getCursorStateMs(cursorState.id.wbKey, cursorState.id.wsName)
                val colRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.colRulerStateMs
                val rowRulerStateMs: Ms<RulerState>? = wsStateMs?.value?.rowRulerStateMs
                if (wsStateMs != null && cursorStateMs != null && colRulerStateMs != null && rowRulerStateMs != null) {
                    return when (keyEvent.key) {
                        Key.V -> {
                            pasteRangeToCursorAction.pasteRange(wsStateMs.value.cursorState)
                            true
                        }
                        Key.C -> {
                            copyCursorRangeToClipboardAction.copyCursorRangeToClipboard(cursorState)
                            true
                        }
                        Key.Z -> {
                            undoOnCursorAct.undoOnCursor(cursorState)
                            true
                        }
                        Key.DirectionUp -> {
                            ctrlUp(wbws)
                            true
                        }
                        Key.DirectionDown -> {
                            ctrlDown(wbws)
                            true
                        }
                        Key.DirectionRight -> {
                            ctrlRight(wbws)
                            true
                        }
                        Key.DirectionLeft -> {
                            ctrlLeft(wbws)
                            true
                        }
                        Key.Spacebar -> {
                            selectWholeCol.selectWholeColForAllSelectedCells(wbws)
                            true
                        }
                        else -> false
                    }

                } else {
                    return false
                }
            } else {
                return false
            }
        } else {
            return false
        }
    }

    fun home(wsStateMs:Ms<WorksheetState>?){
        wsStateMs?.also {
            val wsState by it
            val cursorState by wsState.cursorStateMs
            val targetCell = CellAddress(wsState.firstCol, cursorState.mainCell.rowIndex)
            val newCursorState = cursorState.setMainCell(targetCell).removeAllExceptMainCell()
            wsAction.makeSliderFollowCursorMainCell(newCursorState, wsState)
        }
    }

    override fun home(wbwsSt: WbWsSt) {
        home(sc.getWsStateMs(wbwsSt))
    }

    override fun home(wbws: WbWs) {
        home(sc.getWsStateMs(wbws))
    }

    fun end(wsStateMs: Ms<WorksheetState>?){
        wsStateMs?.also {
            val wsState by wsStateMs
            val cursorState by wsState.cursorStateMs
            val targetCell = CellAddress(wsState.lastCol, cursorState.mainCell.rowIndex)
            val newCursorState = cursorState.setMainCell(targetCell).removeAllExceptMainCell()
            wsState.cursorStateMs.value = newCursorState
            wsAction.makeSliderFollowCursorMainCell(newCursorState, wsState)
        }
    }

    override fun end(wbwsSt: WbWsSt) {
        end(sc.getWsStateMs(wbwsSt))
    }

    override fun end(wbws: WbWs) {
        end(sc.getWsStateMs(wbws))
    }

    private fun ctrlUpNoUpdate(wbws: WbWs): CursorState? {
        // go to the nearest non-empty cell on the same col of anchor cell
        val rt = sc.getWsState(wbws)?.let { wsState: WorksheetState ->
            val cursorState: CursorState by wsState.cursorStateMs
            val worksheet = wsState.worksheet
            val mainCell: CellAddress = cursorState.mainCell
            val colIndex = mainCell.colIndex
            val rowIndex = mainCell.rowIndex
            val row = worksheet.getCol(colIndex).map { it.address.rowIndex }.filter { it < rowIndex }.maxOrNull()
            if (row != null) {
                cursorState.setMainCell(CellAddress(colIndex, row))
            } else {
                cursorState
                    .setMainCell(CellAddress(colIndex, wsState.firstRow)).removeAllExceptMainCell()
            }
        }
        return rt
    }

    fun ctrlUp(wsStateMs: Ms<WorksheetState>?) {
        wsStateMs?.also{
            val wsState by it
            val cursorStateMs = wsState.cursorStateMs
            // go to the nearest non-empty cell on the same col of anchor cell
            val cursorState by cursorStateMs
            val newCursorState = ctrlUpNoUpdate(wsState)
            if (newCursorState != null) {
                cursorStateMs.value = newCursorState
                wsAction.makeSliderFollowCursorMainCell(newCursorState, wsState)
            }
        }
    }
    override fun ctrlUp(wbwsSt: WbWsSt) {
        ctrlUp(sc.getWsStateMs(wbwsSt))
    }

    override fun ctrlUp(wbws: WbWs) {
        ctrlUp(sc.getWsStateMs(wbws))
    }

    private fun ctrlDownNoUpdate(wbws: WbWs): CursorState? {
        val rt = sc.getWsState(wbws)?.let { wsState: WorksheetState ->
            val cursorState: CursorState by wsState.cursorStateMs
            val worksheet = wsState.worksheet
            val mainCell: CellAddress = cursorState.mainCell
            val colIndex = mainCell.colIndex
            val rowIndex = mainCell.rowIndex
            val row = worksheet.getCol(colIndex).map { it.address.rowIndex }.filter { it > rowIndex }.minOrNull()
            if (row != null) {
                cursorState.setMainCell(CellAddress(colIndex, row))
            } else {
                cursorState
                    .setMainCell(CellAddress(colIndex, wsState.lastRow))
                    .removeAllExceptMainCell()
            }
        }
        return rt
    }

    fun ctrlDown(wsStateMs: Ms<WorksheetState>?) {
        wsStateMs?.also {
            val wsState by it
            val cursorStateMs = wsStateMs.value.cursorStateMs
            val newCursor = ctrlDownNoUpdate(wsState)
            if (newCursor != null) {
                cursorStateMs.value = newCursor
                wsAction.makeSliderFollowCursorMainCell(newCursor, wsState)
            }
        }
    }
    override fun ctrlDown(wbwsSt: WbWsSt) {
        ctrlDown(sc.getWsStateMs(wbwsSt))
    }

    override fun ctrlDown(wbws: WbWs) {
        ctrlDown(sc.getWsStateMs(wbws))
    }


    fun ctrlRight(wsStateMs: Ms<WorksheetState>?) {
        wsStateMs?.also {
            val wsState by it
            val cursorStateMs = wsStateMs.value.cursorStateMs
            val newCursorState = ctrlRightNoUpdate(wsState)
            if (newCursorState != null) {
                cursorStateMs.value = newCursorState
                wsAction.makeSliderFollowCursorMainCell(newCursorState, wsState)
            }
        }
    }
    override fun ctrlRight(wbwsSt: WbWsSt) {
        ctrlRight(sc.getWsStateMs(wbwsSt))
    }

    override fun ctrlRight(wbws: WbWs) {
        ctrlRight(sc.getWsStateMs(wbws))
    }

    /**
     * produce a new cursor state without updating any ms
     */
    private fun ctrlRightNoUpdate(wbws: WbWs): CursorState? {
        val cursorState: CursorState? = sc.getCursorState(wbws)
        val wsState: WorksheetState? = sc.getWsState(wbws)
        if (cursorState != null && wsState != null) {
            val worksheet = wsState.worksheet
            val mainCell: CellAddress = cursorState.mainCell
            val colIndex = mainCell.colIndex
            val rowIndex = mainCell.rowIndex
            val col = worksheet.getRow(mainCell.rowIndex)
                .map { it.address.colIndex }
                .filter { it > colIndex }
                .minOrNull()
            if (col != null) {
                return cursorState.setMainCell(CellAddress(col, rowIndex))
            } else {
                return cursorState
                    .setMainCell(CellAddress(wsState.lastCol, rowIndex))
                    .removeAllExceptMainCell()
            }
        } else {
            return null
        }
    }

    fun ctrlShiftLeft(wsState: WorksheetState?) {
        if (wsState != null) {
            val cursorState by wsState.cursorStateMs
            val cell1 = cursorState.mainCell
            val minCol = cursorState.minCol
            if (minCol != null) {
                val anchor2 = (this.ctrlLeftNoUpdate(wsState) ?: cursorState).mainCell
                val minRow = cursorState.minRow
                val maxRow = cursorState.maxRow

                if (minRow != null && maxRow != null) {
                    val cell3 = CellAddress(cell1.colIndex, minRow)
                    val cell4 = CellAddress(cell1.colIndex, maxRow)
                    wsState.cursorStateMs.value = cursorState.setMainCell(cell1).removeAllExceptMainCell().addFragRange(
                        RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                    )
                }
            }
        }
    }
    override fun ctrlShiftLeft(wbwsSt: WbWsSt) {
        val wsState: WorksheetState? = sc.getWsState(wbwsSt)
        ctrlShiftLeft(wsState)
    }

    override fun ctrlShiftLeft(wbws: WbWs) {
        val wsState: WorksheetState? = sc.getWsState(wbws)
        ctrlShiftLeft(wsState)
    }

    fun ctrlShiftRight(wsState: WorksheetState?){
        wsState?.also {
            val cursorState by wsState.cursorStateMs
            val cell1 = cursorState.mainCell
            val maxCol = cursorState.maxCol
            if (maxCol != null) {
                val anchor2 = this.ctrlRightNoUpdate(wsState)?.mainCell
                if (anchor2 != null) {
                    val minRow = cursorState.minRow
                    val maxRow = cursorState.maxRow

                    if (minRow != null && maxRow != null) {
                        val cell3 = CellAddress(cell1.colIndex, minRow)
                        val cell4 = CellAddress(cell1.colIndex, maxRow)
                        wsState.cursorStateMs.value =
                            cursorState.setMainCell(cell1).removeAllExceptMainCell().addFragRange(
                                RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                            )
                    }
                }
            }
        }
    }

    override fun ctrlShiftRight(wbwsSt: WbWsSt) {
        ctrlShiftRight(sc.getWsState(wbwsSt))
    }

    override fun ctrlShiftRight(wbws: WbWs) {
        ctrlShiftRight(sc.getWsState(wbws))
    }

    fun ctrlShiftUp(wsState: WorksheetState?) {
        wsState?.also {
            val cursorState by wsState.cursorStateMs
            val cell1 = cursorState.mainCell
            val minRow = cursorState.minRow
            if (minRow != null) {
                val anchor2 = this.ctrlUpNoUpdate(wsState)?.mainCell
                if (anchor2 != null) {
                    val maxCol = cursorState.maxCol
                    val minCol = cursorState.minCol
                    if (maxCol != null && minCol != null) {
                        val cell3 = CellAddress(maxCol, cell1.rowIndex)
                        val cell4 = CellAddress(minCol, cell1.rowIndex)
                        wsState.cursorStateMs.value =
                            cursorState.setMainCell(cell1).removeAllExceptMainCell().addFragRange(
                                RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                            )
                    }
                }
            }
        }

    }
    override fun ctrlShiftUp(wbwsSt: WbWsSt) {
        ctrlShiftUp(sc.getWsState(wbwsSt))
    }

    override fun ctrlShiftUp(wbws: WbWs) {
        ctrlShiftUp(sc.getWsState(wbws))
    }

    fun ctrlShiftDown(wsState: WorksheetState?) {
        wsState?.also {
            var cursorState by wsState.cursorStateMs
            val cell1 = cursorState.mainCell
            val maxRow = cursorState.maxRow
            if (maxRow != null) {
                val anchor2 = this.ctrlDownNoUpdate(
                    cursorState.setMainCell(CellAddress(cell1.colIndex, maxRow))
                )?.mainCell
                if (anchor2 != null) {
                    val maxCol = cursorState.maxCol
                    val minCol = cursorState.minCol
                    if (maxCol != null && minCol != null) {
                        val cell3 = CellAddress(maxCol, cell1.rowIndex)
                        val cell4 = CellAddress(minCol, cell1.rowIndex)
                        cursorState = cursorState.setMainCell(cell1).removeAllExceptMainCell().addFragRange(
                            RangeAddress(listOf(cell1, cell3, cell4, anchor2))
                        )
                    }
                }
            }

        }

    }
    override fun ctrlShiftDown(wbwsSt: WbWsSt) {
        ctrlShiftDown(sc.getWsState(wbwsSt))
    }

    override fun ctrlShiftDown(wbws: WbWs) {
        ctrlShiftDown(sc.getWsState(wbws))
    }

    fun ctrlLeft(cursorStateMs:Ms<CursorState>?) {
        cursorStateMs?.also {
            val cs by cursorStateMs
            this.ctrlLeftNoUpdate(cs)?.also { newCursor ->
                cursorStateMs.value = newCursor
                wsAction.makeSliderFollowCursorMainCell(newCursor, cs)
            }
        }
    }
    override fun ctrlLeft(wbwsSt: WbWsSt) {
        ctrlLeft(sc.getCursorStateMs(wbwsSt))
    }

    override fun ctrlLeft(wbws: WbWs) {
        ctrlLeft(sc.getCursorStateMs(wbws))
    }

    private fun ctrlLeftNoUpdate(wbws: WbWs): CursorState? {
        val rt = sc.getWsState(wbws)?.let { wsState: WorksheetState ->
            val cursorState: CursorState by wsState.cursorStateMs
            val worksheet = wsState.worksheet
            val mainCell: CellAddress = cursorState.mainCell
            val anchorColIndex = mainCell.colIndex
            val anchoRowIndex = mainCell.rowIndex
            val col = worksheet.getRow(anchoRowIndex)
                .map { it.address.colIndex }
                .filter { it < anchorColIndex }
                .maxOrNull()
            if (col != null) {
                cursorState.setMainCell(CellAddress(col, anchoRowIndex))
            } else {
                cursorState
                    .setMainCell(CellAddress(wsState.firstCol, anchoRowIndex))
                    .removeAllExceptMainCell()
            }
        }
        return rt
    }



    fun up(cursorStateMs:Ms<CursorState>?) {
        cursorStateMs?.also {
            cursorStateMs.value = cursorStateMs.value.up()
            wsAction.makeSliderFollowCursorMainCell(cursorStateMs.value, cursorStateMs.value)
        }
    }
    override fun up(wbwsSt: WbWsSt) {
        up(sc.getCursorStateMs(wbwsSt))
    }

    override fun up(wbws: WbWs) {
        up(sc.getCursorStateMs(wbws))
    }



    fun down(cursorStateMs:Ms<CursorState>?) {
        cursorStateMs?.also {
            cursorStateMs.value = cursorStateMs.value.down()
            wsAction.makeSliderFollowCursorMainCell(cursorStateMs.value, cursorStateMs.value)
        }
    }
    override fun down(wbwsSt: WbWsSt) {
        down(sc.getCursorStateMs(wbwsSt))
    }

    override fun down(wbws: WbWs) {
        down(sc.getCursorStateMs(wbws))
    }

    fun left(cursorStateMs:Ms<CursorState>?) {
        cursorStateMs?.also {
            cursorStateMs.value = cursorStateMs.value.left()
            wsAction.makeSliderFollowCursorMainCell(cursorStateMs.value, cursorStateMs.value)
        }
    }
    override fun left(wbwsSt: WbWsSt) {
        left(sc.getCursorStateMs(wbwsSt))
    }

    override fun left(wbws: WbWs) {
        left(sc.getCursorStateMs(wbws))
    }

    fun right(cursorStateMs:Ms<CursorState>?) {
        cursorStateMs?.also {
            val cursorState by cursorStateMs
            cursorStateMs.value = cursorStateMs.value.right()
            wsAction.makeSliderFollowCursorMainCell(cursorStateMs.value, cursorState)
        }
    }

    override fun right(wbwsSt: WbWsSt) {
        right(sc.getCursorStateMs(wbwsSt))
    }

    override fun right(wbws: WbWs) {
        right(sc.getCursorStateMs(wbws))
    }

    fun shiftUp(cursorStateMs:Ms<CursorState>?) {
        cursorStateMs?.also {
            var cursorState by cursorStateMs
            val mainCell = cursorState.mainCell
            val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
            cursorState = cursorState.removeAllFragmentedCells()
                .setMainRange(RangeAddresses.from2Cells(mainCell, theOtherCell.upOneRow()))
            wsAction.makeSliderFollowCursorMainCell(cursorState, cursorState)
            val followTarget = cursorState.mainRange?.topLeft ?: cursorState.mainCell
            makeSliderFollowCellAct.makeSliderFollowCell(cursorState,followTarget)
        }
    }
    override fun shiftUp(wbwsSt: WbWsSt) {
        shiftUp(sc.getCursorStateMs(wbwsSt))
    }

    override fun shiftUp(wbws: WbWs) {
        shiftUp(sc.getCursorStateMs(wbws))
    }


    fun shiftDown(cursorStateMs:Ms<CursorState>?) {
        cursorStateMs?.also {
            var cursorState by cursorStateMs
            val mainCell = cursorState.mainCell
            val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
            cursorState = cursorState.removeAllFragmentedCells()
                .setMainRange(RangeAddresses.from2Cells(mainCell, theOtherCell.downOneRow()))
            val followTarget = cursorState.mainRange?.botRight ?: cursorState.mainCell
            makeSliderFollowCellAct.makeSliderFollowCell(cursorState,followTarget)
        }
    }
    override fun shiftDown(wbwsSt: WbWsSt) {
        shiftDown(sc.getCursorStateMs(wbwsSt))
    }

    override fun shiftDown(wbws: WbWs) {
        shiftDown(sc.getCursorStateMs(wbws))
    }

    fun shiftLeft(cursorStateMs:Ms<CursorState>?) {
        cursorStateMs?.also { cursorStateMs ->
            var cursorState by cursorStateMs
            val mainCell = cursorState.mainCell
            val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
            cursorState = cursorState.removeAllFragmentedCells()
                .setMainRange(RangeAddresses.from2Cells(mainCell, theOtherCell.leftOneCol()))
            val followTarget = cursorState.mainRange?.topLeft ?: cursorState.mainCell
            makeSliderFollowCellAct.makeSliderFollowCell(cursorState,followTarget)
        }

    }
    override fun shiftLeft(wbwsSt: WbWsSt) {
        shiftLeft(sc.getCursorStateMs(wbwsSt))
    }

    override fun shiftLeft(wbws: WbWs) {
        shiftLeft(sc.getCursorStateMs(wbws))
    }

    fun shiftRight(cursorStateMs:Ms<CursorState>?) {
        cursorStateMs?.also {
            var cursorState by cursorStateMs
            val mainCell = cursorState.mainCell
            val theOtherCell = cursorState.mainRange?.takeCrossCell(mainCell) ?: mainCell
            cursorState = cursorState.removeAllFragmentedCells()
                .setMainRange(RangeAddresses.from2Cells(mainCell, theOtherCell.rightOneCol()))
            val followTarget = cursorState.mainRange?.botRight ?: cursorState.mainCell
            makeSliderFollowCellAct.makeSliderFollowCell(cursorState,followTarget)
        }
    }
    override fun shiftRight(wbwsSt: WbWsSt) {
        shiftRight(sc.getCursorStateMs(wbwsSt))
    }

    override fun shiftRight(wbws: WbWs) {
        shiftRight(sc.getCursorStateMs(wbws))
    }

    override fun f2(wbws: WbWs) {
        openCellEditor.openCellEditor(wbws)
    }
    override fun f2(wbwsSt: WbWsSt) {
        openCellEditor.openCellEditor(wbwsSt)
    }

    fun _onDeleteKey(cursorState:CursorState?) {
        cursorState?.also {
            val req = DeleteMultiAtCursorRequest(
                wbKey = cursorState.id.wbKey,
                wsName = cursorState.id.wsName,
                windowId = null
            )
            wsAction.deleteMultiCellAtCursor(req)
        }
    }
    override fun onDeleteKey(wbwsSt: WbWsSt) {
        _onDeleteKey(sc.getCursorState(wbwsSt))
    }

    override fun onDeleteKey(wbws: WbWs) {
        _onDeleteKey(sc.getCursorState(wbws))
    }
}
