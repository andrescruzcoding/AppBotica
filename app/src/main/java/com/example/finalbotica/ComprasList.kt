package com.example.finalbotica

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ComprasList (
    private val context: Activity,
    internal var compralist: List<Compras>
) : ArrayAdapter<Compras>(context, R.layout.layout_list_compra, compralist) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.layout_list_compra, null, true)

        val tvidCompra = listViewItem.findViewById<TextView>(R.id.textViewCompra)
        val tvMediment = listViewItem.findViewById<TextView>(R.id.textViewCMedicamento)
        val tvLabo = listViewItem.findViewById<TextView>(R.id.textViewCLaboratorio)
        val tvLote = listViewItem.findViewById<TextView>(R.id.textViewLote)
        val tvPrecio = listViewItem.findViewById<TextView>(R.id.textViewCPrecio)
        val tvFecha = listViewItem.findViewById<TextView>(R.id.textViewFecha)
        val tvCantidad = listViewItem.findViewById<TextView>(R.id.textViewCantidad)
        val tvMonto = listViewItem.findViewById<TextView>(R.id.textViewMonto)

        val comp = compralist[position]

        tvidCompra.text = "Nro de Compra: ${comp.id_compra}"
        tvMediment.text = "Medicamento: ${comp.nombre_medi}"
        tvLabo.text = "Laboratorio: ${comp.nombre_lab}"
        tvFecha.text = "Fecha: ${comp.fecha_compra}"
        tvLote.text = "Nro Lote: ${comp.lote}"
        tvPrecio.text = "Precio: S/${comp.precio}"
        tvCantidad.text = "Cantidad: ${comp.cantidad}"
        tvMonto.text = "Monto: S/${comp.monto}"

        return listViewItem
    }
}