document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const userData = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value
    };

    try {
        const result = await UserAPI.register(userData);
        if (result.success) {
            Utils.showSuccess('Registration successful! Please login.');
            setTimeout(() => {
                Utils.navigateTo('/pages/login.html');
            }, 2000);
        }
    } catch (error) {
        Utils.showError(error.message);
    }
});