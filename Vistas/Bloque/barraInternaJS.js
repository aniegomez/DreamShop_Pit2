$('.button-collapse').sideNav();
$('.dropdown-button').dropdown();
$('.modal').modal();
$('select').material_select();

// $('#miCuenta').click(function()
// {
//     window.location = "MiCuenta.html";
// });

$('#salir').click(function()
{
    window.location = "LoginInterno.html";
    sessionStorage.removeItem("objUsuario");
    sessionStorage.removeItem("listaPagina");
})