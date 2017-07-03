package com.virtualightning.fileresolver.base

import java.awt.Menu
import java.awt.MenuBar
import javax.swing.JFrame

@Suppress("LeakingThis")
abstract class BaseUI(builder: UIBuilder) : JFrame(builder.uiName) {
    init {
        isResizable = builder.isResizeAble
        size = builder.size
        if(builder.minimumSize != null)
            minimumSize = builder.minimumSize

        defaultCloseOperation = EXIT_ON_CLOSE
    }

    protected fun addMenu(vararg menu : Menu) {
        menuBar = MenuBar()
        menu.forEach {
            menuBar.add(it)
        }
    }
}