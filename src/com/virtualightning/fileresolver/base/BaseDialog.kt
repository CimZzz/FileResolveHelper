package com.virtualightning.fileresolver.base

import javax.swing.*

abstract class BaseDialog(builder: UIBuilder,owner : BaseUI,var callBack : ICallback) : JDialog(owner) {
    init {
        isResizable = builder.isResizeAble
        size = builder.size
        if(builder.minimumSize != null)
            minimumSize = builder.minimumSize

        defaultCloseOperation = JDialog.EXIT_ON_CLOSE
        isVisible = true
    }
}