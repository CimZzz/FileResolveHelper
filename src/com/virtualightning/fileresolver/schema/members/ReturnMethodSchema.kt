package com.virtualightning.fileresolver.schema.members

import com.virtualightning.fileresolver.environment.IMethodCallback
import com.virtualightning.fileresolver.schema.base.MethodSchema
import com.virtualightning.fileresolver.schema.base.ValueSchema

class ReturnMethodSchema(name : String, val callback : IMethodCallback) : MethodSchema(name) {
    fun invoke(valueArr : MutableList<ValueSchema<*>>) : ValueSchema<*> = callback.invoke(this,valueArr)
}