let map;
let startMarker = null;
let endMarker = null;

function loadGoogleMapsScript() {
  // Dynamically load the Google Maps script
  const script = document.createElement("script");
  script.src = `https://maps.googleapis.com/maps/api/js?key=${GOOGLE_MAPS_API_KEY}&callback=initMap`;
  script.defer = true;
  document.head.appendChild(script);
}

function initMap() {
  map = new google.maps.Map(document.getElementById("map"), {
    center: { lat: 37.7749, lng: -122.4194 }, // Default center (San Francisco)
    zoom: 10,
  });

  let activeListener = null;

  function setMapClickListener(callback) {
    // Remove previous listener, if any
    if (activeListener) {
      google.maps.event.removeListener(activeListener);
    }

    // Add new listener
    activeListener = map.addListener("click", callback);
  }

  // Click listener for setting start location
  document.getElementById("setStart").addEventListener("click", () => {
    console.log("Setting start location");
    setMapClickListener((event) => {
      const lat = event.latLng.lat();
      const lng = event.latLng.lng();

      if (startMarker) {
        startMarker.setMap(null);
      }

      startMarker = new google.maps.Marker({
        position: event.latLng,
        map: map,
        label: "S",
      });

      document.getElementById("startLatitude").value = lat;
      document.getElementById("startLongitude").value = lng;
      getAddress(lat, lng, "startAddress");
    });
  });

  // Click listener for setting destination location
  document.getElementById("setDestination").addEventListener("click", () => {
    setMapClickListener((event) => {
      const lat = event.latLng.lat();
      const lng = event.latLng.lng();

      if (endMarker) {
        endMarker.setMap(null);
      }

      endMarker = new google.maps.Marker({
        position: event.latLng,
        map: map,
        label: "D",
      });

      document.getElementById("endLatitude").value = lat;
      document.getElementById("endLongitude").value = lng;
      getAddress(lat, lng, "endAddress");
    });
  });
}

// Geocode the address entered by the user and place the marker on the map
function geocodeAddress(address, markerType) {
  const geocoder = new google.maps.Geocoder();
  geocoder.geocode({ address: address }, function(results, status) {
    if (status === "OK") {
      const latLng = results[0].geometry.location;
      const lat = latLng.lat();
      const lng = latLng.lng();

      // Place marker on the map based on the address
      if (markerType === "start") {
        if (startMarker) {
          startMarker.setMap(null);
        }
        startMarker = new google.maps.Marker({
          position: latLng,
          map: map,
          label: "S",
        });
        document.getElementById("startLatitude").value = lat;
        document.getElementById("startLongitude").value = lng;
        document.getElementById("startAddress").value = results[0].formatted_address;
      } else if (markerType === "destination") {
        if (endMarker) {
          endMarker.setMap(null);
        }
        endMarker = new google.maps.Marker({
          position: latLng,
          map: map,
          label: "D",
        });
        document.getElementById("endLatitude").value = lat;
        document.getElementById("endLongitude").value = lng;
        document.getElementById("endAddress").value = results[0].formatted_address;
      }

      map.setCenter(latLng); // Center the map on the address
    } else {
      alert("Geocoding failed: " + status);
    }
  });
}

// Function to get the address from latitude and longitude
function getAddress(lat, lng, addressFieldId) {
  const geocoder = new google.maps.Geocoder();
  const latLng = { lat: parseFloat(lat), lng: parseFloat(lng) };

  geocoder.geocode({ location: latLng }, (results, status) => {
    if (status === "OK" && results[0]) {
      document.getElementById(addressFieldId).value = results[0].formatted_address;
    } else {
      console.error("Geocoding failed: " + status);
    }
  });
}

// Load the Google Maps script
loadGoogleMapsScript();
