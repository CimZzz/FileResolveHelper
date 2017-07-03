package com.virtualightning.fileresolver.widget

import javax.swing.*
import java.awt.*
import java.awt.event.ActionListener
import java.util.ArrayList

@Suppress("unused")
class JGroupPanel : JPanel {
    /*用来管理组的三个容器*/
    private val pNorth = object : JPanel() {

    }
    private val pCenter = JPanel()
    private val pSouth = JPanel()

    /*当前全部组的集合*/
    private val groupList = ArrayList<JGroupContainer>()

    /*是否已禁止添加组件*/
    private var forbidFlag = false

    /*当前激活的组*/
    private var activeGroup: JGroupContainer? = null
    @Transient internal var al: ActionListener = ActionListener { e ->
        val bttTitle = e.source as JButton
        expandGroup(bttTitle.parent as JGroupContainer)
    }

    private var hasCreateDefaultGroup = false

    constructor() {
        initComponents()
        createDefaultGroup()
    }

    private fun initComponents() {
        this.layout = BorderLayout()
        this.add(pNorth, BorderLayout.NORTH)
        this.add(pCenter, BorderLayout.CENTER)
        this.add(pSouth, BorderLayout.SOUTH)
        pNorth.layout = GroupLayout()
        pCenter.layout = BorderLayout()
        pSouth.layout = GroupLayout()
        forbidFlag = true
    }

    private fun createDefaultGroup() {
        //Default Group
        val bg = arrayOf(Color.black, Color.red, Color.orange, Color.yellow, Color.green, Color.cyan, Color.blue, Color.white)
        for (i in 1..bg.size) {
            insertGroup(i - 1, "Group " + i, bg[i - 1])
            val mc = Color(255 - bg[i - 1].red,
                    255 - bg[i - 1].green,
                    255 - bg[i - 1].blue)
            for (j in 1..5) {
                val bttMember = JButton("Member $j of $i")
                addMember(i - 1, bttMember)
                bttMember.preferredSize = Dimension(1, 40)
                bttMember.isOpaque = false
                bttMember.foreground = mc
            }
            getGroup(i - 1).setMemberGap(20, 5)
            getGroup(i - 1).titleButton.foreground = bg[i - 1]
        }
        hasCreateDefaultGroup = true
    }

    /**
     * @param groupNames String[] 预设组名
     */
    constructor(groupNames: Array<String>) {
        initComponents()
        addGroup(groupNames)
    }

    /**
     * 展开组
     * @param name String 组名
     */
    fun expandGroup(name: String) {
        (groupCount - 1 downTo 0)
                .filter { getGroupName(it) == name }
                .forEach { expandGroup(it) }
    }

    /**
     * 展开组
     * @param index int 组的顺序号
     */
    fun expandGroup(index: Int) {
        expandGroup(getGroup(index))
    }

    /**
     * 展开组
     * @param group JGroupContainer 组
     */
    private fun expandGroup(group: JGroupContainer) {
        pNorth.removeAll()
        pCenter.removeAll()
        pSouth.removeAll()
        var hasAddCenter = false
        groupList.indices
                .asSequence()
                .map { groupList[it] }
                .forEach {
                    if (hasAddCenter) {
                        pSouth.add(it)
                    } else if (it === group) {
                        pCenter.add(it, BorderLayout.CENTER)
                        hasAddCenter = true
                    } else {
                        pNorth.add(it)
                    }
                }
        if (activeGroup != null) {
            activeGroup!!.collapse()
        }
        activeGroup = group
        activeGroup!!.expand()
        pNorth.doLayout()
        pCenter.doLayout()
        pSouth.doLayout()
        doLayout()
    }

    /**
     * 收缩组
     * @param name String 组名
     */
    fun collapseGroup(name: String) {
        (groupCount - 1 downTo 0)
                .filter { getGroupName(it) == name }
                .forEach { collapseGroup(it) }
    }

    /**
     * 收缩组
     * @param index int 组的顺序号
     */
    fun collapseGroup(index: Int) {
        collapseGroup(getGroup(index))
    }

