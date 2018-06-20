$(function()
{            
    cargarProductos();
});

function cargarProductos()
{               
    var idCategoria = getCategoria();
    $.ajax(
    {
        type: "GET",
        url: IP + "/detalleproducto/readProductoPorCategoria/" + idCategoria,
        data: [],
        success: function(respuesta)
        {
            if(respuesta.length == 0)                    
            {
                $('#titulo').html("Lo sentimos<br><br>Categoría eleccionada no tiene productos");
            }
            else
            {
                $('#titulo').html("Categoría: " + respuesta[0].idProductoBean.idCategoriaBean.descripcion);

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
                    '<span>' + x.idProductoBean.descripcion + '</span><br>' +
                    '<p style="display: none">' + JSON.stringify(x) + '</p>' +
                    '</a>' +

                    '<span>Marca: ' + x.idProductoBean.idMarcaBean.descripcion + '</span><br>' +                            
                    '<span>Cantidad: ' + x.cantidad + '</span><br>' +
                    '<span>Precio: S/' + x.idProductoBean.precio + '</span><br>';

                    if( x.idDescuentoBean.porcentaje > 0)                
                        td += '<span style="color: red;">Descuento: S/' + x.idProductoBean.precio * x.idDescuentoBean.porcentaje / 100 + '</span><br>';

                    td += '' +
                    '<span>Color: ' + x.idColorBean.descripcion + '</span><br>' +
                    '<a href="javascript:;" onclick="verColores(' + x.idProductoBean.idProducto + ')">Ver más colores disponibles</a><br>' +
                    '</td>';

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
            }
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
function verColores(idProducto)
{            
    $.ajax(
    {
        type: "GET",
        url: IP + "/detalleproducto/readProductoPorProducto/" + idProducto,
        data: [],
        success: function(respuesta)
        {
            if(respuesta.length == 0)                    
            {
                $('#detalleNombreProducto').html("No hay más colores en este producto.");
            }
            else
            {
                $("#tableDetalleProducto").empty();
                $('#detalleNombreProducto').html
                (
                    "Producto:  " + respuesta[0].idProductoBean.descripcion  + "<br>" +
                    "Marca:     " + respuesta[0].idProductoBean.idMarcaBean.descripcion  + "<br>" +
                    "Precio:    " + respuesta[0].idProductoBean.precio 
                );

                var tr = "";
                var td = "";
                var fotos_x_fila = 3;

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
                    '<span>' + x.idProductoBean.descripcion + '</span><br>' +
                    '<p style="display: none">' + JSON.stringify(x) + '</p>' +
                    '</a>' +

                    '<span>Cantidad: ' + x.cantidad + '</span><br>' +
                    '<span>Color: ' + x.idColorBean.descripcion + '</span><br>';
                    
                    if( x.idDescuentoBean.porcentaje > 0)
                        td += '<span style="color: red;">Descuento: ' + x.idDescuentoBean.porcentaje + '</span><br>';

                    td += '</td>';

                    //Si solo hay un valor lo agregamos directamente
                    if(tamanio == 1)
                        $('#tableDetalleProducto').append('<tr>' + td +'</tr>');
                    //Sino Si es múltiplo del valor asignado en la parte superior (fotos_x_fila), no es válido la primera vez
                    //O si es la última vuelta agregar (Ya que puede que hayan registros sobrantes)
                    //Por ejemplo: Si hay 12 registros solo tomaría 12, por eso la última condición
                    //Se suma i + 1 ya que i siempre empieza desde 0 y para este caso necesitamos hacer operaciones con el 1
                    else if( ((i + 1) % fotos_x_fila == 0 && i != 0) || (i+1) == tamanio )
                    {
                        $('#tableDetalleProducto').append('<tr>' + td +'</tr>');
                        td = "";
                    }
                });

                $('#modalMasColores').modal('open');
            }
        },
        error: function(e1, e2, e3)
        {
            Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
        }
    });
}

function getCategoria()
{
    try
    {
        if(sessionStorage.getItem("idCategoria") == null)
            return 0;
        return sessionStorage.getItem("idCategoria");
    }
    catch(error)
    {return 0;}
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