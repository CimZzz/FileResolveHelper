package com.virtualightning.fileresolver.ui

import com.virtualightning.fileresolver.base.BaseUI
import com.virtualightning.fileresolver.base.UIBuilder
import com.virtualightning.fileresolver.dialogs.BlockManagerDialog
import com.virtualightning.fileresolver.dialogs.NewProtocolDialog
import com.virtualightning.fileresolver.entity.Block
import com.virtualightning.fileresolver.entity.Protocol
import com.virtualightning.fileresolver.environment.*
import com.virtualightning.fileresolver.utils.Info
import com.virtualightning.fileresolver.utils.newMenu
import com.virtualightning.fileresolver.utils.newMenuItem
import com.virtualightning.fileresolver.widget.ByteTableModel
import com.virtualightning.fileresolver.widget.LeftTreeCellRenderer
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
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
    val blockManageItem : MenuItem

    val dataTree : JTree

    val filePathLabel : JLabel
    val curByteLabel : JLabel
    val totalByteLabel : JLabel
    val remainingByteLabel: JLabel
    val curBitLabel : JLabel
    val radixSpinner : JSpinner
    val bytesShowCountsSpinner : JSpinner
    val byteTable : JTable
    val byteTableModel : ByteTableModel

    val logArea : JTextArea
    val logField : JTextField

    init {
        //Initialize menu bar
        val fileMenu = newMenu("File")
        newMenuItem("Open File",fileMenu,KeyEvent.VK_O).addActionListener Listener@{
            if(Context.isOpenFile) {
                if (showWarnDialog("Are you sure of that ? ", "Open file") == 2)
                    return@Listener

                closeFile()
            }

            openFile(openFileDialog(filter = { it?.isFile?:false })?:return@Listener)
        }

        closeMenuItem = newMenuItem("Close File",fileMenu)
        closeMenuItem.isEnabled = false
        closeMenuItem.addActionListener Listener@{
            if(Context.isHasProtocol) {
                if (showWarnDialog("Are you sure of that ? ", "Close file") == 2)
                    return@Listener
            }

            closeFile()
        }
        fileMenu.addSeparator()


        val operatorMenu = newMenu("Operator")
        newMenuItem("Create a protocol",operatorMenu,KeyEvent.VK_P).addActionListener Listener@ {
            if(Context.isHasProtocol) {
                if(showWarnDialog("Create a new protocol causes the before protocol information to miss,are you sure of that ? ","Create a protocol") == 2) {
                    return@Listener
                }

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
                if(showWarnDialog("Open a protocol causes the before protocol information to miss,are you sure of that ? ","Open a protocol") == 2) {
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








        byteTable = JTable()
        byteTableModel = ByteTableModel()
        byteTable.model = byteTableModel




        val bottomPanel = JPanel()
        bottomPanel.preferredSize = Dimension(0,200)
        logArea = JTextArea()
        logArea.background = Color(0x97,0x97,0x97)
        logArea.foreground = Color.WHITE
        logArea.isEditable = false

        logField = JTextField()
        logField.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) {

            }

            override fun keyPressed(e: KeyEvent?) {
            }

            override fun keyReleased(e: KeyEvent?) {
                if (e!!.keyCode == KeyEvent.VK_ENTER) {
                    logArea.append("> ${logField.text}\n")
                    logField.text = ""
                }
            }

        })

        bottomPanel.layout = BorderLayout()
        bottomPanel.add(logArea,BorderLayout.CENTER)
        bottomPanel.add(logField,BorderLayout.SOUTH)
        bottomPanel.border = BorderFactory.createTitledBorder("控制台")




        rightPanel.layout = BorderLayout()
        rightPanel.add(logPanel,BorderLayout.NORTH)
        val bytesTablePane = JScrollPane(byteTable)
        bytesTablePane.isWheelScrollingEnabled = true
        rightPanel.add(bytesTablePane,BorderLayout.CENTER)
        rightPanel.add(bottomPanel,BorderLayout.SOUTH)

        topSplitPane.leftComponent = leftPanel
        topSplitPane.rightComponent = rightPanel
        topSplitPane.isContinuousLayout = true
        topSplitPane.dividerLocation = 200
        topSplitPane.dividerSize = 1


        container.add(topSplitPane)
//
//        /*TEST*/
//        val protocol = Protocol("HTTP")
//        protocol.addBlock(Block("Header"))
//        createNewProtocol(protocol)
//        /*TEST*/

        setLocationRelativeTo(null)
        isVisible = true
    }

    fun openFile(file :File) {
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
    }


    fun resetFile() {

    }

    fun closeFile() {
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
    }

    fun createNewProtocol(protocol: Protocol) {
        Context.createNewProtocol(protocol)

        saveProtocolItem.isEnabled = true
        blockManageItem.isEnabled = true

        dataTree.model = DefaultTreeModel(protocol.rootNode)
        dataTree.updateUI()
    }

    fun openProtocol(file : File) {
    }

    fun resetProtocol() {
        Context.resetProtocol()
    }

    fun closeProtocol() {
        saveProtocolItem.isEnabled = false
        blockManageItem.isEnabled = false

        Context.closeProtocol()
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

    fun updateTree() {
        dataTree.updateUI()
    }
}