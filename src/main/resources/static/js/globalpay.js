// Navigation Functions
function openQuickPay() {
  window.location.href = "QuickPay.html";
}
function backToDashBoard() {
  window.location.href = "Dashboard.html";
}


function openBankTransfer() {
  alert("Bank Transfer – Coming Soon!");
}

function openIntlTransfer() {
  alert("International Transfer – Coming Soon!");
}



// Form Submission Handling
document.addEventListener("DOMContentLoaded", function () {
 
  // Display username if available
  const username = localStorage.getItem('username');
  if (username) {
    const userDisplay = document.getElementById('userDisplay');
    if (userDisplay) {
      userDisplay.textContent = username;
    }
  }
});

// Logout
function logout() {
  localStorage.removeItem('username');
  window.location.href = '/index.html';
}
