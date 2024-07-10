$(document).ready(function () {
  $('#loginForm').submit(function (event) {
    event.preventDefault();

    // Obtener los valores de correo y contraseña del formulario
    let correo = $('#email').val();
    let contraseña = $('#password').val();

    $.post("/login", { email: correo, password: contraseña })
      .done(function () {
        window.location.href = '/menuprincipal';
      })
      .fail(function () {
        const alertMessage = $("#alertMessage");

        const appendAlert = (message, type) => {
          const wrapper = document.createElement('div');
          wrapper.innerHTML = [
            `<div class="alert alert-info alert-dismissible" role="alert">`,
            `   <div>${message}</div>`,
            '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
            '</div>'
          ].join('');

          alertMessage.append(wrapper);
        };

        appendAlert('Usuario o contraseña incorrectos. Inténtalo de nuevo.', 'danger');

        setTimeout(() => {
          alertMessage.fadeOut("slow", function () {
            alertMessage.empty();
          });
        }, 3000);
      });
  });
});
