package util.extension

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CustomModalDialog(
    visible: Boolean,
    title: String,
    message: String,
    onDismiss: () -> Unit,
) {
    if (visible) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .width(500.dp)
                    .height(400.dp),
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                    ) {
                        // Header
                        Text(
                            title, 
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.onSurface
                        )
                        Spacer(Modifier.height(16.dp))
                        
                        // Content
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            color = MaterialTheme.colors.background,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = message,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .verticalScroll(rememberScrollState()),
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onBackground,
                                lineHeight = MaterialTheme.typography.body2.lineHeight
                            )
                        }
                        
                        Spacer(Modifier.height(20.dp))
                        
                        // Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            Button(
                                onClick = onDismiss,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.primary,
                                    contentColor = MaterialTheme.colors.onPrimary
                                ),
                                shape = RoundedCornerShape(8.dp),
                                elevation = ButtonDefaults.elevation(defaultElevation = 2.dp)
                            ) { 
                                Text("OK", style = MaterialTheme.typography.button) 
                            }
                        }
                    }
                }
            }
        }
    }
}
