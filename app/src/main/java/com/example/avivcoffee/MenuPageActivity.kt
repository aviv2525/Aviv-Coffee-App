package com.example.avivcoffee

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class MenuItem(val name: String, val price: String, val imageResId: Int, val isVegan: Boolean)

class MenuPageActivity : AppCompatActivity() {
    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var veganSwitch: Switch
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val menuItems = listOf(
        MenuItem(getString(R.string.item_espresso), "10₪", R.drawable.espresso3, true),
        MenuItem(getString(R.string.item_cappuccino), "13₪", R.drawable.cappuccinos, false),
        MenuItem(getString(R.string.item_soy_cappuccino), "13₪", R.drawable.soy_cappuccino, true),
        MenuItem(getString(R.string.item_americano), "12₪", R.drawable.espresso1, true),
        MenuItem(getString(R.string.filter_coffee), "17₪", R.drawable.filtercoffee, true),
        MenuItem(getString(R.string.banana_muffins), "20₪", R.drawable.banana_muffins, true),
        MenuItem(getString(R.string.item_sandwich), "27₪", R.drawable.sandwich, false),
        MenuItem(getString(R.string.item_vegan_sandwich), "22₪", R.drawable.vegan_sandwiches, true),
        MenuItem(getString(R.string.chocolate_cake), "27₪", R.drawable.chocolate_cake, false),
        MenuItem(getString(R.string.item_latte), "13₪", R.drawable.cappu, false),
        MenuItem(getString(R.string.item_chocolate_brownie), "20₪", R.drawable.brownie, false)
    )


        menuRecyclerView = findViewById(R.id.recyclerView)
        veganSwitch = findViewById(R.id.veganBtn)
        val backButton: Button = findViewById(R.id.BackBtn)

        menuRecyclerView.layoutManager = LinearLayoutManager(this)
        menuAdapter = MenuAdapter(menuItems)
        menuRecyclerView.adapter = menuAdapter

        veganSwitch.setOnCheckedChangeListener { _, isChecked ->
            val filteredItems = if (isChecked) {
                menuItems.filter { it.isVegan }
            } else {
                menuItems
            }
            menuAdapter.updateList(filteredItems)
        }
        backButton.setOnClickListener {
            finish()
        }

}
}
