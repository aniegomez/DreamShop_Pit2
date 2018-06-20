var objCliente;
var objProducto, idProducto;
var arreglo_idColores = [];
var dataDetalleProductos;

$(function()
{
	validarUsuario();
    validarDetalle();
    eventos();
	cargarDetalleProducto();
});

function validarUsuario()
{
    objUsuario = sessionStorage.getItem("objUsuario");
    if(objUsuario == null)
        window.location = "LoginInterno.html";
    else
    {
        objUsuario = JSON.parse(objUsuario);
        $('#idUsuario').val(objUsuario.idUsuario);
    }
}

function validarDetalle()
{
   
    var objProducto = sessionStorage.getItem("objProducto");
    if( objProducto == null)
        window.location = "MantenerProducto.html";
    else
    {

        objProducto = JSON.parse(objProducto);
        idProducto = objProducto.idProducto;

        $('#nombreProducto').html( objProducto.descripcion );
        $('#categoria').html( objProducto.idCategoriaBean.descripcion );
        $('#marca').html( objProducto.idMarcaBean.descripcion );
    }
}

function eventos()
{
	
	$('#reportePDF').click(function()
    {
        ReportePDF(dataDetalleProductos);
    });
	
    $('#btnCrearProducto').click(function()
    {
        $('#txtProductoC').val('');
        $('#txtPrecioC').val('');
        $('#cboCategoriaC').val( $('#cboCategoriaC option:first').val() );
        $('#cboMarcaC').val( $('#cboMarcaC option:first').val() );

        $('#modalCrearProducto').modal('open');
    });

    $('#modalCrearProducto').submit(function(e)
    {
        e.preventDefault();

        var idProductoBean           = new Object();
        idProductoBean['idProducto'] = idProducto;
        var idColorBean              = new Object();
        idColorBean['idColor']       = $('#cboColorC').val();

        var data =
        {
            idProductoBean: idProductoBean,
            idColorBean: idColorBean,
            cantidad: $('#txtCantidadC').val()
        };

        data = JSON.stringify(data);

        $.ajax(
        {
            type: "POST",
            url: IP + "/detalleproducto/createDetalleProducto",
            dataType: 'json',
            data: data,
            contentType:"application/json",
            success: function(respuesta)
            {
                if(respuesta.error)
                    Materialize.toast(respuesta.mensaje, 5000);
                else
                    location.reload();
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);                
            }
        });
    });

    $('#formularioActualizarProducto').submit(function(e)
    {
        e.preventDefault();
            
        var idProductoBean           = new Object();
        idProductoBean['idProducto'] = idProducto;
        var idColorBean              = new Object();
        idColorBean['idColor']       = $('#txtIdColorA').val();

        var data =
        {
            idProductoBean: idProductoBean,
            idColorBean: idColorBean,
            cantidad: $('#txtCantidadA').val()
        };

        data = JSON.stringify(data);

        $.ajax(
        {
            type: "POST",
            url: IP + "/detalleproducto/updateDetalleProducto",
            dataType: 'json',
            data: data,
            contentType:"application/json",
            success: function(respuesta)
            {
                if(respuesta.error)
                    Materialize.toast(respuesta.mensaje, 5000);
                else
                    location.reload();
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);                
            }
        });
    });

    $('#btnModalAsignarDescuento').click(function()
    {
        var contador = 0;
        arreglo_idColores = [];
        $.each($('#tbDetalleProducto tbody').find("tr"), function(i, tr)
        {
            var td 		 = $(tr).find("td")[5];
            var checkbox = $(td).find("input")[0];
            var p_data 	 = $(tr).find("p")[0];
            var data 	 = $(p_data).html();
            data 		 = JSON.parse(data);            

            if(checkbox != undefined)
                if( $(checkbox).is(":checked") )
                {
                	arreglo_idColores.push(data.idColorBean.idColor);
                    contador++;
                }
        });

        if(contador == 0)
            Materialize.toast("Elija un color", 1200);
        else
        {
			var url = IP + '/descuento/readDescuentoActivo';
			
		    $.get(url, function(respuesta)
		    {
		        $('#tbDescuento tbody').empty();

		        $.each(respuesta, function(i, x)
		        {
		            var tr =    ''  +
		                        '<tr>' +
		                            '<td>' +
		                            	(i+1) +
		                            	'<p style="display: none;">' +
		                            		JSON.stringify(x) +
		                            	'</p>' +
		                            '</td>' +
		            				'<td>' + x.descripcion	+ '</td>' +
		            				'<td>' + x.porcentaje 	+ '</td>' +
                    				' <td>' +
			                        	'<p>' +
			                            	'<input name="descuento" type="radio" id="chk_descuento_' + i + '" />' +
			                            	'<label for="chk_descuento_' + i + '"></label>' +
			                        	'</p>' +
			                       	'</td>';


		            $('#tbDescuento').append(tr);
		        });
		    });

            $('#modalAsignarDescuento').modal('open');
        }
    });

    $('#btnAsignarDescuento').click(function()
    {
    	var contador = 0;
    	var idDescuento = 0;
        $.each($('#tbDescuento tbody').find("tr"), function(i, tr)
        {
            var td 		 = $(tr).find("td")[3];
            var checkbox = $(td).find("input")[0];
            var p_data 	 = $(tr).find("p")[0];
            var data 	 = $(p_data).html();
            data 		 = JSON.parse(data);

            if(checkbox != undefined)
                if( $(checkbox).is(":checked") )
                {
                	idDescuento = data.idDescuento;
                    contador++;
                }
        });

		if(contador == 0)
            Materialize.toast("No ha seleccionado un descuento", 1200);
        else
        {
        	var data = JSON.stringify(arreglo_idColores);

            $.ajax(
            {
                type: "POST",
                url: IP + "/descuento/asignarDescuentoDetalleProducto/" + idDescuento + "/" + idProducto,
                data: data,
                dataType: 'json',
                contentType: "application/json",
                success: function(respuesta)
                {
                    cargarDetalleProducto();
                    $('#modalAsignarDescuento').modal('close');
                },
                error: function(e1, e2, e3)
                {
                    Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);                    
                }
            });
        }
    });
}

