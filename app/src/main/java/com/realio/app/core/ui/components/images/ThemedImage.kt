import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.realio.app.R

@Composable
fun ThemedImage(
    @DrawableRes darkImage: Int,
    @DrawableRes lightImage: Int,
    modifier: Modifier = Modifier) {
    val isDarkTheme = isSystemInDarkTheme()
    val imageRes = if (isDarkTheme) {
        darkImage
    } else {
        lightImage
    }

    Image(
        painter = painterResource(id = imageRes),
        contentDescription = stringResource(id = R.string.app_name),
        modifier = modifier
    )
}