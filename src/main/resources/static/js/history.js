// Wait until DOM is fully loaded
document.addEventListener("DOMContentLoaded", function() {

	// Display username if available
	const username = localStorage.getItem('username');
	if (username) {
		const userDisplay = document.getElementById('userDisplay');
		if (userDisplay) {
			userDisplay.textContent = username;
		}
	}
});


function backToDashBoard() {
	window.location.href = "Dashboard.html";
}

function logout() {
	localStorage.removeItem("username");
	window.location.href = "index.html";
}

document.addEventListener("DOMContentLoaded", () => {
	const tableBody = document.getElementById("historyTableBody");
	const searchInput = document.getElementById("searchText");
	const startDateInput = document.getElementById("startDate");
	const endDateInput = document.getElementById("endDate");
	const currentUser = localStorage.getItem("username");
	const accountNumber = localStorage.getItem("accountNumber");
	localStorage.setItem("accountNumber", accountNumber);

	function fetchHistory() {
		const search = searchInput.value?.trim();
		const start = startDateInput.value;
		const end = endDateInput.value;

		let url = `/v1/transactioncontroller/history?accountNumber=${accountNumber}&`;
		if (search) url += `search=${encodeURIComponent(search)}&`;
		if (start) url += `startDate=${start}&`;
		if (end) url += `endDate=${end}&`;

		if (url.endsWith("&") || url.endsWith("?")) {
			url = url.slice(0, -1);
		}

		fetch(url)
			.then(res => {
				if (!res.ok) throw new Error("Failed to fetch data");
				return res.json();
			})
			.then(data => {
				tableBody.innerHTML = "";
				if (!data || data.length === 0) {
					tableBody.innerHTML = "<tr><td colspan='7'>No records found</td></tr>";
					return;
				}

				data.forEach((tx, index) => {
					const formattedTimestamp = tx.timestamp
						? tx.timestamp.replace("T", " ").split(".")[0]
						: "";

					const row = `
					<tr>
					              <td>${index + 1}</td>
					              <td>${formattedTimestamp}</td>
					              <td>${tx.fromAccount}</td>
					             <td>${tx.fromAccountHolderName}</td>
					              <td>${tx.toAccount}</td>
					              <td>${tx.toAccountHolderName}</td>
					              <td>${tx.transactionType}</td>
					              <td>₹${parseFloat(tx.amount).toFixed(2)}</td>
					              <td>₹${parseFloat(tx.balance).toFixed(2)}</td>
					            </tr>
					          `;
					tableBody.innerHTML += row;
				});
	
			})
			.catch(err => {
				console.error("Error fetching transactions:", err);
				tableBody.innerHTML = "<tr><td colspan='9'>Error loading data</td></tr>";
			});
	}

	// Event listeners
	searchInput.addEventListener("input", fetchHistory);
	startDateInput.addEventListener("change", fetchHistory);
	endDateInput.addEventListener("change", fetchHistory);

	// Initial fetch
	fetchHistory();
});