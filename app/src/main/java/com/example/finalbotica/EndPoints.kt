package com.example.finalbotica


object EndPoints {
    private val URL_ROOT = "https://api-bdbotica.onrender.com/api/"
    val URL_ADD_MEDICAMENTO = URL_ROOT + "post_medicamento"
    val URL_GET_MEDICAMENTOS = URL_ROOT + "all_medicamento"
    val URL_DELETE_MEDICAMENTO = URL_ROOT + "delete_medicamento"
}