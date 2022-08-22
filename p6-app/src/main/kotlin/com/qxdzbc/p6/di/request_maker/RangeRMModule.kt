package com.qxdzbc.p6.di.request_maker

import com.qxdzbc.p6.app.action.range.RangeRM
import com.qxdzbc.p6.app.action.range.RangeRMImp
import com.qxdzbc.p6.app.action.range.paste_range.rm.FakePasteRangeRM
import com.qxdzbc.p6.app.action.range.paste_range.rm.PasteRangeRM
import com.qxdzbc.p6.app.action.range.paste_range.rm.PasteRangeRMImp
import com.qxdzbc.p6.app.action.range.range_to_clipboard.rm.CopyRangeToClipboardRM
import com.qxdzbc.p6.app.action.range.range_to_clipboard.rm.CopyRangeToClipboardRMImp
import com.qxdzbc.p6.app.action.range.range_to_clipboard.rm.FakeCopyRangeToClipboardRM
import com.qxdzbc.p6.di.Fake
import com.qxdzbc.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface RangeRMModule {
    @Binds
    @P6Singleton

    fun PasteRangeRMLocal(i: PasteRangeRMImp): PasteRangeRM

    @Binds
    @P6Singleton

    fun CopyRangeToClipboardRMLocal(i: CopyRangeToClipboardRMImp): CopyRangeToClipboardRM

    @Binds
    @P6Singleton
    @com.qxdzbc.p6.di.Fake
    fun FakePasteRangeRequestMaker(i: FakePasteRangeRM): PasteRangeRM

    @Binds
    @P6Singleton
    fun RangeRequestMaker(i: RangeRMImp): RangeRM

    @Binds
    @P6Singleton
    @com.qxdzbc.p6.di.Fake
    fun FakeRangeToClipboardRequestMaker(i: FakeCopyRangeToClipboardRM): CopyRangeToClipboardRM
}