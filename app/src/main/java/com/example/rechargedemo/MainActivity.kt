package com.example.rechargedemo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val sharedAppLoginNumber = "AppLoginNumber"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedNumber: SharedPreferences = this.getSharedPreferences(sharedAppLoginNumber,
            Context.MODE_PRIVATE)

        val sharedPhoneNumber = sharedNumber.getString("sharedPhoneNumber", null)

        // Checks weather user already verified number or not
        if (sharedPhoneNumber != null) {
            // Number Verification page
            val intent = Intent(this@MainActivity, RechargeCategories::class.java)
            intent.putExtra("phoneNumber", sharedPhoneNumber)
            startActivity(intent)
        }

        country_code_picker.registerCarrierNumberEditText(phone_number_edit_text)

        btn_get_otp.setOnClickListener {
            val intent = Intent(this,ManageOtpActivity::class.java)
            intent.putExtra("phoneNumber",country_code_picker.fullNumberWithPlus.replace(" ",""))
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}