package com.example.finalbotica

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

class ViewMedicamentosActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var medicamentList: MutableList<Medicamentos>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewmedicamentos)

        listView = findViewById(R.id.listViewMedicamentos)
        medicamentList = mutableListOf()
        loadMedicamentos()

        // 👉 Cuando ya tengas el adapter asignado, podrás hacer clic en un item
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedMedicamento = medicamentList[position]
            // Popup tipo Toast
            /*
            Toast.makeText(
                this,
                "Seleccionaste el item id: ${selectedMedicamento.id_medi}",
                Toast.LENGTH_SHORT
            ).show()

            */
            AlertDialog.Builder(this)
                .setTitle("Eliminar medicamento")
                .setMessage("¿Deseas eliminar el medicamento: ${selectedMedicamento.descripcion}?")
                .setPositiveButton("Eliminar") { _, _ ->
                    // Llama al método que hace DELETE
                    eliminarMedicamento(selectedMedicamento.id_medi)
                }
                .setNegativeButton("Cancelar", null)
                .show()

        }
    }

    private fun loadMedicamentos() {
        val stringRequest = StringRequest(
            Request.Method.GET,
            EndPoints.URL_GET_MEDICAMENTOS,
            Response.Listener { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getBoolean("ok")) {

                        val array = obj.getJSONArray("data") // 👈 nombre correcto del JSON

                        medicamentList.clear()

                        for (i in 0 until array.length()) {
                            val objectMed = array.getJSONObject(i)

                            val medicamento = Medicamentos(
                                objectMed.getInt("id_medi"),
                                objectMed.getString("descripcion"),
                                objectMed.getString("observacion"),
                                objectMed.getInt("stock"),
                                objectMed.getDouble("pre_cos"),
                                objectMed.getDouble("pre_ven")
                            )

                            medicamentList.add(medicamento)
                        }

                        val adapter = MedicamentosList(this@ViewMedicamentosActivity, medicamentList)
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

    private fun eliminarMedicamento(id: Int) {
        // Crear JSON con el ID del medicamento
        val params = JSONObject()
        try {
            params.put("id_medi", id)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonRequest = object : com.android.volley.toolbox.JsonObjectRequest(
            Request.Method.POST,  // o Method.DELETE si tu backend lo soporta
            EndPoints.URL_DELETE_MEDICAMENTO,
            params,
            Response.Listener { response ->
                try {
                    // Mostrar mensaje del servidor
                    Toast.makeText(applicationContext, response.getString("message"), Toast.LENGTH_LONG).show()
                    // Opcional: recargar lista de medicamentos
                    loadMedicamentos()
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
}
