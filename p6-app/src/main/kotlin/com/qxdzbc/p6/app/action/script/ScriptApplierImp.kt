package com.qxdzbc.p6.app.action.script

import com.qxdzbc.p6.app.action.script.new_script.applier.NewScriptApplier
import com.qxdzbc.p6.app.action.script.script_change.applier.ScriptChangeApplier
import javax.inject.Inject

class ScriptApplierImp @Inject constructor(
    val ns: NewScriptApplier,
    val sc: ScriptChangeApplier
) : ScriptApplier, NewScriptApplier by ns, ScriptChangeApplier by sc