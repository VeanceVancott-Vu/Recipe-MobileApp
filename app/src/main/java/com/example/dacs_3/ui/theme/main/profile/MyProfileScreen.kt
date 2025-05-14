import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dacs_3.R
import com.example.dacs_3.model.User
import com.example.dacs_3.utils.TopBar
import com.example.dacs_3.viewmodel.AuthViewModel

@Composable
fun MyProfileScreen(authViewModel: AuthViewModel = viewModel(),
                    navController: NavController
) {

    val user by authViewModel.currentUser.collectAsState()

    Log.d("My profile Screen", "user: $user")
    LaunchedEffect(Unit) {
        authViewModel.fetchAndSetCurrentUser()
    }

    Scaffold(
        topBar = {
            TopBar("My profile", showRightIcon = false)
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(top = innerPadding.calculateTopPadding()-35.dp)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                user?.let { ProfileHeader(it) }
                BioCard()
                Spacer(modifier = Modifier.height(40.dp))
                SectionCard(
                    iconRes = R.drawable.person,
                    title = "Personal Information",
                    {navController.navigate("edit_profile")}
                )
                SectionCard(
                    iconRes = R.drawable.settings,
                    title = "Setting",
                    {navController.navigate("edit_profile")}

                )
                SectionCard(
                    iconRes = R.drawable.bookmark,
                    title = "My Recipes",
                    {navController.navigate("edit_profile")}

                )
                SectionCard(
                    iconRes = R.drawable.person,
                    title = "My Statistics",
                    {navController.navigate("edit_profile")}

                )
            }
        }
    )
}


@Composable
fun ProfileHeader(user: User) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 24.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.mockrecipeimage),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(70.dp))
        )
        Spacer(modifier = Modifier.width(30.dp))
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = user.username,
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xff3b684d)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = user.email,
                color = Color(0xff9ab0a3),
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.pin_drop),
                    contentDescription = "Location",
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xff3b684d)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${user.location}", color = Color(0xff3b684d), fontSize = 16.sp)
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
                .shadow(8.dp, RoundedCornerShape(16.dp), clip = false) // <- shadow here
                .background(Color(0xffdbe6de), RoundedCornerShape(16.dp))
                .padding(16.dp)

        ) {
            Text(
                text = "Burned the kitchen 3 times, still call myself a Master Chef. Anyone wanna save my pasta?",
                fontSize = 15.sp,
                color = Color(0xff0a3d1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .shadow(8.dp, RoundedCornerShape(16.dp), clip = false) // <- shadow here

                    .width(160.dp)
                    .background(Color(0xffdbe6de), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center


            )
            {
                Text(
                    text = "Kitchen Buddy: 01",
                    fontSize = 14.sp,
                    color = Color(0xff0a3d1f)
                )
            }

            Spacer(modifier = Modifier.width(24.dp)) // Adds space between the two boxes


            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .shadow(8.dp, RoundedCornerShape(16.dp), clip = false) // <- shadow here

                    .width(160.dp)
                    .background(Color(0xffdbe6de), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center


            )
            {
                Text(
                    text = "Follower : 01",
                    fontSize = 14.sp,
                    color = Color(0xff0a3d1f)
                )
            }

        }
    }


@Composable
fun SectionCard(iconRes: Int, title: String,onClick:()->Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp), clip = false) // <- shadow here
            .clickable { onClick() } // 🔥 Make the whole card clickable
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
                    Icon(
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
            Icon(
                painter = painterResource(id = R.drawable.arrow_forward),
                contentDescription = "Expand",
                modifier = Modifier.size(24.dp),
                tint = Color(0xff3b684d)


            )
        }
    }
}



@Preview
@Composable
fun MyProfileScreenPreview()
{
    MyProfileScreen(navController = rememberNavController())
}
