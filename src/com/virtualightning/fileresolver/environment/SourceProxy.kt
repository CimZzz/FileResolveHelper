package com.virtualightning.fileresolver.environment

import com.virtualightning.fileresolver.entity.ByteData
import com.virtualightning.fileresolver.entity.BitData
import com.virtualightning.fileresolver.exceptions.MethodException
import com.virtualightning.fileresolver.proxies.AbstractReadable
import java.util.*


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
    var bufferSize = 0


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
                readShowBytes(1,125L)
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
        this.source!!.seek(currentPosition - 1)
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

        return totalReadLength
    }


    fun seekPosition(newPosition: Long) : Long {
        if(!isOpenFile)
            throw MethodException("Seek failed ! Cause file closed")

        if(newPosition > totalLength || newPosition < 0)
            throw MethodException("Seek failed ! Cause new position is over than total length")

        this.source!!.seek(newPosition - 1)
        return newPosition - currentPosition
    }



    fun updatePosition(movePosition: Long,isMovePosition: Boolean) {
        if(!isMovePosition) {
            this.source!!.seek(currentPosition - 1)
            return
        }


        val newPosition = currentPosition + movePosition

        if(newPosition > totalLength + 1 || newPosition <= 0)
            throw MethodException("Wrong position : $newPosition")

        readShowBytes(newPosition,movePosition)
        currentPosition = newPosition
        callback!!.invoke(AbstractReadableCallbackCode.CURRENT_POSITION, currentPosition)
        callback!!.invoke(AbstractReadableCallbackCode.REMAIN_BYTE_COUNT, totalLength - currentPosition)
    }

    private fun readShowBytes(currentPosition : Long,movePosition : Long) {
        val tempByteArray = ByteArray(125)
        var tempBufferSize : Int = bufferSize
        when(movePosition) {
            0L -> {
                if(remainBitCount != 8) {
                    byteArray[0] = remainBits.toByte()
                    ByteDataCache.cacheByteData(byteDataArray.poll())
                    byteDataArray.push(BitData(currentPosition, remainBits, remainBitCount))
                }
            }
            else -> {
                if(movePosition >= 125  || movePosition <= -125) {
                    tempBufferSize = this.source!!.readByteArray(tempByteArray,0, tempByteArray.size)
                } else {
                    val intPosition = movePosition.toInt()
                    if(movePosition > 0) {
                        tempBufferSize -= intPosition
                        System.arraycopy(byteArray, intPosition, tempByteArray, 0, tempBufferSize)
                        if (tempBufferSize + currentPosition < totalLength)
                            tempBufferSize += this.source!!.readByteArray(tempByteArray, tempBufferSize, 125 - tempBufferSize)
                    } else {
                        tempBufferSize += intPosition
                        System.arraycopy(byteArray, 0, tempByteArray, -intPosition, tempBufferSize)
                        tempBufferSize += this.source!!.readByteArray(tempByteArray, 0, 125 - tempBufferSize)
                    }
                }

                this.source!!.seek(currentPosition - 1)
            }
        }

        if(movePosition != 0L) {
            byteDataArray.forEach {
                ByteDataCache.cacheByteData(it)
            }
            byteDataArray.clear()
            (0 until tempBufferSize).forEach {
                val byteData = ByteDataCache.getByteData()
                byteData.byte = tempByteArray[it].toInt() and 0xFF
                byteData.location = currentPosition + it
                byteData.locationStr = null
                byteData.ascii = null
                byteData.changeRadix(radix)
                byteDataArray.add(byteData)
            }
            System.arraycopy(tempByteArray,0, byteArray,0,tempBufferSize)
            bufferSize = tempBufferSize
        }
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