    fun collapseCurrentGroup() {
        if (activeGroup != null)
            collapseGroup(activeGroup!!)
    }

    /**
     * 收缩组
     * @param group JGroupContainer 组
     */
    private fun collapseGroup(group: JGroupContainer) {
        if (group === activeGroup) {
            activeGroup!!.collapse()
            activeGroup = null
        }
    }

    /**
     * 添加组
     * @param name String 组名
     */
    fun addGroup(name: String) {
        this.insertGroup(groupCount, name)
    }

    /**
     * 添加多个组
     * @param names String[] 组名
     */
    fun addGroup(names: Array<String>) {
        for (i in names.indices) {
            addGroup(names[i])
        }
    }

    /**
     * 插入一个组
     * @param index int 顺序号
     * *
     * @param name String 组名
     * *
     * @param bg Color 背景色
     */
    @JvmOverloads fun insertGroup(index: Int, name: String, bg: Color = UIManager.getColor("Desktop.background")) {
        if (index < 0 || index > groupList.size) {
            throw ArrayIndexOutOfBoundsException("index:" + index +
                    " >count:" + groupList.size)
        }
        if (hasCreateDefaultGroup) {
            while (groupCount > 0) {
                removeGroup(0)
            }
            hasCreateDefaultGroup = false
        }
        val countNorth = pNorth.componentCount
        val countCenter = pCenter.componentCount
        val countSouth = pSouth.componentCount
        val group: JGroupContainer
        if (index <= countNorth) {
            group = insertGroup(pNorth, index, name, bg)
        } else if (index <= countNorth + countCenter) {
            group = insertGroup(pCenter, index - countNorth, name, bg)
        } else if (index <= countNorth + countCenter + countSouth) {
            group = insertGroup(pSouth, index - countNorth - countCenter, name,
                    bg)
        } else {
            group = insertGroup(pSouth, countSouth, name, bg)
        }
        group.titleButton.addActionListener(al)
        groupList.add(index, group)

    }

    /**
     * * 插入一个组
     * *
     * @param p JPanel 目标面板
     * *
     * @param index int 顺序号
     * *
     * @param name String 组名
     * *
     * @return JGroupContainer
     */
    private fun insertGroup(p:JPanel, index:Int, name:String,
                            bg:Color):JGroupContainer {
        val group = JGroupContainer(name, bg)
        p.add(group,index)
        return group
    }

    /**
     * 删除一个组
     * @param index int 顺序号
     */
    fun removeGroup(index:Int) {
        val c = groupList[index]
        c.parent.remove(c)
        c.titleButton.removeActionListener(al)
    }

    /**
     * 删除一个组
     * @param name String 组名
     */
    fun removeGroup(name:String) {
        (groupCount - 1 downTo 0)
                .filter { getGroupName(it) == name }
                .forEach { this.removeGroup(it) }
    }

    /**
     * 设置组名
     * @param index int 顺序号
     * *
     * @param name String 组名
     */
    fun setGroupName(index:Int, name:String) {
        this.getGroup(index).name = name
    }

    /**
     * 取得组名
     * @param groupIndex int 顺序号
     * *
     * @return String 组名
     */
    fun getGroupName(groupIndex:Int):String {
        return getGroup(groupIndex).name
    }

    /**
     * 取得全部组名
     * @return String[]
     */
    val groupNames:Array<String?>
        get() {
            val sResult = arrayOfNulls<String>(groupCount)
            for (i in 0..groupCount - 1)
            {
                sResult[i] = getGroupName(i)
            }
            return sResult
        }

    /**
     * 取得当前组的总数
     * @return int
     */
    val groupCount:Int
        get() {
            return groupList.size
        }

    /**
     * 往组中添加成员组件
     * @param groupIndex int 组的顺序号
     * *
     * @param member Component 成员组件
     */
    fun addMember(groupIndex:Int, member:Component) {
        getGroup(groupIndex).addMember(getGroup(groupIndex).memberCount,
                member)
    }

