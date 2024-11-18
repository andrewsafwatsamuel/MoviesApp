package moviesapp.moviesapp.domain.useCases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.moviesapp.CreditsResponse
import com.moviesapp.DetailsResponse
import com.moviesapp.MovieResponse
import com.moviesapp.Videos
import com.moviesapp.data_sources.repositories.MoviesRepository
import com.moviesapp.domain.useCases.DetailsUseCases
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single.just
import io.reactivex.schedulers.Schedulers
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class DetailsUseCasesKtTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `1- retrieveDetails with successful conditions then result have a value`() {
        val repositoryMock = mock<MoviesRepository> {
            on { retrieveDetails(1010) } doReturn just(
                DetailsResponse(
                    false,
                    "", 0, listOf(),"",
                    1010, "", "", "", "", 0.0f,
                    "", listOf(), listOf(), "", 0, 0, listOf(),
                    "", "", "", false, 0.0, 0, Videos(listOf())
                )
            )
        }
        val id = 1010L
        val connected = true
        val loading = MutableLiveData<Boolean>()
        val result = MutableLiveData<DetailsResponse>()

        with(DetailsUseCases(repositoryMock, id)) {
            retrieveDetails(connected, loading, result)
                ?.subscribeOn(Schedulers.trampoline())
                ?.observeOn(Schedulers.trampoline())
                ?.subscribe()
        }

        assert(result.value != null)
    }

    @Test
    fun `2- retrieveDetails while loading then result have no value`() {
        val repositoryMock = mock<MoviesRepository> {
            on { retrieveDetails(1010) } doReturn just(
                DetailsResponse(
                    false,
                    "", 0, listOf(),"",
                    1010, "", "", "", "", 0.0f,
                    "", listOf(), listOf(), "", 0, 0, listOf(),
                    "", "", "", false, 0.0, 0, Videos(listOf())
                )
            )
        }
        val id = 1010L
        val connected = true
        val loading = MutableLiveData<Boolean>()
        val result = MutableLiveData<DetailsResponse>()

        with(DetailsUseCases(repositoryMock, id)) {
            loading.value = true
            retrieveDetails(connected, loading, result)
                ?.subscribeOn(Schedulers.trampoline())
                ?.observeOn(Schedulers.trampoline())
                ?.subscribe()
        }

        assert(result.value == null)
    }

    @Test
    fun `3- retrieveDetails when not connected then result have no value`() {
        val repositoryMock = mock<MoviesRepository> {
            on { retrieveDetails(1010) } doReturn just(
                DetailsResponse(
                    false,
                    "", 0, listOf(),"",
                    1010, "", "", "", "", 0.0f,
                    "", listOf(), listOf(), "", 0, 0, listOf(),
                    "", "", "", false, 0.0, 0,
                    Videos(listOf())
                )
            )
        }
        val id = 1010L
        val connected = false
        val loading = MutableLiveData<Boolean>()
        val result = MutableLiveData<DetailsResponse>()

        with(DetailsUseCases(repositoryMock, id)) {
            retrieveDetails(connected, loading, result)
                ?.subscribeOn(Schedulers.trampoline())
                ?.observeOn(Schedulers.trampoline())
                ?.subscribe()
        }

        assert(result.value == null)
    }

    @Test(expected = IllegalStateException::class)
    fun `4- retrieveDetails movie id equal 0 then result have a value`() {
        val repositoryMock = mock<MoviesRepository> {
            on { retrieveDetails(1010) } doReturn just(
                DetailsResponse(
                    false,
                    "", 0, listOf(),"",
                    1010, "", "", "", "", 0.0f,
                    "", listOf(), listOf(), "", 0, 0, listOf(),
                    "", "", "", false, 0.0, 0,
                    Videos(listOf())
                )
            )
        }
        val connected = true
        val loading = MutableLiveData<Boolean>()
        val result = MutableLiveData<DetailsResponse>()

        with(DetailsUseCases(repositoryMock)) {
            retrieveDetails(connected, loading, result)
                ?.subscribeOn(Schedulers.trampoline())
                ?.observeOn(Schedulers.trampoline())
                ?.subscribe()
        }
    }

    @Test
    fun `5- retrieveCredits with successful conditions then result contains value`() {
        val repositoryMock = mock<MoviesRepository> {
            on { retrieveCredits(1010) } doReturn just(CreditsResponse(listOf(), listOf()))
        }
        val connected = true
        val loading = MutableLiveData<Boolean>()
        val result = MutableLiveData<CreditsResponse>()

        with(DetailsUseCases(repositoryMock,1010)) {
            retrieveCredits(connected, loading, result)
                ?.subscribeOn(Schedulers.trampoline())
                ?.observeOn(Schedulers.trampoline())
                ?.subscribe()
        }
        assert(result.value != null)
    }

    @Test
    fun `6- retrieveCredits when not connected then result value will be null`() {
        val repositoryMock = mock<MoviesRepository> {
            on { retrieveCredits(1010) } doReturn just(CreditsResponse(listOf(), listOf()))
        }
        val connected = false
        val loading = MutableLiveData<Boolean>()
        val result = MutableLiveData<CreditsResponse>()

        with(DetailsUseCases(repositoryMock,1010)) {
            retrieveCredits(connected, loading, result)
                ?.subscribeOn(Schedulers.trampoline())
                ?.observeOn(Schedulers.trampoline())
                ?.subscribe()
        }
        assert(result.value == null)
    }

    @Test
    fun `7- retrieveCredits while loading then result value will be null`() {
        val repositoryMock = mock<MoviesRepository> {
            on { retrieveCredits(1010) } doReturn just(CreditsResponse(listOf(), listOf()))
        }
        val connected = true
        val loading = MutableLiveData<Boolean>()
        val result = MutableLiveData<CreditsResponse>()

        with(DetailsUseCases(repositoryMock,1010)) {
            loading.value=true
            retrieveCredits(connected, loading, result)
                ?.subscribeOn(Schedulers.trampoline())
                ?.observeOn(Schedulers.trampoline())
                ?.subscribe()
        }
        assert(result.value == null)
    }

    @Test(expected = IllegalStateException::class)
    fun `8- retrieveCredits when id equals 0 then throw exception`() {
        val repositoryMock = mock<MoviesRepository> {
            on { retrieveCredits(1010) } doReturn just(CreditsResponse(listOf(), listOf()))
        }
        val connected = true
        val loading = MutableLiveData<Boolean>()
        val result = MutableLiveData<CreditsResponse>()

        with(DetailsUseCases(repositoryMock)) {
            retrieveCredits(connected, loading, result)
                ?.subscribeOn(Schedulers.trampoline())
                ?.observeOn(Schedulers.trampoline())
                ?.subscribe()
        }
    }

    @Test
    fun `9- retrieveRelated with successful conditions then result contains value`() {
        val repositoryMock = mock<MoviesRepository> {
            on { retrieveRelated(1010,1) } doReturn just(
                MovieResponse(1,10,1, listOf()))
        }
        val connected = true
        val loading = MutableLiveData<Boolean>()
        val result = MutableLiveData<MovieResponse>()

        with(DetailsUseCases(repositoryMock,1010)) {
            retrieveRelated(1,connected, loading, result)
                ?.subscribeOn(Schedulers.trampoline())
                ?.observeOn(Schedulers.trampoline())
                ?.subscribe()
        }
        assert(result.value != null)
    }

    @Test
    fun `10- retrieveRelated when not connected then result value will be null`() {
        val repositoryMock = mock<MoviesRepository> {
            on { retrieveRelated(1010,1) } doReturn just(
                MovieResponse(1,10,1, listOf()))
        }
        val connected = false
        val loading = MutableLiveData<Boolean>()
        val result = MutableLiveData<MovieResponse>()

        with(DetailsUseCases(repositoryMock,1010)) {
            retrieveRelated(1,connected, loading, result)
                ?.subscribeOn(Schedulers.trampoline())
                ?.observeOn(Schedulers.trampoline())
                ?.subscribe()
        }
        assert(result.value == null)
    }

    @Test
    fun `11- retrieveRelated while loading then result value will be null`() {
        val repositoryMock = mock<MoviesRepository> {
            on { retrieveRelated(1010,1) } doReturn just(
                MovieResponse(1,10,1, listOf()))
        }
        val connected = true
        val loading = MutableLiveData<Boolean>()
        val result = MutableLiveData<MovieResponse>()

        with(DetailsUseCases(repositoryMock,1010)) {
            loading.value=true
            retrieveRelated(1,connected, loading, result)
                ?.subscribeOn(Schedulers.trampoline())
                ?.observeOn(Schedulers.trampoline())
                ?.subscribe()
        }
        assert(result.value == null)
    }

    @Test(expected = IllegalStateException::class)
    fun `12- retrieveRelated with id equals 0 then through illegal state exception`() {
        val repositoryMock = mock<MoviesRepository> {
            on { retrieveRelated(1010,1) } doReturn just(
                MovieResponse(1,10,1, listOf()))
        }
        val connected = true
        val loading = MutableLiveData<Boolean>()
        val result = MutableLiveData<MovieResponse>()

        with(DetailsUseCases(repositoryMock)) {
            retrieveRelated(1,connected, loading, result)
                ?.subscribeOn(Schedulers.trampoline())
                ?.observeOn(Schedulers.trampoline())
                ?.subscribe()
        }
    }
}