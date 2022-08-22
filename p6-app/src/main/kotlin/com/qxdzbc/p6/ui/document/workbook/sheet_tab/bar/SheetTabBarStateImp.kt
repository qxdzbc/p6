package com.qxdzbc.p6.ui.document.workbook.sheet_tab.bar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.ui.common.compose.Ms
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointer
import com.qxdzbc.p6.ui.document.workbook.sheet_tab.tab.SheetTabState
import com.qxdzbc.p6.ui.document.workbook.sheet_tab.tab.SheetTabStateImp


data class SheetTabBarStateImp constructor(
    override val activeSheetPointerMs: Ms<ActiveWorksheetPointer>,
    private val wbMs:Ms<Workbook>,
) : SheetTabBarState {
    private val wb by wbMs
    override val tabStateList: List<SheetTabState> get() = wb.worksheets.map {
        SheetTabStateImp(
            sheetName = it.name,
            isSelected = activeSheetPointer.isPointingTo(it.name)
        )
    }

    private val map get()= tabStateList.associateBy { it.sheetName }
    override var activeSheetPointer: ActiveWorksheetPointer by this.activeSheetPointerMs

    override fun getTabState(sheetName: String): SheetTabState? {
        return map[sheetName]
    }

    override val size: Int
        get() = wb.size
}