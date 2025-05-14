import os
import random
from PIL import Image, ImageDraw, ImageFont
import numpy as np

# Create directories for images
os.makedirs('images', exist_ok=True)
os.makedirs('images/posters', exist_ok=True)
os.makedirs('images/categories', exist_ok=True)

# Function to create a movie poster image
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
            random.randint(100, 255),
            random.randint(30, 100)  # Alpha (transparency)
        )
        x1 = random.randint(0, width)
        y1 = random.randint(0, height)
        x2 = random.randint(0, width)
        y2 = random.randint(0, height)
        draw.rectangle([x1, y1, x2, y2], fill=shape_color)
    
    # Add title text
    try:
        # Try to use a font if available
        title_font = ImageFont.truetype("arial.ttf", 48)
        info_font = ImageFont.truetype("arial.ttf", 24)
    except IOError:
        # Fall back to default font
        title_font = ImageFont.load_default()
        info_font = ImageFont.load_default()
    
    # Add title with shadow effect
    title_x, title_y = width // 2, height - 200
    # Shadow
    draw.text((title_x+2, title_y+2), title, font=title_font, fill=(0, 0, 0), anchor="mm")
    # Main text
    draw.text((title_x, title_y), title, font=title_font, fill=(255, 255, 255), anchor="mm")
    
    # Add genre, year, and rating
    info_text = f"{genre} • {year} • {rating}★"
    draw.text((width // 2, height - 120), info_text, font=info_font, fill=(200, 200, 200), anchor="mm")
    
    # Add a "NETFIX" watermark
    watermark_font = title_font
    draw.text((width // 2, 50), "NETFIX", font=watermark_font, fill=(229, 9, 20), anchor="mm")
    
    # Save the image
    image.save(f"images/posters/{filename}")
    print(f"Created poster: {filename}")
    return filename

# Function to create a category banner
def create_category_banner(category_name, filename):
    # Create a wide banner image (16:9 aspect ratio)
    width, height = 1280, 720
    
    # Choose a background color based on the category
    color_map = {
        'Action': (180, 30, 30),
        'Comedy': (255, 191, 0),
        'Drama': (70, 130, 180),
        'Sci-Fi': (75, 0, 130),
        'Documentary': (34, 139, 34),
        'Horror': (25, 25, 25),
        'Romance': (219, 112, 147),
        'Thriller': (47, 79, 79),
        'Animation': (255, 140, 0),
        'Fantasy': (148, 0, 211)
    }
    
    bg_color = color_map.get(category_name, (random.randint(20, 180), random.randint(20, 180), random.randint(20, 180)))
    image = Image.new('RGB', (width, height), color=bg_color)
    draw = ImageDraw.Draw(image)
    
    # Add some random shapes for visual interest
    for _ in range(10):
        shape_color = (
            min(bg_color[0] + random.randint(-20, 100), 255),
            min(bg_color[1] + random.randint(-20, 100), 255),
            min(bg_color[2] + random.randint(-20, 100), 255),
            random.randint(30, 150)  # Alpha (transparency)
        )
        
        # Choose between rectangle, circle, or line
        shape_type = random.choice(['rectangle', 'circle', 'line'])
        
        if shape_type == 'rectangle':
            x1 = random.randint(0, width)
            y1 = random.randint(0, height)
            x2 = random.randint(0, width)
            y2 = random.randint(0, height)
            draw.rectangle([x1, y1, x2, y2], fill=shape_color)
        elif shape_type == 'circle':
            x1 = random.randint(0, width)
            y1 = random.randint(0, height)
            radius = random.randint(20, 200)
            draw.ellipse([x1-radius, y1-radius, x1+radius, y1+radius], fill=shape_color)
        else:  # line
            x1 = random.randint(0, width)
            y1 = random.randint(0, height)
            x2 = random.randint(0, width)
            y2 = random.randint(0, height)
            draw.line([x1, y1, x2, y2], fill=shape_color, width=random.randint(5, 20))
    
    # Add category name text
    try:
        # Try to use a font if available
        title_font = ImageFont.truetype("arial.ttf", 120)
    except IOError:
        # Fall back to default font
        title_font = ImageFont.load_default()
    
    # Add title with shadow effect
    title_x, title_y = width // 2, height // 2
    # Shadow
    draw.text((title_x+4, title_y+4), category_name, font=title_font, fill=(0, 0, 0), anchor="mm")
    # Main text
    draw.text((title_x, title_y), category_name, font=title_font, fill=(255, 255, 255), anchor="mm")
    
    # Add a "NETFIX" watermark
    try:
        watermark_font = ImageFont.truetype("arial.ttf", 36)
    except IOError:
        watermark_font = ImageFont.load_default()
    
    draw.text((width - 100, height - 50), "NETFIX", font=watermark_font, fill=(229, 9, 20), anchor="mm")
    
    # Save the image
    image.save(f"images/categories/{filename}")
    print(f"Created category banner: {filename}")
    return filename

# Create movie posters for different genres
movie_data = [
    {"title": "Cosmic Adventure", "genre": "Sci-Fi", "year": 2025, "rating": 4.8},
    {"title": "The Last Detective", "genre": "Thriller", "year": 2024, "rating": 4.6},
    {"title": "Love in Paris", "genre": "Romance", "year": 2025, "rating": 4.5},
    {"title": "Jungle Quest", "genre": "Adventure", "year": 2024, "rating": 4.7},
    {"title": "Midnight Shadows", "genre": "Horror", "year": 2025, "rating": 4.4},
    {"title": "Laugh Factory", "genre": "Comedy", "year": 2024, "rating": 4.9},
    {"title": "Urban Legend", "genre": "Drama", "year": 2025, "rating": 4.3},
    {"title": "Robot Revolution", "genre": "Sci-Fi", "year": 2024, "rating": 4.7},
    {"title": "Mountain Explorer", "genre": "Documentary", "year": 2025, "rating": 4.8},
    {"title": "Magical Kingdom", "genre": "Fantasy", "year": 2024, "rating": 4.5},
    {"title": "Speed Racers", "genre": "Action", "year": 2025, "rating": 4.6},
    {"title": "Cartoon World", "genre": "Animation", "year": 2024, "rating": 4.9}
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

# Create category banners
categories = [
    "Action", "Comedy", "Drama", "Sci-Fi", "Documentary",
    "Horror", "Romance", "Thriller", "Animation", "Fantasy"
]

for i, category in enumerate(categories):
    create_category_banner(category, f"category_{i+1}.jpg")

print("All images created successfully!")

# Create an HTML file that uses these images
html_content = """<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Netfix App Trailer with Images</title>
    <style>
        body {
            background-color: #000;
            color: white;
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            overflow-x: hidden;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        header {
            text-align: center;
            margin-bottom: 40px;
            height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            background: linear-gradient(rgba(0,0,0,0.7), rgba(0,0,0,0.7)), url('images/categories/category_4.jpg');
            background-size: cover;
            background-position: center;
        }
        h1 {
            color: #E50914;
            font-size: 72px;
            margin: 0;
            animation: fadeInUp 1.5s ease-out;
        }
        h2 {
            color: #E50914;
            font-size: 36px;
            margin-top: 40px;
            animation: fadeInLeft 1s ease-out;
        }
        .tagline {
            font-size: 24px;
            margin-top: 20px;
            opacity: 0;
            animation: fadeIn 1.5s ease-out 0.5s forwards;
        }
        .section {
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            padding: 60px 0;
            opacity: 0;
            transform: translateY(50px);
            transition: opacity 1s ease, transform 1s ease;
        }
        .section.visible {
            opacity: 1;
            transform: translateY(0);
        }
        .feature-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 30px;
            margin-top: 40px;
        }
        .feature-card {
            background-color: rgba(40, 40, 40, 0.7);
            border-radius: 8px;
            padding: 30px;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            height: 100%;
        }
        .feature-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 10px 20px rgba(229, 9, 20, 0.3);
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
        .category-showcase {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            margin-top: 30px;
        }
        .category-item {
            width: calc(33.33% - 14px);
            aspect-ratio: 16/9;
            border-radius: 5px;
            overflow: hidden;
            position: relative;
            transition: transform 0.3s ease;
        }
        .category-item:hover {
            transform: scale(1.05);
        }
        .category-item img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        .category-name {
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            background: linear-gradient(transparent, rgba(0,0,0,0.8));
            padding: 20px;
            font-weight: bold;
            font-size: 18px;
        }
        .movie-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
            gap: 20px;
            margin-top: 30px;
        }
        .movie-card {
            background-color: #333;
            border-radius: 5px;
            overflow: hidden;
            transition: transform 0.3s ease;
            height: 100%;
        }
        .movie-card:hover {
            transform: scale(1.05);
        }
        .movie-poster {
            width: 100%;
            aspect-ratio: 2/3;
            overflow: hidden;
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
            transition: background-color 0.3s ease, transform 0.3s ease;
        }
        .cta-button:hover {
            background-color: #F40612;
            transform: scale(1.05);
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
        @keyframes fadeInUp {
            from { 
                opacity: 0;
                transform: translateY(50px);
            }
            to { 
                opacity: 1;
                transform: translateY(0);
            }
        }
        @keyframes fadeInLeft {
            from { 
                opacity: 0;
                transform: translateX(-50px);
            }
            to { 
                opacity: 1;
                transform: translateX(0);
            }
        }
        
        /* Responsive design */
        @media (max-width: 768px) {
            h1 { font-size: 48px; }
            h2 { font-size: 28px; }
            .category-item { width: calc(50% - 10px); }
        }
        @media (max-width: 480px) {
            h1 { font-size: 36px; }
            h2 { font-size: 24px; }
            .category-item { width: 100%; }
        }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1>NETFIX</h1>
            <p class="tagline">Your Ultimate Streaming Experience</p>
        </header>

        <section id="intro" class="section">
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
        </section>

        <section id="categories" class="section">
            <h2>Discover Content Across Genres</h2>
            <p>Browse our extensive collection of movies and TV shows organized by genre.</p>
            
            <div class="category-showcase">
                <div class="category-item">
                    <img src="images/categories/category_1.jpg" alt="Action">
                    <div class="category-name">Action</div>
                </div>
                <div class="category-item">
                    <img src="images/categories/category_2.jpg" alt="Comedy">
                    <div class="category-name">Comedy</div>
                </div>
                <div class="category-item">
                    <img src="images/categories/category_3.jpg" alt="Drama">
                    <div class="category-name">Drama</div>
                </div>
                <div class="category-item">
                    <img src="images/categories/category_4.jpg" alt="Sci-Fi">
                    <div class="category-name">Sci-Fi</div>
                </div>
                <div class="category-item">
                    <img src="images/categories/category_5.jpg" alt="Documentary">
                    <div class="category-name">Documentary</div>
                </div>
                <div class="category-item">
                    <img src="images/categories/category_6.jpg" alt="Horror">
                    <div class="category-name">Horror</div>
                </div>
            </div>
        </section>

        <section id="trending" class="section">
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
        </section>

        <section id="features" class="section">
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
        </section>

        <footer>
            <h1>NETFIX</h1>
            <p>Stream Smarter. Download Now.</p>
            <p>&copy; 2025 Netfix. All rights reserved.</p>
        </footer>
    </div>

    <script>
        // Intersection Observer to trigger animations when sections come into view
        document.addEventListener('DOMContentLoaded', function() {
            const sections = document.querySelectorAll('.section');
            
            const observer = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        entry.target.classList.add('visible');
                    }
                });
            }, {
                threshold: 0.1
            });
            
            sections.forEach(section => {
                observer.observe(section);
            });
        });
    </script>
</body>
</html>
"""

# Save the HTML file
with open("netfix_trailer_with_images.html", "w") as f:
    f.write(html_content)

print("HTML trailer page with images created: netfix_trailer_with_images.html")
