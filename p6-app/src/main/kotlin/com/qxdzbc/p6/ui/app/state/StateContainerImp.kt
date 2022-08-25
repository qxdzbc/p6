package com.qxdzbc.p6.ui.app.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.common.Rs
import com.qxdzbc.common.Rse
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.d.Cell
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.di.state.app_state.DocumentContainerMs
import com.qxdzbc.p6.di.state.app_state.SubAppStateContainerMs
import com.qxdzbc.p6.ui.app.cell_editor.in_cell.state.CellEditorState
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.qxdzbc.p6.ui.script_editor.code_container.CentralScriptContainer
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState
import com.qxdzbc.p6.ui.window.state.WindowState
import com.github.michaelbull.result.Result
import java.nio.file.Path
import javax.inject.Inject

class StateContainerImp @Inject constructor(
    @AppStateMs
    override val appStateMs: Ms<AppState>,
    @DocumentContainerMs
    val docContMs:Ms<DocumentContainer>,
    @SubAppStateContainerMs
    val subAppStateContMs: Ms<SubAppStateContainer>
) : StateContainer, AbsSubAppStateContainer() {

    private var subAppStateCont by subAppStateContMs

    override var appState by appStateMs
    override val centralScriptContainerMs: Ms<CentralScriptContainer>
        get() = appState.centralScriptContainerMs
    override var centralScriptContainer: CentralScriptContainer by centralScriptContainerMs

    private val docCont by docContMs
    override val globalWbContMs: Ms<WorkbookContainer>
        get() = docCont.globalWbContMs
    override var globalWbCont: WorkbookContainer by globalWbContMs

    override val cellEditorStateMs: Ms<CellEditorState>
        get() = appState.cellEditorStateMs
    override var cellEditorState: CellEditorState by cellEditorStateMs

    override val windowStateMsListMs: Ms<List<Ms<WindowState>>>
        get() = subAppStateCont.windowStateMsListMs
    override var windowStateMsList: List<MutableState<WindowState>> by windowStateMsListMs

    override val globalWbStateContMs: Ms<WorkbookStateContainer>
        get() = subAppStateCont.globalWbStateContMs
    override var globalWbStateCont: WorkbookStateContainer by globalWbStateContMs

    override fun getStateByWorkbookKeyRs(workbookKey: WorkbookKey): Rse<QueryByWorkbookKeyResult2> {
        return subAppStateCont.getStateByWorkbookKeyRs(workbookKey)
    }

    override fun addWbStateFor(wb: Workbook): StateContainer {
        subAppStateCont = subAppStateCont.addWbStateFor(wb)
        return this
    }

    override fun removeWindowState(windowState: Ms<WindowState>): StateContainer {
        subAppStateCont = subAppStateCont.removeWindowState(windowState)
        return this
    }

    override fun removeWindowState(windowId: String): StateContainer {
        subAppStateCont= subAppStateCont.removeWindowState(windowId)
        return this
    }

    override fun addWindowState(windowState: Ms<WindowState>): StateContainer {
        subAppStateCont= subAppStateCont.addWindowState(windowState)
        return this
    }

    override fun createNewWindowStateMs(): Pair<StateContainer, Ms<WindowState>> {
         val o=subAppStateCont.createNewWindowStateMs()
        subAppStateCont= o.first
        return this to o.second
    }

    override fun createNewWindowStateMs(windowId: String): Pair<StateContainer, Ms<WindowState>> {
        val o = subAppStateCont.createNewWindowStateMs(windowId)
        subAppStateCont = o.first
        return this to o.second
    }

    override fun getWbStateMsRs(wbKeySt: St<WorkbookKey>): Rse<Ms<WorkbookState>> {
        return subAppStateCont.getWbStateMsRs(wbKeySt)
    }

    override fun getWbStateMsRs(wbKey: WorkbookKey): Rse<Ms<WorkbookState>> {
        return subAppStateCont.getWbStateMsRs(wbKey)
    }

    override fun getWsStateMsRs(wbKey: WorkbookKey, wsName: String): Rse<Ms<WorksheetState>> {
        return subAppStateCont.getWsStateMsRs(wbKey, wsName)
    }

    override fun getWindowStateMsByWbKeyRs(wbKey: WorkbookKey): Result<Ms<WindowState>, ErrorReport> {
        return subAppStateCont.getWindowStateMsByWbKeyRs(wbKey)
    }

    override fun getFocusStateMsByWbKeyRs(wbKey: WorkbookKey): Rs<Ms<WindowFocusState>, ErrorReport> {
        return subAppStateCont.getFocusStateMsByWbKeyRs(wbKey)
    }

    override fun getWindowStateMsByIdRs(windowId: String): Rs<Ms<WindowState>, ErrorReport> {
        return subAppStateCont.getWindowStateMsByIdRs(windowId)
    }

    override fun getCursorStateMs(wbKey: WorkbookKey, wsName: String): Ms<CursorState>? {
        return subAppStateCont.getCursorStateMs(wbKey, wsName)
    }

    override fun getWbWsSt(wbKey: WorkbookKey, wsName: String): WbWsSt? {
        return docCont.getWbWsSt(wbKey, wsName)
    }

    override fun getWbWsSt(wbWs: WbWs): WbWsSt? {
        return docCont.getWbWsSt(wbWs)
    }

    override fun getWbKeySt(wbKey: WorkbookKey): St<WorkbookKey>? {
        return docCont.getWbKeySt(wbKey)
    }

    override fun getWsNameSt(wbKey: WorkbookKey, wsName: String): St<String>? {
        return docCont.getWsNameSt(wbKey, wsName)
    }

    override fun getWsNameSt(wbKeySt: St<WorkbookKey>, wsName: String): St<String>? {
        return docCont.getWsNameSt(wbKeySt, wsName)
    }

    override fun getWbRs(wbKey: WorkbookKey): Rs<Workbook, ErrorReport> {
        return docCont.getWbRs(wbKey)
    }

    override fun getWbRs(path: Path): Result<Workbook, ErrorReport> {
        return docCont.getWbRs(path)
    }

    override fun getWbMsRs(wbKeySt: St<WorkbookKey>): Result<Ms<Workbook>, ErrorReport> {
        return docCont.getWbMsRs(wbKeySt)
    }

    override fun getWbMsRs(wbKey: WorkbookKey): Result<Ms<Workbook>, ErrorReport> {
        return docCont.getWbMsRs(wbKey)
    }

    override fun getWbMsRs(path: Path): Result<Ms<Workbook>, ErrorReport> {
        return docCont.getWbMsRs(path)
    }

    override fun getWb(wbKey: WorkbookKey): Workbook? {
        return docCont.getWb(wbKey)
    }

    override fun getWb(path: Path): Workbook? {
        return docCont.getWb(path)
    }

    override fun getWbMs(wbKeySt: St<WorkbookKey>): Ms<Workbook>? {
        return docCont.getWbMs(wbKeySt)
    }

    override fun getWbMs(wbKey: WorkbookKey): Ms<Workbook>? {
        return docCont.getWbMs(wbKey)
    }

    override fun getWbRs(wbKeySt: St<WorkbookKey>): Result<Workbook, ErrorReport> {
        return docCont.getWbRs(wbKeySt)
    }

    override fun getWsRs(wbKey: WorkbookKey, wsName: String): Rs<Worksheet, ErrorReport> {
        return docCont.getWsRs(wbKey, wsName)
    }

    override fun getWsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rs<Worksheet, ErrorReport> {
        return docCont.getWsRs(wbKeySt, wsNameSt)
    }

    override fun getWsRs(wbwsSt: WbWsSt): Rs<Worksheet, ErrorReport> {
        return docCont.getWsRs(wbwsSt)
    }

    override fun getWs(wbKey: WorkbookKey, wsName: String): Worksheet? {
        return docCont.getWs(wbKey, wsName)
    }

    override fun getWs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Worksheet? {
        return docCont.getWs(wbKeySt, wsNameSt)
    }

    override fun getWs(wbws: WbWs): Worksheet? {
        return docCont.getWs(wbws)
    }

    override fun getWs(wbwsSt: WbWsSt): Worksheet? {
        return docCont.getWs(wbwsSt)
    }

    override fun getWsMsRs(wbKey: WorkbookKey, wsName: String): Rs<Ms<Worksheet>, ErrorReport> {
        return docCont.getWsMsRs(wbKey, wsName)
    }

    override fun getWsMsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rs<Ms<Worksheet>, ErrorReport> {
        return docCont.getWsMsRs(wbKeySt, wsNameSt)
    }

    override fun getWsMsRs(wbwsSt: WbWsSt): Rs<Ms<Worksheet>, ErrorReport> {
        return docCont.getWsMsRs(wbwsSt)
    }

    override fun getWsMs(wbKey: WorkbookKey, wsName: String): Ms<Worksheet>? {
        return docCont.getWsMs(wbKey, wsName)
    }

    override fun getWsMs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Ms<Worksheet>? {
        return docCont.getWsMs(wbKeySt, wsNameSt)
    }

    override fun getWsMs(wbws: WbWs): Ms<Worksheet>? {
        return docCont.getWsMs(wbws)
    }

    override fun getWsMs(wbwsSt: WbWsSt): Ms<Worksheet>? {
        return docCont.getWsMs(wbwsSt)
    }

    override fun getRangeRs(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Rs<Range, ErrorReport> {
        return docCont.getRangeRs(wbKey, wsName, rangeAddress)
    }

    override fun getRangeRs(rangeId: RangeId): Rs<Range, ErrorReport> {
        return docCont.getRangeRs(rangeId)
    }

    override fun getRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range? {
        return docCont.getRange(wbKey, wsName, rangeAddress)
    }

    override fun getLazyRange(wbKey: WorkbookKey, wsName: String, rangeAddress: RangeAddress): Range? {
        return docCont.getLazyRange(wbKey, wsName, rangeAddress)
    }

    override fun getLazyRangeRs(
        wbKey: WorkbookKey,
        wsName: String,
        rangeAddress: RangeAddress
    ): Rs<Range, ErrorReport> {
        return docCont.getLazyRangeRs(wbKey, wsName, rangeAddress)
    }

    override fun getLazyRangeRs(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        rangeAddress: RangeAddress
    ): Rs<Range, ErrorReport> {
        return docCont.getLazyRangeRs(wbKeySt, wsNameSt, rangeAddress)
    }

    override fun getCellRs(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Rs<Cell, ErrorReport> {
        return docCont.getCellRs(wbKey, wsName, cellAddress)
    }

    override fun getCellRs(
        wbKeySt: St<WorkbookKey>,
        wsNameSt: St<String>,
        cellAddress: CellAddress
    ): Rs<Cell, ErrorReport> {
        return docCont.getCellRs(wbKeySt, wsNameSt, cellAddress)
    }

    override fun getCell(wbKey: WorkbookKey, wsName: String, cellAddress: CellAddress): Cell? {
        return docCont.getCell(wbKey, wsName, cellAddress)
    }

    override fun replaceWb(newWb: Workbook): DocumentContainer {
        return docCont.replaceWb(newWb)
    }

    override fun getWb(wbKeySt: St<WorkbookKey>): Workbook? {
        return docCont.getWb(wbKeySt)
    }
}
