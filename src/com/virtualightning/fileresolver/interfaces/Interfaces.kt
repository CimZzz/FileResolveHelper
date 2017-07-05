package com.virtualightning.fileresolver.interfaces

import java.io.File

typealias ICallback<E> =  (Boolean,E?,String?) -> Unit
object NullCallback : ICallback<Any> {
    override fun invoke(p1: Boolean, p2: Any?, p3: String?) {
    }

}

typealias IFilter =  (file : File?)->Boolean

typealias