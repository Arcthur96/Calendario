package com.example.calendarioagenda

class estudiante (idEvento: String, tituloEvento: String, descripcionEvento: String, fecha: String) {
    var noCtrl: String =""
    var nomEst: String=""
    var carrera: String =""
    var edad: String =""

    init{
        this.noCtrl = idEvento
        this.nomEst = tituloEvento
        this.carrera = descripcionEvento
        this.edad = fecha
    }
}
