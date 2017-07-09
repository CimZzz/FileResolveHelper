package com.virtualightning.fileresolver.schema.operator

import com.virtualightning.fileresolver.schema.base.OperatorSchema
import com.virtualightning.fileresolver.schema.base.ValueSchema


object DivideSchema : OperatorSchema("/") {

    override fun priority(): Int = 2

    override fun operator(leftSchema: ValueSchema<*>, rightSchema: ValueSchema<*>): ValueSchema<*> {
        return leftSchema.divValue(rightSchema)
    }
}