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

class ViewCompra : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var compraList: MutableList<Compras>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_compra)

        listView = findViewById(R.id.listViewCompra)
        compraList = mutableListOf()
        loadCompras()

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedCompra = compraList[position]

            AlertDialog.Builder(this)
                .setTitle("Eliminar Compra")
                .setMessage("¿Deseas eliminar la compra: ${selectedCompra.id_compra}?")
                .setPositiveButton("Eliminar") { _, _ ->
                    // Llama al método DELETE
                    eliminarCompra(selectedCompra.id_compra)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Activa el botón "atrás" (home)
            setHomeAsUpIndicator(R.drawable.ic_baseline_reply_24) // Cambia icono
        }
    }

    private fun loadCompras() {
        val stringRequest = StringRequest(
            Request.Method.GET,
            EndPoints.URL_GET_COMPRAS,
            Response.Listener { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getBoolean("ok")) {

                        val array = obj.getJSONArray("data") // 👈 nombre correcto del JSON
                        compraList.clear()

                        for (i in 0 until array.length()) {
                            val objectComp = array.getJSONObject(i)

                            val compra = Compras(
                                objectComp.getInt("id_compra"),
                                objectComp.getInt("id_medi"),
                                objectComp.getString("ruc_lab"),
                                objectComp.getString("nombre_medi"),
                                objectComp.getString("nombre_lab"),
                                objectComp.getInt("lote"),
                                objectComp.getInt("cantidad"),
                                objectComp.getDouble("precio"),
                                objectComp.getString("fecha_compra"),
                                objectComp.getDouble("monto")
                            )
                            compraList.add(compra)
                        }

                        val adapter = ComprasList(this@ViewCompra, compraList)
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

    private fun eliminarCompra(id: Int) {
        val params = JSONObject()
        try {
            params.put("id_compra", id)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonRequest = object : com.android.volley.toolbox.JsonObjectRequest(
            Request.Method.POST,  // o Method.DELETE si tu backend lo soporta
            EndPoints.URL_DELETE_COMPRA,
            params,
            Response.Listener { response ->
                try {
                    // Mostrar mensaje del servidor
                    Toast.makeText(applicationContext, response.getString("message"), Toast.LENGTH_LONG).show()

                    loadCompras()
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
                val intent = Intent(this, CompraActivity::class.java)
                startActivity(intent)
                finish() // opcional, para que no vuelva aquí al presionar atrás
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}