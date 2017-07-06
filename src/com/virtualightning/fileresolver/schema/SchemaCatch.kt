package com.virtualightning.fileresolver.schema

import com.virtualightning.fileresolver.environment.commaCharInt
import com.virtualightning.fileresolver.environment.eofCharInt
import com.virtualightning.fileresolver.environment.spaceCharInt
import com.virtualightning.fileresolver.tools.CustomStringStream

object SchemaCatch {
    var breakPoint : Int = eofCharInt
    var matchStr = ""

    fun catchSchema(reader : CustomStringStream) : BaseSchema? {
        matchStr = ""
        breakPoint = eofCharInt
        var curNode = SchemaTree.rootNode
        var isGotted = false
        var isSpaced = false
        loop@ while (true) {
            breakPoint = reader.read()
            when(breakPoint) {
                spaceCharInt-> {
                    if(isGotted)
                        isSpaced = true
                    continue@loop
                }
                commaCharInt->break@loop
                eofCharInt->break@loop
                else -> {
                    if(isSpaced) {
                        reader.back()
                        break@loop
                    }

                    isGotted = true
                    var nextNode = curNode.childMap[breakPoint]
                    if(nextNode == null) {
                        isGotted = false
                        break@loop
                    }
                    curNode = nextNode
                    matchStr += breakPoint.toChar()
                }
            }
        }

        if(isGotted)
            return curNode.schema
        else return null
    }
}