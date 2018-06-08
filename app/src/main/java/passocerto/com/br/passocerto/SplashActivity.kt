package passocerto.com.br.passocerto

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils.loadAnimation
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        carregar()
    }

    fun carregar(){

        val animacao = loadAnimation(this, R.anim.animacao_splash)
        img_logo_splash.clearAnimation()
        img_logo_splash.startAnimation(animacao)

        Handler().postDelayed({
            val intent = Intent(this@SplashActivity,
                    MainActivity::class.java)

            startActivity(intent)
            this@SplashActivity.finish()

        }, 3000)
    }
}
