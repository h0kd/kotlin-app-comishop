package com.example.comishop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var fireBase : FirebaseAuth
    private lateinit var estadoAutenti : FirebaseAuth.AuthStateListener
    lateinit var botonIngreso : Button
    lateinit var campoCorreo : EditText
    lateinit var campoPwd : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inicializar()
    }

    fun inicializar(){
        fireBase = Firebase.auth
        botonIngreso = findViewById(R.id.boton_ingreso)
        campoCorreo = findViewById(R.id.correo_field)
        campoPwd = findViewById(R.id.pwd_field)
        botonIngreso.setOnClickListener {
            validar(campoCorreo.text.toString(),campoPwd.text.toString())
        }
    }

    fun validar(email: String , pwd : String){
        fireBase.signInWithEmailAndPassword(email,pwd)
            .addOnCompleteListener(this){task->
                if (task.isSuccessful){
                    val user = fireBase.currentUser
                    if (user?.email.toString()=="admin@gmail.com"){
                        Toast.makeText(this, "me lleva a la vista del admin", Toast.LENGTH_SHORT).show()
                        Toast.makeText(baseContext,"Bienvenido "+ user?.email.toString(), Toast.LENGTH_LONG).show()
                        val intent = Intent(this,AdminActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(baseContext,"Bienvenido "+ user?.email.toString(), Toast.LENGTH_LONG).show()
                        val nombre = user?.email.toString()
                        val intent = Intent(this,ProductosActivity::class.java)
                        intent.putExtra("nombre",nombre)
                        startActivity(intent)
                    }
                }else{
                    Toast.makeText(baseContext, "error de autentificacion", Toast.LENGTH_SHORT).show()
                }
            }
    }
}