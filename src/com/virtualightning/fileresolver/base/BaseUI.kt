package com.virtualightning.fileresolver.base

import com.virtualightning.fileresolver.interfaces.IFilter
import jdk.nashorn.internal.scripts.JO
import java.awt.Menu
import java.awt.MenuBar
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.filechooser.FileFilter

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

    protected fun showWarnDialog(msg : String,title : String? = null) : Int{
        return JOptionPane.showConfirmDialog(null,msg,title,JOptionPane.OK_CANCEL_OPTION)
    }

    protected fun showAlertDialog(msg : String,title : String? = null) {
        JOptionPane.showMessageDialog(null,msg,title,JOptionPane.ERROR_MESSAGE)
    }

    protected fun openFileDialog(filePath : String = "~", filter : IFilter? = null) : File?{
        val fileChooser = JFileChooser(filePath)
        fileChooser.fileFilter = object : FileFilter() {
            override fun getDescription(): String = ""
            override fun accept(f: File?): Boolean = filter?.invoke(f) ?: false
        }
        fileChooser.showOpenDialog(null)
        return fileChooser.selectedFile
    }
}