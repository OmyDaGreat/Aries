package util.extension

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
  modifier: Modifier = Modifier,
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
      modifier = Modifier.weight(1f),
    )
    TextField(
      value = textValue,
      onValueChange = { newText ->
        textValue = newText
        val intValue = newText.toIntOrNull() ?: 0
        onValueChange(intValue)
      },
      label = { Text("Value") },
      modifier = Modifier.padding(start = 8.dp),
    )
  }
}

@Composable
fun String.toRichHtmlString(): AnnotatedString {
  val state = rememberRichTextState()
  LaunchedEffect(this) { state.setHtml(this@toRichHtmlString) }
  return state.annotatedString
}

@Composable
fun ScrollableDropdownMenu(
  options: List<String>,
  initialSelectedItem: String,
  onItemSelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var expanded by remember { mutableStateOf(false) }
  var selectedItem by remember { mutableStateOf(initialSelectedItem) }

  Box(modifier = modifier) {
    OutlinedButton(
      onClick = { expanded = !expanded },
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(selectedItem)
      Icon(
        imageVector = Icons.Default.ArrowDropDown,
        contentDescription = "Expand dropdown"
      )
    }

    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false }
    ) {
      Column(
        modifier = Modifier
          .width(IntrinsicSize.Max)
          .height(IntrinsicSize.Max)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        options.forEach { option ->
          DropdownMenuItem(
            onClick = {
              selectedItem = option
              expanded = false
              onItemSelected(option)
            }
          ) {
            Text(text = option)
          }
        }
      }
    }
  }
}
