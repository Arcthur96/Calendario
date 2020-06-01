package com.example.calendarioagenda

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main_login.*
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

import kotlinx.android.synthetic.main.activity_main_login.*
import kotlinx.android.synthetic.main.content_main_activity_login.*

class MainActivityLogin : AppCompatActivity() {

    val IP = "http://192.168.1.68" //Direccion IP del servidor web que almacena los servicios web
    var bandera: Boolean = false
    var idProd = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_login)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //  .setAction("Action", null).show()
            if (txtCorr.text.isEmpty() || txtContra.text.isEmpty()){
                txtCorr.setError("Error, faltan datos de entrada")
                txtCorr.requestFocus()
            } else{
                var corr = txtCorr.text.toString()
                var contra = txtContra.text.toString()
                var admin = adminbd(this)
                var query = "Select * from usuario where correoUsr = '$corr' and contrasena='$contra'"
                val resul = admin.Consulta(query)
                if (resul!!.moveToFirst()){
                    corr = resul.getString(0)
                    contra = resul.getString(2)
                    val acti = Intent(this, MainActivity::class.java)
                    acti.putExtra(MainActivity.EXTRA_CORR,corr)
                    acti.putExtra(MainActivity.EXTRA_CONTRA,contra)
                    startActivity(acti)
                } else{
                    Toast.makeText(this, "Correo o contraseña invalido", Toast.LENGTH_LONG).show();
                    txtCorr.requestFocus()
                }
            }
        }
    }

    fun fbRegistrar_click(v: View){
        val acti : Intent = Intent(this,MainRegistro::class.java)
        startActivity(acti)
    }

    fun getAllUsuarios(view: View) {
        val wsURL = IP + "/WSAgenda/getUsuarios.php"
        val admin = adminbd(this)
        admin.Ejecuta("DELETE FROM usuario")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, null,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                val sensadoJson = response.getJSONArray("usuario")
                for (i in 0 until sensadoJson.length()) {
                    //Los nombres del getString son como los arroja el servicio web
                    val correoUsr = sensadoJson.getJSONObject(i).getString("correoUsr")
                    val nomUsr = sensadoJson.getJSONObject(i).getString("nomUsr")
                    val contrasena = sensadoJson.getJSONObject(i).getString("contrasena")
                    val sentencia =
                        "Insert into usuario (correoUsr, nomUsr, contrasena) values ('${correoUsr}','${nomUsr}', '${contrasena}')"
                    val res = admin.Ejecuta(sentencia)
                }
                Toast.makeText(this, "Se cargaron los usuarios", Toast.LENGTH_SHORT).show();
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error getALLUsuarios:  " + error.message.toString(), Toast.LENGTH_SHORT).show();
                Log.d("García", error.message.toString())
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

}
