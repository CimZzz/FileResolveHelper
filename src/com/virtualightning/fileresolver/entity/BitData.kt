package com.virtualightning.fileresolver.entity

import com.virtualightning.fileresolver.environment.completionArray


class BitData(location : Long , byte : Int, remainSize : Int) : ByteData(location,byte) {
    init{
        ascii = ' '
        var str = java.lang.Integer.toBinaryString(byte)
        str = "${completionArray[str.length]}$str"
        byteStr = str.substring(8 - remainSize)
    }

    override fun changeRadix(radix: String) {

    }
}