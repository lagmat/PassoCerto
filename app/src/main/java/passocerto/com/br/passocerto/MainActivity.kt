package passocerto.com.br.passocerto

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ic_msg.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


public class MainActivity : AppCompatActivity() {

    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        session = SessionManager(applicationContext)

        if(session.isLoggedIn())
        {
            var i : Intent = Intent(applicationContext, HomeActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            finish()
        }

        btn_avancar.setOnClickListener {
            efetuarLogin()
        }


        btn_social_facebook.setOnClickListener {
            implementacoes_futuras()
        }

        btn_social_Google.setOnClickListener {
            implementacoes_futuras()
        }

        btn_cadastro.setOnClickListener {
            val intent = Intent(this, CadastroUsuarioActivity::class.java)
            startActivity(intent);
        }

        lnk_esqueceu_senha.setOnClickListener {
            implementacoes_futuras()
        }

        lnk_termos_uso.setOnClickListener {
            implementacoes_futuras()
        }
    }

    protected fun implementacoes_futuras() {

        val d = AlertDialog.Builder(this)

        val child = layoutInflater.inflate(R.layout.ic_msg, null)
        d.setView(child)

        val msg = child.Lbl_msg
        val btn01 = child.btn_msg01
        btn01.setText("OK")
        btn01.setVisibility(View.VISIBLE)

        val btn02 = child.btn_msg02
        btn02.setText("Sim")
        btn02.setVisibility(View.GONE)

        val btn03 = child.btn_msg03
        btn03.setText("Não")
        btn03.setVisibility(View.GONE)

        val alertDialog = d.create()

        btn01.setOnClickListener(View.OnClickListener {
            alertDialog.dismiss()
        })

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent)

        msg.setText(R.string.msg_implementacao_futura)
        alertDialog.show()
        return
    }

    protected fun efetuarLogin() {

        try {

            val d = AlertDialog.Builder(this)

            val child = layoutInflater.inflate(R.layout.ic_msg, null)
            d.setView(child)

            val msg = child.Lbl_msg
            val btn01 = child.btn_msg01
            btn01.setText("OK")
            btn01.setVisibility(View.VISIBLE)

            val btn02 = child.btn_msg02
            btn02.setText("Sim")
            btn02.setVisibility(View.GONE)

            val btn03 = child.btn_msg03
            btn03.setText("Não")
            btn03.setVisibility(View.GONE)

            val alertDialog = d.create()

            btn01.setOnClickListener(View.OnClickListener {
                alertDialog.dismiss()
            })

            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent)

            if (edt_login.text.toString().isEmpty()) {
                msg.setText(R.string.msg_req_login)
                alertDialog.show()
                return
            }

            if (edt_senha.text.toString().isEmpty()) {
                msg.setText(R.string.msg_req_senha)
                alertDialog.show()
                return
            }

                val retrofit = Retrofit.Builder()
                        .baseUrl("http://www.hubnerspage.com.br/passocerto/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

          val rest = retrofit.create(UsuarioREST::class.java)
            val intent = Intent(this, HomeActivity::class.java)
            rest.getUsuarioPorLogin(edt_login.text.toString(), edt_senha.text.toString())
                    .enqueue(object : Callback<Usuario> {
                        override fun onResponse(call: Call<Usuario>?, response: Response<Usuario>?) {

                            if (response?.code() == 200) {

                                session.criateLoginSession(response.body()?.nome.toString(), response.body()?.login.toString(), response.body()?.email.toString())
                                startActivity(intent);
                            }
                            if (response?.code() == 404) {
                                msg.setText(R.string.msg_erro_login)
                                alertDialog.show()
                                return;
                            }
                        }
                        override fun onFailure(call: Call<Usuario>?, t: Throwable?) {
                            msg.setText(R.string.msg_erro_api)
                            alertDialog.show()
                        }
                    })

        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}
