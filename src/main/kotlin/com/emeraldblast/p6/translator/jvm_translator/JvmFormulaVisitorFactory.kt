package com.emeraldblast.p6.translator.jvm_translator

import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.ui.common.compose.St
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface JvmFormulaVisitorFactory{
    fun create(
        @Assisted("1") wbKeySt: St<WorkbookKey>,
        @Assisted("2") wsNameSt: St<String>
    ): JvmFormulaVisitor
}
