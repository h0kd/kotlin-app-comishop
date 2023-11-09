package com.example.comishop

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class VentaActivity2 : AppCompatActivity() {
    lateinit var botonRegresar: Button
    lateinit var botonConfirmar: Button
    lateinit var precioUni: EditText
    lateinit var precioDespacho: EditText
    lateinit var direccionVenta: EditText
    lateinit var telefonoVenta: EditText
    lateinit var nombreCliente: EditText
    lateinit var nombrePVenta: EditText
    lateinit var precioVenta: EditText
    var productoAVender: Product? = null
    lateinit var imagen: ImageView
    lateinit var calculo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venta2)
        inicializar()


        productoAVender = intent.getParcelableExtra("producto_a_vender") as? Product


        if (productoAVender != null) {
            // Establece los valores del producto en los elementos de la vista
            precioUni.setText(productoAVender?.precio.toString())
            nombrePVenta.setText(productoAVender?.nombre)



        }
    }

    fun inicializar() {
        imagen = findViewById(R.id.imagen_nuevo)
        precioUni = findViewById(R.id.precio_u_nuevo)
        precioDespacho = findViewById(R.id.valor_des_nuevo)
        direccionVenta = findViewById(R.id.direccion_nuevo)
        telefonoVenta = findViewById(R.id.telefono_nuevo)
        nombreCliente = findViewById(R.id.nombre_cliente_nuevo)
        nombrePVenta = findViewById(R.id.nombre_producto_nuevo)
        botonRegresar = findViewById(R.id.regresar_nuevo)
        botonConfirmar = findViewById(R.id.confirmar_nuevo)
        precioVenta = findViewById(R.id.precio_venta_nuevo)
        calculo = findViewById(R.id.field_calculo)

        botonRegresar.setOnClickListener {
            finish()
        }

        botonConfirmar.setOnClickListener {

            confirmarVenta()

        }

        // Agrega TextWatchers a los campos de texto para realizar el cálculo automáticamente
        precioUni.addTextChangedListener(textWatcher)
        precioVenta.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // No necesitas implementar nada aquí
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // No necesitas implementar nada aquí
        }

        override fun afterTextChanged(s: Editable?) {
            // Llama a la función calcular() cuando se cambie el texto en los campos
            calcular()
        }
    }


    fun confirmarVenta() {
        val precioUnitario = precioUni.text.toString().toInt()
        val valorDespacho = precioDespacho.text.toString().toInt()
        val direccion = direccionVenta.text.toString()
        val telefono = telefonoVenta.text.toString()
        val nombreCliente = nombreCliente.text.toString()
        val nombreProducto = nombrePVenta.text.toString()
        val precioVenta = precioVenta.text.toString().toInt()

        // Crea un objeto Venta con los datos del formulario
        val nuevaVenta = productoAVender?.let {
            Venta(
                nombreProducto,
                precioUnitario,
                valorDespacho,
                direccion,
                telefono,
                nombreCliente,
                precioVenta,
                it.url
            )
        }  // La URL del producto deberá manejarse según tus necesidades

        // Obtén una referencia a la base de datos Firebase
        val databaseReference = FirebaseDatabase.getInstance().getReference("venta")

        // Genera una clave única para la venta
        val ventaKey = databaseReference.push().key

        if (ventaKey != null) {
            // Agrega la nueva venta a la base de datos
            productoAVender?.let {
                databaseReference.child(it.nombre).setValue(nuevaVenta).addOnCompleteListener { task ->
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

    fun calcular() {
        val precioUnitarioStr = precioUni.text.toString()
        val precioVentaStr = precioVenta.text.toString()

        if (precioUnitarioStr.isNotEmpty() && precioVentaStr.isNotEmpty()) {
            val precioUnitario = precioUnitarioStr.toInt()
            val precioVenta = precioVentaStr.toInt()

            // Calcula la resta y aplica el factor de 0.3
            val resultadoResta = (precioVenta - precioUnitario) * 0.3

            // Formatea el resultado para mostrarlo con dos decimales
            val resultadoRestaStr = String.format("%.0f", resultadoResta)

            // Establece el resultado en el TextView "calculo"
            calculo.text = "Comisión: $resultadoRestaStr"
        } else {
            // Manejar el caso en el que los campos estén vacíos
            calculo.text = "Los campos de precio unitario y precio de venta deben contener valores válidos."
        }
    }
}

