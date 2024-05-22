import com.android.bademjon.model.UserDataModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserDataModelTest {

    private lateinit var userDataModel: UserDataModel

    @Before
    fun setup() {
        userDataModel = UserDataModel()
    }

    @Test
    fun testUpdateName() {
        val newName = "Alice"
        userDataModel.updateName(newName)
        assertEquals(newName, userDataModel.name.value)
    }

    @Test
    fun testUpdateWeight() {
        val newWeight = 70
        userDataModel.updateWeight(newWeight)
        assertEquals(newWeight.toString(), userDataModel.weight.value)
    }

    @Test
    fun testUpdateHeight() {
        val newHeight = 170
        userDataModel.updateHeight(newHeight)
        assertEquals(newHeight.toString(), userDataModel.height.value)
    }

    @Test
    fun testUpdateWeight() {
        val userDataModel = UserDataModel()
        val newWeight = 70
        userDataModel.updateWeight(newWeight)
        assertEquals(newWeight.toString(), userDataModel.weight.value)
    }

    @Test
    fun testUpdateHeight() {
        val userDataModel = UserDataModel()
        val newHeight = 170
        userDataModel.updateHeight(newHeight)
        assertEquals(newHeight.toString(), userDataModel.height.value)
    }

    @Test
    fun testUpdateAge() {
        val userDataModel = UserDataModel()
        val newAge = 25
        userDataModel.updateAge(newAge)
        assertEquals(newAge.toString(), userDataModel.age.value)
    }

    @Test
    fun testUpdateWrist() {
        val userDataModel = UserDataModel()
        val newWrist = 16
        userDataModel.updateWrist(newWrist)
        assertEquals(newWrist.toString(), userDataModel.wrist.value)
    }

    @Test
    fun testUpdateRate() {
        val userDataModel = UserDataModel()
        val newRate = 80
        userDataModel.updateRate(newRate)
        assertEquals(newRate.toString(), userDataModel.rate.value)
    }

    @Test
    fun testUpdateGender() {
        val userDataModel = UserDataModel()
        val newGender = 1 // assuming 1 as male
        userDataModel.updateGender(newGender)
        assertEquals(newGender.toString(), userDataModel.gender.value)
    }

    @Test
    fun testUpdateDisease() {
        val userDataModel = UserDataModel()
        val newDisease = 2 // assuming 2 as a specific disease code
        userDataModel.updateDisease(newDisease)
        assertEquals(newDisease.toString(), userDataModel.disease.value)
    }
}
