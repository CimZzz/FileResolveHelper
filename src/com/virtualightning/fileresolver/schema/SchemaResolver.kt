package com.virtualightning.fileresolver.schema

import com.virtualightning.fileresolver.environment.RESOLVE_ERROR
import com.virtualightning.fileresolver.interfaces.IResolverCallback
import com.virtualightning.fileresolver.schema.exceptions.HandleStackException
import com.virtualightning.fileresolver.schema.operator.OperatorSchema
import com.virtualightning.fileresolver.tools.CustomStringStream
import java.util.*


object SchemaResolver {
    val schemaStack = LinkedList<BaseSchema>()

    var result = false
    var isNeedOperator = false


    fun resolve(str: String, callback : IResolverCallback) {
        result = false
        isNeedOperator = false
        schemaStack.clear()
        val reader = CustomStringStream(str)
        while (true)  {
            val schema = SchemaCatch.catchSchema(reader)?:break

            if(schema is FieldSchema) {
                if(isNeedOperator)
                    break

                schemaStack.push(schema)
                isNeedOperator = true
            }
            else if(schema is OperatorSchema) {
                if(!isNeedOperator)
                    break

                schemaStack.push(schema)
                isNeedOperator = true
            }
            else if (schema is LeftBracketSchema) {
                schemaStack.push(schema)
            }
            else if (schema is RightBracketSchema) {
                handleStack()
            }
        }

        if(result) {
            //处理栈
        } else {
            callback.invoke(RESOLVE_ERROR,null)
        }
    }

    fun handleStack(endSchema : BaseSchema? = null) {
        var rightSchema : FieldSchema?
        var leftSchema : FieldSchema
        var operatorSchema : OperatorSchema?
        var preCheckOperator : OperatorSchema?

        leftSchema = schemaStack.poll() as FieldSchema? ?:throw HandleStackException("Null Statement")
        operatorSchema = schemaStack.poll() as OperatorSchema? ?:throw HandleStackException("Null Operator")
        rightSchema = schemaStack.poll() as FieldSchema

        while (true) {
            preCheckOperator = schemaStack.poll() as OperatorSchema
            if(preCheckOperator)
        }

    }
}