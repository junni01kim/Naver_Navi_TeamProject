package com.hansung.sherpa.ui.preference

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hansung.sherpa.R
import com.hansung.sherpa.ui.preference.carousel.MultiBrowseCarousel
import com.hansung.sherpa.ui.preference.carousel.rememberCarouselState

data class CarouselItem(
    val id: Int,
    @DrawableRes val imageResId: Int,
    @StringRes val contentDescriptionResId: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSyncScreen() {
    val openAlertDialog = remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var items by remember { mutableStateOf(
        listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5"
            , "Item 6", "Item 7", "Item 8", "Item 9", "Item 10"
        )) }

    val itemss =
        listOf(
            CarouselItem(0, R.drawable._1, R.string.float_icon_1),
            CarouselItem(1, R.drawable._2, R.string.float_icon_2),
            CarouselItem(2, R.drawable._3, R.string.float_icon_3),
            CarouselItem(3, R.drawable._4, R.string.float_icon_4),
            CarouselItem(4, R.drawable._5, R.string.float_icon_5),
            CarouselItem(5, R.drawable._6, R.string.float_icon_5),
            CarouselItem(6, R.drawable._7, R.string.float_icon_5),
            CarouselItem(7, R.drawable._8, R.string.float_icon_5),
        )

    var currentIndex by remember { mutableStateOf(0) }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        MaterialTheme(
            colorScheme = lightColorScheme(
                primary = Color(0xFF6200EE),
                onBackground = Color.Black,
                onSurfaceVariant = Color.Gray
            )
        ) {
            Surface(shape = RoundedCornerShape(20.dp)) {
                SearchBar(
                    onSearch = { query -> println("Search query: $query") }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Carousel
        // Carousel
        /*LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(items) { index, item ->
                var itemOffset by remember { mutableStateOf(0f) }
                val density = LocalDensity.current
                val itemHeight = with(density) { itemOffset.toDp() }

                // 크기를 애니메이션으로 변경
                val size by animateDpAsState(
                    targetValue = when (index) {
                        0 -> 250.dp  // Large
                        1 -> 200.dp  // Medium
                        2, 3 -> 100.dp  // Small
                        else -> 0.dp
                    },
                    animationSpec = tween(durationMillis = 300), label = ""
                )

                Box(
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            itemOffset = coordinates.positionInParent().y
                        }
                        .height(size)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    CarouselItem(size, item)
                }
            }
        }*/

        MultiBrowseCarousel(
            state = rememberCarouselState { itemss.count() },
            modifier = Modifier.width(412.dp).fillMaxHeight(),
            preferredItemWidth = 186.dp,
            itemSpacing = 8.dp,
            contentPadding = PaddingValues(vertical = 16.dp),
            orientation = Orientation.Vertical
        ) { i ->
            val item = itemss[i]
            Image(
                modifier = Modifier.height(205.dp).maskClip(MaterialTheme.shapes.extraLarge),
                painter = painterResource(id = item.imageResId),
                contentDescription = stringResource(item.contentDescriptionResId),
                contentScale = ContentScale.Crop
            )
        }
    }
}


@Composable
fun CarouselItem(height: androidx.compose.ui.unit.Dp, item: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Preview
@Composable
fun PreviewAccountSyncScreen() {
    AccountSyncScreen()
}

@SuppressLint("RememberReturnType")
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    placeholderText: String = "Search...",
    onSearch: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // Create an interaction source for handling hover state
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    // Animate background color based on hover state
    val backgroundColor by animateColorAsState(
        targetValue = if (isHovered) Color(0xFFEFEFEF) else MaterialTheme.colorScheme.background
    )

    TextField(
        value = searchQuery,
        onValueChange = { newValue ->
            searchQuery = newValue
            onSearch(newValue.text)
        },
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(8.dp),
        interactionSource = interactionSource,
        placeholder = {
            Text(text = placeholderText)
        },
        leadingIcon = {
            AnimatedVisibility(visible = searchQuery.text.isEmpty(),
                enter = slideInHorizontally(animationSpec = tween(durationMillis = 200)) { fullWidth ->
                    -fullWidth / 3
                } + fadeIn(
                    animationSpec = tween(durationMillis = 200)
                ),
                exit = slideOutHorizontally(animationSpec = tween(durationMillis = 200)) { fullWidth ->
                    -fullWidth / 3
                } + fadeOut(
                    animationSpec = tween(durationMillis = 200)
                )) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            }
        },
        trailingIcon = {
            AnimatedVisibility(visible = searchQuery.text.isNotEmpty()) {
                IconButton(onClick = { searchQuery = TextFieldValue("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Icon"
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        singleLine = true
    )
}

@Preview
@Composable
fun SearchBarPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF6200EE),
            onBackground = Color.Black,
            onSurfaceVariant = Color.Gray
        )
    ) {
        Surface {
            SearchBar(
                onSearch = { query -> println("Search query: $query") }
            )
        }
    }
}