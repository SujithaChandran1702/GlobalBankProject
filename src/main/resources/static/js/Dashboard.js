// Show username from localStorage
document.addEventListener("DOMContentLoaded", () => {
	const urlParams = new URLSearchParams(window.location.search);
	const urlUsername = urlParams.get("username");

	if (urlUsername) {
		localStorage.setItem("username", urlUsername);
	}

	const user = localStorage.getItem("username") || "User";
	document.getElementById("userDisplay").textContent = user;
});


// Toggle balance panel
function toggleBalance() {
	console.log("Toggling balance");
	document.getElementById("balancePanel").classList.toggle("open");
}

// Toggle history panel
/*function toggleHistory() {
	console.log("Toggling history");
	window.location.href = "history.html";
}*/

function goToHistory() {
	console.log("Going to History page");
	window.location.href = "history.html";
}

// Logout function
function logout() {
	localStorage.removeItem("username");
	window.location.href = "index.html";
}


document.addEventListener("DOMContentLoaded", () => {
	const user = localStorage.getItem("username");
	document.getElementById("userDisplay").textContent = user;

	fetch(`/v1/pagecontroller/balance?userName=${user}`)
		.then(res => res.json())
		.then(data => {
			const amount = data.balance;
			document.querySelector("#balancePanel p").textContent = `₹ ${amount.toLocaleString("en-IN")}`;
		})
		.catch(err => console.error("Failed to fetch balance:", err));
});

function showAccountDetails() {
	const user = localStorage.getItem("username") || "User";
	fetch(`/v1/pagecontroller/account-details?userName=${user}`)
		.then(res => res.json())
		.then(data => {
			document.getElementById("holderName").textContent = data.holderName;
			document.getElementById("accountNumber").textContent = data.accountNumber;
			document.getElementById("ifscCode").textContent = data.ifscCode;
			document.getElementById("accountType").textContent = data.accountType;
			document.getElementById("panNumber").textContent = data.pan;
			document.getElementById("email").textContent = data.email;
			document.getElementById("phoneNumber").textContent = data.phone;

			if (data.accountNumber) {
				localStorage.setItem("accountNumber", data.accountNumber);
				console.log("Stored accountNumber in localStorage:", data.accountNumber);
			} else {
				console.warn("Account number not found in data");
			}

			document.getElementById("accountDetails").style.display = "block";
		})
		.catch(err => console.error("Failed to load account details:", err));
}

// Toggle Account Summary Panel
const accountSummaryBtn = document.getElementById("accountSummaryBtn");
const accountSummaryPanel = document.getElementById("accountSummaryPanel");
const accountSummaryCloseBtn = document.getElementById("accountSummaryCloseBtn");

accountSummaryBtn.addEventListener("click", () => {
	accountSummaryPanel.classList.toggle("open");
});

accountSummaryCloseBtn.addEventListener("click", () => {
	accountSummaryPanel.classList.remove("open");
});

// Show username from localStorage and fetch balance
document.addEventListener("DOMContentLoaded", () => {
	const urlParams = new URLSearchParams(window.location.search);
	const urlUsername = urlParams.get("username");

	if (urlUsername) {
		localStorage.setItem("username", urlUsername);
	}

	const user = localStorage.getItem("username") || "User";
	document.getElementById("userDisplay").textContent = user;

	// Fetch balance
	fetch(`/v1/pagecontroller/balance?userName=${user}`)
		.then(res => res.json())
		.then(data => {
			const amount = data.balance;
			document.querySelector("#balancePanel p").textContent = `₹ ${amount.toLocaleString("en-IN")}`;
		})
		.catch(err => console.error("Failed to fetch balance:", err));
});

// Toggle balance panel
function toggleBalance() {
	console.log("Toggling balance");
	document.getElementById("balancePanel").classList.toggle("open");
}

// Toggle history panel
/*function toggleHistory() {
	console.log("Toggling history");
	document.getElementById("historyPanel").classList.toggle("open");
}*/

// Logout function
function logout() {
	localStorage.removeItem("username");
	window.location.href = "index.html";
}

function toggleAccountSummary() {
	const panel = document.getElementById('accountSummaryPanel');
	panel.classList.toggle('open');

	if (panel.classList.contains('open')) {
		// Replace with the actual logged-in username
		const username = localStorage.getItem('username');

		fetch(`/v1/userdetailcontroller/summary/${username}`)
			.then(res => res.json())
			.then(data => {
				document.getElementById('holderName').innerText = data.userName;
				document.getElementById('accountNumber').innerText = data.accountNumber;
				document.getElementById('balanceAmount').innerText = `₹ ${data.balance.toFixed(2)}`;
				document.getElementById('accountType').innerText = data.accountType;
				document.getElementById('ifscCode').innerText = data.ifscCode;
				document.getElementById('panNumber').innerText = data.panNumber;
				document.getElementById('phoneNumber').innerText = data.phoneNumber;
				document.getElementById('email').innerText = data.email;
			})
			.catch(err => {
				console.error('Error fetching account summary:', err);
			});
	}
}
function goToGlobalPay() {
	window.location.href = 'globalpay.html';
}

function getUsernameFromURL() {
	const params = new URLSearchParams(window.location.search);
	return params.get('username');
}

// On page load, extract and store username in sessionStorage
window.onload = function() {
	const username = getUsernameFromURL();
	if (username) {
		sessionStorage.setItem("username", username);
		console.log("Username stored in sessionStorage:", username);
	} else {
		console.warn("No username found in URL");
	}
};

