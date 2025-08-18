package com.example.finalbotica

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class LaboratoriosList(
    private val context: Activity,
    internal var laboratoriolist: List<Laboratorios>
) : ArrayAdapter<Laboratorios>(context, R.layout.layout_list_laboratorio, laboratoriolist) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.layout_list_laboratorio, null, true)

        val tvRuc = listViewItem.findViewById<TextView>(R.id.textViewRuc)
        val tvRazon = listViewItem.findViewById<TextView>(R.id.textViewRazon)
        val tvDireccion = listViewItem.findViewById<TextView>(R.id.textViewDireccion)
        val tvTelefono = listViewItem.findViewById<TextView>(R.id.textViewTelefono)
        val tvCorreo = listViewItem.findViewById<TextView>(R.id.textViewEmail)

        val labo = laboratoriolist[position]

        tvRuc.text = "RUC: ${labo.ruc_lab}"
        tvRazon.text = "Razon Social: ${labo.razon_social}"
        tvDireccion.text = "Direccion: ${labo.direccion}"
        tvTelefono.text = "Telefono: ${labo.telefono}"
        tvCorreo.text = "Correo: ${labo.email}"

        return listViewItem
    }
}