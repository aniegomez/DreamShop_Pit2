$(function()
{
    cargarProductos();
});

function cargarProductos()
{               
    $.ajax(
    {
        type: "GET",
        url: IP + "/detalleproducto/readProductoTop",                    
        data: [],
        success: function(respuesta)
        {
            var tr = "";
            var td = "";
            var fotos_x_fila = 5;

            var tamanio = respuesta.length;
            var residuo = tamanio % fotos_x_fila;

            respuesta.map(function(x, i)
            {
                var src;
                if(x.foto_ruta == null || x.foto_ruta == '')
                    src = './Resources/imagenes/default.png';
                else
                    src = 'data:image/png;base64,' + x.foto_ruta + '"';

                td +=   '<td>' +
                '<a onclick="anadirCarrito(this)" href="javascript:;">' +
                    '<img width="100" height="100" style="border-radius: 20% !important;" src="' + src + '">' +
                    '<br><br>' +
                    '<span>' + x.idProductoBean.descripcion + '</span>' +
                    '<p style="display: none">' + JSON.stringify(x) + '</p>' +
                '</a>' + '&nbsp;&nbsp;' +

                '<a href="javascript:;" onclick="agregarFavorito(' + x.idProductoBean.idProducto + ',' + x.idColorBean.idColor + ')" style="color: green;">' +
                    '<i class="fa fa-star" aria-hidden="true"></i>'
                +'</a></br>' +

                '<span>Marca: ' + x.idProductoBean.idMarcaBean.descripcion + '</span><br>' +
                '<span>Color: ' + x.idColorBean.descripcion+ '</span><br>' +
                '<span>Cantidad: ' + x.cantidad + '</span><br>' +
                '<span>Precio: S/' + x.idProductoBean.precio + '</span>';

                if( x.idDescuentoBean.porcentaje > 0)                
                    td += '<br><span style="color: red;">Descuento: S/' + x.idProductoBean.precio * x.idDescuentoBean.porcentaje / 100 + '</span>';

                td += '</td>';

                //Si solo hay un valor lo agregamos directamente
                if(tamanio == 1)
                    $('#tableProductos').append('<tr>' + td +'</tr>');
                //Sino Si es múltiplo del valor asignado en la parte superior (fotos_x_fila), no es válido la primera vez
                //O si es la última vuelta agregar (Ya que puede que hayan registros sobrantes)
                //Por ejemplo: Si hay 12 registros solo tomaría 12, por eso la última condición
                //Se suma i + 1 ya que i siempre empieza desde 0 y para este caso necesitamos hacer operaciones con el 1
                else if( ((i + 1) % fotos_x_fila == 0 && i != 0) || (i+1) == tamanio )
                {
                    $('#tableProductos').append('<tr>' + td +'</tr>');
                    td = "";
                }
            });
        },
        error: function(e1, e2, e3)
        {
            Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
        }
    });
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
    objeto['porcentaje']    = json.idDescuentoBean.porcentaje;
    objeto['idDescuento']   = json.idDescuentoBean.idDescuento;

    return objeto;
}

function agregarFavorito(idProducto, idColor)
{
    var objCliente = sessionStorage.getItem("objCliente");

    if(objCliente == null)
        Materialize.toast("Debe iniciar sesión para agregar un favorito a su cuenta", 1500);
    else
    {
        var rpta = confirm("Desea añadir este producto a su lista de favoritos?");

        if(!rpta)
            return;

        objCliente = JSON.parse(objCliente);

        var idClienteBean                       = new Object();
        idClienteBean["idCliente"]              = objCliente.idCliente;

        var idProductoBean                      = new Object();
        idProductoBean["idProducto"]            = idProducto;

        var idColorBean                         = new Object();
        idColorBean["idColor"]                  = idColor;

        var detalle_ProductoBean                = new Object();
        detalle_ProductoBean['idProductoBean']  = idProductoBean;
        detalle_ProductoBean['idColorBean']     = idColorBean;

        var data =
        {
            idClienteBean:        idClienteBean,
            detalle_ProductoBean: detalle_ProductoBean
        };

        data = JSON.stringify(data);        

        $.ajax(
        {
            type: "POST",
            url: IP + "/favorito/createFavorito",
            data: data,
            dataType: 'json',
            contentType: "application/json",
            success: function(respuesta)
            {
                if(respuesta.error)
                    Materialize.toast("Producto ya se encuentra en su lista de favoritos", 1500);
                else
                    Materialize.toast("Se agregó a su lista de favoritos");
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
            }
        });
    }
}