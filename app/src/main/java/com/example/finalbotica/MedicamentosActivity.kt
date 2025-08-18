package com.example.finalbotica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject


class MedicamentosActivity : AppCompatActivity() {

    private lateinit var txtDescripcion: EditText
    private lateinit var txtObservacion: EditText
    private lateinit var txtStock: EditText
    private lateinit var txtCosto: EditText
    private lateinit var txtVenta: EditText

    private lateinit var btnAgregar: Button
    private lateinit var btnVer: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main6)

        txtDescripcion = findViewById(R.id.txtdescripcion)
        txtStock = findViewById(R.id.txtstock)
        txtCosto = findViewById(R.id.txtcosto)
        txtVenta = findViewById(R.id.txtventa)
        txtObservacion = findViewById(R.id.txtobservacion)

        btnAgregar = findViewById(R.id.buttonAddMedicamento)
        btnVer = findViewById(R.id.buttonViewMedicamento)

        btnAgregar.setOnClickListener { addMedicamento() }

        btnVer.setOnClickListener {
            val intent = Intent(applicationContext, ViewMedicamentosActivity::class.java)
            startActivity(intent)
        }
    }

    // Insertar medicamento
    private fun addMedicamento() {
        val descripcion = txtDescripcion.text.toString()
        val preCos = txtCosto.text.toString()
        val preVen = txtVenta.text.toString()
        val observacion = txtObservacion.text.toString()
        val stock = txtStock.text.toString()

        // Crear JSON con los valores
        val params = JSONObject()
        try {
            params.put("descripcion", descripcion)
            params.put("pre_cos", preCos.toDoubleOrNull() ?: 0.0)
            params.put("pre_ven", preVen.toDoubleOrNull() ?: 0.0)
            params.put("observacion", observacion)
            params.put("stock", stock.toIntOrNull() ?: 0)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonRequest = object : com.android.volley.toolbox.JsonObjectRequest(
            Request.Method.POST,
            EndPoints.URL_ADD_MEDICAMENTO,
            params,
            Response.Listener { response ->
                try {
                    Toast.makeText(applicationContext, response.getString("message"), Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
            }) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        // agregar a la cola
        VolleySingleton.instance?.addToRequestQueue(jsonRequest)
    }
}
