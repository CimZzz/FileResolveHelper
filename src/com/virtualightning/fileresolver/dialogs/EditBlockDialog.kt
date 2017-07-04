package com.virtualightning.fileresolver.dialogs

import com.virtualightning.fileresolver.base.BaseDialog
import com.virtualightning.fileresolver.base.BaseUI
import com.virtualightning.fileresolver.base.UIBuilder
import com.virtualightning.fileresolver.entity.Block
import com.virtualightning.fileresolver.entity.Format
import com.virtualightning.fileresolver.environment.Context
import com.virtualightning.fileresolver.interfaces.NullCallback
import com.virtualightning.fileresolver.widget.BlockListModel
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import javax.swing.*


private val builder = UIBuilder(
        uiName = "Block manager",
        isResizeAble = false,
        size = Dimension(500,300)
)

class EditBlockDialog(baseUI : BaseUI,val block : Block) : BaseDialog<Any>(builder,baseUI,NullCallback) {
    val upBtn : JButton
    val downBtn : JButton
    val newBtn : JButton
    val editBtn : JButton
    val delBtn : JButton

    val formatTable: JTable = JTable()
    var lastSelection : Int = -1

    init {

        val operatorPanel = JPanel()
        operatorPanel.layout = BoxLayout(operatorPanel,BoxLayout.Y_AXIS)

        newBtn = JButton("New")
        newBtn.alignmentX = Component.CENTER_ALIGNMENT
        newBtn.maximumSize = Dimension(80,30)
        newBtn.addActionListener {
            NewBlockDialog(baseUI,{
                _,block,_->
                block?.let {
                    formatTable.updateUI()
                }
            })
        }
        operatorPanel.add(newBtn)

        editBtn = JButton("Edit")
        editBtn.alignmentX = Component.CENTER_ALIGNMENT
        editBtn.maximumSize = Dimension(80,30)
        editBtn.isEnabled = false
        operatorPanel.add(editBtn)

        delBtn = JButton("Delete")
        delBtn.alignmentX = Component.CENTER_ALIGNMENT
        delBtn.maximumSize = Dimension(80,30)
        delBtn.isEnabled = false
        delBtn.addActionListener {
            formatTable.updateUI()
            handleListSelectionEnable(true)
        }
        operatorPanel.add(delBtn)

        upBtn = JButton("Up")
        upBtn.alignmentX = Component.CENTER_ALIGNMENT
        upBtn.maximumSize = Dimension(80,30)
        upBtn.isEnabled = false
        upBtn.addActionListener {
            formatTable.updateUI()
        }
        operatorPanel.add(upBtn)

        downBtn = JButton("Down")
        downBtn.alignmentX = Component.CENTER_ALIGNMENT
        downBtn.maximumSize = Dimension(80,30)
        downBtn.isEnabled = false
        downBtn.addActionListener {
            formatTable.updateUI()
        }
        operatorPanel.add(downBtn)

//        formatTable.model = BlockListModel(Context.protocol!!.blockList)
//        formatTable.addListSelectionListener {
//            handleListSelectionEnable(false)
//        }

        contentPane.layout = BorderLayout()
        contentPane.add(formatTable,BorderLayout.CENTER)
        contentPane.add(operatorPanel,BorderLayout.EAST)

        isModal = true
        setLocationRelativeTo(baseUI)
        isVisible = true
    }

    private fun handleListSelectionEnable(sizeChange : Boolean) {
//        if(!sizeChange && lastSelection == formatTable.selectedIndex)
//            return

//        lastSelection = formatTable.selectedIndex

        val size = Context.protocol?.blockList?.size?: return

        if(size == 0) {
            delBtn.isEnabled = false
            editBtn.isEnabled = false
            upBtn.isEnabled = false
            downBtn.isEnabled = false
        }
        else if(lastSelection == 0) {
            delBtn.isEnabled = true
            editBtn.isEnabled = true
            upBtn.isEnabled = false
            downBtn.isEnabled = size > 1
        }
        else if(lastSelection == size - 1) {
            delBtn.isEnabled = true
            editBtn.isEnabled = true
            upBtn.isEnabled = size > 1
            downBtn.isEnabled = false
        }
        else if(lastSelection >= size) {
//            formatTable.selectedIndex = -1
            delBtn.isEnabled = false
            editBtn.isEnabled = false
            upBtn.isEnabled = false
            downBtn.isEnabled = false
        }
        else {
            delBtn.isEnabled = true
            editBtn.isEnabled = true
            upBtn.isEnabled = true
            downBtn.isEnabled = true
        }
    }
}