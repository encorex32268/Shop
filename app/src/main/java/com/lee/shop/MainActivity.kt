package com.lee.shop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.dynamic.IFragmentWrapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.row_item.view.*

class MainActivity : AppCompatActivity() {

   // var signup = false
    val SIGNUP_RC = 100
    val NICKNAME_RC =200
    val TAG = MainActivity::class.java.simpleName
    val auth = FirebaseAuth.getInstance()

    val functions = listOf<String>(
        "Camera","Invite friend","Parking","Movie","Bus","News","A","B"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

//        if (!signup)
//        {
//            startActivityForResult(Intent(this,SignUpActivity::class.java),SIGNUP_RC)
//
//        }
        auth.addAuthStateListener {
            changedUser(it)
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        //Spinner
        val colors  = arrayOf("Red","Green","Blue")
        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,colors)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                Log.d(TAG, "onItemSelected: ${colors[position]}")
            }
        }
        //recycler
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = FunctionAdapter()

    }

    inner class FunctionAdapter : RecyclerView.Adapter<FunctionAdapter.FunctionViewHolder>(){

        inner  class  FunctionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val functionname : TextView = itemView.functionname
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item,parent,false)
            return FunctionViewHolder(view)
        }
        override fun getItemCount() = functions.size

        override fun onBindViewHolder(holder: FunctionViewHolder, position: Int) {
            val name = functions[position]
            holder.functionname.text  = name
            holder.itemView.setOnClickListener {
                functionClicked(holder,position)

            }
        }


    }

    private fun functionClicked(holder: FunctionAdapter.FunctionViewHolder, position: Int) {
        when(position){
            1 -> startActivity(Intent(this,ContactActivity::class.java))
            2 -> startActivity(Intent(this,ParkingActivity::class.java))
            3 -> startActivity(Intent(this,MovieActivity::class.java))
            4 -> startActivity(Intent(this,BusActivity::class.java))
        }

    }


    override fun onResume() {
        super.onResume()
        // use Extensions
//        userNickname.text = getNickname()
        FirebaseDatabase.getInstance().getReference("users")
            .child(auth.currentUser!!.uid)
            .child("nickname")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error : DatabaseError) {

                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    userNickname.text =  dataSnapshot.value as String
                }
            })

    }

    private fun changedUser(auth: FirebaseAuth) {
        if (auth.currentUser == null){
            startActivityForResult(Intent(this,SignUpActivity::class.java),SIGNUP_RC)
        }else{
            Log.d(TAG, "changedUser: " + auth.uid)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGNUP_RC){
            if (resultCode == Activity.RESULT_OK){
                //create nick name
                startActivityForResult(Intent(this,NicknameActivity::class.java),NICKNAME_RC)
            }
        }
        if (requestCode == NICKNAME_RC){
            if (resultCode == Activity.RESULT_OK){

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
