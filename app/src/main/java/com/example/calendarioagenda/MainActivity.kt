package com.example.calendarioagenda

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.sql.Date
import java.text.ParseException
import java.util.*
import java.text.DateFormat;
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast



class MainActivity : AppCompatActivity() {

    var corru: String = ""
    var contra: String = ""

    companion object {
        val EXTRA_CORR = "correo"
        val EXTRA_CONTRA = "contrasena"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val reg = intent
        if (reg != null && reg.hasExtra(EXTRA_CORR) && reg.hasExtra(EXTRA_CONTRA)) {
            //se ejecuta cuando se haya logeado o cuando venga de un registro
            corru = reg.getStringExtra(EXTRA_CORR)
            contra = reg.getStringExtra(EXTRA_CONTRA)
        } else {

            val acti: Intent = Intent(this, MainActivityLogin::class.java)
            startActivity(acti)

            fab.setOnClickListener { view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
    }

    fun Registrar_click(v: View){
        val acti : Intent = Intent(this,MainActivityEvento::class.java)
        startActivity(acti)
    }

    fun Mostrar_click(v: View){
        val acti : Intent = Intent(this,Main2Activity_recycler::class.java)
        startActivity(acti)
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
