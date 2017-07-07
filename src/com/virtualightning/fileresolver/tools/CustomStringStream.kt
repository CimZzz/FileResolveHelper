package com.virtualightning.fileresolver.tools

import com.virtualightning.fileresolver.environment.eofCharInt
import java.io.StringReader

class CustomStringStream(val source : String){
    var curIndex = 0


    fun read() : Int = if(curIndex >= source.length) eofCharInt else source[curIndex++].toInt()

    fun isEnd() : Boolean = curIndex >= source.length

    fun back() {
        curIndex --
    }
}