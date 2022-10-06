package com.qxdzbc.p6.ui.document.worksheet.cursor.thumb

import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface ThumbStateFactory{
    fun create(
        @Assisted("1") cursorStateSt: St<CursorState>,
        @Assisted("2") cellLayoutCoorMapSt: St<Map<CellAddress, LayoutCoorWrapper>>,
    ): ThumbStateImp
}
