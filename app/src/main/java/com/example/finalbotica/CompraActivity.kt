package com.example.finalbotica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import org.json.JSONObject

class CompraActivity : AppCompatActivity() {

    private lateinit var spinnerMedicament: Spinner
    private lateinit var spinnerLaborat: Spinner
    private lateinit var txtLote: EditText
    private lateinit var txtCantidad: EditText
    private lateinit var labelPrecio: TextView

    private lateinit var btnAgregar: Button
    private lateinit var btnVer: Button

    private val medList: MutableList<Medicamentos> = mutableListOf()
    private val medNames: MutableList<String> = mutableListOf()

    private val labList: MutableList<Laboratorios> = mutableListOf()
    private val labNames: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compra)

        txtLote = findViewById(R.id.txtLote)
        labelPrecio = findViewById(R.id.mostrarPrecio)
        txtCantidad = findViewById(R.id.txtCantidad)
        spinnerMedicament = findViewById(R.id.spinnerMedica)
        spinnerLaborat = findViewById(R.id.spinnerLabo)

        btnAgregar = findViewById(R.id.buttonAddCompra)
        btnVer = findViewById(R.id.buttonViewCompras)

        btnAgregar = findViewById(R.id.buttonAddCompra)
        btnVer = findViewById(R.id.buttonViewCompras)

        btnAgregar.setOnClickListener { addCompra() }
        btnVer.setOnClickListener {
            val intent = Intent(applicationContext, ViewCompra::class.java)
            startActivity(intent)
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_reply_24)
        }

        // Cargar datos remotos
        loadMedicamentos()
        loadLaboratorios()

        // Listener para cuando el usuario elige un medicamento -> mostrar precio
        spinnerMedicament.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0 && position < medList.size) {
                    val med = medList[position]
                    // Mostrar precio (formateado a 2 decimales)
                    labelPrecio.text = String.format("S/ %.2f", med.pre_ven)
                } else {
                    labelPrecio.text = ""
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                labelPrecio.text = ""
            }
        }
    }

    // ---------- Cargar medicamentos ----------
    private fun loadMedicamentos() {
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET,
            EndPoints.URL_GET_MEDICAMENTOS,
            null,
            Response.Listener { response ->
                try {
                    val ok = response.optBoolean("ok", false)
                    if (!ok) {
                        Toast.makeText(this, response.optString("message", "Error al obtener medicamentos"), Toast.LENGTH_LONG).show()
                        return@Listener
                    }

                    val array = response.getJSONArray("data")
                    medList.clear()
                    medNames.clear()

                    // recorrer todos los elementos (for)
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        val id = obj.getInt("id_medi")
                        val descripcion = obj.optString("descripcion", "sin descripcion")
                        val observa = obj.getString("observacion")
                        val sto = obj.getInt("stock")
                        val cos = obj.getDouble("pre_cos")
                        val preVen = obj.optDouble("pre_ven", 0.0)

                        // agregar a listas
                        medList.add(Medicamentos(id, descripcion, observa, sto, cos, preVen))
                        medNames.add(descripcion) // lo que se mostrará en el spinner
                    }

                    // crear adapter y asignar al spinner
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, medNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerMedicament.adapter = adapter

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error parseando medicamentos", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.message ?: "Error de red al obtener medicamentos", Toast.LENGTH_LONG).show()
            }
        )

        VolleySingleton.instance?.addToRequestQueue(jsonRequest)
    }

    // ---------- Cargar laboratorios ----------
    private fun loadLaboratorios() {
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET,
            EndPoints.URL_GET_LABORATORIOS,
            null,
            Response.Listener { response ->
                try {
                    val ok = response.optBoolean("ok", false)
                    if (!ok) {
                        Toast.makeText(this, response.optString("message", "Error al obtener laboratorios"), Toast.LENGTH_LONG).show()
                        return@Listener
                    }

                    val array = response.getJSONArray("data")
                    labList.clear()
                    labNames.clear()

                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        val ruc = obj.optString("ruc_lab", "")
                        val razon = obj.optString("razon_social", "sin razon")
                        val dir = obj.getString("direccion")
                        val tel = obj.getString("telefono")
                        val ema = obj.getString("email")

                        labList.add(Laboratorios(ruc, razon, dir, tel, ema))
                        labNames.add(razon) // lo que se mostrará en el spinner
                    }

                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, labNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerLaborat.adapter = adapter

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error parseando laboratorios", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.message ?: "Error de red al obtener laboratorios", Toast.LENGTH_LONG).show()
            }
        )

        VolleySingleton.instance?.addToRequestQueue(jsonRequest)
    }

    private fun addCompra() {
        // obtener posiciones seleccionadas
        val medPos = spinnerMedicament.selectedItemPosition
        val labPos = spinnerLaborat.selectedItemPosition

        if (medPos < 0 || labPos < 0) {
            Toast.makeText(this, "Seleccione medicamento y laboratorio", Toast.LENGTH_SHORT).show()
            return
        }


        val selectedMed = medList[medPos]      // .id (Int)
        val selectedLab = labList[labPos]      // .ruc (String)

        val lote = txtLote.text.toString().trim()
        val cantidadStr = txtCantidad.text.toString().trim()
        val cantidad = cantidadStr.toIntOrNull() ?: 0

        if (lote.isBlank() || cantidad <= 0) {
            Toast.makeText(this, "Ingrese lote y cantidad válidos", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear JSON con los valores solicitados
        val params = JSONObject()
        try {
            params.put("id_medi", selectedMed.id_medi)    // clave: id_medi (ajusta si tu API espera otro nombre)
            params.put("ruc_lab", selectedLab.ruc_lab)   // clave: ruc_lab
            params.put("lote", lote)
            params.put("cantidad", cantidad)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonRequest = object : com.android.volley.toolbox.JsonObjectRequest(
            Request.Method.POST,
            EndPoints.URL_ADD_COMPRA,
            params,
            Response.Listener { response ->
                try {
                    Toast.makeText(applicationContext, response.getString("message"), Toast.LENGTH_LONG).show()
                    // Opcional: limpiar campos después de crear la compra
                    txtLote.text.clear()
                    txtCantidad.text.clear()
                    // Opcional: recargar lista de compras si tienes una función para eso
                    // loadCompras()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError ->
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
            android.R.id.home -> {
                val intent = Intent(this, MainActivity3::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}