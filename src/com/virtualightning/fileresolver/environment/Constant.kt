package com.virtualightning.fileresolver.environment

import com.virtualightning.fileresolver.utils.Info
import java.io.File
import java.nio.charset.Charset


val byteCountArray = arrayOf(25,50,100,125)
val radixArray = arrayOf("Binary","Decimal","Hexadecimal")
val byteTableName = arrayOf("Address","Bytes","ASCII")
val remainBitFlagArray = arrayOf(0x1,0x3,0x7,0xF,0x1F,0x3F,0x7F,0xFF)
val completionArray = arrayOf("00000000","0000000","000000","00000","0000","000","00","0","")
val charsetArray = arrayOf(Charset.forName("utf-8"),Charset.forName("gbk"),Charset.forName("gb2312"))
val formatTableName = arrayOf("Name","Type","Value")

val TYPE_PROTOCOL : Byte = 0
val TYPE_BLOCK : Byte = 1
val TYPE_FIELD : Byte = 2
val TYPE_STRING : Byte = 3

fun initConstant() {
    Info(File(".").absolutePath)
}