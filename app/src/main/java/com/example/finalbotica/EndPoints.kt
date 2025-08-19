package com.example.finalbotica


object EndPoints {
    private val URL_ROOT = "https://api-bdbotica.onrender.com/api/"
    val URL_AUTHENTICATE_USER = URL_ROOT + "authenticate"

    val URL_GET_MEDICAMENTOS = URL_ROOT + "all_medicamento"
    val URL_DELETE_MEDICAMENTO = URL_ROOT + "delete_medicamento"
    val URL_ADD_MEDICAMENTO = URL_ROOT + "post_medicamento"

    val URL_GET_LABORATORIOS = URL_ROOT + "all_laboratorio"
    val URL_DELETE_LABORATORIO = URL_ROOT + "delete_laboratorio"
    val URL_ADD_LABORATORIO = URL_ROOT + "post_laboratorio"

    val URL_GET_COMPRAS = URL_ROOT + "all_compra"
    val URL_DELETE_COMPRA = URL_ROOT + "delete_compra"
    val URL_ADD_COMPRA = URL_ROOT + "post_compra"
}