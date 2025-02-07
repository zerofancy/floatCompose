package top.ntutn.floatcompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import top.ntutn.floatcompose.ui.theme.FloatComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FloatComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        showAction = {
                            Toast.makeText(this, "I'm a Toast", Toast.LENGTH_SHORT).show()
                        },
                        hideAction = {
                            Toast.makeText(this, "I'm gone", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, showAction: () -> Unit = {}, hideAction: () -> Unit = {}) {
    Column(modifier = modifier) {
        Text(
            text = "Hello $name!",
            modifier = Modifier
        )
        Row {
            Button(onClick = showAction) {
                Text("Show")
            }
            Spacer(Modifier.weight(1f))
            Button(onClick  = hideAction) {
                Text("Hide")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FloatComposeTheme {
        Greeting("Android")
    }
}