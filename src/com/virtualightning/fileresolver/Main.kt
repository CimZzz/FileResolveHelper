package com.virtualightning.fileresolver

import com.virtualightning.fileresolver.environment.initSchemaTree
import com.virtualightning.fileresolver.ui.MainUI
import javax.swing.UIManager


fun main(args : Array<String>) {
    initSchemaTree()
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    MainUI()
}