package util.extension

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ScrollableDropdownMenu(
    options: List<String>,
    initialSelectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    searchEnabled: Boolean = true,
    maxDisplayItems: Int = 6,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(initialSelectedItem) }
    var searchText by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    // Filter options based on search text
    val filteredOptions = remember(searchText, options) {
        if (searchText.isEmpty()) {
            options
        } else {
            options.filter { it.contains(searchText, ignoreCase = true) }
        }
    }

    // Update selected item when initialSelectedItem changes
    LaunchedEffect(initialSelectedItem) {
        selectedItem = initialSelectedItem
    }

    Box(modifier = modifier) {
        // Main dropdown button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            elevation = if (isFocused || expanded) 4.dp else 2.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (label.isNotEmpty()) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.primary,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                    Text(
                        text = if (selectedItem.isEmpty()) "Select an option" else selectedItem,
                        style = MaterialTheme.typography.body1,
                        color = if (selectedItem.isEmpty()) 
                            MaterialTheme.colors.onSurface.copy(alpha = 0.6f) 
                        else 
                            MaterialTheme.colors.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = if (expanded) "Collapse dropdown" else "Expand dropdown",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Enhanced dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { 
                expanded = false
                searchText = ""
                isFocused = false
            },
            modifier = Modifier
                .width(300.dp)
                .heightIn(max = 300.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.surface,
                elevation = 8.dp,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column {
                    // Search field (if enabled)
                    if (searchEnabled && options.size > 10) {
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text("Search...") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = MaterialTheme.colors.primary
                                )
                            },
                            trailingIcon = if (searchText.isNotEmpty()) {
                                {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = "Clear search",
                                        tint = MaterialTheme.colors.primary,
                                        modifier = Modifier.clickable { searchText = "" }
                                    )
                                }
                            } else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .onFocusChanged { isFocused = it.isFocused },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
                    }

                    // Options list
                    if (filteredOptions.isEmpty()) {
                        Text(
                            text = "No options found",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    } else {
                        Column(
                            modifier = Modifier
                                .heightIn(max = 200.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            filteredOptions.take(maxDisplayItems * 10).forEach { option ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedItem = option
                                        expanded = false
                                        searchText = ""
                                        isFocused = false
                                        onItemSelected(option)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (option == selectedItem) 
                                                MaterialTheme.colors.primary.copy(alpha = 0.08f)
                                            else 
                                                Color.Transparent,
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 4.dp)
                                ) {
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.body2,
                                        color = if (option == selectedItem) 
                                            MaterialTheme.colors.primary
                                        else 
                                            MaterialTheme.colors.onSurface,
                                        fontWeight = if (option == selectedItem) FontWeight.Medium else FontWeight.Normal,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
