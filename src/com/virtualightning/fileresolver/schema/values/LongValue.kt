package com.virtualightning.fileresolver.schema.values

import com.virtualightning.fileresolver.schema.exceptions.InvalidOperatorException


class LongValue(value : Long) : NumberValue<Long>(value) {
    override fun type(): String = "Long"

    override fun transfer(value: Any): ValueSchema<*> = LongValue(value as Long)

    override fun level(): Int = 20

    override fun addValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        if (otherSchema !is NumberValue)
            throw InvalidOperatorException("${otherSchema.type()} cannot supported operator \"+\"")

        if(level() >= otherSchema.level())
            return LongValue(value!! + otherSchema.value!! as Long)
        else return otherSchema.transfer(value!!).addValue(otherSchema)
    }

    override fun subValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        if (otherSchema !is NumberValue)
            throw InvalidOperatorException("${otherSchema.type()} cannot supported operator \"+\"")

        if(level() >= otherSchema.level())
            return LongValue(value!! - otherSchema.value!! as Long)
        else return otherSchema.transfer(value!!).subValue(otherSchema)
    }

    override fun mulValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        if (otherSchema !is NumberValue)
            throw InvalidOperatorException("${otherSchema.type()} cannot supported operator \"*\"")

        if(level() >= otherSchema.level())
            return LongValue(value!! * otherSchema.value!! as Long)
        else return otherSchema.transfer(value!!).mulValue(otherSchema)
    }

    override fun divValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        if (otherSchema !is NumberValue)
            throw InvalidOperatorException("${otherSchema.type()} cannot supported operator \"/\"")

        if(level() >= otherSchema.level())
            return LongValue(value!! / otherSchema.value!! as Long)
        else return otherSchema.transfer(value!!).divValue(otherSchema)
    }
}