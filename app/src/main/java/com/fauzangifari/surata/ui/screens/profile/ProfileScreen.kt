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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fauzangifari.surata.ui.components.*
import com.fauzangifari.surata.ui.theme.Blue900
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

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
    onUpdateProfile: (UserProfile) -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    val profile by userProfileFlow.collectAsState(initial = UserProfile())
    val isLoading by viewModel.isLoading.collectAsState()
    val isEditMode by viewModel.isEditMode.collectAsState()
    val editedPhone by viewModel.editedPhone.collectAsState()
    val editedPersonalEmail by viewModel.editedPersonalEmail.collectAsState()
    val phoneError by viewModel.phoneError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val showSaveDialog by viewModel.showSaveDialog.collectAsState()
    val showChangePhotoDialog by viewModel.showChangePhotoDialog.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val showToast by viewModel.showToast.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val profileState by viewModel.profile.collectAsState()

    val scrollState = rememberScrollState()

    LaunchedEffect(profile) {
        viewModel.loadProfile(profile)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.background)
        ) {
            ProfileHeader(
                profile = profileState,
                isEditMode = isEditMode,
                onChangePhotoClick = { viewModel.onShowChangePhotoDialog() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProfileInfoCard(
                profile = profileState,
                isEditMode = isEditMode,
                editedPhone = editedPhone,
                editedPersonalEmail = editedPersonalEmail,
                phoneError = phoneError,
                emailError = emailError,
                onPhoneChange = { viewModel.onPhoneChange(it) },
                onPersonalEmailChange = { viewModel.onPersonalEmailChange(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isEditMode) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.onEditModeToggle(false) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Blue900
                        )
                    ) {
                        Text(
                            text = "Batal",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Button(
                        onClick = { viewModel.onSaveClick() },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue900
                        ),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Simpan",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            } else {
                Button(
                    onClick = { viewModel.onEditModeToggle(true) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue900
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Edit Profil",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Toast at top
        if (showToast) {
            CustomToast(
                message = toastMessage,
                type = if (isSuccess) ToastType.SUCCESS else ToastType.ERROR,
                visible = showToast,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissSaveDialog() },
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Blue900
                )
            },
            title = {
                Text(
                    text = "Simpan Perubahan?",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    text = "Apakah Anda yakin ingin menyimpan perubahan profil?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.saveProfile(onUpdateProfile) },
                    enabled = !isLoading
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.onDismissSaveDialog() },
                    enabled = !isLoading
                ) {
                    Text("Batal")
                }
            }
        )
    }

    if (showChangePhotoDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissChangePhotoDialog() },
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null
                )
            },
            title = {
                Text(
                    text = "Ubah Foto Profil",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    text = "Fitur ubah foto profil akan segera tersedia.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.onDismissChangePhotoDialog() }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun ProfileHeader(
    profile: UserProfile,
    isEditMode: Boolean,
    onChangePhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
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
                    SmallFloatingActionButton(
                        onClick = onChangePhotoClick,
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

            Text(
                text = profile.name.ifBlank { "Nama Pengguna" },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

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
    phoneError: String?,
    emailError: String?,
    onPhoneChange: (String) -> Unit,
    onPersonalEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Informasi Pribadi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Nama (Read-only)
            ProfileInfoItem(
                icon = Icons.Default.Person,
                label = "Nama Lengkap",
                value = profile.name.ifBlank { "-" },
                isEditable = false
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Email Sekolah (Read-only)
            ProfileInfoItem(
                icon = Icons.Default.Email,
                label = "Email Sekolah",
                value = profile.schoolEmail.ifBlank { "-" },
                isEditable = false
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            if (isEditMode) {
                EditableProfileField(
                    icon = Icons.Default.MailOutline,
                    label = "Email Pribadi",
                    value = editedPersonalEmail,
                    onValueChange = onPersonalEmailChange,
                    placeholder = "contoh@email.com",
                    errorMessage = emailError
                )
            } else {
                ProfileInfoItem(
                    icon = Icons.Default.MailOutline,
                    label = "Email Pribadi",
                    value = profile.personalEmail.ifBlank { "-" },
                    isEditable = true
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

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

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // No Telepon (Editable)
            if (isEditMode) {
                EditableProfileField(
                    icon = Icons.Default.Phone,
                    label = "No. Telepon",
                    value = editedPhone,
                    onValueChange = onPhoneChange,
                    placeholder = "08xxxxxxxxxx",
                    errorMessage = phoneError
                )
            } else {
                ProfileInfoItem(
                    icon = Icons.Default.Phone,
                    label = "No. Telepon",
                    value = profile.phone.ifBlank { "-" },
                    isEditable = true
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // NISN/NIP (Read-only)
            ProfileInfoItem(
                icon = Icons.Default.AccountCircle,
                label = "NISN / NIP",
                value = profile.idNumber.ifBlank { "-" },
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
            .padding(vertical = 8.dp),
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
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )

                if (isEditable) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Dapat diedit",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

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
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (errorMessage != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = { Text(label) },
                    placeholder = { Text(placeholder) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null,
                    singleLine = true,
                    supportingText = if (errorMessage != null) {
                        { Text(errorMessage) }
                    } else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        errorLabelColor = MaterialTheme.colorScheme.error
                    )
                )
            }
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