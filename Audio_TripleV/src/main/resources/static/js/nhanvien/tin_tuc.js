
    document.querySelectorAll('#play-list .secondary-box').forEach(item => {
    item.addEventListener('click', function () {
        const videoId = this.querySelector('img').getAttribute('data-src').match(/vi\/(.*?)\//)[1];
        const iframe = document.getElementById('ifram-play');
        iframe.src = `https://www.youtube.com/embed/${videoId}`;
    });
});

