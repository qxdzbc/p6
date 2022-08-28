package com.qxdzbc.p6.translator.formula.function_def.formula_back_converter

import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import javax.inject.Inject

class FunctionFormulaBackConverter_ForGetRangeAddress @Inject constructor() : FunctionFormulaBackConverter {
    override fun toFormula(u: ExUnit.Func): String? {
        val args = u.args
        if (args.size == 3) {
            val a1 = args[0]
            val a2 = args[1]
            val a3 = args[2]
            if(a1 is ExUnit.WbKeyStUnit && a2 is ExUnit.WsNameStUnit && a3 is ExUnit.RangeAddressUnit){
                val wb:String = a1.toFormula()
                val ws:String = a2.toFormula()
                val range:String = a3.toFormula()
                return range+ws+wb
            }else{
                return null
            }
        } else {
            return null
        }
    }

    override fun toFormulaSelective(u: ExUnit.Func, wbKey: WorkbookKey?, wsName: String?): String? {
        val args = u.args
        if (args.size == 3) {
            val a1 = args[0]
            val a2 = args[1]
            val a3 = args[2]
            if(a1 is ExUnit.WbKeyStUnit && a2 is ExUnit.WsNameStUnit && a3 is ExUnit.RangeAddressUnit){
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
                    val cellAddress: String = a3.toFormula()
                    return cellAddress + ws + wb
                }
//                val wb:String = a1.toFormulaSelective(wbKey, wsName)
//                val ws:String = a2.toFormulaSelective(wbKey, wsName)
//                val range:String = a3.toFormulaSelective(wbKey, wsName)
//                return range+ws+wb
            }else{
                return null
            }
        } else {
            return null
        }
    }
}
