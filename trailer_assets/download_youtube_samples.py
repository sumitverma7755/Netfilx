import os
from pytube import YouTube
import time

# Create directories for different categories
categories = ['action', 'comedy', 'drama', 'scifi', 'documentary']
for category in categories:
    os.makedirs(f'clips/{category}', exist_ok=True)

# List of free, publicly available YouTube videos for each category
# These are Creative Commons or public domain videos
videos = [
    # Action
    {'url': 'https://www.youtube.com/watch?v=LXb3EKWsInQ', 'category': 'action', 'name': 'action_sample1.mp4'},
    {'url': 'https://www.youtube.com/watch?v=S_VAL4M0Cxk', 'category': 'action', 'name': 'action_sample2.mp4'},
    
    # Comedy
    {'url': 'https://www.youtube.com/watch?v=VJm7IPrBmLY', 'category': 'comedy', 'name': 'comedy_sample1.mp4'},
    {'url': 'https://www.youtube.com/watch?v=0jCr8QSGYss', 'category': 'comedy', 'name': 'comedy_sample2.mp4'},
    
    # Drama
    {'url': 'https://www.youtube.com/watch?v=EngW7tLk6R8', 'category': 'drama', 'name': 'drama_sample1.mp4'},
    {'url': 'https://www.youtube.com/watch?v=Jk8S2KFq7as', 'category': 'drama', 'name': 'drama_sample2.mp4'},
    
    # Sci-Fi
    {'url': 'https://www.youtube.com/watch?v=YoHD9XEInc0', 'category': 'scifi', 'name': 'scifi_sample1.mp4'},
    {'url': 'https://www.youtube.com/watch?v=ZSt9tm3RoUU', 'category': 'scifi', 'name': 'scifi_sample2.mp4'},
    
    # Documentary
    {'url': 'https://www.youtube.com/watch?v=6v2L2UGZJAM', 'category': 'documentary', 'name': 'documentary_sample1.mp4'},
    {'url': 'https://www.youtube.com/watch?v=jP55meT96ug', 'category': 'documentary', 'name': 'documentary_sample2.mp4'},
]

print("Starting download of video clips...")

# Download each video
for i, video in enumerate(videos):
    try:
        print(f"Downloading {video['name']} ({i+1}/{len(videos)})...")
        
        # Create the full path for saving
        save_path = os.path.join('clips', video['category'])
        
        # Download the video in the lowest resolution to save time and space
        yt = YouTube(video['url'])
        stream = yt.streams.filter(progressive=True, file_extension='mp4').order_by('resolution').first()
        
        if stream:
            # Download and rename the file
            downloaded_file = stream.download(output_path=save_path)
            new_file_path = os.path.join(save_path, video['name'])
            
            # Rename the file if needed
            if downloaded_file != new_file_path:
                if os.path.exists(new_file_path):
                    os.remove(new_file_path)
                os.rename(downloaded_file, new_file_path)
            
            print(f"Successfully downloaded {video['name']}")
        else:
            print(f"No suitable stream found for {video['name']}")
        
        # Sleep to avoid overwhelming the server
        time.sleep(1)
    except Exception as e:
        print(f"Error downloading {video['name']}: {str(e)}")

print("All downloads completed!")

# Create a simple HTML trailer page that shows the videos
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
            margin-bottom: 40px;
        }
        h1 {
            color: #E50914;
            font-size: 48px;
            margin: 0;
        }
        h2 {
            color: #E50914;
            font-size: 32px;
            margin-top: 40px;
        }
        .category {
            margin-bottom: 40px;
        }
        .video-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            margin-top: 20px;
        }
        .video-item {
            width: calc(50% - 10px);
        }
        video {
            width: 100%;
            height: auto;
            border-radius: 5px;
        }
        p {
            font-size: 18px;
            line-height: 1.6;
        }
        footer {
            text-align: center;
            margin-top: 60px;
            padding: 20px;
            border-top: 1px solid #333;
        }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1>NETFIX</h1>
            <p>Your Ultimate Streaming Experience</p>
        </header>

        <section>
            <p>Welcome to Netfix, the revolutionary streaming platform that brings you the best content across all genres. Our app is designed to provide a seamless and personalized viewing experience.</p>
        </section>

        <section class="category">
            <h2>Action</h2>
            <p>Experience heart-pounding action with our extensive collection of action movies and series.</p>
            <div class="video-container">
                <div class="video-item">
                    <video controls>
                        <source src="clips/action/action_sample1.mp4" type="video/mp4">
                        Your browser does not support the video tag.
                    </video>
                </div>
                <div class="video-item">
                    <video controls>
                        <source src="clips/action/action_sample2.mp4" type="video/mp4">
                        Your browser does not support the video tag.
                    </video>
                </div>
            </div>
        </section>

        <section class="category">
            <h2>Comedy</h2>
            <p>Laugh out loud with our selection of hilarious comedies that will brighten your day.</p>
            <div class="video-container">
                <div class="video-item">
                    <video controls>
                        <source src="clips/comedy/comedy_sample1.mp4" type="video/mp4">
                        Your browser does not support the video tag.
                    </video>
                </div>
                <div class="video-item">
                    <video controls>
                        <source src="clips/comedy/comedy_sample2.mp4" type="video/mp4">
                        Your browser does not support the video tag.
                    </video>
                </div>
            </div>
        </section>

        <section class="category">
            <h2>Drama</h2>
            <p>Immerse yourself in compelling stories with our drama collection.</p>
            <div class="video-container">
                <div class="video-item">
                    <video controls>
                        <source src="clips/drama/drama_sample1.mp4" type="video/mp4">
                        Your browser does not support the video tag.
                    </video>
                </div>
                <div class="video-item">
                    <video controls>
                        <source src="clips/drama/drama_sample2.mp4" type="video/mp4">
                        Your browser does not support the video tag.
                    </video>
                </div>
            </div>
        </section>

        <section class="category">
            <h2>Sci-Fi</h2>
            <p>Explore new worlds and futuristic concepts with our sci-fi selection.</p>
            <div class="video-container">
                <div class="video-item">
                    <video controls>
                        <source src="clips/scifi/scifi_sample1.mp4" type="video/mp4">
                        Your browser does not support the video tag.
                    </video>
                </div>
                <div class="video-item">
                    <video controls>
                        <source src="clips/scifi/scifi_sample2.mp4" type="video/mp4">
                        Your browser does not support the video tag.
                    </video>
                </div>
            </div>
        </section>

        <section class="category">
            <h2>Documentary</h2>
            <p>Discover fascinating facts and stories with our documentary collection.</p>
            <div class="video-container">
                <div class="video-item">
                    <video controls>
                        <source src="clips/documentary/documentary_sample1.mp4" type="video/mp4">
                        Your browser does not support the video tag.
                    </video>
                </div>
                <div class="video-item">
                    <video controls>
                        <source src="clips/documentary/documentary_sample2.mp4" type="video/mp4">
                        Your browser does not support the video tag.
                    </video>
                </div>
            </div>
        </section>

        <footer>
            <h1>NETFIX</h1>
            <p>Stream Smarter. Download Now.</p>
        </footer>
    </div>
</body>
</html>
"""

# Save the HTML file
with open("netfix_trailer.html", "w") as f:
    f.write(html_content)

print("HTML trailer page created: netfix_trailer.html")
