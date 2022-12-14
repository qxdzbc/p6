package com.qxdzbc.p6.translator.formula.execution_unit

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.cell.address.CRAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

data class IntUnit(val v: Int) :ExUnit {
    companion object{
        fun Int.toExUnit(): IntUnit {
            return IntUnit(this)
        }
    }
    override fun runRs(): Result<Int, ErrorReport> {
        return Ok(this.v)
    }

    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): ExUnit {
        return this
    }

    override fun toFormula(): String {
        return this.v.toString()
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String {
        return v.toString()
    }
}

