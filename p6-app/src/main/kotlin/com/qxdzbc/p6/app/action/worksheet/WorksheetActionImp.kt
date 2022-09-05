package com.qxdzbc.p6.app.action.worksheet

import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardAction
import com.qxdzbc.p6.app.action.worksheet.action2.WorksheetAction2
import com.qxdzbc.p6.app.action.worksheet.delete_multi.DeleteMultiCellAction
import com.qxdzbc.p6.app.action.worksheet.release_focus.RestoreWindowFocusState
import javax.inject.Inject

class WorksheetActionImp @Inject constructor(
    private val worksheetAction2: WorksheetAction2,
    private val rangeToClipboardAction: RangeToClipboardAction,
    private val deleteMultiAct: DeleteMultiCellAction,
    private val restoreWindowFocusState: RestoreWindowFocusState,
) : WorksheetAction,
    RangeToClipboardAction by rangeToClipboardAction,
    WorksheetAction2 by worksheetAction2,
    DeleteMultiCellAction by deleteMultiAct,
    RestoreWindowFocusState by restoreWindowFocusState {
}
