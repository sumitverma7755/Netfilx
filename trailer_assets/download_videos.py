import os
import requests
import urllib.request
import time

# Create directories for different categories
categories = ['action', 'comedy', 'drama', 'scifi', 'documentary']
for category in categories:
    os.makedirs(f'clips/{category}', exist_ok=True)

# List of free stock videos from Pexels (direct download links)
# These are sample videos that could represent different movie categories
videos = [
    # Action clips
    {'url': 'https://player.vimeo.com/external/371433846.sd.mp4?s=236da2f62a23f90d5b1b2a7b59fd8c5c6f0bdd0e&profile_id=139&oauth2_token_id=57447761', 'category': 'action', 'name': 'car_chase.mp4'},
    {'url': 'https://player.vimeo.com/external/403295710.sd.mp4?s=788b046826f92983ada6e5caf067113fdb49e209&profile_id=165&oauth2_token_id=57447761', 'category': 'action', 'name': 'explosion.mp4'},
    
    # Comedy clips
    {'url': 'https://player.vimeo.com/external/363625327.sd.mp4?s=a287d2557de92e1fb2dd0cc31d5b7c9a4b5765d9&profile_id=139&oauth2_token_id=57447761', 'category': 'comedy', 'name': 'funny_dog.mp4'},
    {'url': 'https://player.vimeo.com/external/368320203.sd.mp4?s=38d3553d0e39f8c5b91b3fa7799bc6b7b7b22fef&profile_id=139&oauth2_token_id=57447761', 'category': 'comedy', 'name': 'funny_cat.mp4'},
    
    # Drama clips
    {'url': 'https://player.vimeo.com/external/363629595.sd.mp4?s=d3765e92a7f991e5a1d3d0b3d54ba4c2ef137be7&profile_id=139&oauth2_token_id=57447761', 'category': 'drama', 'name': 'sunset_walk.mp4'},
    {'url': 'https://player.vimeo.com/external/368484050.sd.mp4?s=ddd9a9a0d7f9b229912df4c4ac09e98b0adf0b6d&profile_id=139&oauth2_token_id=57447761', 'category': 'drama', 'name': 'rain_window.mp4'},
    
    # Sci-Fi clips
    {'url': 'https://player.vimeo.com/external/330412624.sd.mp4?s=388fd9c94a87e4a38aafd06e9faff10fd921ad8d&profile_id=139&oauth2_token_id=57447761', 'category': 'scifi', 'name': 'space.mp4'},
    {'url': 'https://player.vimeo.com/external/317221840.sd.mp4?s=2c677cf5e5f10aa2bf4b14c5ddc224c4ca3d5d5e&profile_id=165&oauth2_token_id=57447761', 'category': 'scifi', 'name': 'tech_interface.mp4'},
    
    # Documentary clips
    {'url': 'https://player.vimeo.com/external/328428416.sd.mp4?s=39df9f60fdeaeff0f4a6286c490a46f7552176f8&profile_id=165&oauth2_token_id=57447761', 'category': 'documentary', 'name': 'nature.mp4'},
    {'url': 'https://player.vimeo.com/external/371845661.sd.mp4?s=1ef39e3c9f7d4f4ec2e35908da448fb3498ec30d&profile_id=139&oauth2_token_id=57447761', 'category': 'documentary', 'name': 'cityscape.mp4'},
]

print("Starting download of video clips...")

# Download each video
for i, video in enumerate(videos):
    try:
        print(f"Downloading {video['name']} ({i+1}/{len(videos)})...")
        
        # Create the full path for saving
        save_path = os.path.join('clips', video['category'], video['name'])
        
        # Download the video
        urllib.request.urlretrieve(video['url'], save_path)
        
        print(f"Successfully downloaded {video['name']}")
        
        # Sleep to avoid overwhelming the server
        time.sleep(1)
    except Exception as e:
        print(f"Error downloading {video['name']}: {str(e)}")

print("All downloads completed!")
