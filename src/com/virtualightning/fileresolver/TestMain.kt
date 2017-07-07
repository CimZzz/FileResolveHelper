package com.virtualightning.fileresolver

import com.virtualightning.fileresolver.schema.*
import com.virtualightning.fileresolver.schema.operator.AddSchema
import com.virtualightning.fileresolver.schema.operator.MultiplySchema
import com.virtualightning.fileresolver.schema.others.LeftBracketSchema
import com.virtualightning.fileresolver.schema.others.RightBracketSchema
import com.virtualightning.fileresolver.schema.values.IntValue

fun main(args: Array<String>) {
    SchemaTree.addSchema(FieldSchema("a",IntValue(3)))
    SchemaTree.addSchema(FieldSchema("b",IntValue(4)))
    SchemaTree.addSchema(MethodSchema("haha",{
        println("HaHa : $it")
        return@MethodSchema IntValue(10)
    }))
    SchemaTree.addSchema(MethodSchema("heihei",{
        println("HeiHei : $it")
        return@MethodSchema IntValue(10)
    }))
    SchemaTree.addSchema(LeftBracketSchema)
    SchemaTree.addSchema(RightBracketSchema)
    SchemaTree.addSchema(AddSchema)
    SchemaTree.addSchema(MultiplySchema)

    SchemaResolver.resolve("a + b * (a + b) + haha(12+haha())",{
        code,args->
        println("Error ${args!![0]}")
    })

}