package com.example.finalbotica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import org.json.JSONException
import org.json.JSONObject

class MainActivity2 : AppCompatActivity() {

    lateinit var txtUsuario: EditText
    lateinit var txtPassword: EditText
    lateinit var btnIniciar: Button
    lateinit var btnCerrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Activa el botón "atrás" (home)
            setHomeAsUpIndicator(R.drawable.ic_baseline_reply_24) // Cambia icono
        }

        txtUsuario = findViewById(R.id.txtUsuario)
        txtPassword = findViewById(R.id.txtPassword)
        btnIniciar = findViewById(R.id.btnIniciar)
        btnCerrar = findViewById(R.id.btncerrar2)

        btnIniciar.setOnClickListener {
            authenticateUser()
        }

        // Botón cerrar
        btnCerrar.setOnClickListener {
            finishAffinity() // Cierra toda la app
        }
    }

    private fun authenticateUser() {
        val usuario = txtUsuario.text.toString()
        val password = txtPassword.text.toString()

        if (usuario.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear JSON con los valores
        val params = JSONObject()
        try {
            params.put("usuario", usuario)
            params.put("clave", password)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonRequest = object : com.android.volley.toolbox.JsonObjectRequest(
            Request.Method.POST,
            EndPoints.URL_AUTHENTICATE_USER,
            params,
            Response.Listener { response ->
                try {
                    // Interpretar la respuesta (ajusta según el formato que devuelva tu API)
                    val ok = response.optBoolean("ok", false)
                    val message = if (response.has("message")) response.getString("message") else null

                    if (ok) {
                        // Ejemplo: si la API devuelve un token, lo puedes leer así:
                        // if (response.has("token")) { val token = response.getString("token") /*guardar token*/ }

                        Toast.makeText(applicationContext, message ?: "Autenticación exitosa", Toast.LENGTH_LONG).show()
                        // Opcional: navegar a otra Activity o guardar estado de sesión aquí
                        val intent = Intent(this, MainActivity3::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, message ?: "Credenciales incorrectas", Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Error parseando respuesta", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                // Mostrar mensaje de error (puede ser null)
                Toast.makeText(applicationContext, error.message ?: "Error en la petición", Toast.LENGTH_LONG).show()
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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // opcional, para que no vuelva aquí al presionar atrás
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}