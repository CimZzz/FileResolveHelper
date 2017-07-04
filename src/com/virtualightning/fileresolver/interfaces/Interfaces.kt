package com.virtualightning.fileresolver.interfaces

import java.io.File

typealias ICallback<E> =  (Boolean,E?,String?) -> Unit
typealias IFilter =  (file : File?)->Boolean