    /**
     * 往组中插入成员组件
     * @param groupIndex int 组的顺序号
     * *
     * @param memberIndex int 插入的顺序号
     * *
     * @param member Component 成员组件
     */
    fun insertMember(groupIndex:Int, memberIndex:Int, member:Component) {
        getGroup(groupIndex).addMember(memberIndex, member)
    }

    /**
     * 从组中移除成员组件
     * @param groupIndex int
     * *
     * @param memberIndex int
     */
    fun removeMember(groupIndex:Int, memberIndex:Int) {
        getGroup(groupIndex).removeMember(memberIndex)
    }

    /**
     * 取得成员组件
     * @param groupIndex int 组的顺序号
     * *
     * @param memberIndex int 成员组件的顺序号
     * *
     * @return Component 成员组件
     */
    fun getMember(groupIndex:Int, memberIndex:Int):Component {
        return getGroup(groupIndex).getMember(memberIndex)
    }

    /**
     * 取得全部成员组件
     * @param groupIndex int 组的顺序号
     * *
     * @return Component[] 全部成员组件
     */
    fun getMembers(groupIndex:Int):Array<Component?> {
        return getGroup(groupIndex).members
    }

    /**
     * 取得成员组件的总数
     * @param groupIndex int 组的顺序号
     * *
     * @return int 总数
     */
    fun getMemberCount(groupIndex:Int):Int {
        return getGroup(groupIndex).memberCount
    }

    /**
     * 取得组
     * @param index int 组的顺序号
     * *
     * @return JGroupContainer 组
     */
    private fun getGroup(index:Int):JGroupContainer {
        return groupList[index]
    }

    /**
     * 覆写的addImpl方法,禁止再向JGroupPane中添加组件
     * @param comp Component
     * *
     * @param constraints Object
     * *
     * @param index int
     */
    override fun addImpl(comp:Component, constraints:Any, index:Int) {
        if (forbidFlag)
        {
            if (comp !is JGroupContainer)
            {
                throw UnsupportedOperationException(
                        "JGroupPane can't add component!")
            }
        }
        else
        {
            super.addImpl(comp, constraints, index)
        }
    }

    /**
     * <P>Title: OpenSwing</P>
     * <P>Description: 组面板布局管理器</P>
     * <P>Copyright: Copyright (c) 2004</P>
     * <P>Company: </P>
     * @author <A href="mailto:sunkingxie@hotmail.com" mce_href="mailto:sunkingxie@hotmail.com">SunKing</A>
     * *
     * @version 1.0
     */
    internal inner class GroupLayout:LayoutManager, java.io.Serializable {
        var vgap = 0
        var hgap = 0
        constructor()

        constructor(hg:Int, vg:Int) {
            this.hgap = hg
            this.vgap = vg
        }

        override fun addLayoutComponent(name:String, comp:Component) {}

        override fun removeLayoutComponent(comp:Component) {}

        override fun preferredLayoutSize(parent:Container):Dimension {
            synchronized (parent.treeLock) {
                val insets = parent.insets
                val ncomponents = parent.componentCount
                var w = 0
                var h = 0
                for (i in 0..ncomponents - 1)
                {
                    val comp = parent.getComponent(i)
                    val d = comp.preferredSize
                    if (w < d.width)
                    {
                        w = d.width
                    }
                    h += d.height + vgap
                }
                return Dimension(insets.left + insets.right + w + 2 * hgap,
                        insets.top + insets.bottom + h + 2 * vgap)
            }
        }

        override fun minimumLayoutSize(parent:Container):Dimension {
            return preferredLayoutSize(parent)
        }

        override fun layoutContainer(parent:Container) {
            synchronized (parent.treeLock) {
                val insets = parent.insets
                val ncomponents = parent.componentCount
                if (ncomponents == 0)
                {
                    return
                }
                var y = insets.top + vgap
                for (c in 0..ncomponents - 1)
                {
                    val h = parent.getComponent(c).preferredSize.height
                    parent.getComponent(c).setBounds(
                            insets.left + hgap,
                            y,
                            parent.width - insets.left - insets.right -
                                    2 * hgap, h)
                    y += h + vgap
                }
            }
        }

        override fun toString():String {
            return javaClass.name
        }
    }

