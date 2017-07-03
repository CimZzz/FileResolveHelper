package com.virtualightning.fileresolver

import com.virtualightning.fileresolver.ui.MainUI
import javax.swing.UIManager


fun main(args : Array<String>) {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    MainUI()
}