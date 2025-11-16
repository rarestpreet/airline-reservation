// API Base URL
const API_BASE_URL = '/flightreservation/api';

// Flight API
const FlightAPI = {
    async searchFlights(source, destination, departDate) {
        try {
            const response = await fetch(
                `${API_BASE_URL}/flights/search?source=${encodeURIComponent(source)}&destination=${encodeURIComponent(destination)}&departDate=${departDate}`,
                {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    credentials: 'include'
                }
            );

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || 'Failed to search flights');
            }

            return await response.json();
        } catch (error) {
            console.error('Error searching flights:', error);
            throw error;
        }
    },

    async getFlight(flightId) {
        try {
            const response = await fetch(`${API_BASE_URL}/flights/${flightId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Failed to get flight');
            }

            return await response.json();
        } catch (error) {
            console.error('Error getting flight:', error);
            throw error;
        }
    },

    async getAllFlights() {
        try {
            const response = await fetch(`${API_BASE_URL}/flights`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Failed to get flights');
            }

            return await response.json();
        } catch (error) {
            console.error('Error getting flights:', error);
            throw error;
        }
    }
};

// User API
const UserAPI = {
    async register(userData) {
        try {
            const response = await fetch(`${API_BASE_URL}/users/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify(userData)
            });

            const result = await response.json();

            if (!response.ok) {
                throw new Error(result.message || 'Registration failed');
            }

            return result;
        } catch (error) {
            console.error('Error registering user:', error);
            throw error;
        }
    },

    async login(email, password) {
        try {
            const response = await fetch(`${API_BASE_URL}/users/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify({ email, password })
            });

            const result = await response.json();

            if (!response.ok) {
                throw new Error(result.message || 'Login failed');
            }

            return result;
        } catch (error) {
            console.error('Error logging in:', error);
            throw error;
        }
    }
};

// Reservation API
const ReservationAPI = {
    async createReservation(reservationData) {
        try {
            const response = await fetch(`${API_BASE_URL}/reservations`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify(reservationData)
            });

            const result = await response.json();

            if (!response.ok) {
                throw new Error(result.message || 'Reservation failed');
            }

            return result;
        } catch (error) {
            console.error('Error creating reservation:', error);
            throw error;
        }
    },

    async getReservation(reservationId) {
        try {
            const response = await fetch(`${API_BASE_URL}/reservations/${reservationId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Failed to get reservation');
            }

            return await response.json();
        } catch (error) {
            console.error('Error getting reservation:', error);
            throw error;
        }
    }
};