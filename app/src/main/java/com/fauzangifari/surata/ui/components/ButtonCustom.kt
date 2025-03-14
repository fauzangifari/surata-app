package com.fauzangifari.surata.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.surata.ui.theme.Blue800
import com.fauzangifari.surata.ui.theme.PlusJakartaSans

enum class ButtonType {
    REGULAR, PILL
}

enum class ButtonStyle {
    FILLED, OUTLINED
}

@Composable
fun ButtonCustom(
    value: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    fontSize: Int,
    buttonType: ButtonType = ButtonType.REGULAR,
    buttonStyle: ButtonStyle = ButtonStyle.FILLED,
    backgroundColor: Color = Blue800,
    textColor: Color = Color.White,
    borderColor: Color = Blue800,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = if (buttonType == ButtonType.PILL) RoundedCornerShape(50.dp) else RoundedCornerShape(8.dp)

    when (buttonStyle) {
        ButtonStyle.FILLED -> {
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
                shape = shape,
                modifier = modifier.height(40.dp)
            ) {
                ButtonContent(value, leadingIcon, trailingIcon, fontSize, textColor)
            }
        }

        ButtonStyle.OUTLINED -> {
            OutlinedButton(
                onClick = onClick,
                shape = shape,
                border = BorderStroke(2.dp, borderColor),
                modifier = modifier.height(40.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = textColor)
            ) {
                ButtonContent(value, leadingIcon, trailingIcon, fontSize, textColor)
            }
        }
    }
}

@Composable
private fun ButtonContent(
    value: String,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    fontSize: Int,
    textColor: Color
) {
    Row {
        leadingIcon?.invoke()
        if (leadingIcon != null) Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = value,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            fontFamily = PlusJakartaSans
        )

        if (trailingIcon != null) Spacer(modifier = Modifier.size(8.dp))
        trailingIcon?.invoke()
    }
}

@Preview(showBackground = true)
@Composable
fun FilledRegularButtonPreview() {
    ButtonCustom(
        value = "Filled Regular",
        onClick = {},
        fontSize = 16,
        buttonType = ButtonType.REGULAR,
        buttonStyle = ButtonStyle.FILLED
    )
}

@Preview(showBackground = true)
@Composable
fun OutlinedRegularButtonPreview() {
    ButtonCustom(
        value = "Outlined Regular",
        onClick = {},
        fontSize = 16,
        buttonType = ButtonType.REGULAR,
        buttonStyle = ButtonStyle.OUTLINED
    )
}

@Preview(showBackground = true)
@Composable
fun FilledPillButtonPreview() {
    ButtonCustom(
        value = "Filled Pill",
        onClick = {},
        fontSize = 16,
        buttonType = ButtonType.PILL,
        buttonStyle = ButtonStyle.FILLED
    )
}

@Preview(showBackground = true)
@Composable
fun OutlinedPillButtonPreview() {
    ButtonCustom(
        value = "Outlined Pill",
        onClick = {},
        fontSize = 16,
        buttonType = ButtonType.PILL,
        buttonStyle = ButtonStyle.OUTLINED
    )
}
