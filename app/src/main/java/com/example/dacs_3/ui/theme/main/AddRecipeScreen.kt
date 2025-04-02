package ai.codia.x.composeui.demo


import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dacs_3.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen() {

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            imageUri = uri
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    var title:String by remember { mutableStateOf("") }
    var description:String by remember { mutableStateOf("") }
    var servingSize:String by remember { mutableStateOf("") }
    var cookingTime:String by remember { mutableStateOf("") }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // **Top Bar**
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /*TODO: Back action */
                //  navController.navigate("homepage")
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrowback),
                    contentDescription = "Back",
                    tint = Color(0xFF4A7C59)
                )
            }
            Button(
                onClick = { /* TODO: Save Draft */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F764E))
            ) {
                Text("Save Draft", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // **Image Edit & Add Section**
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFBCC6C0)),
            contentAlignment = Alignment.TopStart
        ) {
            Image(
                //  painter =  painterResource(id = R.drawable.mockrecipeimage),
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { /* TODO: Edit action */
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        println("Add Photo")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = "edit",
                        tint = Color(0xFF4A7C59),
                        modifier = Modifier.size(20.dp)
                    )
                    Text("Edit", color = Color(0xFF4A7C59))


                }
                Button(
                    onClick = { /* TODO: Add Photo */

                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        println("Add Photo")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.addwhite),
                        contentDescription = "add",
                        tint = Color(0xFF4A7C59),
                        modifier = Modifier.size(20.dp)
                    )
                    Text("Add Photo", color = Color(0xFF4A7C59))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // **Recipe Title Input**
        TextField(
            value = title,
            onValueChange = { title = it },
            placeholder = {
                Text(
                    "Example: The Best Sweet and Sour Fish Soup",
                    color = Color(0xFF9AB0A3)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(67.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE8F0E8))
                .padding(8.dp)
                .heightIn(
                    min = 56.dp,
                    max = 200.dp
                ), //  Automatically expands but limits max height
            maxLines = Int.MAX_VALUE, // Allows unlimited lines
            minLines = 1, // Starts with one line,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // **Story Input**
        TextField(
            value = description,
            onValueChange = { description = it },
            placeholder = {
                Text(
                    "Share the story behind this dish...",
                    color = Color(0xFF9AB0A3)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE8F0E8))
                .padding(8.dp)
                .heightIn(
                    min = 56.dp,
                    max = 200.dp
                ), // ✅ Automatically expands but limits max height
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),

            )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically // Align columns properly
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally // Center text above the TextField
            ) {
                Text(
                    "Serving Size",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A7C59),
                    textAlign = TextAlign.Center
                )
                TextField(
                    value = servingSize,
                    onValueChange = { servingSize = it },
                    placeholder = { Text("e.g. 4 servings", color = Color(0xFF9AB0A3)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE8F0E8))
                        .padding(8.dp),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
                )
            }

            Spacer(modifier = Modifier.width(16.dp)) // Space between columns

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally // Center text above the TextField
            ) {
                Text(
                    "Cooking Time",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A7C59),
                    textAlign = TextAlign.Center
                )
                TextField(
                    value = cookingTime,
                    onValueChange = { cookingTime = it },
                    placeholder = { Text("e.g. 30 min", color = Color(0xFF9AB0A3)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE8F0E8))
                        .padding(8.dp),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
                )
            }
        }



        Spacer(modifier = Modifier.height(16.dp))

        // **Ingredients Section**
        Text("Ingredients", fontWeight = FontWeight.Bold, color = Color(0xFF4A7C59))

        repeat(3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE8F0E8))
                        .padding(8.dp)
                )
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE8F0E8))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddRecipeScreenPreview() {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AddRecipeScreen()
        }

}
