document.getElementById('urlShortenerForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const originalUrl = document.getElementById('originalUrl').value;
    fetch('/api/shorten', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ originalUrl: originalUrl }),
    })
        .then(response => {console.log(response); return response.json()})
        .then(data => {
            const resultDiv = document.getElementById('result');
            resultDiv.innerHTML = `<div class="alert alert-success" role="alert">Shortened URL: <a href="/api/${data.shortLink}">${data.shortLink}</a></div>`;
        })
        .catch((error) => {
            console.error('Error:', error);
        });
});
