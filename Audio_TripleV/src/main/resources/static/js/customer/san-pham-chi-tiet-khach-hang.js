
    // Initialize AOS
    AOS.init({
    duration: 1000,
    once: true,
});

<!-- Custom JavaScript for interactive effects -->

    // Add hover effect on product cards
    document.querySelectorAll('.product-card').forEach(card => {
    card.addEventListener('mouseover', () => {
        card.classList.add('shadow-lg');
    });
    card.addEventListener('mouseout', () => {
    card.classList.remove('shadow-lg');
});
});

    // Toggle icon and smooth scroll for "Xem cấu hình chi tiết" button
    const toggleSpecsButton = document.querySelector('.toggle-specs');
    const moreSpecs = document.getElementById('moreSpecs');

    toggleSpecsButton.addEventListener('click', function() {
    // Toggle the collapse
    const bsCollapse = new bootstrap.Collapse(moreSpecs, {
    toggle: false
});
    if (moreSpecs.classList.contains('show')) {
    bsCollapse.hide();
} else {
    bsCollapse.show();
}

    // Toggle the icon
    const icon = this.querySelector('i');
    icon.classList.toggle('fa-chevron-down');
    icon.classList.toggle('fa-chevron-up');

    // Smooth scroll to the detailed specs
    setTimeout(() => {
    moreSpecs.scrollIntoView({ behavior: 'smooth' });
}, 300); // Delay to allow collapse animation to start
});
