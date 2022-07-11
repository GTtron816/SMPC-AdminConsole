package com.gttron.yukino.smpc.adminconsole

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class Displaycomplaint : AppCompatActivity() {
    companion object {
        var comid = "comid"
    }
   var name=""
    var useremail=""
    var addr=""
    var conatct=""
    var date=""
    var cid=""
    var subject=""
    var comlaint=""
    var status=""
    var reason=""
    var case="open"
    var message=""
    var sendreason=""
    var c=0





    var cmid = ""
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_displaycomplaint)
        supportActionBar?.hide()
        var cid = intent.getStringExtra(comid)
        cmid = cid.toString()
        disdata()
        val homeback = findViewById<ImageButton>(R.id.homeback)
        homeback.setOnClickListener{
            finish()
        }

    }




    private fun disdata() {
        var USEREMAIL : String?=""
        var NAME : String?=""
        var ADDR : String?=""
        var CONTACT : String?=""
        var DATE : String?=""
        var CID: String?=""
        var SUBJECT : String?=""
        var COMPLAINT : String?=""
        var STATUS : String?=""
        var REASON: String?=""

        db.collection("complaints")
           .whereEqualTo("cid", cmid).whereEqualTo("case","open")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    NAME = document.getString("name")
                    USEREMAIL = document.getString("useremail")
                    ADDR = document.getString("addr")
                    CONTACT = document.getString("contact")
                    DATE = document.getString("date")
                    CID = document.getString("cid")
                    SUBJECT = document.getString("subject")
                    COMPLAINT = document.getString("complaint")
                    STATUS = document.getString("status")
                    REASON = document.getString("reason")


                }
                name=NAME.toString()
                useremail=USEREMAIL.toString()
                addr=ADDR.toString()
                conatct=CONTACT.toString()
                date=DATE.toString()
                cid=CID.toString()
                subject=SUBJECT.toString()
                comlaint=COMPLAINT.toString()
                status=STATUS.toString()
                reason=REASON.toString()
               setcard()

            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "DATA FETCH FAILED: NO SUCH DATA", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Error getting documents.", exception)
            }


    }



    private fun setcard() {

        if(name=="")
        {
            Toast.makeText(this, "DATA FETCH FAILED: NO SUCH DATA", Toast.LENGTH_SHORT).show()
        }
        val setname=findViewById<TextView>(R.id.na)
        val setuseremail=findViewById<TextView>(R.id.useremail)
        val setdate=findViewById<TextView>(R.id.date)
        val setaddr=findViewById<TextView>(R.id.addr)
        val setcontact=findViewById<TextView>(R.id.contact)
        val setid=findViewById<TextView>(R.id.complaintid)
        val setsubject=findViewById<TextView>(R.id.subject)
        val setcomplaint=findViewById<TextView>(R.id.complaint)
        val rejectbutton=findViewById<Button>(R.id.reject)
        val approvebutton=findViewById<Button>(R.id.approve)
        val resolvedbutton=findViewById<Button>(R.id.resolve)

          setname.setText(name)
        setuseremail.setText(useremail)
         setaddr.setText(addr)
        setdate.setText(date)
        setcontact.setText(conatct)
        setid.setText(cid)
        setsubject.setText(subject)
        setcomplaint.setText(comlaint)
        if(status == "Approved")
        {
            approvebutton.visibility = View.INVISIBLE

        }
       else
        {
            resolvedbutton.visibility = View.INVISIBLE
        }


        rejectbutton.setOnClickListener{
           rejectdialog()
        }
        approvebutton.setOnClickListener{
            approvedialog()
        }

        resolvedbutton.setOnClickListener{
            resolvedialog()
        }


    }

    private fun approvedialog() {
        val approvepop = LayoutInflater.from(this).inflate(R.layout.approve, null)
        val builder2 = AlertDialog.Builder(this)
            .setView(approvepop)
            .setTitle("Confirm Approval")

        val alertdialog = builder2.show()
        val aconfirm=approvepop.findViewById<ImageButton>(R.id.apconfirm)

        val acancel=approvepop.findViewById<ImageButton>(R.id.apcancel)
        aconfirm.setOnClickListener{
            alertdialog.dismiss()
            status="Approved"
           updatedata()
           sendmail()

        }
       acancel.setOnClickListener{
            alertdialog.dismiss()
        }
    }

    private fun resolvedialog() {
        val resolvepop = LayoutInflater.from(this).inflate(R.layout.approve, null)
        val builder2 = AlertDialog.Builder(this)
            .setView(resolvepop)
            .setTitle("Confirm Approval")

        val alertdialog = builder2.show()
        val aconfirm=resolvepop.findViewById<ImageButton>(R.id.apconfirm)

        val acancel=resolvepop.findViewById<ImageButton>(R.id.apcancel)
        aconfirm.setOnClickListener{
            alertdialog.dismiss()
            case="close"
            status="Resolved"
            updatedata()
           sendmail()
        }
        acancel.setOnClickListener{
            alertdialog.dismiss()
        }
    }

    private fun rejectdialog() {
        val rejpop = LayoutInflater.from(this).inflate(R.layout.rejectpopup, null)
        val builder = AlertDialog.Builder(this)
            .setView(rejpop)
            .setTitle("Confirm Rejection")

        val alertdialog = builder.show()
val confirm=rejpop.findViewById<ImageButton>(R.id.confirm)
        val re=rejpop.findViewById<EditText>(R.id.reason)
        val err=rejpop.findViewById<TextView>(R.id.err)
val cancel=rejpop.findViewById<ImageButton>(R.id.cancel)
        confirm.setOnClickListener{


            if(re.text?.length==0)
            {
                re.error="Field cannot be empty"
                err.setText("Field cannot be empty")

            }


            else{
                alertdialog.dismiss()
                status="Rejected"
                  case="close"
                  reason=re.text.toString()
                  sendreason="because "+reason+"."
                   updatedata()
                   sendmail()}

        }
        cancel.setOnClickListener{
            alertdialog.dismiss()
        }


    }

    private fun sendmail() {

        try {

            subject = "<<ID: " + cid + ">>SUBJECT : Complaint" + status
            message = "Name: "+name+"\n\nComplaint With ID: " +cid+"\nSubmitted on: "+date+"\nis "+status+" "+sendreason
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "plain/text"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(useremail))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            emailIntent.putExtra(Intent.EXTRA_TEXT, message)
            this.startActivity(Intent.createChooser(emailIntent, "Sending email..."))
            c=1
        } catch (t: Throwable) {
            Toast.makeText(this, "Request failed try again: $t", Toast.LENGTH_LONG).show()

        }
    }

    private fun updatedata() {

        val user = hashMapOf(
           "status" to status,
           "reason" to reason,
            "case" to case
        )
        db.collection("complaints")
            .whereEqualTo("cid", cmid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    db.collection("complaints").document(document.id).set(user, SetOptions.merge())
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }





    }



    public override fun onResume() {
        super.onResume()
        if(c==1)
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }




    }





}







