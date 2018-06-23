var objCliente;

$(function()
{
	validarCliente();
	listarFavoritos();
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

function listarFavoritos()
{
	var url = IP + '/favorito/readFavoritoPorCliente/' + objCliente.idCliente;
	
    $.get(url, function(respuesta)
    {
        $.each(respuesta, function(i, x)
        {
			var producto 	= x.detalle_ProductoBean.idProductoBean;
			var color 		= x.detalle_ProductoBean.idColorBean;
			var descuento 	= x.detalle_ProductoBean.idDescuentoBean;
			
            var tr =    ''  +
                        '<tr>' +
                            '<td>' + (i+1)  																						+ '</td>' +
							'<td>' + producto.idCategoriaBean.descripcion  															+ '</td>' +
							'<td>' + producto.idMarcaBean.descripcion  																+ '</td>' +
                            '<td>' +
                                ( ( x.detalle_ProductoBean.cantidad <= 0) ? producto.descripcion :
								'<a onclick="anadirCarrito(this)" href="javascript:;">' +
				                    '<span>' + producto.descripcion + '</span>' +
				                    '<p style="display: none">' + JSON.stringify(x.detalle_ProductoBean) + '</p>' +
				                '</a>' ) +
                            '</td>' +
            				'<td>' +
            					'<a onclick="verFotoProducto(' + producto.idProducto + ',' + color.idColor + ')" href="javascript:;">' +
            						color.descripcion +
            					'</a>' +
            				'</td>'+
            				'<td>' + x.detalle_ProductoBean.cantidad																+ '</td>' +
							'<td>' + 'S/. ' + parseFloat( producto.precio ).toFixed(2)												+ '</td>' +
							'<td>' + 'S/. ' + ( ( descuento == null ) ? "0.00" : parseFloat( producto.precio * descuento.porcentaje / 100 ) .toFixed(2)	) 	+ '</td>' +
            				'<td>' +
            					'<a onclick="eliminarProducto(' + producto.idProducto + ',' + color.idColor + ')" href="javascript:;">'+
            						'<i class="fa fa-trash" aria-hidden="true"></i>' +
            					'</a>' +
            				'</td>' +
                        '</tr>';


            $('#tbFavoritos').append(tr);
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

function eliminarProducto(idProducto, idColor)
{
	var rpta = confirm("Seguro que desea eliminar este producto del carrito?");

	if(rpta)
	{
		$.ajax(
		{
			type: "POST",
			url: IP + "/favorito/deleteFavorito/" + objCliente.idCliente + "/" + idProducto + "/" + idColor,
			dataType: 'json',
			data: {},
			contentType:"application/json",
			success: function(respuesta)
			{
				if(respuesta.error)
					Materialize.toast(respuesta.mensaje, 5000);
				else
					window.location.reload();
			},
			error: function(e1, e2, e3)
			{
				Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
			}
		});		        
    }
}

function anadirCarrito(source)
{
    var td   = $(source).parent("td");
    var p    = $(td).find("p")[0];
    var json = JSON.parse($(p).html());

    var rpta = confirm('Desea añadir "' + json.idProductoBean.descripcion + '" al carrito de compras?');
    if(rpta)
    {                   
        if( sessionStorage.getItem("listaCarrito") == null)
        {
            var arreglo = new Array();
            var objeto  = getObjetoProducto(json, 1);

            arreglo.push(objeto);
            sessionStorage.setItem("listaCarrito", JSON.stringify(arreglo));
            $('#miCarrito').html(JSON.parse( sessionStorage.getItem("listaCarrito") ).length) ;
            Materialize.toast('Se añadió al carrito de compras.', 5000);
        }
        else
        {
            var agregar = true;
            var arreglo = JSON.parse(sessionStorage.getItem("listaCarrito"));
            var objeto  = getObjetoProducto(json, 1);

            $.each(arreglo, function(i, x)
            {
                //Si se repite el producto y color ya no se añade
                if( x.idProducto == objeto.idProducto && x.idColor == objeto.idColor )
                    agregar = false;
            });

            if(agregar)
            {
                arreglo.push(objeto);
                sessionStorage.setItem("listaCarrito", JSON.stringify(arreglo));
                $('#miCarrito').html(JSON.parse( sessionStorage.getItem("listaCarrito") ).length) ;
                Materialize.toast('Se añadió al carrito de compras.', 5000);
            }
            else
                Materialize.toast('Producto ya se encuentra en el carrito de compras.', 5000);
        }
    }
}

function getObjetoProducto(json, cantidad)
{
    var objeto              = new Object();
    objeto['idProducto']    = json.idProductoBean.idProducto;
    objeto['producto']      = json.idProductoBean.descripcion;
    objeto['idCategoria']   = json.idProductoBean.idCategoriaBean.idCategoria;
    objeto['categoria']     = json.idProductoBean.idCategoriaBean.descripcion;
    objeto['idMarca']       = json.idProductoBean.idMarcaBean.idMarca;
    objeto['marca']         = json.idProductoBean.idMarcaBean.descripcion;
    objeto['idColor']       = json.idColorBean.idColor;
    objeto['color']         = json.idColorBean.descripcion;
    objeto['cantidad']      = cantidad;
    objeto['precio']        = json.idProductoBean.precio;
    objeto['porcentaje']     = json.idDescuentoBean.porcentaje;


    return objeto;
}