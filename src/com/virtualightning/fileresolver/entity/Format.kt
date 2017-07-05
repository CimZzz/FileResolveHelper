package com.virtualightning.fileresolver.entity

import com.virtualightning.fileresolver.environment.TYPE_BLOCK
import com.virtualightning.fileresolver.environment.TYPE_STRING
import java.io.DataInputStream
import java.io.DataOutputStream
import java.nio.charset.Charset

abstract class Format<E> (val formatName: String, val type : Byte) {
    val formatNode : LeftTreeNode = LeftTreeNode(formatName, type)
    var e : E? = null

    abstract fun readDataFromBytes(byteArr : ByteArray,offset : Int)
    abstract fun sizeOfData() : Int
    abstract fun typeName() : String
    abstract fun writeToFile(outputStream: DataOutputStream)
    abstract fun readFromFile(inputStream: DataInputStream)
}

fun ReadFormatFromStream(inputStream : DataInputStream) : Format<*> {
    val nameSize = inputStream.readInt()
    val name = String(inputStream.readBytes(nameSize), Charset.forName("utf-8"))
    val formatType = inputStream.readByte()

    var format : Format<*>? = null

    when(formatType) {
        TYPE_STRING->format = StringFormat(name)
    }

    if(format == null)
        throw Exception("Unsupported format type : $formatType")

    format.readFromFile(inputStream)
    return format
}