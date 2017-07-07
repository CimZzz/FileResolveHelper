package com.virtualightning.fileresolver.schema.operator

import com.virtualightning.fileresolver.schema.values.StringValue
import com.virtualightning.fileresolver.schema.values.ValueSchema


object AddSchema : OperatorSchema("+") {
    override fun priority(): Int = 1

    override fun operator(leftSchema: ValueSchema<*>, rightSchema: ValueSchema<*>): ValueSchema<*> {
        return leftSchema.addValue(rightSchema)
    }
}