package util.extension

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.rememberRichTextState

@Composable
fun Spinner(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    modifier: Modifier = Modifier
) {
    var textValue by remember(value) { mutableStateOf(value.toString()) }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Slider(
            value = value.toFloat(),
            onValueChange = { newValue ->
                val intValue = newValue.toInt()
                onValueChange(intValue)
                textValue = intValue.toString()
            },
            valueRange = range.first.toFloat()..range.last.toFloat(),
            steps = range.last - range.first,
            modifier = Modifier.weight(1f)
        )
        TextField(
            value = textValue,
            onValueChange = { newText ->
                textValue = newText
                val intValue = newText.toIntOrNull() ?: 0
                onValueChange(intValue)
            },
            label = { Text("Value") },
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun String.toRichHtmlString(): AnnotatedString {
    val state = rememberRichTextState()
    LaunchedEffect(this) {
        state.setHtml(this@toRichHtmlString)
    }
    return state.annotatedString
}