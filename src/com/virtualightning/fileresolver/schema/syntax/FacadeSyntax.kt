package com.virtualightning.fileresolver.schema.syntax

import com.virtualightning.fileresolver.environment.ISyntaxCallback
import com.virtualightning.fileresolver.environment.SyntaxCallbackCode
import com.virtualightning.fileresolver.schema.SchemaCatch
import com.virtualightning.fileresolver.schema.SchemaTree
import com.virtualightning.fileresolver.schema.base.BaseSchema
import com.virtualightning.fileresolver.schema.base.ValueSchema
import com.virtualightning.fileresolver.exceptions.SyntaxException
import com.virtualightning.fileresolver.schema.members.FieldSchema
import com.virtualightning.fileresolver.schema.members.NonReturnMethodSchema
import com.virtualightning.fileresolver.schema.others.EqualSchema
import com.virtualightning.fileresolver.schema.others.UndeclaredFieldSchema
import com.virtualightning.fileresolver.schema.others.VarSchema
import com.virtualightning.fileresolver.tools.CustomStringStream


object FacadeSyntax {
    private val TYPE_VAR = 0
    private val TYPE_REDEFINED = 1
    private val TYPE_ONLY_COMPUTE = 2
    private val TYPE_NON_RETURN = 3


    fun resolve(command : String,callback : ISyntaxCallback) {

        ComputationSyntax.init()

        val reader = CustomStringStream(command)
        var type = TYPE_ONLY_COMPUTE
        var decSchema : BaseSchema? = null

        try {
            var tempSchema: BaseSchema = SchemaCatch.catchSchema(reader)
            when (tempSchema) {
                is VarSchema -> {
                    if (!matchUndeclaredFieldSchema(SchemaCatch.catchSchema(reader)))
                        throw SyntaxException("The right of \"val\" word must be an undeclared variable")

                    decSchema = FieldSchema(SchemaCatch.matchStr)

                    if (!matchEqualSchema(SchemaCatch.catchSchema(reader)))
                        throw SyntaxException("After assignment statement must be word of \"=\"")

                    type = TYPE_VAR
                }
                is FieldSchema -> {
                    decSchema = tempSchema

                    tempSchema = SchemaCatch.catchSchema(reader)

                    if (matchEqualSchema(tempSchema)) {
                        type = TYPE_REDEFINED
                    } else {
                        ComputationSyntax.resolve(decSchema)
                        ComputationSyntax.resolve(tempSchema)
                    }
                }
                is UndeclaredFieldSchema -> {
                    throw SyntaxException("Unknown variable \"${SchemaCatch.matchStr}\"")
                }
                is NonReturnMethodSchema -> {
                    type = TYPE_NON_RETURN
                    decSchema = tempSchema
                }
                else -> ComputationSyntax.resolve(tempSchema)
            }

            when (type) {
                TYPE_VAR -> {
                    val field = decSchema!! as FieldSchema
                    field.value = getValueSchema(reader)
                    SchemaTree.addSchema(decSchema)
                    callback.invoke(SyntaxCallbackCode.SUCCESS, "Declaration ${field.schemaName} = ${field.value.toString()}")
                }
                TYPE_REDEFINED -> {
                    val field = decSchema!! as FieldSchema
                    field.value = getValueSchema(reader)
                    callback.invoke(SyntaxCallbackCode.SUCCESS, "Redefine ${field.schemaName} = ${field.value.toString()}")
                }
                TYPE_ONLY_COMPUTE -> {
                    callback.invoke(SyntaxCallbackCode.SUCCESS, "Value is ${getValueSchema(reader).value}")
                }
                TYPE_NON_RETURN -> {
                    val method = decSchema!! as NonReturnMethodSchema
                    val msg = method.invoke(getNonReturnMethodArg(reader))
                    if(msg == null)
                        callback.invoke(SyntaxCallbackCode.SUCCESS, "Method ${method.schemaName}() successful !")
                    else {
                        when(msg) {
                            is String-> callback.invoke(SyntaxCallbackCode.SUCCESS, "Method ${method.schemaName}() successful : $msg")
                            is Int -> callback.invoke(msg, "Method ${method.schemaName}() successful !")
                        }

                    }
                }
            }
        } catch (e : Exception) {
            e.printStackTrace()
            callback.invoke(SyntaxCallbackCode.ERROR, "${e.javaClass.simpleName} : ${e.message?:"Unknown message"}")
        }
    }

    private fun getValueSchema(reader: CustomStringStream) : ValueSchema<*> {
        return ComputationSyntax.getValueSchema(reader)
    }

    private fun getNonReturnMethodArg(reader: CustomStringStream) :  MutableList<ValueSchema<*>> {
        return ComputationSyntax.getNonReturnMethodArg(reader)
    }

    private fun matchUndeclaredFieldSchema(schema: BaseSchema) : Boolean = schema is UndeclaredFieldSchema
    private fun matchEqualSchema(schema: BaseSchema) : Boolean = schema is EqualSchema
}