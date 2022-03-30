package com.example.rechargedemo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_recharge_categories.*

class RechargeCategories : AppCompatActivity() {

    private var radioButton: String? = null
    private lateinit var phoneNumber: String
    private lateinit var auth: FirebaseAuth
    private val sharedAppLoginNumber = "AppLoginNumber"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge_categories)

        phoneNumber = intent.getStringExtra("phoneNumber").toString()

        auth = FirebaseAuth.getInstance()

        btn_continue.setOnClickListener {
            when (radioButton) {
                "btn_recharge_self" -> {
                    val intent = Intent(this, RechargeActivity::class.java)
                    intent.putExtra("phoneNumber", phoneNumber)
                    startActivity(intent)
                }
                "btn_recharge_friend" -> {
                    val intent = Intent(this,RechargeActivity::class.java)
                    intent.putExtra("phoneNumber", phoneNumber)
                    startActivity(intent)
                }
                null -> {
                    Toast.makeText(this,"Please select an option", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.btn_recharge_self ->
                    if (checked) {
                        radioButton = "btn_recharge_self"
                    }
                R.id.btn_recharge_friend ->
                    if (checked) {
                        radioButton = "btn_recharge_friend"
                    }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            // Logic for Logout
            auth.signOut()

            // Clearing data in sharedPreferences
            val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedAppLoginNumber,
                Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this@RechargeCategories, MainActivity::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }


    override fun onBackPressed() {
        finishAffinity()
    }
}