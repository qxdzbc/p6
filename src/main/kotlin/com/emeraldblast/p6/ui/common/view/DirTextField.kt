package com.emeraldblast.p6.ui.common.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.emeraldblast.p6.ui.common.compose.testApp
import com.emeraldblast.p6.ui.theme.P6DefaultTypoGraphy
import com.emeraldblast.p6.ui.theme.P6GrayColors

/**
 * A text field with a three-dot button on the right side.
 */
@Composable
fun DirTextField(
    text: String,
    modifier: Modifier= Modifier,
    enabled:Boolean = true,
    textFq: FocusRequester? = null,
    onValueChange: (String) -> Unit,
    onClickBrowseButton: () -> Unit,
) {
    Row (modifier = modifier.fillMaxWidth().height(25.dp)){
        SingleLineInputText(
            text = text,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1.0F),
            enabled = enabled,
            textFq = textFq
        )
        Spacer(modifier = Modifier.width(4.dp))
        BrowseButton(enabled = enabled) { onClickBrowseButton() }
    }
}

fun main() {
    testApp {
        MaterialTheme(colors = P6GrayColors, typography = P6DefaultTypoGraphy) {
//            DirTextField(
//                "ABC",Modifier,true,
//                {},
//                {},
//            )
        }
    }
}
