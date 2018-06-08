package passocerto.com.br.passocerto

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cadastro_usuario.*
import kotlinx.android.synthetic.main.ic_msg.view.*
import passocerto.com.br.passocerto.R.id.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



public  class CadastroUsuarioActivity : AppCompatActivity() {

    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)

        session = SessionManager(applicationContext)

        btn_prosseguir.setOnClickListener {
            // Handler code here.
            efetuarCadastro();
        }

        btn_voltar.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent);
        }
    }

     private fun efetuarCadastro() {

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

            if (edt_add_nome.text.toString().isEmpty()) {
                msg.setText(R.string.msg_req_nome)
                alertDialog.show()
                return
            }

            if (edt_add_email.text.toString().isEmpty()) {
                msg.setText(R.string.msg_req_email)
                alertDialog.show()
                return
            }

            if (!edt_add_email.text.toString().contains("@") || (!edt_add_email.text.toString().contains("."))) {
                msg.setText(R.string.msg_erro_email);
                alertDialog.show();
                return;
            }

            if (edt_add_login.text.toString().isEmpty()) {
                msg.setText(R.string.msg_req_login)
                alertDialog.show()
                return
            }

            if (edt_add_senha.text.toString().isEmpty()) {
                msg.setText(R.string.msg_req_senha)
                alertDialog.show()
                return
            }

            if (edt_add_rep.text.toString().isEmpty()) {
                msg.setText(R.string.msg_req_rep)
                alertDialog.show()
                return
            }

            if (edt_add_rep.text.toString() != edt_add_senha.text.toString()) {
                msg.setText(R.string.msg_dif_rep)
                alertDialog.show()
                return
            }

            val retrofit = Retrofit.Builder()
                    .baseUrl("http://www.hubnerspage.com.br/passocerto/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()


            val usuario = Usuario(nome = edt_add_nome.text.toString(), email = edt_add_email.text.toString(), login = edt_add_login.text.toString(), senha = edt_add_senha.text.toString(), ativo = 1)

            val rest = retrofit.create(UsuarioREST::class.java)
            val intent = Intent(this, HomeActivity::class.java)
            rest.inserirUsuario(usuario)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>?, response: Response<Void>?) {

                            if (response?.code() == 201) {
                                session.criateLoginSession(edt_add_nome.text.toString(),edt_add_login.text.toString(),edt_add_email.text.toString())

                                msg.setText("Usuário inserido com sucesso");
                                alertDialog.show();
                                startActivity(intent);
                            }
                            if (response?.code() == 404) {
                                msg.setText(R.string.msg_erro_login)
                                alertDialog.show()
                                return;
                            }
                        }
                        override fun onFailure(call: Call<Void>?, t: Throwable?) {
                            msg.setText(R.string.msg_erro_api)
                            alertDialog.show()
                        }
                    })

        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}
