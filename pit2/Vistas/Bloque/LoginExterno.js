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

	$('#recuperarClave').click(function()
	{
		window.location = "RecuperarClave.html";
	});

	$('#formularioLoginExterno').submit(function(e)
	{
		e.preventDefault();
		
		var data = 
		{
			correo: $('#correo').val(),
			clave: $('#clave').val()
		};

		data = JSON.stringify(data);

		$.ajax(
		{
			type: "POST",
			url: IP + "/cliente/loginCliente",
			dataType: 'json',
			data: data,
			contentType:"application/json;",
			success: function(respuesta)
			{
				if(respuesta.idCliente <= 0)
					Materialize.toast("Datos incorrectos", 5000);
				else
				{
					sessionStorage.setItem("objCliente", JSON.stringify(respuesta));
					var objPedido = sessionStorage.getItem("objPedido");

					var listaCarrito = sessionStorage.getItem("listaCarrito");
					if( listaCarrito == null || JSON.parse(listaCarrito) == 0 )
						window.location = "Inicio.html";
					else
					{
						var objPedido = sessionStorage.getItem("objPedido");

						if(objPedido == null || objPedido == false)
							window.location = "Inicio.html";
						else
							window.location = "RealizarPedido.html";
					}
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