package com.emeraldblast.p6.app.action.workbook.set_active_ws

import com.emeraldblast.p6.app.action.common_data_structure.WithWbWs
import com.emeraldblast.p6.app.communication.res_req_template.request.remote.RequestToP6WithWorkbookKey
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.app.document.workbook.toModel
import com.emeraldblast.p6.proto.AppEventProtos
import com.google.protobuf.ByteString

class SetActiveWorksheetWithIndexRequest(
    val wbKey: WorkbookKey,
    val wsIndex: Int,
)

class SetActiveWorksheetRequest(
    override val wbKey: WorkbookKey,
    override val wsName: String,
) : RequestToP6WithWorkbookKey,WithWbWs {
    override fun toProtoBytes(): ByteString {
        return this.toProto().toByteString()
    }
    companion object{
        fun AppEventProtos.SetActiveWorksheetRequestProto.toModel():SetActiveWorksheetRequest{
            return SetActiveWorksheetRequest(
                wbKey = workbookKey.toModel(),
                wsName = worksheetName
            )
        }

    }
    fun toProto(): AppEventProtos.SetActiveWorksheetRequestProto {
        val rt = AppEventProtos.SetActiveWorksheetRequestProto.newBuilder()
            .setWorkbookKey(wbKey.toProto())
            .setWorksheetName(wsName)
            .build()
        return rt
    }
}

