package com.virtualightning.fileresolver.environment

import com.virtualightning.fileresolver.entity.ByteData
import com.virtualightning.fileresolver.entity.Protocol
import com.virtualightning.fileresolver.utils.Wran
import java.io.*


object Context {
    var isOpenFile = false
    var isHasProtocol = false



    var protocol : Protocol? = null


    var byteCounts : Int = 25
    var radix : String = "Hexadecimal"
    var selectFile : File? = null
    var inputStream : RandomAccessFile? = null
    var totalBytes : Long = -1
    var currentBytes : Long = -1
    var remainBits : Int = -1
    var remainBitCount : Int = -1


    @JvmStatic
    fun openFile(selectFile : File) : Boolean {
        try {
            this.selectFile = selectFile
            inputStream = RandomAccessFile(selectFile,"r")
            currentBytes = 1
            totalBytes = selectFile.length()
            isOpenFile = true
            return true
        } catch (e : Exception) {
            this.selectFile = null
            inputStream = null
            Wran(e)
            return false
        }
    }

    @JvmStatic
    fun readBytes() : ArrayList<ByteData>? {
        inputStream!!.seek(currentBytes - 1)
        var currentBytesTemp = currentBytes
        val newList = ArrayList<ByteData>(byteCounts)
        try {
            for (i in 1..byteCounts) {
                var tempByte = inputStream!!.read()
                if(tempByte == -1)
                    break
                if(remainBits != -1) {
                    val nextRemainBits = remainBitFlagArray[remainBitCount - 1] and tempByte
                    tempByte = (tempByte ushr remainBitCount) or (remainBits shl (8 - remainBitCount))
                    remainBits = nextRemainBits
                }
                newList.add(ByteData(currentBytesTemp,tempByte))
                currentBytesTemp ++
            }
        } catch (e : Exception) {
            return null
        }

        return newList
    }

    @JvmStatic
    fun closeFile() {
        try{
            isOpenFile = false
            selectFile = null
            totalBytes = -1
            currentBytes = -1
            remainBits = -1
            remainBitCount = -1
            if(inputStream != null) {
                inputStream!!.close()
                inputStream = null
            }
        } catch (e : Exception) {
            Wran(e)
        }
    }





    @JvmStatic
    fun createNewProtocol(protocol : Protocol) {
        this.protocol = protocol
        isHasProtocol = true
    }

    @JvmStatic
    fun openProtocol(file : File) {

    }

    @JvmStatic
    fun resetProtocol() {
        this.protocol!!.clearData()
    }

    @JvmStatic
    fun closeProtocol() {
        if(this.protocol == null)
            return

        this.protocol!!.destroy()
        isHasProtocol = true
    }
}