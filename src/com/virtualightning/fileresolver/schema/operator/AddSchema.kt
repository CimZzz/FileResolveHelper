package com.virtualightning.fileresolver.schema.operator

import com.virtualightning.fileresolver.schema.base.OperatorSchema
import com.virtualightning.fileresolver.schema.base.ValueSchema


object AddSchema : OperatorSchema("+") {
    override fun priority(): Int = 1

    override fun operator(leftSchema: ValueSchema<*>, rightSchema: ValueSchema<*>): ValueSchema<*> {
        return leftSchema.addValue(rightSchema)
    }
}