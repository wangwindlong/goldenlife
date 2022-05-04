package net.wangyl.life.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.wangyl.life.R

private val DefaultShape = Shapes(
  small = RoundedCornerShape(4.dp),
  medium = RoundedCornerShape(4.dp),
  large = RoundedCornerShape(8.dp)
)

private val LightColorPalette = MyComposeColors(
  statusBarBg = white1.convert(ColorSpaces.Acescg),
  bottomBar = white1,
  background = white2,
  listItem = white,
  divider = white3,
  chatPage = white2,
  textPrimary = black3,
  textPrimaryMe = black3,
  textSecondary = grey1,
  onBackground = grey3,
  icon = black,
  iconCurrent = green3,
  badge = red1,
  onBadge = white,
  bubbleMe = green1,
  bubbleOthers = white,
  textFieldBackground = white,
  more = grey4,
  chatPageBgAlpha = 0f,
)
private val DarkColorPalette = MyComposeColors(
  statusBarBg = black1.convert(ColorSpaces.Acescg),
  bottomBar = black1,
  background = black2,
  listItem = black3,
  divider = black4,
  chatPage = black2,
  textPrimary = white4,
  textPrimaryMe = black6,
  textSecondary = grey1,
  onBackground = grey1,
  icon = white5,
  iconCurrent = green3,
  badge = red1,
  onBadge = white,
  bubbleMe = green2,
  bubbleOthers = black5,
  textFieldBackground = black7,
  more = grey5,
  chatPageBgAlpha = 0f,
)
private val NewYearColorPalette = MyComposeColors(
  statusBarBg = red4.convert(ColorSpaces.Acescg),
  bottomBar = red4,
  background = red5,
  listItem = red2,
  divider = red3,
  chatPage = red5,
  textPrimary = white,
  textPrimaryMe = black6,
  textSecondary = grey2,
  onBackground = grey2,
  icon = white5,
  iconCurrent = green3,
  badge = yellow1,
  onBadge = black3,
  bubbleMe = green2,
  bubbleOthers = red6,
  textFieldBackground = red7,
  more = red8,
  chatPageBgAlpha = 1f,
)

private val LocalMyComposeColors = compositionLocalOf {
  LightColorPalette
}

private val LocalShapes = compositionLocalOf {
  DefaultShape
}

private val LocalTypography = compositionLocalOf {
  DefaultTypoGraphy
}

object MyComposeTheme {
  val colors: MyComposeColors
    @Composable
    get() = LocalMyComposeColors.current
  val shapes: Shapes
    @Composable
    get()  = LocalShapes.current

  val typography: Typography
    @Composable
    @ReadOnlyComposable
    get() = LocalTypography.current

  enum class Theme(val state: Int) {
    Light(0), Dark(1), Custom(-1);

    fun isDark() = state == 1

      companion object {
      fun fromInt(value: Int) = values().first { it.state == value }
    }
  }
}

@Stable
class MyComposeColors(
  statusBarBg: Color,
  bottomBar: Color,
  background: Color,
  listItem: Color,
  divider: Color,
  chatPage: Color,
  textPrimary: Color,
  textPrimaryMe: Color,
  textSecondary: Color,
  onBackground: Color,
  icon: Color,
  iconCurrent: Color,
  badge: Color,
  onBadge: Color,
  bubbleMe: Color,
  bubbleOthers: Color,
  textFieldBackground: Color,
  more: Color,
  chatPageBgAlpha: Float,
) {
  var statusBarBg: Color by mutableStateOf(statusBarBg)
    private set
  var bottomBar: Color by mutableStateOf(bottomBar)
    private set
  var background: Color by mutableStateOf(background)
    private set
  var listItem: Color by mutableStateOf(listItem)
    private set
  var chatListDivider: Color by mutableStateOf(divider)
    private set
  var chatPage: Color by mutableStateOf(chatPage)
    private set
  var textPrimary: Color by mutableStateOf(textPrimary)
    private set
  var textPrimaryMe: Color by mutableStateOf(textPrimaryMe)
    private set
  var textSecondary: Color by mutableStateOf(textSecondary)
    private set
  var onBackground: Color by mutableStateOf(onBackground)
    private set
  var icon: Color by mutableStateOf(icon)
    private set
  var iconSelected: Color by mutableStateOf(iconCurrent)
    private set
  var badge: Color by mutableStateOf(badge)
    private set
  var onBadge: Color by mutableStateOf(onBadge)
    private set
  var bubbleMe: Color by mutableStateOf(bubbleMe)
    private set
  var bubbleOthers: Color by mutableStateOf(bubbleOthers)
    private set
  var textFieldBackground: Color by mutableStateOf(textFieldBackground)
    private set
  var more: Color by mutableStateOf(more)
    private set
  var chatPageBgAlpha: Float by mutableStateOf(chatPageBgAlpha)
    private set
}

