package com.virtualightning.fileresolver.interfaces

import com.virtualightning.fileresolver.schema.BaseSchema
import com.virtualightning.fileresolver.tools.CustomStringStream
import java.io.File
import java.io.StringReader

typealias ICallback<E> =  (Boolean,E?,String?) -> Unit
object NullCallback : ICallback<Any> {
    override fun invoke(p1: Boolean, p2: Any?, p3: String?) {
    }

}

typealias IFilter =  (file : File?)->Boolean




typealias IResolverCallback = (Int,Array<Any>?) -> Unit
typealias IResolver = (reader : CustomStringStream) -> Boolean
typealias IOperator =