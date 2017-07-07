package com.virtualightning.fileresolver.tools

import com.virtualightning.fileresolver.schema.BaseSchema
import com.virtualightning.fileresolver.schema.MethodSchema
import java.util.*

data class SyntaxTool (
    val schemaStack : LinkedList<BaseSchema> = LinkedList<BaseSchema>(),
    var leftBracketCount : Int = 0,
    var argCount: Int = 0,
    var methodSchema: MethodSchema? = null
) {
    fun init() {
        schemaStack.clear()
        leftBracketCount = 0
        argCount = 0
    }
}