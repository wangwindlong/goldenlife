package net.wangyl.life.vector

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

class Outlined {

    companion object {
        val Book: ImageVector
            get() {
                if (_book != null) {
                    return _book!!
                }
                _book = materialIcon(name = "Outlined.Book") {
                    materialPath {
                        moveTo(18.0f, 2.0f)
                        lineTo(6.0f, 2.0f)
                        curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
                        verticalLineToRelative(16.0f)
                        curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
                        horizontalLineToRelative(12.0f)
                        curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
                        lineTo(20.0f, 4.0f)
                        curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
                        close()
                        moveTo(9.0f, 4.0f)
                        horizontalLineToRelative(2.0f)
                        verticalLineToRelative(5.0f)
                        lineToRelative(-1.0f, -0.75f)
                        lineTo(9.0f, 9.0f)
                        lineTo(9.0f, 4.0f)
                        close()
                        moveTo(18.0f, 20.0f)
                        lineTo(6.0f, 20.0f)
                        lineTo(6.0f, 4.0f)
                        horizontalLineToRelative(1.0f)
                        verticalLineToRelative(9.0f)
                        lineToRelative(3.0f, -2.25f)
                        lineTo(13.0f, 13.0f)
                        lineTo(13.0f, 4.0f)
                        horizontalLineToRelative(5.0f)
                        verticalLineToRelative(16.0f)
                        close()
                    }
                }
                return _book!!
            }

        private var _book: ImageVector? = null
    }
}
