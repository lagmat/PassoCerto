package passocerto.com.br.passocerto

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cadastro_evento.*
import kotlinx.android.synthetic.main.ic_msg.view.*
import passocerto.com.br.passocerto.R.id.*

class CadastroEventoActivity : AppCompatActivity() {

    var dbHandler: DatabaseHandler? = null
    var isEditMode = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_evento)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initDB()
        initOperations()

        btn_voltar.setOnClickListener {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent);
        }

        btn_telefonar.setOnClickListener {
           efetuarLigacao()
        }

        btn_compartilhar.setOnClickListener {
            efetuarCompartilhamento()
        }
    }

    private fun initDB() {
        dbHandler = DatabaseHandler(this)
        if (intent != null && intent.getStringExtra("Mode") == "A") {
            isEditMode = true
            btn_telefonar.setVisibility(View.VISIBLE);
            btn_compartilhar.setVisibility(View.VISIBLE);
            val evento: Evento = dbHandler!!.getEvento(intent.getIntExtra("Id",0))
            edt_nome.setText(evento.nome)
            edt_local.setText(evento.local)
            edt_data.setText(evento.data)
            edt_hora.setText(evento.hora)
            edt_ddd.setText(evento.ddd)
            edt_telefone.setText(evento.telefone)
        }
        if (intent != null && intent.getStringExtra("Mode") == "E") {
            val evento: Evento = dbHandler!!.getEvento(intent.getIntExtra("Id",0))
            val success = dbHandler?.excluirEvento(intent.getIntExtra("Id", 0)) as Boolean
            if (success)
                finish()
        }
    }

    private fun initOperations() {
        btn_prosseguir.setOnClickListener({

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

                if (edt_nome.text.toString().isEmpty()) {
                    msg.setText(R.string.msg_req_nome)
                    alertDialog.show()
                    return@setOnClickListener
                }

                if (edt_local.text.toString().isEmpty()) {
                    msg.setText(R.string.msg_req_local)
                    alertDialog.show()
                    return@setOnClickListener
                }

                if (edt_data.text.toString().isEmpty()) {
                    msg.setText(R.string.msg_req_data)
                    alertDialog.show()
                    return@setOnClickListener
                }

                if (edt_hora.text.toString().isEmpty()) {
                    msg.setText(R.string.msg_req_hora)
                    alertDialog.show()
                    return@setOnClickListener
                }

                if (edt_ddd.text.toString().isEmpty()) {
                    msg.setText(R.string.msg_req_ddd)
                    alertDialog.show()
                    return@setOnClickListener
                }

                if (edt_telefone.text.toString().isEmpty()) {
                    msg.setText(R.string.msg_req_telefone)
                    alertDialog.show()
                    return@setOnClickListener
                }

            var success: Boolean = false
            if (!isEditMode) {
                val evento: Evento = Evento()
                evento.nome = edt_nome.text.toString()
                evento.local = edt_local.text.toString()
                evento.data = edt_data.text.toString()
                evento.hora = edt_hora.text.toString()
                evento.ddd = edt_ddd.text.toString()
                evento.telefone = edt_telefone.text.toString()
                success = dbHandler?.cadastrarEvento(evento) as Boolean
            } else {
                val evento: Evento = Evento()
                evento.id = intent.getIntExtra("Id", 0)
                evento.nome = edt_nome.text.toString()
                evento.local = edt_local.text.toString()
                evento.data = edt_data.text.toString()
                evento.hora = edt_hora.text.toString()
                evento.ddd = edt_ddd.text.toString()
                evento.telefone = edt_telefone.text.toString()
                success = dbHandler?.atualizarEvento(evento) as Boolean
            }
            if (success)
                finish()

        } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun efetuarLigacao(){
        val ddd: String  = edt_ddd.getText().toString();
        val telefone: String  = edt_telefone.getText().toString();
        val chamada = Intent(Intent.ACTION_DIAL)
        chamada.data = Uri.parse("tel:" +"+55" + ddd + telefone)
        startActivity(chamada)
    }

    fun efetuarCompartilhamento() {

        val nome: String  = edt_nome.getText().toString();
        val local: String  = edt_local.getText().toString();
        val data: String  = edt_data.getText().toString();
        val hora: String  = edt_hora.getText().toString();
        val sendIntent = Intent(Intent())
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "App Passo Certo" + "\n\n" + "Evento: " + nome + "\n" + "Local: " + local + "\n" + "Data: " + data + "\n" + "Hora: " + hora + "\n");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
