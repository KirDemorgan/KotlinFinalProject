package com.example.appwithregistration

class User(val login: String, val email: String, val password: String, val cryptos: MutableList<CryptoCurrency> = mutableListOf())

class CryptoCurrency(val amount: Double, val type: String)
