import os
from PIL import Image, ImageDraw, ImageFont
import random

# Create directories for images
os.makedirs('images', exist_ok=True)
os.makedirs('images/posters', exist_ok=True)

# Function to create a simple movie poster image
def create_movie_poster(title, genre, year, rating, filename):
    # Create a blank image with movie poster dimensions (2:3 aspect ratio)
    width, height = 600, 900
    image = Image.new('RGB', (width, height), color=(random.randint(20, 60), random.randint(20, 60), random.randint(20, 60)))
    draw = ImageDraw.Draw(image)
    
    # Add some random shapes for visual interest
    for _ in range(5):
        shape_color = (
            random.randint(100, 255),
            random.randint(100, 255),
            random.randint(100, 255)
        )
        # Make sure x1 < x2 and y1 < y2
        x1 = random.randint(0, width-100)
        y1 = random.randint(0, height-100)
        x2 = x1 + random.randint(50, 100)
        y2 = y1 + random.randint(50, 100)
        draw.rectangle([x1, y1, x2, y2], fill=shape_color)
    
    # Try to use a font if available
    try:
        title_font = ImageFont.truetype("arial.ttf", 48)
        info_font = ImageFont.truetype("arial.ttf", 24)
    except IOError:
        # Fall back to default font
        title_font = ImageFont.load_default()
        info_font = ImageFont.load_default()
    
    # Add title text (centered)
    title_x, title_y = width // 2, height - 200
    draw.text((title_x, title_y), title, font=title_font, fill=(255, 255, 255), anchor="mm")
    
    # Add genre, year, and rating
    info_text = f"{genre} • {year} • {rating}★"
    draw.text((width // 2, height - 120), info_text, font=info_font, fill=(200, 200, 200), anchor="mm")
    
    # Add a "NETFIX" watermark
    draw.text((width // 2, 50), "NETFIX", font=title_font, fill=(229, 9, 20), anchor="mm")
    
    # Save the image
    image.save(f"images/posters/{filename}")
    print(f"Created poster: {filename}")
    return filename

# Create movie posters for different genres
movie_data = [
    {"title": "Cosmic Adventure", "genre": "Sci-Fi", "year": 2025, "rating": 4.8},
    {"title": "The Last Detective", "genre": "Thriller", "year": 2024, "rating": 4.6},
    {"title": "Love in Paris", "genre": "Romance", "year": 2025, "rating": 4.5},
    {"title": "Jungle Quest", "genre": "Adventure", "year": 2024, "rating": 4.7},
    {"title": "Midnight Shadows", "genre": "Horror", "year": 2025, "rating": 4.4},
    {"title": "Laugh Factory", "genre": "Comedy", "year": 2024, "rating": 4.9}
]

# Create movie posters
for i, movie in enumerate(movie_data):
    create_movie_poster(
        movie["title"],
        movie["genre"],
        movie["year"],
        movie["rating"],
        f"movie_{i+1}.jpg"
    )

print("All images created successfully!")

# Create a simple HTML trailer
html_content = """<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Netfix App Trailer</title>
    <style>
        body {
            background-color: #000;
            color: white;
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        header {
            text-align: center;
            padding: 100px 0;
            background: linear-gradient(rgba(0,0,0,0.7), rgba(0,0,0,0.7)), url('https://images.unsplash.com/photo-1478720568477-152d9b164e26?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80');
            background-size: cover;
            background-position: center;
            margin-bottom: 50px;
        }
        h1 {
            color: #E50914;
            font-size: 72px;
            margin: 0;
            animation: fadeIn 1.5s;
        }
        h2 {
            color: #E50914;
            font-size: 36px;
            margin-top: 40px;
            animation: fadeIn 1.5s;
        }
        .tagline {
            font-size: 24px;
            margin-top: 20px;
            animation: fadeIn 2s;
        }
        .section {
            margin-bottom: 80px;
            animation: fadeIn 1.5s;
        }
        .movie-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 20px;
            margin-top: 30px;
        }
        .movie-card {
            background-color: #333;
            border-radius: 5px;
            overflow: hidden;
            transition: transform 0.3s;
        }
        .movie-card:hover {
            transform: scale(1.05);
        }
        .movie-poster {
            width: 100%;
            aspect-ratio: 2/3;
        }
        .movie-poster img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        .movie-info {
            padding: 15px;
        }
        .movie-title {
            font-size: 16px;
            font-weight: bold;
            margin-bottom: 5px;
        }
        .movie-meta {
            font-size: 14px;
            color: #999;
        }
        .feature-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 30px;
            margin-top: 40px;
        }
        .feature-card {
            background-color: #222;
            border-radius: 8px;
            padding: 30px;
            transition: transform 0.3s;
        }
        .feature-card:hover {
            transform: translateY(-10px);
        }
        .feature-icon {
            font-size: 48px;
            margin-bottom: 20px;
            color: #E50914;
        }
        .feature-title {
            font-size: 24px;
            margin-bottom: 15px;
            color: #E50914;
        }
        .feature-desc {
            font-size: 16px;
            line-height: 1.6;
        }
        .cta-button {
            display: inline-block;
            background-color: #E50914;
            color: white;
            padding: 15px 30px;
            border-radius: 5px;
            font-size: 18px;
            font-weight: bold;
            text-decoration: none;
            margin-top: 30px;
            transition: background-color 0.3s;
        }
        .cta-button:hover {
            background-color: #F40612;
        }
        footer {
            text-align: center;
            margin-top: 60px;
            padding: 40px 20px;
            border-top: 1px solid #333;
        }
        
        /* Animations */
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
        
        /* Responsive design */
        @media (max-width: 768px) {
            h1 { font-size: 48px; }
            h2 { font-size: 28px; }
        }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1>NETFIX</h1>
            <p class="tagline">Your Ultimate Streaming Experience</p>
        </header>

        <div class="section">
            <h2>Welcome to Netfix</h2>
            <p>The revolutionary streaming platform that brings you the best content across all genres. Our app is designed to provide a seamless and personalized viewing experience.</p>
            
            <div class="feature-grid">
                <div class="feature-card">
                    <div class="feature-icon">👤</div>
                    <h3 class="feature-title">Personalized Profiles</h3>
                    <p class="feature-desc">Create multiple profiles for everyone in your household, each with their own preferences and recommendations.</p>
                </div>
                
                <div class="feature-card">
                    <div class="feature-icon">🎬</div>
                    <h3 class="feature-title">Extensive Library</h3>
                    <p class="feature-desc">Access thousands of movies and TV shows across various genres and languages.</p>
                </div>
                
                <div class="feature-card">
                    <div class="feature-icon">📱</div>
                    <h3 class="feature-title">Watch Anywhere</h3>
                    <p class="feature-desc">Stream on your phone, tablet, or TV with our seamless cross-device experience.</p>
                </div>
            </div>
        </div>

        <div class="section">
            <h2>Trending Now</h2>
            <p>Check out what's popular on Netfix right now.</p>
            
            <div class="movie-grid">
                <div class="movie-card">
                    <div class="movie-poster">
                        <img src="images/posters/movie_1.jpg" alt="Cosmic Adventure">
                    </div>
                    <div class="movie-info">
                        <div class="movie-title">Cosmic Adventure</div>
                        <div class="movie-meta">2025 • Sci-Fi • 4.8 ⭐</div>
                    </div>
                </div>
                
                <div class="movie-card">
                    <div class="movie-poster">
                        <img src="images/posters/movie_2.jpg" alt="The Last Detective">
                    </div>
                    <div class="movie-info">
                        <div class="movie-title">The Last Detective</div>
                        <div class="movie-meta">2024 • Thriller • 4.6 ⭐</div>
                    </div>
                </div>
                
                <div class="movie-card">
                    <div class="movie-poster">
                        <img src="images/posters/movie_3.jpg" alt="Love in Paris">
                    </div>
                    <div class="movie-info">
                        <div class="movie-title">Love in Paris</div>
                        <div class="movie-meta">2025 • Romance • 4.5 ⭐</div>
                    </div>
                </div>
                
                <div class="movie-card">
                    <div class="movie-poster">
                        <img src="images/posters/movie_4.jpg" alt="Jungle Quest">
                    </div>
                    <div class="movie-info">
                        <div class="movie-title">Jungle Quest</div>
                        <div class="movie-meta">2024 • Adventure • 4.7 ⭐</div>
                    </div>
                </div>
                
                <div class="movie-card">
                    <div class="movie-poster">
                        <img src="images/posters/movie_5.jpg" alt="Midnight Shadows">
                    </div>
                    <div class="movie-info">
                        <div class="movie-title">Midnight Shadows</div>
                        <div class="movie-meta">2025 • Horror • 4.4 ⭐</div>
                    </div>
                </div>
                
                <div class="movie-card">
                    <div class="movie-poster">
                        <img src="images/posters/movie_6.jpg" alt="Laugh Factory">
                    </div>
                    <div class="movie-info">
                        <div class="movie-title">Laugh Factory</div>
                        <div class="movie-meta">2024 • Comedy • 4.9 ⭐</div>
                    </div>
                </div>
            </div>
        </div>

        <div class="section">
            <h2>Smart Features</h2>
            <p>Netfix is designed with you in mind, offering a range of features to enhance your viewing experience.</p>
            
            <div class="feature-grid">
                <div class="feature-card">
                    <div class="feature-icon">🔍</div>
                    <h3 class="feature-title">Smart Search</h3>
                    <p class="feature-desc">Find exactly what you're looking for with our intelligent search functionality.</p>
                </div>
                
                <div class="feature-card">
                    <div class="feature-icon">💾</div>
                    <h3 class="feature-title">Download & Watch</h3>
                    <p class="feature-desc">Download your favorite content and watch it offline, anytime, anywhere.</p>
                </div>
                
                <div class="feature-card">
                    <div class="feature-icon">🤖</div>
                    <h3 class="feature-title">AI Recommendations</h3>
                    <p class="feature-desc">Our AI learns your preferences and suggests content you'll love.</p>
                </div>
                
                <div class="feature-card">
                    <div class="feature-icon">🔄</div>
                    <h3 class="feature-title">Seamless Streaming</h3>
                    <p class="feature-desc">Enjoy uninterrupted viewing with adaptive streaming quality.</p>
                </div>
            </div>
            
            <div style="text-align: center; margin-top: 50px;">
                <a href="#" class="cta-button">Download Netfix Now</a>
            </div>
        </div>

        <footer>
            <h1>NETFIX</h1>
            <p>Stream Smarter. Download Now.</p>
            <p>&copy; 2025 Netfix. All rights reserved.</p>
        </footer>
    </div>

    <script>
        // Add a simple animation when scrolling
        document.addEventListener('DOMContentLoaded', function() {
            const sections = document.querySelectorAll('.section');
            
            function checkScroll() {
                sections.forEach(section => {
                    const sectionTop = section.getBoundingClientRect().top;
                    const windowHeight = window.innerHeight;
                    
                    if (sectionTop < windowHeight * 0.75) {
                        section.style.opacity = '1';
                        section.style.transform = 'translateY(0)';
                    }
                });
            }
            
            // Set initial styles
            sections.forEach(section => {
                section.style.opacity = '0';
                section.style.transform = 'translateY(50px)';
                section.style.transition = 'opacity 1s ease, transform 1s ease';
            });
            
            // Check on scroll
            window.addEventListener('scroll', checkScroll);
            
            // Check on initial load
            checkScroll();
        });
    </script>
</body>
</html>
"""

# Save the HTML file
with open("netfix_simple_trailer.html", "w") as f:
    f.write(html_content)

print("Simple HTML trailer created: netfix_simple_trailer.html")
