package com.example.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import krencfs.rencfsmultiplatform.generated.resources.Res
import krencfs.rencfsmultiplatform.generated.resources.application_icon
import krencfs.rencfsmultiplatform.generated.resources.application_name
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun GravitationalLogoAnimation(
    icon: ImageVector = vectorResource(Res.drawable.application_icon),
    contentDescription: String = stringResource(Res.string.application_name),
    modifier: Modifier = Modifier,
    iconSize: Dp = 160.dp,
    atmosphereColor: Color = Color.Cyan.copy(alpha = 0.3f),
    particleCount: Int = 240,
    gravitationalPulse: Boolean = true,
    animationDuration: Int = 4000,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")

    // Core animation values
    val atmosphereRadius = infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "atmosphereRadius",
    ).value

    val gravitationalField = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration * 2, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "gravitationalField",
    ).value

    val pulseIntensity = infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration / 2, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulseIntensity",
    ).value

    val totalSize = iconSize * 2 // Add padding for atmospheric effects

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(totalSize)
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .drawWithContent {
                val center = Offset(size.width / 2f, size.height / 2f)

                val baseRadius = iconSize.toPx() / 2f

                val maxSafeRadius = (size.minDimension / 2f) - 20.dp.toPx()

                // Draw atmospheric layers (behind content) with bounds checking
                drawAtmosphericLayers(
                    center = center,
                    baseRadius = baseRadius,
                    maxSafeRadius = maxSafeRadius,
                    atmosphereRadius = atmosphereRadius,
                    atmosphereColor = atmosphereColor,
                    pulseIntensity = if (gravitationalPulse) pulseIntensity else 1f,
                )

                // Draw the logo content
                drawContent()

                // Draw gravitational field lines with bounds checking
                drawGravitationalField(
                    center = center,
                    baseRadius = baseRadius,
                    maxSafeRadius = maxSafeRadius,
                    fieldRotation = gravitationalField,
                    particleCount = particleCount,
                    fieldColor = atmosphereColor,
                )
            },
    ) {
        Box(
            modifier = Modifier
                .size(iconSize)
                .clip(CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                imageVector = icon,
                contentDescription = contentDescription,
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            )
        }
    }
}

private fun DrawScope.drawAtmosphericLayers(
    center: Offset,
    baseRadius: Float,
    maxSafeRadius: Float,
    atmosphereRadius: Float,
    atmosphereColor: Color,
    pulseIntensity: Float,
) {
    // Calculate layer radii with bounds checking
    val outerRadius = min(baseRadius * atmosphereRadius * 1.4f, maxSafeRadius)
    val middleRadius = min(baseRadius * atmosphereRadius * 1.1f, maxSafeRadius * 0.8f)
    val innerRadius = min(baseRadius * 1.05f, maxSafeRadius * 0.6f)

    // Outer atmosphere - most transparent
    if (outerRadius > 0) {
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to Color.Transparent,
                    0.7f to atmosphereColor.copy(alpha = 0.05f * pulseIntensity),
                    0.85f to atmosphereColor.copy(alpha = 0.15f * pulseIntensity),
                    1.0f to Color.Transparent,
                ),
                center = center,
                radius = outerRadius,
            ),
            radius = outerRadius,
            center = center,
        )
    }

    // Middle atmosphere layer
    if (middleRadius > 0) {
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to Color.Transparent,
                    0.6f to Color.Transparent,
                    0.75f to atmosphereColor.copy(alpha = 0.1f * pulseIntensity),
                    0.9f to atmosphereColor.copy(alpha = 0.25f * pulseIntensity),
                    1.0f to Color.Transparent,
                ),
                center = center,
                radius = middleRadius,
            ),
            radius = middleRadius,
            center = center,
        )
    }

    // Inner atmosphere glow
    if (innerRadius > 0) {
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to Color.Transparent,
                    0.8f to Color.Transparent,
                    0.92f to atmosphereColor.copy(alpha = 0.2f * pulseIntensity),
                    1.0f to atmosphereColor.copy(alpha = 0.4f * pulseIntensity),
                ),
                center = center,
                radius = innerRadius,
            ),
            radius = innerRadius,
            center = center,
        )
    }
}

private fun DrawScope.drawGravitationalField(
    center: Offset,
    baseRadius: Float,
    maxSafeRadius: Float,
    fieldRotation: Float,
    particleCount: Int,
    fieldColor: Color,
) {
    // Draw rotating field particles with bounds checking
    repeat(particleCount) { index ->
        val angle = (fieldRotation + (index * 360f / particleCount)) * (PI / 180f)
        val distance = min(
            baseRadius * (0.9f + 0.4f * sin((fieldRotation * PI / 180f + index).toFloat()).toFloat()),
            maxSafeRadius * 0.7f,
        )

        // Calculate particle position
        val particleX = center.x + cos(angle) * distance
        val particleY = center.y + sin(angle) * distance
        val particleCenter = Offset(particleX.toFloat(), particleY.toFloat())

        // Only draw if particle is within safe bounds
        val distanceFromCenter = sqrt(
            (
                (particleX - center.x) * (particleX - center.x) +
                    (particleY - center.y) * (particleY - center.y)
                ).toDouble(),
        ).toFloat()

        if (distanceFromCenter + 2.dp.toPx() <= maxSafeRadius) {
            // Draw field particle with trail effect
            drawCircle(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.0f to fieldColor.copy(alpha = 0.8f),
                        0.5f to fieldColor.copy(alpha = 0.4f),
                        1.0f to Color.Transparent,
                    ),
                    center = particleCenter,
                    radius = 0.3.dp.toPx(),
                ),
                radius = 1.dp.toPx(),
                center = particleCenter,
            )
        }
    }
}

private fun DrawScope.drawEnergyGlow(
    center: Offset,
    baseRadius: Float,
    maxSafeRadius: Float,
    intensity: Float,
    glowColor: Color,
) {
    val glowRadius = min(baseRadius * 0.9f, maxSafeRadius * 0.5f)

    if (glowRadius > 0) {
        // Subtle energy overlay on the logo
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to Color.Transparent,
                    0.7f to Color.Transparent,
                    0.85f to glowColor.copy(alpha = 0.1f * intensity),
                    1.0f to glowColor.copy(alpha = 0.05f * intensity),
                ),
                center = center,
                radius = glowRadius,
            ),
            radius = glowRadius,
            center = center,
            blendMode = BlendMode.Screen,
        )
    }
}

@Composable
fun ExampleUsage() {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Full gravitational effect with complete circle
        GravitationalLogoAnimation(
            iconSize = 160.dp,
            atmosphereColor = Color(0xFF1E90FF).copy(alpha = 0.3f),
            gravitationalPulse = true,
        )
    }
}
