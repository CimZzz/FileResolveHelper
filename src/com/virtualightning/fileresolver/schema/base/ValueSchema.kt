package com.virtualightning.fileresolver.schema.base

abstract class ValueSchema<E>(var value : E? = null) : ComputableSchema("") {
    var isConstantValue = true

    abstract fun addValue(otherSchema : ValueSchema<*>) : ValueSchema<*>
    abstract fun subValue(otherSchema : ValueSchema<*>) : ValueSchema<*>
    abstract fun mulValue(otherSchema : ValueSchema<*>) : ValueSchema<*>
    abstract fun divValue(otherSchema : ValueSchema<*>) : ValueSchema<*>
    abstract fun type() : String
    abstract fun level() : Int
    abstract fun transfer(value : Any) : ValueSchema<*>


    override fun toString(): String {
        return value.toString()
    }
}