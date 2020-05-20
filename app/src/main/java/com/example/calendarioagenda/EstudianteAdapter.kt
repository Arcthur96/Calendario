package com.example.calendarioagenda

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.celda_prototipo_estudiante.view.*


class EstudianteAdapter(private var mListaEstudiantes:List<estudiante>,
                        private val mContext: Context, private val clickListener: (estudiante) -> Unit)
    : RecyclerView.Adapter<EstudianteAdapter.EstudianteViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstudianteViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return EstudianteViewHolder(layoutInflater.inflate(R.layout.celda_prototipo_estudiante, parent, false))
    }

    override fun onBindViewHolder(holder: EstudianteViewHolder, position: Int) {
        holder.bind(mListaEstudiantes[position], mContext, clickListener)
    }

    override fun getItemCount(): Int = mListaEstudiantes.size

    fun setTask(estudiantes: List<estudiante>){
        mListaEstudiantes = estudiantes
        notifyDataSetChanged()
    }

    fun getTasks(): List<estudiante> = mListaEstudiantes

    class EstudianteViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind (estud:estudiante, context: Context, clickListener: (estudiante) -> Unit){
            //Asigna los valores a los elementos del la celda_prototipo_estudiante
            itemView.tvTitulo.text = estud.nomEst.toString()
            itemView.tvDescripcion.text = estud.carrera.toString()
            itemView.tvFecha.text = estud.edad.toString()

            itemView.setOnClickListener{ clickListener(estud)}
        }
    }
}
