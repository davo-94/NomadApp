package cl.vasquez.nomadapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import cl.vasquez.nomadapp.viewmodel.PostViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class PostViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: PostViewModel

    @Before
    fun setup() {
        viewModel = PostViewModel()
    }

    @Test
    fun `estado inicial posts esta vacio`() = runTest {
        val posts = viewModel.posts.value
        assertTrue(posts.isEmpty())
    }

    @Test
    fun `loadPosts no crashea`() = runTest {
        viewModel.loadPosts()
        advanceUntilIdle()

        // Si no lanza excepción, el test pasa
        assertTrue(true)
    }
}
