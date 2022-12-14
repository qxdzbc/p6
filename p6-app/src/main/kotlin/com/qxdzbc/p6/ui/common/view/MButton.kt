package com.qxdzbc.p6.ui.common.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.P6R
import com.qxdzbc.p6.ui.common.compose.P6TestApp
import com.qxdzbc.p6.ui.theme.P6AllWhiteColors

/**
 * Standard button used in the entire app
 */
@Composable
fun MButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
    content: @Composable () -> Unit = {}
) {
    Button(
        onClick = onClick,
        shape = P6R.shape.buttonShape,
        modifier = modifier.height(24.dp).widthIn(71.dp),
        contentPadding = PaddingValues(horizontal = 5.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colors.onSecondary)
    ) {
        content()
    }
}

@Composable
fun MButton(
    label: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
) {
    MButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(label)
    }
}

fun main() {
    P6TestApp {
        MaterialTheme(colors = P6AllWhiteColors) {
            Column {
                MButton {
                    BasicText("Ok")
                }
                Divider()
                MButton {
                    BasicText("Cancel")
                }
                Divider()
                MButton {
                    BasicText("Apply")
                }
            }
        }
    }
}
