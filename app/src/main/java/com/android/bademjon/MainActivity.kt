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


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppView(ChangeLanguage = ::ChangeLanguage)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ContextWrapper(newBase.setAppLocale(newBase)))
    }

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: Int,
    unselectedIcon: Int,
    title: String,
    badgeAmount: Int? = null

) {
    BadgedBox(
        modifier = Modifier.fillMaxHeight(),
        badge = { TabBarBadgeView(badgeAmount) }) {

        // To show top bar on selected item
        if (isSelected) {
            Divider(
                color = MaterialTheme.colorScheme.secondary,
                thickness = dimensionResource(id = R.dimen._5sdp),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .align(Alignment.Center)
            )
        }
        Column {
            Image(
                painter = painterResource(id =  if (isSelected) {
                    selectedIcon
                } else {
                    unselectedIcon
                }),
                contentDescription = title,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .size(35.dp),
                colorFilter = ColorFilter.tint(color =  if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.secondary
                })
            )

            Text(
                text = title, modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontFamily = GetFont(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge(modifier = Modifier.padding(top = 40.dp, start = 15.dp)) {
            Text(count.toString())

        }
    }
}

// End navigation








// pages contents

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileView() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .absolutePadding(bottom = 200.dp)
            .background(color = colorResource(id = R.color.background))
    ) {
        AppBarView()
        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .weight(1.5F),
            verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(15.dp)
                ),
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.name_and_surname)+" : ",
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1F),
                    textAlign = TextAlign.End,
                    fontFamily = GetFont(),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                var userDataSaver=UserDataSaver(LocalContext.current)
                BasicTextField(
                    value = userData.name.value,
                    onValueChange = {
                        userData.updateName(it)
                        CoroutineScope(Dispatchers.IO).launch {
                        userDataSaver.saver(userData)

                    } },
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .weight(1F)
                        .background(color = colorResource(id = R.color.transparent)),
                    textStyle = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Start
                    )
                )


            }

        }

        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .weight(3F),
            verticalAlignment = Alignment.CenterVertically) {

            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .weight(1F)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(15.dp)
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(id = R.string.height),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = GetFont(),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
                BasicTextField(
                    value = userData.height.value.toString(),
                    onValueChange = {
                        var i=0;
                        if(it.toIntOrNull()!=null) {i=it.toInt()}
                        userData.updateHeight(i) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center)
                )


            }

            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .weight(1F)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(15.dp)
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(id = R.string.weight),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = GetFont(),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
                BasicTextField(
                    value = userData.weight.value.toString(),
                    onValueChange = {
                        var i=0;
                        if(it.toIntOrNull()!=null) {i=it.toInt()}
                        userData.updateWeight(i) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center)
                )


            }

            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .weight(1F)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(15.dp)
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(id = R.string.age),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = GetFont(),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
                BasicTextField(
                    value = userData.age.value.toString(),
                    onValueChange = {
                        var i=0;
                        if(it.toIntOrNull()!=null) {i=it.toInt()}
                        userData.updateAge(i) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center)
                )


            }


        }
        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .weight(3F),
            verticalAlignment = Alignment.CenterVertically) {

            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .weight(1F)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(15.dp)
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(id = R.string.around_the_wrist),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = GetFont(),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
                BasicTextField(
                    value =userData.wrist.value.toString(),
                    onValueChange = {
                        var i=0;
                        if(it.toIntOrNull()!=null) {i=it.toInt()}
                        userData.updateWrist(i) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center)
                )


            }

            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .weight(2F)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(15.dp)
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(id = R.string.activity_rate),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = GetFont(),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))


                val itemList = listOf<String>(
                    stringResource(id = R.string.rate1),
                    stringResource(id = R.string.rate2),
                    stringResource(id = R.string.rate3))
                var buttonModifier = Modifier.width(100.dp)

                DropdownList(itemList = itemList,
                            selectedIndex = userData.rate.value.toInt(),
                            modifier = buttonModifier,
                            onItemClick = {userData.updateRate(it)})




            }



        }
        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .weight(3F),
            verticalAlignment = Alignment.CenterVertically) {


            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .weight(1F)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(15.dp)
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(id = R.string.gender),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = GetFont(),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))

                val itemList1 = listOf<String>(
                    stringResource(id = R.string.male),
                    stringResource(id = R.string.female))
                var buttonModifier1 = Modifier.width(100.dp)

                DropdownList(itemList = itemList1,
                    selectedIndex = userData.gender.value.toInt(),
                    modifier = buttonModifier1,
                    onItemClick = {userData.updateGender(it)})


            }

            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .weight(2F)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(15.dp)
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(id = R.string.special_disease),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = GetFont(),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))


                val itemList2 = listOf<String>(
                    stringResource(id = R.string.ihave),
                    stringResource(id = R.string.idhave))
                var buttonModifier2 = Modifier.width(100.dp)

                DropdownList(itemList = itemList2,
                    selectedIndex = userData.disease.value.toInt(),
                    modifier = buttonModifier2,
                    onItemClick = {userData.updateDisease(it)})


            }


        }
    }
}


