package com.qxdzbc.p6.ui.document.worksheet.slider

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.ui.document.worksheet.cursor.state.CursorState

abstract class BaseSlider : GridSlider {
    override val currentDisplayedRange: RangeAddress
        get() = RangeAddress(visibleColRange,visibleRowRange)
    override val lastVisibleColNotMargin: Int
        get() = if (marginCol != null) {
            maxOf(lastVisibleCol - 1, 0)
//            lastVisibleCol
        } else {
            lastVisibleCol
        }

    override val lastVisibleRowNotMargin: Int
        get() {
            return if (marginRow != null) {
                maxOf(lastVisibleRow - 1, 0)
            } else {
                lastVisibleRow
            }
        }

    override fun containAddressNotMargin(cellAddress: CellAddress): Boolean {
        return cellAddress.colIndex in visibleColRangeExcludeMargin && cellAddress.rowIndex in visibleRowRangeExcludeMargin
    }

    override val visibleColRangeExcludeMargin: IntRange
        get() {
            return if (marginCol != null) {
                firstVisibleCol..maxOf(firstVisibleCol, marginCol!! - 1)
            } else {
                visibleColRange
            }
        }

    override val visibleRowRangeExcludeMargin: IntRange
        get() {
            return if (marginRow != null) {
                firstVisibleRow..maxOf(firstVisibleRow, marginRow!! - 1)
            } else {
                visibleRowRange
            }
        }

    override fun followCursorMainRangeBotRight(cursorState: CursorState): GridSlider {
        val cell = cursorState.mainRange?.botRight
        val rt = cell?.let {
            this.followCell(it)
        } ?: this
        return rt
    }

    override fun followCell(cellAddress: CellAddress): GridSlider {
        val newMainCell: CellAddress = cellAddress
        if (!this.containAddressNotMargin(newMainCell)) {
            // x: cursor in the far right
            val maxColDif = newMainCell.colIndex - this.lastVisibleColNotMargin
            if (maxColDif > 0) {
                return this.shiftRight(maxColDif)
            } else {
                // x: cursor in the far left
                val minColDif = this.firstVisibleCol - newMainCell.colIndex
                if (minColDif > 0) {
                    return this.shiftLeft(minColDif)
                }
            }

            val maxRowDif = newMainCell.rowIndex - this.lastVisibleRowNotMargin
            if (maxRowDif > 0) {
                // x: cursor at bot
                return this.shiftDown(maxRowDif)
            } else {
                // x: cursor at top
                val minRowDif = this.firstVisibleRow - newMainCell.rowIndex
                if (minRowDif > 0) {
                    return this.shiftUp(minRowDif)
                }
            }
            return this
        } else {
            return this
        }
    }

    override fun followCursorMainCell(cursorState: CursorState): GridSlider {
        val newMainCell: CellAddress = cursorState.mainCell
        return this.followCell(newMainCell)
    }
}
