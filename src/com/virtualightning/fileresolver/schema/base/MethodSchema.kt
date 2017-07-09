package com.virtualightning.fileresolver.schema.base

import com.virtualightning.fileresolver.exceptions.MethodException

abstract class MethodSchema(schemaName : String) : BaseSchema(schemaName) {
    fun checkArgumentLength(valueArr : MutableList<ValueSchema<*>>, length : Int, isLessThan: Boolean = false) {
        val argLength = valueArr.size
        if( (isLessThan && argLength > length) || (!isLessThan && argLength != length) )
            throw MethodException("Method arguments length are not valid : $schemaName() must have ${if(isLessThan)"at least " else ""}$length arguments")
    }
}