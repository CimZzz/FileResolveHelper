package com.virtualightning.fileresolver.schema

import com.virtualightning.fileresolver.environment.RESOLVE_ERROR
import com.virtualightning.fileresolver.interfaces.IResolver
import com.virtualightning.fileresolver.interfaces.IResolverCallback
import com.virtualightning.fileresolver.tools.CustomStringStream
import java.io.StringReader
import java.util.*


object SchemaResolver {
    val schemaStack = LinkedList<BaseSchema>()

    var result = false
    var isNeedOperator = false

    val normalResolver : IResolver = Resolve@{
        reader ->
        val schema = SchemaCatch.catchSchema(reader)?:return@Resolve false

        if(schema is FieldSchema) {
            if(isNeedOperator)
                return@Resolve false

            schemaStack.push(schema)
            isNeedOperator = true
        }
        else if(schema is OperatorSchema) {
            if(!isNeedOperator)
                return@Resolve false

            schemaStack.push(schema)
            isNeedOperator = true
        }

        return@Resolve true
    }

    var resolver : IResolver = normalResolver

    fun resolve(str: String, callback : IResolverCallback) {
        result = false
        isNeedOperator = false
        schemaStack.clear()
        val reader = CustomStringStream(str)
        resolver = normalResolver
        while (resolver.invoke(reader)) ;

        if(result) {
            //处理栈
        } else {
            callback.invoke(RESOLVE_ERROR,null)
        }
    }
}