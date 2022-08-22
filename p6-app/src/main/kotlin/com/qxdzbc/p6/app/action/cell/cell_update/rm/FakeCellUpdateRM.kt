package com.qxdzbc.p6.app.action.cell.cell_update.rm

import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateResponse
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.common.compose.StateUtils.toMs
import javax.inject.Inject

class FakeCellUpdateRM @Inject constructor(
) : CellUpdateRM {
    override fun updateCell(request: CellUpdateRequest): CellUpdateResponse? {
        return CellUpdateResponse(
            WorkbookUpdateCommonResponse(
                errorReport = null,
                wbKey = WorkbookKey("Book1"),
                newWorkbook = WorkbookImp(
                    keyMs = WorkbookKey("Book1").toMs(),
                ).apply {
                    this.createNewWs("Sheet123")
                }
            )
        )
    }

}