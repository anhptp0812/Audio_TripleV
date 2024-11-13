
    function handleSearch() {
    const searchInput = document.getElementById('searchInput');

    // Check if the input is visible
    if (searchInput.style.display === 'none' || searchInput.style.display === '') {
    searchInput.style.display = 'block'; // Show the input field
    searchInput.focus();  // Automatically focus the input field
} else {
    // If input is visible, proceed with search
    const searchValue = searchInput.value.trim();  // Get the trimmed value
    if (searchValue) {
    // Redirect to sang_pham.html with the search term
    window.location.href = `sang_pham.html?search=${encodeURIComponent(searchValue)}`;
}
    // Clear the input field after search
    searchInput.value = '';
    searchInput.style.display = 'none';  // Hide the input field again
}
}
