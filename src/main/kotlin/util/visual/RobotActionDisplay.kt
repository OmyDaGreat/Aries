package util.visual

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.malefic.emkt.Signal
import xyz.malefic.emkt.Signals

class RobotActionSignal(
    val action: String,
) : Signal()

fun logAction(action: String) {
    Signals.emit(RobotActionSignal(action))
}

@Composable
fun RobotActionDisplay(
    elevation: Dp = 2.dp,
    cornerRadius: Dp = 12.dp,
    modifier: Modifier = Modifier,
) {
    var currentAction by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        Signals.on<RobotActionSignal> { signal ->
            currentAction = signal.action
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = elevation,
        shape = RoundedCornerShape(cornerRadius),
        backgroundColor = MaterialTheme.colors.surface,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Current Robot Action",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp),
            )

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colors.primary.copy(alpha = 0.1f),
                            RoundedCornerShape(8.dp),
                        ).padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = currentAction.ifEmpty { "Waiting for action..." },
                    style = MaterialTheme.typography.body1,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp,
                    color =
                        if (currentAction.isEmpty()) {
                            MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                        } else {
                            MaterialTheme.colors.primary
                        },
                    fontWeight = if (currentAction.isEmpty()) FontWeight.Normal else FontWeight.Bold,
                )
            }
        }
    }
}
