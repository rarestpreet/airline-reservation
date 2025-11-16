// Get flights from URL parameters or sessionStorage
let flights = [];
let searchCriteria = null;

async function loadFlights() {
    try {
        // Check if flights are passed via URL parameters (from search)
        const urlParams = new URLSearchParams(window.location.search);
        const source = urlParams.get('source');
        const destination = urlParams.get('destination');
        const departDate = urlParams.get('departDate');

        // Check sessionStorage for search results
        const storedFlights = sessionStorage.getItem('searchResults');
        const storedCriteria = sessionStorage.getItem('searchCriteria');

        if (source && destination && departDate) {
            // Search flights using API
            searchCriteria = {source, destination, departDate};
            showSearchInfo(searchCriteria);
            flights = await FlightAPI.searchFlights(source, destination, departDate);
        } else if (storedFlights) {
            // Use stored search results
            flights = JSON.parse(storedFlights);
            if (storedCriteria) {
                searchCriteria = JSON.parse(storedCriteria);
                showSearchInfo(searchCriteria);
            }
        } else {
            // Load all flights
            flights = await FlightAPI.getAllFlights();
        }

        displayFlights(flights);
    } catch (error) {
        console.error('Error loading flights:', error);
        Utils.showError('Failed to load flights: ' + error.message);
        document.getElementById('loadingSpinner').style.display = 'none';
        document.getElementById('noFlights').style.display = 'block';
    }
}

function showSearchInfo(criteria) {
    const searchInfo = document.getElementById('searchInfo');
    const searchCriteria = document.getElementById('searchCriteria');
    searchInfo.style.display = 'block';
    searchCriteria.innerHTML = `
                <strong>From:</strong> ${criteria.source} | 
                <strong>To:</strong> ${criteria.destination} | 
                <strong>Date:</strong> ${Utils.formatDate(criteria.departDate)}
            `;
}

function displayFlights(flights) {
    const container = document.getElementById('flightsContainer');
    const loadingSpinner = document.getElementById('loadingSpinner');
    const noFlights = document.getElementById('noFlights');

    loadingSpinner.style.display = 'none';

    if (!flights || flights.length === 0) {
        noFlights.style.display = 'block';
        return;
    }

    container.innerHTML = '';

    flights.forEach(flight => {
        const flightCard = createFlightCard(flight);
        container.appendChild(flightCard);
    });
}

function createFlightCard(flight) {
    const col = document.createElement('div');
    col.className = 'col-md-6 col-lg-4';

    const card = document.createElement('div');
    card.className = 'card flight-card';

    const header = document.createElement('div');
    header.className = 'flight-header';
    header.innerHTML = `
                <h5 class="mb-0">
                    <i class="fas fa-plane"></i> ${flight.operatingAirlines || 'Airline'}
                </h5>
                <small>Flight #${flight.flightNumber || 'N/A'}</small>
            `;

    const body = document.createElement('div');
    body.className = 'flight-info';

    // Route information
    const routeInfo = document.createElement('div');
    routeInfo.className = 'route-info';
    routeInfo.innerHTML = `
                <div>
                    <div class="city-code">${flight.departureCity || 'N/A'}</div>
                    <small class="text-muted">Departure</small>
                </div>
                <div class="arrow-icon">
                    <i class="fas fa-arrow-right"></i>
                </div>
                <div>
                    <div class="city-code">${flight.arrivalCity || 'N/A'}</div>
                    <small class="text-muted">Arrival</small>
                </div>
            `;

    // Flight details
    const details = document.createElement('div');
    details.className = 'flight-details';
    details.innerHTML = `
                <div class="detail-item">
                    <div class="detail-label">Departure Date</div>
                    <div class="detail-value">${Utils.formatDate(flight.dateOfDeparture) || 'N/A'}</div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">Departure Time</div>
                    <div class="detail-value">${Utils.formatDateTime(flight.estimatedDepartureTime) || 'N/A'}</div>
                </div>
            `;

    // Select button
    const selectBtn = document.createElement('button');
    selectBtn.className = 'btn btn-primary w-100 mt-3';
    selectBtn.innerHTML = '<i class="fas fa-check"></i> Select Flight';
    selectBtn.onclick = () => selectFlight(flight.id);

    body.appendChild(routeInfo);
    body.appendChild(details);
    body.appendChild(selectBtn);

    card.appendChild(header);
    card.appendChild(body);
    col.appendChild(card);

    return col;
}

function selectFlight(flightId) {
    // Navigate to complete reservation page
    window.location.href = `/pages/completeReservation.html?flightId=${flightId}`;
}

// Load flights when page loads
document.addEventListener('DOMContentLoaded', loadFlights);
