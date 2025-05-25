import pandas as pd
import googlemaps
import time
import math

# === CONFIGURATION ===
GOOGLE_API_KEY = 'AIzaSyDKEeeuLicdClFhYahkSRV5_lUZKfjyuyU'  # Replace this with your actual key
INPUT_FILE = '/content/Final_Points.csv'
OUTPUT_FILE = 'batched_road_distance_matrix.csv'
BATCH_LIMIT = 10  # You can do 10x10 requests (100 elements max)

# === STEP 1: Load and Clean Locations ===
df = pd.read_csv(INPUT_FILE, header=None)
locations = []

for index, row in df.iterrows():
    try:
        loc_id = row[0].strip()
        name = row[1].strip()
        coords = row[2].replace('°', '').replace('�', '').replace('N', '').replace('E', '').split(',')
        lat = float(coords[0].strip())
        lon = float(coords[1].strip())
        locations.append((loc_id, name, lat, lon))
    except Exception as e:
        print(f"Skipping row {index} due to error: {e}")

locations_df = pd.DataFrame(locations, columns=["LocationID", "Name", "Latitude", "Longitude"])
coordinates = [f"{lat},{lon}" for lat, lon in zip(locations_df['Latitude'], locations_df['Longitude'])]
location_ids = locations_df["LocationID"].tolist()
n = len(location_ids)

# === STEP 2: Initialize Google Maps Client ===
gmaps = googlemaps.Client(key=GOOGLE_API_KEY)

# === STEP 3: Prepare empty distance matrix ===
distance_matrix = pd.DataFrame(index=location_ids, columns=location_ids)

# === STEP 4: Batch Process Distances ===
print(f"Calculating distances for {n} locations in batches...")

for i_start in range(0, n, BATCH_LIMIT):
    i_end = min(i_start + BATCH_LIMIT, n)
    origins = coordinates[i_start:i_end]
    origin_ids = location_ids[i_start:i_end]

    for j_start in range(0, n, BATCH_LIMIT):
        j_end = min(j_start + BATCH_LIMIT, n)
        destinations = coordinates[j_start:j_end]
        dest_ids = location_ids[j_start:j_end]

        try:
            result = gmaps.distance_matrix(
                origins=origins,
                destinations=destinations,
                mode="driving"
            )
        except Exception as e:
            print(f"API error between batch {i_start}-{i_end} and {j_start}-{j_end}: {e}")
            continue

        # Fill matrix
        for i, origin_id in enumerate(origin_ids):
            for j, dest_id in enumerate(dest_ids):
                element = result['rows'][i]['elements'][j]
                if element['status'] == 'OK':
                    distance_km = round(element['distance']['value'] / 1000.0, 2)
                else:
                    distance_km = None
                distance_matrix.at[origin_id, dest_id] = distance_km

        time.sleep(1)  # Avoid hitting rate limits

# === STEP 5: Save to CSV ===
distance_matrix.reset_index(inplace=True)
distance_matrix.rename(columns={"index": "LocationID"}, inplace=True)
distance_matrix.to_csv(OUTPUT_FILE, index=False)

print(f"✅ Road distance matrix saved to: {OUTPUT_FILE}")
