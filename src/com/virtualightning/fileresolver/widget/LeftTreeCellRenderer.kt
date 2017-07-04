package com.virtualightning.fileresolver.widget

import com.virtualightning.fileresolver.entity.LeftTreeNode
import com.virtualightning.fileresolver.environment.TYPE_BLOCK
import com.virtualightning.fileresolver.environment.TYPE_FIELD
import com.virtualightning.fileresolver.environment.TYPE_PROTOCOL
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.Image
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JTree
import javax.swing.tree.TreeCellRenderer



class LeftTreeCellRenderer : TreeCellRenderer {
    val label = JLabel()
    val protocolPic = ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/protocol.png"))
    val blockPic = ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/block.png"))
    val fieldPic = ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/field.png"))

    init {
        protocolPic.image = protocolPic.image.getScaledInstance(20,20, Image.SCALE_DEFAULT)
        blockPic.image = blockPic.image.getScaledInstance(20,20, Image.SCALE_DEFAULT)
        fieldPic.image = fieldPic.image.getScaledInstance(20,20, Image.SCALE_DEFAULT)
        label.isOpaque = true
    }

    override fun getTreeCellRendererComponent(tree: JTree?, value: Any?, selected: Boolean, expanded: Boolean, leaf: Boolean, row: Int, hasFocus: Boolean): Component {
        if(value !is LeftTreeNode) {
            label.text = ""
            label.background = Color(0xFF,0xFF,0xFF,0x00)
            label.icon = null
            return label
        }
        val node =  value
        label.text = node.nodeName
        if(selected) {
            if(hasFocus)
                label.background = Color(0x23,0x81,0xE4,0xFF)
            else label.background = Color(0x26,0x65,0xA7,0xFF)
        } else {
            label.background = Color(0xFF,0xFF,0xFF,0x00)
        }


        when(node.nodeType) {
            TYPE_PROTOCOL ->label.icon = protocolPic
            TYPE_BLOCK ->label.icon = blockPic
            TYPE_FIELD ->label.icon = fieldPic
        }

        return label
    }

}