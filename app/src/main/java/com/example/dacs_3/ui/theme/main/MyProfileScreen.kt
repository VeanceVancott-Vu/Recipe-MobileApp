import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dacs_3.R

@Composable
fun MyProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar()
        ProfileHeader()
        BioCard()
        SectionCard(
            iconRes = R.drawable.image5_368212,
            title = "Personal Information"
        )
        SectionCard(
            iconRes = R.drawable.image7_368216,
            title = "My Statistics"
        )
        SectionCard(
            iconRes = R.drawable.image9_368210,
            title = "My Recipes"
        )
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Back */ }) {
            Image(painterResource(id = R.drawable.image1_367148), contentDescription = "Back")
        }
        Text(
            text = "My Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xff3b684d)
        )
        IconButton(onClick = { /* Settings */ }) {
            Image(painterResource(id = R.drawable.image2_367155), contentDescription = "Settings")
        }
    }
}

@Composable
fun ProfileHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 24.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.image3_367159),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "Léonie Diane",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xff3b684d)
            )
            Text(
                text = "@2510lenie",
                color = Color(0xff9ab0a3),
                fontSize = 14.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.image4_368223),
                    contentDescription = "Location",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Viet Nam", color = Color(0xff3b684d), fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun BioCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xffdbe6de), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Burned the kitchen 3 times, still call myself a Master Chef. Anyone wanna save my pasta?",
            fontSize = 16.sp,
            color = Color(0xff0a3d1f)
        )
    }
}

@Composable
fun SectionCard(iconRes: Int, title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xffdbe6de), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xff98b4a2), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xff3b684d)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.image8_368218),
                contentDescription = "Expand",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
