package com.virtualightning.fileresolver.schema.members

import com.virtualightning.fileresolver.environment.INonReturnMethodCallback
import com.virtualightning.fileresolver.schema.base.MethodSchema
import com.virtualightning.fileresolver.schema.base.ValueSchema


class NonReturnMethodSchema(name : String,val callback : INonReturnMethodCallback) : MethodSchema(name) {
    fun invoke(valueArr : MutableList<ValueSchema<*>>) : Any? = callback.invoke(this,valueArr)
}