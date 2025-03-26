package util.extension

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState

object ScrollOption {
    val activeDialogs = mutableStateMapOf<String, DialogRequest>()

    // Non-composable function to request a dialog
    fun requestScrollableMessageDialog(
        message: String,
        title: String,
    ) {
        val dialogId = "${title}_${System.currentTimeMillis()}"
        activeDialogs[dialogId] = DialogRequest(message, title)
    }

    // Internal data class to hold dialog information
    data class DialogRequest(
        val message: String,
        val title: String,
    )

    // Add this composable function to be used in your app's main UI
    @Composable
    fun DialogHost() {
        activeDialogs.forEach { (dialogId, request) ->
            ComposeDialog(
                message = request.message,
                title = request.title,
                onDismiss = { activeDialogs.remove(dialogId) },
                isVisible = true,
            )
        }
    }
}

@Composable
private fun ComposeDialog(
    message: String,
    title: String,
    onDismiss: () -> Unit,
    isVisible: Boolean,
) {
    if (!isVisible) return

    val dialogState =
        rememberDialogState(
            position = WindowPosition(Alignment.Center),
            width = 450.dp,
            height = 350.dp,
        )

    DialogWindow(
        onCloseRequest = onDismiss,
        state = dialogState,
        title = title,
        resizable = true,
    ) {
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize().padding(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    // Scrollable text area
                    Text(
                        text = message,
                        modifier =
                            Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                        style = MaterialTheme.typography.body1,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Button row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.padding(4.dp),
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}
