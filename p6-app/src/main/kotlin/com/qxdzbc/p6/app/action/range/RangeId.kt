package com.qxdzbc.p6.app.action.range

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.proto.DocProtos

interface RangeId: WbWs {
    val rangeAddress: RangeAddress
    fun toProto(): DocProtos.RangeIdProto
    fun toDm():RangeId
}
