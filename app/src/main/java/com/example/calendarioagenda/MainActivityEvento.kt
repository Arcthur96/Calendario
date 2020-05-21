package com.example.calendarioagenda

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_evento.*
import kotlinx.android.synthetic.main.content_main_activity_evento.*

class MainActivityEvento : AppCompatActivity() {

    //var no: String = ""
    var nom: String = ""
    var carr: String = ""
    var edad: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_evento)


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
