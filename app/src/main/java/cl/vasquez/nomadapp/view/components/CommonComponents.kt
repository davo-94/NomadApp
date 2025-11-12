package cl.vasquez.nomadapp.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import cl.vasquez.nomadapp.ui.theme.MagentaPrimary
import cl.vasquez.nomadapp.ui.theme.DarkCardBackground
import cl.vasquez.nomadapp.ui.theme.CyanAccent
import androidx.compose.foundation.BorderStroke

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MagentaPrimary)
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyLarge, color = Color.White)
    }
}

@Composable
fun SecondaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, MagentaPrimary)
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyLarge, color = MagentaPrimary)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogCard(
    title: String,
    date: String,
    description: String,
    imageUrl: String?,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(onClick = onClick)
            .border(1.dp, MagentaPrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardBackground)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Imagen de fondo
            if (!imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    DarkCardBackground,
                                    DarkCardBackground.copy(alpha = 0.7f)
                                )
                            )
                        )
                )
            }
            
            // Gradiente oscuro con magenta al fondo (más sofisticado)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.5f),
                                Color.Black.copy(alpha = 0.85f)
                            ),
                            startY = 0f,
                            endY = 220f * 4
                        )
                    )
            )

            // Texto superpuesto en la parte inferior con acento magenta
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                // Línea decorativa magenta
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(3.dp)
                        .background(MagentaPrimary, RoundedCornerShape(2.dp))
                        .padding(bottom = 8.dp)
                )
                
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 2
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    color = CyanAccent.copy(alpha = 0.9f),
                    fontSize = MaterialTheme.typography.bodySmall.fontSize * 0.9
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f),
                    maxLines = 2
                )
            }
        }
    }
}
