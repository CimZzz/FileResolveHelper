package com.virtualightning.fileresolver.entity

import com.virtualightning.fileresolver.environment.TYPE_BLOCK
import java.io.DataInputStream
import java.io.DataOutputStream
import java.nio.charset.Charset

class Block(val blockName: String) {
    val blockNode : LeftTreeNode = LeftTreeNode(blockName, TYPE_BLOCK)
    val formatList : ArrayList<Format<*>> = ArrayList()


    fun addFormat(format : Format<*>) {
        formatList.add(format)
        blockNode.add(format.formatNode)
    }

    fun removeFormat(index : Int) {
        val format = formatList.removeAt(index)
        blockNode.remove(format.formatNode)
    }


    fun writeToFile(outputStream: DataOutputStream) {
        outputStream.writeInt(blockName.length)
        outputStream.writeUTF(blockName)
        outputStream.writeInt(formatList.size)
        formatList.forEach {
            it.writeToFile(outputStream)
        }
    }

    override fun toString(): String {
        return blockName
    }
}


fun ReadBlockFromStream(inputStream : DataInputStream) : Block {
    val nameSize = inputStream.readInt()
    val name = String(inputStream.readBytes(nameSize), Charset.forName("utf-8"))
    val block = Block(name)
    val formatSize = inputStream.readInt()

    (formatSize downTo 1).forEach {
        val format = ReadFormatFromStream(inputStream)
        block.addFormat(format)
    }

    return block
}