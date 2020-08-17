package com.example.gitstarscounter.models

class StarModel(private var count: Int, private var date: String) {
    /*??????????????
    Нужен ли владелец
    Нужно ли брать тип больше инта
    Нужно ли изменить тип String у даты на тип Date
    ??????????????*/
    fun getCount(): Int {
        return count
    }

    fun getDate(): String {
        return date
    }
}