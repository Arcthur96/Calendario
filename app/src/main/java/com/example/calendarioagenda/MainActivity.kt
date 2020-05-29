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
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.content_main_activity_evento.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    val IP = "http://192.168.1.68" //Direccion IP del servidor web que almacena los servicios web
    var bandera: Boolean = false
    var idProd = 0

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
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("EEEE dd MMMM yyyy")
        val formattedDate = df.format(c.time)
        txtHora.text = "Hoy es : $formattedDate"
    }

    fun Registrar_click(v: View){
        val acti : Intent = Intent(this,MainActivityEvento::class.java)
        startActivity(acti)
    }

    fun Mostrar_click(v: View){
        val acti : Intent = Intent(this,Main2Activity_recycler::class.java)
        startActivity(acti)
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
