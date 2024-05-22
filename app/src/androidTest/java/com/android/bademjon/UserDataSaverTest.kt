import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.android.bademjon.helper.UserDataSaver
import com.android.bademjon.model.UserDataModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.unsetMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserDataSaverTest {

    private lateinit var context: Context
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var userDataSaver: UserDataSaver

    @Before
    fun setup() {
        context = mockk()
        dataStore = mockk()
        every { context.dataStore } returns dataStore
        userDataSaver = UserDataSaver(context)
    }

    @Test
    fun `test saver function`() = runBlockingTest {
        val userData = UserDataModel("Alice", 60, 170, 30, 15, 65, 1, 0, 0)
        userDataSaver.saver(userData)

        // Verify that the appropriate values are saved in the data store
        // You can add more verification steps based on your requirements
        // For example:
        assertEquals("Alice", dataStore.data[nameKey])
        assertEquals("60", dataStore.data[weightKey])
        assertEquals("170", dataStore.data[heightKey])
        assertEquals("30", dataStore.data[ageKey])
    }
}
