package com.virtualightning.fileresolver.entity

import com.virtualightning.fileresolver.environment.TYPE_PROTOCOL
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset
import java.util.*

class Protocol (val protocolName : String) {
    val blockList : ArrayList<Block> = ArrayList()
    val rootNode : LeftTreeNode = LeftTreeNode(protocolName, TYPE_PROTOCOL)


    fun addBlock(block : Block) {
        blockList.add(block)
        rootNode.add(block.blockNode)
    }

    fun removeBlock(index : Int) {
        val block = blockList.removeAt(index)
        rootNode.remove(block.blockNode)
    }

    fun swapBlock(index: Int,afterIndex : Int) {
        Collections.swap(blockList,index,index - 1)
    }

    fun clearData() {

    }

    fun destroy() {

    }
}

fun ReadProtocolFromFile(file : File) : Protocol {
    val inputStream = DataInputStream(FileInputStream(file))

    val nameSize = inputStream.readInt()
    val name = String(inputStream.readBytes(nameSize), Charset.forName("utf-8"))
    val protocol = Protocol(name)
    val blockSize = inputStream.readInt()

    (blockSize downTo 1).forEach {
        val block = ReadBlockFromStream(inputStream)
        protocol.addBlock(block)
    }

    return protocol
}