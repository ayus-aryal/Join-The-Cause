import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jointhecause.R

class SearchScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchScreen()
        }
    }
}

@Composable
fun SearchScreen() {

    val customFontFamily = FontFamily(
        Font(R.font.inter) // Replace 'inter' with the name of your font file (without extension)
    )

    var location by remember { mutableStateOf(TextFieldValue("")) }
    var checkInDate by remember { mutableStateOf(TextFieldValue("")) }
    var checkOutDate by remember { mutableStateOf(TextFieldValue("")) }
    var noOfBags by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFCA61F), Color(0xFFFCA61F)))),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .padding(30.dp)
                .align(Alignment.TopCenter)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Find Your Luggage Storage ", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color(0xFF333333), fontFamily = customFontFamily)

                CustomTextField(value = location, onValueChange = { location = it }, label = "Enter Location")
                CustomTextField(value = checkInDate, onValueChange = { checkInDate = it }, label = "Check-In Date")
                CustomTextField(value = checkOutDate, onValueChange = { checkOutDate = it }, label = "Check-Out Date")
                CustomTextField(value = noOfBags, onValueChange = { noOfBags = it }, label = "Number of Bags")

                Button(
                    onClick = { /* Search Action */ },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8976FD)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Search", fontSize = 18.sp, color = Color.White, fontFamily = customFontFamily)
                }
            }
        }
    }
}

@Composable
fun CustomTextField(value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun SearchScreenn() {
    SearchScreen()
    
}