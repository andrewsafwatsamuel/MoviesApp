import com.example.SuccessfulQuery
import com.example.domain.databaseGateWay.movieAppDatabase

interface SearchRepository {
    fun getSuccessfulQueries(): List<SuccessfulQuery>
    fun checkPresentQuery(queryString: String): SuccessfulQuery
    fun addQuery(query: SuccessfulQuery)
}

class SearchRepositoryImplementer : SearchRepository {
    override fun getSuccessfulQueries() = movieAppDatabase.successfulQueryDao.queryAll()

    override fun checkPresentQuery(queryString: String) =
        movieAppDatabase.successfulQueryDao.checkPresentQuery(queryString)

    override fun addQuery(query: SuccessfulQuery) = movieAppDatabase.successfulQueryDao.addQuery(query)
}