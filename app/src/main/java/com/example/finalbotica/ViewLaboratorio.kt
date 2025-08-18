package com.example.finalbotica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class ViewLaboratorio : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var laboratorioList: MutableList<Laboratorios>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_laboratorio)

        listView = findViewById(R.id.listViewLaboratorio)
        laboratorioList = mutableListOf()
        loadLaboratorios()

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedLaboratorio = laboratorioList[position]

            AlertDialog.Builder(this)
                .setTitle("Eliminar laboratorio")
                .setMessage("¿Deseas eliminar el laboratorio: ${selectedLaboratorio.razon_social}?")
                .setPositiveButton("Eliminar") { _, _ ->
                    // Llama al método DELETE
                    eliminarLaboratorio(selectedLaboratorio.ruc_lab)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Activa el botón "atrás" (home)
            setHomeAsUpIndicator(R.drawable.ic_baseline_reply_24) // Cambia icono
        }
    }

    private fun loadLaboratorios() {
        val stringRequest = StringRequest(
            Request.Method.GET,
            EndPoints.URL_GET_LABORATORIOS,
            Response.Listener { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getBoolean("ok")) {

                        val array = obj.getJSONArray("data") // 👈 nombre correcto del JSON
                        laboratorioList.clear()

                        for (i in 0 until array.length()) {
                            val objectMed = array.getJSONObject(i)

                            val laboratorio = Laboratorios(
                                objectMed.getString("ruc_lab"),
                                objectMed.getString("razon_social"),
                                objectMed.getString("direccion"),
                                objectMed.getString("telefono"),
                                objectMed.getString("email")
                            )
                            laboratorioList.add(laboratorio)
                        }

                        val adapter = LaboratoriosList(this@ViewLaboratorio, laboratorioList)
                        listView.adapter = adapter

                    } else {
                        Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Error parseando JSON", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { volleyError ->
                Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
            })

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun eliminarLaboratorio(id: String) {
        val params = JSONObject()
        try {
            params.put("ruc_lab", id)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonRequest = object : com.android.volley.toolbox.JsonObjectRequest(
            Request.Method.POST,  // o Method.DELETE si tu backend lo soporta
            EndPoints.URL_DELETE_LABORATORIO,
            params,
            Response.Listener { response ->
                try {
                    // Mostrar mensaje del servidor
                    Toast.makeText(applicationContext, response.getString("message"), Toast.LENGTH_LONG).show()
                    // Opcional: recargar lista de medicamentos
                    loadLaboratorios()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        // Agregar la solicitud a la cola de Volley
        VolleySingleton.instance?.addToRequestQueue(jsonRequest)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { // Este es el botón "up" (flecha o icono que pusiste)
                val intent = Intent(this, LaboratorioActivity::class.java)
                startActivity(intent)
                finish() // opcional, para que no vuelva aquí al presionar atrás
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}