function cargarDetalleProducto()
{
	var url = IP + '/detalleproducto/readProductoPorProducto/' + idProducto;
	
    $.get(url, function(respuesta)
    {
		dataDetalleProductos = respuesta;
		
        $('#tbDetalleProducto tbody').empty();

        
        $.each(respuesta, function(i, x)
        {						
            var producto    = x.idProductoBean;
            var color       = x.idColorBean;
            var descuento   = x.idDescuentoBean;

            var tr =    ''  +
                        '<tr>' +
                            '<td>' +
                                (i+1) +
                                '<p style="display:none;">' + JSON.stringify(x) + '</p>' +
                            '</td>' +
            				'<td>' +
            					'<a onclick="verFotoProducto(' + producto.idProducto + ',' + color.idColor + ')" href="javascript:;">' +
            						color.descripcion +
            					'</a>' +                                
            				'</td>'+
            				'<td>' + x.cantidad																+ '</td>' +
							'<td>' + 'S/. ' + parseFloat( producto.precio ).toFixed(2)						+ '</td>' +
							'<td>' +
                                        'S/. ' + ( ( descuento.idDescuento <= 0 ) ? "0.00"
                                        : parseFloat( producto.precio * descuento.porcentaje / 100 ) .toFixed(2)	) 	+
                            '</td>';

                if( descuento.idDescuento <= 0 )
                    tr += ' <td>' +
                                '<p style="margin: 0px;">' +
                                    '<input type="checkbox" id="chk_' + i + '" />' +
                                    '<label for="chk_' + i + '"></label>' +
                                '</p>' +
                            '</td>';
                else
                    tr += ' <td>' +
                                '<a href="javascript:;" onclick="desasignarDescuento(this)">' +
                                    '<i class="fa fa-times" aria-hidden="true" style="font-size: 25px;"></i>' +
                                    '<p style="display: none;">' + JSON.stringify(x) + '</p>' +
                                '</a>' +
                            '</td>';


                tr+=        '<td>' +
                                '<a href="javascript:;" onclick="elegirFoto(this)">' +
                                    '<i class="fa fa-upload" aria-hidden="true"></i>' +
                                '</a>' +
                                '<div style="display: none">' +
                                    '<input type="file" onchange="subirFoto(this)">' +
                                '</div>' +
                            '</td>' +
            				'<td>' +
                                '<a onclick="modalActualizar(this)" href="javascript:;">'+
                                    '<i class="fa fa-history" aria-hidden="true"></i>' +
                                    '<p style="display: none;">' + JSON.stringify(x) + '</p>' +
                                '</a>' +
                                '&nbsp;&nbsp;' +
                                '<a onclick="eliminarProducto(' + producto.idProducto + ',' + color.idColor + ')" href="javascript:;">'+
                                    '<i class="fa fa-trash" aria-hidden="true"></i>' +
                                '</a>' +
                            '</td>' +
            				'</td>' +
                        '</tr>';


            $('#tbDetalleProducto').append(tr);
        });
    });

    var url = IP + '/combo/readColor';
    $.get(url, function(respuesta)
    {
        $('#cboColorC').empty().append('<option value="" disabled selected>[Seleccione]</option>');

        respuesta.map(function(x, i)
        {
            $('#cboColorC').append( new Option(x.descripcion,x.idColor) );
        });
    });
}

