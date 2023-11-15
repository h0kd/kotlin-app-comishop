package com.example.comishop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class VentaEditarActivity : AppCompatActivity() {
    lateinit var botonRegresar: Button
    lateinit var botonConfirmar: Button
    lateinit var precioUni: EditText
    lateinit var precioDespacho: EditText
    lateinit var direccionVenta: EditText
    lateinit var telefonoVenta: EditText
    lateinit var nombreCliente: EditText
    lateinit var nombrePVenta: EditText
    lateinit var precioVenta: EditText
    lateinit var cantidadVenta: EditText
    lateinit var imagen: ImageView

    var ventaAEditar: Venta? = null
    var productoAVender: Venta? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venta)
        inicializar()
    }

    fun inicializar() {
        precioUni = findViewById(R.id.precio_u_venta2)
        precioDespacho = findViewById(R.id.valor_despacho_venta2)
        direccionVenta = findViewById(R.id.direccion_venta2)
        telefonoVenta = findViewById(R.id.telefono_venta2)
        nombreCliente = findViewById(R.id.nombre_cliente_venta2)
        nombrePVenta = findViewById(R.id.nombre_p_venta2)
        botonRegresar = findViewById(R.id.boton_regreso2)
        botonConfirmar = findViewById(R.id.boton_confirmar2)
        precioVenta = findViewById(R.id.precio_venta_2)
        imagen = findViewById(R.id.imagen_nuevo)
        cantidadVenta = findViewById(R.id.cantidad_editar_venta)
        botonRegresar.setOnClickListener {
            finish()
        }

        // Obtén el objeto Venta desde el Intent
        ventaAEditar = intent.getParcelableExtra("ventaEditar") as? Venta

        if (ventaAEditar != null) {
            // Editar una venta existente
            precioUni.setText(ventaAEditar?.precioUnitario.toString())
            nombrePVenta.setText(ventaAEditar?.nombreProducto)
            precioDespacho.setText(ventaAEditar?.valorDespacho.toString())
            direccionVenta.setText(ventaAEditar?.direccion)
            telefonoVenta.setText(ventaAEditar?.telefono)
            nombreCliente.setText(ventaAEditar?.nombreCliente)
            precioVenta.setText(ventaAEditar?.precioVenta.toString())
            cantidadVenta.setText(ventaAEditar?.cantidad.toString())
            precioVenta.isEnabled = false

            Picasso.get()
                .load(ventaAEditar?.url)
                .placeholder(R.mipmap.ic_launcher)
                .into(imagen)

        } else if (productoAVender != null) {
            // Realizar una nueva venta
            precioUni.setText(productoAVender?.precioUnitario.toString())
            nombrePVenta.setText(productoAVender?.nombreProducto)
        } else {
            Toast.makeText(
                this,
                "No se recibió ningún producto para la venta.",
                Toast.LENGTH_SHORT
            ).show()
        }

        botonConfirmar.setOnClickListener {
            confirmarVenta()
        }
    }

    fun confirmarVenta() {
        val precioUnitario = precioUni.text.toString().toInt()
        val valorDespacho = precioDespacho.text.toString().toInt()
        val direccion = direccionVenta.text.toString()
        val telefono = telefonoVenta.text.toString()
        val nombreCliente = nombreCliente.text.toString()
        val nombreProducto = nombrePVenta.text.toString()
        val cantidad = cantidadVenta.text.toString().toInt()

        // Modificación aquí
        val precioVentaText = precioVenta.text.toString()
        val precioVenta = if (precioVentaText.isNotEmpty()) precioVentaText.toInt() else 0

        // Verificar si el precioVenta es cero y mostrar un mensaje si es necesario
        if (precioVenta == 0) {
            Toast.makeText(
                this,
                "Ingresa un valor válido para el precio de venta",
                Toast.LENGTH_SHORT
            ).show()
            return  // Sale de la función para evitar continuar con la lógica
        }

        if (ventaAEditar != null) {
            // Editar una venta existente
            val ventaEditada = ventaAEditar?.url?.let {
                Venta(
                    nombreProducto,
                    precioUnitario,
                    valorDespacho,
                    direccion,
                    telefono,
                    nombreCliente,
                    precioVenta,
                    it,
                    cantidad,
                    ventaAEditar!!.idUser // Conserva el UID original
                )
            }

            // Obtiene la referencia de la venta en la base de datos
            val ventaRef = FirebaseDatabase.getInstance().getReference("venta").child(ventaAEditar!!.idUser!!)

            // Actualiza los datos de la venta
            if (ventaRef != null) {
                ventaAEditar!!.idUser?.let {
                    ventaRef.child(ventaAEditar!!.nombreProducto).setValue(ventaEditada).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Venta editada con éxito", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Error al editar la venta", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        } else if (productoAVender != null) {
            // Agregar una nueva venta
            val nuevaVenta = Venta(
                // Genera un nuevo ID o utiliza lógica específica para asignar uno
                nombreProducto,
                precioUnitario,
                valorDespacho,
                direccion,
                telefono,
                nombreCliente,
                precioVenta,
                productoAVender!!.url,
                cantidad,
                FirebaseAuth.getInstance().currentUser?.uid // UID del usuario actual
            )

            val databaseReference = FirebaseDatabase.getInstance().getReference("ventas")
            val ventaKey = databaseReference.child(nuevaVenta.idUser!!).push().key

            if (ventaKey != null) {
                // Agrega la nueva venta
                databaseReference.child(nuevaVenta.idUser!!).child(ventaKey).setValue(nuevaVenta)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Venta confirmada con éxito", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Error al confirmar la venta", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}



