package util.extension

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScrollableDropdownMenu(
    options: List<String>,
    initialSelectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(initialSelectedItem) }

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(selectedItem)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Expand dropdown",
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            Column(
                modifier =
                    Modifier
                        .width(IntrinsicSize.Max)
                        .height(IntrinsicSize.Max)
                        .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            selectedItem = option
                            expanded = false
                            onItemSelected(option)
                        },
                    ) {
                        Text(text = option)
                    }
                }
            }
        }
    }
}
