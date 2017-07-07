package com.virtualightning.fileresolver.schema

import com.virtualightning.fileresolver.environment.RESOLVE_ERROR
import com.virtualightning.fileresolver.interfaces.IResolverCallback
import com.virtualightning.fileresolver.schema.exceptions.DecimalException
import com.virtualightning.fileresolver.schema.exceptions.SyntaxException
import com.virtualightning.fileresolver.schema.operator.OperatorSchema
import com.virtualightning.fileresolver.schema.others.CommaSchema
import com.virtualightning.fileresolver.schema.others.LeftBracketSchema
import com.virtualightning.fileresolver.schema.others.RightBracketSchema
import com.virtualightning.fileresolver.schema.values.ValueSchema
import com.virtualightning.fileresolver.tools.CustomStringStream
import com.virtualightning.fileresolver.tools.SyntaxTool
import java.util.*


object SchemaResolver {
    val methodToolStack = LinkedList<SyntaxTool>()
    val methodArgumentStack = LinkedList<ValueSchema<*>>()

    val globalSyntaxTool = SyntaxTool()
    var currentTool = globalSyntaxTool

    var isNeedOperator = false

    /*Method Analyzer*/
    var isFirstSwitchMethod = false

    private fun init() {
        methodToolStack.clear()
        methodArgumentStack.clear()
        globalSyntaxTool.init()
        currentTool = globalSyntaxTool

        isNeedOperator = false

        isFirstSwitchMethod = false
    }

    fun resolve(str: String, callback : IResolverCallback) {
        init()
        val reader = CustomStringStream(str)
        try {
            while (!reader.isEnd()) {
                val schema = SchemaCatch.catchSchema(reader)

                if(isFirstSwitchMethod) {
                    if(schema is LeftBracketSchema)
                        isFirstSwitchMethod = false
                    else throw SyntaxException("Miss \"(\" after methodSchema")

                    continue
                }

                when(schema) {
                    is CommaSchema-> {
                        if(!isNeedOperator)
                            throw SyntaxException("Syntax error at \",\"")
                        if(currentTool.methodSchema == null)
                            throw SyntaxException("Syntax error at \",\"")

                        if(currentTool.leftBracketCount != 0)
                            throw SyntaxException("Error methodSchema params")

                        currentTool.argCount++
                        methodArgumentStack.push(handleStack(currentTool.schemaStack))
                        isNeedOperator = false
                    }
                    is MethodSchema-> {
                        if (SchemaResolver.isNeedOperator)
                            throw SyntaxException("Syntax error")

                        if(currentTool.methodSchema != null)
                            methodToolStack.push(currentTool)

                        currentTool = SyntaxTool(methodSchema = schema)
                        isFirstSwitchMethod = true
                    }
                    is FieldSchema-> {
                        if (SchemaResolver.isNeedOperator)
                            throw SyntaxException("Syntax error")

                        currentTool.schemaStack.push(schema.value)
                        isNeedOperator = true
                    }
                    is ValueSchema<*>-> {
                        if (SchemaResolver.isNeedOperator)
                            throw SyntaxException("Syntax error")

                        currentTool.schemaStack.push(schema)
                        isNeedOperator = true
                    }
                    is OperatorSchema-> {
                        if (!SchemaResolver.isNeedOperator)
                            throw SyntaxException("Syntax error")

                        currentTool.schemaStack.push(schema)
                        isNeedOperator = false
                    }
                    is LeftBracketSchema -> {
                        currentTool.schemaStack.push(schema)
                        isNeedOperator = false
                        currentTool.leftBracketCount++
                    }
                    is RightBracketSchema -> {
                        if(currentTool.leftBracketCount == 0) {
                            //Method
                            if(currentTool.methodSchema == null)
                                throw SyntaxException("Syntax error")

                            if(currentTool.schemaStack.size != 0) {
                                currentTool.argCount++
                                methodArgumentStack.push(handleStack(currentTool.schemaStack))
                            }

                            println("Run methodSchema : ${currentTool.methodSchema!!.schemaName}")
                            val valueSchema = currentTool.methodSchema!!.invoke(methodArgumentStack.subList(0, currentTool.argCount))
                            currentTool.init()
                            currentTool = methodToolStack.poll() ?: globalSyntaxTool
                            currentTool.schemaStack.push(valueSchema)
                        } else {
                            currentTool.schemaStack.push(handleStack(currentTool.schemaStack))
                            currentTool.leftBracketCount--
                        }
                    }
                    else -> throw SyntaxException("Syntax error")
                }

            }

            if(currentTool.methodSchema != null || methodToolStack.size != 0 || currentTool.leftBracketCount != 0)
                throw SyntaxException("Syntax error")
        } catch (e : Exception) {
            e.printStackTrace()
            callback.invoke(RESOLVE_ERROR, arrayOf(e.message!!))
        }
        println("${handleStack(globalSyntaxTool.schemaStack).value}")
    }


    fun handleStack(stack : LinkedList<BaseSchema>) : ValueSchema<*> {
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