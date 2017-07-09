package com.virtualightning.fileresolver.schema

import com.virtualightning.fileresolver.environment.*
import com.virtualightning.fileresolver.schema.base.BaseSchema
import com.virtualightning.fileresolver.exceptions.DecimalException
import com.virtualightning.fileresolver.exceptions.SyntaxException
import com.virtualightning.fileresolver.schema.others.EndSchema
import com.virtualightning.fileresolver.schema.others.UndeclaredFieldSchema
import com.virtualightning.fileresolver.schema.values.DoubleValue
import com.virtualightning.fileresolver.schema.values.IntValue
import com.virtualightning.fileresolver.schema.values.LongValue
import com.virtualightning.fileresolver.schema.values.StringValue
import com.virtualightning.fileresolver.tools.CustomStringStream

object SchemaCatch {
    val CHAR_TYPE_FIELD = 0
    val CHAR_TYPE_STRING = 1
    val CHAR_TYPE_NUMBER = 2
    val CHAR_TYPE_NULL = 3

    var breakPoint = eofCharInt
    var matchStr = ""
    var charType = CHAR_TYPE_FIELD
    var curNode : SchemaTree.TreeNode? = SchemaTree.rootNode
    var isContinue = true
    var isNeedBack = false

    /*Char Analyzer*/
    var isEscape = false

    /*Number Analyzer*/
    var integer : Double = 0.0
    var decimalCount = 1
    var isDecimal = false

    /*Field Analyzer*/
    var nameCount = 0

    val facadeAnalyzer : ICharAnalyzer = {
        when(it) {
            spaceCharInt->Unit
            eofCharInt-> {
                charType = CHAR_TYPE_NULL
                isContinue = false
            }
            leftBracketCharInt-> {
                curNode = curNode!!.childMap[it]!!
                isContinue = false
            }
            rightBracketCharInt-> {
                curNode = curNode!!.childMap[it]!!
                isContinue = false
                matchStr += it
            }
            commaCharInt-> {
                curNode = curNode!!.childMap[it]!!
                isContinue = false
                matchStr += it
            }
            equalCharInt-> {
                curNode = curNode!!.childMap[it]!!
                isContinue = false
                matchStr += it
            }
            in operatorCharIntArray-> {
                curNode = curNode!!.childMap[it]!!
                isContinue = false
                matchStr += it
            }
            quoteCharInt-> {
                //String Field
                charType = CHAR_TYPE_STRING
                curAnalyzer = stringAnalyzer
            }
            in oneCharInt..nineCharInt -> {
                //Number Field
                charType = CHAR_TYPE_NUMBER
                curAnalyzer = numberAnalyzer
                curAnalyzer.invoke(it)
            }
            in aLowCharInt..zLowCharInt -> {
                //Field
                charType = CHAR_TYPE_FIELD
                curAnalyzer = fieldAnalyzer
                curAnalyzer.invoke(it)
            }
            in aUpCharInt..zUpCharInt -> {
                //Field
                charType = CHAR_TYPE_FIELD
                curAnalyzer = fieldAnalyzer
                curAnalyzer.invoke(it)
            }
            else-> throw SyntaxException("Syntax error $it")

        }
    }



    val stringAnalyzer : ICharAnalyzer = Analyzer@ {
        when(it) {
            eofCharInt-> {
                throw SyntaxException("Syntax error")
            }
            backSlashCharInt-> {
                if(isEscape) {
                    isEscape = false
                    matchStr += '\\'
                    return@Analyzer
                } else {
                    isEscape = true
                    return@Analyzer
                }
            }
            nLowCharInt-> {
                if(isEscape) {
                    isEscape = false
                    matchStr += '\n'
                    return@Analyzer
                }
            }
            quoteCharInt-> {
                if(isEscape) {
                    isEscape = false
                    matchStr += '\"'
                    return@Analyzer
                } else {
                    isContinue = false
                    return@Analyzer
                }
            }
        }

        matchStr += it.toChar()
    }



    val numberAnalyzer : ICharAnalyzer = Analyzer@ {
        when (it) {
            spaceCharInt-> {
                isContinue = false
            }
            eofCharInt-> {
                isContinue = false
            }
            in operatorCharIntArray-> {
                isNeedBack = true
                isContinue = false
            }
            rightBracketCharInt-> {
                isNeedBack = true
                isContinue = false
            }
            commaCharInt-> {
                isNeedBack = true
                isContinue = false
            }
            pointCharInt-> {
                if(isDecimal)
                    throw SyntaxException("Syntax error")
                else isDecimal = true

                matchStr += it
            }
            in zeroCharInt..nineCharInt-> {
                val num = it - zeroCharInt
                if(!isDecimal)
                    integer = integer * 10 + num
                else {
                    if(decimalCount > 6)
                        throw DecimalException("Can't have more than six decimal")
                    integer += num * 1.0 / tenMultipleArray[decimalCount - 1]
                    decimalCount ++
                }

                matchStr += it
            }

            else-> throw SyntaxException("Syntax error")

        }
    }

    val fieldAnalyzer : ICharAnalyzer = Analyzer@ {
        when (it) {
            in aLowCharInt..zLowCharInt->{}
            in aUpCharInt..zUpCharInt->{}
            in zeroCharInt..nineCharInt->{}
            spaceCharInt-> {
                isContinue = false
            }
            eofCharInt-> {
                isContinue = false
            }
            in operatorCharIntArray-> {
                isNeedBack = true
                isContinue = false
            }
            leftBracketCharInt-> {
                isNeedBack = true
                isContinue = false
            }
            rightBracketCharInt-> {
                isNeedBack = true
                isContinue = false
            }
            commaCharInt-> {
                isNeedBack = true
                isContinue = false
            }
            else-> throw SyntaxException("Field name contain non-alphanumeric ${it.toChar()}")
        }

        if(isContinue) {
            if(nameCount >= 20)
                throw SyntaxException("Field name length cannot more than 20")
            curNode = curNode?.childMap?.get(it)
            nameCount ++

            matchStr += it.toChar()
        }

    }



    var curAnalyzer = facadeAnalyzer


    fun catchSchema(reader: CustomStringStream) : BaseSchema {
        init()

        while (isContinue)
            curAnalyzer.invoke(reader.read())

        if(isNeedBack)
            reader.back()

        when(charType) {
            CHAR_TYPE_FIELD->return curNode?.schema?: UndeclaredFieldSchema
            CHAR_TYPE_NUMBER-> {
                val is64BitNum = integer > 0x7FFFFFFF
                if(isDecimal)
                    return DoubleValue(integer)
                else  {
                    if(is64BitNum)
                        return LongValue(integer.toLong())
                    else return IntValue(integer.toInt())
                }
            }
            CHAR_TYPE_NULL->return EndSchema
            else->return StringValue(matchStr)
        }
    }



    private fun init() {
        curAnalyzer = facadeAnalyzer
        breakPoint = eofCharInt
        matchStr = ""
        charType = CHAR_TYPE_FIELD
        curNode = SchemaTree.rootNode
        isContinue = true
        isNeedBack = false
        isEscape = false
        integer = 0.0
        decimalCount = 1
        isDecimal = false
        nameCount= 0
    }
}