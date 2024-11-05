package com.example.credassignment

import android.health.connect.datatypes.units.Percentage
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.credassignment.models.ApiResponse
import com.example.credassignment.models.Item
import com.example.credassignment.models.OpenState
import com.example.credassignment.network.ApiService
import com.example.credassignment.ui.theme.CredAssignmentTheme
import com.example.credassignment.ui.theme.DarkBlack
import com.example.credassignment.viewModel.ItemViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback
import kotlin.math.roundToInt

var selectedPlan : EmiPlan = EmiPlan(
    isChecked = false,
    Months = "12 months",
    Price = "4,247",
    Color = Color(0xFF41323C)
    );
var ItemsList :  List<Item> = emptyList()

class MainActivity : ComponentActivity() {
    private val viewModel: ItemViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchItems()
        setContent {
            @OptIn(ExperimentalMaterial3Api::class)
            val items by viewModel.items.collectAsState()
            val isLoading by viewModel.isLoading.collectAsState()
//            fetchItems()
            if (isLoading) {
                LoadingScreen(modifier = Modifier.fillMaxSize()) // Show a loading screen while fetching data

            } else {
                if (items.isNotEmpty()) {
                    var firstBottomSheetOpenState = items[0].open_state
                    var firstBottomSheetCloseState = items[0].closed_state
                    var secondBottomSheetOpenState = items[1].open_state
                    var secondBottomSheetCloseState = items[1].closed_state
                    var thirdBottomSheetOpenState = items[2].open_state
                    var thirdBottomSheetCloseState = items[2].closed_state

            val money = remember{
                mutableStateOf(0f)
            }
            val totalBalance: Int = items[0].open_state.body.card.max_range

            val scaffoldState = rememberBottomSheetScaffoldState()
            val scope = rememberCoroutineScope()
            var isSheetVisible by rememberSaveable { mutableStateOf(false) }

            val scaffoldState2 = rememberBottomSheetScaffoldState()
            val scope2 = rememberCoroutineScope()
            var isSheetVisible2 by rememberSaveable { mutableStateOf(false) }




            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(DarkBlack)
//                    .background(Color(0xFF0F1419))
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
//                    .clickable(indication = null, //
//                        interactionSource = remember { MutableInteractionSource() }) {
//
////                        isSheetVisible = false;
//                        scope.launch {
//                            scaffoldState.bottomSheetState.partialExpand()
//                            isSheetVisible = true;
//                        }
//                    }
                ,

                contentAlignment = Alignment.Center,
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight(0.1f)
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp)
                            .background(Color.Black.copy(alpha = 0.3f)),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                        ) {

                            Box(modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(0.1f)),
                                contentAlignment = Alignment.Center
                            ){
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Close",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Box(modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(0.1f)),
                                contentAlignment = Alignment.Center
                            ){
                                Icon(imageVector = Icons.Default.QuestionMark, contentDescription = "Close",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }


                        }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                            .background(DarkBlack),
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Row(modifier = Modifier
                                .clickable(indication = null, //
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    scope.launch {
                                        scaffoldState.bottomSheetState.partialExpand()
                                        scaffoldState2.bottomSheetState.partialExpand()
                                        isSheetVisible = false;
                                        isSheetVisible2 = false;
                                    }
                                }
                                .fillMaxWidth()
                                .padding(end = 25.dp)
                                .fillMaxHeight(0.2f),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                Column(modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .padding(start = 25.dp, top = 25.dp, bottom = 10.dp),
                                    verticalArrangement = Arrangement.Center

                                ) {
                                    if (!isSheetVisible) {
                                        Text(
                                            modifier = Modifier
                                                .padding(top = 15.dp, start = 15.dp),
                                            text = "${firstBottomSheetOpenState.body.title}",
                                            color = Color.White.copy(alpha = 0.7f),
                                            fontSize = 22.sp
                                        )
                                        Text(
                                            modifier = Modifier
                                                .padding(top = 5.dp, start = 15.dp, bottom = 8.dp),
                                            text = "${firstBottomSheetOpenState.body.subtitle}",
                                            color = Color.White.copy(alpha = 0.3f),
                                            fontSize = 14.sp
                                        )
                                    }
                                    else{
                                        Text(
                                            modifier = Modifier
                                                .padding(top = 15.dp, start = 15.dp),
                                            text = "${firstBottomSheetCloseState.body.key1}",
                                            color = Color.White.copy(alpha = 0.3f),
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            modifier = Modifier
                                                .padding(top = 5.dp, start = 15.dp, bottom = 8.dp),
                                            text = "₹${formatNumber(((money.value/100) * totalBalance).roundToInt())}",
                                            color = Color.White.copy(alpha = 0.1f),

                                            fontSize = 23.sp
                                        )
                                    }
                                }

                                if(isSheetVisible) {
                                    Box(
                                        modifier = Modifier

                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown, // Add a checkmark icon
                                            contentDescription = "down arrow",
                                            tint = Color.White,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }

                            }


                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(start = 35.dp, end = 35.dp),
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
//                                CircularProgressIndicator(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .fillMaxHeight(0.7f)
//                                        .clip(RoundedCornerShape(10))
//                                        .background(Color.White),
//                                    initialValue = 50,
//                                    primaryColor = Color(0xFFCD8B73),
//                                    secondaryColor = Color(0xFFFAE6DC),
//                                    circleRadius = 230f,
//                                    onPositionChange = { position ->
//                                        money.value = position
//                                    }
//                                )
                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.7f)
                                    .clip(RoundedCornerShape(10))
                                    .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressBar(currentProgressToBeReturned = {
                                        money.value = it
                                    },
                                        onDragEnabled = true,
                                        animDelay = 0,
                                        animDuration = 0,

                                        )
                                    Text(text = "${firstBottomSheetOpenState.body.footer}",
                                        color = Color.Black.copy(0.4f),
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .fillMaxWidth(0.7f)
                                            .padding(bottom = 20.dp),
                                        textAlign = TextAlign.Center

                                    )
                                }


                            }

                        }
                    }


                    }






                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.07f)
                        .align(Alignment.BottomCenter)
                        .clickable {
                            scope.launch {
                                scaffoldState.bottomSheetState.expand()
                                isSheetVisible = true;
                            }
                        }
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(Color(0xff37469B)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${items[0].cta_text}", color = Color.White, fontSize = 18.sp)
                }

            BottomSheetScaffold(
                sheetSwipeEnabled = false,
                scaffoldState = scaffoldState,
                sheetPeekHeight =  0.dp,
                sheetContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.71f)
                    ) {
                        Column(modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .padding(start = 15.dp, end = 15.dp),
                            horizontalAlignment = Alignment.Start,

                        ) {


                            if(!isSheetVisible2) {
                                Column(modifier = Modifier
                                    .fillMaxWidth(0.9f)

                                    .padding(start = 15.dp, bottom = 10.dp),
                                    verticalArrangement = Arrangement.Center

                                ) {
                                    Text(
                                        modifier = Modifier
                                            .padding(top = 15.dp),
                                        text = "${secondBottomSheetOpenState.body.title}",
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 22.sp
                                    )
                                    Text(
                                        modifier = Modifier
                                            .padding(top = 5.dp),
                                        text ="${secondBottomSheetOpenState.body.subtitle}",
                                        color = Color.White.copy(alpha = 0.3f),
                                        fontSize = 15.sp
                                    )
                                }
                            }
                            else{
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(indication = null, //
                                        interactionSource = remember { MutableInteractionSource() }) {
                                        scope.launch {
                                            scaffoldState2.bottomSheetState.partialExpand()
                                            isSheetVisible2 = false;
                                        }
                                    }
                                    .padding(start = 15.dp, bottom = 10.dp),
//                                    .background(Color.Green),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            modifier = Modifier
                                                .padding(top = 15.dp, start = 15.dp),
                                            text = "${secondBottomSheetCloseState.body.key1}",
                                            color = Color.White.copy(alpha = 0.3f),
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            modifier = Modifier
                                                .padding(top = 5.dp, start = 15.dp, bottom = 8.dp),
                                            text = "₹${selectedPlan.Price} /mo",
                                            color = Color.White.copy(alpha = 0.1f),

                                            fontSize = 23.sp
                                        )
                                    }
                                    Column {
                                        Text(
                                            modifier = Modifier
                                                .padding(top = 15.dp, start = 15.dp),
                                            text = "${secondBottomSheetCloseState.body.key2}",
                                            color = Color.White.copy(alpha = 0.3f),
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            modifier = Modifier
                                                .padding(top = 5.dp, start = 15.dp, bottom = 8.dp),
                                            text = "${selectedPlan.Months}",
                                            color = Color.White.copy(alpha = 0.1f),
                                            fontSize = 23.sp
                                        )
                                    }

                                    Box(modifier = Modifier
//                                        .background(Color.Cyan)
                                        .padding(end = 15.dp, start = 55.dp)
                                    ){
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown, // Add a checkmark icon
                                            contentDescription = "down arrow",
                                            tint = Color.White,
                                            modifier = Modifier.size(30.dp)

                                        )
                                    }
                                }
                            }

                            EmiCards()
                            Box(modifier = Modifier
//                                .clip(RoundedCornerShape(25))
                                .border(
                                    width = 1.5.dp, // Border disappears when checked
                                    color = Color.White.copy(alpha = 0.8f),
                                    shape = CircleShape
                                )
                            ) {
                                Text(text = "${secondBottomSheetOpenState.body.footer}",
                                    textAlign = TextAlign.Center,
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 15.sp,
                                    modifier = Modifier
                                        .padding(15.dp)
                                        .clickable { }
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.11f)
                                .align(Alignment.BottomCenter)
                                .clickable {
                                    scope.launch {
                                        scaffoldState2.bottomSheetState.expand()
                                        isSheetVisible2 = true;
                                    }
                                }
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                .background(Color(0xff37469B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("${items[1].cta_text}", color = Color.White, fontSize = 18.sp)
                        }

                        BottomSheetScaffold(
                            scaffoldState = scaffoldState2,
                            sheetPeekHeight =  0.dp,
                            sheetTonalElevation = 5.dp,
                            sheetContent = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.82f)
                                ) {
                                    Column(modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth()
                                        .padding(start = 15.dp, end = 15.dp),
                                        horizontalAlignment = Alignment.Start,

                                        ) {

                                        Column(modifier = Modifier
                                            .fillMaxWidth(0.9f)

                                            .padding(start = 15.dp, bottom = 10.dp),
                                            verticalArrangement = Arrangement.Center

                                        ) {
                                            Text(modifier = Modifier
                                                .padding(top = 15.dp)
                                                ,
                                                text = "${thirdBottomSheetOpenState.body.title}", color = Color.White.copy(alpha = 0.7f),fontSize = 22.sp)
                                            Text(modifier = Modifier
                                                .padding(top = 5.dp),
                                                text = "${thirdBottomSheetOpenState.body.subtitle}", color = Color.White.copy(alpha = 0.3f),fontSize = 15.sp)
                                        }

                                        Row(modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(0.2f)
//                                            .padding(start = 5.dp, end = 5.dp, top= 5.dp, bottom = 15.dp)
                                            .padding(5.dp)
//                                            .background(Color.Black)
                                            ,
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceAround
                                        ) {
                                            Row(modifier = Modifier
                                                .fillMaxWidth(0.9f)
                                                .padding(5.dp)) {
                                                Box(modifier = Modifier
                                                    .clip(RoundedCornerShape(15.dp))
                                                    .size(50.dp)
                                                ){
                                                    Image(painter = painterResource(id = R.drawable.hdfc_bank_logo), contentDescription ="" )
                                                }
                                                Column(
//                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = Alignment.Start,
                                                    modifier = Modifier
                                                        .padding(start = 15.dp)
                                                ) {
                                                    Text(text = "HDFC Bank", color = Color.White, fontSize = 18.sp)
                                                    Text(text = "50100117009192", color = Color.White.copy(0.2f), fontSize = 16.sp)
                                                }
                                            }

                                            var isBankSelected = remember {
                                                mutableStateOf(false)
                                            }
                                            Box(modifier = Modifier
                                                .clip(CircleShape)
                                                .clickable { isBankSelected.value = true }
                                            ){
                                                ToggleableCircle(isBankSelected.value, Color(0xff23283C))
                                            }

                                        }



                                        Box(modifier = Modifier
//                                .clip(RoundedCornerShape(25))
                                            .border(
                                                width = 1.5.dp, // Border disappears when checked
                                                color = Color.White.copy(alpha = 0.8f),
                                                shape = CircleShape
                                            )
                                        ) {
                                            Text(text = "${thirdBottomSheetOpenState.body.footer}",
                                                textAlign = TextAlign.Center,
                                                color = Color.White.copy(alpha = 0.8f),
                                                fontSize = 15.sp,
                                                modifier = Modifier
                                                    .padding(15.dp)
                                                    .clickable { }
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(0.175f)
                                            .align(Alignment.BottomCenter)
                                            .clickable {
                                                scope.launch {
                                                    scaffoldState.bottomSheetState.expand()

                                                }
                                            }
                                            .clip(
                                                RoundedCornerShape(
                                                    topStart = 16.dp,
                                                    topEnd = 16.dp
                                                )
                                            )
                                            .background(Color(0xff37469B)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("${items[2].cta_text}", color = Color.White, fontSize = 18.sp)
                                    }

                                }
                            },
                            sheetContainerColor = Color(0xff23283C)
                        ) {

                        }

                    }
                },
                sheetContainerColor = Color(0xff191928)
            ) {

            }

        }


            }
        }


    }
    }
}

