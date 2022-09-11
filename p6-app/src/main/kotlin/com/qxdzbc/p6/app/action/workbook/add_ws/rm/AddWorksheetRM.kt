package com.qxdzbc.p6.app.action.workbook.add_ws.rm

import com.qxdzbc.p6.app.common.utils.RseNav
import com.qxdzbc.p6.app.action.workbook.add_ws.AddWorksheetRequest
import com.qxdzbc.p6.app.action.workbook.add_ws.AddWorksheetResponse

interface AddWorksheetRM {
    fun makeAddWsRequest(req: AddWorksheetRequest): RseNav<AddWorksheetResponse>
}
