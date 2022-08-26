package com.qxdzbc.p6.app.document.cell.d

import com.qxdzbc.common.CanCheckEmpty
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

interface CellContent:CanCheckEmpty {

    val cellValueMs:Ms<CellValue>
    val cellValueAfterRun: CellValue
    val currentCellValue: CellValue

    val formula: String?
    fun formula(wbKey:WorkbookKey?=null, wsName:String?=null): String?

    fun reRun():CellContent?
    val editableContent: String
    val displayValue: String
    fun setValue(cv: CellValue): CellContent
    val isFormula: Boolean
}

