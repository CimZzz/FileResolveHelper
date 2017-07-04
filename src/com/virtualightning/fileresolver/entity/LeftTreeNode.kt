package com.virtualightning.fileresolver.entity

import javax.swing.tree.DefaultMutableTreeNode

data class LeftTreeNode (
         var nodeName : String,
         var nodeType : Byte
 ) : DefaultMutableTreeNode()