@Composable
fun DietView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background))
    ) {
        AppBarView()
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .absolutePadding(bottom = 70.dp)
            .background(color = colorResource(id = R.color.background))
    ) {
        AppBarView()
        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .weight(2F),
            verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1F), horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text=homeData.receipt.value.toString()+" "+stringResource(id = R.string.calories)+"\n"+stringResource(id = R.string.receipt),
                    fontFamily = GetFont(),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Image(
                    painter = painterResource(id = R.drawable.rice_bowl),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(30.dp)
                )
            }
            Column(modifier = Modifier.weight(2F), horizontalAlignment = Alignment.CenterHorizontally){
               Box (Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
                   Text(
                       text = homeData.leftOver.value.toString()+" " + stringResource(id = R.string.calories) + "\n" + stringResource(
                           id = R.string.left_over
                       ),
                       fontFamily = GetFont(),
                       color = MaterialTheme.colorScheme.primary,
                       textAlign = TextAlign.Center,
                       fontWeight = FontWeight.Bold
                   )
                   CircularProgressIndicator(
                       modifier = Modifier.size(200.dp, 200.dp),
                       strokeWidth = 15.dp,
                       progress = homeData.leftOverProcess.value,
                       color = MaterialTheme.colorScheme.primary,
                       trackColor = MaterialTheme.colorScheme.secondary,
                   )
               }
            }
            Column(modifier = Modifier.weight(1F), horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text=homeData.burned.value.toString()+" "+stringResource(id = R.string.calories)+"\n"+stringResource(id = R.string.burned),
                    fontFamily = GetFont(),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Image(
                    painter = painterResource(id = R.drawable.fire),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }
        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .weight(1.5F),
            verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier
                .weight(1F)
                .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text=stringResource(id = R.string.fat),
                    fontFamily = GetFont(),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)),
                    progress = homeData.fatProcess.value,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.secondary,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text=homeData.fat.value.toString()+" "+stringResource(id = R.string.gram),
                    fontFamily = GetFont(),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(modifier = Modifier
                .weight(1F)
                .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text=stringResource(id = R.string.protein),
                    fontFamily = GetFont(),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)),
                    progress = homeData.proteinProcess.value,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.secondary,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text=homeData.protein.value.toString()+" "+stringResource(id = R.string.gram),
                    fontFamily = GetFont(),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(modifier = Modifier
                .weight(1F)
                .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text=stringResource(id = R.string.carbohydrate),
                    fontFamily = GetFont(),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)),
                    progress = homeData.carboProcess.value,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.secondary,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text=homeData.carbo.value.toString()+" "+stringResource(id = R.string.gram),
                    fontFamily = GetFont(),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .weight(2.5F), horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(20.dp))
            Button(modifier = Modifier
                .width(350.dp)
                .height(40.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = { AddProtein() },
                shape = RoundedCornerShape(10.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.milk_carton),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1F),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)
                )
                Text(modifier = Modifier
                    .weight(7F),
                    text=stringResource(id = R.string.breakfast)+" : "+stringResource(id = R.string.suggested)+" "+homeData.breakfast.value.toString()+" "+stringResource(id = R.string.calories),
                    fontFamily = GetFont(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary
                )
                Image(
                    painter = painterResource(id = R.drawable.group),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1F),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(modifier = Modifier
                .width(350.dp)
                .height(40.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = { AddCarbo() },
                shape = RoundedCornerShape(10.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.beef_burger),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1F),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)
                )
                Text(modifier = Modifier
                    .weight(7F),
                    text=stringResource(id = R.string.lunch)+" : "+stringResource(id = R.string.suggested)+" "+homeData.lunch.value.toString()+" "+stringResource(id = R.string.calories),
                    fontFamily = GetFont(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary
                )
                Image(
                    painter = painterResource(id = R.drawable.group),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1F),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(modifier = Modifier
                .width(350.dp)
                .height(40.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = { AddFat() },
                shape = RoundedCornerShape(10.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.turkeycock),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1F),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)
                )
                Text(modifier = Modifier
                    .weight(7F),
                    text=stringResource(id = R.string.dinner)+" : "+stringResource(id = R.string.suggested)+" "+homeData.dinner.value.toString()+" "+stringResource(id = R.string.calories),
                    fontFamily = GetFont(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary
                )
                Image(
                    painter = painterResource(id = R.drawable.group),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1F),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(modifier = Modifier
                .width(350.dp)
                .height(40.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = { DesAll() },
                shape = RoundedCornerShape(10.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.del),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1F),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)
                )
                Text(modifier = Modifier
                    .weight(7F),
                    text=stringResource(id = R.string.clear)+" "+stringResource(id = R.string.calories),
                    fontFamily = GetFont(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary
                )
                Image(
                    painter = painterResource(id = R.drawable.dec),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1F),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)
                )
            }
        }
    }
}


@Composable
fun SportView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background))
    ) {
        AppBarView()
    }
}


@Composable
fun SettingView(ChangeLanguage: ()-> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background))
    ) {
        AppBarView()
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        Button(onClick = { ChangeLanguage()})
        {
            Text(
                text=stringResource(id = R.string.language),
                fontFamily = GetFont(),
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
        }

    }
}


