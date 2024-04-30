package com.example.appwithregistration

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class ItemsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        val itemsList: RecyclerView = findViewById(R.id.itemsList)
        val items = arrayListOf<Item>()

        items.add(Item(1, "1", "Tree", "Tree description", "Tree text", 100))
        items.add(Item(2, "2", "Plastic Tree", "Tree description", "Tree text", 200))
        items.add(Item(3, "3", "Real Tree", "Tree description", "Tree text", 300))
      }
}