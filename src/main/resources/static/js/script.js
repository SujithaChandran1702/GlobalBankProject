function showSignUp() {
	document.getElementById("loginContainer").classList.add("hidden");
	document.getElementById("signUpContainer").classList.remove("hidden");
}

function showLogin() {
	document.getElementById("signUpContainer").classList.add("hidden");
	document.getElementById("loginContainer").classList.remove("hidden");
}

document.getElementById("signUpFormElement").addEventListener("submit", function(event) {
	event.preventDefault();

	const form = event.target;

	const data = {
		userName: form.userName.value,
		password: form.password.value,
		email: form.email.value,
		phoneNumber: form.phoneNumber.value,
		panNumber: form.panNumber.value
	};

	fetch("/v1/registrationcontroller/signup", {
		method: "POST",
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify(data)
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


/*document.getElementById("loginForm").addEventListener("submit", function (e) {
  e.preventDefault();

  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  fetch("/v1/pagecontroller/apilogin", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ userName: username, password: password })
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error("Login failed");
      }
      return res.json();
    })
    .then((response) => {
      // Store username in sessionStorage
      sessionStorage.setItem("username", response.username);

      // Redirect to Quick Pay page or dashboard
      window.location.href = "/quick-pay.html";
    })
    .catch((error) => {
      console.error("Error during login:", error);
      alert("Invalid username or password");
    });
});*/


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