package com.virtualightning.fileresolver.widget

import com.virtualightning.fileresolver.entity.Format
import com.virtualightning.fileresolver.environment.byteTableName
import javax.swing.table.DefaultTableModel


class FormatTableModel : DefaultTableModel() {
    var formatDataList: ArrayList<Format<*>>? = null
    set(value) {
        if(field != null)
            field!!.clear()

        field = value
    }

    override fun getRowCount(): Int {
        return formatDataList?.size ?: 0
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
        val data = formatDataList!![row]

        when(column) {
        }

        return ""
    }
}