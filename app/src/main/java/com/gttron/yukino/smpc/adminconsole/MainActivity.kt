package com.gttron.yukino.smpc.adminconsole

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    var cmid =""
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        auth = Firebase.auth
        val srh=findViewById<Button>(R.id.srch)
        srh.setOnClickListener{
            val cmp=findViewById<TextInputEditText>(R.id.cmpid)
            val cmpidbox=findViewById<TextInputLayout>(R.id.compidbox)
            if (cmp.text?.length == 0)
            {
                cmp.error="Field cannot be empty"
                cmpidbox.helperText="Field cannot be empty"

            }


            else {
                cmid = cmp.text.toString()
                val intent = Intent(this, Displaycomplaint::class.java)
                intent.putExtra(Displaycomplaint.comid, cmid)
                startActivity(intent)
            }




        }
        val exitbtn=findViewById<ImageButton>(R.id.exitbutton)
        exitbtn.setOnClickListener{
            val approvepop = LayoutInflater.from(this).inflate(R.layout.approve, null)
            val builder2 = AlertDialog.Builder(this)
                .setView(approvepop)
                .setTitle("Exit App?")

            val alertdialog = builder2.show()
            val aconfirm=approvepop.findViewById<ImageButton>(R.id.apconfirm)

            val acancel=approvepop.findViewById<ImageButton>(R.id.apcancel)
            aconfirm.setOnClickListener{
                alertdialog.dismiss()

               finishAffinity()

            }
            acancel.setOnClickListener{
                alertdialog.dismiss()
            }
        }



        val logoutbt=findViewById<ImageButton>(R.id.logoutbt)
        logoutbt.setOnClickListener {


            val approvepop = LayoutInflater.from(this).inflate(R.layout.approve, null)
            val builder2 = AlertDialog.Builder(this)
                .setView(approvepop)
                .setTitle("LOGOUT?")

            val alertdialog = builder2.show()
            val aconfirm = approvepop.findViewById<ImageButton>(R.id.apconfirm)

            val acancel = approvepop.findViewById<ImageButton>(R.id.apcancel)
            aconfirm.setOnClickListener {
                alertdialog.dismiss()


                auth.signOut()
                finish()

                val intent = Intent(this, Login::class.java)
                startActivity(intent)


            }
            acancel.setOnClickListener {
                alertdialog.dismiss()
            }


        }
























        }



    }



