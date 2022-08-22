package com.qxdzbc.p6.app.action.applier

import com.qxdzbc.p6.app.communication.res_req_template.response.Response

/**
 * Base Applier interface for all response appliers
 */
interface ResApplier<T: Response>{
    fun applyRes(res:T?)
}