package com.virtualightning.fileresolver.dialogs

import com.virtualightning.fileresolver.base.BaseDialog
import com.virtualightning.fileresolver.base.BaseUI
import com.virtualightning.fileresolver.base.UIBuilder
import com.virtualightning.fileresolver.entity.Protocol
import com.virtualightning.fileresolver.interfaces.ICallback
import java.awt.*
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

private val builder = UIBuilder(
        uiName = "Create new protocol",
        isResizeAble = true,
        size = Dimension(400,100)
)

class NewProtocolDialog(baseUI : BaseUI, callback : ICallback<Protocol>) : BaseDialog<Protocol>(builder,baseUI,callback) {
    init {
        val infoPanel = JPanel()
        val gridLayout = GridBagLayout()
        val constraint = GridBagConstraints()
        infoPanel.layout = gridLayout
        constraint.fill = GridBagConstraints.HORIZONTAL

        var tempLabel = JLabel("Protocol Name : ")
        constraint.gridx = 0
        constraint.gridy = 0
        constraint.gridwidth = 2
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tempLabel,constraint)
        infoPanel.add(tempLabel)

        val input = JTextField()
        constraint.gridx = 2
        constraint.gridy = 0
        constraint.gridwidth = 8
        constraint.weightx = 1.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(input,constraint)
        infoPanel.add(input)

        val tips = JLabel()
        tips.foreground = Color.RED
        constraint.gridx = 0
        constraint.gridy = 1
        constraint.gridwidth = 10
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tips,constraint)
        infoPanel.add(tips)


        val operatorPanel = JPanel()
        operatorPanel.layout = FlowLayout(FlowLayout.RIGHT)
        val cancelBtn = JButton("取消")
        cancelBtn.addActionListener {
            dispose()
        }
        operatorPanel.add(cancelBtn)
        val confirmBtn = JButton("确定")
        confirmBtn.addActionListener Listener@{
            val protocolName = input.text

            if(!protocolName.matches(Regex("^[a-zA-Z0-9]{1,11}$"))) {
                tips.text = "Protocol name must be a string consisting of numbers,letters,and the length less than 11 "
                return@Listener
            }

            setResultAndQuit(true,Protocol(protocolName))
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