package com.virtualightning.fileresolver.schema.values

import com.virtualightning.fileresolver.schema.base.ValueSchema
import com.virtualightning.fileresolver.exceptions.InvalidOperatorException


class DoubleValue(value : Double) : NumberValue<Double>(value) {
    override fun type(): String = "Double"

    override fun transfer(value: Any): ValueSchema<*> = DoubleValue(value as Double)

    override fun level(): Int = 30

    override fun addValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        if (otherSchema !is NumberValue)
            throw InvalidOperatorException("${otherSchema.type()} cannot supported operator \"+\"")

        if(level() >= otherSchema.level())
            return DoubleValue(value!! + (otherSchema.value!! as Number).toDouble() )
        else return otherSchema.transfer(value!!).addValue(otherSchema)
    }

    override fun subValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        if (otherSchema !is NumberValue)
            throw InvalidOperatorException("${otherSchema.type()} cannot supported operator \"-\"")

        if(level() >= otherSchema.level())
            return DoubleValue(value!! - (otherSchema.value!! as Number).toDouble())
        else return otherSchema.transfer(value!!).subValue(otherSchema)
    }

    override fun mulValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        if (otherSchema !is NumberValue)
            throw InvalidOperatorException("${otherSchema.type()} cannot supported operator \"*\"")

        if(level() >= otherSchema.level())
            return DoubleValue(value!! * (otherSchema.value!! as Number).toDouble())
        else return otherSchema.transfer(value!!).mulValue(otherSchema)
    }

    override fun divValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        if (otherSchema !is NumberValue)
            throw InvalidOperatorException("${otherSchema.type()} cannot supported operator \"/\"")

        if(level() >= otherSchema.level())
            return DoubleValue(value!! / (otherSchema.value!! as Number).toDouble())
        else return otherSchema.transfer(value!!).divValue(otherSchema)
    }
}