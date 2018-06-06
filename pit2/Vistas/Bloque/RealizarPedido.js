var objCliente;
var total = 0.0;

$(function()
{    
    validarCliente();
	validarPedido();
    eventos();
    cargarDepartamentos();
});

function validarCliente()
{
    objCliente = sessionStorage.getItem("objCliente");
    if(objCliente == null)
        window.location = "LoginExterno.html";
    else
    {
        objCliente = JSON.parse(objCliente);

        $('#idCliente').val(objCliente.idCliente);
        $('#nombre').val(objCliente.nombre);
        $('#apellido_p').val(objCliente.apellido_p);
        $('#apellido_m').val(objCliente.apellido_m);
        $('#idTipoDocumento').val(3).change();
        $('#nroDocumento').val(objCliente.nro_documento);
        $('#correo').val(objCliente.correo);
        $('#idEstadoCivil').val(3).change();
    }
}

function validarPedido()
{
	var listaCarrito = sessionStorage.getItem("listaCarrito");
	if( listaCarrito == null)
    	window.location = "MiCarrito.html";
    else if( JSON.parse(listaCarrito) == 0)
    	window.location = "MiCarrito.html";
    else
    {            	
    	listaCarrito = JSON.parse(listaCarrito);
    	total = 0.0;

    	$.each(listaCarrito, function(i, x)
    	{
    		total += parseFloat(calcularTotal(x));
    	});

        $('#total').html(total);
    }
}

