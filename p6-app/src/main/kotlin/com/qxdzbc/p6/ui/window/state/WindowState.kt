package com.qxdzbc.p6.ui.window.state

import com.qxdzbc.p6.app.common.utils.Rse
import com.qxdzbc.p6.app.common.utils.WithSize
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.app.oddity.OddityContainer
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.message.api.connection.kernel_context.KernelContext
import com.qxdzbc.p6.ui.common.compose.Ms
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

    val globalWbStateContMs: Ms<WorkbookStateContainer>
    val formulaBarState:FormulaBarState
    val wbKeySet:Set<WorkbookKey>
    fun containWbKey(wbKey: WorkbookKey):Boolean

    /**
     * This is the globally shared workbook container in the app, not the sole container of this window state
     */
    val globalWbContMs:Ms<WorkbookContainer>
    val workbookStateMsList: List<Ms<WorkbookState>>
    val workbookStateList: List<WorkbookState>
    val workbookList: List<Workbook>

    fun removeWorkbookState(wbKey: WorkbookKey):WindowState

    fun addWbKey(wbKey: WorkbookKey):WindowState
    fun addWbKeyRs(wbKey: WorkbookKey): Rse<WindowState>

    /**
     * point this window to a new set of workbook. Remove all the current wb of this window state
     */
    @Deprecated("dont use")
    fun setWorkbookList(workbookList: List<Workbook>): WindowState
    fun setWbKeySet(wbKeySet: Set<WorkbookKey>): WindowState

    val activeWorkbookPointerMs: Ms<ActiveWorkbookPointer>
    var activeWorkbookPointer: ActiveWorkbookPointer
    val activeWorkbookState: WorkbookState?
    val activeWorkbookStateMs: Ms<WorkbookState>?
    val activeWbKey:WorkbookKey? get() = activeWorkbookPointer.wbKey

    val oddityContainerMs: Ms<OddityContainer>
    var oddityContainer: OddityContainer
    fun publishError(errorReport: ErrorReport):WindowState

    val workbookTabBarState: WorkbookTabBarState

    val saveDialogStateMs: Ms<FileDialogState>
    val saveDialogState:FileDialogState

    val loadDialogStateMs: Ms<FileDialogState>
    var loadDialogState:FileDialogState

    /**
     * replace a workbook key held by this window state with a new workbook key. Also update active workbook pointer if that is needed
     */
    fun replaceWorkbookKey(oldWbKey:WorkbookKey, newWbKey: WorkbookKey):WindowState
}