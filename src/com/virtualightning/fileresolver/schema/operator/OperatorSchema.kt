package com.virtualightning.fileresolver.schema.operator

import com.virtualightning.fileresolver.schema.BaseSchema
import com.virtualightning.fileresolver.schema.values.ValueSchema

abstract class OperatorSchema(name : String) : BaseSchema(name) {
    abstract fun operator(leftSchema : ValueSchema<*>, rightSchema : ValueSchema<*>) : ValueSchema<*>
    abstract fun priority() : Int
}