package com.virtualightning.fileresolver.entity

import com.virtualightning.fileresolver.environment.completionArray


open class ByteData(
        var location : Long = 0,
        var byte : Int = 0
) {
    var locationStr : String? = null
    get() {
        if(field == null) {
            val str = java.lang.Long.toHexString(location)
            var length = str.length
            if(length > 8)
                length = 7
            else length -= 1

            field = "0x${completionArray[length]}$str"
        }
        return field
    }
    var byteStr : String? = null
    var ascii : Char? = null
    get() {
        if(field == null) {
            field = byte.toChar()
        }
        return field
    }

    open fun changeRadix(radix : String) {
        val str : String
        when(radix) {
            "Binary"-> {
                str = java.lang.Integer.toBinaryString(byte)
                byteStr = "${completionArray[str.length]}$str"
            }
            "Decimal"-> {
                str = byte.toString()
                byteStr = "${completionArray[5 + str.length]}$str"
            }
            "Hexadecimal"-> {
                str = java.lang.Integer.toHexString(byte)
                if (str.length < 2)
                    byteStr = "0x0$str"
                else byteStr = "0x$str"
            }
        }
    }
}