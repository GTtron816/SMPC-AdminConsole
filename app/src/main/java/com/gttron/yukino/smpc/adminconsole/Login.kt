package com.gttron.yukino.smpc.adminconsole

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    val passpattern="^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,}$"
    val passwordpattern=Regex(passpattern)
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    var email=""
    var useremail=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        auth = Firebase.auth
        val currentuser=auth.currentUser
        if (currentuser!=null)
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
        val loginemail=findViewById<TextInputEditText>(R.id.loginemail)
        val loginb = findViewById<Button>(R.id.loginbt)
        val emailbox=findViewById<TextInputLayout>(R.id.emailbox)
        loginb.setOnClickListener {

            if(loginemail.text?.length == 0)
            {
                loginemail.error="Field cannot be empty"
                emailbox.helperText="Field cannot be empty"
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(loginemail.text.toString()).matches())
            {
                loginemail.error="Invalid Email"
                emailbox.helperText="Invalid Email"
            }
            else
            {
            useremail=loginemail.text.toString()
            loginuser()}
        }

    }

    private fun loginuser() {


        var USEREMAIL : String?=""


        db.collection("adminaccounts")
            .whereEqualTo("email", useremail)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {


                    USEREMAIL = document.getString("email")



                }

                email=USEREMAIL.toString()

                login()

            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "LOGIN FAILED", Toast.LENGTH_SHORT).show()
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

    }





    fun login()
    {
        val passbox=findViewById<TextInputLayout>(R.id.passbox)
        auth = Firebase.auth
        if(useremail==email)
        {
            val pass=findViewById<TextInputEditText>(R.id.loginpw)
            val password=pass.text.toString()
            if (pass.text?.length==0)
            {
                pass.error="Field cannot be empty"
                passbox.helperText="Field cannot be empty"
            }
            else if(pass.text?.length!!< 8)
            {
                pass.error="Password length must be greater than  8 characters"
                passbox.helperText="Password length must be greater than  8 characters"
            }
            else if(!passwordpattern.matches(pass.text.toString()))
            {
                pass.error="Password should contain a letter and digit combination"
                passbox.helperText="Password should contain a letter and digit combination"

            }

            else{

            auth.signInWithEmailAndPassword(useremail,password)
                .addOnSuccessListener {

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }


                .addOnFailureListener {

                    Toast.makeText(this, "LOGIN FAILED", Toast.LENGTH_LONG).show()


                }}}

        else{
            Toast.makeText(this, "LOGIN FAILED", Toast.LENGTH_LONG).show()
        }


    }















}

