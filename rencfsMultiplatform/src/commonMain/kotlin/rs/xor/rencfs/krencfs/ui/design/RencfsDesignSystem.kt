package rs.xor.rencfs.krencfs.ui.design

import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import krencfs.rencfsmultiplatform.generated.resources.*
import org.jetbrains.compose.resources.Font
import kotlin.math.cos
import kotlin.math.sin

object DesignSystem {

    object Colors {

        object Palette {
            val Primary_Black_900 = Color(0xFF201C1A)
            val Primary_Black_800 = Color(0xFF363231)
            val Primary_Black_700 = Color(0xFF4F4A48)
            val Primary_Black_600 = Color(0xFF686362)
            val Primary_Black_500 = Color(0xFF868281)
            val Primary_Black_400 = Color(0xFF9D9999)
            val Primary_Black_300 = Color(0xFFB4B1B1)
            val Primary_Black_200 = Color(0xFFD2D1D0)
            val Primary_Black_100 = Color(0xFFF0F0F0)

            val Primary_Orange_900 = Color(0xFF702E24)
            val Primary_Orange_800 = Color(0xFF8A3529)
            val Primary_Orange_700 = Color(0xFFA33D2D)
            val Primary_Orange_600 = Color(0xFFBD4533)
            val Primary_Orange_500 = Color(0xFFCA6A5C)
            val Primary_Orange_400 = Color(0xFFD4867A)
            val Primary_Orange_300 = Color(0xFFDEA299)
            val Primary_Orange_200 = Color(0xFFECC8C2)
            val Primary_Orange_100 = Color(0xFFF2DAD6)

            val Secondary_Green_900 = Color(0xFF3D5652)
            val Secondary_Green_800 = Color(0xFF4F726D)
            val Secondary_Green_700 = Color(0xFF5C8680)
            val Secondary_Green_600 = Color(0xFF6A9B94)
            val Secondary_Green_500 = Color(0xFF88AFA9)
            val Secondary_Green_400 = Color(0xFF9EBEB9)
            val Secondary_Green_300 = Color(0xFFB5CDCA)
            val Secondary_Green_200 = Color(0xFFD3E1DF)
            val Secondary_Green_100 = Color(0xFFF1F5F5)

            val Secondary_Brown_900 = Color(0xFF483932)
            val Secondary_Brown_800 = Color(0xFF5E493F)
            val Secondary_Brown_700 = Color(0xFF6E5549)
            val Secondary_Brown_600 = Color(0xFF7F6153)
            val Secondary_Brown_500 = Color(0xFF998175)
            val Secondary_Brown_400 = Color(0xFFAB988F)
            val Secondary_Brown_300 = Color(0xFFBFB0A9)
            val Secondary_Brown_200 = Color(0xFFD9D0CC)
            val Secondary_Brown_100 = Color(0xFFF3F0EE)
        }

        val Primary_Black = Palette.Primary_Black_900
        val On_Primary_Black = Palette.Primary_Black_100
        val Primary_Black_Contour = Palette.Primary_Black_800

        val Primary_Orange = Palette.Primary_Orange_600

        val Secondary_Green = Palette.Secondary_Green_600

        val Secondary_Brown = Palette.Secondary_Brown_600

        val Notification_Info = Color (0xFF467599)
        val Notification_Success = Color(0xFF21A872)
        val Notification_Warning = Color(0xFFE9D018)
        val Notification_Error = Color(0xFFD2331B)

        object Gradient {
            val Oxidized_Iron = listOf(
                    Color(0xFF914227),
                    Color(0xFF8F3000),
                    Color(0xFFB54737),
                    Color(0xFFEB986C),
                    Color(0xFFE08B7A),
                )

            val Oxidized_Copper = listOf(
                    Color(0xFF3D5652),
                    Color(0xFF4F726D),
                    Color(0xFF6A9B94),
                    Color(0xFF9EBEB9),
                    Color(0xFF5C8680),
                )
        }
    }

    object Typography {
        @Composable
        fun JetBrainsMono() = FontFamily(
            Font(
                Res.font.jetbrainsmono_regular,
                FontWeight.Normal,
                FontStyle.Normal
            ),
            Font(
                Res.font.jetbrainsmono_italic,
                FontWeight.Normal,
                FontStyle.Italic
            ),

            Font(
                Res.font.jetbrainsmono_bold,
                FontWeight.Bold,
                FontStyle.Normal
            ),
            Font(
                Res.font.jetbrainsmono_bold_italic,
                FontWeight.Bold,
                FontStyle.Italic
            ),

            Font(
                Res.font.jetbrainsmono_extrabold,
                FontWeight.ExtraBold,
                FontStyle.Normal
            ),
            Font(
                Res.font.jetbrainsmono_extrabold_italic,
                FontWeight.ExtraBold,
                FontStyle.Italic
            ),

            Font(
                Res.font.jetbrainsmono_medium,
                FontWeight.Medium,
                FontStyle.Normal
            ),
            Font(
                Res.font.jetbrainsmono_medium_italic,
                FontWeight.Medium,
                FontStyle.Italic
            )
        )
    }
}

//calculate offset based on angle and size
fun gradientOffset(angleInDegrees: Double, width: Float, height: Float): Offset {
    val angleInRadians = Math.toRadians(angleInDegrees)
    val x = cos(angleInRadians).toFloat() * width
    val y = sin(angleInRadians).toFloat() * height
    return Offset(x, y)
}

@Composable
fun Modifier.backgroundAngularGradient(
    colors: List<Color>,
    startAngleInDegrees: Double = 101.14,
    endAngleInDegrees: Double = -51.23
): Modifier {
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    return onSizeChanged { size ->
            containerSize = size // Capture the container size
        }
        .background(
            brush = Brush.linearGradient(
                colors = colors,
                end = gradientOffset(startAngleInDegrees, containerSize.width.toFloat(), containerSize.height.toFloat()),
                start = gradientOffset(endAngleInDegrees, containerSize.width.toFloat(), containerSize.height.toFloat())
            )
        )
}
