package com.qxdzbc.p6.app.action.app.load_wb

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.path.PPath
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookImp.Companion.toShallowModel
import com.qxdzbc.p6.app.file.loader.P6FileLoader
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.proto.DocProtos.WorkbookProto

import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.app.state.TranslatorContainer
import com.qxdzbc.p6.ui.file.P6FileLoaderErrors
import com.qxdzbc.p6.ui.format2.CellFormatTable.Companion.toModel
import com.squareup.anvil.annotations.ContributesBinding
import java.util.*
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class LoadWorkbookActionImp @Inject constructor(
    val stateContMs: Ms<StateContainer>,
    val errorRouter: ErrorRouter,
    private val loader: P6FileLoader,
    val translatorContainerMs:Ms<TranslatorContainer>,
) : LoadWorkbookAction {

    private var sc by stateContMs
    private val tc by translatorContainerMs
    private var wbCont by sc.wbContMs
    private var wbStateCont by sc.wbStateContMs

    override fun loadWorkbook(request: LoadWorkbookRequest): LoadWorkbookResponse {
        val path: PPath = request.path
        val windowId: String? = request.windowId
        val windowStateMs = sc.getWindowStateMsDefaultRs(windowId)
        if (path.exists() && path.isRegularFile()) {
            if (path.isReadable()) {
                val res = loadWb(request)
                applyResponse(res.first,res.second)
                return res.first
            } else {
                val e = P6FileLoaderErrors.notReadableFile(path)
                if (windowStateMs is Ok) {
                    windowStateMs.value.value.publishError(e)
                } else {
                    errorRouter.publishToApp(e)
                }
                return LoadWorkbookResponse(
                    errorReport = e,
                    windowId = request.windowId,
                    wb = null
                )
            }
        } else {
            val e = P6FileLoaderErrors.notAFile(path)
            if (windowStateMs is Ok) {
                windowStateMs.value.value.publishError(e)
            } else {
                errorRouter.publishToApp(e)
            }
            return LoadWorkbookResponse(
                errorReport = e,
                windowId = request.windowId,
                wb = null
            )
        }
    }

    fun loadWb(request: LoadWorkbookRequest): Pair<LoadWorkbookResponse,WorkbookProto?> {
        val loadRs = loader.load3Rs(request.path.path)
        when (loadRs) {
            is Ok -> {
                val wb = loadRs.value.toShallowModel(tc::getTranslatorOrCreate)
                return LoadWorkbookResponse(
                    windowId = request.windowId,
                    errorReport = null,
                    wb = wb
                ) to loadRs.value
            }
            is Err -> {
                return LoadWorkbookResponse(
                    windowId = request.windowId,
                    errorReport = loadRs.error,
                    wb = null
                ) to null
            }
        }
    }

    fun applyResponse(res:LoadWorkbookResponse?,proto:WorkbookProto?) {
        if (res != null) {
            val err = res.errorReport
            if (err != null) {
                errorRouter.publishToWindow(err, res.windowId, res.wbKey)
            } else {
                apply(res.windowId, res.wb,proto)
            }
        }
    }

    fun apply(windowId: String?, workbook: Workbook?,proto:WorkbookProto?) {
        val windowStateMsRs =  sc.getWindowStateMsDefaultRs(windowId)
        workbook?.also { wb ->
            wbCont = wbCont.addOrOverWriteWb(wb)
            when(windowStateMsRs){
                is Ok ->{
                    val windowStateMs = windowStateMsRs.value
                    val wbk = wb.key
                    val wbkMs = wb.keyMs
                    wbStateCont.getWbStateMs(wbk)?.also {
                        it.value = it.value.setWindowId(windowId).setNeedSave(false)
                    }
                    windowStateMs.value = windowStateMs.value.addWbKey(wbkMs)
                }
                is Err ->{
                    // x: designated window does not exist and can't get a default window state => create a new window for the loaded workbook with the provided window id
                    val newWindowId = windowId ?: UUID.randomUUID().toString()
                    val (newStateCont, newOuterWindowStateMs) = sc.createNewWindowStateMs(newWindowId)
                    sc = newStateCont
                    val newWindowStateMs = newOuterWindowStateMs.value.innerWindowStateMs
                    wbStateCont.getWbStateMs(wb.key)?.also {
                        it.value = it.value.setWindowId(newWindowId).setNeedSave(false)
                        newWindowStateMs.value.activeWbPointer = newWindowStateMs.value.activeWbPointer.pointTo(it.value.wbKeyMs)
                    }
                    val s2 = newWindowStateMs.value.addWbKey(wb.keyMs)
                    newWindowStateMs.value = s2
                }
            }
            proto?.worksheetList?.forEach {wsProto->
                sc.getCellFormatTableMs(WbWs(wb.key,wsProto.name))?.also {
                    val cellFormatTableProto = wsProto.cellFormatTable.toModel()
                    it.value = cellFormatTableProto
                }
            }
        }
    }
}
