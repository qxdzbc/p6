package com.qxdzbc.p6.app.action.app.save_wb

import com.qxdzbc.p6.app.communication.res_req_template.request.RequestWithWorkbookKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.p6.proto.AppProtos

data class SaveWorkbookRequest(
    override val wbKey:WorkbookKey,
    val path:String,
): RequestWithWorkbookKey {
    companion object{
        fun AppProtos.SaveWorkbookRequestProto.toModel():SaveWorkbookRequest{
            return SaveWorkbookRequest(
                wbKey = wbKey.toModel(),
                path = path
            )
        }
    }

//    override fun toProtoBytes(): ByteString {
//        return this.toProto().toByteString()
//    }
    fun toProto(): AppProtos.SaveWorkbookRequestProto {
        return AppProtos.SaveWorkbookRequestProto.newBuilder()
            .setWbKey(wbKey.toProto())
            .setPath(path)
            .build()
    }
}


