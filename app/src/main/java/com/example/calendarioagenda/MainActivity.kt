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

        etFecha.setOnClickListener{
            val c = Calendar.getInstance()
            var day = c.get(Calendar.DAY_OF_MONTH)
            var month = c.get(Calendar.MONTH)
            var year = c.get(Calendar.YEAR)
            if (etFecha.length() != 0){
                val sFec : String = etFecha.text.toString()
                val fecha : Date = convFecha(sFec)
                day = dayFromDate(fecha)
                month = monthFromDate(fecha)
                year = yearFromDate(fecha)
            }

            val dpd= DatePickerDialog(this,android.R.style.Theme_Holo_Dialog,
                DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
                    val monthOfYear = ""
                    val sMes = "$monthOfYear"
                    val iMes = sMes.toInt()+1
                    etFecha.setText("$year-" + iMes.toString() + "-$dayOfMonth")
                }, year, month, day)

            //show datepicker
            dpd.show()
        }
    }

    private fun convFecha(sFec: String): Date{
        var formatoDelTexto = SimpleDateFormat("yyyy-MM-dd")
        var fecha: Date? = null
        try {
            fecha = formatoDelTexto.parse(sFec) as Date?;
        }catch (ex: ParseException){
            val sFec1 = "1900-01-01"
            fecha = formatoDelTexto.parse(sFec1) as Date?;
        }
        return fecha!!
    }

    private fun monthFromDate(date: Date): Int{
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.MONTH)
    }

    private fun yearFromDate(date: Date): Int{
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.YEAR)
    }

    private fun dayFromDate(date: Date): Int{
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_MONTH)
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
