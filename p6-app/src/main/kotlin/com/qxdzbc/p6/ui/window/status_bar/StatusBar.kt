package com.qxdzbc.p6.ui.window.status_bar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.window.status_bar.rpc_status.RPCStatusView

@Composable
fun StatusBar(
    state:StatusBarState
){
    Row{
        RPCStatusView(
            state = state.rpcServerStatusState,
            onClick = {
                state.rpcServerStatusState = state.rpcServerStatusState.showDetail()
            }
        )
    }
}
