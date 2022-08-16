package com.emeraldblast.p6.app.common.utils

import com.emeraldblast.p6.app.common.Rs
import com.emeraldblast.p6.common.exception.error.CommonErrors
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Ok
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject

class FileUtilImp @Inject constructor(): FileUtil {
    override fun readString(path: Path): Rs<String?, ErrorReport> {
        try{
            return Ok(Files.readString(path))
        }catch (e:Throwable){
            return CommonErrors.ExceptionError.report("Encounter exception when trying to read file:${path}",e).toErr()
        }

    }
}
