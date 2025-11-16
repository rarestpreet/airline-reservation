document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const result = await UserAPI.login(email, password);
        if (result.success) {
            Utils.showSuccess('Login successful!');
            setTimeout(() => {
                Utils.navigateTo('/pages/findFlights.html');
            }, 1000);
        }
    } catch (error) {
        Utils.showError(error.message);
    }
});