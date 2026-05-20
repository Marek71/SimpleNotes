package com.mhsoft.simplenotes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mhsoft.simplenotes.ui.NoteViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

private val noteColors = listOf(
    "#FFF8E1",
    "#E3F2FD",
    "#E8F5E9",
    "#FCE4EC",
    "#F3E5F5",
    "#ECEFF1",
    "#FFF3E0",
    "#E0F7FA",
    "#FFEBEE",
    "#EDE7F6",
    "#E1F5FE",
    "#F1F8E9",
    "#263238",
    "#1A237E",
    "#4A148C",
    "#1B5E20"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditNoteScreen(
    noteId: Long,
    viewModel: NoteViewModel,
    onBack: () -> Unit
) {
    var loaded by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("#FFF8E1") }

    var createdAt by remember { mutableStateOf<Long?>(null) }
    var updatedAt by remember { mutableStateOf<Long?>(null) }

    var showColorPickerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(noteId) {
        if (noteId != 0L) {
            val note = viewModel.getNoteById(noteId)

            if (note != null) {
                title = note.title
                text = note.text
                selectedColor = note.colorHex
                createdAt = note.createdAt
                updatedAt = note.updatedAt
            }
        }

        loaded = true
    }

    if (!loaded) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Načítání...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    fun saveNote() {
        viewModel.saveNote(
            id = noteId.takeIf { it != 0L },
            title = title.trim().ifBlank { "Bez názvu" },
            text = text.trim(),
            colorHex = selectedColor,
            onSaved = onBack
        )
    }

    val noteContentColor = contentColorForNoteBackground(selectedColor)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (noteId == 0L) "Nová poznámka" else "Upravit poznámku",
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Lokálně uložená poznámka",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.78f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Zpět"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { saveNote() }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "Uložit"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 3.dp,
                shadowElevation = 6.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Button(
                    onClick = { saveNote() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .imePadding(),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Uložit poznámku",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = parseColor(selectedColor),
                    contentColor = noteContentColor
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Editor",
                        style = MaterialTheme.typography.labelLarge,
                        color = noteContentColor.copy(alpha = 0.72f),
                        fontWeight = FontWeight.SemiBold
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Název") },
                        placeholder = { Text("Například: Nápad na projekt") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = noteTextFieldColors(noteContentColor)
                    )

                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        label = { Text("Text poznámky") },
                        placeholder = { Text("Začněte psát...") },
                        shape = RoundedCornerShape(16.dp),
                        colors = noteTextFieldColors(noteContentColor)
                    )
                }
            }

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Barva poznámky",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = selectedColor.uppercase(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(parseColor(selectedColor))
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = CircleShape
                                )
                        )
                    }

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        noteColors.forEach { colorHex ->
                            ColorCircle(
                                colorHex = colorHex,
                                selected = selectedColor.equals(colorHex, ignoreCase = true),
                                onClick = { selectedColor = colorHex }
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = { showColorPickerDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(vertical = 13.dp)
                    ) {
                        Text("Namíchat vlastní barvu")
                    }
                }
            }

            if (createdAt != null && updatedAt != null) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Informace",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )

                        HorizontalDivider()

                        InfoRow(
                            label = "Vytvořeno",
                            value = formatTimestamp(createdAt!!)
                        )

                        InfoRow(
                            label = "Naposledy upraveno",
                            value = formatTimestamp(updatedAt!!)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(72.dp))
        }
    }

    if (showColorPickerDialog) {
        ColorPickerDialog(
            initialColorHex = selectedColor,
            onDismiss = {
                showColorPickerDialog = false
            },
            onConfirm = { colorHex ->
                selectedColor = colorHex
                showColorPickerDialog = false
            }
        )
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ColorCircle(
    colorHex: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val circleContentColor = contentColorForNoteBackground(colorHex)

    Box(
        modifier = Modifier
            .size(44.dp)
            .background(
                color = parseColor(colorHex),
                shape = CircleShape
            )
            .border(
                width = if (selected) 3.dp else 1.dp,
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Surface(
                modifier = Modifier.size(22.dp),
                shape = CircleShape,
                color = circleContentColor.copy(alpha = 0.92f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = if (circleContentColor == Color.White) Color.Black else Color.White,
                    modifier = Modifier.padding(3.dp)
                )
            }
        }
    }
}

@Composable
private fun ColorPickerDialog(
    initialColorHex: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val initialRgb = remember(initialColorHex) {
        hexToRgb(initialColorHex)
    }

    var red by remember { mutableFloatStateOf(initialRgb.red.toFloat()) }
    var green by remember { mutableFloatStateOf(initialRgb.green.toFloat()) }
    var blue by remember { mutableFloatStateOf(initialRgb.blue.toFloat()) }

    var hexText by remember {
        mutableStateOf(rgbToHex(initialRgb.red, initialRgb.green, initialRgb.blue))
    }

    fun syncFromSliders() {
        hexText = rgbToHex(
            red.roundToInt(),
            green.roundToInt(),
            blue.roundToInt()
        )
    }

    val previewColor = Color(
        red = red.roundToInt(),
        green = green.roundToInt(),
        blue = blue.roundToInt()
    )

    val previewContentColor = contentColorForNoteBackground(hexText)

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        title = {
            Text(
                text = "Vlastní barva",
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(92.dp),
                    shape = RoundedCornerShape(22.dp),
                    color = previewColor,
                    tonalElevation = 2.dp
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = hexText.uppercase(),
                            color = previewContentColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                OutlinedTextField(
                    value = hexText,
                    onValueChange = { value ->
                        hexText = value.uppercase()

                        if (isValidHexColor(value)) {
                            val rgb = hexToRgb(value)
                            red = rgb.red.toFloat()
                            green = rgb.green.toFloat()
                            blue = rgb.blue.toFloat()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("HEX barva") },
                    placeholder = { Text("#FFF8E1") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    isError = hexText.isNotBlank() && !isValidHexColor(hexText)
                )

                ColorSlider(
                    label = "Červená",
                    value = red,
                    onValueChange = {
                        red = it
                        syncFromSliders()
                    }
                )

                ColorSlider(
                    label = "Zelená",
                    value = green,
                    onValueChange = {
                        green = it
                        syncFromSliders()
                    }
                )

                ColorSlider(
                    label = "Modrá",
                    value = blue,
                    onValueChange = {
                        blue = it
                        syncFromSliders()
                    }
                )

                Text(
                    text = "Text v poznámce se automaticky přepne na světlý nebo tmavý podle kontrastu vybrané barvy.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = {
                    if (isValidHexColor(hexText)) {
                        onConfirm(hexText.uppercase())
                    }
                },
                enabled = isValidHexColor(hexText)
            ) {
                Text("Použít")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Zrušit")
            }
        }
    )
}

@Composable
private fun ColorSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = value.roundToInt().toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..255f
        )
    }
}

