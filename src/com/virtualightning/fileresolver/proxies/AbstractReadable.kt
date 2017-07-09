package com.virtualightning.fileresolver.proxies

import com.virtualightning.fileresolver.entity.ByteData
import com.virtualightning.fileresolver.environment.IFileReadableCallback


abstract class AbstractReadable(val path : String,val callback : IFileReadableCallback) {
    var totalLength : Long = -1
    var currentPosition : Long = -1
    var remainBits : Int = -1
    var remainBitCount : Int = -1


    abstract fun init()
    abstract fun readByte(wannaMovePosition : Boolean = false) : Int
    abstract fun readByteArray(byteArray: ArrayList<ByteData>,length:Long,wannaMovePosition : Boolean = false)
    abstract fun seek(position : Long)
    abstract fun close()
}