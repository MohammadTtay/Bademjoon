import com.android.bademjon.model.HomeDataModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HomeDataModelTest {

    private lateinit var homeDataModel: HomeDataModel

    @Before
    fun setup() {
        homeDataModel = HomeDataModel()
    }

    @Test
    fun testReceipt() {
        val receiptValue = 100
        homeDataModel.receipt(receiptValue)
        assertEquals(receiptValue, homeDataModel.receipt.value)
    }

    @Test
    fun testLeftOver() {
        val leftOverValue = 50
        homeDataModel.leftOver(leftOverValue)
        assertEquals(leftOverValue, homeDataModel.leftOver.value)
    }

    @Test
    fun testLeftOverProcess() {
        val leftOverProcessValue = 0.5f
        homeDataModel.leftOverProcess(leftOverProcessValue)
        assertEquals(leftOverProcessValue, homeDataModel.leftOverProcess.value, 0.0)
    }

    // Repeat the same test format for other functions like burned, fat, fatProcess, protein, proteinProcess, carbo, carboProcess, breakfast, lunch, dinner
}
