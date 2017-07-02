package com.virtualightning.fileresolver.ui

import com.virtualightning.fileresolver.base.BaseUI
import com.virtualightning.fileresolver.base.UIBuilder
import com.virtualightning.fileresolver.dialogs.FileDialog
import com.virtualightning.fileresolver.utils.newMenu
import com.virtualightning.fileresolver.utils.newMenuItem
import java.awt.MenuItem
import java.awt.Toolkit

val builder = UIBuilder(
        uiName = "FileResolverHelper",
        isResizeAble = false,
        size = Toolkit.getDefaultToolkit().screenSize
)

class MainUI : BaseUI(builder) {
    val closeMenu : MenuItem

    init {
        val fileMenu = newMenu("File")
        newMenuItem("Open File",fileMenu).addActionListener({
            e->
            FileDialog(this,{
                result,data->

            })
        })
        closeMenu = newMenuItem("Close File",fileMenu)
        fileMenu.addSeparator()




        addMenu(fileMenu)

        setLocationRelativeTo(null)
    }

}