package com.qxdzbc.p6.translator.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.cell.address.CRAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

object NothingUnit : ExUnit {
    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): ExUnit {
        return this
    }

    override fun toFormula(): String? {
        return null
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String? {
        return null
    }

    override fun runRs(): Result<Unit, ErrorReport> {
        return Ok(Unit)
    }
}
