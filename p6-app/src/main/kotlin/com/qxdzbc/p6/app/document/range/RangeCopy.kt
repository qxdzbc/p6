package com.qxdzbc.p6.app.document.range

import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.common.table.ImmutableTableCR
import com.qxdzbc.p6.app.action.range.RangeIdDM.Companion.toModel
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.CellImp.Companion.toShallowModel
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.proto.RangeProtos.RangeCopyProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit

/**
 * contain a [RangeId] and a list of [Cell] in that range
 */
data class RangeCopy(
    val rangeId: RangeId,
    val cells: List<Cell>
) {
    companion object {
        fun RangeCopyProto.toModel(translator: P6Translator<ExUnit>): RangeCopy {
            return RangeCopy(
                rangeId = this.id.toModel(),
                cells = this.cellList.map { it.toShallowModel(translator) }
            )
        }

        fun fromProtoBytes(data: ByteArray, translator: P6Translator<ExUnit>): RangeCopy {
            return RangeCopyProto.newBuilder().mergeFrom(data).build().toModel(translator)
        }
    }

    val cellTable = ImmutableTableCR(cells.groupBy { it.address.colIndex }.map { (col, cellList) ->
        col to cellList.associateBy { it.address.rowIndex }
    }.toMap())

    fun toProto(): RangeCopyProto {
        return RangeCopyProto.newBuilder()
            .setId(this.rangeId.toProto())
            .addAllCell(this.cells.map { it.toProto() })
            .build()
    }

    /**
     * shift all the cell in this object using vector: topLeft of this range -> [newAnchorCell].
     * [rangeId] is preserved.
     */
    fun shiftCells(newAnchorCell:CellAddress): RangeCopy {
        val sourceTopLeft: CellAddress = this.rangeId.rangeAddress.topLeft
        val newCells=this.cells.map{
            it.shift(sourceTopLeft,newAnchorCell)
        }
        return this.copy(cells = newCells)
    }

}
