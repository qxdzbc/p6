package com.qxdzbc.p6.translator.formula.execution_unit

import androidx.compose.runtime.getValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.document.cell.address.CRAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.formula.FunctionMap
import com.qxdzbc.p6.ui.common.color_generator.ColorMap

data class FuncUnit(
    override val funcName: String,
    override val args: List<ExUnit>,
    val functionMapSt: St<FunctionMap>,
) : ExUnit, BaseFunc() {

    override val functionMap by functionMapSt

    override fun getCellRangeExUnit(): List<ExUnit> {
        return args.flatMap { it.getCellRangeExUnit() }
    }

    override fun getRanges(): List<RangeAddress> {
        return args.flatMap { it.getRanges() }
    }
    override fun getRangeIds(): List<RangeId> {
        return args.flatMap { it.getRangeIds() }
    }

    override fun toColorFormula(
        colorMap: ColorMap,
        wbKey: WorkbookKey?,
        wsName: String?
    ): AnnotatedString {
        val u = this
        val argsStr = u.args.map { it.toColorFormula(colorMap,wbKey, wsName) }.filterNotNull()
        val rt= buildAnnotatedString {
            append(u.funcName)
            append("(")

            argsStr.withIndex().forEach {(i,a)->
                if(i!=0){
                    append(",")
                }
                append(a)
            }

            append(")")
        }
        return rt
    }

    override fun shift(
        oldAnchorCell: CRAddress<Int, Int>,
        newAnchorCell: CRAddress<Int, Int>
    ): ExUnit {
        val newArgs = args.map { it.shift(oldAnchorCell, newAnchorCell) }
        return this.copy(args = newArgs)
    }

    override fun toFormula(): String {
        val u = this
        val argsStr = u.args.map { it.toFormula() }.filterNotNull().joinToString(", ")
        return "${u.funcName}(${argsStr})"
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String {
        val u = this
        val argsStr = u.args.map { it.toShortFormula(wbKey, wsName) }.filterNotNull().joinToString(", ")
        return "${u.funcName}(${argsStr})"
    }
}

