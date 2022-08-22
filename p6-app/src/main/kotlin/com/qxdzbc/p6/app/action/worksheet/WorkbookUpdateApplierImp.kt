package com.qxdzbc.p6.app.action.worksheet

import com.qxdzbc.p6.app.action.applier.WorkbookUpdateCommonApplier
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import javax.inject.Inject

class WorkbookUpdateApplierImp @Inject constructor(
    private val wbUpdateApplier: WorkbookUpdateCommonApplier
): WorkbookUpdateApplier {
    override fun applyRes(res: WorkbookUpdateCommonResponse?) {
        wbUpdateApplier.apply(res)
    }
}