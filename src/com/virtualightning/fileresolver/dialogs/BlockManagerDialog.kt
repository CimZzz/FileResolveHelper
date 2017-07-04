package com.virtualightning.fileresolver.dialogs

import com.virtualightning.fileresolver.base.BaseDialog
import com.virtualightning.fileresolver.base.BaseUI
import com.virtualightning.fileresolver.base.UIBuilder
import com.virtualightning.fileresolver.entity.Block
import com.virtualightning.fileresolver.environment.Context
import com.virtualightning.fileresolver.interfaces.ICallback
import com.virtualightning.fileresolver.utils.Info
import com.virtualightning.fileresolver.widget.BlockListModel
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import java.awt.Component.CENTER_ALIGNMENT
import java.awt.SystemColor.text
import javax.swing.JButton



private val builder = UIBuilder(
        uiName = "Block manager",
        isResizeAble = false,
        size = Dimension(500,300)
)

class BlockManagerDialog(baseUI : BaseUI,callback : ICallback<Any>) : BaseDialog<Any>(builder,baseUI,callback) {
    val upBtn : JButton
    val downBtn : JButton
    val newBtn : JButton
    val editBtn : JButton
    val delBtn : JButton

    val blockList : JList<Block> = JList()
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
                    item ->
                    Context.protocol?.addBlock(item) ?: return@let
                    blockList.updateUI()
                }
            })
        }
        operatorPanel.add(newBtn)

        editBtn = JButton("Edit")
        editBtn.alignmentX = Component.CENTER_ALIGNMENT
        editBtn.maximumSize = Dimension(80,30)
        editBtn.isEnabled = false
        editBtn.addActionListener {
            EditBlockDialog(baseUI,Context.protocol!!.blockList[blockList.selectedIndex])
        }
        operatorPanel.add(editBtn)

        delBtn = JButton("Delete")
        delBtn.alignmentX = Component.CENTER_ALIGNMENT
        delBtn.maximumSize = Dimension(80,30)
        delBtn.isEnabled = false
        delBtn.addActionListener {
            Context.protocol?.removeBlock(blockList.selectedIndex)?: return@addActionListener
            blockList.updateUI()
            handleListSelectionEnable(true)
        }
        operatorPanel.add(delBtn)

        upBtn = JButton("Up")
        upBtn.alignmentX = Component.CENTER_ALIGNMENT
        upBtn.maximumSize = Dimension(80,30)
        upBtn.isEnabled = false
        upBtn.addActionListener {
            Context.protocol?.swapBlock(blockList.selectedIndex,blockList.selectedIndex - 1) ?: return@addActionListener
            blockList.selectedIndex = blockList.selectedIndex - 1
            blockList.updateUI()
        }
        operatorPanel.add(upBtn)

        downBtn = JButton("Down")
        downBtn.alignmentX = Component.CENTER_ALIGNMENT
        downBtn.maximumSize = Dimension(80,30)
        downBtn.isEnabled = false
        downBtn.addActionListener {
            Context.protocol?.swapBlock(blockList.selectedIndex,blockList.selectedIndex + 1) ?: return@addActionListener
            blockList.selectedIndex = blockList.selectedIndex + 1
            blockList.updateUI()
        }
        operatorPanel.add(downBtn)

        blockList.model = BlockListModel(Context.protocol!!.blockList)
        blockList.addListSelectionListener {
            handleListSelectionEnable(false)
        }

        contentPane.layout = BorderLayout()
        contentPane.add(blockList,BorderLayout.CENTER)
        contentPane.add(operatorPanel,BorderLayout.EAST)

        isModal = true
        setLocationRelativeTo(baseUI)
        isVisible = true
    }

    private fun handleListSelectionEnable(sizeChange : Boolean) {
        if(!sizeChange && lastSelection == blockList.selectedIndex)
            return

        lastSelection = blockList.selectedIndex

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
            blockList.selectedIndex = -1
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

    override fun dispose() {
        callBack.invoke(true,null,null)
        super.dispose()
    }
}