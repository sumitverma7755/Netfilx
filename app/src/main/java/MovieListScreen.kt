import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun MovieListScreen(movies: List<Movie>, onMovieClick: (Movie) -> Unit) {
    LazyColumn {
        items(movies) { movie ->
            MovieItem(movie = movie, onMovieClick = onMovieClick)
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onMovieClick: (Movie) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onMovieClick(movie) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = movie.thumbnailUrl,
                contentDescription = movie.title,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(text = movie.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}