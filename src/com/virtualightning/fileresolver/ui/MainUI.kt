package com.virtualightning.fileresolver.ui

import com.sun.tools.javac.comp.Flow
import com.virtualightning.fileresolver.base.BaseUI
import com.virtualightning.fileresolver.base.UIBuilder
import com.virtualightning.fileresolver.entity.ByteData
import com.virtualightning.fileresolver.environment.Context
import com.virtualightning.fileresolver.environment.byteCountArray
import com.virtualightning.fileresolver.environment.radixArray
import com.virtualightning.fileresolver.utils.Info
import com.virtualightning.fileresolver.utils.newMenu
import com.virtualightning.fileresolver.utils.newMenuItem
import com.virtualightning.fileresolver.widget.ByteTableModel
import com.virtualightning.fileresolver.widget.LeftTreeCellRenderer
import java.awt.*
import java.awt.event.KeyEvent
import java.io.File
import java.util.*
import javax.swing.*
import javax.swing.filechooser.FileFilter
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

val builder = UIBuilder(
        uiName = "FileResolverHelper",
        isResizeAble = true,
        size = Dimension(Toolkit.getDefaultToolkit().screenSize.width / 3 * 2,Toolkit.getDefaultToolkit().screenSize.height / 3 * 2)
)

class MainUI : BaseUI(builder) {
    val closeMenuItem : MenuItem
    val operatorMenu : Menu
    val filePathLabel : JLabel
    val curByteLabel : JLabel
    val totalByteLabel : JLabel
    val restByteLabel : JLabel
    val curBitLabel : JLabel
    val readBlockBtn : JButton
    val readParamsBtn : JButton
    val byteTable : JTable

    private val random : Random
    private val colorList : List<Color?>
    init {
        random = Random()
        colorList = listOf(Color.GREEN,Color.BLUE,Color.RED,Color.YELLOW,Color.YELLOW)
        //Initialize menu bar
        val fileMenu = newMenu("File")
        newMenuItem("Open File",fileMenu,KeyEvent.VK_O).addActionListener({
            val fileChooser = JFileChooser("~")
            fileChooser.fileFilter = object : FileFilter() {
                override fun getDescription(): String = ""
                override fun accept(f: File?): Boolean = f?.isFile ?: false
            }
            fileChooser.showOpenDialog(null)
            val file = fileChooser.selectedFile
            if(file != null) {
                if(Context.isOpenFile)
                    closeFile()
                openFile(file)
            }
        })
        closeMenuItem = newMenuItem("Close File",fileMenu)
        closeMenuItem.isEnabled = false
        closeMenuItem.addActionListener {
            closeFile()
        }
        fileMenu.addSeparator()


        operatorMenu = newMenu("Operator")
        operatorMenu.isEnabled = false
        newMenuItem("Parse next block",operatorMenu,KeyEvent.VK_F6)
        newMenuItem("Parse next params in block",operatorMenu,KeyEvent.VK_F7)

        addMenu(fileMenu,operatorMenu)


        //Initialize main ui
        val container = contentPane
        val topSplitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
        val leftPanel = JPanel()
        val blockTree = JTree()
        val mo = DefaultMutableTreeNode("123")
        mo.add(DefaultMutableTreeNode("123"))
        blockTree.model = DefaultTreeModel(mo)
        blockTree.cellRenderer = LeftTreeCellRenderer()
        blockTree.minimumSize = Dimension(50,50)

        leftPanel.layout = BorderLayout()
        leftPanel.add(blockTree,BorderLayout.CENTER)


        val rightPanel = JPanel()

        val logPanel = JPanel()
        val gridLayout = GridBagLayout()
        val constraint = GridBagConstraints()
        logPanel.layout = gridLayout
        logPanel.preferredSize = Dimension(0,100)

        constraint.fill = GridBagConstraints.HORIZONTAL

        var tempLabel : JLabel
        tempLabel = genLabel()
        tempLabel.text = "Current File :"
        constraint.gridx = 0
        constraint.gridy = 0
        constraint.gridwidth = 2
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tempLabel,constraint)
        logPanel.add(tempLabel)

        filePathLabel = genLabel()
        filePathLabel.text = "--"
        constraint.gridx = 2
        constraint.gridy = 0
        constraint.gridwidth = 8
        constraint.weightx = 1.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(filePathLabel,constraint)
        logPanel.add(filePathLabel)

