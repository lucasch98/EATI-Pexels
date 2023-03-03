package com.eati.pexels.presentation


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eati.pexels.domain.Photo


@Composable
fun PhotosScreen(viewModel: PhotosViewModel) {
    val result by viewModel.photosFlow.collectAsState()

    Photos(result, viewModel::updateResults)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Photos(results: List<Photo>, updateResults: (String) -> Unit) {

    val focusManager = LocalFocusManager.current
    var search by remember{ mutableStateOf("Architecture") }

    val onActionSearch = {
        focusManager.clearFocus()
        updateResults(search)
    }


    LaunchedEffect(Unit) {
        updateResults("Architecture")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                title = { Text("EATI Photos") }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = search,
                        onValueChange = { newSeach -> search = newSeach},
                        label = { Text(text = "Search for photo") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {onActionSearch()}
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onKeyEvent {
                                if (it.key == Key.Enter) {
                                    onActionSearch()
                                    true
                                }
                                false
                            }
                    )
                }
            }
            items(results) { Photo ->
                PhotoCard(Photo.originalPhoto, Photo.photographer, Photo.postUrl)
            }
        }
    }
}

@Composable
fun PhotoCard(
    url: String,
    photographer: String,
    postUrl: String
){
    val uriHandler = LocalUriHandler.current
    var isExpanded by remember {mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp / 2
    val screenWidth = configuration.screenWidthDp.dp

    var imgHeight: Dp
    var imgWidth: Dp
    if (!isExpanded) {
        imgHeight = 120.dp
        imgWidth = 120.dp
    }
    else{
        imgHeight = screenHeight
        imgWidth = screenWidth
    }

    Card (
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface
    ){
        Row(
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = url,
                contentDescription = photographer,
                modifier = Modifier
                    .size(height = imgHeight, width = imgWidth)
                    .padding(8.dp)
                    .clickable {
                        isExpanded = !isExpanded
                    },
                contentScale = ContentScale.Crop,
            )
            Column(
                verticalArrangement = Arrangement.Top
            ) {
                if (!isExpanded) {
                    Text(
                        text = photographer,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    ClickableText(
                        text = AnnotatedString(postUrl),
                        style = TextStyle(
                            color = MaterialTheme.colors.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                        onClick =  {
                            uriHandler.openUri(postUrl)
                        }
                    )
                }

            }
        }
    }
}