function verFotoProducto(idProducto, idColor)
{
	var url = IP + "/detalleproducto/getFotoProducto/" + idProducto  + "/" + idColor;
	$.get(url, function(respuesta)
	{
		if(respuesta == null || respuesta == '')
		{
			var src = './Resources/imagenes/default.png';
			$('#imagenProducto').attr("src", src);
		}					
		else					
			$('#imagenProducto').attr("src", url);

		
		$('#modalVerFoto').modal('open');
	});
}

function elegirFoto(source)
{
    var td      = $(source).parents("td")[0];
    var input   = $(td).find("input")[0];

    $( input ).trigger('click');
}

function subirFoto(source)
{
    var tr      = $(source).parents("tr")[0];
    var td      = $(tr).find("td")[0];
    var p       = $(td).find("p")[0];
    var datos    = $(p).html();
    datos        = JSON.parse(datos);

    var file = source.files[0];

    if(file.size > 1024000)
        alert('Tamaño máximo es 1mb');
    else if( file.type != "image/jpeg")
        alert('Imagen no es de tipo jpg');
    else
    {
        var reader = new FileReader();
        reader.onload = function()
        {
            var idProductoBean = new Object();
            idProductoBean['idProducto'] = datos.idProductoBean.idProducto;
            var idColorBean = new Object();
            idColorBean['idColor'] = datos.idColorBean.idColor;

            var data = 
            {
                idProductoBean: idProductoBean,
                idColorBean: idColorBean,
                foto_ruta: this.result
            }

            data = JSON.stringify(data);

            $.ajax(
            {
                type: "POST",
                url: IP + "/detalleproducto/guardarFoto",
                data: data,
                dataType: 'json',
                contentType: "application/json",
                success: function(respuesta)
                {
                    cargarDetalleProducto();
                },
                error: function(e1, e2, e3)
                {
                    Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);                    
                }
            });
        }

        if(source.files[0] != null)
        {
            Materialize.toast("Guardando Foto", 2000);
            reader.readAsDataURL(source.files[0]);
        }
    }
}

function modalActualizar(source)
{
    var td    = $(source).parents("td")[0];
    var p     = $(td).find("p")[0];
    var datos = $(p).html();
    datos     = JSON.parse(datos);

    $('#txtIdProductoA').val( datos.idProductoBean.idProducto ) ;
    $('#txtIdColorA').val( datos.idColorBean.idColor ) ;
    $('#txtColorA').val( datos.idColorBean.descripcion ) ;
    $('#txtCantidadA').val( datos.cantidad ) ;

    Materialize.updateTextFields();

    $('#modalActualizarProducto').modal('open');
}

function eliminarProducto(idProducto, idColor)
{
    var rpta = confirm("¿Desea eliminar este producto?");
    if( rpta )
    {
        $.ajax(
        {
            type: "POST",
            url: IP + "/detalleproducto/deleteDetalleProducto/" + idProducto + "/" + idColor,
            dataType: 'json',
            data: {},
            contentType:"application/json",
            success: function(respuesta)
            {
                if(respuesta.error)
                    Materialize.toast(respuesta.mensaje, 5000);
                else
                    location.reload();
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);                
            }
        });
    }
    
}

function desasignarDescuento(source)
{
	var rpta = confirm("Desea quitar descuento?");
    if( !rpta )
        return;

    var td          = $(source).parents("td")[0];
    var p           = $(td).find("p")[0];
    var datos       = $(p).html();
    datos           = JSON.parse(datos);

    var idProducto  = datos.idProductoBean.idProducto;
    var idColor     = datos.idColorBean.idColor;

    $.ajax(
    {
    	type: "POST",
    	url: IP + "/descuento/desAsignarDescuentoDetalleProducto/" + idProducto + "/" + idColor,
    	dataType: 'json',
    	data: {},
    	contentType:"application/json",
    	success: function(respuesta)
    	{
    		if(respuesta.error)
    			Materialize.toast(respuesta.mensaje, 5000);
    		else
    			cargarDetalleProducto();
    	},
    	error: function(e1, e2, e3)
    	{
    		Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);    		
    	}
    });
}