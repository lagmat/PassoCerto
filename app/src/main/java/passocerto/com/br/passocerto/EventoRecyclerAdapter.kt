package passocerto.com.br.passocerto

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import java.util.ArrayList

class EventoRecyclerAdapter(eventsList: List<Evento>, internal var context: Context) : RecyclerView.Adapter<EventoRecyclerAdapter.EventViewHolder>() {

    internal var eventsList: List<Evento> = ArrayList()
    init {
        this.eventsList = eventsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_itens_evento, parent, false)
        return EventViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val events = eventsList[position]
        holder.nome.text = events.nome
        holder.local.text = events.local
        holder.list_item.background = ContextCompat.getDrawable(context, R.color.colorPrimary)

        holder.itemView.setOnClickListener {
            val i = Intent(context, CadastroEventoActivity::class.java)
            i.putExtra("Mode", "A")
            i.putExtra("Id", events.id)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }

        holder.ivdelete.setOnClickListener{
            val i = Intent(context, CadastroEventoActivity::class.java)
            i.putExtra("Mode", "E")
            i.putExtra("Id", events.id)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nome: TextView = view.findViewById(R.id.tvNome) as TextView
        var local: TextView = view.findViewById(R.id.tvLocal) as TextView
        var list_item: LinearLayout = view.findViewById(R.id.list_item) as LinearLayout
        var ivdelete: ImageView = view.findViewById(R.id.ivDelete) as ImageView
    }
}
