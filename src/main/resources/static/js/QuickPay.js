// Handles opening the popup and populating form with user details
function openPopup() {
	const panel = document.getElementById("popupOverlay");

	const username = sessionStorage.getItem('username') || localStorage.getItem('username');
	console.log("Username from session/local storage:", username);

	if (!username) {
		alert("User not logged in");
		return;
	}

	fetch(`/v1/userdetailcontroller/summary/${username}`)
		.then(res => {
			if (!res.ok) throw new Error("Failed to fetch user details");
			return res.json();
		})
		.then(data => {
			console.log("Fetched user details for popup:", data);
			document.getElementById('debitNumber').value = data.accountNumber || '';
			document.getElementById('debitName').value = data.userName || '';
			document.getElementById('accountType').value = data.accountType || '';

			panel.classList.add('open'); // Show the popup
		})
		.catch(err => {
			console.error('Error fetching account summary:', err);
			alert("Failed to load user details");
		});
}

// Handles form submission for transferring money
document.getElementById('combinedForm').addEventListener('submit', function(e) {
	e.preventDefault();

	const fromUsername = sessionStorage.getItem('username') || localStorage.getItem('username');
	const toAccountNumber = document.getElementById('accountNumber').value;
	const amount = parseFloat(document.getElementById('amount').value);

	if (!fromUsername || !toAccountNumber || !amount) {
		alert("Please complete the form.");
		return;
	}

	fetch('/v1/userdetailcontroller/transfer', {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify({
			fromUserName: fromUsername,
			toAccountNumber: toAccountNumber,
			amount: amount
		})
	})
		.then(res => {
			if (!res.ok) throw new Error("Transfer failed");
			return res.text();
		})
		.then(response => {
			alert("Transfer successful!");
			console.log("Sending confirmation emails to sender and receiver...");
			closePopup();
		})
		.catch(error => {
			console.error("Transfer error:", error);
			alert("Failed to complete transfer.");
		});
});

// Close popup
function closePopup() {
	document.getElementById("popupOverlay").classList.remove('open');
}

// Set the username on DOM load
document.addEventListener("DOMContentLoaded", function() {
	const username = localStorage.getItem('username') || sessionStorage.getItem('username');
	if (username) {
		const userDisplay = document.getElementById('userDisplay');
		if (userDisplay) {
			userDisplay.textContent = username;
		}
	}
});

// Logout function
function logout() {
	localStorage.removeItem('username');
	sessionStorage.removeItem('username');
	window.location.href = '/index.html';
}

function addBeneficiary() {
	window.location.href = '/addBeneficiary.html';
}
