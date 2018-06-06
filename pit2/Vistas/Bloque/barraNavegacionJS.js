$('.button-collapse').sideNav();
$('.dropdown-button').dropdown();
$('.modal').modal();
$('select').material_select();

contadorCarrito();

function contadorCarrito()
{
	var listaCarrito = sessionStorage.getItem("listaCarrito");
	if( listaCarrito == null)
		$('#miCarrito').html("0");
	else if( JSON.parse(listaCarrito) == 0)
		$('#miCarrito').html("0");
	else
		$('#miCarrito').html(JSON.parse(listaCarrito).length) ;
}

$('#miCuenta').click(function()
{
    window.location = "MiCuenta.html";
});

$('#misPagos').click(function()
{
    window.location = "MisPagos.html";
})

$('#misFavoritos').click(function()
{
    window.location = "MisFavoritos.html";
})

$('#cambiarClave').click(function()
{
    window.location = "CambiarClave.html";
})

$('#misPedidos').click(function()
{
    window.location = "MisPedidos.html";
})

$('#salir').click(function()
{
    window.location = "Inicio.html";
    sessionStorage.removeItem("objCliente");
})