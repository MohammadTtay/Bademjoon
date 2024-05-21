package com.android.bademjon.model

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel

class HomeDataModel: ViewModel() {
    val receipt= mutableIntStateOf(0)
    val leftOver= mutableIntStateOf(0)
    val leftOverProcess= mutableFloatStateOf(0F)
    val burned= mutableIntStateOf(0)
    val fat= mutableIntStateOf(0)
    val fatProcess= mutableFloatStateOf(0F)
    val protein= mutableIntStateOf(0)
    val proteinProcess= mutableFloatStateOf(0F)
    val carbo= mutableIntStateOf(0)
    val carboProcess= mutableFloatStateOf(0F)
    val breakfast= mutableIntStateOf(0)
    val lunch= mutableIntStateOf(0)
    val dinner= mutableIntStateOf(0)

    fun receipt(receipt: Int) {
        this.receipt.intValue=receipt;
    }
    fun leftOver(leftOver: Int) {
        this.leftOver.intValue=leftOver;
    }
    fun leftOverProcess(leftOverProcess: Float) {
        this.leftOverProcess.floatValue=leftOverProcess;
    }
    fun burned(burned: Int) {
        this.burned.intValue=burned;
    }
    fun fat(fat: Int) {
        this.fat.intValue=fat;
    }
    fun fatProcess(fatProcess: Float) {
        this.fatProcess.floatValue=fatProcess;
    }
    fun protein(protein: Int) {
        this.protein.intValue=protein;
    }
    fun proteinProcess(proteinProcess: Float) {
        this.proteinProcess.floatValue=proteinProcess;
    }
    fun carbo(carbo: Int) {
        this.carbo.intValue=carbo;
    }
    fun carboProcess(carboProcess: Float) {
        this.carboProcess.floatValue=carboProcess;
    }
    fun breakfast(breakfast: Int) {
        this.breakfast.intValue=breakfast;
    }
    fun lunch(lunch: Int) {
        this.lunch.intValue=lunch;
    }
    fun dinner(dinner: Int) {
        this.dinner.intValue=dinner;
    }
}
