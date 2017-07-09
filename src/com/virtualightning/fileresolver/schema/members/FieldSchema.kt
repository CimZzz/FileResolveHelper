package com.virtualightning.fileresolver.schema.members

import com.virtualightning.fileresolver.schema.base.BaseSchema
import com.virtualightning.fileresolver.schema.base.ValueSchema

class FieldSchema(name : String,var value : ValueSchema<*>? = null) : BaseSchema(name) {
}