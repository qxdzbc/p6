package com.qxdzbc.p6.app.action.cursor.paste_range_to_cursor

import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt

interface PasteRangeToCursor{
    /**
     * Paste whatever in the clipboard into the current position of a cursor at [wbws]
     */
    fun pasteRange(wbws: WbWs)
    fun pasteRange(wbwsSt: WbWsSt)
}
