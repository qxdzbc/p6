package com.qxdzbc.p6.app.action.workbook.add_ws.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.common.utils.RseNav


import com.qxdzbc.p6.app.action.workbook.add_ws.AddWorksheetResponse
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CreateNewWorksheetApplierImp @Inject constructor(
    private val stateContMs:Ms<SubAppStateContainer>,
    private val wbContMs:Ms<WorkbookContainer>,
) : CreateNewWorksheetApplier {

    private var stateCont by stateContMs
    private var wbCont by wbContMs

    override fun applyRs(res: RseNav<AddWorksheetResponse>): RseNav<AddWorksheetResponse> {
        val rt = res.andThen { addRs ->
            wbCont =wbCont.overwriteWB(addRs.newWb)
            val wbMs=stateCont.getWbStateMs(addRs.newWb.key)
            if(wbMs!=null){
                wbMs.value = wbMs.value.refreshWsState()
            }
            Ok(addRs)
        }
        return rt
    }
}
