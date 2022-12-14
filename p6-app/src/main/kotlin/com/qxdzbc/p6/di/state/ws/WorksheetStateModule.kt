package com.qxdzbc.p6.di.state.ws

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.common.compose.layout_coor_wrapper.LayoutCoorWrapper
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.document.cell.address.CellAddresses
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointer
import com.qxdzbc.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointerImp
import com.qxdzbc.p6.ui.document.worksheet.resize_bar.ResizeBarState
import com.qxdzbc.p6.ui.document.worksheet.resize_bar.ResizeBarStateImp
import com.qxdzbc.p6.ui.document.worksheet.ruler.RulerType
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectState
import com.qxdzbc.p6.ui.document.worksheet.select_rect.SelectRectStateImp
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSlider
import com.qxdzbc.p6.ui.document.worksheet.slider.GridSliderImp
import com.qxdzbc.p6.ui.document.worksheet.state.CellStateContainer
import com.qxdzbc.p6.ui.document.worksheet.state.CellStateContainers
import com.qxdzbc.p6.ui.document.worksheet.state.RangeConstraint
import dagger.Binds
import dagger.Provides
import org.antlr.v4.runtime.tree.ParseTree

@dagger.Module
interface WorksheetStateModule {

    @Binds
    @DefaultBaseGridSlider
    fun baseGridSlider(b:GridSliderImp):GridSlider

    companion object {
        @DefaultCellLayoutMap
        @Provides
        fun defaultCellLayoutMap():Ms<Map<CellAddress, LayoutCoorWrapper>>{
            return  ms(emptyMap())
        }

        @Provides
        @DefaultLayoutCoorMs
        fun defaultLayoutCoorMs():Ms<LayoutCoorWrapper?>{
            return ms(null)
        }

        @Provides
        @DefaultVisibleRowRange
        fun DefaultVisibleRowRange():IntRange{
            return P6R.worksheetValue.defaultVisibleRowRange
        }

        @Provides
        @DefaultVisibleColRange
        fun DefaultVisibleColRange():IntRange{
            return P6R.worksheetValue.defaultVisibleColRange
        }


        @Provides
        @DefaultCellStateContainer
        fun DefaultCellStateContainer(): Ms<CellStateContainer> {
            return CellStateContainers.immutable().toMs()
        }

        @Provides
        @DefaultSelectRectState
        fun SelectRectState(): SelectRectState {
            return SelectRectStateImp()
        }

        @Provides
        @DefaultSelectRectStateMs
        fun SelectRectStateMs(@DefaultSelectRectState i:SelectRectState): Ms<SelectRectState> {
            return ms(i)
        }

        @Provides
        @DefaultColResizeBarStateMs
        fun ResizeColBarStateMs(): Ms<ResizeBarState> {
            return ms(ResizeBarStateImp(dimen = RulerType.Col, size = P6R.size.value.defaultRowHeight))
        }

        @Provides
        @DefaultRowResizeBarStateMs
        fun ResizeRowBarStateMs(): Ms<ResizeBarState> {
            return ms(ResizeBarStateImp(dimen = RulerType.Row, size = P6R.size.value.rowRulerWidth))
        }

        @Provides
        @DefaultTopLeftCellAddress
        fun DefaultTopLeftCellAddress(): CellAddress {
            return CellAddresses.A1
        }

        @Provides
        @DefaultTopLeftCellAddressMs
        fun DefaultTopLeftCellAddressMs(@DefaultTopLeftCellAddress i:CellAddress):Ms <CellAddress> {
            return ms(i)
        }

        @Provides
        @DefaultRangeConstraint
        fun DefaultRangeConstraint(): RangeConstraint {
            return P6R.worksheetValue.defaultRangeConstraint
        }

        @Provides
        @DefaultClipBoardRange
        fun clipboardRange(): RangeAddress {
            return RangeAddresses.InvalidRange
        }

        @Provides
        @DefaultActiveWorksheetPointer
        fun DefaultActiveWorksheetPointer(): Ms<ActiveWorksheetPointer> {
            return ms(ActiveWorksheetPointerImp())
        }

        @Provides
        @EmptyCellAddressSet
        fun ecSet(): Set<CellAddress> {
            return emptySet()
        }

        @Provides
        @EmptyRangeAddressSet
        fun erSet(): Set<RangeAddress> {
            return emptySet()
        }

        @Provides
        @NullRangeAddress
        fun nullRangeAddress():RangeAddress?{
            return null
        }


    }
}
