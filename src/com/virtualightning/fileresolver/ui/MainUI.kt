package com.virtualightning.fileresolver.ui

import com.virtualightning.fileresolver.base.BaseUI
import com.virtualightning.fileresolver.base.UIBuilder
import com.virtualightning.fileresolver.dialogs.BlockManagerDialog
import com.virtualightning.fileresolver.dialogs.NewProtocolDialog
import com.virtualightning.fileresolver.entity.ByteData
import com.virtualightning.fileresolver.entity.Protocol
import com.virtualightning.fileresolver.environment.*
import com.virtualightning.fileresolver.proxies.FileReadable
import com.virtualightning.fileresolver.schema.syntax.FacadeSyntax
import com.virtualightning.fileresolver.utils.Info
import com.virtualightning.fileresolver.utils.newMenu
import com.virtualightning.fileresolver.utils.newMenuItem
import com.virtualightning.fileresolver.widget.ByteTableModel
import com.virtualightning.fileresolver.widget.LeftTreeCellRenderer
import com.virtualightning.fileresolver.widget.LogArea
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.File
import java.util.*
import javax.swing.*
import javax.swing.tree.DefaultTreeModel
import kotlin.collections.ArrayList

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
//    val remainBitLabel: JLabel
    val radixBox: JComboBox<String>
    val bytesShowCountsBox: JComboBox<Int>
    val byteTable : JTable
    val byteTableModel : ByteTableModel

    val logArea : LogArea
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
                if (showWarnDialog("Open a protocol causes the before protocol information to miss,are you sure of that ? ", "Open a protocol") == 2) {
                    return@Listener
                }
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

//        tempLabel = JLabel()
//        tempLabel.text = "Remaining Bits Of Current Byte:"
//        constraint.gridx = 0
//        constraint.gridy = 2
//        constraint.gridwidth = 3
//        constraint.weightx = 0.0
//        constraint.weighty = 0.0
//        constraint.ipadx = 8
//        gridLayout.setConstraints(tempLabel,constraint)
//        logPanel.add(tempLabel)

