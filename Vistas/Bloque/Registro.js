var consultando = false;

$(function()
{
	cargarCombos();
	eventos();
});

function cargarCombos()
{
	$.ajax(
	{
		type: "GET",
		url: IP + "/combo/readTipoDocumento",
		data: [],
		success: function(respuesta)
		{					
			respuesta.map(function(x, i)
			{
				$('#idTipoDocumento').append( new Option(x.descripcion,x.idTipoDocumento) );
			});
		},
		error: function(e1, e2, e3)
		{
			Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
		}
	});

	$.ajax(
	{
		type: "GET",
		url: IP + "/combo/readEstadoCivil",
		data: [],
		success: function(respuesta)
		{					
			respuesta.map(function(x, i)
			{
				$('#idEstadoCivil').append( new Option(x.descripcion,x.idEstadoCivil) );
			});
		},
		error: function(e1, e2, e3)
		{
			Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
		}
	});
}

function eventos()
{

	$('#iniciarSesion').click(function()
	{
		window.location = "LoginExterno.html";
	});

	$('#recuperarClave').click(function()
	{
		window.location = "RecuperarClave.html";
	});

	$('#formularioRegistro').submit(function(e)
	{
		e.preventDefault();

		if(consultando)
		{
			Materialize.toast("Solicitud anterior en consulta", 5000);
			return;					
		}

		if( $('#clave').val() != $('#confirmarClave').val() )
		{
			Materialize.toast('Contraseñas no coinciden', 5000);
			return;
		}
		

		var objeto = new Object();
		objeto["idTipoDocumento"] = $('#idTipoDocumento').val();
		var idTipoDocumento = objeto;

		var objeto = new Object();
		objeto["idEstadoCivil"] = $('#idEstadoCivil').val();
		var idEstadoCivil = objeto;

		var objeto = new Object();
		objeto["idEstado"] = 1;
		var idEstado = objeto;

		var data = 
		{
			nombre: $('#nombre').val(),
			apellido_p: $('#apellido_p').val(),
			apellido_m: $('#apellido_m').val(),
			idTipoDocumentoBean: idTipoDocumento,
			nro_documento: $('#nroDocumento').val(),
			correo: $('#correo').val(),
			clave: $('#clave').val(),
			idEstadoCivilBean: idEstadoCivil
		};

		data = JSON.stringify(data);

		consultando = true;
		Materialize.toast("Realizando Registro", 3000);

		$.ajax(
		{
			type: "POST",
			url: IP + "/cliente/createCliente",
			dataType: 'json',
			data: data,
			contentType:"application/json",
			success: function(respuesta)
			{
				if(respuesta.error)
					Materialize.toast(respuesta.mensaje, 5000);
				else
				{
					Materialize.toast("Creación exitosa", 5000);

					url = IP + "/cliente/getCliente/" + respuesta.idCliente,
					$.get(url, function(x)
					{
						sessionStorage.setItem( "objCliente", JSON.stringify(x) );

						var listaCarrito = sessionStorage.getItem("listaCarrito");
						if( listaCarrito == null || JSON.parse(listaCarrito) == 0 )
							window.location = "Inicio.html";
						else
						{
							var objPedido = sessionStorage.getItem("objPedido");

							if(objPedido == null || objPedido == false)
								window.location = "Inicio.html";
							else
								window.location = "GenerarPedido.html";
						}
					});
				}

				consultando = false;
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
