package com.virtualightning.fileresolver.environment

import com.virtualightning.fileresolver.entity.ByteData
import com.virtualightning.fileresolver.entity.RemainByteData
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


    fun openSource(abstractReadable: AbstractReadable) {
        this.source = abstractReadable
        abstractReadable.init()
        totalLength = abstractReadable.getSize()

        if(totalLength != 0L) {
            currentPosition = 0
            remainBits = 0
            remainBitCount = 8
            readShowBytes(1)
            currentPosition = 1
        } else {
            currentPosition = 0
            remainBits = -1
            remainBitCount = -1
        }

        callback!!.invoke(AbstractReadableCallbackCode.TOTAL_LENGTH, totalLength)
        callback!!.invoke(AbstractReadableCallbackCode.CURRENT_POSITION, currentPosition)
        callback!!.invoke(AbstractReadableCallbackCode.REMAIN_BIT_COUNT, remainBitCount)
        callback!!.invoke(AbstractReadableCallbackCode.REMAIN_BYTE_COUNT, totalLength)
    }

    fun readShowBytes(newPosition : Long) {
        val abstractReadable = this.source!!

        if(currentPosition == newPosition) {
            if(remainBitCount != 8) {
                byteDataArray[0] = RemainByteData(currentPosition, remainBits, remainBitCount)
            }
        }
        else {
            var movePosition = newPosition - currentPosition

            if(movePosition > 125)
                movePosition = 125L

            if(movePosition >= byteDataArray.size)
                byteDataArray.clear()
            else (0 until movePosition).forEach { byteDataArray.poll() }


            if(remainBitCount != 8) {
                movePosition --
                byteDataArray[0] = RemainByteData(newPosition, remainBits, remainBitCount)
            }

            var tempNewPosition = newPosition
            val readSize = abstractReadable.readByteArray(newPosition - 1, byteArray,0,movePosition)
            (0 until readSize).forEach {
                val byteData = ByteData(tempNewPosition++, byteArray[it.toInt()].toInt())
                byteData.changeRadix(radix)
                byteDataArray.add(byteData)
            }
        }

        //通知更新
        callback!!.invoke(AbstractReadableCallbackCode.UPDATE_DATA_TABLE, byteDataArray)
    }
}