// End pages











// pages sections


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppBarView(){
    var userDataSaver=UserDataSaver(LocalContext.current)
    CenterAlignedTopAppBar(modifier = Modifier
        .clickable {
            CoroutineScope(Dispatchers.IO).launch {
                    userDataSaver.saver(userData)

            }},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Row {
            Image(
                    painter = painterResource(id = R.drawable.eggplant),
                    contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .size(35.dp),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                )
            Text(
                text=stringResource(id = R.string.app_name),
                fontFamily = GetFont(),
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            }
        }
    )
}


@Composable
fun DropdownList(itemList: List<String>,selectedIndex: Int, modifier: Modifier, onItemClick: (Int) -> Unit) {

    var showDropdown by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        // button
        Box(
            modifier = modifier
                .clickable { showDropdown = true },
//            .clickable { showDropdown = !showDropdown },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = itemList[selectedIndex],
                modifier = Modifier.padding(3.dp),
                textAlign = TextAlign.Center,
                fontFamily = GetFont(),
                color = MaterialTheme.colorScheme.secondary
            )
            
        }

        // dropdown list
        Box() {
            if (showDropdown) {
                Popup(
                    alignment = Alignment.TopCenter,
                    properties = PopupProperties(
                        excludeFromSystemGesture = true,
                    ),
                    // to dismiss on click outside
                    onDismissRequest = { showDropdown = false }
                ) {

                    Column(
                        modifier = modifier
                            .heightIn(max = 90.dp)
                            .verticalScroll(state = scrollState)
                            .border(width = 1.dp, color = Color.Gray),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        itemList.onEachIndexed { index, item ->
                            if (index != 0) {
                                Divider(thickness = 1.dp, color = Color.LightGray)
                            }
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primary)
                                    .fillMaxWidth()
                                    .clickable {
                                        onItemClick(index)
                                        showDropdown = !showDropdown

                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = item,
                                    textAlign = TextAlign.Center,
                                    fontFamily = GetFont(),
                                    color = MaterialTheme.colorScheme.secondary)
                            }
                        }

                    }
                }
            }
        }
    }

}


// Ens sections









// helpers


// priview for editor
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    //AppView()
}


fun GetFont(): FontFamily {

    val PersianFamily = FontFamily(
        Font(R.font.iran_sans_medium, FontWeight.Light),
        Font(R.font.iran_sans_regular, FontWeight.Normal),
        Font(R.font.iran_sans_regular, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.iran_sans_medium, FontWeight.Medium),
        Font(R.font.iran_sans_bold, FontWeight.Bold)
    )

    val EnglishFamily = FontFamily(
        Font(R.font.product_sans_medium, FontWeight.Light),
        Font(R.font.product_sans_regular, FontWeight.Normal),
        Font(R.font.product_sans_regular, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.product_sans_medium, FontWeight.Medium),
        Font(R.font.product_sans_bold, FontWeight.Bold)
    )

    return PersianFamily;
}


@Composable
fun CheckIntent():Boolean{
    val context = LocalContext.current
    val activity = context.findActivity()
    val intent = activity?.intent
    try {
        val uri= intent?.data
        val height= uri?.getQueryParameter("height")?.toInt()
        val weight= uri?.getQueryParameter("weight")?.toInt()
        if(!intentNull.value && uri!=null && height!=null && weight!=null) {
            homeData.receipt(10 * height)
            homeData.burned(6 * weight)
            homeData.leftOver(homeData.receipt.intValue - homeData.burned.intValue + 85)
            homeData.leftOverProcess(Random.nextFloat())
            homeData.fatProcess(Random.nextFloat())
            homeData.proteinProcess(Random.nextFloat())
            homeData.carboProcess(Random.nextFloat())
            homeData.fat(3 * height)
            homeData.protein(9 * weight)
            homeData.carbo(15 * height)
            homeData.breakfast(4 * weight)
            homeData.lunch(5 * height)
            homeData.dinner(6 * weight)
            return true
        }
    }catch(_:Exception){}
    return false
}


@Composable
fun ClearIntent() {
    intentNull.value=true
}



fun AddProtein() {
    homeData.protein(homeData.protein.intValue+1)
}


fun AddCarbo() {
    homeData.carbo(homeData.carbo.intValue+1)
}


fun AddFat() {
    homeData.fat(homeData.fat.intValue+1)
}


fun DesAll() {
    if(homeData.protein.intValue>0) homeData.protein(homeData.protein.intValue-1)
    if(homeData.carbo.intValue>0) homeData.carbo(homeData.carbo.intValue-1)
    if(homeData.fat.intValue>0) homeData.fat(homeData.fat.intValue-1)
}


@Composable
fun GetFloat(min:Int,max:Int):Float {
    if(min==0) return 0F
    var min2:Float=min*1F
    var max2:Float=max*1F
    if(min2>=max2) max2=min2*1.5F

    var one:Float=max2/100F
    var one2:Float=1F/100F
    var per:Float=min2/one
    return per*one2
}


fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

// End helpers