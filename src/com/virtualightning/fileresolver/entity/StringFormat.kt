package com.virtualightning.fileresolver.entity

import com.virtualightning.fileresolver.environment.TYPE_STRING
import java.io.DataInputStream
import java.io.DataOutputStream

class StringFormat(name : String) : Format<String>(name, TYPE_STRING) {
    var charsetIndex : Byte = 0
    var length : Int = 0

    override fun readDataFromBytes(byteArr: ByteArray, offset: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sizeOfData(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    override fun writeToFile(outputStream: DataOutputStream) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readFromFile(inputStream: DataInputStream) {
        charsetIndex = inputStream.readByte()
        val isNeedContract = inputStream.readBoolean()
        if(isNeedContract) {

        }
        else length = inputStream.readInt()
    }
}