//
//@Composable
//fun CircularProgressBar(
//    percentage: Float,
//    number: Int,
//    fontSize: TextUnit = 35.sp,
//    radius: Dp = 100.dp,
//    color: Color = Color.Yellow,
//    strokeWidth: Dp = 15.dp,
//    animDuration: Int = 1000,
//    animDelay: Int = 9
//
//){
//    var animationPlayed by remember{
//        mutableStateOf(false)
//    }
//    val curPercentage = animateFloatAsState(
//        targetValue = if(animationPlayed) percentage else 0f,
//        animationSpec = tween(
//            durationMillis = animDuration,
//            delayMillis = animDelay
//        )
//    )
//    LaunchedEffect(key1 = true) {
//        animationPlayed = true
//    }
//
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier.size(radius *2f)
//    ) {
//        Canvas(modifier = Modifier.size(radius *2f)) {
//            drawArc(
//                color = color,
//                -90f,
//                360 * curPercentage.value,
//                useCenter = false,
//                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
//            )
//        }
//        Text(
//            text = (curPercentage.value * number).toInt().toString(),
//            color = Color.Black,
//            fontSize = fontSize,
//            fontWeight = FontWeight.Bold
//        )
//    }
//
//}

//@Composable
//fun BottomSheet(){
//    BottomSheetScaffold(sheetContent = ) {
//
//    }
//}

