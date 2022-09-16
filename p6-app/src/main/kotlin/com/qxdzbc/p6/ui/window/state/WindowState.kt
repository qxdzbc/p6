package com.qxdzbc.p6.ui.window.state

import com.qxdzbc.common.Rse
import com.qxdzbc.common.WithSize
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.oddity.OddityContainer
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelContext
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.ui.common.color_generator.FormulaColorProvider
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.qxdzbc.p6.ui.window.file_dialog.state.FileDialogState
import com.qxdzbc.p6.ui.window.focus_state.WindowFocusState
import com.qxdzbc.p6.ui.window.formula_bar.FormulaBarState
import com.qxdzbc.p6.ui.window.kernel_dialog.ShowDialogState
import com.qxdzbc.p6.ui.window.status_bar.StatusBarState
import com.qxdzbc.p6.ui.window.workbook_tab.bar.WorkbookTabBarState
import kotlinx.coroutines.CompletableDeferred
import java.nio.file.Path

interface WindowState : WithSize {
    val id:String

    val formulaColorProvider: FormulaColorProvider

    val focusStateMs:Ms<WindowFocusState>
    var focusState:WindowFocusState

    val kernel:KernelContext
    val statusBarStateMs:Ms<StatusBarState>
    var statusBarState:StatusBarState

    val openCommonFileDialog:Boolean
    val commonFileDialogJob: CompletableDeferred<Path?>?
    fun setCommonFileDialogJob(job:CompletableDeferred<Path?>?):WindowState
    fun removeCommonFileDialogJob():WindowState

    val showConnectToKernelDialogStateMs:Ms<ShowDialogState>
    var showConnectToKernelDialogState:ShowDialogState

    val showStartKernelDialogStateMs:Ms<ShowDialogState>
    var showStartKernelDialogState: ShowDialogState

    val wbStateContMs: Ms<WorkbookStateContainer>
    val formulaBarState:FormulaBarState
    val wbKeyMsSet:Set<Ms<WorkbookKey>>
    val wbKeySet:Set<WorkbookKey>
    fun containWbKey(wbKey: WorkbookKey):Boolean

    /**
     * This is the globally shared workbook container in the app, not the sole container of this window state
     */
    val wbContMs:Ms<WorkbookContainer>
    val wbStateMsList: List<Ms<WorkbookState>>
    val wbStateList: List<WorkbookState>
    val wbList: List<Workbook>

    fun removeWbState(wbKeyMs: Ms<WorkbookKey>):WindowState

    fun addWbKey(wbKey: Ms<WorkbookKey>):WindowState
    fun addWbKeyRs(wbKey: Ms<WorkbookKey>): Rse<WindowState>


    fun setWbKeySet(wbKeySet: Set<Ms<WorkbookKey>>): WindowState

    val activeWbPointerMs: Ms<ActiveWorkbookPointer>
    var activeWbPointer: ActiveWorkbookPointer
    val activeWbState: WorkbookState?
    val activeWbStateMs: Ms<WorkbookState>?
    val activeWbKey:WorkbookKey? get() = activeWbPointer.wbKey

    val oddityContainerMs: Ms<OddityContainer>
    var oddityContainer: OddityContainer
    fun publishError(errorReport: ErrorReport):WindowState

    val wbTabBarState: WorkbookTabBarState

    val saveDialogStateMs: Ms<FileDialogState>
    val saveDialogState:FileDialogState

    val loadDialogStateMs: Ms<FileDialogState>
    var loadDialogState:FileDialogState

    val windowTitle:String
}
