import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.channels.produce

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            background = Color(0xFFDADADA),
            primary = Color.Black,
            isLight = false
        ),
        typography = Typography(
            defaultFontFamily = FontFamily.Monospace,

            )
    ) {
        val scope = rememberCoroutineScope()
        var poke by remember { mutableStateOf<Pokemon?>(null) }
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold (
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState, modifier = Modifier.padding(16.dp).background(Color.Red))
            }
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
                content = {
                    LaunchedEffect(true) {
                        scope.launch {
                            poke = try {
                                Greeting().greeting()
                            } catch (e: Exception) {
                                null
                            }
                        }
                    }
                    if (poke == null) {
                        CircularProgressIndicator()
                    } else {
                        Text(
                            poke!!.name.replaceFirstChar(Char::uppercaseChar),
                            fontSize = 50.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )
                        Text(poke!!.types[0].type.name)
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(modifier = Modifier
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(Color.White)
                            .size(300.dp),
                            contentAlignment = Alignment.Center,
                            content = {
                                KamelImage(
                                    modifier = Modifier.fillMaxSize(),
                                    contentDescription = "Poke Sprite",
                                    resource = asyncPainterResource(poke!!.sprites.frontDefault),
                                    onLoading = { progress -> CircularProgressIndicator(progress) },
                                    onFailure = { exception ->
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = exception.message.toString(),
                                                actionLabel = "Hide",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    }
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(onClick = {
                            scope.launch {
                                poke = null
                                try {
                                    poke = Greeting().greeting()
                                } catch (error: Exception) {
                                    snackbarHostState.showSnackbar(
                                        message = error.message.toString(),
                                        actionLabel = "Hide",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                               
                            }
                        }) {
                            Text("Randomize")
                        }
                       
                    }
                }
            )
        }
    }
}