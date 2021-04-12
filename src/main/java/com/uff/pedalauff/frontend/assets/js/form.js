
$("#myform").validate({
    rules: {
        your_email: {
            required: true,
            email: true,
        },
        your_username: {
            required: true,
            username: true
        },
        password: "required",
        comfirm_password: {
            equalTo: "#password"
        }
    },

    messages: {
        your_nome_completo: {
            required: "Informe seu nome completo",
            nome_completo: true
        },
        your_cpf: {
            required: "Informe seu cpf",
            cpf: true
        },
        your_celular: {
            required: "Informe seu telefone",
            celular: true
        },
        username: {
            required: "Informe um nome de usuário",
            username: true
        },
        your_email: {
            required: "Informe um email",
            email: true
        },
        password: {
            required: "Informe uma senha"
        },
        comfirm_password: {
            required: "Confirme sua senha",
            equalTo: "Senha não confere"
        }
    },
    success: function(label, element) {
        if ($(element).hasClass("valid")) {
            label.addClass("valid");
        }
    },
    focusInvalid: false,
    focusCleanup: true,
    onkeyup: false,
});


$(document)
    .on('click', 'form button[type=button]', function(e) {
        var isValid = $(e.target).parents('form').isValid();
        if (!isValid) {
            e.preventDefault(); //prevent the default action
        }
    });

$()


function desabilitar() {
    if (!document.getElementById('radio-outro').checked) {
        document.getElementById('money').setAttribute("disabled", "disabled");
        document.getElementById('money').innerText
    } else {
        document.getElementById('onoff').value = ''; //Evita que o usuário defina um texto e desabilite o campo após realiza-lo
        document.getElementById('money').setAttribute("disabled", "disabled");
    }
}

function habilitar() {
    if (document.getElementById('radio-outro').checked) {
        document.getElementById('money').removeAttribute("disabled");
    } else {
        document.getElementById('onoff').value = ''; //Evita que o usuário defina um texto e desabilite o campo após realiza-lo
        document.getElementById('money').setAttribute("disabled", "disabled");
    }
}

