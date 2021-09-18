function handleSubmit(event) {
    event.preventDefault();
    const data = new FormData(event.target);
    const value = Object.fromEntries(data.entries());
    console.log(event.target.action)
    let response = $.ajax({
        type: 'POST',
        url: event.target.action,
        dataType: 'json',
        data: JSON.stringify(value),
        contentType: 'application/json',
        async: false
    }).responseText;

    window.location.href = "http://localhost:63342/pedalauff/pedala-uff/com/uff/pedalauff/frontend/logado/home-logado.html";
    if (response === "true") {
    } else {
        alert(response)
        window.location.href = "#";
    }

}

document.querySelectorAll('.some-class').forEach(item => {
    item.addEventListener('submit', handleSubmit);
})