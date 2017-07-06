package com.virtualightning.fileresolver.utils

import java.awt.*
import javax.swing.JPanel

val constraint = GridBagConstraints()
val defaultInsets = Insets(5,5,2,2)

fun addGridBagComponent(component: Component,
                 parent : Container,
                 fill : Int = GridBagConstraints.HORIZONTAL,
                 insets : Insets = defaultInsets,
                 gridX : Int = 0,
                 gridY : Int = 0,
                 gridWidth : Int = 0,
                 weightX : Double = 0.0,
                 weightY: Double = 0.0,
                 ipadX : Int = 0) {
    constraint.fill = fill
    constraint.gridx = gridX
    constraint.gridy = gridY
    constraint.gridwidth = gridWidth
    constraint.weightx = weightX
    constraint.weighty = weightY
    constraint.ipadx = ipadX
    constraint.insets = insets
    (parent.layout as GridBagLayout).setConstraints(component,constraint)
    parent.add(component)
}

fun getSeparator(color : Color,size : Int = 2) : Component {
    val separator = JPanel()
    separator.background = color
    separator.preferredSize = Dimension(0,size)
    return separator
}

fun setContainerEnable(components: Array<Component>,isEnable : Boolean) {
    components.forEach {
        if(it is Container)
            setContainerEnable(it.components,isEnable)

        it.isEnabled = isEnable
    }
}

fun setContainerEnable(components: ArrayList<Component>,isEnable : Boolean) {
    components.forEach {
        if(it is Container)
            setContainerEnable(it.components,isEnable)

        it.isEnabled = isEnable
    }
}