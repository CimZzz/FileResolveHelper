package com.virtualightning.fileresolver.environment

import com.virtualightning.fileresolver.entity.ByteData
import com.virtualightning.fileresolver.entity.Protocol
import com.virtualightning.fileresolver.utils.Warn
import java.io.*
import java.util.*


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
    var currentByteDataArr = ArrayList<ByteData>(125)



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
            Warn(e)
            return false
        }
    }

    @JvmStatic
    fun readBytes() : Boolean {
        inputStream!!.seek(currentBytes - 1)
        currentByteDataArr.clear()
        var currentBytesTemp = currentBytes
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
                val byteData = ByteData(currentBytesTemp,tempByte)
                byteData.changeRadix(radix)
                currentByteDataArr.add(byteData)
                currentBytesTemp ++
            }

            return true
        } catch (e : Exception) {
            return false
        }
    }

    @JvmStatic
    fun changeByteCount(count : Int) {

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
            Warn(e)
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

    object QuickCommand {
    }

    object ByteSource {

    }
}