$(function()
{
	var objCliente = sessionStorage.getItem("objCliente");
	if(objCliente == null)
		window.location = "LoginExterno.html";
	else
	{
		objCliente = JSON.parse(objCliente);

		$('#idCliente').val(objCliente.idCliente);
	}

	eventos();
});

function eventos()
{

	$('#formularioCambiarClave').submit(function(e)
	{
		e.preventDefault();

		if( $('#clave').val() != $('#confirmarClave').val() )
		{
			Materialize.toast('Contraseñas no coinciden', 5000);
			return;
		}

		var data = 
		{
			idCliente: $('#idCliente').val(),
			clave: $('#clave').val(),
		};

		data = JSON.stringify(data);
		

		$.ajax(
		{
			type: "POST",
			url: IP + "/cliente/cambiarClave",
			dataType: 'json',
			data: data,
			contentType:"application/json",
			success: function(respuesta)
			{
				if(respuesta.error)
					Materialize.toast(respuesta.mensaje, 5000);
				else
				{
					Materialize.toast("Cambio exitoso", 5000);
					window.location = "Inicio.html";
				}
			},
			error: function(e1, e2, e3)
			{
				Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
			}
		});
	});
}

function validarPathVariable(objeto)
{
	if(objeto == "" || objeto == null)
		return 0;
	else
		return objeto;
}