package com.virtualightning.fileresolver.schema

import com.virtualightning.fileresolver.interfaces.IMethodCallback
import com.virtualightning.fileresolver.schema.values.ValueSchema

class MethodSchema(name : String,val callback : IMethodCallback) : BaseSchema(name) {
    fun invoke(valueArr : MutableList<ValueSchema<*>>) : ValueSchema<*> = callback.invoke(valueArr)
}