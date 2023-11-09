package com.example.comishop

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class AdaptadorDeCeldasHistorial(private val context: Context, private val almacenDatos: ArrayList<Venta>): BaseAdapter() {

    private val inflador: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val elementosSeleccionados = HashSet<Venta>()

    override fun getCount(): Int {
        return almacenDatos.size
    }

    override fun getItem(position: Int): Any {
        return almacenDatos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val filaCelda = inflador.inflate(R.layout.adaptador_celda_historial, parent, false)
        val imagen: ImageView = filaCelda.findViewById(R.id.image_historial)
        val nombreProducto: TextView = filaCelda.findViewById(R.id.nombre_p_historiak)
        val precioUnitario: TextView = filaCelda.findViewById(R.id.precio_u_historial)
        val precioDespacho: TextView = filaCelda.findViewById(R.id.valor_despacho_historial)
        val telefono: TextView = filaCelda.findViewById(R.id.telefono_historia)
        val nombreCliente: TextView = filaCelda.findViewById(R.id.nombre_historial)
        val direccion: TextView = filaCelda.findViewById(R.id.direccion_historial)

        val productoVendido = almacenDatos[position]

        nombreProducto.text = "Nombre Producto: " + productoVendido.nombreProducto
        precioUnitario.text = "Precio Unitario: " + productoVendido.precioUnitario.toString()
        precioDespacho.text = "Precio Despacho: " + productoVendido.valorDespacho.toString()
        telefono.text = "Telefono: " + productoVendido.telefono
        nombreCliente.text = "Nombre Cliente: " + productoVendido.nombreCliente
        direccion.text = "Direccion: " + productoVendido.direccion

        val imageUrl = productoVendido.url

        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get().load(imageUrl).into(imagen)
        } else {
            // Maneja el caso en el que la URL de la imagen está vacía o nula
            // Puedes establecer una imagen de marcador de posición o mostrar un mensaje de error, según lo prefieras.
        }

        if (elementosSeleccionados.contains(productoVendido)) {
            // Aplica un estilo diferente para los elementos seleccionados
            filaCelda.setBackgroundColor(Color.GREEN)
        } else {
            // Restaura la apariencia predeterminada para los elementos no seleccionados
            filaCelda.setBackgroundColor(Color.TRANSPARENT)
        }

        filaCelda.setOnClickListener {
            if (elementosSeleccionados.contains(productoVendido)) {
                // El elemento ya está seleccionado, deseléccionalo
                elementosSeleccionados.remove(productoVendido)
            } else {
                // El elemento no está seleccionado, selecciónalo
                elementosSeleccionados.add(productoVendido)
            }
            notifyDataSetChanged()
        }

        return filaCelda
    }

    fun getItemsSeleccionados(): Set<Venta> {
        return elementosSeleccionados
    }

    fun clearSelection() {
        elementosSeleccionados.clear()
        notifyDataSetChanged()
    }
    fun removeItem() {
        almacenDatos.removeAll(elementosSeleccionados)
        elementosSeleccionados.clear()
        notifyDataSetChanged()
    }


}