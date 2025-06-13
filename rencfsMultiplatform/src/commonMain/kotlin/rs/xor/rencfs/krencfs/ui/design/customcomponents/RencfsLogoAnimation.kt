package rs.xor.rencfs.krencfs.ui.design.customcomponents

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import krencfs.rencfsmultiplatform.generated.resources.Res
import krencfs.rencfsmultiplatform.generated.resources.application_icon
import krencfs.rencfsmultiplatform.generated.resources.application_name
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun RencfsLogoAnimation(
    icon: ImageVector = vectorResource(Res.drawable.application_icon),
    contentDescription: String = stringResource(Res.string.application_name),
    modifier: Modifier = Modifier,
    iconSize: Dp = 180.dp,
    shimmerColor: Color = Color.White.copy(alpha = 0.12f),
    shimmerWidthFraction: Float = 0.1f,
    shimmerDuration: Int = 3200,
) {
    val shimmerX by rememberBouncingShimmerX(shimmerDuration)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(iconSize),
    ) {
        // 🌒 Outer shadow behind icon using drawBehind for Compose Multiplatform
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val radius = size.minDimension / 2f
                    val center = center

                    drawCircle(
                        brush = Brush.radialGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.68f to Color.Transparent,
                                0.82f to Color.Black.copy(alpha = 0.15f),
                                0.95f to Color.Black.copy(alpha = 0.3f),
                                1.0f to Color.Transparent,
                            ),
                            center = center,
                            radius = radius,
                        ),
                        radius = radius,
                        center = center,
                    )
                },
        )

        // 💫 Foreground shimmer clipped to a circular mask
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .drawWithContent {
                    drawContent()

                    val shimmerStartX = size.width * shimmerX
                    val shimmerEndX = shimmerStartX + size.width * shimmerWidthFraction

                    val shimmerGradient = Brush.linearGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent,
                            0.35f to shimmerColor.copy(alpha = 0.05f),
                            0.5f to shimmerColor,
                            0.65f to shimmerColor.copy(alpha = 0.05f),
                            1.0f to Color.Transparent,
                        ),
                        start = Offset(shimmerStartX - size.width, 0f),
                        end = Offset(shimmerEndX + size.width, size.height),
                    )

                    drawRect(
                        brush = shimmerGradient,
                        size = size,
                        blendMode = BlendMode.Lighten,
                    )
                },
        ) {
            Image(
                imageVector = icon,
                contentDescription = contentDescription,
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp), // avoids cropping inside circular clip
            )
        }
    }
}

@Composable
fun rememberBouncingShimmerX(
    durationMillis: Int = 2500,
): State<Float> {
    val shimmerX = remember { Animatable(-1f) }

    LaunchedEffect(Unit) {
        var direction = 1
        while (true) {
            shimmerX.animateTo(
                targetValue = if (direction == 1) 2f else -1f,
                animationSpec = tween(durationMillis, easing = LinearEasing),
            )
            direction *= -1
        }
    }

    return shimmerX.asState()
}
