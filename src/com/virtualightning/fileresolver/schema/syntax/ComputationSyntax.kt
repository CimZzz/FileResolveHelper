package com.virtualightning.fileresolver.schema.syntax

import com.virtualightning.fileresolver.schema.SchemaCatch
import com.virtualightning.fileresolver.schema.base.*
import com.virtualightning.fileresolver.exceptions.SyntaxException
import com.virtualightning.fileresolver.exceptions.DecimalException
import com.virtualightning.fileresolver.exceptions.NullPointerException
import com.virtualightning.fileresolver.schema.members.FieldSchema
import com.virtualightning.fileresolver.schema.members.ReturnMethodSchema
import com.virtualightning.fileresolver.schema.others.CommaSchema
import com.virtualightning.fileresolver.schema.others.LeftBracketSchema
import com.virtualightning.fileresolver.schema.others.RightBracketSchema
import com.virtualightning.fileresolver.tools.CustomStringStream
import com.virtualightning.fileresolver.tools.SyntaxTool
import java.util.*


object ComputationSyntax {
    val globalSyntaxTool = SyntaxTool()

    val methodToolStack = LinkedList<SyntaxTool>()
    val methodArgumentStack = LinkedList<ValueSchema<*>>()
    val argList = LinkedList<ValueSchema<*>>()

    var isNeedOperator = false
    var isSwitchToMethod = false
    var isNonMethod = false
    var curSyntaxTool = globalSyntaxTool

    fun init() {
        globalSyntaxTool.init()
        methodToolStack.forEach { it.init() }
        methodToolStack.clear()
        methodArgumentStack.clear()
        argList.clear()

        isNeedOperator = false
        isSwitchToMethod = false
        isNonMethod = false
        curSyntaxTool = globalSyntaxTool
    }

    fun getValueSchema(reader : CustomStringStream) : ValueSchema<*> {
        while (!reader.isEnd())
            resolve(SchemaCatch.catchSchema(reader))

        if(curSyntaxTool.leftBracketCount != 0)
            throw SyntaxException("Miss\")\"")
        if(curSyntaxTool != globalSyntaxTool)
            throw SyntaxException("Method miss \")\" : ${curSyntaxTool.methodSchema!!.schemaName}")
        if(methodArgumentStack.size != 0)
            throw Exception("Program error : method args stack is not empty")

        return handleStack()
    }

    fun getNonReturnMethodArg(reader : CustomStringStream) : MutableList<ValueSchema<*>> {
        init()
        isSwitchToMethod = true
        isNonMethod = true
        while (!reader.isEnd()) {
            if (isNonMethod)
                resolve(SchemaCatch.catchSchema(reader))
            else throw SyntaxException("Non-Return method must a stand-alone statement")
        }

        if(curSyntaxTool.leftBracketCount != 0)
            throw SyntaxException("Miss\")\"")
        if(curSyntaxTool != globalSyntaxTool)
            throw SyntaxException("Method miss \")\" : ${curSyntaxTool.methodSchema!!.schemaName}")
        if(methodArgumentStack.size != 0)
            throw Exception("Program error : method args stack is not empty")

        return argList
    }

