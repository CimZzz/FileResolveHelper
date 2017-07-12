package com.virtualightning.fileresolver.widget

import com.virtualightning.fileresolver.entity.ByteData
import com.virtualightning.fileresolver.environment.Context
import com.virtualightning.fileresolver.environment.SourceProxy
import com.virtualightning.fileresolver.environment.byteTableName
import java.util.*
import javax.swing.table.DefaultTableModel


class ByteTableModel(var byteDataList : LinkedList<ByteData>) : DefaultTableModel() {


    fun changeRadix() {
        byteDataList.forEach {
            it.changeRadix(SourceProxy.radix)
        }
    }

    override fun getRowCount(): Int {
        if(byteDataList == null)
            return 0
        if(byteDataList.size > SourceProxy.byteCounts) return SourceProxy.byteCounts else return byteDataList.size
    }

    override fun getColumnCount(): Int {
        return byteTableName.size
    }

    override fun getColumnName(column: Int): String {
        return byteTableName[column]
    }

    override fun getColumnClass(columnIndex: Int): Class<*> {
        return String.javaClass
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        return false
    }

    override fun getValueAt(row: Int, column: Int): Any {
        val data = byteDataList[row]

        when(column) {
            0->return data.locationStr!!
            1->return data.byteStr!!
            2->return data.ascii!!
        }

        return ""
    }
}