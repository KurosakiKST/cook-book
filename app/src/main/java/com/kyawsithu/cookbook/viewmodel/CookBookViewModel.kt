package com.kyawsithu.cookbook.viewmodel

import androidx.lifecycle.*
import com.kyawsithu.cookbook.model.database.CookBookRepository
import com.kyawsithu.cookbook.model.entites.CookBook
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class CookBookViewModel(private val repository: CookBookRepository): ViewModel(){
    fun insert(dish: CookBook) = viewModelScope.launch {
        repository.insertCookBookData(dish)
    }

    val allDishesList: LiveData<List<CookBook>> = repository.allDishesList.asLiveData()
}

class CookBookViewModelFactory(private val repository : CookBookRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass : Class<T>) : T
    {
        if(modelClass.isAssignableFrom(CookBookViewModel::class.java)){
            return CookBookViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}