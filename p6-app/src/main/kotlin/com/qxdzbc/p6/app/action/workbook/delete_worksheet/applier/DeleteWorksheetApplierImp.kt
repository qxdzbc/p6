package com.qxdzbc.p6.app.action.workbook.delete_worksheet.applier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.flatMap
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms

import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class DeleteWorksheetApplierImp @Inject constructor(
    private val docContMs: Ms<DocumentContainer>,
    val stateContMs: Ms<SubAppStateContainer>,
    val transContMs:Ms<TranslatorContainer>,
) : DeleteWorksheetApplier {

    private var appState by docContMs
    private var sc by stateContMs
    private var tc by transContMs

    override fun applyResRs(deletedWsName:String,rs: Rse<Workbook>): Rse<Unit> {
        return rs.flatMap { newWB->
            // x: update wb
            appState = appState.replaceWb(newWB)
            // x: update wb state
            val wbKey = newWB.key
            val wbStateMs = sc.getWbStateMs(wbKey)
            if(wbStateMs!=null){
                val newWbState=wbStateMs.value.refresh().setNeedSave(true)
                wbStateMs.value = newWbState
            }
            //update translator map
            tc = tc.removeTranslator(wbKey,deletedWsName)
            Unit.toOk()
        }
    }
}
