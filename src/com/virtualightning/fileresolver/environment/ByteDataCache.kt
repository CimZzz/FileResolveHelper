package com.virtualightning.fileresolver.environment

import com.virtualightning.fileresolver.entity.BitData
import com.virtualightning.fileresolver.entity.ByteData
import java.util.*


object ByteDataCache {
    val byteDataCacheList : LinkedList<ByteData> = LinkedList()

    fun getByteData() : ByteData = byteDataCacheList.poll()?:ByteData()

    fun cacheByteData(byteData: ByteData) {
        if(byteData !is BitData)
            byteDataCacheList.push(byteData)
    }

}