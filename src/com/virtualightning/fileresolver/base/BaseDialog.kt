package com.virtualightning.fileresolver.base

import com.virtualightning.fileresolver.interfaces.ICallback
import javax.swing.*

abstract class BaseDialog<E>(builder: UIBuilder,owner : BaseUI,var callBack : ICallback<E>) : JDialog(owner,builder.uiName) {
    init {
        isResizable = builder.isResizeAble
        size = builder.size
        if(builder.minimumSize != null)
            minimumSize = builder.minimumSize

        defaultCloseOperation = JDialog.DISPOSE_ON_CLOSE
    }


    protected fun setResultAndQuit(result : Boolean,data : E? = null,msg : String? = null) {
        callBack.invoke(result,data,msg)
        dispose()
    }
}