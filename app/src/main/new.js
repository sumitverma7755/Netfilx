const express = require('express');
const app = express();
const port = 3000;

app.use(express.json());

app.get('/movies', (req, res) => {
  const movies = [
    {
      id: 1,
      title: 'Movie 1',
      description: 'Description of Movie 1',
      thumbnailUrl: 'https://via.placeholder.com/150',
      videoUrl: 'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4',
    },
    {
      id: 2,
      title: 'Movie 2',
      description: 'Description of Movie 2',
      thumbnailUrl: 'https://via.placeholder.com/150',
      videoUrl: 'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4',
    },
    {
      id: 3,
      title: 'Movie 3',
      description: 'Description of Movie 3',
      thumbnailUrl: 'https://via.placeholder.com/150',
      videoUrl: 'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4',
    },
  ];
  res.json(movies);
});

app.listen(port, () => {
  console.log(`Server listening at http://localhost:${port}`);
});