//        remainBitLabel = JLabel()
//        remainBitLabel.text = "--"
//        constraint.gridx = 3
//        constraint.gridy = 2
//        constraint.gridwidth = 2
//        constraint.weightx = 1.0
//        constraint.weighty = 0.0
//        constraint.ipadx = 8
//        gridLayout.setConstraints(remainBitLabel,constraint)
//        logPanel.add(remainBitLabel)

        tempLabel = JLabel()
        tempLabel.text = "Remaining Bytes:"
        constraint.gridx = 0
        constraint.gridy = 2
        constraint.gridwidth = 2
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tempLabel,constraint)
        logPanel.add(tempLabel)

        remainingByteLabel = JLabel()
        remainingByteLabel.text = "--"
        constraint.gridx = 2
        constraint.gridy = 2
        constraint.gridwidth = 8
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

        bytesShowCountsBox = JComboBox(byteCountArray)
        bytesShowCountsBox.isEnabled = false
        bytesShowCountsBox.addItemListener {
            e->
            changeByteCount(e.item as Int)
        }
        constraint.gridx = 3
        constraint.gridy = 3
        constraint.gridwidth = 2
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(bytesShowCountsBox,constraint)
        logPanel.add(bytesShowCountsBox)

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

        radixBox = JComboBox(radixArray)
        radixBox.selectedItem = radixArray[2]
        radixBox.isEnabled = false
        radixBox.addItemListener {
            e->
            changeRadix(e.item as String)
        }
        constraint.gridx = 8
        constraint.gridy = 3
        constraint.gridwidth = 2
        constraint.weightx = 1.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(radixBox,constraint)
        logPanel.add(radixBox)








        byteTable = JTable()
        byteTableModel = ByteTableModel(SourceProxy.byteDataArray)
        byteTable.model = byteTableModel




        val bottomPanel = JPanel()
        bottomPanel.preferredSize = Dimension(0,200)
        logArea = LogArea()
        logArea.background = Color(0x2F,0x2F,0x2F)
        logArea.isEditable = false
        logArea.autoscrolls = true

        logField = JTextField()
        logField.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) {

            }

            override fun keyPressed(e: KeyEvent?) {
            }

            override fun keyReleased(e: KeyEvent?) {
                if(e == null)
                    return

                onLogFieldKeyEvent(e)
            }

        })

        bottomPanel.layout = BorderLayout()
        bottomPanel.add(JScrollPane(logArea),BorderLayout.CENTER)
        bottomPanel.add(logField,BorderLayout.SOUTH)
        bottomPanel.border = BorderFactory.createTitledBorder("控制台")


        rightPanel.layout = BorderLayout()
        rightPanel.add(logPanel,BorderLayout.NORTH)

        val bytesTablePane = JScrollPane(byteTable)
        bytesTablePane.isWheelScrollingEnabled = true

        val byteSplitPane = JSplitPane(JSplitPane.VERTICAL_SPLIT)
        byteSplitPane.topComponent = bytesTablePane
        byteSplitPane.bottomComponent = bottomPanel
        byteSplitPane.isContinuousLayout = true
        byteSplitPane.dividerLocation = 200
        byteSplitPane.dividerSize = 1

        rightPanel.add(byteSplitPane,BorderLayout.CENTER)

        topSplitPane.leftComponent = leftPanel
        topSplitPane.rightComponent = rightPanel
        topSplitPane.isContinuousLayout = true
        topSplitPane.dividerLocation = 200
        topSplitPane.dividerSize = 1


        container.add(topSplitPane)


        SourceProxy.callback = {
            code,any->
            when(code) {
                AbstractReadableCallbackCode.TOTAL_LENGTH->{
                    val value = any as Long
                    if(value == -1L)
                        totalByteLabel.text = "--"
                    else totalByteLabel.text = "$value"
                }
                AbstractReadableCallbackCode.CURRENT_POSITION->{
                    val value = any as Long
                    if(value == -1L)
                        curByteLabel.text = "--"
                    else curByteLabel.text = "$value"
                }
                AbstractReadableCallbackCode.REMAIN_BYTE_COUNT->{
                    val value = any as Long
                    if(value == -1L)
                        remainingByteLabel.text = "--"
                    else remainingByteLabel.text = "$value"
                }
//                AbstractReadableCallbackCode.REMAIN_BIT_COUNT->{
//                    val value = any as Int
//                    if(value == -1)
//                        remainBitLabel.text = "--"
//                    else remainBitLabel.text = "$value"
//                }
                AbstractReadableCallbackCode.UPDATE_DATA_TABLE-> byteTable.updateUI()
                AbstractReadableCallbackCode.CLEAR-> {
                    totalByteLabel.text = "--"
                    curByteLabel.text = "--"
                    remainingByteLabel.text = "--"
//                    remainBitLabel.text = "--"
                    filePathLabel.text = "--"
                    byteTable.updateUI()
                }
                AbstractReadableCallbackCode.ERROR->showAlertDialog(any as String,"Error")
            }

        }

        setLocationRelativeTo(null)
        isVisible = true
    }

    private fun onLogFieldKeyEvent(e : KeyEvent) {
        when(e.keyCode) {
            KeyEvent.VK_ENTER-> {
                val command = logField.text
                logArea.normal("> $command \n")
                QuickCommand.pushQuickCommand(command)
                FacadeSyntax.resolve(command,{
                    code,msg->
                    when(code) {
                        SyntaxCallbackCode.SUCCESS -> logArea.success(msg)
                        SyntaxCallbackCode.ERROR -> logArea.error(msg)
                        SyntaxCallbackCode.CLEAR -> logArea.clear()
                    }
                })
                logField.text = ""
            }
            KeyEvent.VK_UP-> {
                val command = QuickCommand.forwardCommand()?:return
                logField.text = command
            }
            KeyEvent.VK_DOWN-> {
                val command = QuickCommand.nextCommand()?:return
                logField.text = command
            }
        }
    }

    /* Source */

    fun openFile(file :File) {
        if(SourceProxy.openSource(FileReadable(file))) {
            filePathLabel.text = file.absolutePath
            closeMenuItem.isEnabled = true
            bytesShowCountsBox.isEnabled = true
            radixBox.isEnabled = true

            logArea.system("Open file : ${file.absolutePath}")
        }
    }

    fun closeFile() {
        SourceProxy.closeSource()
        closeMenuItem.isEnabled = false
        bytesShowCountsBox.isEnabled = false
        radixBox.isEnabled = false

        logArea.system("Closed file")
    }

    /* Protocol */

    fun createNewProtocol(protocol: Protocol) {
        Context.createNewProtocol(protocol)

        saveProtocolItem.isEnabled = true
        blockManageItem.isEnabled = true

        dataTree.model = DefaultTreeModel(protocol.rootNode)
        dataTree.updateUI()
    }

    fun openProtocol(file : File) {
    }

    fun closeProtocol() {
        saveProtocolItem.isEnabled = false
        blockManageItem.isEnabled = false

        Context.closeProtocol()
    }






    private fun changeByteCount(count : Int) {
        SourceProxy.byteCounts = count
        byteTable.updateUI()
    }

    private fun changeRadix(radix : String) {
        SourceProxy.radix = radix
        byteTableModel.changeRadix()
        byteTable.updateUI()
    }

    fun updateTree() {
        dataTree.updateUI()
    }
}