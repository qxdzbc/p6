package com.qxdzbc.p6.app.action.remote_request_maker

import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Message
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Response
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.toModel
import com.qxdzbc.p6.proto.P6MsgProtos
import org.zeromq.ZMQ
import javax.inject.Inject

class P6MessageSenderImp @Inject constructor() : P6MessageSender {
    override fun send(p6Mesg: P6Message, socket: ZMQ.Socket): P6Response {
        socket.send(p6Mesg.toProto().toByteArray())
        val recv = socket.recv()
        val p6Response = P6MsgProtos.P6ResponseProto.newBuilder()
            .mergeFrom(recv)
            .build().toModel()
        return p6Response
    }
}