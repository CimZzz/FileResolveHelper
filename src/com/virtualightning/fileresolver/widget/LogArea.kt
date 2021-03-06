package com.virtualightning.fileresolver.widget

import java.awt.Color
import javax.swing.*
import javax.swing.text.*


class LogArea : JTextPane() {
    val redStyle : Style = addStyle("red",null)
    val greenStyle : Style = addStyle("green",null)
    val yellowStyle : Style = addStyle("yellow",null)

    init {
        foreground = Color.WHITE
        StyleConstants.setForeground(redStyle, Color.RED)
        StyleConstants.setForeground(greenStyle, Color.GREEN)
        StyleConstants.setForeground(yellowStyle, Color.YELLOW)
    }

    fun normal(str : String) = document.insertString(document.length, str, null)

    fun error(str : String) = document.insertString(document.length,str + "\n",redStyle)

    fun success(str : String) = document.insertString(document.length,str + "\n",greenStyle)

    fun system(str : String) = document.insertString(document.length,str + "\n",yellowStyle)

    fun clear() = document.remove(0,document.length)
}