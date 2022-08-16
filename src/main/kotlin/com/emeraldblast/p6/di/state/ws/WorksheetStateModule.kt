package com.emeraldblast.p6.di.state.ws

import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.range.address.RangeAddress
import com.emeraldblast.p6.app.document.range.address.RangeAddresses
import com.emeraldblast.p6.ui.common.R
import com.emeraldblast.p6.ui.common.compose.LayoutCoorWrapper
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.MsUtils.toMs
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointer
import com.emeraldblast.p6.ui.document.workbook.active_sheet_pointer.ActiveWorksheetPointerImp
import com.emeraldblast.p6.ui.document.worksheet.resize_bar.ResizeBarState
import com.emeraldblast.p6.ui.document.worksheet.resize_bar.ResizeBarStateImp
import com.emeraldblast.p6.ui.document.worksheet.ruler.RulerType
import com.emeraldblast.p6.ui.document.worksheet.select_rect.SelectRectState
import com.emeraldblast.p6.ui.document.worksheet.select_rect.SelectRectStateImp
import com.emeraldblast.p6.ui.document.worksheet.slider.GridSlider
import com.emeraldblast.p6.ui.document.worksheet.slider.GridSliderImp
import com.emeraldblast.p6.ui.document.worksheet.state.CellStateContainer
import com.emeraldblast.p6.ui.document.worksheet.state.CellStateContainers
import com.emeraldblast.p6.ui.document.worksheet.state.RangeConstraint
import dagger.Binds
import dagger.Provides

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
            return R.worksheetValue.defaultVisibleRowRange
        }

        @Provides
        @DefaultVisibleColRange
        fun DefaultVisibleColRange():IntRange{
            return R.worksheetValue.defaultVisibleColRange
        }


        @Provides
        @DefaultCellStateContainer
        fun DefaultCellStateContainer(): Ms<CellStateContainer> {
            return CellStateContainers.immutable().toMs()
        }

        @Provides
        @DefaultSelectRectStateMs
        fun SelectRectStateMs(): Ms<SelectRectState> {
            return ms(SelectRectStateImp())
        }

        @Provides
        @DefaultColResizeBarStateMs
        fun ResizeColBarStateMs(): Ms<ResizeBarState> {
            return ms(ResizeBarStateImp(dimen = RulerType.Col, size = R.size.value.defaultRowHeight))
        }

        @Provides
        @DefaultRowResizeBarStateMs
        fun ResizeRowBarStateMs(): Ms<ResizeBarState> {
            return ms(ResizeBarStateImp(dimen = RulerType.Row, size = R.size.value.rowRulerWidth))
        }

        @Provides
        @DefaultTopLeftCellAddress
        fun DefaultTopLeftCellAddress(): CellAddress {
            return CellAddress(1, 1)
        }

        @Provides
        @DefaultRangeConstraint
        fun DefaultRangeConstraint(): RangeConstraint {
            return R.worksheetValue.defaultRangeConstraint
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
