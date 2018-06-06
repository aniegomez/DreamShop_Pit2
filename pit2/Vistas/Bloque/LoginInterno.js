$(function()
{
	eventos();
});

function eventos()
{				
	$('#formularioLoginInterno').submit(function(e)
	{
		e.preventDefault();
		
		var data = 
		{
			usuario: $('#usuario').val(),
			clave: $('#clave').val()
		};

		data = JSON.stringify(data);

		$.ajax(
		{
			type: "POST",
			url: IP + "/usuario/loginUsuario",
			dataType: 'json',
			data: data,
			contentType:"application/json;",
			success: function(respuesta)
			{
				if(respuesta.idUsuario <= 0)
					Materialize.toast("Datos incorrectos", 5000);
				else
				{
					sessionStorage.setItem("objUsuario", JSON.stringify(respuesta));
					var objPedido = sessionStorage.getItem("objPedido");
					
					window.location = "MenuPrincipal.html";
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