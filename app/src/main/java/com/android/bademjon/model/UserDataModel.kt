package com.android.bademjon.model

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class UserDataModel: ViewModel()
{
    var name = mutableStateOf("")
    var weight = mutableStateOf("0")
    var height = mutableStateOf("0")
    var age= mutableStateOf("0")
    var wrist= mutableStateOf("0")
    var rate= mutableStateOf("0")
    var gender= mutableStateOf("0")
    var disease= mutableStateOf("0")


    fun updateName(newName: String) {
        name.value = newName
    }
    fun updateWeight(newWeight: Int) {
        weight.value = newWeight.toString()
    }
    fun updateHeight(newHeight: Int) {
        height.value = newHeight.toString()
    }
    fun updateAge(newAge: Int) {
        age.value = newAge.toString()
    }
    fun updateWrist(newWrist: Int) {
        wrist.value = newWrist.toString()
    }
    fun updateRate(newRate: Int) {
        rate.value = newRate.toString()
    }
    fun updateGender(newGender: Int) {
        gender.value = newGender.toString()
    }
    fun updateDisease(newDisease: Int) {
        disease.value = newDisease.toString()
    }
}