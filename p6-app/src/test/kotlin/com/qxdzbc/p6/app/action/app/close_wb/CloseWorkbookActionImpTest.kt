package com.qxdzbc.p6.app.action.app.close_wb

import io.kotest.matchers.collections.*
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import test.BaseAppStateTest
import kotlin.test.*

class CloseWorkbookActionImpTest: BaseAppStateTest(){
    lateinit var closeWbAct: CloseWorkbookActionImp
    @BeforeTest
    override fun _b() {
        super._b()
        closeWbAct = ts.comp.closeWbAct()
    }

    @Test
    fun `close 1 wb`(){
        val wbk = ts.wbKey1
        val windowStateMs = sc.getWindowStateMsById(ts.window1Id)

        assertNotNull(windowStateMs)
        assertTrue(wbk in windowStateMs.value.wbKeySet)
        assertNotNull(sc.getWb(wbk))

        closeWbAct.closeWb(CloseWorkbookRequest(
            wbKey = wbk,
            windowId = ts.window1Id
        ))

        assertTrue(wbk !in windowStateMs.value.wbKeySet )
        assertNull(sc.getWb(wbk))
    }

    @Test
    fun `test state obj after closeWb`(){
        val wbk = ts.wbKey1
        test("state obj after removing wb") {
            val input = CloseWbState(
                wbCont = ts.sc.wbCont,
                translatorContainer = ts.appState.translatorContainer,
                respectiveWindowState = ts.sc.getWindowStateByWbKey(wbk)
            )
            preCondition {
                input.wbCont.containWb(wbk) shouldBe true
                input.wbCont.getWb(wbk) shouldNotBe null
                wbk shouldBeIn input.respectiveWindowState?.wbKeySet!!
            }
            val out = closeWbAct.closeWb(ts.wbKey1Ms, input)
            postCondition {
                out.wbCont.containWb(wbk) shouldBe false
                out.wbCont.getWb(wbk) shouldBe null
                wbk shouldNotBeIn out.respectiveWindowState?.wbKeySet!!
            }
        }
    }
    @Test
    fun `test state assignment after closeWb`(){
        val wbk = ts.wbKey1
        val windowStateMs = ts.sc.getWindowStateMsByWbKey(wbk)
        test("state obj after removing wb") {
            preCondition {
                ts.sc.wbCont.containWb(wbk) shouldBe true
                ts.sc.wbCont.getWb(wbk) shouldNotBe null
                wbk shouldBeIn windowStateMs?.value?.wbKeySet!!
            }
            closeWbAct.closeWb(ts.wbKey1Ms)
            postCondition {
                ts.sc.wbCont.containWb(wbk) shouldBe false
                ts.sc.wbCont.getWb(wbk) shouldBe null
                wbk shouldNotBeIn windowStateMs?.value?.wbKeySet!!
            }
        }
    }
}

