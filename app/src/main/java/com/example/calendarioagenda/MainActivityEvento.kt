package com.example.calendarioagenda

import android.app.DatePickerDialog
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_evento.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main_activity_evento.*
import kotlinx.android.synthetic.main.content_main_activity_evento.etFecha
import java.sql.Date
import java.text.ParseException
import java.util.*

class MainActivityEvento : AppCompatActivity() {

    //var no: String = ""
    var nom: String = ""
    var carr: String = ""
    var edad: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_evento)

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
                    //val iMes = sMes.toInt()+1
                    etFecha.setText("$year-" + "$month" + "-$dayOfMonth")
                }, year, month, day)

            //show datepicker
            dpd.show()
        }

        val c = Calendar.getInstance()
        val df = SimpleDateFormat("EEEE dd MMMM yyyy")
        val formattedDate = df.format(c.time)
        txtHora.text = "Fecha alctual : $formattedDate"


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

    fun agregarEstudiante(v: View){
        if (txtTitulo.text.isEmpty() || txtDescripcion.text.isEmpty() ||
            txtFecha.text.isEmpty())
        {
            Toast.makeText(this, "Falta información ", Toast.LENGTH_SHORT).show();
            txtTitulo.requestFocus()
        }
        else{
            leerCajas()
            val sentencia = "INSERT INTO evento(tituloEvento,descripcionEvento,fecha)"+
                    "values('$nom','$carr','$edad')"
            val usua = usuariosbd(this)
            if(usua.Ejecuta(sentencia) == 1)
            {
                Toast.makeText(this, "Se agrego evento", Toast.LENGTH_SHORT).show();
                limpiarCajas(v)
            }
            else{
                Toast.makeText(this, "El evento no se pudo agregar", Toast.LENGTH_SHORT).show();
                txtTitulo.requestFocus()
            }
        }

    }
    fun leerCajas()
    {
        nom = txtTitulo.text.toString()
        carr = txtDescripcion.text.toString()
        edad = txtFecha.text.toString()
    }

    fun limpiarCajas(v: View)
    {
        nom=""
        carr=""
        edad=""
        txtTitulo.setText("")
        txtDescripcion.setText("")
        txtFecha.setText("")
        btnAgregar.isEnabled = true
        txtTitulo.requestFocus()
    }
}
