package passocerto.com.br.passocerto

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var session: SessionManager
    var eventoRecyclerAdapter: EventoRecyclerAdapter? = null;
    var fab: FloatingActionButton? = null
    var recyclerView: RecyclerView? = null
    var dbHandler: DatabaseHandler? = null
    var listEvents: List<Evento> = ArrayList<Evento>()
    var linearLayoutManager: LinearLayoutManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        initViews()
        initOperations()
        //initDB()

        session = SessionManager(applicationContext)

        var user: HashMap<String,String> = session.getUserDetails()
        var nome: String = user.get(SessionManager.KEY_NOME)!!
        var login: String = user.get(SessionManager.KEY_LOGIN)!!
        var email: String = user.get(SessionManager.KEY_EMAIL)!!

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_cadastro_evento -> {
                val intent = Intent(this, CadastroEventoActivity::class.java)
                startActivity(intent);
            }
            R.id.nav_mapa -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent);
            }

            R.id.nav_sobre -> {
                val intent = Intent(this, SobreActivity::class.java)
                startActivity(intent);
            }

            R.id.nav_sair -> {
                session.logoutUser()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent);
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun initDB() {
        dbHandler = DatabaseHandler(this)
        listEvents = (dbHandler as DatabaseHandler).evento()
        eventoRecyclerAdapter = EventoRecyclerAdapter(eventsList = listEvents, context = applicationContext)
        (recyclerView as RecyclerView).adapter = eventoRecyclerAdapter
    }

    fun initViews() {
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        eventoRecyclerAdapter = EventoRecyclerAdapter(eventsList = listEvents, context = applicationContext)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        (recyclerView as RecyclerView).layoutManager = linearLayoutManager
    }

    fun initOperations() {
        fab?.setOnClickListener { view ->
            val i = Intent(applicationContext, CadastroEventoActivity::class.java)
            i.putExtra("Mode", "")
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        initDB()
    }

}
