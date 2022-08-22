package com.qxdzbc.p6.app.action.script.new_script.rm

import com.qxdzbc.p6.app.action.common_data_structure.SingleSignalResponse
import com.qxdzbc.p6.app.action.script.new_script.NewScriptRequest
import com.qxdzbc.p6.app.action.script.new_script.NewScriptResponse
import javax.inject.Inject

class FakeNewScriptRM @Inject constructor(
) : NewScriptRM {
    override fun newScript(request: NewScriptRequest): NewScriptResponse? {
        return NewScriptResponse(SingleSignalResponse(null))
    }
}