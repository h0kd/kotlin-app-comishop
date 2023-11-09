package com.example.comishop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class AgregarProductoActivity : AppCompatActivity() {
    lateinit var campoId: EditText
    lateinit var campoNombre: EditText
    lateinit var campoDescripcion: EditText
    lateinit var campoPrecio: EditText
    lateinit var campoUrl: EditText
    lateinit var botonGuardar: Button
    lateinit var botonVolver: Button
    var productoAEditar: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_producto)
        inicializar()

        // Verificar si se pasaron datos para edición
        productoAEditar = intent.getParcelableExtra("producto_a_editar") as? Product

        if (productoAEditar != null) {
            campoId.setText(productoAEditar?.id.toString())
            campoNombre.setText(productoAEditar?.nombre)
            campoDescripcion.setText(productoAEditar?.descripcion)
            campoPrecio.setText(productoAEditar?.precio.toString())
            campoUrl.setText(productoAEditar?.url)
            campoNombre.isEnabled = false
        }
    }

    fun inicializar() {
        campoId = findViewById(R.id.field_id)
        campoNombre = findViewById(R.id.field_nombre)
        campoDescripcion = findViewById(R.id.field_descripcion)
        campoPrecio = findViewById(R.id.field_precio)
        campoUrl = findViewById(R.id.field_url)
        botonGuardar = findViewById(R.id.boton_insertar_producto)
        botonVolver = findViewById(R.id.boton_volver_admin2)

        botonGuardar.setOnClickListener {
            ingresarDatos()
        }
        botonVolver.setOnClickListener {
            finish()
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }
    }

    fun ingresarDatos() {
        val id = campoId.text.toString().toInt()
        val nombre = campoNombre.text.toString()
        val descripcion = campoDescripcion.text.toString()
        val precio = campoPrecio.text.toString().toInt()
        val url = campoUrl.text.toString()

        val nuevoProducto = Product(id, nombre, descripcion, precio, url)
        val databaseReference = FirebaseDatabase.getInstance().getReference("producto")

        if (productoAEditar != null) {
            // Actualizar el producto existente en lugar de agregar uno nuevo
            productoAEditar?.nombre?.let { databaseReference.child(it).setValue(nuevoProducto) }
        } else {
            // Agregar un nuevo producto
            databaseReference.child(nombre).setValue(nuevoProducto)
        }

        Toast.makeText(this, "Producto guardado con éxito", Toast.LENGTH_SHORT).show()
        finish()
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
    }
}
