package com.qxdzbc.p6.app.communication.applier.app.create_new_wb


import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse
import com.qxdzbc.p6.app.action.app.create_new_wb.applier.CreateNewWorkbookApplier
import com.qxdzbc.p6.app.action.app.create_new_wb.applier.CreateNewWorkbookApplierImp
import com.qxdzbc.p6.app.document.workbook.WorkbookImp
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.p6.ui.window.state.WindowState
import org.mockito.kotlin.mock
import test.TestSample
import kotlin.test.*

class CreateNewWorkbookInternalApplierImpTest {

    lateinit var applier: CreateNewWorkbookApplierImp
    lateinit var appStateMs: Ms<AppState>
    lateinit var errRouter: ErrorRouter
    lateinit var windowStateMs: Ms<WindowState>
    val newWB = WorkbookImp(WorkbookKey("newWb").toMs())
    lateinit var okRes: CreateNewWorkbookResponse
    lateinit var errRes: CreateNewWorkbookResponse
    lateinit var ts: TestSample

    @BeforeTest
    fun b() {
        errRouter = mock()
        ts = TestSample()
        appStateMs = ts.sampleAppStateMs()
        applier = CreateNewWorkbookApplierImp(
         baseApplier = ts.p6Comp.baseApplier(),
         stateContMs = ts.p6Comp.stateContMs(),
            pickDefaultActiveWb = ts.p6Comp.pickDefaultActiveWbAction()
        )
        windowStateMs = appStateMs.value.windowStateMsList[0]
        okRes = CreateNewWorkbookResponse(
            errorReport = null,
            wb = newWB,
            windowId = windowStateMs.value.id
        )
        errRes = CreateNewWorkbookResponse(
            errorReport = CommonErrors.Unknown.header.toErrorReport(),
            wb = null,
            windowId = windowStateMs.value.id
        )
    }

    @Test
    fun `apply ok on window`() {
        val wds by windowStateMs
        assertNull(appStateMs.value.wbCont.getWb(newWB.key))
        assertNull(appStateMs.value.getWindowStateMsByWbKey(newWB.key))
        /**/
        applier.iapply(okRes.wb, okRes.windowId)
        assertNotNull(appStateMs.value.wbCont.getWb(newWB.key))
        assertNotNull(appStateMs.value.getWindowStateMsByWbKey(newWB.key))
    }

    @Test
    fun `apply ok with null window id`() {
        val res = okRes.copy(windowId = null)
        val wdsCount = appStateMs.value.windowStateMsList.size
        testApplyOnApp(res)
        assertEquals(wdsCount, appStateMs.value.windowStateMsList.size)
    }

    @Test
    fun `apply ok with invalid qqq window id`() {
        val res = okRes.copy(windowId = "new windowId")
        val wdsCount = appStateMs.value.windowStateMsList.size
        testApplyOnApp(res)
        assertEquals(wdsCount+1, appStateMs.value.windowStateMsList.size)
    }

    fun testApplyOnApp(res: CreateNewWorkbookResponse) {
        assertNull(appStateMs.value.wbCont.getWb(newWB.key))
        assertNull(appStateMs.value.getWindowStateMsByWbKey(newWB.key))
        /**/
        applier.iapply(res.wb, res.windowId)
        assertNotNull(appStateMs.value.wbCont.getWb(newWB.key))
        assertNotNull(appStateMs.value.getWindowStateMsByWbKey(newWB.key))
    }

    @Test
    fun `apply ok with invalid window id`() {
        val res = okRes.copy(windowId = "invalid window id")
        val wdsCount = appStateMs.value.windowStateMsList.size
        testApplyOnApp(res)
        assertEquals(wdsCount + 1, appStateMs.value.windowStateMsList.size)
    }

//    @Test
//    fun `apply ok with invalid window id on single window`() {
//        appStateMs.value = TestSample.sampleAppStateMs().value
//        val res:CreateNewWorkbookResponse = okRes.copy(windowId = "invalid window id")
//        val wdsCount = appStateMs.value.windowStateMsList.size
//        testApplyOnApp(res)
//        assertEquals(wdsCount,appStateMs.value.windowStateMsList.size)
//    }
}
