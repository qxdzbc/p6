package com.qxdzbc.p6.app.action.window

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import javax.inject.Inject

class WindowEventApplierImp @Inject constructor(
    val appStateMs:Ms<AppState>
) : WindowEventApplier {
    var appState by appStateMs
}
