package com.qxdzbc.p6.app.action.workbook

import com.qxdzbc.p6.app.action.workbook.click_on_ws_tab_item.SwitchWorksheetAction
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.DeleteWorksheetAction
import com.qxdzbc.p6.app.action.workbook.new_worksheet.NewWorksheetAction
import com.qxdzbc.p6.app.action.workbook.rename_ws.RenameWorksheetAction
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetAction
import com.qxdzbc.p6.app.action.worksheet.release_focus.RestoreWindowFocusState
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class,boundType=WorkbookAction::class)
class WorkbookActionImp @Inject constructor(
    private val newWsAction:NewWorksheetAction,
    private val delWsAct: DeleteWorksheetAction,
    private val renameWsAct: RenameWorksheetAction,
    private val setActiveWsAct:SetActiveWorksheetAction,
    private val restoreWindowFocusState: RestoreWindowFocusState,
    private val switchWorksheetAction: SwitchWorksheetAction,
) : WorkbookAction,
    NewWorksheetAction by newWsAction,
    DeleteWorksheetAction by delWsAct,
    RenameWorksheetAction by renameWsAct,
    SetActiveWorksheetAction by setActiveWsAct,
    RestoreWindowFocusState by restoreWindowFocusState,
    SwitchWorksheetAction by switchWorksheetAction

