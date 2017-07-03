package com.virtualightning.fileresolver.dialogs

import com.virtualightning.fileresolver.base.BaseDialog
import com.virtualightning.fileresolver.base.BaseUI
import com.virtualightning.fileresolver.base.UIBuilder
import com.virtualightning.fileresolver.interfaces.ICallback
import java.awt.Dimension
import javax.swing.JButton


val builder = UIBuilder(
        uiName = "Select a file to decode",
        isResizeAble = false,
        size = Dimension(400,800)
)

class FileDialog(baseUI : BaseUI,callback : ICallback<String>) : BaseDialog<String>(builder,baseUI,callback) {
    init{
        val btn = JButton("Quit")
        btn.addActionListener {
            setResultAndQuit(true,"123")
        }
        add(btn)

        setLocationRelativeTo(baseUI)
        isVisible = true
    }


}
