//logout function
function logout() {
	localStorage.removeItem('username');
	sessionStorage.removeItem('username');
	window.location.href = '/index.html';
}

function toggleAccountSummary() {
	alert("Toggling Account Summary...");
	// Your account summary toggle logic
}

// Populate username in header
document.addEventListener("DOMContentLoaded", function() {
	const username = localStorage.getItem('username') || sessionStorage.getItem('username');
	if (username) {
		const userDisplay = document.getElementById('userDisplay');
		if (userDisplay) {
			userDisplay.textContent = username;
		}
		loadBeneficiaries(username); // Load saved beneficiaries
	} else {
		alert("User not logged in. Redirecting to login page.");
		window.location.href = '/index.html';
	}
});

function loadBeneficiaries(username) {
	fetch(`/v1/beneficiaries/user/${username}`)
		.then(response => {
			if (!response.ok) throw new Error("Failed to fetch beneficiaries");
			return response.json();
		})
		.then(beneficiaries => {
			const tableBody = document.getElementById("beneficiaryTable").getElementsByTagName("tbody")[0];
			tableBody.innerHTML = ""; // Clear existing rows
			beneficiaries.forEach(b => {
				const row = tableBody.insertRow();
				row.insertCell(0).innerText = b.name;
				row.insertCell(1).innerText = b.accountNumber;
				row.insertCell(2).innerText = b.ifscCode;

				const actionCell = row.insertCell(3);
				const payButton = document.createElement("button");
				payButton.textContent = "Pay";
				payButton.onclick = function() {
					alert(`Proceeding to pay ${b.name}`);
					// Add navigation to payment page if needed
				};
				actionCell.appendChild(payButton);
			});
		})
		.catch(error => {
			console.error("Error loading beneficiaries:", error);
			alert("Unable to load beneficiaries.");
		});
}

function addBeneficiary() {
	const name = document.getElementById("name").value.trim();
	const accountNumber = document.getElementById("accountNumber").value.trim();
	const reAccountNumber = document.getElementById("reAccountNumber").value.trim();
	const ifsc = document.getElementById("ifsc").value.trim();
	const addedByUserName = sessionStorage.getItem("username") || localStorage.getItem("username");

	if (!name || !accountNumber || !reAccountNumber || !ifsc || !addedByUserName) {
		alert("Please fill all fields.");
		return;
	}

	if (accountNumber !== reAccountNumber) {
		alert("Account numbers do not match.");
		return;
	}

	const beneficiaryData = {
		name: name,
		accountNumber: accountNumber,
		ifscCode: ifsc,
		addedByUserName: addedByUserName
	};

	fetch("/v1/beneficiaries/add", {
		method: "POST",
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify(beneficiaryData)
	})
		.then(response => {
			if (!response.ok) {
				throw new Error("Failed to add beneficiary");
			}
			return response.text();
		})
		.then(message => {
			alert(message);
			loadBeneficiaries(addedByUserName); // Reload updated list
			document.getElementById("name").value = "";
			document.getElementById("accountNumber").value = "";
			document.getElementById("reAccountNumber").value = "";
			document.getElementById("ifsc").value = "";
		})
		.catch(error => {
			console.error("Error:", error);
			alert("Failed to add beneficiary. Please try again.");
		});
}
