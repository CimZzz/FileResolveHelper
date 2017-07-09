package com.virtualightning.fileresolver.environment

import com.virtualightning.fileresolver.schema.members.FieldSchema
import com.virtualightning.fileresolver.schema.SchemaTree
import com.virtualightning.fileresolver.schema.base.BaseSchema
import com.virtualightning.fileresolver.schema.base.MethodSchema
import com.virtualightning.fileresolver.schema.base.ValueSchema
import com.virtualightning.fileresolver.schema.members.NonReturnMethodSchema
import com.virtualightning.fileresolver.schema.operator.AddSchema
import com.virtualightning.fileresolver.schema.operator.DivideSchema
import com.virtualightning.fileresolver.schema.operator.MultiplySchema
import com.virtualightning.fileresolver.schema.operator.SubtractSchema
import com.virtualightning.fileresolver.schema.others.*
import com.virtualightning.fileresolver.schema.values.BooleanValue
import com.virtualightning.fileresolver.tools.CustomStringStream
import java.nio.charset.Charset


val charsetArray = arrayOf(Charset.forName("utf-8"),Charset.forName("gbk"),Charset.forName("gb2312"))

val orderArray = arrayOf("Big-Endian Order","Little-Endian Order")

val intTypeArray = arrayOf("uint8","uint16","uint32","int64","int8","int16","int32")
val intLengthTypeArray = arrayOf("bit","byte","other int format")
val formatTableName = arrayOf("Name","Type","Byte Size","Value")

val TYPE_PROTOCOL : Byte = 0
val TYPE_BLOCK : Byte = 1
val TYPE_FIELD : Byte = 2
val TYPE_STRING : Byte = 3

/*System Config*/
val byteCountArray = arrayOf(25,50,100,125)
val radixArray = arrayOf("Binary","Decimal","Hexadecimal")
val byteTableName = arrayOf("Address","Bytes","ASCII")
val completionArray = arrayOf("00000000","0000000","000000","00000","0000","000","00","0","")

/*Quick Command*/
val QUICK_COMMAND_CAPTAIN = 10

/*Syntax Callback Code*/
val spaceCharInt = ' '.toInt()
val commaCharInt = ','.toInt()
val equalCharInt = '='.toInt()
val wrapCharInt = '\n'.toInt()
val tabCharInt = '\t'.toInt()
val leftBracketCharInt = '('.toInt()
val rightBracketCharInt = ')'.toInt()
val quoteCharInt = '"'.toInt()
val eofCharInt = -1
val zeroCharInt = '0'.toInt()
val oneCharInt = '1'.toInt()
val nineCharInt = '9'.toInt()
val aLowCharInt = 'a'.toInt()
val nLowCharInt = 'n'.toInt()
val aUpCharInt = 'A'.toInt()
val zLowCharInt = 'z'.toInt()
val zUpCharInt = 'Z'.toInt()
val pointCharInt = '.'.toInt()
val minusCharInt = '-'.toInt()
val addCharInt = '+'.toInt()
val multiCharInt = '*'.toInt()
val divCharInt = '/'.toInt()
val backSlashCharInt = '\\'.toInt()
val operatorCharIntArray = arrayOf(minusCharInt, divCharInt, multiCharInt, addCharInt)
val tenMultipleArray = arrayOf(10,100,1000,10000,100000,1000000)

typealias ISyntaxCallback = (Int, String) -> Unit
object SyntaxCallbackCode {
    @JvmStatic
    val ERROR = -1
    @JvmStatic
    val SUCCESS = 0
    @JvmStatic
    val CLEAR = 1
}


/*Abstract File Callback Code*/
val remainBitFlagArray = arrayOf(0x0,0x1,0x3,0x7,0xF,0x1F,0x3F,0x7F,0xFF)
typealias IFileReadableCallback = (Int, Any) -> Unit
object AbstractReadableCallbackCode {
    @JvmStatic
    val TOTAL_LENGTH = 0
    val CURRENT_POSITION = 1
    val REMAIN_BIT_COUNT = 2
    val REMAIN_BYTE_COUNT = 3
    val CLEAR = 4
}






/*Schema Constant*/
typealias IResolver = (reader : CustomStringStream) -> Boolean
typealias ICharAnalyzer = (char : Int) -> Unit
typealias ISchemaAnalyzer = (char : BaseSchema) -> Unit
typealias IMethodCallback = (MethodSchema, MutableList<ValueSchema<*>>) -> ValueSchema<*>
typealias INonReturnMethodCallback = (MethodSchema, MutableList<ValueSchema<*>>) -> Any?
var trueField = FieldSchema("true", BooleanValue(true))
var falseField = FieldSchema("false", BooleanValue(false))






/*Init*/

fun initSchemaTree() {
    SchemaTree.addSchema(trueField)
    SchemaTree.addSchema(falseField)

    SchemaTree.addSchema(LeftBracketSchema)
    SchemaTree.addSchema(RightBracketSchema)
    SchemaTree.addSchema(CommaSchema)
    SchemaTree.addSchema(VarSchema)
    SchemaTree.addSchema(EqualSchema)

    SchemaTree.addSchema(AddSchema)
    SchemaTree.addSchema(DivideSchema)
    SchemaTree.addSchema(MultiplySchema)
    SchemaTree.addSchema(SubtractSchema)

    SchemaTree.addSchema(NonReturnMethodSchema("skip",{
        method,args->
        println("skip params length : $args")
        null
    }))

    SchemaTree.addSchema(NonReturnMethodSchema("clear",{
        method,args->
        method.checkArgumentLength(args,0)
        SyntaxCallbackCode.CLEAR
    }))
}
