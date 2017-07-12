package com.virtualightning.fileresolver.proxies

import com.virtualightning.fileresolver.entity.ByteData
import com.virtualightning.fileresolver.environment.IFileReadableCallback


abstract class AbstractReadable {
    abstract fun init()
    abstract fun getSize() : Long
    abstract fun readByte(location : Long) : Byte
    abstract fun readByteArray(location : Long,byteArray: ByteArray,offset : Long,length:Long) : Long
    abstract fun close()
}