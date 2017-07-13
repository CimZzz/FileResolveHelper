package com.virtualightning.fileresolver.schema.base

import com.virtualightning.fileresolver.exceptions.MethodException

abstract class MethodSchema(schemaName : String) : BaseSchema(schemaName) {
    fun checkArgumentLength(valueArr : MutableList<ValueSchema<*>>, length : Int, isLessThan: Boolean = false) {
        val argLength = valueArr.size
        if( (isLessThan && argLength > length) || (!isLessThan && argLength != length) )
            throw MethodException("Method arguments length are not valid : $schemaName() must have ${if(isLessThan)"at least " else ""}$length arguments")
    }

    fun valueNullException() : Unit = throw MethodException("Method params value don't be null")
    fun repeatValueException(value : Any) : Unit = throw MethodException("Method params value repeat : $value")
    fun unknownValueException(value : Any) : Unit = throw MethodException("Method params value unknown : $value")
    fun runException(msg : String) : Unit = throw MethodException("Method run error : $msg")
    fun paramsLengthException(need:String,real:Int) : Unit = throw MethodException("Method params length wrong , need : $need , real : $real")
}