package com.virtualightning.fileresolver

import com.virtualightning.fileresolver.base.BaseUI
import com.virtualightning.fileresolver.base.UIBuilder
import com.virtualightning.fileresolver.ui.MainUI
import com.virtualightning.fileresolver.utils.Info
import com.virtualightning.fileresolver.widget.CodeArea
import java.awt.BorderLayout
import java.awt.Dimension
import java.io.InputStreamReader
import java.io.StringReader
import javax.swing.JFrame
import javax.swing.UIManager


fun main(args : Array<String>) {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    MainUI()
}