function eventos()
{
    $('#confirmar').click(function()
    {
        var $id = $('#idDireccion');
        var regexp = /^\d{9}$/;

        if($id.val() == '')
            Materialize.toast('Dirección no puede estar vacía', 1200);
        else
        {
            var rpta = confirm("Una vez confirmado solo podrá cancelarlo a través de Atención al Cliente. ¿Desea realizar pedido?");
            if(rpta)
            {
                var listaCarrito = sessionStorage.getItem("listaCarrito");
                listaCarrito = JSON.parse(listaCarrito);

                var listaDetallePedido = new Array();

                $.each(listaCarrito, function(i, x)
                {                        
                    var idProductoBean = new Object();
                    idProductoBean["idProducto"] = x.idProducto;

                    var idColorBean = new Object();
                    idColorBean["idColor"] = x.idColor;

                    var idDescuentoBean = new Object();
                    idDescuentoBean["idDescuento"] = x.idDescuento;

                    var detalle_ProductoBean = new Object();
                    detalle_ProductoBean["idProductoBean"]  = idProductoBean;
                    detalle_ProductoBean["idColorBean"]     = idColorBean;
                    detalle_ProductoBean["idDescuentoBean"] = idDescuentoBean;

                    var detalle_PedidoBean = new Object();
                    detalle_PedidoBean["detalle_ProductoBean"]  = detalle_ProductoBean;
                    detalle_PedidoBean["cantidad"]              = x.cantidad;
                    detalle_PedidoBean["precio"]                = parseFloat(x.precio).toFixed(2);
                    detalle_PedidoBean["subTotal"]              = calcularTotal(x);

                    listaDetallePedido.push(detalle_PedidoBean);
                });


                var idClienteBean = new Object();
                idClienteBean['idCliente'] = objCliente.idCliente;

                var data = 
                {
                    idClienteBean: idClienteBean,
                    total: total,
                    listaDetallePedido: listaDetallePedido
                };

                data = JSON.stringify(data);

                $.ajax(
                {
                    type: "POST",
                    url: IP + "/pedido/createPedido",
                    dataType: 'json',
                    data: data,
                    contentType:"application/json",
                    success: function(respuesta)
                    {
                        if(respuesta.error)
                            Materialize.toast(respuesta.mensaje, 5000);
                        else
                        {
                            sessionStorage.removeItem("listaCarrito");
                            window.location = "ResultadoPedido.html";
                        }
                    },
                    error: function(e1, e2, e3)
                    {
                        Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
                    }
                });
            }                
        }        
    });

    $('#formularioNuevaDireccion').on("submit", function(e)
    {
        e.preventDefault();

        var idClienteBean = new Object();
        idClienteBean['idCliente'] = objCliente.idCliente;

        var idDistritoBean = new Object();
        idDistritoBean['idDistrito'] = $('#cboDistrito').val();

        var data = 
        {
            idClienteBean:  idClienteBean,
            idDistritoBean: idDistritoBean,
            descripcion:    $('#txtDireccion').val(),
            referencia:     $('#txtReferencia').val()
        };

        data = JSON.stringify(data);

        $.ajax(
        {
            type: "POST",
            url: IP + "/direccion/createDireccion",
            dataType: 'json',
            data: data,
            contentType:"application/json",
            success: function(respuesta)
            {
                if(respuesta.error)
                    Materialize.toast(respuesta.mensaje, 5000);
                else
                {
                    var ubigeo = '' +
                                 $('#cboDistrito :selected').text() + ' - ' +
                                 $('#cboProvincia :selected').text() + ' - ' +
                                 $('#cboDepartamento :selected').text();

                    $('#idDireccion').val(respuesta.idDireccion);
                    $('#direccion').val( $('#txtDireccion').val() );
                    $('#ubigeo').val( ubigeo );
                    $('#referencia').val( $('#txtReferencia').val() );

                    $('#txtDireccion').val("");
                    $('#txtReferencia').val("");

                    $("#cboDepartamento").val( $("#cboDepartamento option:first").val() );
                    $('#cboProvincia').empty().append('<option value="" disabled selected>[Seleccione]</option>');
                    $('#cboDistrito').empty().append('<option value="" disabled selected>[Seleccione]</option>');

                    $('#modalNuevaDireccion').modal('close');
                }
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
            }
        });
    });

    $('#cboDepartamento').on('change', function(e)
    {
        $('#cboProvincia').empty().append('<option value="" disabled selected>[Seleccione]</option>');
        $('#cboDistrito').empty().append('<option value="" disabled selected>[Seleccione]</option>');

        $.ajax(
        {
            type: "GET",
            url: IP + "/combo/readProvinciaPorDepartamento/" + $('#cboDepartamento').val(),
            data: [],
            success: function(respuesta)
            {
                $('#cboProvincia').empty().append('<option value="" disabled selected>[Seleccione]</option>');

                respuesta.map(function(x, i)
                {
                    $('#cboProvincia').append( new Option(x.descripcion,x.idProvincia) );
                });
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
            }
        });
    });

    $('#cboProvincia').on('change', function(e)
    {
        $('#cboDistrito').empty().append('<option value="" disabled selected>[Seleccione]</option>');

        $.ajax(
        {
            type: "GET",
            url: IP + "/combo/readDistritoPorProvincia/" + $('#cboProvincia').val(),
            data: [],
            success: function(respuesta)
            {
                $('#cboDistrito').empty().append('<option value="" disabled selected>[Seleccione]</option>');

                respuesta.map(function(x, i)
                {
                    $('#cboDistrito').append( new Option(x.descripcion,x.idDistrito) );
                });
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
            }
        });
    });

    $('#btnExistentes').click(function()
    {
        var tbDireccion = $('#tbDireccion tbody');

        tbDireccion.empty();

        $.ajax(
        {
            type: "GET",
            url: IP + "/direccion/readDireccionPorCliente/" + objCliente.idCliente,
            data: [],
            success: function(respuesta)
            {
                respuesta.map(function(x, i)
                {
                    var idFila = 'tbDireccionItem' + (i+1);

                    var fila = '' +
                    '<td>' +
                        '<p style="display: none;">' + JSON.stringify(x) + '</p>' +
                        (i+1) + 
                    '</td>' +
                    '<td>' + x.descripcion + '</td>' +
                    '<td>' + x.idDistritoBean.descripcion + '</td>' +
                    '<td>' + x.referencia + '</td>' +
                    '<td>' +
                        '<p>' +
                            '<input name="direccion" type="radio" id="' + idFila +'" />' +
                            '<label for="' + idFila + '">Elegir</label>' +
                        '</p>' +
                    '</td>';



                    $('#tbDireccion').append('<tr>' + fila +'</tr>');
                });

                $('#modalListaDireccion').modal('open');                
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
            }
        });    
    });

    $('#btnSeleccionarDireccion').click(function()
    {
        var haySeleccionados = false;

        $('#tbDireccion > tbody  > tr').each(function(i, tr)
        {
            if( !haySeleccionados )
            {
                var td_radio     = $(tr).find('td')[4];
                var radio        = $(td_radio).find("input")[0];

                haySeleccionados = $(radio).is(":checked");
                
                if( haySeleccionados )
                {
                    var td_data      = $(tr).find('td')[0];
                    var p_data       = $(td_data).find("p")[0];
                    var data         = $(p_data).html();

                    data = JSON.parse(data);

                    $('#idDireccion').val(data.idDireccion);
                    $('#direccion').val(data.descripcion);
                    $('#ubigeo').val(data.idDistritoBean.descripcion);
                    $('#referencia').val(data.referencia);
                }
            }
        });

        if( haySeleccionados )
            $('#modalListaDireccion').modal('close');
        else
            Materialize.toast("Seleccione una dirección", 1200);
    });
}

function cargarDepartamentos()
{
    $.ajax(
    {
        type: "GET",
        url: IP + "/combo/readDepartamentoPorPais/1",
        data: [],
        success: function(respuesta)
        {
            $('#cboDepartamento').empty().append('<option value="" disabled selected>[Seleccione]</option>');

            respuesta.map(function(x, i)
            {
                $('#cboDepartamento').append( new Option(x.descripcion,x.idDepartamento) );
            });
        },
        error: function(e1, e2, e3)
        {
            Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
        }
    });
}

function calcularTotal(x)
{
    var descuento = 100 - x.porcentaje;
    return parseFloat(  (parseFloat(x.precio) * x.cantidad) * descuento / 100  ).toFixed(2);
}   