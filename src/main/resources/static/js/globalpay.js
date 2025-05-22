function openQuickPay() {
	window.location.href = "QuickPay.html";

}

function openBankTransfer() {
	alert("Bank Transfer – Coming Soon!");
}

function openIntlTransfer() {
	alert("Bank Transfer – Coming Soon!");
}
function openPopup() {
  document.getElementById("popupOverlay").style.display = "flex";
}

function closePopup() {
  document.getElementById("popupOverlay").style.display = "none";
}

document.getElementById("combinedForm").addEventListener("submit", function (e) {
  e.preventDefault();
  alert("Payment submitted successfully!");
  closePopup();
  this.reset();
});
closePopup();

