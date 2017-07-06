package com.virtualightning.fileresolver.schema

object SchemaTree {
    val rootNode : TreeNode = TreeNode(null,-1)


    fun addSchema(schema: BaseSchema) : Boolean {
        var curNode = rootNode

        schema.schemaName.chars().forEach {
            var nextNode = curNode.childMap[it]
            if(nextNode == null) {
                nextNode = TreeNode(curNode,it)
                curNode.childMap[it] = nextNode
            }
            curNode = nextNode
        }

        if(curNode.schema != null)
            return false
        curNode.schema = schema

        return true
    }

    fun removeSchema(name : String) : Boolean {
        var curNode = rootNode
        var founded = true

        name.chars().forEach {
            val nextNode = curNode.childMap[it]
            if(nextNode == null) {
                founded = false
                return@forEach
            }
            curNode = nextNode
        }

        if(!founded || curNode.schema == null)
            return false

        while (true) {
            if(curNode.childMap.size == 0) {
                val parent = curNode.parent?:break
                curNode.parent = null
                parent.childMap.remove(curNode.nodeKey)
                curNode = parent
            }
            else break
        }

        return true
    }

    data class TreeNode (
            var parent : TreeNode?,
            val nodeKey : Int,
            val childMap : HashMap<Int, TreeNode> = HashMap(),
            var schema : BaseSchema? = null
    )
}