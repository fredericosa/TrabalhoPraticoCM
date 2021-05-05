package com.example.trabalhopraticocm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.trabalhopraticocm.api.EndPoints
import com.example.trabalhopraticocm.api.OutputLogin
import com.example.trabalhopraticocm.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var checkboxInput: CheckBox
    private lateinit var preferences: SharedPreferences
    private var lembrar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        usernameInput = findViewById(R.id.username)
        passwordInput = findViewById(R.id.password)
        checkboxInput = findViewById(R.id.lembrar)

        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        lembrar = preferences.getBoolean("lembrar", false)

        if(lembrar){
            val intent = Intent(this@Login, Menu::class.java)
            startActivity(intent);
            finish()
        }
    }

    fun login(view: View) {
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val username = usernameInput.text.toString()
        val password = passwordInput.text.toString()
        val checked_remember: Boolean = checkboxInput.isChecked
        val call = request.login(username = username, password = password)

        call.enqueue(object : Callback<OutputLogin> {
            override fun onResponse(call: Call<OutputLogin>, response: Response<OutputLogin>){
                if (response.isSuccessful){
                    val c: OutputLogin = response.body()!!
                    if(TextUtils.isEmpty(usernameInput.text) || TextUtils.isEmpty(passwordInput.text)) {
                        Toast.makeText(this@Login, "Erro ao fazer login.", Toast.LENGTH_LONG).show()
                    }else{
                        if(c.status == "false"){
                            Toast.makeText(this@Login, c.MSG, Toast.LENGTH_LONG).show()
                        }else{
                            val preferences_edit : SharedPreferences.Editor = preferences.edit()
                            preferences_edit.putString("username", username)
                            preferences_edit.putString("password", password)
                            preferences_edit.putInt("id", c.id)
                            preferences_edit.putBoolean("lembrar", checked_remember)
                            preferences_edit.apply()

                            val intent = Intent(this@Login, Menu::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
            override fun onFailure(call: Call<OutputLogin>, t: Throwable){
                Toast.makeText(this@Login,"${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}