package com.qxdzbc.p6.translator.formula.execution_unit

import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.action.range.RangeIdImp
import com.qxdzbc.p6.app.document.cell.address.GenericCellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.formula.FunctionMap
import com.qxdzbc.p6.translator.formula.function_def.P6FunctionDefinitions
import com.qxdzbc.p6.ui.common.color_generator.ColorMap

data class GetRange(
    override val funcName: String = P6FunctionDefinitions.getRangeRs,
    val wbKeyUnit: WbKeyStUnit,
    val wsNameUnit: WsNameStUnit,
    val rangeAddressUnit: RangeAddressUnit,
    val functionMapSt: St<FunctionMap>,
) : ExUnit, BaseFunc() {

    override val functionMap by functionMapSt
    override val args: List<ExUnit>
        get() = listOf(wbKeyUnit, wsNameUnit, rangeAddressUnit)
    override fun getRangeIds(): List<RangeId> {
        return listOf(
            RangeIdImp(
            rangeAddress = rangeAddressUnit.rangeAddress,
            wbKeySt = wbKeyUnit.wbKeySt,
            wsNameSt = wsNameUnit.nameSt
        )
        )
    }
    override fun getCellRangeExUnit(): List<ExUnit> {
        return listOf(this)
    }

    override fun getRanges(): List<RangeAddress> {
        return listOf(rangeAddressUnit.rangeAddress)
    }

    override fun toColorFormula(
        colorMap: ColorMap,
        wbKey: WorkbookKey?,
        wsName: String?
    ): AnnotatedString {
        val str: String = toShortFormula(wbKey, wsName)
        val color: Color? = colorMap.getColor(this)
        val rt: AnnotatedString = buildAnnotatedString {
            if (color != null) {
                withStyle(style = SpanStyle(color = color)) {
                    append(str)
                }
            } else {
                append(str)
            }
        }

        return rt
    }

    override fun shift(
        oldAnchorCell: GenericCellAddress<Int, Int>,
        newAnchorCell: GenericCellAddress<Int, Int>
    ): ExUnit {
        return this.copy(rangeAddressUnit = rangeAddressUnit.shift(oldAnchorCell, newAnchorCell))
    }

    override fun toFormula(): String {
        val a1 = wbKeyUnit
        val a2 = wsNameUnit
        val a3 = rangeAddressUnit
        val wb: String = a1.toFormula()
        val ws: String = a2.toFormula()
        val range: String = a3.toFormula()
        return range + ws + wb
    }

    override fun toShortFormula(wbKey: WorkbookKey?, wsName: String?): String {
        val a1 = wbKeyUnit
        val a2 = wsNameUnit
        val a3 = rangeAddressUnit
        val currentWbKey = a1.wbKeySt.value
        val currentWsName = a2.nameSt.value
        val cellAddress: String = a3.rangeAddress.label
        if (currentWbKey == wbKey) {
            if (currentWsName == wsName) {
                return cellAddress
            } else {
                return cellAddress + a2.toFormula()
            }
        } else {
            val wb: String = a1.toFormula()
            val ws: String = a2.toFormula()
            val ca: String = a3.toFormula()
            return ca + ws + wb
        }
    }
}

