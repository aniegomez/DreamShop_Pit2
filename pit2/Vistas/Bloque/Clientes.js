var objUsuario;
var arreglo_idProducto = [];

$(function()
{
	validarUsuario();
    eventos();
	listarClientes();
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

function eventos()
{
}

function listarClientes()
{
    $('#tbClientes tbody').empty();

	var url = IP + '/pedido/readClientes';
	
    $.get(url, function(respuesta)
    {
        $.each(respuesta, function(i, x)
        {
            var cliente = x.idClienteBean;

            var tr =    ''  +
                        '<tr>' +
                            '<td>' + (i+1)  						                                         + '</td>' +
							'<td>' + cliente.apellido_p + ' ' + cliente.apellido_m + ' ' + cliente.nombre    + '</td>' +
                            '<td>' + cliente.idTipoDocumentoBean.descripcion + ': ' + cliente.nro_documento  + '</td>' +
                            '<td>' +
                                ( (x.pedidos <= 0)
                                    ? x.pedidos
                                    :   '<a href="javascript:;" onclick="verPedidosPorCliente(' + cliente.idCliente + ', this)">' +
                                            x.pedidos +
                                        '<a>' ) +
                            '</td>' +
                            '<td>' + cliente.idEstadoBean.descripcion                                        + '</td>' +
                            '<td>' + ( (cliente.telefono == null) ? '-' : cliente.telefono )                 + '</td>' +
                            '<td>' + cliente.correo + '</td>' +
                        '</tr>';


            $('#tbClientes').append(tr);
        });
    });
}

function verPedidosPorCliente(idCliente, source)
{
    $('#tbPedidos tbody').empty();

    var url = IP + "/pedido/readPedidoPorCliente/" + idCliente;    

    var data =
    {
        fecha:    '',
        idEstado: 0
    };

    $.get(url, data, function(respuesta)
    {
        $.each(respuesta, function(i, x)
        {
            var tr =    ''  +
                        '<tr>' +
                            '<td>' + (i+1)                                      + '</td>' +
                            '<td>' + x.fecha_pedido                             + '</td>' +
                            '<td>' + 'S/. ' + parseFloat(x.total).toFixed(2)    + '</td>' +
                            '<td>' + x.idEstadoBean.descripcion                 + '</td>' +
                            '<td>' +
                                '<a onclick="verDetallePedido(' + x.idPedido + ')" href="javascript:;">Ver Detalle pedido</a>' +
                            '</td>'+                            
                        '</tr>';


            $('#tbPedidos').append(tr);
        });

        $('#tbDetallePedido tbody').empty();
        
        var tr      = $(source).parents("tr")[0];
        var td      = $(tr).find("td")[1];
        var nombre  = $(td).html();
        $('#nombreCliente').html(nombre);

        $('#modalPedidos').modal('open');
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
                            '<td>' + (i+1)                                                                              + '</td>' +
                            '<td>' + producto.idCategoriaBean.descripcion                                               + '</td>' +
                            '<td>' + producto.idMarcaBean.descripcion                                                   + '</td>' +
                            '<td>' + producto.descripcion                                                               + '</td>' +
                            '<td>' + color.descripcion                                                                  + '</td>' +
                            '<td>' + 'S/. ' + parseFloat(x.precio).toFixed(2)                                           + '</td>' +
                            '<td>' + x.cantidad                                                                         + '</td>' +
                            '<td>' + 'S/. ' + parseFloat(x.precio * x.cantidad).toFixed(2)                                        + '</td>' +
                            '<td>' +
                                'S/. ' + ((descuento.idDescuento <= 0) ? '0.00' : parseFloat(x.precio * x.cantidad * descuento.porcentaje / 100).toFixed(2)) +
                            '</td>' +
                            '<td>' + 'S/. ' + parseFloat(x.subTotal).toFixed(2)                                         + '</td>' +
                        '</tr>';

            $('#tbDetallePedido').append(tr);
        });

        Materialize.updateTextFields();
        $('#modalDetallePedido').modal('open');
    });
}