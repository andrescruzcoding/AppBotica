package com.example.finalbotica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import org.json.JSONException
import org.json.JSONObject

class LaboratorioActivity : AppCompatActivity() {

    private lateinit var txtRucLab: EditText
    private lateinit var txtRazon: EditText
    private lateinit var txtDireccion: EditText
    private lateinit var txtNroTel: EditText
    private lateinit var txtCorreo: EditText

    private lateinit var btnAgregar: Button
    private lateinit var btnVer: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laboratorio)

        txtRucLab = findViewById(R.id.txtRucLab)
        txtRazon = findViewById(R.id.txtRazon)
        txtDireccion = findViewById(R.id.txtDireccion)
        txtNroTel = findViewById(R.id.txtNroTel)
        txtCorreo = findViewById(R.id.txtCorreo)

        btnAgregar = findViewById(R.id.buttonAddLaboratorio)
        btnVer = findViewById(R.id.buttonViewLaboratorio)

        btnAgregar.setOnClickListener { addLaboratorio() }

        btnVer.setOnClickListener {
            val intent = Intent(applicationContext, ViewLaboratorio::class.java)
            startActivity(intent)
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Activa el botón "atrás" (home)
            setHomeAsUpIndicator(R.drawable.ic_baseline_reply_24) // Cambia icono
        }
    }

    private fun addLaboratorio() {
        val ruc = txtRucLab.text.toString()
        val razon = txtRazon.text.toString()
        val direccion = txtDireccion.text.toString()
        val telefono = txtNroTel.text.toString()
        val correo = txtCorreo.text.toString()

        // Crear JSON con los valores
        val params = JSONObject()
        try {
            params.put("ruc_lab", ruc)
            params.put("razon_social", razon)
            params.put("direccion", direccion)
            params.put("telefono", telefono)
            params.put("email", correo)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonRequest = object : com.android.volley.toolbox.JsonObjectRequest(
            Request.Method.POST,
            EndPoints.URL_ADD_LABORATORIO,
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

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { // Este es el botón "up" (flecha o icono que pusiste)
                val intent = Intent(this, MainActivity3::class.java)
                startActivity(intent)
                finish() // opcional, para que no vuelva aquí al presionar atrás
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}