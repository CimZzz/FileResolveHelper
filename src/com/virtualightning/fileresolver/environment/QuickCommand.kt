package com.virtualightning.fileresolver.environment

import java.util.*

object QuickCommand {
    var quickCommandIndex = -1
    var quickCommandList : LinkedList<String> = LinkedList()

    @JvmStatic
    fun pushQuickCommand(str : String) {
        quickCommandIndex = -1
        quickCommandList.push(str)
        if(quickCommandList.size > QUICK_COMMAND_CAPTAIN)
            quickCommandList.removeLast()
    }

    @JvmStatic
    fun forwardCommand() : String? {
        if(quickCommandList.size == 0)
            return null

        if(quickCommandIndex == quickCommandList.size - 1 || quickCommandIndex == -1)
            quickCommandIndex = 0
        else quickCommandIndex++

        return quickCommandList[quickCommandIndex]
    }

    @JvmStatic
    fun nextCommand() : String? {
        if(quickCommandList.size == 0)
            return null
        if(quickCommandIndex <= 0)
            quickCommandIndex = quickCommandList.size - 1
        else quickCommandIndex--

        return quickCommandList[quickCommandIndex]
    }
}