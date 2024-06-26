package com.example.appwithregistration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CryptoAdapter(private val login: String, private val cryptoList: MutableList<CryptoCurrency>) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    class CryptoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amount: TextView = view.findViewById(R.id.crypto_amount)
        val type: TextView = view.findViewById(R.id.crypto_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.crypto_item, parent, false)
        return CryptoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val crypto = cryptoList[position]
        holder.amount.text = crypto.amount.toString()
        holder.type.text = crypto.type

        holder.itemView.setOnClickListener {
            val db = DBhelper(holder.itemView.context, null)
            db.deleteCryptoFromUser(login, cryptoList[position])
            cryptoList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return cryptoList.size
    }

    fun addCrypto(crypto: CryptoCurrency) {
        cryptoList.add(crypto)
        notifyItemInserted(cryptoList.size - 1)
    }
}
