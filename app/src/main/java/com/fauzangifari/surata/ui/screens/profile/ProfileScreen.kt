package com.fauzangifari.surata.ui.screens.profile

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fauzangifari.surata.ui.components.*
import com.fauzangifari.surata.ui.theme.Blue900
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class UserProfile(
    val name: String = "",
    val schoolEmail: String = "",
    val personalEmail: String = "",
    val placeOfBirth: String = "",
    val dateOfBirth: String = "",
    val phone: String = "",
    val idNumber: String = "",
    val photoUrl: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userProfileFlow: Flow<UserProfile> = flowOf(UserProfile()),
    onNavigateBack: () -> Unit = {},
    onUpdateProfile: (UserProfile) -> Unit = {}
) {
    val profile by userProfileFlow.collectAsState(initial = UserProfile())
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    // State untuk edit mode
    var isEditMode by remember { mutableStateOf(false) }
    var editedPhone by remember { mutableStateOf("") }
    var editedPersonalEmail by remember { mutableStateOf("") }
    var showSaveDialog by remember { mutableStateOf(false) }
    var showChangePhotoDialog by remember { mutableStateOf(false) }

    // Toast state
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.SUCCESS) }
    var showToast by remember { mutableStateOf(false) }

    // Update edited values when profile changes
    LaunchedEffect(profile) {
        editedPhone = profile.phone
        editedPersonalEmail = profile.personalEmail
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header dengan foto profil
            ProfileHeader(
                profile = profile,
                isEditMode = isEditMode,
                onChangePhotoClick = { showChangePhotoDialog = true }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Card dengan informasi detail
            ProfileInfoCard(
                profile = profile,
                isEditMode = isEditMode,
                editedPhone = editedPhone,
                editedPersonalEmail = editedPersonalEmail,
                onPhoneChange = { editedPhone = it },
                onPersonalEmailChange = { editedPersonalEmail = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            if (isEditMode) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ButtonCustom(
                        value = "Batal",
                        fontSize = 14,
                        textColor = Blue900,
                        buttonStyle = ButtonStyle.OUTLINED,
                        onClick = {
                            isEditMode = false
                            editedPhone = profile.phone
                            editedPersonalEmail = profile.personalEmail
                        },
                        modifier = Modifier.weight(1f)
                    )
                    ButtonCustom(
                        value = "Simpan",
                        fontSize = 14,
                        onClick = { showSaveDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                ButtonCustom(
                    value = "Edit Profil",
                    fontSize = 14,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    onClick = { isEditMode = true },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Toast at top
        if (showToast) {
            CustomToast(
                message = toastMessage,
                type = toastType,
                visible = showToast,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }

    if (showSaveDialog) {
        CustomDialog(
            title = "Simpan Perubahan?",
            message = "Apakah Anda yakin ingin menyimpan perubahan profil?",
            onConfirm = {
                val updatedProfile = profile.copy(
                    phone = editedPhone,
                    personalEmail = editedPersonalEmail
                )
                onUpdateProfile(updatedProfile)
                isEditMode = false
                showSaveDialog = false

                // Show success toast
                toastMessage = "Profil berhasil diperbarui"
                toastType = ToastType.SUCCESS
                showToast = true
                scope.launch {
                    delay(3000)
                    showToast = false
                }
            },
            onDismiss = { showSaveDialog = false }
        ) {
            // Empty content
        }
    }

    if (showChangePhotoDialog) {
        CustomDialog(
            title = "Ubah Foto Profil",
            message = "Fitur ubah foto profil akan segera tersedia.",
            onConfirm = { showChangePhotoDialog = false },
            onDismiss = { showChangePhotoDialog = false }
        ) {
            // Empty content
        }
    }
}

@Composable
private fun ProfileHeader(
    profile: UserProfile,
    isEditMode: Boolean,
    onChangePhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                ProfileAvatar(
                    name = profile.name,
                    photoUrl = profile.photoUrl,
                    modifier = Modifier.size(100.dp)
                )

                if (isEditMode) {
                    FloatingActionButton(
                        onClick = onChangePhotoClick,
                        modifier = Modifier.size(36.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Ubah foto",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nama
            Text(
                text = profile.name.ifBlank { "Nama Pengguna" },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ID Number sebagai subtitle
            Text(
                text = profile.idNumber.ifBlank { "-" },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProfileAvatar(
    name: String,
    photoUrl: String?,
    modifier: Modifier = Modifier
) {
    // Avatar dengan inisial sebagai fallback
    val initials = name
        .split("\\s+".toRegex())
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "U" }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        // TODO: Implement image loading jika ada photoUrl
        // Untuk saat ini gunakan inisial
        Text(
            text = initials,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun ProfileInfoCard(
    profile: UserProfile,
    isEditMode: Boolean,
    editedPhone: String,
    editedPersonalEmail: String,
    onPhoneChange: (String) -> Unit,
    onPersonalEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Informasi Pribadi",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Nama (Read-only)
            ProfileInfoItem(
                icon = Icons.Default.Person,
                label = "Nama Lengkap",
                value = profile.name.ifBlank { "-" },
                isEditable = false
            )

            ProfileDivider()

            // Email Sekolah (Read-only)
            ProfileInfoItem(
                icon = Icons.Default.Email,
                label = "Email Sekolah",
                value = profile.schoolEmail.ifBlank { "-" },
                isEditable = false
            )

            ProfileDivider()

            // Email Pribadi (Editable)
            if (isEditMode) {
                EditableProfileField(
                    icon = Icons.Default.MailOutline,
                    label = "Email Pribadi",
                    value = editedPersonalEmail,
                    onValueChange = onPersonalEmailChange,
                    placeholder = "Masukkan email pribadi"
                )
            } else {
                ProfileInfoItem(
                    icon = Icons.Default.MailOutline,
                    label = "Email Pribadi",
                    value = profile.personalEmail.ifBlank { "-" },
                    isEditable = true
                )
            }

            ProfileDivider()

            // Tempat & Tanggal Lahir (Read-only)
            ProfileInfoItem(
                icon = Icons.Default.DateRange,
                label = "Tempat & Tanggal Lahir",
                value = buildString {
                    if (profile.placeOfBirth.isNotBlank()) {
                        append(profile.placeOfBirth)
                    }
                    if (profile.dateOfBirth.isNotBlank()) {
                        if (profile.placeOfBirth.isNotBlank()) append(", ")
                        append(profile.dateOfBirth)
                    }
                    if (isEmpty()) append("-")
                },
                isEditable = false
            )

            ProfileDivider()

            // No Telepon (Editable)
            if (isEditMode) {
                EditableProfileField(
                    icon = Icons.Default.Phone,
                    label = "No. Telepon",
                    value = editedPhone,
                    onValueChange = onPhoneChange,
                    placeholder = "Masukkan nomor telepon"
                )
            } else {
                ProfileInfoItem(
                    icon = Icons.Default.Phone,
                    label = "No. Telepon",
                    value = profile.phone.ifBlank { "-" },
                    isEditable = true
                )
            }

            ProfileDivider()

            // NISN/NIP (Read-only)
            ProfileInfoItem(
                icon = Icons.Default.AccountCircle,
                label = "NISN / NIP",
                value = profile.idNumber.ifBlank { "-" },
                isLast = true,
                isEditable = false
            )
        }
    }
}

@Composable
private fun ProfileInfoItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    isLast: Boolean = false,
    isEditable: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )

                if (isEditable) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Dapat diedit",
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun EditableProfileField(
    icon: ImageVector,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(24.dp)
                .padding(top = 12.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            TextInput(
                label = label,
                value = value,
                placeholder = placeholder,
                onValueChange = onValueChange,
                singleLine = true
            )
        }
    }
}

@Composable
private fun ProfileDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 40.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
}