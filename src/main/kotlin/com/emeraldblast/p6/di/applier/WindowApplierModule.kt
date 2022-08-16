package com.emeraldblast.p6.di.applier

import com.emeraldblast.p6.app.action.window.WindowEventApplier
import com.emeraldblast.p6.app.action.window.WindowEventApplierImp
import com.emeraldblast.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface WindowApplierModule {
    @Binds
    @P6Singleton
    fun WindowEventApplier(i: WindowEventApplierImp): WindowEventApplier

}
