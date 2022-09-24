package com.qxdzbc.p6.ui.example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.qxdzbc.common.compose.StateUtils.rms
import com.qxdzbc.p6.ui.common.compose.P6TestApp

fun main() {
    P6TestApp {
        val numberMs = rms(123)
        val cpNumber = rms(0)
        val cc = rememberCoroutineScope()

        var t1: String by rms("t1")
        var t2: String by rms("t2")
        var f by rms(true)
        val fc = remember { FocusRequester() }
        val fc2 = remember { FocusRequester() }
        LaunchedEffect(f) {
            if (f) {
                fc.requestFocus()
            } else {
                fc2.requestFocus()
            }
        }
        Column {
            BasicTextField(
                value = t1,
                onValueChange = { t1 = it },
                modifier = Modifier.focusRequester(fc)
            )
            BasicTextField(
                value = t2,
                onValueChange = { t2 = it },
                modifier = Modifier.focusRequester(fc2)
            )
            MButton("Switch") {
                f= !f
            }
        }
    }
}
