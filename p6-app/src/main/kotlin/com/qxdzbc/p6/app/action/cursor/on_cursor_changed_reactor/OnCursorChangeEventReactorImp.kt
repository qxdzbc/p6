package com.qxdzbc.p6.app.action.cursor.on_cursor_changed_reactor

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorId
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.state.TextSizeSelectorState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class OnCursorChangeEventReactorImp @Inject constructor(
    val stateContainerSt: St<@JvmSuppressWildcards StateContainer>
) : OnCursorChangeEventReactor {

    private val sc by stateContainerSt

    override fun onCursorChanged(cursorId: CursorId) {
        sc.getCursorState(cursorId)?.also { cursorState ->
            val cellStateAtCursor = sc.getCellState(cursorState.mainCellId)
            sc.getTextSizeSelectorStateMs(cursorId.wbKey)?.also { textSizeSelectorStateMs ->
                textSizeSelectorStateMs.value = textSizeSelectorStateMs.value.setHeaderText(
                    cellStateAtCursor?.textFormat?.textSize?.toString() ?: TextSizeSelectorState.defaultHeader
                )
            }

            sc.getTextColorSelectorStateMs(cursorId.wbKey)?.also { textColorSelectorStateMs ->
                textColorSelectorStateMs.value = textColorSelectorStateMs.value.setCurrentColor(
                    cellStateAtCursor?.textFormat?.textColor
                )
            }

            sc.getCellBackgroundColorSelectorStateMs(cursorId.wbKey)?.also {cellBackgroundColorSelectorStateMs->
                cellBackgroundColorSelectorStateMs.value = cellBackgroundColorSelectorStateMs.value.setCurrentColor(
                    cellStateAtCursor?.cellFormat?.backgroundColor
                )
            }
        }
    }
}