@Composable
fun MyComposeTheme(theme: MyComposeTheme.Theme = MyComposeTheme.Theme.Light, content: @Composable () -> Unit) {
  val targetColors = when (theme) {
    MyComposeTheme.Theme.Light -> LightColorPalette
    MyComposeTheme.Theme.Dark -> DarkColorPalette
    MyComposeTheme.Theme.Custom -> NewYearColorPalette
  }

  val statusBarBg = animateColorAsState(targetColors.statusBarBg, TweenSpec(600))
  val bottomBar = animateColorAsState(targetColors.bottomBar, TweenSpec(600))
  val background = animateColorAsState(targetColors.background, TweenSpec(600))
  val listItem = animateColorAsState(targetColors.listItem, TweenSpec(600))
  val chatListDivider = animateColorAsState(targetColors.chatListDivider, TweenSpec(600))
  val chatPage = animateColorAsState(targetColors.chatPage, TweenSpec(600))
  val textPrimary = animateColorAsState(targetColors.textPrimary, TweenSpec(600))
  val textPrimaryMe = animateColorAsState(targetColors.textPrimaryMe, TweenSpec(600))
  val textSecondary = animateColorAsState(targetColors.textSecondary, TweenSpec(600))
  val onBackground = animateColorAsState(targetColors.onBackground, TweenSpec(600))
  val icon = animateColorAsState(targetColors.icon, TweenSpec(600))
  val iconCurrent = animateColorAsState(targetColors.iconSelected, TweenSpec(600))
  val badge = animateColorAsState(targetColors.badge, TweenSpec(600))
  val onBadge = animateColorAsState(targetColors.onBadge, TweenSpec(600))
  val bubbleMe = animateColorAsState(targetColors.bubbleMe, TweenSpec(600))
  val bubbleOthers = animateColorAsState(targetColors.bubbleOthers, TweenSpec(600))
  val textFieldBackground = animateColorAsState(targetColors.textFieldBackground, TweenSpec(600))
  val more = animateColorAsState(targetColors.more, TweenSpec(600))
  val chatPageBgAlpha = animateFloatAsState(targetColors.chatPageBgAlpha, TweenSpec(600))

  val colors = MyComposeColors(
    statusBarBg = statusBarBg.value,
    bottomBar = bottomBar.value,
    background = background.value,
    listItem = listItem.value,
    divider = chatListDivider.value,
    chatPage = chatPage.value,
    textPrimary = textPrimary.value,
    textPrimaryMe = textPrimaryMe.value,
    textSecondary = textSecondary.value,
    onBackground = onBackground.value,
    icon = icon.value,
    iconCurrent = iconCurrent.value,
    badge = badge.value,
    onBadge = onBadge.value,
    bubbleMe = bubbleMe.value,
    bubbleOthers = bubbleOthers.value,
    textFieldBackground = textFieldBackground.value,
    more = more.value,
    chatPageBgAlpha = chatPageBgAlpha.value,
  )

  CompositionLocalProvider(LocalMyComposeColors provides colors) {
    MaterialTheme(
      shapes = MyComposeTheme.shapes,
      typography = MyComposeTheme.typography,
      content = content
    )
  }
}


private val Domine = FontFamily(
  Font(R.font.iconfont),
  Font(R.font.iconfont, FontWeight.Bold)
)

val DefaultTypoGraphy = Typography(
//  defaultFontFamily = Montserrat,
  h4 = TextStyle(
    fontWeight = FontWeight.SemiBold,
    fontSize = 30.sp,
    letterSpacing = 0.sp
  ),
  h5 = TextStyle(
    fontWeight = FontWeight.SemiBold,
    fontSize = 24.sp,
    letterSpacing = 0.sp
  ),
  h6 = TextStyle(
    fontWeight = FontWeight.SemiBold,
    fontSize = 20.sp,
    letterSpacing = 0.sp
  ),
  subtitle1 = TextStyle(
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    letterSpacing = 0.15.sp
  ),
  subtitle2 = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    letterSpacing = 0.1.sp
  ),
  body1 = TextStyle(
    fontFamily = Domine,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    letterSpacing = 0.5.sp
  ),
  body2 = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    letterSpacing = 0.25.sp
  ),
  button = TextStyle(
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    letterSpacing = 1.25.sp
  ),
  caption = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    letterSpacing = 0.4.sp
  ),
  overline = TextStyle(
    fontWeight = FontWeight.SemiBold,
    fontSize = 12.sp,
    letterSpacing = 1.sp
  )
)

