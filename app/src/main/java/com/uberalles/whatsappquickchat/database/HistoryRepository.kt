package com.uberalles.whatsappquickchat.database

import androidx.lifecycle.LiveData

class HistoryRepository(private val historyDao: HistoryDao) {

    val getAll: LiveData<List<History>> = historyDao.getAll()

    suspend fun insert(history: History){
        historyDao.insertPhone(history)
    }

    suspend fun delete(){
        historyDao.deleteHistory()
    }
}