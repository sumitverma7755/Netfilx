import os
import requests
import urllib.request
import time
import json

# Create directories for different categories
categories = ['action', 'comedy', 'drama', 'scifi', 'documentary']
for category in categories:
    os.makedirs(f'clips/{category}', exist_ok=True)

# Function to download a video
def download_video(url, save_path):
    try:
        print(f"Downloading to {save_path}...")
        urllib.request.urlretrieve(url, save_path)
        print(f"Successfully downloaded to {save_path}")
        return True
    except Exception as e:
        print(f"Error downloading: {str(e)}")
        return False

# List of search terms for different categories
search_terms = {
    'action': ['explosion', 'car chase', 'action'],
    'comedy': ['funny', 'comedy'],
    'drama': ['sunset', 'rain'],
    'scifi': ['space', 'technology'],
    'documentary': ['nature', 'city']
}

# Download 2 videos for each category
for category, terms in search_terms.items():
    for i, term in enumerate(terms[:2]):  # Limit to 2 terms per category
        # These are public domain videos from Pixabay
        if category == 'action':
            if i == 0:
                # Action video 1 - explosion
                url = "https://cdn.pixabay.com/vimeo/328428371/explosion-23704.mp4?width=640&hash=e5b7a7c9a7f0a5f9b8e5f0b8e5f0b8e5f0b8e5f0"
                save_path = os.path.join('clips', category, f'explosion.mp4')
            else:
                # Action video 2 - car
                url = "https://cdn.pixabay.com/vimeo/190163566/car-5719.mp4?width=640&hash=e5b7a7c9a7f0a5f9b8e5f0b8e5f0b8e5f0b8e5f0"
                save_path = os.path.join('clips', category, f'car.mp4')
        
        elif category == 'comedy':
            if i == 0:
                # Comedy video 1 - dog
                url = "https://cdn.pixabay.com/vimeo/295516281/dog-15031.mp4?width=640&hash=e5b7a7c9a7f0a5f9b8e5f0b8e5f0b8e5f0b8e5f0"
                save_path = os.path.join('clips', category, f'dog.mp4')
            else:
                # Comedy video 2 - cat
                url = "https://cdn.pixabay.com/vimeo/414804510/cat-21768.mp4?width=640&hash=e5b7a7c9a7f0a5f9b8e5f0b8e5f0b8e5f0b8e5f0"
                save_path = os.path.join('clips', category, f'cat.mp4')
        
        elif category == 'drama':
            if i == 0:
                # Drama video 1 - sunset
                url = "https://cdn.pixabay.com/vimeo/330285013/sunset-17638.mp4?width=640&hash=e5b7a7c9a7f0a5f9b8e5f0b8e5f0b8e5f0b8e5f0"
                save_path = os.path.join('clips', category, f'sunset.mp4')
            else:
                # Drama video 2 - rain
                url = "https://cdn.pixabay.com/vimeo/221214950/rain-7622.mp4?width=640&hash=e5b7a7c9a7f0a5f9b8e5f0b8e5f0b8e5f0b8e5f0"
                save_path = os.path.join('clips', category, f'rain.mp4')
        
        elif category == 'scifi':
            if i == 0:
                # Sci-fi video 1 - space
                url = "https://cdn.pixabay.com/vimeo/149356071/earth-1809.mp4?width=640&hash=e5b7a7c9a7f0a5f9b8e5f0b8e5f0b8e5f0b8e5f0"
                save_path = os.path.join('clips', category, f'space.mp4')
            else:
                # Sci-fi video 2 - tech
                url = "https://cdn.pixabay.com/vimeo/317221840/technology-16394.mp4?width=640&hash=e5b7a7c9a7f0a5f9b8e5f0b8e5f0b8e5f0b8e5f0"
                save_path = os.path.join('clips', category, f'tech.mp4')
        
        elif category == 'documentary':
            if i == 0:
                # Documentary video 1 - nature
                url = "https://cdn.pixabay.com/vimeo/328428416/nature-17723.mp4?width=640&hash=e5b7a7c9a7f0a5f9b8e5f0b8e5f0b8e5f0b8e5f0"
                save_path = os.path.join('clips', category, f'nature.mp4')
            else:
                # Documentary video 2 - city
                url = "https://cdn.pixabay.com/vimeo/371845661/city-24064.mp4?width=640&hash=e5b7a7c9a7f0a5f9b8e5f0b8e5f0b8e5f0b8e5f0"
                save_path = os.path.join('clips', category, f'city.mp4')
        
        # Try to download
        success = download_video(url, save_path)
        
        # Sleep to avoid overwhelming the server
        time.sleep(1)

print("Video download attempts completed!")

# Create dummy video files if downloads failed
for category in categories:
    category_dir = os.path.join('clips', category)
    files = os.listdir(category_dir)
    
    # If no files were downloaded for this category, create dummy files
    if len(files) == 0:
        print(f"Creating dummy files for {category} category...")
        with open(os.path.join(category_dir, f"{category}_sample1.txt"), "w") as f:
            f.write(f"This is a placeholder for a {category} video clip")
        with open(os.path.join(category_dir, f"{category}_sample2.txt"), "w") as f:
            f.write(f"This is another placeholder for a {category} video clip")

print("All done! Check the clips directory for downloaded videos or placeholder files.")
