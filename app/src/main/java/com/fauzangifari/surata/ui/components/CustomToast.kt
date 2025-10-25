package com.fauzangifari.surata.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.surata.R
import com.fauzangifari.surata.ui.theme.PlusJakartaSans
import com.fauzangifari.surata.ui.theme.GreenMedium
import com.fauzangifari.surata.ui.theme.RedMedium
import com.fauzangifari.surata.ui.theme.White
import com.fauzangifari.surata.ui.theme.YellowMedium

enum class ToastType { SUCCESS, ERROR, WARNING }

@Composable
fun CustomToast(
    message: String,
    type: ToastType,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    visible: Boolean = true,
) {
    val (bgColor, iconRes) = when (type) {
        ToastType.SUCCESS -> Pair(GreenMedium, R.drawable.ic_check_circle_24)
        ToastType.ERROR -> Pair(RedMedium, R.drawable.ic_error_24)
        ToastType.WARNING -> Pair(YellowMedium, R.drawable.ic_warning_24)
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300)),
        modifier = modifier
            .fillMaxWidth()
            .padding(padding)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    clip = false
                )
                .clip(RoundedCornerShape(12.dp))
                .background(bgColor)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = type.name,
                    tint = White,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = message,
                    color = White,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomToastPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        CustomToast(
            message = "Login berhasil!",
            type = ToastType.SUCCESS,
            visible = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomToastErrorPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        CustomToast(
            message = "Terjadi kesalahan saat login",
            type = ToastType.ERROR,
            visible = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomToastWarningPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        CustomToast(
            message = "Peringatan: Data belum lengkap",
            type = ToastType.WARNING,
            visible = true
        )
    }
}
