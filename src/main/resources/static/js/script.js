function showSignUp() {
	document.getElementById("loginContainer").classList.add("hidden");
	document.getElementById("signUpContainer").classList.remove("hidden");
}

function showLogin() {
	document.getElementById("signUpContainer").classList.add("hidden");
	document.getElementById("loginContainer").classList.remove("hidden");
}

document.getElementById("signUpFormElement").addEventListener("submit", function(event) {
	event.preventDefault(); // Prevent normal form submission

	const formData = new FormData(this);

	fetch("/v1/registrationcontroller/signup", {
		method: "POST",
		body: formData
	})
		.then(async response => {
			if (!response.ok) {
				// Try to extract and format validation error messages
				const errorData = await response.json();
				const errorMessages = Object.entries(errorData)
					.map(([field, message]) => `${field}: ${message}`)
					.join("\n");

				alert("Validation Errors:\n" + errorMessages);
				throw new Error("Validation failed");
			}
			return response.json();
		})
		.then(data => {
			console.log("Registration successful:", data);
			/*alert("Registration Successful!");*/
			showSuccessMessage(); // Optional: your existing success handler
		})
		.catch(error => {
			console.error("Error:", error);
		});
});


function showSuccessMessage() {
	const messageDiv = document.getElementById("successMessage");
	messageDiv.classList.remove("hidden");
	setTimeout(() => {
		messageDiv.classList.add("hidden");
		showLogin();
	}, 500);
}

document.addEventListener("DOMContentLoaded", () => {
	const urlParams = new URLSearchParams(window.location.search);
	if (urlParams.get("error") === "true") {
		document.getElementById("loginError").style.display = "block";
	}
})