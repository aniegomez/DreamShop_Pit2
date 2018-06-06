$(function()
{
	eventos();
});

function eventos()
{
	$('#registrarse').click(function()
	{
		window.location = "Registro.html";
	});

	$('#iniciarSesion').click(function()
	{
		window.location = "LoginExterno.html";
	});


	$('#formularioRecuperarClave').submit(function(e)
	{
		e.preventDefault();

		$('#btnRecuperarClave').attr("disabled", true);
		
		var data = 
		{
			correo: $('#correo').val()
		};

		data = JSON.stringify(data);

		$.ajax(
		{
			type: "POST",
			url: IP + "/cliente/recuperarClave",
			dataType: 'json',
			data: data,
			contentType:"application/json;",
			success: function(respuesta)
			{
				if(respuesta.error)
					Materialize.toast(respuesta.mensaje);
				else
					Materialize.toast("Se envió la nueva clave a su correo", 5000);

				$('#btnRecuperarClave').removeAttr("disabled")
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