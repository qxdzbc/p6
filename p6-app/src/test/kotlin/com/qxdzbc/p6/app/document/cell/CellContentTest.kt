package com.qxdzbc.p6.app.document.cell

import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.document.cell.CellContentImp
import com.qxdzbc.p6.app.document.cell.CellValue
import com.qxdzbc.p6.translator.formula.execution_unit.BoolUnit.Companion.FALSE
import kotlin.test.*

class CellContentTest {

    @Test
    fun run() {
        val c = CellContentImp(
            cellValueMs = CellValue.from(123).toMs(),
            exUnit = FALSE,
            originalText ="=FALSE"
        )
        val c2 = c.reRunRs().component1()
        assertEquals(CellValue.from(false), c2?.reRunRs()?.component1()?.cellValueAfterRun)
    }
}
