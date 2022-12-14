package com.qxdzbc.p6.app.action.workbook.rename_ws


import androidx.compose.runtime.getValue
import com.github.michaelbull.result.*
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.action.cell.cell_update.CommonReactionWhenAppStatesChanged
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetRequest
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetResponse
import com.qxdzbc.p6.app.command.BaseCommand
import com.qxdzbc.p6.app.command.Command
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.SubAppStateContainer
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
data class RenameWorksheetActionImp @Inject constructor(
    val stateContMs: Ms<SubAppStateContainer>,
    val commonReactionWhenAppStatesChanged: CommonReactionWhenAppStatesChanged,
    val appStateMs: Ms<AppState>,
    val docContMs: Ms<DocumentContainer>,
    val errorRouter: ErrorRouter,
) : RenameWorksheetAction {

    private val sc by stateContMs
    private val dc by docContMs
    val appState by appStateMs

    override fun renameWorksheetRs(request: RenameWorksheetRequest, undoable: Boolean): Result<Unit, ErrorReport> {

        if (undoable) {
            makeCommand(request)?.also { command ->
                sc.getUndoStackMs(WbWs(request.wbKey, request.oldName))?.also {
                    it.value = it.value.add(command)
                }
            }
        }

        val o = makeRenameRequest(request)
        val applyRs = applyResRs(o)
        return applyRs
    }

    fun makeCommand(request: RenameWorksheetRequest): Command? {
        val wbStateMs = sc.getWbStateMs(request.wbKey)
        if (wbStateMs != null) {
            val command = object : BaseCommand() {
                val _oldName = request.oldName
                val _newName = request.newName
                val _wbKeySt = wbStateMs.value.wbKeyMs

                val _originalRequest
                    get() = RenameWorksheetRequest(
                        wbKey = _wbKeySt.value,
                        oldName = _oldName,
                        newName = _newName
                    )

                val _reversedReq
                    get() = RenameWorksheetRequest(
                        wbKey = _wbKeySt.value,
                        oldName = _newName,
                        newName = _oldName
                    )

                override fun run() {
                    val res = makeRenameRequest(_originalRequest)
                    applyResRs(res)
                }

                override fun undo() {
                    val res = makeRenameRequest(_reversedReq)
                    applyResRs(res)
                }
            }
            return command
        } else {
            return null
        }
    }

    fun makeRenameRequest(request: RenameWorksheetRequest): RenameWorksheetResponse {
        val req = request
        return RenameWorksheetResponse(
            wbKey = req.wbKey,
            oldName = req.oldName,
            newName = req.newName
        )
    }

    fun applyResRs(res: RenameWorksheetResponse?): Rse<Unit> {
        if (res != null) {
            val err = res.errorReport
            if (err != null) {
                errorRouter.publishToWindow(err, res.wbKey)
                return err.toErr()
            } else {
                apply(res.wbKey, res.oldName, res.newName)
                return Ok(Unit)
            }
        } else {
            return Ok(Unit)
        }
    }

    fun apply(wbKey: WorkbookKey, oldName: String, newName: String, publishError: Boolean = true) {
        sc.getWbStateMsRs(wbKey).onSuccess { wbStateMs ->
            val wbState by wbStateMs
            val wb = wbState.wb
            if (oldName != newName) {
                // x: rename the sheet in wb
                val renameRs = wb.renameWsRs(oldName, newName)
                renameRs.onSuccess { newWb ->
                    docContMs.value = dc.replaceWb(newWb)
                    commonReactionWhenAppStatesChanged.onWsChanged(WbWs(wbKey,newName))
                }.onFailure {
                    errorRouter.publishToWindow(renameRs.unwrapError(), wbStateMs.value.wbKey)
                }
            }
        }.onFailure {
            if (publishError) {
                errorRouter.publishToApp(it)
            }
        }
    }
}
