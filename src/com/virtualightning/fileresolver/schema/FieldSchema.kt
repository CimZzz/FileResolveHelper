package com.virtualightning.fileresolver.schema

import com.virtualightning.fileresolver.schema.values.ValueSchema

class FieldSchema(name : String,var value : ValueSchema<*>? = null) : BaseSchema(name) {
}