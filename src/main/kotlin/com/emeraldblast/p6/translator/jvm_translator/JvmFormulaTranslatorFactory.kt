package com.emeraldblast.p6.translator.jvm_translator

import com.emeraldblast.p6.formula.translator.antlr.FormulaBaseVisitor
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import dagger.assisted.AssistedFactory

@AssistedFactory
interface JvmFormulaTranslatorFactory{
    fun create(visitor: FormulaBaseVisitor<ExUnit>): JvmFormulaTranslator
}
