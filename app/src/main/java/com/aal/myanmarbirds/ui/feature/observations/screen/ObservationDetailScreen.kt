package com.aal.myanmarbirds.ui.feature.observations.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.aal.myanmarbirds.db.entities.ObservationEntity
import com.aal.myanmarbirds.ui.base.EventHandler
import com.aal.myanmarbirds.ui.feature.components.MBTopAppBar
import com.aal.myanmarbirds.ui.feature.observations.viewmodel.ObservationDetailScreenEvent
import com.aal.myanmarbirds.ui.feature.observations.viewmodel.ObservationDetailViewModel
import com.aal.myanmarbirds.util.toReadableDate
import com.google.gson.Gson
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ObservationDetailScreen(
    observationJsonString: String,
    observationDetailViewModel: ObservationDetailViewModel = hiltViewModel(),
    onEvent: (ObservationDetailScreenEvent) -> Unit
) {
    val state by observationDetailViewModel.uiState.collectAsState()

    EventHandler(observationDetailViewModel) { event ->
        onEvent(event)
    }
    val observation: ObservationEntity = remember {
        Gson().fromJson(observationJsonString, ObservationEntity::class.java)
    }

    Scaffold(
        topBar = {
            MBTopAppBar(
                text = "Observation Details",
                onLightbulbClick = {
                    onEvent(ObservationDetailScreenEvent.BackPressed)
                },
                isHomeScreen = false
            )
        }
    ) { padding ->
        ObservationScreenContent(
            innerPadding = padding,
            observation = observation
        )

    }
}

@Composable
fun ObservationScreenContent(
    innerPadding: PaddingValues,
    observation: ObservationEntity
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        ObservationImage(observation.imagePath)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Bird Name: ${observation.birdName}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        ObservationLabel("Location", observation.locName)

        Spacer(modifier = Modifier.height(8.dp))

        ObservationLabel("Date", observation.date.toReadableDate())

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Coordinates:",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Latitude: ${observation.latitude}",
            color = Color(0xFF2962FF)
        )

        Text(
            text = "Longitude: ${observation.longitude}",
            color = Color(0xFF2962FF)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Notes:",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = observation.note,
            fontSize = 15.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun ObservationImage(imagePath: String?) {

    val painter = rememberAsyncImagePainter(imagePath)

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {

        Image(
            painter = painter,
            contentDescription = "Bird Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ObservationLabel(
    title: String,
    value: String
) {
    Row {
        Text(
            text = "$title: ",
            fontWeight = FontWeight.Medium
        )

        Text(
            text = value,
            color = Color.Gray
        )
    }
}

@Preview
@Composable
fun ObservationDetailPreview() {
    val localDate = LocalDate.parse("23 Mar 2026", DateTimeFormatter.ofPattern("dd MMM yyyy"))
    val dateInMillis: Long =
        localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    ObservationScreenContent(
        innerPadding = PaddingValues(),
        observation = ObservationEntity(
            id = 1,
            birdName = "ASDF",
            note = "Test",
            date = dateInMillis,
            latitude = 111.87,
            longitude = 123.85,
            imagePath = "asdfs",
            bodyColor = "Blue",
            locName = "adsfasdf"
        )
    )
}