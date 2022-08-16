package com.emeraldblast.p6.app.common.utils.binary_copier

import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result
/**
 * Copy a byte array into the system's clipboard
 */
interface BinaryCopier {
    fun copyRs(data:ByteArray):Result<ByteArray,ErrorReport>
    fun copy(data:ByteArray):ByteArray
}