    fun resolve(schema: BaseSchema) {
        if(isSwitchToMethod) {
            if(schema !is LeftBracketSchema)
                throw SyntaxException("Miss \"(\" after methodSchema")
            isSwitchToMethod = false
            return
        }

        when(schema) {
            is OperatorSchema -> {
                //Unary Operator(Not available)

                //Operator Schema
                if(!isNeedOperator)
                    throw SyntaxException("Operator syntax is not valid : ${schema.schemaName}")
                isNeedOperator = false

                pushSchemaStack(schema)
            }
            is FieldSchema -> {
                //Field Schema
                if(isNeedOperator)
                    throw SyntaxException("Field syntax is not valid : ${schema.schemaName}")
                isNeedOperator = true

                val valueSchema = schema.value?:throw NullPointerException("Field value is null : ${schema.schemaName}")

                pushSchemaStack(valueSchema)
            }
            is ValueSchema<*> -> {
                //Value Schema
                if(isNeedOperator)
                    throw SyntaxException("${schema.value} syntax is not valid")
                isNeedOperator = true

                pushSchemaStack(schema)
            }
            is ReturnMethodSchema -> {
                if(isNeedOperator)
                    throw SyntaxException("Field syntax is not valid : ${schema.schemaName}")
                isNeedOperator = false
                isSwitchToMethod = true
                switchToMethod(schema)
            }
            is LeftBracketSchema -> {
                if(isNeedOperator)
                    throw SyntaxException("\"(\" syntax is not valid")

                pushLeftBracket(schema)
            }
            is RightBracketSchema -> {
                if(curSyntaxTool.leftBracketCount == 0) {
                    //Method
                    if(curSyntaxTool.methodSchema == null && !isNonMethod)
                        throw SyntaxException("\")\" syntax is not valid")

                    if(curSyntaxTool.schemaStack.size != 0) {
                        curSyntaxTool.argCount++
                        methodArgumentStack.push(handleStack())
                    }

                    argList.clear()
                    (curSyntaxTool.argCount downTo 1).forEach {
                        argList.addFirst(methodArgumentStack.poll())
                    }

                    if(isNonMethod) {
                        isNonMethod = false
                        return
                    }

                    val valueSchema = curSyntaxTool.methodSchema!!.invoke(argList)

                    backToLastSchema(valueSchema)
                } else handleRightBracket()

                isNeedOperator = true
            }
            is CommaSchema -> {
                if(!isNeedOperator)
                    throw SyntaxException("\",\" syntax is not valid")
                if(curSyntaxTool.methodSchema == null && !isNonMethod)
                    throw SyntaxException("\",\" syntax is not valid")

                if(curSyntaxTool.leftBracketCount != 0)
                    throw SyntaxException("Method params syntax is not valid : ${curSyntaxTool.methodSchema!!.schemaName}")

                curSyntaxTool.argCount ++
                methodArgumentStack.push(handleStack())
                isNeedOperator = false
            }
        }
    }

    private fun switchToMethod(schema : ReturnMethodSchema) {
        if(curSyntaxTool.methodSchema != null)
            methodToolStack.push(curSyntaxTool)
        curSyntaxTool = SyntaxTool()
        curSyntaxTool.methodSchema = schema
    }

    private fun pushSchemaStack(schema : ComputableSchema) {
        curSyntaxTool.schemaStack.push(schema)
    }

    private fun pushLeftBracket(schema : LeftBracketSchema) {
        curSyntaxTool.schemaStack.push(schema)
        curSyntaxTool.leftBracketCount++
    }

    private fun handleRightBracket() {
        curSyntaxTool.schemaStack.push(handleStack())
        curSyntaxTool.leftBracketCount--
    }

    private fun backToLastSchema(valueSchema : ValueSchema<*>) {
        curSyntaxTool.init()
        curSyntaxTool = methodToolStack.poll() ?: globalSyntaxTool
        curSyntaxTool.schemaStack.push(valueSchema)
    }

    private fun handleStack() : ValueSchema<*> {
        val stack = curSyntaxTool.schemaStack
        var rightSchema : ValueSchema<*>? = null
        var leftSchema : ValueSchema<*>? = null
        var preCheckSchema : ValueSchema<*>? = null
        var operatorSchema : OperatorSchema? = null
        var preCheckOperator : OperatorSchema? = null
        var tempSchema : BaseSchema?
        var doOperator : Boolean
        var doPoll = true
        var isEnd = false

        while (stack.size != 0) {
            if(doPoll) {
                tempSchema = stack.poll()
                if (tempSchema is OperatorSchema) {
                    operatorSchema = tempSchema
                } else if (tempSchema is ValueSchema<*>) {
                    if (rightSchema == null)
                        rightSchema = tempSchema
                    else if(leftSchema == null)
                        leftSchema = tempSchema
                    else preCheckSchema = tempSchema
                }
            }

            if(preCheckOperator != null) {
                leftSchema = preCheckOperator.operator(preCheckSchema!!,leftSchema!!)

                preCheckOperator = null
                preCheckSchema = null
                doPoll = false
            }
            else if(operatorSchema != null) {
                if(rightSchema != null && leftSchema != null) {
                    doOperator = false
                    tempSchema = stack.poll()
                    if(tempSchema == null || tempSchema is LeftBracketSchema) {
                        doOperator = true
                        isEnd = true
                    }
                    else {
                        preCheckOperator = tempSchema as OperatorSchema
                        if(preCheckOperator.priority() <= operatorSchema.priority())
                            doOperator = true
                    }


                    if(doOperator) {
                        rightSchema = operatorSchema.operator(leftSchema,rightSchema)
                        if(preCheckOperator != null) {
                            operatorSchema = preCheckOperator
                            preCheckOperator = null
                        }
                        else operatorSchema = null

                        doPoll = true
                        leftSchema = null

                        if (isEnd) {
                            break
                        }
                    }

                }
            }
        }


        return rightSchema?:throw DecimalException("Wrong value")
    }
}