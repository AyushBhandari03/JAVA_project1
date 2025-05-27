pip install googlemaps

import pandas as pd
import googlemaps
import time

# === CONFIGURATION ===
GOOGLE_API_KEY = 'AIzaSyCSorWsucWnOIhE8Z0rcIBUGc-Myfp2eas'  # Replace with your actual key
INPUT_FILE = '/content/map_points1.csv'
OUTPUT_FILE = 'new_matrix.csv'
BATCH_LIMIT = 10  # 10x10 = 100 elements per API request

# === STEP 1: Load and Clean Locations ===
df = pd.read_csv(INPUT_FILE, header=0)  # First row is header in your file
locations = []

for index, row in df.iterrows():
    try:
        loc_id = str(row[0]).strip()
        name = str(row[1]).strip()
        lat = float(row[2])
        lon = float(row[3])
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