        tempLabel = genLabel()
        tempLabel.text = "Current Byte Location:"
        constraint.gridx = 0
        constraint.gridy = 1
        constraint.gridwidth = 3
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tempLabel,constraint)
        logPanel.add(tempLabel)

        curByteLabel = genLabel()
        curByteLabel.text = "--"
        constraint.gridx = 3
        constraint.gridy = 1
        constraint.gridwidth = 2
        constraint.weightx = 1.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(curByteLabel,constraint)
        logPanel.add(curByteLabel)

        tempLabel = genLabel()
        tempLabel.text = "Total Bytes:"
        constraint.gridx = 5
        constraint.gridy = 1
        constraint.gridwidth = 3
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tempLabel,constraint)
        logPanel.add(tempLabel)

        totalByteLabel = genLabel()
        totalByteLabel.text = "--"
        constraint.gridx = 8
        constraint.gridy = 1
        constraint.gridwidth = 2
        constraint.weightx = 1.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(totalByteLabel,constraint)
        logPanel.add(totalByteLabel)

        tempLabel = genLabel()
        tempLabel.text = "Remaining Bits Of Current Byte:"
        constraint.gridx = 0
        constraint.gridy = 2
        constraint.gridwidth = 3
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tempLabel,constraint)
        logPanel.add(tempLabel)

        curBitLabel = genLabel()
        curBitLabel.text = "--"
        constraint.gridx = 3
        constraint.gridy = 2
        constraint.gridwidth = 2
        constraint.weightx = 1.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(curBitLabel,constraint)
        logPanel.add(curBitLabel)

        tempLabel = genLabel()
        tempLabel.text = "Remaining Bytes:"
        constraint.gridx = 5
        constraint.gridy = 2
        constraint.gridwidth = 3
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tempLabel,constraint)
        logPanel.add(tempLabel)

        restByteLabel = genLabel()
        restByteLabel.text = "--"
        constraint.gridx = 8
        constraint.gridy = 2
        constraint.gridwidth = 2
        constraint.weightx = 1.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(restByteLabel,constraint)
        logPanel.add(restByteLabel)


        tempLabel = genLabel()
        tempLabel.text = "Display Byte Count:"
        constraint.gridx = 0
        constraint.gridy = 3
        constraint.gridwidth = 3
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tempLabel,constraint)
        logPanel.add(tempLabel)

        val bytesShowCountsSpinner = JSpinner()
        bytesShowCountsSpinner.model = SpinnerListModel(byteCountArray)
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

        tempLabel = genLabel()
        tempLabel.text = "Display Radix:"
        constraint.gridx = 5
        constraint.gridy = 3
        constraint.gridwidth = 3
        constraint.weightx = 0.0
        constraint.weighty = 0.0
        constraint.ipadx = 8
        gridLayout.setConstraints(tempLabel,constraint)
        logPanel.add(tempLabel)

        val radixSpinner = JSpinner()
        radixSpinner.model = SpinnerListModel(radixArray)
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
        readParamsBtn = JButton("Read next params")
        operatorPanel.add(readBlockBtn)
        operatorPanel.add(readParamsBtn)

        gridLayout.setConstraints(operatorPanel,constraint)
        logPanel.add(operatorPanel)














        byteTable = JTable()
        byteTable.model = ByteTableModel()

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
        Context.isOpenFile = true
        closeMenuItem.isEnabled = true
        Context.selectFile = file

        (byteTable.model as ByteTableModel).byteDataList = arrayListOf(ByteData(1,0x61),ByteData(1,0x43))
        byteTable.updateUI()
        Info("Open File : ${Context.selectFile!!.absolutePath}" )
    }

    private fun closeFile() {
        Context.isOpenFile = false
        closeMenuItem.isEnabled = false
        Info("Close File : ${Context.selectFile!!.absolutePath}")
        Context.selectFile = null
    }

    private fun changeByteCount(count : Int) {
        Info(count)
    }
    private fun changeRadix(radix : String) {
        Info(radix)
    }

    private fun genLabel() : JLabel {
        val tempLabel = JLabel()
        tempLabel.isOpaque = true
        tempLabel.background = colorList[random.nextInt(colorList.size)]
        return tempLabel
    }
}