package com.qxdzbc.p6.app.action.workbook.set_active_ws.applier

import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetResponse2
import com.qxdzbc.p6.app.common.utils.RseNav

interface SetActiveWorksheetApplier {
    fun apply(res: RseNav<SetActiveWorksheetResponse2>): RseNav<SetActiveWorksheetResponse2>
}

