package com.qxdzbc.p6.ui.document.worksheet.slider

import dagger.assisted.AssistedFactory

@AssistedFactory
interface LimitedGridSliderFactory{
    fun create(): LimitedSlider
}
