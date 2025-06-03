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

	const tableBody = document.getElementById("historyTableBody");
	const searchInput = document.getElementById("searchText");
	const startDateInput = document.getElementById("startDate");
	const endDateInput = document.getElementById("endDate");

	function fetchHistory() {
		const accountNumber = localStorage.getItem("accountNumber");
		if (!accountNumber) {
			console.error("Account number not found in localStorage");
			tableBody.innerHTML = "<tr><td colspan='9'>Account number missing. Please login again.</td></tr>";
			return;
		}

		const search = searchInput.value?.trim();
		const start = startDateInput.value;
		const end = endDateInput.value;

		let url = `/v1/transactioncontroller/history?accountNumber=${accountNumber}`;
		const params = [];

		if (search) params.push(`search=${encodeURIComponent(search)}`);
		if (start) params.push(`startDate=${start}`);
		if (end) params.push(`endDate=${end}`);

		if (params.length > 0) {
			url += '&' + params.join('&');
		}

		fetch(url)
			.then((res) => {
				if (!res.ok) throw new Error("Failed to fetch data");
				return res.json();
			})
			.then((data) => {
				console.log("Fetched transactions:", data);  // Debug log
				tableBody.innerHTML = "";
				if (!data || data.length === 0) {
					tableBody.innerHTML = "<tr><td colspan='9'>No records found</td></tr>";
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
			.catch((err) => {
				console.error("Error fetching transactions:", err);
				tableBody.innerHTML = "<tr><td colspan='9'>Error loading data</td></tr>";
			});
	}

	// Debounce search input to avoid too many requests
	let debounceTimeout;
	searchInput.addEventListener("input", () => {
		clearTimeout(debounceTimeout);
		debounceTimeout = setTimeout(fetchHistory, 300);
	});

	startDateInput.addEventListener("change", fetchHistory);
	endDateInput.addEventListener("change", fetchHistory);

	// Initial load
	fetchHistory();
});


// PDF download button
const pdfBtn = document.getElementById("downloadPdfBtn");
pdfBtn.addEventListener("click", () => {
	const accountNumber = localStorage.getItem("accountNumber");
	if (!accountNumber) {
		alert("Account number not found. Please log in again.");
		return;
	}

	let url = `/v1/transactioncontroller//download/pdf?accountNumber=${accountNumber}`;

	const search = searchInput.value?.trim();
	const start = startDateInput.value;
	const end = endDateInput.value;

	const params = [];

	if (search) params.push(`search=${encodeURIComponent(search)}`);
	if (start) params.push(`startDate=${start}`);
	if (end) params.push(`endDate=${end}`);

	if (params.length > 0) {
		url += '&' + params.join('&');
	}

	fetch(url, {
		method: 'GET',
		headers: {
			'Accept': 'application/pdf'
		}
	})
	.then(response => {
		if (!response.ok) throw new Error("Failed to download PDF");
		return response.blob();
	})
	.then(blob => {
		const url = window.URL.createObjectURL(blob);
		const a = document.createElement('a');
		a.href = url;
		a.download = "TransactionHistory.pdf";
		document.body.appendChild(a);
		a.click();
		a.remove();
	})
	.catch(error => {
		console.error("PDF download error:", error);
		alert("Failed to download PDF. Please try again.");
	});
});

function backToDashBoard() {
	window.location.href = "Dashboard.html";
}

function logout() {
	localStorage.removeItem("username");
	window.location.href = "index.html";
}
