package com.qxdzbc.p6.app.communication.event.data_structure.workbook_event

import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookResponse
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.document.workbook.toModel
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.proto.AppProtos
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SaveWorkbookResponseTest{
    @Test
    fun fromProtoBytes(){
        val proto = AppProtos.SaveWorkbookResponseProto.newBuilder()
            .setWbKey(WorkbookKey("B").toProto())
            .setPath("folder123/file.txt")
            .build()

        val o = SaveWorkbookResponse.fromProtoBytes(proto.toByteString())
        assertEquals(proto.wbKey.toModel(),o.wbKey)
        assertEquals(proto.path, o.path)
        assertNull(o.errorReport)
    }

    @Test
    fun `fromProtoBytes error case`(){
        val ee = ErrorReport(header= ErrorHeader("S","M"))

        val proto = AppProtos.SaveWorkbookResponseProto.newBuilder()
            .setWbKey(WorkbookKey("B").toProto())
            .setErrorReport(ee.toProto())
            .build()

        val o = SaveWorkbookResponse.fromProtoBytes(proto.toByteString())
        assertEquals(proto.wbKey.toModel(),o.wbKey)
        assertEquals(proto.path, o.path)
        assertTrue { o.errorReport?.isType(ee.header) ?: false}
    }
}
