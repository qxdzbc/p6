package com.emeraldblast.p6.bench

import com.emeraldblast.p6.ui.app.state.AppStateImp
import com.emeraldblast.p6.ui.common.compose.ms
import dagger.multibindings.ClassKey
import dagger.multibindings.StringKey
import org.zeromq.SocketType
import org.zeromq.ZContext
import org.zeromq.ZMsg
import test.TestSample
import zmq.ZMQ.ZMQ_ROUTER_MANDATORY
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

val random = Random(123)

@OptIn(ExperimentalContracts::class)
fun Any?.n2(): Boolean {
    contract {
        returns(true) implies (this@n2 != null)
        returns(false) implies (this@n2 == null)
    }
    return this@n2 != null
}

class Bench {
    @OptIn(ExperimentalContracts::class)
    fun nn(z: Any?): Boolean {
        contract {
            returns(true) implies (z != null)
            returns(false) implies (z == null)
        }
        return z != null
    }


    @Test
    fun z() {
//        val ts = TestSample()
////        val z = (ts.p6Comp.appStateMs().value as AppStateImp).l1
////        val z = ts.p6Comp.l1()
//        val z = ts.p6Comp.q().l1
//        println(z.size)
    }


    //    @Test
    fun server() {
        val zContext = ZContext()
        val serverSocket = zContext.createSocket(SocketType.REP)
        val port = 7777
        serverSocket.bind("tcp://*:${port}")
        while (true) {
            val recv = serverSocket.recvStr()
            println("Received: ${recv}")
            val num = "response:${recv}"
            serverSocket.send(num)
            if (recv == "stop") {
                break
            }
        }
    }

    //    @Test
    fun server2() {
        val zContext = ZContext()
        ZMQ_ROUTER_MANDATORY
        val serverSocket = zContext.createSocket(SocketType.ROUTER)
        serverSocket.setRouterMandatory(true)
        val port = 7777
        serverSocket.bind("tcp://*:${port}")
        while (true) {
            val msg = ZMsg.recvMsg(serverSocket)
            println("Receive: ${msg}")
            val identity = msg.unwrap()
            val data = msg.pop().getString(Charsets.UTF_8)
            val res = "Response: ${data}"
            val response = ZMsg().apply {
                add(identity)
                add("")
                add(res)
            }
            response.send(serverSocket)
        }
    }


}


