package com.virtualightning.fileresolver.environment

import com.virtualightning.fileresolver.entity.ByteData
import java.io.*


object Context {
    var isOpenFile = false





    var byteCounts : Int = 25
    var radix : String = "Hexadecimal"
    var selectFile : File? = null
    var inputStream : RandomAccessFile? = null
    var totalBytes : Long = -1
    var currentBytes : Long = -1
    var remainBits : Int = -1
    var remainBitCount : Int = -1

    @JvmStatic
    fun openFile() : Boolean {
        try {
            inputStream = RandomAccessFile(selectFile,"r")
            currentBytes = 1
            totalBytes = selectFile!!.length()
            return true
        } catch (e : Exception) {
            return false
        }
    }

    fun readBytes() : ArrayList<ByteData>? {
        val newList = ArrayList<ByteData>(byteCounts)
        try {
            for (i in 1..byteCounts) {
                var tempByte = inputStream!!.read()
                if(tempByte == -1)
                    return newList
                if(remainBits != -1) {
                    val nextRemainBits = remainBitFlagArray[remainBitCount - 1] and tempByte
                    tempByte = (tempByte ushr remainBitCount) or (remainBits shl (8 - remainBitCount))
                    remainBits = nextRemainBits
                }
                newList.add(ByteData(currentBytes,tempByte))
                currentBytes ++
            }
        } catch (e : Exception) {
            return null
        }

        return newList
    }

}