package com.qxdzbc.p6.app.action.workbook.add_ws

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.p6.app.document.worksheet.WorksheetImp.Companion.toShallowModel
import com.qxdzbc.p6.proto.WorkbookProtos
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

data class AddWorksheetRequest(
    val wbKey: WorkbookKey,
    val worksheet: Worksheet,
    val windowId: String? = null
) {
    companion object {
        fun WorkbookProtos.AddWorksheetRequestProto.toModel(
            wbKeyMs: Ms<WorkbookKey>,
            translator: P6Translator<ExUnit>
        ): AddWorksheetRequest {
            val wbk = this.wbKey.toModel()
            return AddWorksheetRequest(
                wbKey = wbk,
                worksheet = this.worksheet.toShallowModel(wbKeyMs, translator)
            )
        }
    }
}