data class EmiPlan(
    var isChecked : Boolean,
    val Months : String,
    val Price : String,
    val Color : Color
)

@Composable
fun EmiCards(){
    val EmiPlans = remember{
        mutableListOf(
            EmiPlan(
                isChecked = false,
                Months = "12 months",
                Price = "4,247",
                Color = Color(0xFF41323C)
            ),
            EmiPlan(
                isChecked = false,
                Months = "9 months",
                Price = "5,580",
                Color = Color(0xFF787391)
            ),
            EmiPlan(
                isChecked = false,
                Months = "6 months",
                Price = "8,276",
                Color = Color(0xFF556987)
            ),

        )
    }

    var selectedEmiPlan by remember {
        mutableStateOf(0)
    }
    LazyRow(
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp)
    ) {

        items(EmiPlans.size){
            Box(modifier = Modifier
                .height(225.dp)
                .width(200.dp)
                .padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable() {
                    selectedEmiPlan = it;
                    EmiPlans[it].isChecked = true
                    selectedPlan = EmiPlans[it]
                }
                .background(EmiPlans[it].Color)
                .padding(15.dp)
            ){
                Column(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.Start
                ){
                    if(selectedEmiPlan == it){
                        ToggleableCircle(true, EmiPlans[it].Color)
                    }
                    else{
                        ToggleableCircle(false,EmiPlans[it].Color)
                    }

                    Column(
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "\u20B9", color = Color(0xffeeeeee),fontSize = 17.sp)
                            Text(text = "${EmiPlans[it].Price}", color = Color(0xffeeeeee), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(text = "/mo", color = Color.White.copy(alpha = 0.3f),fontSize = 17.sp)
                        }
                        Text(text = "for ${EmiPlans[it].Months}", color = Color.White.copy(alpha = 0.7f),fontSize = 17.sp)
                    }


                    Text(text = "See calculations", color = Color.White.copy(alpha = 0.3f),fontSize = 17.sp, modifier = Modifier
                        .drawUnderline(Color.White.copy(alpha = 0.5f)))
                    
                    
                }
            }
        }
    }
}

