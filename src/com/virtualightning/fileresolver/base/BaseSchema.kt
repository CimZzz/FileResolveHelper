package com.virtualightning.fileresolver.base

import com.virtualightning.fileresolver.entity.Format
import com.virtualightning.fileresolver.environment.spaceCharInt
import com.virtualightning.fileresolver.environment.tabCharInt
import com.virtualightning.fileresolver.environment.wrapCharInt
import com.virtualightning.fileresolver.schema.SchemaGroup
import java.io.StringReader

abstract class BaseSchema(val schemaName : String) {


    open fun resolveStream(reader : StringReader) : Format<*>? = null

    fun skipSpaceWithChar(reader: StringReader,char : Char) : Boolean {
        var readChar : Int
        do {
            readChar = reader.read()

            if(readChar != wrapCharInt
                    &&readChar != tabCharInt
                    &&readChar != spaceCharInt)
                break

        } while (readChar != -1)

        return readChar == char.toInt()
    }

    fun catchKeyString(reader: StringReader) : String? {

    }

    protected data class KeyNode(
            val key : String,
            val
    )

    internal data class TreeNode (
        val childMap : HashMap<Int, SchemaGroup.TreeNode> = HashMap(),
        var schema : BaseSchema? = null
    )
}