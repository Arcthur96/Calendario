package com.example.calendarioagenda

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main_registro.*
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

import kotlinx.android.synthetic.main.activity_main_registro.*
import kotlinx.android.synthetic.main.content_main_activity_login.*
import kotlinx.android.synthetic.main.content_main_activity_login.txtContra
import kotlinx.android.synthetic.main.content_main_activity_login.txtCorr
import kotlinx.android.synthetic.main.content_main_registro.*
import org.json.JSONObject

class MainRegistro : AppCompatActivity() {

    val IP = "http://192.168.1.68" //Direccion IP del servidor web que almacena los servicios web
    var bandera: Boolean = false
    var idProd = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_registro)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            if (txtCorr.text.isEmpty() || txtNom.text.isEmpty() || txtContra.text.isEmpty()) {
                txtCorr.setError("Error faltan datos por ingresar")
                txtCorr.requestFocus()
            } else {
                val corr = txtCorr.text.toString()
                val nom = txtNom.text.toString()
                val contra = txtContra.text.toString()
                val sentencia = "Insert into usuario (correoUsr,nomUsr,contrasena) values('$corr','$nom','$contra')"
                val admin = adminbd(this)
                if (admin.Ejecuta(sentencia) == 1) {
                    //Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show();

                    val correo = txtCorr.text.toString()
                    val nom = txtNom.text.toString()
                    val contra = txtContra.text.toString()
                    var jsonEntrada = JSONObject()
                    jsonEntrada.put("correoUsr", correo)
                    jsonEntrada.put("nomUsr", nom)
                    jsonEntrada.put("contrasena", contra)
                    sendRequest(IP + "/WSAgenda/insertUsuario.php", jsonEntrada)

                    val acti : Intent = Intent(this,MainActivity::class.java)
                    acti.putExtra(MainActivity.EXTRA_CORR,corr)
                    acti.putExtra(MainActivity.EXTRA_CONTRA,contra)
                    startActivity(acti)
                } else {
                    Toast.makeText(this, "El usuario no se registro", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    fun getAllProductos(view: View) {
        val wsURL = IP + "/WSagenda/getUsuarios.php"
        val admin = adminbd(this)
        admin.Ejecuta("DELETE FROM usuarios")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, null,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                val sensadoJson = response.getJSONArray("usuarios")
                for (i in 0 until sensadoJson.length()) {
                    //Los nombres del getString son como los arroja el servicio web
                    //val idprod = sensadoJson.getJSONObject(i).getString("idProd")
                    val correo = sensadoJson.getJSONObject(i).getString("correoUsr")
                    val nom = sensadoJson.getJSONObject(i).getString("nomUsr")
                    val contra = sensadoJson.getJSONObject(i).getString("contrasena")
                    val sentencia =
                        "Insert into usuario(correoUsr,nomUsr,contrasena) values ('${correo}','${nom}', '${contra}')"
                    val res = admin.Ejecuta(sentencia)
                }
                Toast.makeText(this, "Productos cargados ", Toast.LENGTH_LONG).show();
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
    fun Agregar(view: View) {
        if (txtCorr.text.toString().isEmpty() || txtNom.text.toString().isEmpty() || txtContra.text.toString().isEmpty()) {
            txtCorr.setError("Falta información de Ingresar")
            Toast.makeText(this, "Falta información de Ingresar", Toast.LENGTH_LONG).show();
            txtCorr.requestFocus()
        } else {
            val correo = txtCorr.text.toString()
            val nom = txtNom.text.toString()
            val contra = txtContra.text.toString()
            var jsonEntrada = JSONObject()
            jsonEntrada.put("correoUsr", correo)
            jsonEntrada.put("nomUsr", nom)
            jsonEntrada.put("contrasena", contra)
            sendRequest(IP + "/WSAgenda/insertUsuario.php", jsonEntrada)
        }
    }

    fun sendRequest(wsURL: String, jsonEnt: JSONObject) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, jsonEnt,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                if (succ == 200) {
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
