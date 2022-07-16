package com.vivant.telemedicine.model

data class ChooseStepModel(
    var imgStep: Int = 0,
    var imgStepDisable: Int = 0,
    var titleStep: String = "",
    var isSelected: Boolean = false
)