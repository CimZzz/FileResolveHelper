package com.virtualightning.fileresolver.proxies

import java.io.File
import java.io.RandomAccessFile


class FileReadable(val file : File) : AbstractReadable {
    var inputStream : RandomAccessFile? = null
    var totalLength : Long = 0

    override fun init() {
        totalLength = file.length()
        inputStream = RandomAccessFile(file,"r")
    }

    override fun getSize(): Long = totalLength

    override fun seek(position: Long) {
        inputStream!!.seek(position)
    }

    override fun readByte(): Int {
        return inputStream!!.read()
    }

    override fun readByteArray(byteArray: ByteArray, offset: Int, length: Int): Int {
        return inputStream!!.read(byteArray,offset,length)
    }

    override fun close() {
        if(inputStream != null) {
            inputStream!!.close()
            inputStream = null
            totalLength = 0
        }
    }
}