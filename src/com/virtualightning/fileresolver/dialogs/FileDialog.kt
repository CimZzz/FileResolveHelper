package com.virtualightning.fileresolver.dialogs

import com.virtualightning.fileresolver.base.BaseDialog
import com.virtualightning.fileresolver.base.BaseUI
import com.virtualightning.fileresolver.base.UIBuilder
import com.virtualightning.fileresolver.interfaces.ICallback
import java.awt.Dimension


val builder = UIBuilder(
        uiName = "Select a file to decode",
        isResizeAble = false,
        size = Dimension(400,800)
)

class FileDialog(baseUI : BaseUI,callback : ICallback) : BaseDialog(builder,baseUI,callback) {
    init{
        setLocationRelativeTo(baseUI)
        isVisible = true
    }
}
