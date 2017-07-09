package com.virtualightning.fileresolver.schema.values

import com.virtualightning.fileresolver.schema.base.ValueSchema
import com.virtualightning.fileresolver.exceptions.InvalidOperatorException

class StringValue(value : String? = null) : ValueSchema<String>(value) {
    override fun type(): String = "String"

    override fun addValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        return StringValue(value + otherSchema.toString())
    }

    override fun subValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        throw InvalidOperatorException("String cannot supported operator \"-\"")
    }

    override fun mulValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        throw InvalidOperatorException("String cannot supported operator \"*\"")
    }

    override fun divValue(otherSchema: ValueSchema<*>): ValueSchema<*> {
        throw InvalidOperatorException("String cannot supported operator \"/\"")
    }

    override fun level(): Int = -1

    override fun transfer(value: Any): ValueSchema<*> = StringValue()

}