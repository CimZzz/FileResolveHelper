package com.virtualightning.fileresolver.tools

import com.virtualightning.fileresolver.schema.base.ComputableSchema
import com.virtualightning.fileresolver.schema.members.ReturnMethodSchema
import java.util.*

data class SyntaxTool(
        val schemaStack : LinkedList<ComputableSchema> = LinkedList<ComputableSchema>(),
        var leftBracketCount : Int = 0,
        var argCount: Int = 0,
        var methodSchema: ReturnMethodSchema? = null
) {
    fun init() {
        schemaStack.clear()
        leftBracketCount = 0
        argCount = 0
    }
}