package com.virtualightning.fileresolver.dialogs

import com.virtualightning.fileresolver.base.BaseDialog
import com.virtualightning.fileresolver.base.BaseUI
import com.virtualightning.fileresolver.base.UIBuilder
import com.virtualightning.fileresolver.entity.Block
import com.virtualightning.fileresolver.entity.Format
import com.virtualightning.fileresolver.environment.intLengthTypeArray
import com.virtualightning.fileresolver.environment.intTypeArray
import com.virtualightning.fileresolver.environment.orderArray
import com.virtualightning.fileresolver.interfaces.ICallback
import com.virtualightning.fileresolver.utils.addGridBagComponent
import com.virtualightning.fileresolver.utils.getSeparator
import com.virtualightning.fileresolver.utils.setContainerEnable
import java.awt.*
import java.awt.event.ItemEvent
import javax.swing.*


private val builder = UIBuilder(
        uiName = "Edit Block",
        isResizeAble = false,
        size = Dimension(400,600)
)

class CreateIntFormatDialog(baseUI : BaseUI?, callback : ICallback<Format<*>>) : BaseDialog<Format<*>>(builder,baseUI,callback){
    val infoPanel = JPanel()
    val nameField = JTextField()
    val nameTips = JLabel()
    val orderBox = JComboBox<String>()
    val type = JLabel("Type : ")
    val typeBox = JComboBox<String>()
    val customComponentList = ArrayList<Component>()
    val customCheckBox = JCheckBox("Check it to customize int format")
    val lengthBox = JComboBox<String>()
    val lengthType = JLabel("Int Length Type : ")
    val lengthPanel = JPanel()
    val lengthCardLayout = CardLayout()

    val bitPanel = JPanel()
    val bitField = JTextField()

    val bytePanel = JPanel()
    val byteField = JTextField()

    val otherFormatPanel = JPanel()
    val otherFormatBox = JComboBox<String>()


    init {
        infoPanel.layout = GridBagLayout()

        addGridBagComponent(
                JLabel("Int Name"),
                infoPanel,
                gridX = 0,
                gridY = 0,
                gridWidth = 4,
                weightX = 0.0,
                ipadX = 8)

        addGridBagComponent(
                nameField,
                infoPanel,
                gridX = 4,
                gridY = 0,
                gridWidth = 6,
                weightX = 1.0,
                ipadX = 8)

        nameTips.foreground = Color.RED
        addGridBagComponent(
                nameTips,
                infoPanel,
                gridX = 0,
                gridY = 1,
                gridWidth = 10,
                weightX = 1.0,
                ipadX = 8)


        addGridBagComponent(
                JLabel("Order"),
                infoPanel,
                gridX = 0,
                gridY = 2,
                gridWidth = 4,
                weightX = 0.0,
                ipadX = 8)

        orderArray.forEach { orderBox.addItem(it) }
        addGridBagComponent(
                orderBox,
                infoPanel,
                gridX = 4,
                gridY = 2,
                gridWidth = 6,
                weightX = 1.0,
                ipadX = 8)


        addGridBagComponent(
                getSeparator(Color.GRAY),
                infoPanel,
                gridX = 0,
                gridY = 3,
                gridWidth = 10,
                weightX = 1.0,
                ipadX = 8)


        addGridBagComponent(
                type,
                infoPanel,
                gridX = 0,
                gridY = 4,
                gridWidth = 4,
                weightX = 0.0,
                ipadX = 8)

        intTypeArray.forEach { typeBox.addItem(it) }
        addGridBagComponent(
                typeBox,
                infoPanel,
                gridX = 4,
                gridY = 4,
                gridWidth = 6,
                weightX = 1.0,
                ipadX = 8)


        customCheckBox.addActionListener {
            if(customCheckBox.isSelected) {
                type.isEnabled = false
                typeBox.isEnabled = false
                setContainerEnable(customComponentList,true)
            } else {
                type.isEnabled = true
                typeBox.isEnabled = true
                setContainerEnable(customComponentList,false)
            }
        }
        addGridBagComponent(
                customCheckBox,
                infoPanel,
                gridX = 0,
                gridY = 5,
                gridWidth = 10,
                weightX = 1.0,
                ipadX = 8)


        customComponentList.add(lengthType)
        addGridBagComponent(
                lengthType,
                infoPanel,
                gridX = 0,
                gridY = 6,
                gridWidth = 4,
                weightX = 0.0,
                ipadX = 8)



        customComponentList.add(lengthBox)
        intLengthTypeArray.forEach { lengthBox.addItem(it) }
        lengthBox.addItemListener {
            if(it.stateChange == ItemEvent.SELECTED) {
                lengthCardLayout.show(lengthPanel, intLengthTypeArray[lengthBox.selectedIndex])
            }
        }
        addGridBagComponent(
                lengthBox,
                infoPanel,
                gridX = 4,
                gridY = 6,
                gridWidth = 6,
                weightX = 1.0,
                ipadX = 8)

        customComponentList.add(lengthPanel)
        lengthPanel.background = Color.RED
        lengthPanel.preferredSize = Dimension(0,30)
        addGridBagComponent(
                lengthPanel,
                infoPanel,
                gridX = 0,
                gridY = 7,
                gridWidth = 10,
                weightX = 1.0,
                ipadX = 8)

        lengthPanel.layout = lengthCardLayout


        bitPanel.layout = GridBagLayout()
        addGridBagComponent(
                JLabel("Bit length : "),
                bitPanel,
                gridX = 0,
                gridY = 0,
                gridWidth = 4,
                weightX = 0.0,
                ipadX = 8)
        addGridBagComponent(
                bitField,
                bitPanel,
                gridX = 4,
                gridY = 0,
                gridWidth = 6,
                weightX = 1.0,
                ipadX = 8)
        lengthPanel.add(intLengthTypeArray[0],bitPanel)


        bytePanel.layout = GridBagLayout()
        addGridBagComponent(
                JLabel("Byte length : "),
                bytePanel,
                gridX = 0,
                gridY = 0,
                gridWidth = 4,
                weightX = 0.0,
                ipadX = 8)
        addGridBagComponent(
                byteField,
                bytePanel,
                gridX = 4,
                gridY = 0,
                gridWidth = 6,
                weightX = 1.0,
                ipadX = 8)
        lengthPanel.add(intLengthTypeArray[1],bytePanel)


        otherFormatPanel.layout = GridBagLayout()
        addGridBagComponent(
                JLabel("Byte length : "),
                otherFormatPanel,
                gridX = 0,
                gridY = 0,
                gridWidth = 4,
                weightX = 0.0,
                ipadX = 8)
        otherFormatBox.addItem("1")
        otherFormatBox.addItem("2")
        otherFormatBox.addItem("3")
        addGridBagComponent(
                otherFormatBox,
                otherFormatPanel,
                gridX = 4,
                gridY = 0,
                gridWidth = 6,
                weightX = 1.0,
                ipadX = 8)
        lengthPanel.add(intLengthTypeArray[2],otherFormatPanel)




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
        contentPane.add(infoPanel, BorderLayout.CENTER)
        contentPane.add(operatorPanel, BorderLayout.SOUTH)



        /*根据实际情况设置*/
        setContainerEnable(customComponentList,false)

        isModal = true
        setLocationRelativeTo(baseUI)
        isVisible = true
    }
}