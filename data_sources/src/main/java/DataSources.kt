import com.moviesapp.contracts.NetworkStatus

object DataSources {
    var networkStatus: NetworkStatus? = null
        set(value) {
            if (field == networkStatus) field = value
        }
}