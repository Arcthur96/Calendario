package com.example.calendarioagenda

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main_registro.*
import android.content.Intent
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main_registro.*
import kotlinx.android.synthetic.main.content_main_activity_login.*
import kotlinx.android.synthetic.main.content_main_activity_login.txtContra
import kotlinx.android.synthetic.main.content_main_activity_login.txtCorr
import kotlinx.android.synthetic.main.content_main_registro.*

class MainRegistro : AppCompatActivity() {

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
                val sentencia = "Insert into usuario (correoUsr,nomusr,contrasena) values('$corr','$nom','$contra')"
                val admin = adminbd(this)
                if (admin.Ejecuta(sentencia) == 1) {
                    txtCorr.setText("")
                    txtNom.setText("")
                    Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show();
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
}
