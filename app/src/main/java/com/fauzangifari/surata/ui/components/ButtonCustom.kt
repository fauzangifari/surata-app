package com.fauzangifari.surata.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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

@Composable
fun ButtonCustom(
    value: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    fontSize: Int,
    buttonType: ButtonType? = ButtonType.REGULAR,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Blue800),
        shape = if (buttonType == ButtonType.PILL) RoundedCornerShape(50.dp) else RoundedCornerShape(8.dp),
        modifier = modifier
            .height(48.dp)
    ) {
        Row {
            leadingIcon?.invoke()
            if (leadingIcon != null) Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = value,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = PlusJakartaSans
            )

            if (trailingIcon != null) Spacer(modifier = Modifier.size(8.dp))
            trailingIcon?.invoke()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegularButtonPreview() {
    ButtonCustom(
        value = "Regular Button",
        onClick = {},
        fontSize = 16,
        buttonType = ButtonType.REGULAR
    )
}

@Preview(showBackground = true)
@Composable
fun PillButtonPreview() {
    ButtonCustom(
        value = "Pill Button",
        onClick = {},
        fontSize = 16,
        buttonType = ButtonType.PILL
    )
}
