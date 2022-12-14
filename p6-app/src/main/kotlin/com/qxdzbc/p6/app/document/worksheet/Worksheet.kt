package com.qxdzbc.p6.app.document.worksheet

import com.github.michaelbull.result.Result
import com.qxdzbc.common.Rse
import com.qxdzbc.common.WithSize
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.common.table.ImmutableTableCR
import com.qxdzbc.p6.app.common.table.TableCR
import com.qxdzbc.p6.app.common.table.TableCRColumn
import com.qxdzbc.p6.app.common.table.TableCRRow
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.CellContent
import com.qxdzbc.p6.app.document.range.Range
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.proto.DocProtos.WorksheetProto
import com.qxdzbc.p6.translator.P6Translator
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.ui.document.worksheet.state.RangeConstraint
import com.qxdzbc.p6.ui.document.worksheet.state.WorksheetId
import java.util.UUID

interface Worksheet : WithSize, WbWsSt {
    /**
     * re-run all the cells but does not update the display text
     */
    fun reRun(): Worksheet

    fun reRunAndRefreshDisplayText():Worksheet

    val idMs: Ms<WorksheetId>
    var id: WorksheetId

    val nameMs: Ms<String>
    val name: String

    fun setWbKeySt(wbKeySt: St<WorkbookKey>): Worksheet

    val usedRange: RangeAddress

    val table: TableCR<Int, Int, Ms<Cell>>
    val cells: List<Cell> get() = cellMsList.map { it.value }
    val cellMsList: List<Ms<Cell>>
    val rangeConstraint: RangeConstraint
    val rowRange:IntRange
    val colRange:IntRange
    override val size: Int get() = table.itemCount

    fun toProto(): WorksheetProto

    /**
     * return a range derived from this worksheet
     */
    fun range(address: RangeAddress): Result<Range, ErrorReport>

    fun updateCellValue(cellAddress: CellAddress, value: Any?): Result<Worksheet, ErrorReport>
    fun updateCellContentRs(cellAddress: CellAddress, cellContent: CellContent): Result<Worksheet, ErrorReport>

    fun getCellsInRange(rangeAddress: RangeAddress): List<Cell>

    fun getCellMsInRange(rangeAddress: RangeAddress): List<Ms<Cell>>

    fun getCellMs(cellAddress: CellAddress): Ms<Cell>?

    fun getCellMs(colIndex: Int, rowIndex: Int): Ms<Cell>?

    fun getCellMs(label: String): Ms<Cell>?

    /**
     * @return error when the requested address does not exist.
     * @return a cell state ms if it exists
     */
    fun getCellMsRs(cellAddress: CellAddress): Rse<Ms<Cell>>

    fun getCell(cellAddress: CellAddress): Cell?

    fun getCell(colIndex: Int, rowIndex: Int): Cell?
    fun getCell(label: String): Cell?

    /**
     * @return a cell if it exists or create an empty cell if the input address is legal
     * @return an error is the input address is illegal (out of the bound limit of this worksheet)
     */
    fun getCellOrDefaultRs(cellAddress: CellAddress): Result<Cell, ErrorReport>

    fun addOrOverwrite(cell: Cell): Worksheet

    fun getColMs(colIndex: Int): List<Ms<Cell>>
    fun getRowMs(rowIndex: Int): List<Ms<Cell>>

    val allColumns:List<TableCRColumn<Int,Cell>>
    val allRows:List<TableCRRow<Int,Cell>>

    fun getCol(colIndex: Int): List<Cell>

    fun getRow(rowIndex: Int): List<Cell>

    fun removeCol(colIndex: Int): Worksheet
    fun removeRow(rowIndex: Int): Worksheet

    fun removeCell(colKey: Int, rowKey: Int): Worksheet
    fun removeCell(cellAddress: CellAddress): Worksheet
    fun removeCell(label:String): Worksheet

    fun removeAllCell(): Worksheet

    fun removeCells(cells: Collection<CellAddress>): Worksheet
    fun setWsName(newName: String): Worksheet
    fun withNewData(wsProto: WorksheetProto, translator: P6Translator<ExUnit>): Worksheet

    fun refreshDisplayText():Worksheet

    companion object {
        fun random():Worksheet{
            val rt= WorksheetImp(
                nameMs= ms("Worksheet-"+UUID.randomUUID().toString()),
                wbKeySt = ms(WorkbookKey.random()),
                table = run {
                    var tb= ImmutableTableCR<Int,Int,Ms<Cell>>()
                    for(c in 1 .. 10){
                        for (r in 1 .. 10){
                           tb = tb.set(c,r, ms(Cell.random(CellAddress(c,r))))
                        }
                    }
                    tb
                }
            )
            return rt
        }

    }
}
