import com.android.bademjon.model.TabBarItem
import org.junit.Assert.assertEquals
import org.junit.Test

class TabBarItemTest {

    @Test
    fun testTabBarItem() {
        val title = "Home"
        val selectedIcon = 123
        val unselectedIcon = 456
        val badgeAmount = 5

        val tabBarItem = TabBarItem(title, selectedIcon, unselectedIcon, badgeAmount)

        assertEquals(title, tabBarItem.title)
        assertEquals(selectedIcon, tabBarItem.selectedIcon)
        assertEquals(unselectedIcon, tabBarItem.unselectedIcon)
        assertEquals(badgeAmount, tabBarItem.badgeAmount)
    }

    @Test
    fun testTabBarItemWithoutBadge() {
        val title = "Profile"
        val selectedIcon = 789
        val unselectedIcon = 1011

        val tabBarItem = TabBarItem(title, selectedIcon, unselectedIcon)

        assertEquals(title, tabBarItem.title)
        assertEquals(selectedIcon, tabBarItem.selectedIcon)
        assertEquals(unselectedIcon, tabBarItem.unselectedIcon)
        assertEquals(null, tabBarItem.badgeAmount)
    }
}