@Composable
fun ToggleableCircle(isChecked: Boolean, checkBoxColor: Color) {
    Box(
        modifier = Modifier
            .size(40.dp) // Size of the circle
            .clip(CircleShape)
            .background(if (isChecked) Color.Black.copy(alpha = 0.1f) else checkBoxColor) // Transparent when checked
            .border(
                width = if (isChecked) 0.dp else 2.dp, // Border disappears when checked
                color = if (isChecked) checkBoxColor else Color.White,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center // Center the checkmark
    ) {
        if (isChecked) {
            Icon(
                imageVector = Icons.Default.Check, // Add a checkmark icon
                contentDescription = "Checked",
                tint = Color.White,
                modifier = Modifier.size(25.dp) // Size of the checkmark
            )
        }
    }
}



fun Modifier.drawUnderline(color: Color): Modifier = this.then(
    Modifier.drawBehind {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 10f), 0f)
        val lineWidth = 2.dp.toPx()
        val startY = size.height - lineWidth / 2

        drawLine(
            color = color,
            start = Offset(0f, startY),
            end = Offset(size.width, startY),
            strokeWidth = lineWidth,
            pathEffect = pathEffect
        )
    }
)
@Composable
fun LoadingScreen(modifier: Modifier) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(text = "Loading...")
    }

}




fun fetchItems() {
    ApiService.RetrofitInstance.api.getItems().enqueue(object : retrofit2.Callback<ApiResponse> {
        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
            if (response.isSuccessful) {
                val items = response.body()?.items
                ItemsList = items ?: emptyList()
                Log.d("max price", "${ ItemsList[0].open_state.body.card.max_range }")
                // Use the items data here
            } else {
                // Handle the error
            }
        }

        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
            Log.e("API Error", "Failed to fetch items: ${t.message}")
        }
    })
}