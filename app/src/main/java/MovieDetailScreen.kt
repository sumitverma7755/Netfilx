import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.netfix.Movie

@Composable
fun MovieDetailScreen(movie: Movie, onPlayClick: (Movie) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        AsyncImage(
            model = movie.thumbnailUrl,
            contentDescription = movie.title,
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = movie.title, modifier = Modifier.padding(top = 8.dp))
        Text(text = movie.description, modifier = Modifier.padding(top = 8.dp))
        Button(onClick = { onPlayClick(movie) }) {
            Text(text = "Play")
        }
    }
}