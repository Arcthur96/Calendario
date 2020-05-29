package com.example.calendarioagenda

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_evento.*
import kotlinx.android.synthetic.main.content_main_activity_evento.*
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main_evento.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main_activity_evento.*
import kotlinx.android.synthetic.main.content_main_activity_evento.etFecha
import org.json.JSONObject
import java.sql.Date
import java.text.ParseException
import java.util.*

class MainActivityEvento : AppCompatActivity() {

    val IP = "http://192.168.1.68" //Direccion IP del servidor web que almacena los servicios web
    var bandera: Boolean = false
    var idProd = 0

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
                    etFecha.setText("$year-" + "$month" + "-$dayOfMonth")
                }, year, month, day)
            //show datepicker
            dpd.show()
        }
    }

    fun Regresar_click(v: View){
        val acti : Intent = Intent(this,MainActivity::class.java)
        startActivity(acti)
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
            etFecha.text.isEmpty())
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
                val titu = txtTitulo.text.toString()
                val descrip = txtDescripcion.text.toString()
                val fec = etFecha.text.toString()
                var jsonEntrada = JSONObject()
                jsonEntrada.put("tituloEvento", titu)
                jsonEntrada.put("descripcionEvento", descrip)
                jsonEntrada.put("fecha",fec)
                sendRequest(IP + "/WSAgenda/insertEvento.php", jsonEntrada)

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
        edad = etFecha.text.toString()
    }

    fun limpiarCajas(v: View)
    {
        nom=""
        carr=""
        edad=""
        txtTitulo.setText("")
        txtDescripcion.setText("")
        etFecha.setText("")
        btnAgregar.isEnabled = true
        txtTitulo.requestFocus()
    }

    fun Agregar(view: View) {
        if (txtTitulo.text.toString().isEmpty() || txtDescripcion.text.toString().isEmpty() || etFecha.text.toString().isEmpty()) {
            txtTitulo.setError("Falta información de Ingresar")
            Toast.makeText(this, "Falta información de Ingresar", Toast.LENGTH_LONG).show();
            txtTitulo.requestFocus()
        } else {
            val titu = txtTitulo.text.toString()
            val descrip = txtDescripcion.text.toString()
            val fec = etFecha.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("tituloEvento", titu)
            jsonEntrada.put("descripcionEvento", descrip)
            jsonEntrada.put("fecha",fec)
            sendRequest(IP + "/WSAgenda/insertEvento.php", jsonEntrada)
        }
    }


    fun getAllProductos(view: View) {
        val wsURL = IP + "/WSAgenda/getEventos.php"
        val admin = usuariosbd(this)
        admin.Ejecuta("DELETE FROM evento")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, null,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                val sensadoJson = response.getJSONArray("eventos")
                for (i in 0 until sensadoJson.length()) {
                    //Los nombres del getString son como los arroja el servicio web
                    //val idprod = sensadoJson.getJSONObject(i).getString("idProd")
                    val tituloEvento = sensadoJson.getJSONObject(i).getString("tituloEvento")
                    val descripcionEvento = sensadoJson.getJSONObject(i).getString("descripcionEvento")
                    val fecha = sensadoJson.getJSONObject(i).getString("fecha")
                    val sentencia =
                        "Insert into Producto(idProd,nomProd,Existencia,Precio) values ('$tituloEvento}',${descripcionEvento}, ${fecha})"
                    val res = admin.Ejecuta(sentencia)
                }
                Toast.makeText(this, "Eventos cargados ", Toast.LENGTH_LONG).show();
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    this,
                    "Error getALLProductos:  " + error.message.toString(),
                    Toast.LENGTH_SHORT
                ).show();
                Log.d("García", error.message.toString())
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    //esta consulta se hace en la base de datos local, partiendo de que la información ya se cargo
    //ejecutanco el boton de Carga Productos del servidor web
    fun Consultar(view: View) {
        if (txtTitulo.text.toString().isEmpty()) {
            txtTitulo.setError("Falta ingresar clave del producto")
            Toast.makeText(this, "Falta información del id", Toast.LENGTH_SHORT).show();
            txtTitulo.requestFocus()
        } else {
            val admin = usuariosbd(this)
            val id: String = txtTitulo.text.toString()
            //                          0       1       2          3    4
            val cur =
                admin.Consulta("select idProd,nomProd,Existencia,Precio from Producto where idProd=$id")
            if (cur!!.moveToFirst()) {
                txtTitulo.setText(cur.getString(1))
                txtDescripcion.setText(cur.getString(2))
                etFecha.setText(cur.getString(3))
            } else {
                Toast.makeText(this, "No existe la clave del producto", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Rutina para mandar ejecutar un web service de tipo Insert, Update o Delete
    fun sendRequest(wsURL: String, jsonEnt: JSONObject) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, jsonEnt,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                if (succ == 200) {
                    txtTitulo.setText("")
                    txtDescripcion.setText("")
                    etFecha.setText("")
                    txtTitulo.requestFocus()
                    Toast.makeText(
                        this,
                        "Success:${succ}  Message:${msg} Se Inserto Producto en el Servidor Web",
                        Toast.LENGTH_SHORT
                    ).show();
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show();
                Log.d("ERROR", "${error.message}");
                Toast.makeText(this, "Error de capa 8 checa URL", Toast.LENGTH_SHORT).show();
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}