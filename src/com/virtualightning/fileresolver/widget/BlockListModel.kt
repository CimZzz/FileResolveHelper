package com.virtualightning.fileresolver.widget

import com.virtualightning.fileresolver.entity.Block
import javax.swing.DefaultListModel


class BlockListModel(val blockList : ArrayList<Block>) : DefaultListModel<Block>() {

    override fun getSize(): Int {
        return blockList.size
    }

    override fun elementAt(index: Int): Block {
        return blockList[index]
    }

    override fun get(index: Int): Block {
        return blockList[index]
    }

    override fun getElementAt(index: Int): Block {
        return blockList[index]
    }
}