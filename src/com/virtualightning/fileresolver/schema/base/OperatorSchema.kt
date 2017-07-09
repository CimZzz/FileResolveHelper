package com.virtualightning.fileresolver.schema.base

abstract class OperatorSchema(name : String) : ComputableSchema(name) {
    abstract fun operator(leftSchema : ValueSchema<*>, rightSchema : ValueSchema<*>) : ValueSchema<*>
    abstract fun priority() : Int
}