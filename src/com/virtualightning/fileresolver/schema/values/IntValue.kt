package com.virtualightning.fileresolver.schema.values

import com.virtualightning.fileresolver.schema.base.ValueSchema
import com.virtualightning.fileresolver.exceptions.InvalidOperatorException


class IntValue(value : Int) : NumberValue<Int>(value) {
    override fun type(): String = "Int"
    override fun transfer(value: Any): ValueSchema<*> = IntValue(value as Int)

    override fun level(): Int = 10

    override fun addValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        if (otherSchema !is NumberValue)
            throw InvalidOperatorException("${otherSchema.type()} cannot supported operator \"+\"")

        if(level() >= otherSchema.level())
            return IntValue(value!! + (otherSchema.value!! as Number).toInt())
        else return otherSchema.transfer(value!!).addValue(otherSchema)
    }

    override fun subValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        if (otherSchema !is NumberValue)
            throw InvalidOperatorException("${otherSchema.type()} cannot supported operator \"-\"")

        if(level() >= otherSchema.level())
            return IntValue(value!! - (otherSchema.value!! as Number).toInt())
        else return otherSchema.transfer(value!!).subValue(otherSchema)
    }

    override fun mulValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        if (otherSchema !is NumberValue)
            throw InvalidOperatorException("${otherSchema.type()} cannot supported operator \"*\"")

        if(level() >= otherSchema.level())
            return IntValue(value!! * (otherSchema.value!! as Number).toInt())
        else return otherSchema.transfer(value!!).mulValue(otherSchema)
    }

    override fun divValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        if (otherSchema !is NumberValue)
            throw InvalidOperatorException("${otherSchema.type()} cannot supported operator \"/\"")

        if(level() >= otherSchema.level())
            return IntValue(value!! /(otherSchema.value!! as Number).toInt())
        else return otherSchema.transfer(value!!).divValue(otherSchema)
    }
}