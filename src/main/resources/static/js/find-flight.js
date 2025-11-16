document.getElementById('searchForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const source = document.getElementById('source').value;
    const destination = document.getElementById('destination').value;
    const departDate = document.getElementById('departDate').value;

    try {
        const flights = await FlightAPI.searchFlights(source, destination, departDate);
        displayFlights(flights);
    } catch (error) {
        Utils.showError(error.message);
    }
});

function displayFlights(flights) {
    const container = document.getElementById('flightsContainer');

    if (flights.length === 0) {
        container.innerHTML = '<div class="alert alert-info">No flights found for your search criteria.</div>';
        return;
    }

    let html = '<div class="row">';
    flights.forEach(flight => {
        html += `
                    <div class="col-md-6 mb-3">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">${flight.operatingAirlines}</h5>
                                <p class="card-text">
                                    <strong>Flight Number:</strong> ${flight.flightNumber}<br>
                                    <strong>From:</strong> ${flight.departureCity}<br>
                                    <strong>To:</strong> ${flight.arrivalCity}<br>
                                    <strong>Departure Date:</strong> ${Utils.formatDate(flight.dateOfDeparture)}
                                </p>
                                <button onclick="selectFlight(${flight.id})" class="btn btn-primary">Select Flight</button>
                            </div>
                        </div>
                    </div>
                `;
    });
    html += '</div>';
    container.innerHTML = html;
}

function selectFlight(flightId) {
    Utils.navigateTo(`/pages/completeReservation.html?flightId=${flightId}`);
}