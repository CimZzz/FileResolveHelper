package com.virtualightning.fileresolver.entity

import com.virtualightning.fileresolver.environment.locationCompletionArray

class ByteData(
        val location : Long,
        val byte : Int
) {
    var locationStr : String? = null
    get() {
        if(field == null) {
            val str = java.lang.Long.toHexString(location)
            var length = str.length
            if(length > 8)
                length = 7
            else length -= 1

            field = "0x${locationCompletionArray[length]}$str"
        }
        return field
    }
    var byteStr : String? = null
    get() {
        if(field == null) {
            val str = java.lang.Integer.toHexString(byte)
            if(str.length < 2)
                field = "0x0$str"
            else field = "0x$str"


        }
        return field
    }
    var ascii : Char? = null
    get() {
        if(field == null) {
            field = byte.toChar()
        }
        return field
    }
}