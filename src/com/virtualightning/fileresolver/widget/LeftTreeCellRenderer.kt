package com.virtualightning.fileresolver.widget

import java.awt.Component
import java.awt.Image
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JTree
import javax.swing.tree.TreeCellRenderer



class LeftTreeCellRenderer : TreeCellRenderer {
    val label = JLabel()
    val fieldPic = ImageIcon(ClassLoader.getSystemClassLoader().getResource("images/field.png"))

    init {
        fieldPic.image = fieldPic.image.getScaledInstance(20,20, Image.SCALE_DEFAULT)
    }

    override fun getTreeCellRendererComponent(tree: JTree?, value: Any?, selected: Boolean, expanded: Boolean, leaf: Boolean, row: Int, hasFocus: Boolean): Component {
        label.text = value.toString()
        label.icon = fieldPic
        return label
    }

}