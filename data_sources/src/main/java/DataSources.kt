import android.content.Context

object DataSources {

    lateinit var applicationContext: Context
        private set

    fun init(applicationContext: Context) {
        this.applicationContext = applicationContext
    }

}