package com.example.domain.useCases

import androidx.paging.PagingSource.LoadResult
import com.example.MovieResponse
import com.example.domain.FakeMoviesRepository
import com.example.domain.FakeMoviesRepositoryError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class MoviesUseCaseTest {

    @Test
    fun `invoke with successful conditions return new page`() = runBlockingTest {

        //arrange
        val repository = FakeMoviesRepository()
        val useCase = MoviesUseCase(repository)

        //act
        val result = useCase("", 1)

        //assert
        assert(result is LoadResult.Page)

    }

    @Test
    fun `invoke when error thrown return error result`() = runBlockingTest {

        //arrange
        val repository = FakeMoviesRepositoryError()
        val useCase = MoviesUseCase(repository)

        //act
        val result = useCase("", 1)

        //assert
        assert(result is LoadResult.Error)

    }

    @Test
    fun `invoke when current page is 1 then previous page is null`() = runBlockingTest {

        //arrange
        val repository = FakeMoviesRepository()
        val useCase = MoviesUseCase(repository)

        //act
        val result = useCase("", 1) as LoadResult.Page

        //assert
        assert(result.prevKey == null)

    }

    @Test
    fun `invoke when current page is 2 then previous page is 1`() = runBlockingTest {

        //arrange
        val repository = FakeMoviesRepository(MovieResponse(2, 10, 10, listOf()))
        val useCase = MoviesUseCase(repository)

        //act
        val result = useCase("", 1) as LoadResult.Page

        //assert
        assert(result.prevKey == 1)

    }

    @Test
    fun `invoke when current page is last page then next page is null`() = runBlockingTest {

        //arrange
        val repository = FakeMoviesRepository(MovieResponse(10, 10, 10, listOf()))
        val useCase = MoviesUseCase(repository)

        //act
        val result = useCase("", 1) as LoadResult.Page

        //assert
        assert(result.nextKey == null)

    }

    @Test
    fun `invoke when current page number less than last page then next page equals current page + 1`() =
        runBlockingTest {

            //arrange
            val currentPage=9
            val repository = FakeMoviesRepository(MovieResponse(currentPage, 10, 10, listOf()))
            val useCase = MoviesUseCase(repository)

            //act
            val result = useCase("", 1) as LoadResult.Page

            //assert
            assert(result.nextKey == currentPage.plus(1))

        }
}
