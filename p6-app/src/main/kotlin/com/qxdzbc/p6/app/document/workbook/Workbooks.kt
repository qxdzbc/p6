package com.qxdzbc.p6.app.document.workbook

import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.workbook.WorkbookImp.Companion.toShallowModel
import com.qxdzbc.p6.proto.DocProtos.WorkbookProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.common.compose.StateUtils.toMs
import com.google.protobuf.ByteString

object Workbooks {

    fun empty(name:String):Workbook{
        return WorkbookImp(keyMs = WorkbookKey(name).toMs())
    }
    fun empty(wbKey: WorkbookKey):Workbook{
        return WorkbookImp(keyMs = wbKey.toMs())
    }
    fun String?.isLegalWbName():Boolean{
        return !this.isNullOrEmpty()
    }
}
