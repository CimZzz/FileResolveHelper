package com.virtualightning.fileresolver.proxies



interface AbstractReadable {
    fun init()
    fun getSize() : Long
    fun seek(position : Long)
    fun readByte() : Int
    fun readByteArray(byteArray: ByteArray,offset : Int,length:Int) : Int
    fun close()
}