package com.virtualightning.fileresolver

import com.virtualightning.fileresolver.schema.BaseSchema
import com.virtualightning.fileresolver.schema.SchemaCatch
import com.virtualightning.fileresolver.schema.SchemaTree
import com.virtualightning.fileresolver.tools.CustomStringStream
import com.virtualightning.fileresolver.utils.Info
import java.io.StringReader

fun main(args: Array<String>) {
    SchemaTree.addSchema(BaseSchema("val"))
    SchemaTree.addSchema(BaseSchema("int"))
    SchemaTree.addSchema(BaseSchema("string"))

    Info(SchemaCatch.catchSchema(CustomStringStream("     stri ng    s"))?.schemaName ?:"null")
    Info(SchemaCatch.matchStr)
    Info(SchemaCatch.breakPoint.toChar())
}