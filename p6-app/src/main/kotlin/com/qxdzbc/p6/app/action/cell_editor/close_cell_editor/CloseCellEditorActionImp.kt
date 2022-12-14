package com.qxdzbc.p6.app.action.cell_editor.close_cell_editor

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.PartialTreeExtractor
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractor
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class CloseCellEditorActionImp @Inject constructor(
    private val stateContMs: Ms<StateContainer>,
    @PartialTreeExtractor
    val treeExtractor: TreeExtractor,
) : CloseCellEditorAction {

    private val stateCont by stateContMs
    private val editorStateMs = stateCont.cellEditorStateMs
    private val editorState by editorStateMs

    override fun closeEditor() {
        val fcsMs = editorState.targetWbKey?.let { stateCont.getFocusStateMsByWbKey(it) }
        if (fcsMs != null) {
            fcsMs.value = fcsMs.value.focusOnCursor()
        }
        editorStateMs.value = editorState.clearAll().close()
    }
}
