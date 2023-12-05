package com.example.semarv2.features.learning

import androidx.lifecycle.ViewModel
import com.example.semarv2.data.source.remote.repository.MateriRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MateriViewModel @Inject constructor(
    private val materiRepository: MateriRepository
) : ViewModel(){

    fun getMateriList() = materiRepository.getListMateri()
    fun getMateriById(id : String) = materiRepository.getMateriById(id)
}