const urlParams = new URLSearchParams(window.location.search);
const reservationId = urlParams.get('reservationId');

if (reservationId) {
    document.getElementById('reservationMessage').textContent =
        `Your reservation ID is: ${reservationId}. A confirmation email has been sent to your email address.`;
}