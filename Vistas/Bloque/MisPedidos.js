var objCliente;

$(function()
{
    validarCliente();
    eventos();
	listarPedidos();
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

function eventos()
{
    $(document).on('change', '.filtros',function()
    {
        Materialize.toast("Cargando", 1500);
        listarPedidos();
    });
}

function listarPedidos()
{
    $('#tbMisPedidos tbody').empty();

    var url = IP + "/pedido/readPedidoPorCliente/" + objCliente.idCliente;    

    var data =
    {
        fecha:    $('#fechaFiltro').val(),
        idEstado: $('#estadoFiltro').val()
    };

    $.get(url, data, function(respuesta)
    {
        $.each(respuesta, function(i, x)
        {
            var tr =    ''  +
                        '<tr>' +
                            '<td>' + (i+1)                      				+ '</td>' +
                            '<td>' + x.fecha_pedido             				+ '</td>' +
                            '<td>' + 'S/. ' + parseFloat(x.total).toFixed(2)    + '</td>' +
                            '<td>' + x.idEstadoBean.descripcion 				+ '</td>' +
                            '<td>' +
                                '<a onclick="verDetallePedido(' + x.idPedido + ')" href="javascript:;">Ver Detalle Pedido</a>' +
                            '</td>'+                            
                        '</tr>';


            $('#tbMisPedidos').append(tr);
        });
    });
}

function verDetallePedido(idPedido)
{
	$('#tbDetallePedido tbody').empty();

    var url = IP + "/pedido/readDetallePedido/" + idPedido;

    $.get(url, function(respuesta)
    {        
        $.each(respuesta, function(i, x)
        {
        	var producto  = x.detalle_ProductoBean.idProductoBean;
        	var color     = x.detalle_ProductoBean.idColorBean;
        	var descuento = x.detalle_ProductoBean.idDescuentoBean;

            var tr =    ''  +
                        '<tr>' +
                            '<td>' + (i+1)                      														+ '</td>' +
                            '<td>' + producto.idCategoriaBean.descripcion   											+ '</td>' +
                            '<td>' + producto.idMarcaBean.descripcion       											+ '</td>' +
                            '<td>' + producto.descripcion 																+ '</td>' +
                            '<td>' + color.descripcion 																	+ '</td>' +
                            '<td>' + 'S/. ' + parseFloat(x.precio).toFixed(2) 											+ '</td>' +
                            '<td>' + x.cantidad 																		+ '</td>' +
                            '<td>' + 'S/. ' + parseFloat(x.precio * x.cantidad).toFixed(2)                                        + '</td>' +
                            '<td>' +
                                'S/. ' + ((descuento.idDescuento <= 0) ? '0.00' : parseFloat(x.precio * x.cantidad * descuento.porcentaje / 100).toFixed(2)) +
                            '</td>' +
                            '<td>' + 'S/. ' + parseFloat(x.subTotal).toFixed(2) 										+ '</td>' +
                        '</tr>';

            $('#tbDetallePedido').append(tr);
        });

		Materialize.updateTextFields();
        $('#modalDetallePedido').modal('open');
    });
}