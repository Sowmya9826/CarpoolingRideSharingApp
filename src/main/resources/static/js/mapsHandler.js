<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Create Ride</title>
  <!-- Google Fonts -->
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
  
  <!-- Include the config.js file -->
  <script src="/js/config.js"></script> <!-- Make sure this path is correct -->

  <style>
    #map {
      height: 400px;
      width: 100%;
      margin-bottom: 20px;
    }
    .hidden {
      display: none;
    }
  </style>

  <script>
    let map, startMarker, endMarker;

    function initMap() {
      map = new google.maps.Map(document.getElementById('map'), {
        center: { lat: 37.7749, lng: -122.4194 }, // Default center: San Francisco
        zoom: 12
      });
    }

    function setLocation(type) {
      map.addListener('click', function(event) {
        const lat = event.latLng.lat();
        const lng = event.latLng.lng();
        
        if (type === 'start') {
          if (startMarker) startMarker.setMap(null);
          startMarker = new google.maps.Marker({
            position: event.latLng,
            map: map,
            label: 'S'
          });
          document.getElementById('startLatitude').value = lat;
          document.getElementById('startLongitude').value = lng;
        } else if (type === 'end') {
          if (endMarker) endMarker.setMap(null);
          endMarker = new google.maps.Marker({
            position: event.latLng,
            map: map,
            label: 'D'
          });
          document.getElementById('endLatitude').value = lat;
          document.getElementById('endLongitude').value = lng;
        }

        calculateFare();
      });
    }

    function calculateFare() {
      const startLat = parseFloat(document.getElementById('startLatitude').value);
      const startLng = parseFloat(document.getElementById('startLongitude').value);
      const endLat = parseFloat(document.getElementById('endLatitude').value);
      const endLng = parseFloat(document.getElementById('endLongitude').value);

      if (isNaN(startLat) || isNaN(startLng) || isNaN(endLat) || isNaN(endLng)) return;

      const url = `/fares/estimateFare?startLatitude=${startLat}&startLongitude=${startLng}&endLatitude=${endLat}&endLongitude=${endLng}`;
      
      fetch(url)
        .then(response => response.json())
        .then(data => {
          document.getElementById('fareAmount').innerText = `Estimated Fare: $${data.toFixed(2)}`;
        })
        .catch(error => {
          console.error('Error calculating fare:', error);
        });
    }

    function loadGoogleMapsScript() {
      const script = document.createElement('script');
      // Dynamically load the Google Maps API using the API key from config.js
      script.src = `https://maps.googleapis.com/maps/api/js?key=${GOOGLE_MAPS_API_KEY}&callback=initMap`;
      script.defer = true;
      document.head.appendChild(script);
    }

    window.onload = loadGoogleMapsScript;
  </script>
</head>
<body>
  <div class="container mt-5">
    <h1>Create Ride</h1>

    <form action="/rides/create" method="post">
      <!-- Map -->
      <div id="map"></div>

      <!-- Start Location Inputs -->
      <div class="mb-3">
        <label for="startAddress" class="form-label">Start Address:</label>
        <input type="text" id="startAddress" class="form-control" placeholder="Enter Start Address" required>
        <button type="button" class="btn btn-primary" onclick="setLocation('start')">Set Start</button>
        <input type="hidden" id="startLatitude" name="startLatitude">
        <input type="hidden" id="startLongitude" name="startLongitude">
      </div>

      <!-- Destination Location Inputs -->
      <div class="mb-3">
        <label for="endAddress" class="form-label">Destination Address:</label>
        <input type="text" id="endAddress" class="form-control" placeholder="Enter Destination Address" required>
        <button type="button" class="btn btn-primary" onclick="setLocation('end')">Set Destination</button>
        <input type="hidden" id="endLatitude" name="endLatitude">
        <input type="hidden" id="endLongitude" name="endLongitude">
      </div>

      <!-- Estimated Fare -->
      <div>
        <label>Estimated Fare:</label>
        <div id="fareAmount">--</div>
      </div>

      <div class="text-center">
        <button type="submit" class="btn btn-primary">Create Ride</button>
      </div>
    </form>
  </div>
</body>
</html>
