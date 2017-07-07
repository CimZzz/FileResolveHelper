package com.virtualightning.fileresolver.schema.operator

import com.virtualightning.fileresolver.schema.values.ValueSchema


object DivideSchema : OperatorSchema("/") {

    override fun priority(): Int = 2

    override fun operator(leftSchema: ValueSchema<*>, rightSchema: ValueSchema<*>): ValueSchema<*> {
        return leftSchema.divValue(rightSchema)
    }
}