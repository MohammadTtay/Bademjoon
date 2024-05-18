package com.android.bademjon

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.bademjon.helper.UserDataSaver
import com.android.bademjon.model.HomeDataModel
import com.android.bademjon.model.TabBarItem
import com.android.bademjon.model.UserDataModel
import com.android.bademjon.ui.theme.BademjonTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random

/**
 * The main activity of the application.
 */
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppView(ChangeLanguage = ::ChangeLanguage)
        }
    }

    /**
     * Attaches the base context to the activity and sets the app locale.
     *
     * @param newBase The new base context.
     */

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ContextWrapper(newBase.setAppLocale(newBase)))
    }

    /**
     * Sets the app locale based on the user's preference.
     *
     * @param newBase The base context.
     * @return The context with the updated locale configuration.
     */

    fun Context.setAppLocale(newBase: Context): Context {
        val sharedPref = applicationContext.getSharedPreferences("app",Context.MODE_PRIVATE)
        var language = sharedPref.getString("lang", "fa")
        val config = resources.configuration

            val locale = Locale(language)
            Locale.setDefault(locale)
            config.setLocale(locale)
            config.setLayoutDirection(locale)

        return createConfigurationContext(config)
    }

    /**
     * Changes the app language between English and Persian.
     */

    fun ChangeLanguage()
    {
        val sharedPref = applicationContext.getSharedPreferences("app",Context.MODE_PRIVATE)
        var defaultValue = sharedPref.getString("lang", "fa")
        if(defaultValue=="fa")
        {
            defaultValue="en"
        }else{
            defaultValue="fa"
        }
        with (sharedPref.edit()) {
            putString("lang", defaultValue)
            apply()
            val intent = intent
            finish()
            startActivity(intent)
        }
    }

}



// views

// bottom navigation bar with pages handler
private val homeData = HomeDataModel()
private val userData = UserDataModel()
private var intentNull =mutableStateOf(false)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppView(ChangeLanguage: ()-> Unit) {

    UserDataSaver(LocalContext.current).geter(userData)


    // setting up the individual tabs

    val profileTab = TabBarItem(
        title = stringResource(R.string.profile),
        selectedIcon = R.drawable.user,
        unselectedIcon = R.drawable.user
    )
    val dietTab = TabBarItem(
        title = stringResource(R.string.diet),
        selectedIcon = R.drawable.pizza,
        unselectedIcon = R.drawable.pizza
    )
    val homeTab = TabBarItem(
        title = stringResource(R.string.home),
        selectedIcon = R.drawable.home,
        unselectedIcon = R.drawable.home,
        badgeAmount = 7
    )
    val sportTab = TabBarItem(
        title = stringResource(id = R.string.sport),
        selectedIcon = R.drawable.lift,
        unselectedIcon = R.drawable.lift,
    )
    val settingsTab = TabBarItem(
        title = stringResource(R.string.setting),
        selectedIcon = R.drawable.setting,
        unselectedIcon = R.drawable.setting,
    )


    // creating a list of all the tabs
    val tabBarItems = listOf(profileTab, dietTab, homeTab, sportTab, settingsTab)

    // creating our navController
    val navController = rememberNavController()



    if(!CheckIntent()) {

        homeData.receipt(homeData.breakfast.intValue+homeData.lunch.intValue+homeData.dinner.intValue)
        homeData.leftOver(homeData.receipt.intValue - homeData.burned.intValue)

        homeData.leftOverProcess(GetFloat(homeData.burned.intValue,homeData.receipt.intValue))
        homeData.fatProcess(GetFloat(homeData.fat.intValue,100))
        homeData.proteinProcess(GetFloat(homeData.protein.intValue,100))
        homeData.carboProcess(GetFloat(homeData.carbo.intValue,100))


        // Male Gender
        if (userData.gender.value.toInt() == 0) {
            homeData.burned((homeData.receipt.intValue/5) * 2)

            homeData.breakfast((homeData.protein.intValue) * ((userData.weight.value.toInt()+userData.height.value.toInt()) /8))
            homeData.lunch((homeData.carbo.intValue) * ((userData.weight.value.toInt()+userData.height.value.toInt()) /12))
            homeData.dinner((homeData.fat.intValue) * ((userData.weight.value.toInt()+userData.height.value.toInt()) /16))

        } else {

            homeData.burned((homeData.receipt.intValue/4) * 2)

            homeData.breakfast((homeData.protein.intValue) * ((userData.weight.value.toInt()+userData.height.value.toInt()) /10))
            homeData.lunch((homeData.carbo.intValue) * ((userData.weight.value.toInt()+userData.height.value.toInt()) /15))
            homeData.dinner((homeData.fat.intValue) * ((userData.weight.value.toInt()+userData.height.value.toInt()) /20))
        }
    }


    BademjonTheme(darkTheme = false) {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(bottomBar = { TabView(tabBarItems, navController) }) {
                NavHost(navController = navController, startDestination = homeTab.title) {
                    composable(profileTab.title) {
                        ClearIntent()
                        ProfileView()
                    }
                    composable(dietTab.title) {
                        DietView()
                    }
                    composable(homeTab.title) {
                        HomeView()
                    }
                    composable(sportTab.title) {
                        SportView()
                    }
                    composable(settingsTab.title) {
                        SettingView(ChangeLanguage = ChangeLanguage)
                    }
                }
            }
        }
    }

}



@Composable
fun TabView(tabBarItems: List<TabBarItem>,
            navController: NavController) {
    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(2)
    }
    NavigationBar(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
            .height(70.dp),
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        // looping over each tab to generate the views and navigation for each item
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxHeight()
                    .size(60.dp),
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    navController.navigate(tabBarItem.title)
                },
                icon = {
                    TabBarIconView(
                        isSelected = selectedTabIndex == index,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.secondary,
                    unselectedTextColor = MaterialTheme.colorScheme.secondary,
                    indicatorColor = MaterialTheme.colorScheme.primary
                )


            )
        }

    }
}