@Composable
private fun noteTextFieldColors(contentColor: Color) = OutlinedTextFieldDefaults.colors(
    focusedTextColor = contentColor,
    unfocusedTextColor = contentColor,
    focusedLabelColor = contentColor.copy(alpha = 0.88f),
    unfocusedLabelColor = contentColor.copy(alpha = 0.72f),
    focusedPlaceholderColor = contentColor.copy(alpha = 0.54f),
    unfocusedPlaceholderColor = contentColor.copy(alpha = 0.46f),
    focusedBorderColor = contentColor.copy(alpha = 0.72f),
    unfocusedBorderColor = contentColor.copy(alpha = 0.38f),
    cursorColor = contentColor
)

private data class RgbColor(
    val red: Int,
    val green: Int,
    val blue: Int
)

private fun parseColor(hex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (_: Exception) {
        Color(0xFFFFF8E1)
    }
}

private fun isValidHexColor(hex: String): Boolean {
    return Regex("^#([A-Fa-f0-9]{6})$").matches(hex.trim())
}

private fun hexToRgb(hex: String): RgbColor {
    val safeHex = if (isValidHexColor(hex)) {
        hex.trim()
    } else {
        "#FFF8E1"
    }

    val colorInt = android.graphics.Color.parseColor(safeHex)

    return RgbColor(
        red = android.graphics.Color.red(colorInt),
        green = android.graphics.Color.green(colorInt),
        blue = android.graphics.Color.blue(colorInt)
    )
}

private fun rgbToHex(
    red: Int,
    green: Int,
    blue: Int
): String {
    return "#%02X%02X%02X".format(
        red.coerceIn(0, 255),
        green.coerceIn(0, 255),
        blue.coerceIn(0, 255)
    )
}

private fun contentColorForNoteBackground(hex: String): Color {
    val rgb = hexToRgb(hex)

    val luminance = (
            0.299 * rgb.red +
                    0.587 * rgb.green +
                    0.114 * rgb.blue
            ) / 255.0

    return if (luminance < 0.55) {
        Color.White
    } else {
        Color.Black
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date(timestamp))
}