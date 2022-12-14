package com.qxdzbc.common.compose.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun testApp(
    size:DpSize = DpSize(500.dp,500.dp),
    content: @Composable ApplicationScope.()->Unit
) {
    application {
        val wState = rememberWindowState(size=size)
        val appScope = this
        Window(
            onCloseRequest = ::exitApplication,
            title = "Test app",
            state = wState
        ) {
            content(appScope)
        }
    }
}
