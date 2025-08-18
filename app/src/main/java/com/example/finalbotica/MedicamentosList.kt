package com.example.finalbotica

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MedicamentosList(
    private val context: Activity,
    internal var medicaments: List<Medicamentos>
) : ArrayAdapter<Medicamentos>(context, R.layout.layout_list_medicamentos, medicaments) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.layout_list_medicamentos, null, true)

        val tvId = listViewItem.findViewById<TextView>(R.id.textViewId)
        val tvDescripcion = listViewItem.findViewById<TextView>(R.id.textViewDescripcion)
        val tvObservacion = listViewItem.findViewById<TextView>(R.id.textViewObservacion)
        val tvStock = listViewItem.findViewById<TextView>(R.id.textViewStock)
        val tvPrecioCosto = listViewItem.findViewById<TextView>(R.id.textViewPrecioCosto)
        val tvPrecioVenta = listViewItem.findViewById<TextView>(R.id.textViewPrecioVenta)

        val medi = medicaments[position]

        tvId.text = "ID: ${medi.id_medi}"
        tvDescripcion.text = "Descripción: ${medi.descripcion}"
        tvObservacion.text = "Observación: ${medi.observacion}"
        tvStock.text = "Stock: ${medi.stock}"
        tvPrecioCosto.text = "Precio Costo: S/ ${medi.pre_cos}"
        tvPrecioVenta.text = "Precio Venta: S/ ${medi.pre_ven}"

        return listViewItem
    }
}
