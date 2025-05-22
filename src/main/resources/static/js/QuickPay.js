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
