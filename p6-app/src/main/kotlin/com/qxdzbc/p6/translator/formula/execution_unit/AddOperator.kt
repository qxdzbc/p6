package com.qxdzbc.p6.translator.formula.execution_unit

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.flatMap
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.document.cell.address.CRAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.color_generator.ColorMap

/**
 * ExUnit for "+" operator
 */
data class AddOperator constructor(val u1: ExUnit, val u2: ExUnit,
//val errX:ExUnitErrors.IncompatibleType = ExUnitErrors.IncompatibleType.instance
) : ExUnit {
    override fun getCellRangeExUnit(): List<ExUnit> {
        return u1.getCellRangeExUnit() + u2.getCellRangeExUnit()
    }

    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): ExUnit {
        return this.copy(
            u1 = u1.shift(oldAnchorCell, newAnchorCell),
            u2 = u2.shift(oldAnchorCell, newAnchorCell)
        )
    }

    override fun getRanges(): List<RangeAddress> {
        return u1.getRanges() + u2.getRanges()
    }

    override fun getRangeIds(): List<RangeId> {
        return u1.getRangeIds() + u2.getRangeIds()
    }

    override fun toColorFormula(
        colorMap: ColorMap,
        wbKey: WorkbookKey?,
        wsName: String?
    ): AnnotatedString? {
        val f1 = u1.toColorFormula(colorMap, wbKey, wsName)
        val f2 = u2.toColorFormula(colorMap, wbKey, wsName)
        if (f1 != null && f2 != null) {
            return buildAnnotatedString {
                append(f1)
                append("+")
                append(f2)
            }
        } else {
            return null
        }
    }

    override fun toFormula(): String? {
        val f1 = u1.toFormula()
        val f2 = u2.toFormula()
        if (f1 != null && f2 != null) {
            return "${f1} + ${f2}"
        } else {
            return null
        }
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String? {
        val f1 = u1.toShortFormula(wbKey, wsName)
        val f2 = u2.toShortFormula(wbKey, wsName)
        if (f1 != null && f2 != null) {
            return "${f1} + ${f2}"
        } else {
            return null
        }
    }

    override fun runRs(): Result<Any, ErrorReport> {
            val r1Rs = u1.runRs()
            val rt: Result<Any, ErrorReport> = r1Rs.flatMap { r1 ->
                u2.runRs().flatMap { r2 ->
                    val trueR1 = r1?.let{ExUnits.extractFromCellOrNull(r1)}?:0
                    val trueR2 = r2?.let{ExUnits.extractFromCellOrNull(r2)}?:0
                    when {
                        trueR1 is Number && trueR2 is Number -> return Ok(trueR1.toDouble() + (trueR2.toDouble()))
                        trueR1 is String -> {
                            if (trueR2 is Number) {
                                val db = trueR2.toDouble()
                                val i = db.toInt()
                                val isInt = db.toInt().toDouble() == db
                                if (isInt) {
                                    return Ok(trueR1 + i.toString())
                                }
                            }
                            return Ok(trueR1 + trueR2.toString())
                        }
                        else -> {
                            val err= ExUnitErrors.IncompatibleType.report(
                                "Expect two numbers or first argument as text, but got ${trueR1::class.simpleName} and ${trueR2::class.simpleName}"
                            ).toErr()
//                            val q = _IncompatibleType()
//                            val err= q.report().toErr()

//                            val err= errX.report(
//                                "Expect two numbers or first argument as text, but got ${trueR1::class.simpleName} and ${trueR2::class.simpleName}"
//                            ).toErr()
                            return err
                        }
                    }
                }
            }
            return rt
    }
}

