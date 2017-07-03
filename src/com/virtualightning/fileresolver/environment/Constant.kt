package com.virtualightning.fileresolver.environment

import com.virtualightning.fileresolver.utils.Info
import java.io.File


val byteCountArray = arrayOf(25,50,100,125)
val radixArray = arrayOf("Binary","Decimal","Hexadecimal")
val tableName = arrayOf("Address","Bytes","ASCII")
val remainBitFlagArray = arrayOf(0x1,0x3,0x7,0xF,0x1F,0x3F,0x7F,0xFF)
val locationCompletionArray = arrayOf("00000000","0000000","000000","00000","0000","000","00","0","")

fun initConstant() {
    Info(File(".").absolutePath)
}