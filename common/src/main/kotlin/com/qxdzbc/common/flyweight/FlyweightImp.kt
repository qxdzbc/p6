package com.qxdzbc.common.flyweight


data class FlyweightImp<T> internal constructor(
    override val value: T,
    override val refCount: Int,
) : Flyweight<T> {

    override fun changeRefCountBy(v: Int): FlyweightImp<T> {
        return this.copy(refCount = maxOf(0,refCount+v))
    }
}
