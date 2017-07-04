package com.virtualightning.fileresolver.ui

import com.virtualightning.fileresolver.base.BaseUI
import com.virtualightning.fileresolver.base.UIBuilder
import com.virtualightning.fileresolver.dialogs.BlockManagerDialog
import com.virtualightning.fileresolver.dialogs.NewProtocolDialog
import com.virtualightning.fileresolver.entity.Protocol
import com.virtualightning.fileresolver.environment.*
import com.virtualightning.fileresolver.utils.Info
import com.virtualightning.fileresolver.utils.newMenu
import com.virtualightning.fileresolver.utils.newMenuItem
import com.virtualightning.fileresolver.widget.ByteTableModel
import com.virtualightning.fileresolver.widget.LeftTreeCellRenderer
import java.awt.*
import java.awt.event.KeyEvent
import java.io.File
import javax.swing.*
import javax.swing.tree.DefaultTreeModel

private val builder = UIBuilder(
        uiName = "FileResolverHelper",
        isResizeAble = true,
        size = Dimension(Toolkit.getDefaultToolkit().screenSize.width / 3 * 2,Toolkit.getDefaultToolkit().screenSize.height / 3 * 2)
)

class MainUI : BaseUI(builder) {
    val closeMenuItem : MenuItem

    val saveProtocolItem : MenuItem
    val parseBlockItem : MenuItem
    val parseParamsItem : MenuItem
    val blockManageItem : MenuItem

    val dataTree : JTree

    val filePathLabel : JLabel
    val curByteLabel : JLabel
    val totalByteLabel : JLabel
    val remainingByteLabel: JLabel
    val curBitLabel : JLabel
    val radixSpinner : JSpinner
    val bytesShowCountsSpinner : JSpinner
    val readBlockBtn : JButton
    val readParamsBtn : JButton
    val byteTable : JTable
    val byteTableModel : ByteTableModel

    init {
        //Initialize menu bar
        val fileMenu = newMenu("File")
        newMenuItem("Open File",fileMenu,KeyEvent.VK_O).addActionListener Listener@{
            if(Context.isOpenFile) {
                if (showWarnDialog("Open a new file causes the before file to close and the protocol data to miss,are you sure of that ? ", "Open file") == 2)
                    return@Listener

                if(Context.isHasProtocol)
                    resetProtocol()

                closeFile()
            }

            openFile(openFileDialog(filter = { it?.isFile?:false })?:return@Listener)
        }

        closeMenuItem = newMenuItem("Close File",fileMenu)
        closeMenuItem.isEnabled = false
        closeMenuItem.addActionListener Listener@{
            if(Context.isHasProtocol) {
                if (showWarnDialog("Close file causes the protocol data to miss,are you sure of that ? ", "Close file") == 2)
                    return@Listener

                resetProtocol()
            }

            closeFile()
        }
        fileMenu.addSeparator()


        val operatorMenu = newMenu("Operator")
        newMenuItem("Create a protocol",operatorMenu,KeyEvent.VK_P).addActionListener Listener@ {
            if(Context.isHasProtocol) {
                if(showWarnDialog("Create a new protocol causes the file to reset and the before protocol information to miss,are you sure of that ? ","Create a protocol") == 2) {
                    return@Listener
                }
                if(Context.isOpenFile)
                    resetFile()

                closeProtocol()
            }

            NewProtocolDialog(this,{
                result, protocol, msg ->
                if(result && protocol != null)
                    createNewProtocol(protocol)
            })
        }
        newMenuItem("Open a protocol",operatorMenu,KeyEvent.VK_L).addActionListener Listener@ {
            if(Context.isHasProtocol) {
                if(showWarnDialog("Open a protocol causes the file to reset and the before protocol information to miss,are you sure of that ? ","Open a protocol") == 2) {
                    return@Listener
                }
                if(Context.isOpenFile)
                    resetFile()

                closeProtocol()
            }

            openProtocol(openFileDialog()?:return@Listener)
        }
        saveProtocolItem = newMenuItem("Save protocol",operatorMenu,KeyEvent.VK_N)
        saveProtocolItem.isEnabled = false
        operatorMenu.addSeparator()

        blockManageItem = newMenuItem("Block Manager",operatorMenu,KeyEvent.VK_B)
        blockManageItem.isEnabled = false
        blockManageItem.addActionListener {
            BlockManagerDialog(this,{
                _, _, _ ->
                updateTree()
            })
        }

        operatorMenu.addSeparator()

        parseBlockItem = newMenuItem("Parse next block",operatorMenu,KeyEvent.VK_F6)
        parseBlockItem.isEnabled = false
        parseBlockItem.addActionListener {
            readBlock()
        }

        parseParamsItem = newMenuItem("Parse next params in block",operatorMenu,KeyEvent.VK_F7)
        parseParamsItem.isEnabled = false
        parseParamsItem.addActionListener {
            readParams()
        }




        addMenu(fileMenu,operatorMenu)


        //Initialize main ui
        val container = contentPane
        val topSplitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
        val leftPanel = JPanel()
        dataTree = JTree()
        dataTree.cellRenderer = LeftTreeCellRenderer()
        dataTree.minimumSize = Dimension(50,50)




        leftPanel.layout = BorderLayout()
        leftPanel.add(dataTree,BorderLayout.CENTER)


        val rightPanel = JPanel()

        val logPanel = JPanel()
        val gridLayout = GridBagLayout()
        val constraint = GridBagConstraints()
        logPanel.layout = gridLayout
        logPanel.preferredSize = Dimension(0,100)

        constraint.fill = GridBagConstraints.HORIZONTAL

        var tempLabel : JLabel = JLabel()
        tempLabel.text = "Current File :"
        constraint.gridx = 0
        constraint.gridy = 0
        constraint.gridwidth = 2
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tempLabel,constraint)
        logPanel.add(tempLabel)

