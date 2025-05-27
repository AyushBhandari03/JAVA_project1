import pandas as pd
import requests
from PIL import Image
from io import BytesIO
import math
import os

# Google Maps API Key
API_KEY = "AIzaSyCeFUDSOEz4YWEIcbytobVe2EneERPN-1c"

# Load CSV
df = pd.read_csv("Final_Points2.csv", header=None)
df.columns = ['id', 'name', 'latitude', 'longitude']
df['latitude'] = pd.to_numeric(df['latitude'], errors='coerce')
df['longitude'] = pd.to_numeric(df['longitude'], errors='coerce')
df.dropna(subset=['latitude', 'longitude'], inplace=True)

# Mercator projection functions
TILE_SIZE = 256
def latlon_to_pixels(lat, lon, zoom):
    scale = 2 ** zoom
    x = (lon + 180.0) / 360.0 * scale * TILE_SIZE
    y = (1 - math.log(math.tan(math.radians(lat)) + 1 / math.cos(math.radians(lat))) / math.pi) / 2 * scale * TILE_SIZE
    return x, y

def pixels_to_latlon(x, y, zoom):
    scale = 2 ** zoom
    lon = x / (scale * TILE_SIZE) * 360.0 - 180.0
    n = math.pi - 2.0 * math.pi * y / (scale * TILE_SIZE)
    lat = math.degrees(math.atan(math.sinh(n)))
    return lat, lon

# Bounding box in pixels
zoom = 14
tile_px = 640

top_left_x, top_left_y = latlon_to_pixels(df['latitude'].max(), df['longitude'].min(), zoom)
bottom_right_x, bottom_right_y = latlon_to_pixels(df['latitude'].min(), df['longitude'].max(), zoom)

# Align to tile boundaries
start_x = int(top_left_x // tile_px) * tile_px
end_x = int(bottom_right_x // tile_px + 1) * tile_px
start_y = int(top_left_y // tile_px) * tile_px
end_y = int(bottom_right_y // tile_px + 1) * tile_px

tiles_x = (end_x - start_x) // tile_px
tiles_y = (end_y - start_y) // tile_px

os.makedirs("tiles", exist_ok=True)
tiles = []

# Download tiles without markers
for y in range(tiles_y):
    row_imgs = []
    for x in range(tiles_x):
        pixel_x = start_x + x * tile_px + tile_px // 2
        pixel_y = start_y + y * tile_px + tile_px // 2
        center_lat, center_lon = pixels_to_latlon(pixel_x, pixel_y, zoom)

        url = (
            f"https://maps.googleapis.com/maps/api/staticmap?"
            f"center={center_lat},{center_lon}&zoom={zoom}&size={tile_px}x{tile_px}&maptype=roadmap"
            f"&key={API_KEY}"
        )
        print(f"Fetching tile {x}, {y}")
        response = requests.get(url)
        img = Image.open(BytesIO(response.content))
        img.save(f"tiles/tile_{y}_{x}.png")
        row_imgs.append(img)
    tiles.append(row_imgs)

# Stitch the final map
final_width = tiles_x * tile_px
final_height = tiles_y * tile_px
final_map = Image.new('RGB', (final_width, final_height))

for y in range(tiles_y):
    for x in range(tiles_x):
        final_map.paste(tiles[y][x], (x * tile_px, y * tile_px))

final_map.save("final.png")
final_map.show()

# Add image-based coordinates
def get_image_coordinates(lat, lon):
    px, py = latlon_to_pixels(lat, lon, zoom)
    image_x = int(px - start_x)
    image_y = int(py - start_y)
    return image_x, image_y

df['image_x'], df['image_y'] = zip(*df.apply(lambda row: get_image_coordinates(row['latitude'], row['longitude']), axis=1))

# Save enhanced CSV
df.to_csv("Final_Points_with_Image_Coordinates.csv", index=False)
print("Saved: Final_Points_with_Image_Coordinates.csv")
