package com.qxdzbc.p6.app.common.table

data class TableCRRowImp<R, E>(
    override val rowIndex: R,
    override val elements: List<E>
) : TableCRRow<R, E>, List<E> by elements
