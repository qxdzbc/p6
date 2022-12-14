package com.qxdzbc.p6.app.action.workbook.set_active_ws

import com.qxdzbc.p6.app.common.proto.ProtoUtils.toModel
import com.qxdzbc.p6.app.communication.res_req_template.response.ResponseWith_WbKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.common.error.ErrorReport
import com.google.protobuf.ByteString
import com.qxdzbc.p6.proto.AppProtos

class SetActiveWorksheetResponse(
    override val wbKey: WorkbookKey,
    val wsName: String,
    override val isError:Boolean = false,
    override val errorReport: ErrorReport? = null
) :ResponseWith_WbKey{
    companion object{
        fun fromProtoBytes(bytes:ByteString):SetActiveWorksheetResponse{
            return AppProtos.SetActiveWorksheetResponseProto.newBuilder().mergeFrom(bytes).build().toModel()
        }
        fun AppProtos.SetActiveWorksheetResponseProto.toModel():SetActiveWorksheetResponse{
            return SetActiveWorksheetResponse(
                wbKey=workbookKey.toModel(),
                wsName = worksheetName,
                isError=isError,
                errorReport = if(this.hasErrorReport()){
                    this.errorReport.toModel()
                }else{
                    null
                }
            )
        }
    }
}


