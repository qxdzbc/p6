package com.qxdzbc.p6.ui.common.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.common.compose.P6TestApp


@Composable
fun OkButton(
    modifier: Modifier = Modifier,
    onOk: () -> Unit = {},
) {
    MButton(onClick = onOk,modifier=modifier){
        BasicText("OK")
    }
}

@Composable
fun CancelButton(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit = {},
) {
    MButton(onClick = onCancel,modifier=modifier){
        BasicText("Cancel")
    }
}

/**
 * an Ok and a Cancel button
 */
@Composable
fun OkCancel(
    okModifier: Modifier = Modifier,
    cancelModifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier,
    onOk: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    Row(modifier = boxModifier) {
        OkButton(okModifier, onOk)
        Spacer(modifier = Modifier.width(P6R.padding.value.betweenButton))
        CancelButton(cancelModifier, onCancel)
    }
}


fun main() {
    P6TestApp {
        OkCancel()
    }
}