    /**
     * <P>Title: OpenSwing</P>
     * <P>Description: 组</P>
     * <P>Copyright: Copyright (c) 2004</P>
     * <P>Company: </P>
     * @author <A href="mailto:sunkingxie@hotmail.com" mce_href="mailto:sunkingxie@hotmail.com">SunKing</A>
     * *
     * @version 1.0
     */
    internal inner class JGroupContainer/**
     * @param name String  组名
     * *
     * @param background Color 成员组件所在面板背景色
     */
    @JvmOverloads  constructor(name:String = "", background:Color = UIManager.getColor("Desktop.background")):JPanel() {
        /**
         * 取得组的标题按钮
         * @return JButton
         */
        val titleButton = JButton()
        /**
         * 取得组的成员组件面板
         * @return JPanel
         */
        val membersContainer = JPanel()
        private val sp:JScrollPane

        init{
            titleButton.text = name
            titleButton.isFocusable = false
            membersContainer.layout = GroupLayout(5, 5)
            this.layout = BorderLayout()
            this.add(titleButton, BorderLayout.NORTH)

            membersContainer.background = background

            val thumbColor = UIManager.getColor("ScrollBar.thumb")
            val trackColor = UIManager.getColor("ScrollBar.track")
            val trackHighlightColor = UIManager.getColor(
                    "ScrollBar.trackHighlight")

            UIManager.put("ScrollBar.thumb", background)
            UIManager.put("ScrollBar.track", background)
            UIManager.put("ScrollBar.trackHighlight", background)
            sp = JScrollPane(membersContainer)
            sp.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
            this.add(sp, BorderLayout.CENTER)
            collapse()
            UIManager.put("ScrollBar.thumb", thumbColor)
            UIManager.put("ScrollBar.track", trackColor)
            UIManager.put("ScrollBar.trackHighlight", trackHighlightColor)

        }

        /**
         * 设置间距
         * @param hgap int 横间距
         * *
         * @param vgap int 竖间距
         */
        fun setMemberGap(hgap:Int, vgap:Int) {
            membersContainer.layout = GroupLayout(hgap, vgap)
        }

        /**
         * 收缩组
         */
        fun collapse() {
            sp.isVisible = false
            this.revalidate()
        }

        /**
         * 展开组
         */
        fun expand() {
            sp.isVisible = true
            this.revalidate()
        }

        /**
         * 设置组名
         * @param name String 组名
         */
        override fun setName(name:String) {
            titleButton.text = name
        }

        /**
         * 取得组名
         * @return String
         */
        override fun getName():String {
            return titleButton.text
        }

        /**
         * 添加一个成员组件
         * @param index int 顺序号
         * *
         * @param c Component 成员组件
         */
        fun addMember(index:Int, c:Component) {
            membersContainer.add(c, index)
            membersContainer.doLayout()
        }

        /**
         * 删除一个成员组件
         * @param index int 顺序号
         */
        fun removeMember(index:Int) {
            membersContainer.remove(index)
            membersContainer.doLayout()
        }

        /**
         * 取得一个成员组件
         * @param index int 顺序号
         * *
         * @return Component 成员组件
         */
        fun getMember(index:Int):Component {
            return membersContainer.getComponent(index)
        }

        /**
         * 取得全部成员组件
         * @return Component[] 成员组件
         */
        val members:Array<Component?>
            get() {
                val coms = arrayOfNulls<Component>(memberCount)
                for (i in coms.indices)
                {
                    coms[i] = membersContainer.getComponent(i)
                }
                return coms
            }

        /**
         * 取得成员组件总数
         * @return int 总数
         */
        val memberCount:Int
            get() {
                return membersContainer.componentCount
            }

        /**
         * 重写的toString方法
         * @return String
         */
        override fun toString():String {
            return name
        }
    }


}