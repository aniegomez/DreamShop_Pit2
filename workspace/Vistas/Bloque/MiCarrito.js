$(function()
{
	listarCarrito();
});

function listarCarrito()
{
	var listaCarrito = sessionStorage.getItem("listaCarrito");
	if( listaCarrito == null)
    {            	
    	$('#mensaje').html("<center><a href='inicio.html'>Su carrito está vacío</a></center>");
    	$('#tbMicarrito').hide();
    }
    else if( JSON.parse(listaCarrito) == 0)
    {
    	$('#mensaje').html("<center><a href='inicio.html'>Su carrito está vacío</a></center>");
    	$('#tbMicarrito').hide();
    }
    else
    {            	
    	listaCarrito = JSON.parse(listaCarrito);
    	var total = 0.0;

        $('#tbMicarrito tbody').empty();
    	$.each(listaCarrito, function(i, x)
    	{
    		total += parseFloat(calcularTotal(x));
        	var tr = 	'' 	+
        				'<tr>' +
            				'<td>' + (i+1) 								                        + '</td>' +
            				'<td>' + x.marca 							                        + '</td>' +
            				'<td>' + x.categoria 						                        + '</td>' +
            				'<td>' + x.producto 						                        + '</td>' +
            				'<td>' +
            					'<a onclick="verFotoProducto(' + x.idProducto + ',' + x.idColor + ')" href="javascript:;">' +
            						x.color +
            					'</a>' +
            				'</td>'+
            				'<td> S/. ' + parseFloat(x.precio).toFixed(2) 	                    + '</td>' +
                            '<td>' +
                                '<a href="javascript:;" onclick="menosCantidad(' + x.idProducto + ',' + x.idColor + ')">' +
                                    '<i class="fa fa-minus" aria-hidden="true"></i>' +
                                '</a>' + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +

                                x.cantidad + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +

                                '<a href="javascript:;" onclick="masCantidad(' + x.idProducto + ',' + x.idColor + ')">' +
                                    '<i class="fa fa-plus" aria-hidden="true"></i>' + 
                                '</a>' +                                
                            '</td>' +
                            '<td> S/. ' + parseFloat(x.precio *  x.cantidad).toFixed(2)                         + '</td>' +
                            '<td> S/. ' + parseFloat(x.precio *  x.cantidad * x.porcentaje / 100).toFixed(2)    + '</td>' +                            
            				'<td> S/. ' + calcularTotal(x)                                   + '</td>' +
            				'<td>' +
            					'<a onclick="eliminarProducto(' + x.idProducto + ',' + x.idColor + ')" href="javascript:;">'+
            						'<i class="fa fa-trash" aria-hidden="true"></i>' +
            					'</a>' +
            				'</td>' +
        				'</tr>';


        	$('#tbMicarrito').append(tr);
    	});

    	$('#mensaje').html("<center><a onclick='realizarPedido()' href='javascript:;'>Realizar Pedido (S/. " + total.toFixed(2) + ")</a></center>");
    }
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

function eliminarProducto(idProducto, idColor)
{
	var rpta = confirm("Seguro que desea eliminar este producto del carrito?");

	if(rpta)
	{
		var listaCarrito = JSON.parse(sessionStorage.getItem("listaCarrito"));
		var nuevaLista = new Array();

		$.each(listaCarrito, function(i, x)
        {
        	if(x.idProducto == idProducto && x.idColor == idColor){}
        	else
        		nuevaLista.push(x);
        });

        sessionStorage.setItem("listaCarrito", JSON.stringify(nuevaLista));
        window.location.reload();
    }
}

function calcularTotal(x)
{
    var descuento = 100 - x.porcentaje;
	return parseFloat(  (parseFloat(x.precio) * x.cantidad) * descuento / 100  ).toFixed(2);
}		

function realizarPedido()
{
	var objCliente = sessionStorage.getItem("objCliente");
	sessionStorage.setItem("objPedido", true);

	if(objCliente != null)
		window.location = "RealizarPedido.html";
	else
		Materialize.toast('Debe iniciar sesión para continuar con el pedido', 5000);
}

function menosCantidad(idProducto, idColor)
{
    var listaCarrito = JSON.parse(sessionStorage.getItem("listaCarrito"));
    var nuevaLista = new Array();

    $.each(listaCarrito, function(i, x)
    {
        if( x.cantidad <= 0)
            x["cantidad"] = 1;

        if(x.idProducto == idProducto && x.idColor == idColor && x.cantidad > 1)
        {
            x["cantidad"] = x.cantidad - 1;
            nuevaLista.push(x);
        }
        else
            nuevaLista.push(x);
    });

    sessionStorage.setItem("listaCarrito", JSON.stringify(nuevaLista));

    listarCarrito();
}

function masCantidad(idProducto, idColor)
{
    var listaCarrito = JSON.parse(sessionStorage.getItem("listaCarrito"));
    var nuevaLista = new Array();

    $.each(listaCarrito, function(i, x)
    {
        if( x.cantidad <= 0)
            x["cantidad"] = 1;

        if(x.idProducto == idProducto && x.idColor == idColor && x.cantidad < 100)
        {
            x["cantidad"] = x.cantidad + 1;
            nuevaLista.push(x);
        }
        else
            nuevaLista.push(x);
    });

    sessionStorage.setItem("listaCarrito", JSON.stringify(nuevaLista));

    listarCarrito();
}