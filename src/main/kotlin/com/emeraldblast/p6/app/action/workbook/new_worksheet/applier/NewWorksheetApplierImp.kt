package com.emeraldblast.p6.app.action.workbook.new_worksheet.applier

import com.emeraldblast.p6.app.common.RseNav
import com.emeraldblast.p6.app.action.applier.BaseApplier
import com.emeraldblast.p6.app.action.workbook.new_worksheet.rm.CreateNewWorksheetResponse2
import com.emeraldblast.p6.app.action.workbook.new_worksheet.CreateNewWorksheetResponse
import com.emeraldblast.p6.ui.app.ErrorRouter
import com.github.michaelbull.result.map
import javax.inject.Inject

class NewWorksheetApplierImp @Inject constructor(
    val internalApplier: NewWorksheetInternalApplier,
    private val errorRouter: ErrorRouter,
) : NewWorksheetApplier {
    override fun applyRes2(rs: RseNav<CreateNewWorksheetResponse2>): RseNav<CreateNewWorksheetResponse2> {
        errorRouter.publishIfPossible(rs)
        return rs.map {
            internalApplier.apply(it.newWb,it.newWsName)
            it
        }
    }
}