        filePathLabel = JLabel()
        filePathLabel.text = "--"
        constraint.gridx = 2
        constraint.gridy = 0
        constraint.gridwidth = 8
        constraint.weightx = 1.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(filePathLabel,constraint)
        logPanel.add(filePathLabel)

        tempLabel = JLabel()
        tempLabel.text = "Current Byte Location:"
        constraint.gridx = 0
        constraint.gridy = 1
        constraint.gridwidth = 3
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tempLabel,constraint)
        logPanel.add(tempLabel)

        curByteLabel = JLabel()
        curByteLabel.text = "--"
        constraint.gridx = 3
        constraint.gridy = 1
        constraint.gridwidth = 2
        constraint.weightx = 1.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(curByteLabel,constraint)
        logPanel.add(curByteLabel)

        tempLabel = JLabel()
        tempLabel.text = "Total Bytes:"
        constraint.gridx = 5
        constraint.gridy = 1
        constraint.gridwidth = 3
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tempLabel,constraint)
        logPanel.add(tempLabel)

        totalByteLabel = JLabel()
        totalByteLabel.text = "--"
        constraint.gridx = 8
        constraint.gridy = 1
        constraint.gridwidth = 2
        constraint.weightx = 1.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(totalByteLabel,constraint)
        logPanel.add(totalByteLabel)

        tempLabel = JLabel()
        tempLabel.text = "Remaining Bits Of Current Byte:"
        constraint.gridx = 0
        constraint.gridy = 2
        constraint.gridwidth = 3
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tempLabel,constraint)
        logPanel.add(tempLabel)

        curBitLabel = JLabel()
        curBitLabel.text = "--"
        constraint.gridx = 3
        constraint.gridy = 2
        constraint.gridwidth = 2
        constraint.weightx = 1.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(curBitLabel,constraint)
        logPanel.add(curBitLabel)

        tempLabel = JLabel()
        tempLabel.text = "Remaining Bytes:"
        constraint.gridx = 5
        constraint.gridy = 2
        constraint.gridwidth = 3
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tempLabel,constraint)
        logPanel.add(tempLabel)

        remainingByteLabel = JLabel()
        remainingByteLabel.text = "--"
        constraint.gridx = 8
        constraint.gridy = 2
        constraint.gridwidth = 2
        constraint.weightx = 1.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(remainingByteLabel,constraint)
        logPanel.add(remainingByteLabel)


        tempLabel = JLabel()
        tempLabel.text = "Display Byte Count:"
        constraint.gridx = 0
        constraint.gridy = 3
        constraint.gridwidth = 3
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tempLabel,constraint)
        logPanel.add(tempLabel)

        bytesShowCountsSpinner = JSpinner()
        bytesShowCountsSpinner.model = SpinnerListModel(byteCountArray)
        bytesShowCountsSpinner.isEnabled = false
        bytesShowCountsSpinner.addChangeListener {
            e->
            changeByteCount((e.source as JSpinner).value as Int)
        }
        constraint.gridx = 3
        constraint.gridy = 3
        constraint.gridwidth = 2
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(bytesShowCountsSpinner,constraint)
        logPanel.add(bytesShowCountsSpinner)

        tempLabel = JLabel()
        tempLabel.text = "Display Radix:"
        constraint.gridx = 5
        constraint.gridy = 3
        constraint.gridwidth = 3
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tempLabel,constraint)
        logPanel.add(tempLabel)

        radixSpinner = JSpinner()
        radixSpinner.model = SpinnerListModel(radixArray)
        radixSpinner.value = "Hexadecimal"
        radixSpinner.isEnabled = false
        radixSpinner.addChangeListener {
            e->
            changeRadix((e.source as JSpinner).value as String)
        }
        constraint.gridx = 8
        constraint.gridy = 3
        constraint.gridwidth = 2
        constraint.weightx = 1.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(radixSpinner,constraint)
        logPanel.add(radixSpinner)

        val operatorPanel = JPanel()
        constraint.gridx = 0
        constraint.gridy = 4
        constraint.gridwidth = 10
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        operatorPanel.layout = FlowLayout(FlowLayout.LEFT)
        readBlockBtn = JButton("Read next block")
        readBlockBtn.isEnabled = false
        readBlockBtn.addActionListener {
            readBlock()
        }

        readParamsBtn = JButton("Read next params")
        readParamsBtn.isEnabled = false
        readParamsBtn.addActionListener {
            readParams()
        }
        operatorPanel.add(readBlockBtn)
        operatorPanel.add(readParamsBtn)

        gridLayout.setConstraints(operatorPanel,constraint)
        logPanel.add(operatorPanel)














        byteTable = JTable()
        byteTableModel = ByteTableModel()
        byteTable.model = byteTableModel

        rightPanel.layout = BorderLayout()
        rightPanel.add(logPanel,BorderLayout.NORTH)
        val bytesTablePane = JScrollPane(byteTable)
        bytesTablePane.isWheelScrollingEnabled = true
        rightPanel.add(bytesTablePane,BorderLayout.CENTER)

        topSplitPane.leftComponent = leftPanel
        topSplitPane.rightComponent = rightPanel
        topSplitPane.isContinuousLayout = true
        topSplitPane.dividerLocation = 200
        topSplitPane.dividerSize = 1


        container.add(topSplitPane)


        setLocationRelativeTo(null)
        isVisible = true
    }

    private fun openFile(file :File) {
        if(!Context.openFile(file)) {
            showAlertDialog("Failed to open file , please try again","Open File Failed!")
            return
        }

        val byteDataList = Context.readBytes()

        if(byteDataList == null) {
            Context.closeFile()
            showAlertDialog("Failed to open file , please try again","Open File Failed!")
            return
        }


        byteTableModel.byteDataList = byteDataList
        byteTableModel.changeRadix()
        byteTable.updateUI()


        closeMenuItem.isEnabled = true

        bytesShowCountsSpinner.isEnabled = true
        radixSpinner.isEnabled = true

        filePathLabel.text = file.absolutePath
        curByteLabel.text = "${Context.currentBytes}"
        curBitLabel.text = "8"
        totalByteLabel.text = "${Context.totalBytes}"
        remainingByteLabel.text = "${Context.totalBytes}"

        Info("Open File : ${Context.selectFile!!.absolutePath}" )

        checkBeginStatus()
    }


    private fun resetFile() {

    }

    private fun closeFile() {
        closeMenuItem.isEnabled = false

        bytesShowCountsSpinner.isEnabled = false
        radixSpinner.isEnabled = false


        filePathLabel.text = "--"
        curByteLabel.text = "--"
        curBitLabel.text = "--"
        totalByteLabel.text = "--"
        remainingByteLabel.text = "--"
        byteTableModel.byteDataList = null
        byteTable.updateUI()

        Info("Close File : ${Context.selectFile!!.absolutePath}")
        Context.closeFile()

        checkEndStatus()
    }

    private fun createNewProtocol(protocol: Protocol) {
        Context.createNewProtocol(protocol)

        saveProtocolItem.isEnabled = true
        blockManageItem.isEnabled = true

        dataTree.model = DefaultTreeModel(protocol.rootNode)
        dataTree.updateUI()

        checkBeginStatus()
    }

    private fun openProtocol(file : File) {
        checkBeginStatus()
    }

    private fun resetProtocol() {
        Context.resetProtocol()
    }

    private fun closeProtocol() {
        saveProtocolItem.isEnabled = false
        blockManageItem.isEnabled = false

        Context.closeProtocol()
        checkEndStatus()
    }

    private fun checkBeginStatus() {
        if(!Context.isOpenFile || !Context.isHasProtocol)
            return

        readBlockBtn.isEnabled = true
        readParamsBtn.isEnabled = true
        parseBlockItem.isEnabled = true
        parseParamsItem.isEnabled = true
    }

    private fun checkEndStatus() {
        readBlockBtn.isEnabled = false
        readParamsBtn.isEnabled = false
        parseBlockItem.isEnabled = false
        parseParamsItem.isEnabled = false
    }

    private fun changeByteCount(count : Int) {
        val lastCount = Context.byteCounts
        Context.byteCounts = count

        val byteDataList = Context.readBytes()

        if(byteDataList == null) {
            showAlertDialog("Failed to read bytes")
            bytesShowCountsSpinner.value = lastCount
            return
        }

        byteTableModel.byteDataList = byteDataList
        byteTableModel.changeRadix()
        byteTable.updateUI()

        Info(count)
    }
    private fun changeRadix(radix : String) {
        Context.radix = radix
        byteTableModel.changeRadix()
        byteTable.updateUI()


        Info(radix)
    }

    private fun updateTree() {
        dataTree.updateUI()
    }

    private fun readBlock() {
        Info("Read Block")
    }

    private fun readParams() {
        Info("Read Params In Block")
    }
}