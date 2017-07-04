package com.virtualightning.fileresolver.widget

import com.virtualightning.fileresolver.entity.ByteData
import com.virtualightning.fileresolver.environment.Context
import com.virtualightning.fileresolver.environment.tableName
import javax.swing.table.DefaultTableModel


class ByteTableModel : DefaultTableModel() {
    var byteDataList : ArrayList<ByteData>? = null
    set(value) {
        if(field != null)
            field!!.clear()

        field = value
    }

    fun changeRadix() {
        byteDataList!!.forEach {
            it.changeRadix(Context.radix)
        }
    }

    override fun getRowCount(): Int {
        return byteDataList?.size ?: 0
    }

    override fun getColumnCount(): Int {
        return tableName.size
    }

    override fun getColumnName(column: Int): String {
        return tableName[column]
    }

    override fun getColumnClass(columnIndex: Int): Class<*> {
        return String.javaClass
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        return false
    }

    override fun getValueAt(row: Int, column: Int): Any {
        val data = byteDataList!![row]

        when(column) {
            0->return data.locationStr!!
            1->return data.byteStr!!
            2->return data.ascii!!
        }

        return ""
    }
}