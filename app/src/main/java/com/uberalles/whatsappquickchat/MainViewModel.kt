package com.uberalles.whatsappquickchat

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uberalles.whatsappquickchat.database.History
import com.uberalles.whatsappquickchat.database.HistoryDatabase
import com.uberalles.whatsappquickchat.database.HistoryRepository
import kotlinx.coroutines.launch

class MainViewModel(private var repository: HistoryRepository) : ViewModel() {
    private val getAll: LiveData<List<History>>
    private val application: Application = Application()

    init {
        val historyDao = HistoryDatabase.getInstance(application).historyDao()
        repository = HistoryRepository(historyDao)
        getAll = repository.getAll
    }

    fun insert(history: History) = viewModelScope.launch {
        repository.insert(history)
    }

    fun getAll(): LiveData<List<History>> {
        return getAll
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.delete()
        }
    }

}