document.querySelectorAll('div[id^=copy-controls]').forEach(function (row) {
    // Connect each "copy value" button to the corresponding text field
    var textbox = row.querySelector('[id^=copy-value]');
    var button = row.querySelector('[id^=copy-button]');
    button.addEventListener('click', function() {
        textbox.select();
        document.execCommand('copy');
        bootbox.alert("Link copied to clipboard.")
    });
});

document.getElementById('copy-value-1').value = window.location.href;
