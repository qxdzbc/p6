package com.qxdzbc.p6.ui.app.state

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.common.utils.Rs
import com.qxdzbc.p6.app.common.utils.Rse
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.common.compose.St
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.map

abstract class AbsSubAppStateContainer : SubAppStateContainer {

    override fun getWsStateMsRs(wbwsSt: WbWsSt): Rse<Ms<WorksheetState>> {
        return this.getWsStateMsRs(wbwsSt.wbKeySt,wbwsSt.wsNameSt)
    }

    override fun getWsStateRs(wbwsSt: WbWsSt): Rse<WorksheetState> {
        return getWsStateMsRs(wbwsSt).map { it.value }
    }

    override fun getWsStateMs(wbwsSt: WbWsSt): Ms<WorksheetState>? {
        return getWsStateMsRs(wbwsSt).component1()
    }

    override fun getWsState(wbwsSt: WbWsSt): WorksheetState? {
        return getWsStateMs(wbwsSt)?.value
    }

    override fun getWsStateMsRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rse<Ms<WorksheetState>> {
        return this.getWbStateRs(wbKeySt).flatMap {
            it.getWsStateMsRs(wsNameSt)
        }
    }

    override fun getWsStateRs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Rse<WorksheetState> {
        return getWsStateMsRs(wbKeySt, wsNameSt).map { it.value }
    }

    override fun getWsStateMs(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): Ms<WorksheetState>? {
        return getWsStateMsRs(wbKeySt, wsNameSt).component1()
    }

    override fun getWsState(wbKeySt: St<WorkbookKey>, wsNameSt: St<String>): WorksheetState? {
        return getWsStateMs(wbKeySt, wsNameSt)?.value
    }

    override fun getCursorStateMs(wbKey: WorkbookKey, wsName: String): Ms<CursorState>? {
        return this.getWsStateMs(wbKey, wsName)?.value?.cursorStateMs
    }

    override fun getFocusStateMsByWbKeyRs(wbKey: WorkbookKey): Rs<Ms<WindowFocusState>, ErrorReport> {
        return this.getWindowStateMsByWbKeyRs(wbKey).map {
            it.value.focusStateMs
        }
    }

    override fun getWsStateMsRs(wbKey: WorkbookKey, wsName: String): Rse<Ms<WorksheetState>> {
        return this.getWbStateRs(wbKey).flatMap { it.getWsStateMsRs(wsName) }
    }

    override fun getWbStateMs(wbKeySt: St<WorkbookKey>): Ms<WorkbookState>? {
        return this.getWbStateMsRs(wbKeySt).component1()
    }

    override fun getWbStateRs(wbKeySt: St<WorkbookKey>): Rse<WorkbookState> {
        return getWbStateMsRs(wbKeySt).map { it.value }
    }

    override fun getWbState(wbKeySt: St<WorkbookKey>): WorkbookState? {
        return getWbStateRs(wbKeySt).component1()
    }

    override fun getWbStateRs(wbKey: WorkbookKey): Rse<WorkbookState> {
        return getWbStateMsRs(wbKey).map { it.value }
    }

    override fun getWbStateMs(wbKey: WorkbookKey): Ms<WorkbookState>? {
        return getWbStateMsRs(wbKey).component1()
    }

    override fun getWbState(wbKey: WorkbookKey): WorkbookState? {
        return getWbStateMs(wbKey)?.value
    }

    override fun getWsStateRs(wbKey: WorkbookKey, wsName: String): Rse<WorksheetState> {
        return getWsStateMsRs(wbKey, wsName).map { it.value }
    }

    override fun getWsStateMs(wbKey: WorkbookKey, wsName: String): Ms<WorksheetState>? {
        return getWsStateMsRs(wbKey, wsName).component1()
    }

    override fun getWsState(wbKey: WorkbookKey, wsName: String): WorksheetState? {
        return getWsStateMs(wbKey, wsName)?.value
    }

    override fun getWsStateMsRs(wbws: WbWs): Rse<Ms<WorksheetState>> {
        return this.getWsStateMsRs(wbws.wbKey, wbws.wsName)
    }

    override fun getWsStateRs(wbws: WbWs): Rse<WorksheetState> {
        return getWsStateMsRs(wbws.wbKey, wbws.wsName).map { it.value }
    }

    override fun getWsStateMs(wbws: WbWs): Ms<WorksheetState>? {
        return getWsStateMsRs(wbws.wbKey, wbws.wsName).component1()
    }

    override fun getWsState(wbws: WbWs): WorksheetState? {
        return getWsStateMs(wbws.wbKey, wbws.wsName)?.value
    }

    override fun getCursorState(wbKey: WorkbookKey, wsName: String): CursorState? {
        return getCursorStateMs(wbKey, wsName)?.value
    }

    override fun getCursorStateMs(wbws: WbWs): Ms<CursorState>? {
        return this.getCursorStateMs(wbws.wbKey, wbws.wsName)
    }

    override fun getCursorState(wbws: WbWs): CursorState? {
        return this.getCursorStateMs(wbws.wbKey, wbws.wsName)?.value
    }

    override fun getActiveCursorMs(wbKey: WorkbookKey): Ms<CursorState>? {
        val rt = this.getWbState(wbKey)?.let { wbState ->
            wbState.activeSheetState?.let { activeWsState ->
                activeWsState.cursorStateMs
            }
        }
        return rt
    }
}