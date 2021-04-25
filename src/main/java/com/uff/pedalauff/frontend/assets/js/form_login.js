function handleSubmit(event) {
    event.preventDefault();
    const data = new FormData(event.target);
    const value = Object.fromEntries(data.entries());
    let response = $.ajax({
        type: 'POST',
        url: event.target.action,
        dataType: 'json',
        data: JSON.stringify(value),
        contentType: 'application/json',
        async: false
    }).responseText;

    if (response === "true") {
        window.location.href = "http://localhost:8080/logado/home-logado";
    }else{
        console.log("Entrou no else");
        alert(response)
    }

}

const formLogin = document.getElementById("myformLogin");
formLogin.addEventListener('submit', handleSubmit);