// Get flight ID from URL
const urlParams = new URLSearchParams(window.location.search);
const flightId = urlParams.get('flightId');

// Load flight information
async function loadFlight() {
    try {
        const flight = await FlightAPI.getFlight(flightId);
        document.getElementById('flightInfo').innerHTML = `
                    <div class="alert alert-info">
                        <h5>Flight Details</h5>
                        <p><strong>${flight.operatingAirlines}</strong> - ${flight.flightNumber}</p>
                        <p>${flight.departureCity} → ${flight.arrivalCity}</p>
                        <p>Departure: ${Utils.formatDate(flight.dateOfDeparture)}</p>
                    </div>
                `;
    } catch (error) {
        Utils.showError('Failed to load flight information');
    }
}

loadFlight();

// Handle reservation form submission
document.getElementById('reservationForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const reservationData = {
        flightId: parseInt(flightId),
        passengerFirstName: document.getElementById('passengerFirstName').value,
        passengerMiddleName: document.getElementById('passengerMiddleName').value,
        passengerLastName: document.getElementById('passengerLastName').value,
        passengerEmail: document.getElementById('passengerEmail').value,
        passengerPhone: document.getElementById('passengerPhone').value,
        nameOnTheCard: document.getElementById('nameOnCard').value,
        cardNumber: document.getElementById('cardNumber').value,
        expirationDate: document.getElementById('expirationDate').value,
        securityCode: document.getElementById('securityCode').value
    };

    try {
        const result = await ReservationAPI.createReservation(reservationData);
        if (result.success) {
            Utils.navigateTo(`/pages/reservation-confirmation.html?reservationId=${result.reservationId}`);
        }
    } catch (error) {
        Utils.showError(error.message);
    }
});