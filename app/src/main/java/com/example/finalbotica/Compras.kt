package com.example.finalbotica

class Compras(
    val id_compra: Int,
    val id_medi: Int,
    val ruc_lab: String,
    val nombre_medi: String,
    val nombre_lab: String,
    val lote: Int,
    val cantidad: Int,
    val precio: Double,
    val fecha_compra: String,
    val monto: Double
)