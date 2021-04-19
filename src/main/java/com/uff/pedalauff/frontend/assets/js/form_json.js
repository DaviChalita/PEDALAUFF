function handleSubmit(event) {
    event.preventDefault();
    const data = new FormData(event.target);
    const value = Object.fromEntries(data.entries());
    let response = $.ajax({
        type: 'POST',
        url: "http://localhost:8080/usuario/salvar",
        dataType: 'json',
        data: JSON.stringify(value),
        contentType: 'application/json',
        async: false
    }).responseText;

    alert(response);

}

const form = document.getElementById("myform");
form.addEventListener('submit', handleSubmit);