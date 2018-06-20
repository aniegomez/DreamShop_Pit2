var objCliente;

$(function()
{	
	validarCliente();
	cargarCombos();
	eventos();
	getFotoCliente();
});

function validarCliente()
{
	objCliente = sessionStorage.getItem("objCliente");
	if(objCliente == null)
		window.location = "LoginExterno.html";
	else
	{
		objCliente = JSON.parse(objCliente);
	}
}

function cargarCombos()
{
	$.when(
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
		}),

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
		})
	).then(function(data)
	{
	    $('#idCliente').val(objCliente.idCliente);
	    $('#nombre').val(objCliente.nombre);
	    $('#apellido_p').val(objCliente.apellido_p);
	    $('#apellido_m').val(objCliente.apellido_m);
	    $('#idTipoDocumento').val(objCliente.idTipoDocumentoBean.idTipoDocumento).change();
	    $('#nroDocumento').val(objCliente.nro_documento);
	    $('#correo').val(objCliente.correo);
	    $('#idEstadoCivil').val(objCliente.idEstadoCivilBean.idEstadoCivil).change();
	    $('#telefono').val(objCliente.telefono);
	});
}

function eventos()
{
	$('#foto').on("change", function()
	{
		var file = this.files[0];

	    if (file.size > 1024000)
	        alert('Tamaño máximo es 1mb');
	    else if( file.type != "image/jpeg")
	    	alert('Imagen no es de tipo jpg');
	    else
	    {
	    	var reader = new FileReader();
	    	reader.onload = function()
	    	{
	    		$('#imagenCliente').attr("src", this.result);
	    		var data = 
	    		{
	    			idCliente: objCliente.idCliente,
	    			foto_ruta: this.result
	    		}

	    		data = JSON.stringify(data);

	    		$.ajax(
	    		{
	    			type: "POST",
	    			url: IP + "/cliente/guardarFoto",
	    			data: data,
	    			dataType: 'json',
	    			contentType: "application/json",
	    			success: function(respuesta)
	    			{
	    				
	    			},
	    			error: function(e1, e2, e3)
	    			{
	    				Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
	    			}
	    		});
	    	}

	    	if(this.files[0] != null)
	    	{
	    		Materialize.toast("Guardando Foto", 2000);
	    		reader.readAsDataURL(this.files[0]);
	    	}
	    	else	    
	    	{
	    		var src = './Resources/imagenes/persona.png';
	    		$('#imagenCliente').attr("src", src);
	    	}
	    }
	});

	$('#formularioUpdate').submit(function(e)
	{
		e.preventDefault();

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
				//Por mientras en duro
				idCliente: $('#idCliente').val(),
				nombre: $('#nombre').val(),
				apellido_p: $('#apellido_p').val(),
				apellido_m: $('#apellido_m').val(),
				idTipoDocumentoBean: idTipoDocumento,
				nro_documento: $('#nroDocumento').val(),
				correo: $('#correo').val(),
				clave: $('#clave').val(),
				idEstadoCivilBean: idEstadoCivil,
				telefono: $('#telefono').val(),
			};

			data = JSON.stringify(data);


			$.ajax(
			{
				type: "POST",
				url: IP + "/cliente/updateCliente",
				dataType: 'json',
				data: data,
				contentType:"application/json",
				success: function(respuesta)
				{
					if(respuesta.error)
						Materialize.toast(respuesta.mensaje, 5000);
					else
					{
						Materialize.toast("Actualización exitosa. Cambios al reingresar.", 5000);
						setTimeout(function(){ location.reload(); }, 2000);
					}
					
				},
				error: function(e1, e2, e3)
				{
					Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
				}
			});
		});
}

function getFotoCliente()
{
	Materialize.toast("Cargando Foto", 1500);

    $.ajax(
    {
        type: "GET",
        url: IP + "/cliente/getFotoCliente/" + objCliente.idCliente,
        data: [],
        success: function(respuesta)
        {

        	var src;
        	if(respuesta == null || respuesta == '')
        		src = './Resources/imagenes/persona.png';
        	else
        		src = IP + "/cliente/getFotoCliente/" + objCliente.idCliente;
        		//src = 'data:image/png;base64,' + IP + "/cliente/getFotoCliente/" + objCliente.idCliente + '"';

			$('#imagenCliente').removeProp("src");
        	$('#imagenCliente').prop("src", src);
        },
        error: function(e1, e2, e3)
        {
            Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
        }
    });
}

function validarPathVariable(objeto)
{
	if(objeto == "" || objeto == null)
		return 0;
	else
		return objeto;
}