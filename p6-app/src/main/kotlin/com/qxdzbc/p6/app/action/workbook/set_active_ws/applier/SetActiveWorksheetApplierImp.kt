package com.qxdzbc.p6.app.action.workbook.set_active_ws.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetResponse2
import com.qxdzbc.p6.app.common.utils.RseNav

import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.ActiveWindowPointer
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class SetActiveWorksheetApplierImp @Inject constructor(
    private val errorRouter: ErrorRouter,
    private val appStateMs: Ms<SubAppStateContainer>,
    private val activeWindowPointerMs:Ms<ActiveWindowPointer>,
) : SetActiveWorksheetApplier {

    private var appState by appStateMs
    private var activeWindowPointer by activeWindowPointerMs

    override fun apply(res: RseNav<SetActiveWorksheetResponse2>): RseNav<SetActiveWorksheetResponse2> {
        res.onSuccess {rs:SetActiveWorksheetResponse2->
            val k = rs.request.wbKey
            rs.newActiveWindowPointer?.also {
                activeWindowPointer = it
            }

            rs.newActiveWbPointer?.also {
                val wdStateMs = appState.getWindowStateMsByWbKey(k)
                if(wdStateMs!=null){
                    wdStateMs.value.activeWbPointerMs.value = it
                }
            }

            rs.newWbState?.also {
                val wbStateMs = appState.getWbStateMs(k)
                if(wbStateMs!=null){
                    wbStateMs.value = it

//                    wbStateMs.value.getWorksheetState(rs.request.wsName)?.also {
//                        it.cursorStateMs.value = it.cursorState.focus()
//                    }
//                    appState.getFocusStateMsByWbKey(rs.newWbState.wbKey)?.also {
//                        it.value = it.value.focusOnCursor()
//                    }
                }
            }



        }.onFailure {
            errorRouter.publish(it)
        }
        return res
    }
}
