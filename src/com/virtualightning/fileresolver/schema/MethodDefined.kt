package com.virtualightning.fileresolver.schema

import com.virtualightning.fileresolver.environment.SourceProxy
import com.virtualightning.fileresolver.environment.SyntaxCallbackCode
import com.virtualightning.fileresolver.schema.members.NonReturnMethodSchema
import com.virtualightning.fileresolver.schema.members.ReturnMethodSchema
import com.virtualightning.fileresolver.schema.values.IntValue
import com.virtualightning.fileresolver.ui.ByteUtils


fun definedMethod() {
    SchemaTree.addSchema(NonReturnMethodSchema("clear",{
        method,args->
        method.checkArgumentLength(args,0)
        SyntaxCallbackCode.CLEAR
    }))


    SchemaTree.addSchema(ReturnMethodSchema("readInt",{
        method,args->
        var length : Int? = null
        var isBigEndian : Boolean? = null
        args.forEach {
            val value = it.value?:method.valueNullException()
            when(value) {
                is Int-> {
                    if(length == null)
                        length = value
                    else method.repeatValueException(value)
                }
                is Boolean-> {
                    if(isBigEndian == null)
                        isBigEndian = value
                    else method.repeatValueException(value)
                }
                else->method.unknownValueException(value)
            }
        }

        if(length == null)
            length = 4
        if(isBigEndian == null)
            isBigEndian = true

        val buffer = ByteArray(length!!)
        val readSize = SourceProxy.readBytes(buffer)
        if(readSize < length!!)
            method.runException("Require size is $length , but read size is only $readSize")

        IntValue(ByteUtils.byteConvertInt(buffer,isBigEndian!!))
    }))














}