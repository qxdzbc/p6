package com.qxdzbc.p6.ui.window.focus_state

import androidx.compose.runtime.getValue
import androidx.compose.ui.focus.FocusRequester
import com.qxdzbc.p6.di.FalseMs
import com.qxdzbc.p6.di.TrueMs
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.app.common.focus_requester.FocusRequesterWrapper
import com.qxdzbc.p6.app.common.focus_requester.FocusRequesterWrapper.Companion.wrap
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorFocusState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

/**
 * A focus state that can only focus on 1 or 0 thing at a time
 */
@ContributesBinding(P6AnvilScope::class)
data class SingleWindowFocusStateImp  constructor(
    private val isCursorFocusedMs:Ms<Boolean>,
    private val isEditorFocusedMs:Ms<Boolean>,
    override val cursorFocusRequester: FocusRequesterWrapper = FocusRequester().wrap(),
    override val editorFocusRequester: FocusRequesterWrapper = FocusRequester().wrap(),
) : WindowFocusState {

    @Inject constructor():this(
        ms(true),
        ms(false),
    )

    override val isCursorFocused: Boolean by isCursorFocusedMs
    override fun setCursorFocus(i: Boolean): SingleWindowFocusStateImp {
        isCursorFocusedMs.value = i
        return this
    }

    private val l = listOf(isCursorFocusedMs, isEditorFocusedMs)

    override fun freeAllFocus(): WindowFocusState {
        editorFocusRequester.freeFocus()
        cursorFocusRequester.freeFocus()
        return this
    }

    override fun restoreDefault(): WindowFocusState {
        return this.focusOnCursor()
    }

    override fun focusOnCursor(): WindowFocusState {
        cursorFocusRequester.requestFocus()
        return this
    }

    override fun freeFocusOnCursor(): WindowFocusState {
        cursorFocusRequester.freeFocus()
        return this
    }

    override val isEditorFocused: Boolean by isEditorFocusedMs
    override fun setCellEditorFocus(i: Boolean): SingleWindowFocusStateImp {
        isEditorFocusedMs.value = i
        return this
    }

    override fun focusOnEditor(): WindowFocusState {
        editorFocusRequester.requestFocus()
        return this
    }

    override fun freeFocusOnEditor(): WindowFocusState {
        editorFocusRequester.freeFocus()
        return this
    }
}
