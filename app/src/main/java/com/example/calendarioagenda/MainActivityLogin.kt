package com.example.calendarioagenda

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main_login.*
import android.content.Intent
import android.view.View
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main_login.*
import kotlinx.android.synthetic.main.content_main_activity_login.*

class MainActivityLogin : AppCompatActivity() {

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

}
