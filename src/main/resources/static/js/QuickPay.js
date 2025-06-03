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
	const reAccountNumber = document.getElementById('reAccountNumber').value;

	if (!fromUsername || !toAccountNumber || !reAccountNumber || isNaN(amount)) {
		alert("Please complete the form.");
		return;
	}

	if (toAccountNumber !== reAccountNumber) {
		alert("Account numbers do not  match");
		return;
	}

	if (amount > 5000) {
		alert("Transfer amount cannot exceed ₹5000 for a new beneficiary.");
		return;
	}

	if (amount <= 0) {
		alert("please enter a valid amount greater than 0");
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

// Set the username and populate beneficiaries on DOM load
document.addEventListener("DOMContentLoaded", function() {
	const username = localStorage.getItem('username') || sessionStorage.getItem('username');

	if (username) {
		const userDisplay = document.getElementById('userDisplay');
		if (userDisplay) {
			userDisplay.textContent = username;
		}

		// Fetch and populate beneficiaries in the dropdown with id="name"
		fetch(`/v1/beneficiaries/user/${username}`)
			.then(res => res.json())
			.then(data => {
				const dropdown = document.getElementById("name");
				dropdown.options.length = 1;

				data.forEach(b => {
					const option = document.createElement("option");
					option.value = JSON.stringify(b);
					option.text = `${b.name} - ${b.accountNumber}`;
					dropdown.appendChild(option);
				});
			})
			.catch(err => {
				console.error("Error fetching beneficiaries:", err);
				alert("Could not load saved beneficiaries.");
			});
	}
});

// Populate form fields when a beneficiary is selected
document.getElementById("name").addEventListener("change", function() {
	const selected = this.value;
	if (!selected) return;

	try {
		const beneficiary = JSON.parse(selected);
		document.getElementById("accountNumber").value = beneficiary.accountNumber;
		document.getElementById("reAccountNumber").value = beneficiary.accountNumber;
		/*if (beneficiary.ifscCode) {
			const ifscInput = document.getElementById("ifscCode");
			if (ifscInput) ifscInput.value = beneficiary.ifscCode;
		}*/
	} catch (err) {
		console.error("Error parsing beneficiary:", err);
	}
});

//close popup
function closePopup() {
	document.getElementById("popupOverlay").classList.remove('open');
	// Clear new beneficiary fields
	document.getElementById('name').value='';
	document.getElementById('accountNumber').value = '';
	document.getElementById('reAccountNumber').value = '';
	document.getElementById('amount').value = '';

	// If you have an IFSC input, clear it too (optional)
	const ifscInput = document.getElementById('ifscCode');
	if (ifscInput) ifscInput.value = '';
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
