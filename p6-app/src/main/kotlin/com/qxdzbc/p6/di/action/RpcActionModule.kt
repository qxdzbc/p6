package com.qxdzbc.p6.di.action

import com.qxdzbc.p6.app.action.rpc.AppRpcAction
import com.qxdzbc.p6.app.action.rpc.AppRpcActionImp
import com.qxdzbc.p6.app.action.rpc.RpcActions
import com.qxdzbc.p6.app.action.rpc.RpcActionsImp
import com.qxdzbc.p6.di.P6Singleton
import dagger.Binds

@dagger.Module
interface RpcActionModule {
    @Binds
    @P6Singleton
    fun rpcAction(i: RpcActionsImp):RpcActions

    @Binds
    @P6Singleton
    fun AppRpcAction(i:AppRpcActionImp): AppRpcAction
}
