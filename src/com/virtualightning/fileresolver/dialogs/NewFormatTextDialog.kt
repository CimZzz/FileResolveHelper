package com.virtualightning.fileresolver.dialogs

import com.virtualightning.fileresolver.base.BaseDialog
import com.virtualightning.fileresolver.base.BaseUI
import com.virtualightning.fileresolver.base.UIBuilder
import com.virtualightning.fileresolver.entity.Block
import com.virtualightning.fileresolver.entity.Format
import com.virtualightning.fileresolver.entity.Protocol
import com.virtualightning.fileresolver.interfaces.ICallback
import java.awt.*
import javax.swing.*

private val builder = UIBuilder(
        uiName = "Create new protocol",
        isResizeAble = true,
        size = Dimension(400,200)
)

class NewFormatDialog(baseUI : BaseUI, callback : ICallback<Format<*>>) : BaseDialog<Format<*>>(builder,baseUI,callback) {
    init {
        val infoPanel = JPanel()
        infoPanel.layout = BorderLayout()

        val editArea = JTextArea()
        editArea.autoscrolls = true
        editArea.lineWrap = true

        val tips = JLabel()
        tips.foreground = Color.RED
        infoPanel.add(JScrollPane(editArea),BorderLayout.CENTER)
        infoPanel.add(tips,BorderLayout.SOUTH)


        val operatorPanel = JPanel()
        operatorPanel.layout = FlowLayout(FlowLayout.RIGHT)
        val cancelBtn = JButton("取消")
        cancelBtn.addActionListener {
            dispose()
        }
        operatorPanel.add(cancelBtn)
        val confirmBtn = JButton("确定")
        confirmBtn.addActionListener Listener@{
        }
        operatorPanel.add(confirmBtn)

        contentPane.layout = BorderLayout()
        contentPane.add(infoPanel,BorderLayout.CENTER)
        contentPane.add(operatorPanel,BorderLayout.SOUTH)

        isModal = true
        setLocationRelativeTo(baseUI)
        isVisible = true
    }


}