package com.virtualightning.fileresolver.proxies

import com.virtualightning.fileresolver.entity.ByteData
import com.virtualightning.fileresolver.environment.AbstractReadableCallbackCode
import com.virtualightning.fileresolver.environment.IFileReadableCallback
import com.virtualightning.fileresolver.environment.remainBitFlagArray
import com.virtualightning.fileresolver.exceptions.MethodException
import com.virtualightning.fileresolver.utils.Warn
import java.io.RandomAccessFile


class ReadableFile(path : String,callback: IFileReadableCallback) : AbstractReadable(path,callback) {

    var accessFile : RandomAccessFile? = null

    override fun init() {
        try {
            accessFile = RandomAccessFile(path, "r")
            currentPosition = 1
            totalLength = accessFile!!.length()
            remainBitCount = 8
            remainBits = 0

            callback.invoke(AbstractReadableCallbackCode.TOTAL_LENGTH, totalLength)
            callback.invoke(AbstractReadableCallbackCode.CURRENT_POSITION, currentPosition)
            callback.invoke(AbstractReadableCallbackCode.REMAIN_BIT_COUNT, remainBitCount)
            callback.invoke(AbstractReadableCallbackCode.REMAIN_BYTE_COUNT, totalLength)

        } catch (e : Exception) {
            Warn("Open file failed : $e : ${e.message}")
            throw MethodException("Open file failed : $path")
        }
    }

    override fun readByte(wannaMovePosition : Boolean) : Int {
        if(currentPosition > totalLength)
            throw MethodException("Reached the end of file")

        val file = accessFile!!
        val byte : Int

        if(remainBitCount != 8) {
            if(currentPosition == totalLength) {
                byte = remainBits

                if(wannaMovePosition) {
                    currentPosition ++
                    callback.invoke(AbstractReadableCallbackCode.CURRENT_POSITION, -1)
                    callback.invoke(AbstractReadableCallbackCode.REMAIN_BIT_COUNT, 0)
                    callback.invoke(AbstractReadableCallbackCode.REMAIN_BYTE_COUNT, 0)
                }
            }
            else {
                file.seek(currentPosition)
                val tempByte = file.read()
                byte = composeBit(remainBits,tempByte)

                if(wannaMovePosition) {
                    remainBits = cutBit(tempByte)
                    currentPosition ++
                    callback.invoke(AbstractReadableCallbackCode.CURRENT_POSITION, currentPosition)
                    callback.invoke(AbstractReadableCallbackCode.REMAIN_BYTE_COUNT, totalLength - currentPosition + 1)
                }
            }
        } else {
            file.seek(currentPosition - 1)
            byte = file.read()

            if(wannaMovePosition) {
                currentPosition ++
                callback.invoke(AbstractReadableCallbackCode.CURRENT_POSITION, currentPosition)
                callback.invoke(AbstractReadableCallbackCode.REMAIN_BYTE_COUNT, totalLength - currentPosition + 1)
            }
        }

        return byte
    }

    override fun readByteArray(byteArray: ArrayList<ByteData>, length:Long, wannaMovePosition : Boolean) : Long {
        if(currentPosition > totalLength)
            return 0

        val file = accessFile!!
        file.seek(currentPosition - 1)

        return 0
    }

    override fun seek(position: Long) {
        val file = accessFile!!
        if(position > totalLength)
            throw MethodException("Cannot seek file position over than file length , file length : $totalLength , expect file postion : $position")
        else if(position < 1)
            throw MethodException("Wrong file position : $position")
        file.seek(position - 1)
        currentPosition = position
        remainBitCount = 8
        remainBits = 0
        callback.invoke(AbstractReadableCallbackCode.CURRENT_POSITION, currentPosition)
        callback.invoke(AbstractReadableCallbackCode.REMAIN_BIT_COUNT, remainBitCount)
    }

    override fun close() {
        val file = accessFile

        if(file != null) {
            file.close()
            accessFile = null
        }

        callback.invoke(AbstractReadableCallbackCode.CLEAR, Unit)
    }

    private fun composeBit(remainBit : Int,newByte : Int) : Int {
        var compositeByte = remainBitFlagArray[8 - remainBitCount] and newByte
        compositeByte = (compositeByte ushr remainBitCount) or (remainBit shl (8 - remainBitCount))
        return compositeByte
    }

    private fun cutBit(newByte: Int) : Int {
        return remainBitFlagArray[remainBitCount] and newByte
    }
}