package com.example.appwithregistration

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CryptoListActivity : AppCompatActivity() {
    private lateinit var cryptoAdapter: CryptoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cryptos)

        val login = intent.getStringExtra("user_login") ?: return

        val db = DBhelper(this, null)
        val cryptoList = db.getUserCryptos(login)

        cryptoAdapter = CryptoAdapter(login, cryptoList)

        val recyclerView: RecyclerView = findViewById(R.id.crypto_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = cryptoAdapter

        val addCryptoButton: Button = findViewById(R.id.add_crypto_button)
        val cryptoAmount: EditText = findViewById(R.id.crypto_amount)
        val cryptoType: EditText = findViewById(R.id.crypto_type)

        addCryptoButton.setOnClickListener {
            val amount = cryptoAmount.text.toString().toDoubleOrNull()
            val type = cryptoType.text.toString()

            if (amount == null || type.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show()
            } else {
                val crypto = CryptoCurrency(amount, type)
                db.addCryptoToUser(login, crypto)
                cryptoAdapter.addCrypto(crypto)
                cryptoAmount.text.clear()
                cryptoType.text.clear()
            }
        }
    }
}
