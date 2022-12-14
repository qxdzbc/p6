package com.qxdzbc.p6.app.action.app.process_save_path

import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.window.file_dialog.WithFileExtension
import com.squareup.anvil.annotations.ContributesBinding
import org.apache.commons.io.FilenameUtils
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject
import javax.swing.filechooser.FileFilter

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class MakeSavePathImp @Inject constructor() : MakeSavePath {
    override fun makeSavePath(originalPath: Path, fileFilter: FileFilter?): Path? {
        if (!Files.isDirectory(originalPath)) {
            if (fileFilter is WithFileExtension) {
                val extensionRs = kotlin.runCatching {
                    FilenameUtils.getExtension(originalPath.toString())
                }
                if (extensionRs.isSuccess) {
                    if (extensionRs.getOrNull() == fileFilter.extension) {
                        return originalPath
                    } else {
                        val newPath = Path.of(originalPath.toString() + ".${fileFilter.extension}")
                        return newPath
                    }
                } else {
                    return originalPath
                }
            } else {
                return originalPath
            }
        } else {
            return null
        }
    }
}
