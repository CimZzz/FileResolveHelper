package com.virtualightning.fileresolver.schema

class SchemaGroup(groupName : String, vararg schemas: BaseSchema) : BaseSchema(groupName) {
    private val schemaMap : HashMap<String, BaseSchema> = HashMap()
    private val rootNode : TreeNode = TreeNode()
    private var curSearchNode = rootNode

    init {
        schemas.forEach {
            val schemaName = it.schemaName.toLowerCase()
            var curNode = rootNode
            schemaName.chars().forEach {
                var nextNode = curNode.childMap[it]
                if(nextNode == null) {
                    nextNode = TreeNode()
                    curNode.childMap[it] = nextNode
                }
                curNode = nextNode
            }
            if(curNode.schema != null)
                throw RuntimeException("重复的Schemas")
            curNode.schema = it

            schemaMap.put(schemaName,it)
        }
    }

    fun init() {
        curSearchNode = rootNode
    }

    fun search(char : Int) : Boolean {
        val curNode = curSearchNode.childMap[char] ?: return false
        curSearchNode = curNode
        return true
    }

    fun find() : BaseSchema? {
        return curSearchNode.schema
    }

    internal data class TreeNode (
        val childMap : HashMap<Int, TreeNode> = HashMap(),
        var schema : BaseSchema? = null
    )
}