package com.virtualightning.fileresolver.schema.operator

import com.virtualightning.fileresolver.schema.BaseSchema

abstract class OperatorSchema(name : String) : BaseSchema(name) {
    abstract fun operator(leftSchema : BaseSchema, rightSchema : BaseSchema? = null) : BaseSchema?
}