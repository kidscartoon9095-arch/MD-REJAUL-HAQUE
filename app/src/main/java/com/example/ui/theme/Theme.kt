package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = SapphirePrimary,
    onPrimary = OnSapphirePrimary,
    primaryContainer = SapphireContainer,
    onPrimaryContainer = OnSapphireContainer,
    secondary = MarigoldSecondary,
    onSecondary = OnMarigoldSecondary,
    secondaryContainer = MarigoldContainer,
    onSecondaryContainer = OnMarigoldContainer,
    tertiary = EmeraldTertiary,
    onTertiary = OnEmeraldTertiary,
    tertiaryContainer = EmeraldContainer,
    onTertiaryContainer = OnEmeraldContainer,
    error = CoralError,
    onError = OnCoralError,
    errorContainer = CoralContainer,
    onErrorContainer = OnCoralContainer,
    background = IvoryBackground,
    onBackground = InkOnBackground,
    surface = PaperSurface,
    onSurface = InkOnBackground,
    surfaceVariant = SoftParchmentVariant,
    onSurfaceVariant = SlateOnSurfaceVariant,
    outline = SubtleOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = SkyBluePrimaryDark,
    onPrimary = OnSkyBlueDark,
    primaryContainer = SapphireContainerDark,
    onPrimaryContainer = OnSapphireContainerDark,
    secondary = AmberSecondaryDark,
    onSecondary = OnAmberDark,
    secondaryContainer = MarigoldContainerDark,
    onSecondaryContainer = OnMarigoldContainerDark,
    tertiary = MintTertiaryDark,
    onTertiary = OnMintDark,
    tertiaryContainer = EmeraldContainerDark,
    onTertiaryContainer = OnEmeraldContainerDark,
    error = RoseErrorDark,
    onError = OnRoseErrorDark,
    errorContainer = CoralContainerDark,
    onErrorContainer = OnCoralContainerDark,
    background = MidnightBackground,
    onBackground = CrispOnBackgroundDark,
    surface = SlateSurfaceDark,
    onSurface = CrispOnBackgroundDark,
    surfaceVariant = SlateVariantDark,
    onSurfaceVariant = MutedTextDark,
    outline = SlateOutlineDark
)

val VidyaQuestShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun VidyaQuestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = VidyaQuestTypography,
        shapes = VidyaQuestShapes,
        content = content
    )
}
