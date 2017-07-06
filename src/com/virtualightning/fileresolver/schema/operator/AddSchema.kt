package com.virtualightning.fileresolver.schema.operator

import com.virtualightning.fileresolver.schema.BaseSchema


object AddSchema : OperatorSchema("+") {
    override fun operator(leftSchema: BaseSchema, rightSchema: BaseSchema?): BaseSchema? {

    }
}