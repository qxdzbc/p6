package com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer

import com.qxdzbc.p6.app.document.worksheet.Worksheet
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt


interface ActiveWorksheetPointer {
    val wsNameSt:St<String>?
    val wsName:String?
    fun isValid():Boolean{
        return wsName!=null
    }
    fun isPointingTo(name:String): Boolean
    fun invalidate(): ActiveWorksheetPointer
    fun pointTo(anotherWorksheetName:St<String>?): ActiveWorksheetPointer
    fun pointTo(anotherWorksheet:Worksheet): ActiveWorksheetPointer
}
