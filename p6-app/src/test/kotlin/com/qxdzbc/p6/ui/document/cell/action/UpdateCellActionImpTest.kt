package com.qxdzbc.p6.ui.document.cell.action

import androidx.compose.runtime.getValue
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequestDM
import com.qxdzbc.p6.app.action.cell.cell_update.UpdateCellAction
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.document.cell.Cell
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.rpc.cell.msg.CellContentDM
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import test.TestSample
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class UpdateCellActionImpTest {
    lateinit var ts: TestSample
    lateinit var act: UpdateCellAction
    val sc get()=ts.sc

    @BeforeTest
    fun b() {
        ts = TestSample()
        act = ts.p6Comp.updateCellAction()
    }

    @Test
    fun updateCell2() {
        // x: precondition
        val wbk = ts.wbKey1
        val wsn = ts.wsn1
        val sc by ts.stateContMs
        val ca = CellAddress("K2")
        fun gc() = sc.getCell(wbk, wsn, ca)
        val c = gc()
        assertNotNull(c)
        assertEquals(null, c.currentValue)
        act.updateCellDM(
            CellUpdateRequestDM(
                cellId = CellIdDM(
                    wbKey = wbk, wsName = wsn, address = ca
                ),
                cellContent = CellContentDM(
                    cellValue = CellValue.Companion.from(123)
                )
            )
        )
        val c2 = gc()
        assertNotNull(c2)
        assertEquals(123.0, c2.currentValue)
    }

    @Test
    fun `updateCell circular reference`(){
        val updateCellAct = ts.p6Comp.updateCellAction()
        val wbws = WbWs(ts.wbKey1,ts.wsn1)
        updateCellAct.updateCellDM(
            request= CellUpdateRequestDM(
                cellId = CellIdDM(CellAddress("A1"),wbws),
                cellContent = CellContentDM.fromFormula("=B1")
            ),
            publishError = false
        )
        updateCellAct.updateCellDM(
            request= CellUpdateRequestDM(
                cellId = CellIdDM(CellAddress("B1"),wbws),
                cellContent = CellContentDM.fromFormula("=A1")
            ),
            publishError = false
        )
        val b1 = sc.getCell(wbws, CellAddress("B1"))!!
        val a1 = sc.getCell(wbws, CellAddress("A1"))!!
        val c1 = (a1.currentValue as Cell).content.cellValue
        val c2 = b1.content.cellValue
        assertEquals(c1,c2)
    }
}


