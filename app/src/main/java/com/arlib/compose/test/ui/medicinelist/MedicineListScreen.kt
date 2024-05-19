package com.arlib.compose.test.ui.medicinelist

import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.arlib.compose.test.MainActivity
import com.arlib.compose.test.R
import com.arlib.compose.test.model.AssociatedDrugItem
import com.arlib.compose.test.ui.medicinelist.viewmodel.MainActivityViewModel
import kotlinx.coroutines.delay
import okhttp3.ResponseBody
import org.json.JSONObject



@Composable
fun MedicineListScreen(
    viewModel: MainActivityViewModel,
    email: String,
    itemClick: () -> Unit,
    onNavUp: () -> Unit
) {
    val context = LocalContext.current

    val onBack = {
        context.startActivity(Intent(context, MainActivity::class.java))
        (context as MainActivity).finish()
    }

    BackPressHandler(onBackPressed = onBack)

    val dataState by viewModel.dataLoadStateFlow.collectAsState(initial = MainActivityViewModel.DataLoadState.Start)
    when (dataState) {
        is MainActivityViewModel.DataLoadState.Failed -> {
            ShowError((dataState as MainActivityViewModel.DataLoadState.Failed).msg)
        }

        MainActivityViewModel.DataLoadState.Loading -> {
            ShowLoader()
        }

        MainActivityViewModel.DataLoadState.Start -> { //do nothing
        }

        is MainActivityViewModel.DataLoadState.Success -> {
            val responseBody =
                (dataState as MainActivityViewModel.DataLoadState.Success).data as ResponseBody

            val d = JSONObject(responseBody.string())
            val drugArray = viewModel.getDrugItmemsFromResponse(d)
            val sortedData =
                remember { drugArray.sortedBy { it.problemName } } //we can move this out of compose itself or put into viewmodel or repo

            ShowList(
                email = email,
                data = sortedData,
                onItemClick = itemClick,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun ShowLoader() {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(60.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowList(
    data: List<AssociatedDrugItem>,
    onItemClick: () -> Unit,
    viewModel: MainActivityViewModel,
    email: String
) {


    //var refreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    if (state.isRefreshing) {
        LaunchedEffect(true) {
            // fetch something
            viewModel.refresh()
            delay(1000)
            state.endRefresh()
        }
    }

    Box(
        //modifier = Modifier.pullRefresh(state)
    ) {
        PullToRefreshContainer(
            state = state,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        Column(
            modifier = Modifier.fillMaxWidth()) {
            Text(
                text = viewModel.getTimeWiseGreeting()+ ", "+ email,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(top = 50.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
            )
            LazyColumn(
                modifier = Modifier.absolutePadding(top = 20.dp),
                contentPadding = PaddingValues(12.dp),//margin at start and end
                verticalArrangement = Arrangement.spacedBy(10.dp), //space between each items in list
            ) {

                itemsIndexed(data) { index, item,  ->
                    ListItemView(position=index, item = item, onItemClick = onItemClick,
                        dataArray = data,viewModel =viewModel
                    )
                }
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
private fun ListItemView(
    item: AssociatedDrugItem,
    onItemClick: () -> Unit,
    position: Int,
    dataArray: List<AssociatedDrugItem>,
    viewModel: MainActivityViewModel

) {

    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        DetailDialog(
            viewModel= viewModel,
            showDialog = showDialog.value,
            onClose = {showDialog.value = false})
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (position == 0 || (dataArray.size>1 && position<dataArray.size && dataArray[position].problemName != dataArray[position-1].problemName)) {
            Text(
                text = item.problemName,
                fontSize = 12.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.absolutePadding(left = 10.dp, right = 10.dp, top = 10.dp, bottom = 10.dp)
            )
        }
        Card(
            elevation = CardDefaults.cardElevation(),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                viewModel.selectedDrug = item
                showDialog.value = true
                //onItemClick()
            }) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.ic_medicine),
                        contentDescription = "",
                        modifier = Modifier
                            .absolutePadding(
                                left = 10.dp,
                                right = 6.dp,
                                top = 12.dp
                            )
                            .height(height = 50.dp)
                            .width(width = 50.dp)
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {

                            Text(
                                text = "Medicine: ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.absolutePadding(right = 10.dp, top = 10.dp)
                            )
                            Text(
                                text = item.name ?: "-",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.absolutePadding(right = 10.dp, top = 10.dp)
                            )
                        }
                        Row {

                            Text(
                                text = "Dose: ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.absolutePadding(right = 10.dp, top = 10.dp)
                            )
                            Text(
                                text = item.dose ?: "-",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.absolutePadding(right = 10.dp, top = 10.dp)
                            )
                        }

                        Row {

                            Text(
                                text = "Strength: ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.absolutePadding(right = 10.dp, top = 10.dp,bottom = 10.dp)
                            )
                            Text(
                                text = item.strength ?: "-",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.absolutePadding(right = 10.dp, top = 10.dp, bottom = 10.dp)
                            )
                        }
                    }

                }

                /*Text(
                text = item.strength ?: "Streng___",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.absolutePadding(
                    left = 10.dp, right = 10.dp, top = 10.dp, bottom = 10.dp
                )
            )*/
            }
        }
    }
}

@Composable

private fun ShowError(msg: String) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = msg,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 16.sp,
            color = Color.Red
        )
    }
}

@Composable
fun DetailDialog(
    showDialog: Boolean,
    onClose: () -> Unit,
    viewModel: MainActivityViewModel
) {
    if (showDialog) {
        Dialog(onDismissRequest =  onClose ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(0.dp),
                color = Color.White
            ) {
                val item = viewModel.selectedDrug!!
                Box(
                    contentAlignment = Alignment.TopStart
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                        ) {

                            Spacer(modifier = Modifier.width(16.dp)) // Add spacing between images

                            Image(
                                painter = painterResource(id = com.google.android.material.R.drawable.ic_clear_black_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(42.dp)
                                    .absolutePadding(
                                        left = 6.dp,
                                        right = 6.dp,
                                        top = 6.dp
                                    )
                                    .clickable {onClose.invoke()}
                            )
                        }

                        Row( modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp))
                            {
                            Spacer(modifier = Modifier.width(16.dp)) // Add spacing between images

                            Image(
                                painter = painterResource(id = R.drawable.ic_medicine),
                                contentDescription = "",
                                modifier = Modifier
                                    .absolutePadding(
                                        left = 10.dp,
                                        right = 6.dp,
                                        top = 12.dp
                                    )
                                    .height(height = 150.dp)
                                    .width(width = 150.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp)) // Add spacing between images


                        }
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                        ) {
                            Text(
                                text = item.problemName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.absolutePadding( right = 10.dp, top = 10.dp, bottom = 10.dp)
                                    .drawBehind {

                                        val strokeWidth = 1 * density
                                        val y = size.height - strokeWidth / 2

                                        drawLine(
                                            Color.LightGray,
                                            Offset(0f, y),
                                            Offset(size.width, y),
                                            strokeWidth
                                        )
                                    }
                            )
                            Row {

                                Text(
                                    text = "Medicine: ",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.absolutePadding(right = 10.dp, top = 10.dp)
                                )
                                Text(
                                    text = item.name ?: "-",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.absolutePadding(right = 10.dp, top = 10.dp)
                                )
                            }
                            Row {

                                Text(
                                    text = "Dose: ",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.absolutePadding(right = 10.dp, top = 10.dp)
                                )
                                Text(
                                    text = item.dose ?: "-",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.absolutePadding(right = 10.dp, top = 10.dp)
                                )
                            }

                            Row {

                                Text(
                                    text = "Strength: ",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.absolutePadding(right = 10.dp, top = 10.dp)
                                )
                                Text(
                                    text = item.strength ?: "-",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.absolutePadding(right = 10.dp, top = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }

}