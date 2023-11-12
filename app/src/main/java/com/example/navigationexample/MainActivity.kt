package com.example.navigationexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navigationexample.ui.theme.NavigationExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home()
                }
            }
        }
    }
}

@Composable
fun Home() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.FIRST_SCREEN) {
        composable(Routes.FIRST_SCREEN) {
            FirstScreen(navigation = navController)
        }
        composable(
            Routes.SECOND_SCREEN,
            arguments = listOf(navArgument(Routes.Values.SECOND_SCREEN_VALUE) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            SecondScreen(
                navigation = navController,
                textToShow = backStackEntry.arguments?.getString(
                    Routes.Values.SECOND_SCREEN_VALUE,
                    "Default value"
                )
            )
        }
        composable(Routes.THIRD_SCREEN) {
            ThirdScreen(navigation = navController)
        }
    }
}

object Routes {

    const val FIRST_SCREEN = "FirstScreen"
    const val SECOND_SCREEN = "SecondScreen/{${Values.SECOND_SCREEN_VALUE}}"
    const val THIRD_SCREEN = "ThirdScreen"

    fun getSecondScreenPath(customValue: String?): String =
        // to avoid null and empty strings
        if (customValue != null && customValue.isNotBlank()) "SecondScreen/$customValue" else "SecondScreen/Empty"

    object Values {
        const val SECOND_SCREEN_VALUE = "customValue"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstScreen(navigation: NavController) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("First screen")
        TextField(
            value = textFieldValue,
            onValueChange = { newText ->
                textFieldValue = newText
            }
        )
        Button(onClick = {
            navigation.navigate(Routes.getSecondScreenPath(textFieldValue.text))
        }) {
            Text(text = "Go to second screen")
        }
    }
}

@Composable
fun SecondScreen(navigation: NavController, textToShow: String?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Second screen")
        Text("Text from previous screen: $textToShow")
        Button(onClick = {
            navigation.popBackStack()
        }) {
            Text(text = "Back to first screen")
        }
        Button(onClick = {
            navigation.navigate(Routes.THIRD_SCREEN)
        }) {
            Text(text = "Go to third screen")
        }
    }
}

@Composable
fun ThirdScreen(navigation: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Third screen")
        Button(onClick = {
            navigation.popBackStack()
        }) {
            Text(text = "Back to second screen")
        }
        Button(onClick = {
            navigation.popBackStack(Routes.FIRST_SCREEN, false)
        }) {
            Text(text = "Back to first screen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NavigationExampleTheme {
        Home()
    }
}