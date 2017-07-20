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

    SchemaTree.addSchema(NonReturnMethodSchema("seek",{
        method,args->
        if (args.size != 1)
            method.paramsLengthException("1",args.size)

        var newPosition : Long = 0
        val value = args[0].value?:method.valueNullException()
        when(value) {
            is Int->newPosition = value.toLong()
            is Long->newPosition = value
            else->method.unknownValueException(value)
        }

        val movePosition = SourceProxy.seekPosition(newPosition)

        val msg : String
        if(movePosition >= 0)
            msg = "Backward $movePosition Bytes"
        else msg = "Forward ${-movePosition} Bytes"

        SourceProxy.updatePosition(movePosition,true)
        msg
    }))

    SchemaTree.addSchema(NonReturnMethodSchema("back",{
        method,args->
        if (args.size != 1)
            method.paramsLengthException("1",args.size)

        var movePosition : Long = 0
        val value = args[0].value?:method.valueNullException()
        when(value) {
            is Int->movePosition = value.toLong()
            is Long->movePosition = value
            else->method.unknownValueException(value)
        }

        movePosition = SourceProxy.back(movePosition)

        val msg : String
        if(movePosition >= 0)
            msg = "Backward $movePosition Bytes"
        else msg = "Forward ${-movePosition} Bytes"

        SourceProxy.updatePosition(movePosition,true)
        msg
    }))

    SchemaTree.addSchema(NonReturnMethodSchema("skip",{
        method,args->
        if (args.size != 1)
            method.paramsLengthException("1",args.size)

        var movePosition : Long = 0
        val value = args[0].value?:method.valueNullException()
        when(value) {
            is Int->movePosition = value.toLong()
            is Long->movePosition = value
            else->method.unknownValueException(value)
        }

        movePosition = SourceProxy.skip(movePosition)

        val msg : String
        if(movePosition >= 0)
            msg = "Backward $movePosition Bytes"
        else msg = "Forward ${-movePosition} Bytes"

        SourceProxy.updatePosition(movePosition,true)
        msg
    }))

    SchemaTree.addSchema(ReturnMethodSchema("readInt",{
        method,args->
        var length : Int? = null
        var isBigEndian : Boolean? = null
        if (args.size >= 3)
            method.paramsLengthException("unless 2",args.size)
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
        if(readSize < length!!) {
            SourceProxy.updatePosition(-1,false)
            method.runException("Require size is $length , but read size is only $readSize")
        }
        else if(readSize > 4) {
            SourceProxy.updatePosition(-1,false)
            method.runException("Max size is 4 , but read size is $readSize")
        }
        SourceProxy.updatePosition(readSize.toLong(),true)

        IntValue(ByteUtils.byteConvertInt(buffer,isBigEndian!!))
    }))














}