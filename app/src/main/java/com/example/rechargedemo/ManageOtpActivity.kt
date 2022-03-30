package com.example.rechargedemo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_manage_otp.*
import java.util.concurrent.TimeUnit

class ManageOtpActivity : AppCompatActivity() {

    private lateinit var phoneNumber: String
    private lateinit var otpId: String
    private var auth = FirebaseAuth.getInstance()
    private val sharedAppLoginNumber = "AppLoginNumber"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_otp)

        phoneNumber = intent.getStringExtra("phoneNumber").toString()

        initiateOtp()

        btn_verify_sign_in.setOnClickListener {
            when {
                enter_otp_edit_text.text.toString().isEmpty() -> {
                    Toast.makeText(this,"Please enter OTP!", Toast.LENGTH_SHORT).show()
                }
                enter_otp_edit_text.text.toString().length != 6 -> {
                    Toast.makeText(this,"Please enter valid OTP!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(otpId,enter_otp_edit_text.text.toString())
                    signInWithPhoneAuthCredential(credential)
                }
            }
        }
    }

    private fun initiateOtp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,  // Phone number to verify
            60,  // Timeout duration
            TimeUnit.SECONDS,  // Unit of timeout
            this,  // Activity (for callback binding)
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                    otpId = s
                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                }
            }) // OnVerificationStateChangedCallbacks
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedAppLoginNumber, Context.MODE_PRIVATE)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val intent = Intent(this@ManageOtpActivity, RechargeCategories::class.java)
                    intent.putExtra("phoneNumber", phoneNumber)
                    startActivity(intent)

                    val sharedPhoneNumber: String = phoneNumber

                    // Feeds data into sharedPreference file from login page
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("sharedPhoneNumber",sharedPhoneNumber)
                    editor.apply()
                    Toast.makeText(applicationContext, "OTP verified", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "Sign in Code Error", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}