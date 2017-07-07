package com.virtualightning.fileresolver.schema.values

import com.virtualightning.fileresolver.schema.exceptions.InvalidOperatorException

class BooleanValue(value : Boolean? = null) : ValueSchema<Boolean>(value){
    override fun addValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        throw InvalidOperatorException("Boolean cannot supported operator \"+\"")
    }

    override fun subValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        throw InvalidOperatorException("Boolean cannot supported operator \"-\"")
    }

    override fun mulValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        throw InvalidOperatorException("Boolean cannot supported operator \"*\"")
    }

    override fun divValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        throw InvalidOperatorException("Boolean cannot supported operator \"/\"")
    }

    override fun type(): String = "Boolean"

    override fun level(): Int = -1

    override fun transfer(value: Any): ValueSchema<*> = BooleanValue()
}