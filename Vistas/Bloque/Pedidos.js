var objUsuario;
var arreglo_idPedido = [];
var dataPedido;
var dataDetallePedido;


$(function()
{
    validaUsuario();
    eventos();
	listarPedidos();
});

function validaUsuario()
{
    objUsuario = sessionStorage.getItem("objUsuario");
    if(objUsuario == null)
        window.location = "LoginInterno.html";
    else
    {
        objUsuario = JSON.parse(objUsuario);
    }
}

function eventos()
{
 $('#reportePDF').click(function()
    {
        ReportePDF(dataPedido);
    });
   
	$('#reportePDF1').click(function()
    {
        ReportePDF1(dataDetallePedido);
    });

    $(document).on('change', '.filtros',function()
    {
        Materialize.toast("Cargando", 1500);
        listarPedidos();
    });

    $('#cambiarEstado').click(function(e)
    {
        var contador = 0;
        arreglo_idPedido = [];
        $('#tbMisPedidos tbody').find("tr").each(function(i, tr)
        {
            var td_chk = $(tr).find("td")[5];
            var chk = $(td_chk).find("input")[0];
            var td_data = $(tr).find("td")[0];
            var p_data = $(td_data).find("p")[0];            
            var data = $(p_data).html();
            data = JSON.parse(data);

            if(chk != undefined)
                if( $(chk).is(":checked") )
                {
                    arreglo_idPedido.push(data.idPedido);
                    contador++;
                }
        });

        if(contador <= 0)
            Materialize.toast("Elija un pedido");
        else
        {
            $('#estadoNuevo').val( $('#estadoNuevo option:first').val() );
            $('#modalCambiarEstado').modal('open');
        }
    });

    $('#btnCambiarEstado').click(function()
    {        
        var data = JSON.stringify(arreglo_idPedido);

        $.ajax(
        {
            type: "POST",
            url: IP + "/pedido/estadoPedido/" + $('#estadoNuevo').val(),
            dataType: 'json',
            data: data,
            contentType:"application/json",
            success: function(respuesta)
            {
                if(respuesta.error)
                    Materialize.toast(respuesta.mensaje, 5000);
                else
                {
                    listarPedidos();
                    $('#modalCambiarEstado').modal('close');
                }
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
            }
        });        
    });
}

function listarPedidos()
{
    $('#tbMisPedidos tbody').empty();

    var url = IP + "/pedido/readPedidoFiltros/";
    var data =
    {
        fecha:    $('#fechaFiltro').val(),
        idEstado: $('#estadoFiltro').val(),
        cliente:  $('#clienteFiltro').val(),
    };

    $.get(url, data,function(respuesta)
    {

        dataPedido = respuesta;
        $.each(respuesta, function(i, x)
        {
            var cli = x.idClienteBean;
            var tr =    ''  +
                        '<tr>' +
                            '<td>' +
                                (i+1) +
                                '<p style="display: none;">' +
                                    JSON.stringify(x) +
                                '</p>' +
                            '</td>' +
                            '<td>' + x.fecha_pedido             				        + '</td>' +
                            '<td>' + 'S/. ' + parseFloat(x.total).toFixed(2)            + '</td>' +
                            '<td>' + x.idEstadoBean.descripcion 				        + '</td>' +
                            '<td>' + cli.nombre + ' - Documento: '  + cli.nro_documento + '</td>' +
                            '<td>' +
                                '<p>' +
                                    '<input type="checkbox" id="chk_pedido_' + i + '" />' +
                                    '<label for="chk_pedido_' + i + '"></label>' +
                                '</p>' +
                            '</td>' +
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
		dataDetallePedido = respuesta;
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