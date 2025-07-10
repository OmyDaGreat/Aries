package util.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf

object ScrollOption {
    val activeDialogs = mutableStateMapOf<String, DialogRequest>()

    fun requestScrollableMessageDialog(
        title: String,
        message: String,
    ) {
        val dialogId = "${title}_${System.currentTimeMillis()}"
        activeDialogs[dialogId] = DialogRequest(title, message)
    }

    data class DialogRequest(
        val title: String,
        val message: String,
    )

    @Composable
    fun DialogHost() {
        activeDialogs.forEach { (dialogId, request) ->
            CustomModalDialog(
                true,
                request.title,
                request.message,
            ) { activeDialogs.remove(dialogId) }
        }
    }
}
