package com.virtualightning.fileresolver.environment

import com.virtualightning.fileresolver.entity.ByteData
import com.virtualightning.fileresolver.entity.RemainByteData
import com.virtualightning.fileresolver.exceptions.MethodException
import com.virtualightning.fileresolver.proxies.AbstractReadable
import sun.security.util.Length
import java.util.*
import kotlin.experimental.and


object SourceProxy {
    var callback : IFileReadableCallback? = null

    var isOpenFile = false

    var byteCounts : Int = 25
    var radix : String = "Hexadecimal"

    var source : AbstractReadable? = null
    var totalLength: Long = -1
    var currentPosition: Long = -1
    var remainBits : Int = -1
    var remainBitCount : Int = -1
    val byteArray = ByteArray(125)
    val byteDataArray = LinkedList<ByteData>()


    fun openSource(abstractReadable: AbstractReadable) : Boolean {
        closeSource()
        try {
            this.source = abstractReadable
            abstractReadable.init()
            totalLength = abstractReadable.getSize()


            if (totalLength != 0L) {
                remainBits = 0
                remainBitCount = 8
                currentPosition = 1
                readShowBytes(-1)
            } else {
                currentPosition = 0
                remainBits = -1
                remainBitCount = -1
            }

            callback!!.invoke(AbstractReadableCallbackCode.TOTAL_LENGTH, totalLength)
            callback!!.invoke(AbstractReadableCallbackCode.CURRENT_POSITION, currentPosition)
            callback!!.invoke(AbstractReadableCallbackCode.REMAIN_BIT_COUNT, remainBitCount)
            callback!!.invoke(AbstractReadableCallbackCode.REMAIN_BYTE_COUNT, totalLength)
            isOpenFile = true
            return true
        } catch (e : Exception) {
            e.printStackTrace()
            callback!!.invoke(AbstractReadableCallbackCode.ERROR,"Failed to open source")
            return false
        }
    }

    fun readBytes(byteArray: ByteArray,isMovePosition : Boolean = true) : Int {
        if(!isOpenFile)
            throw MethodException("Read failed ! Cause file closed")

        var remainSize = byteArray.size
        var totalReadLength = 0
        var curReadLength : Int
        do {
            curReadLength = this.source!!.readByteArray(byteArray,totalReadLength,remainSize)
            if(curReadLength == -1)
                break

            remainSize -= curReadLength
            totalReadLength += curReadLength
        } while (remainSize != 0)

        this.source!!.seek(currentPosition - 1)
        if(isMovePosition) {
            readShowBytes(currentPosition + totalReadLength.toLong())
            callback!!.invoke(AbstractReadableCallbackCode.CURRENT_POSITION, currentPosition)
            callback!!.invoke(AbstractReadableCallbackCode.REMAIN_BYTE_COUNT, totalLength - currentPosition)
        }

        return totalReadLength
    }


    fun seekPosition(newPosition: Long) {
        if(!isOpenFile)
            throw MethodException("Seek failed ! Cause file closed")

        if(newPosition >= totalLength || newPosition < 0)
            throw MethodException("Seek failed ! Cause new position is over than total length")

        this.source!!.seek(newPosition)
        currentPosition = newPosition - 1
    }

    private fun readShowBytes(newPosition : Long) {
        val abstractReadable = this.source!!

        if(currentPosition == newPosition) {
            if(remainBitCount != 8) {
                byteDataArray[0] = RemainByteData(currentPosition - 1, remainBits, remainBitCount)
            }
        }
        else {
            var movePosition : Int
            var position : Long = newPosition

            if(position == -1L) {
                position = 1
                movePosition = 125
            }
            else {
                movePosition = (position - currentPosition).toInt()
                if(movePosition > 125)
                    movePosition = 125
            }

            if(movePosition >= byteDataArray.size)
                byteDataArray.clear()
            else (0 until movePosition).forEach { byteDataArray.poll() }


            if(remainBitCount != 8) {
                movePosition --
                byteDataArray[0] = RemainByteData(position, remainBits, remainBitCount)
            }

            val readSize = abstractReadable.readByteArray(byteArray,0,movePosition)
            (0 until readSize).forEach {
                val byteData = ByteData(position++ - 1, byteArray[it].toInt() and 0xFF)
                byteData.changeRadix(radix)
                byteDataArray.add(byteData)
            }
        }

        if(newPosition != -1L) {
            abstractReadable.seek(newPosition - 1)
            currentPosition = newPosition
        } else abstractReadable.seek(0)


        //通知更新
        callback!!.invoke(AbstractReadableCallbackCode.UPDATE_DATA_TABLE, Unit)
    }

    private fun composeBit(remainBit : Int,newByte : Int) : Int {
        var compositeByte = remainBitFlagArray[8 - remainBitCount] and newByte
        compositeByte = (compositeByte ushr remainBitCount) or (remainBit shl (8 - remainBitCount))
        return compositeByte
    }

    private fun cutBit(newByte: Int) : Int {
        return remainBitFlagArray[remainBitCount] and newByte
    }





    fun closeSource() {
        isOpenFile = false
        byteDataArray.clear()
        if(this.source != null) {
            this.source!!.close()
            this.source = null
        }
        this.callback!!.invoke(AbstractReadableCallbackCode.CLEAR,Unit)
    }
}