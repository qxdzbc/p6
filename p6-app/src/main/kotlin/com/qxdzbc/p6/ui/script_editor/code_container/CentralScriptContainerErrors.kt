package com.qxdzbc.p6.ui.script_editor.code_container

import com.qxdzbc.p6.app.document.worksheet.WorksheetErrors
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport

object CentralScriptContainerErrors {
    val prefix = "UI_CentralScriptContainerErrors_"

    object CantAddScript {
        val header = ErrorHeader(
            errorCode = "${prefix}0",
            errorDescription = "can't add app script to workbook"
        )

        fun report(detail: String?): ErrorReport {
            return (detail?.let {
                header.setDescription(it)
            } ?: header).toErrorReport()
        }
    }
}
