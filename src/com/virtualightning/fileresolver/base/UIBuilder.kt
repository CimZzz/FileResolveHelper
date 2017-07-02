package com.virtualightning.fileresolver.base

import java.awt.Dimension

data class UIBuilder(
        val uiName : String,
        val size : Dimension,
        val minimumSize : Dimension? = null,
        val isResizeAble : Boolean = true
)