package com.example.appwithregistration

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPassword: EditText = findViewById(R.id.user_passwd_auth)
        val authButton: Button = findViewById(R.id.button_auth)
        val linkToReg: TextView = findViewById(R.id.linkToReg)

        linkToReg.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        authButton.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val password = userPassword.text.toString().trim()

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show()
            } else {
                val db = DBhelper(this, null)
                val isAuthExist = db.getUser(login, password)

                if (isAuthExist) {
                    Toast.makeText(this, "Пользователь $login успешно авторизован", Toast.LENGTH_LONG).show()
                    userLogin.text.clear()
                    userPassword.text.clear()

                    val intent = Intent(this, CryptoListActivity::class.java)
                    intent.putExtra("user_login", login)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Пользователь $login не найден", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
