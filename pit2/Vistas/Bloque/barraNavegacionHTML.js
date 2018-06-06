var IP = "http://127.0.0.1:9090";

mostrarBarraNavegacion();

$(function()
{
    var objBarraNavegacion = sessionStorage.getItem("objBarraNavegacion");
    
    if(objBarraNavegacion == null)
    {
        $.ajax(
        {
            type: "GET",
            url: IP + "/combo/readCategoriaActiva",
            data: [],
            success: function(respuesta)
            {
                var categorias;
                respuesta.map(function(x, i)
                {

                    categorias = '' +
                    '<li class="bold">' +
                        '<a onclick="abrirCategoria(' + x.idCategoria +')" class="collapsible-header waves-effect waves-teal" href="javascript:;">' +
                            '<i aria-hidden="true" class="fa fa-home"></i>' +
                            x.descripcion +
                        '</a>' +
                    '</li>';

                    $('#barraNavegacion').append(categorias);
                });

                sessionStorage.setItem( "objBarraNavegacion", JSON.stringify(respuesta) );
            },
            error: function(e1, e2, e3)
            {
                alert('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)');
            }
        });
    }
    else
    {
        var respuesta = JSON.parse(objBarraNavegacion);
        respuesta.map(function(x, i)
        {
            categorias = '' +
            '<li class="bold">' +
                '<a onclick="abrirCategoria(' + x.idCategoria +')" class="collapsible-header waves-effect waves-teal" href="javascript:;">' +
                    '<i aria-hidden="true" class="fa fa-home"></i>' +
                    x.descripcion +
                '</a>' +
            '</li>';

            $('#barraNavegacion').append(categorias);
        });
    }
});

function mostrarBarraNavegacion()
{
    var usuario = '' + 
    '<ul id="opcionesCliente" class="dropdown-content">' +
    '<li><a id="miCuenta" href="javascript:;">Mi Cuenta</a></li>' +
    '<li><a id="misFavoritos" href="javascript:;">Mis Favoritos</a></li>' +
    '<li class="divider"></li>' +
    '<li><a id="misPedidos" href="javascript:;">Mis Pedidos</a></li>' +
    '<li class="divider"></li>' +
    '<li><a id="cambiarClave" href="javascript:;">Cambiar Clave</a></li>' +
    '<li class="divider"></li>' +
    '<li><a id="salir" href="javascript:;">Salir</a></li>' +
    '</ul>';

    var barra = usuario +
    '<nav style="background-color: green; border-top-left-radius: 5px; border-top-right-radius: 5px; height: 40px;">' +
        '<div class="nav-wrapper" style="height: 100%;">' +
        '<a class="button-collapse" data-activates="nav-mobile" href="javascript:;" style="font-size: 20px; display: block; height: 100%; margin-top: -10px;">' +
            '<i class="fa fa-bars"></i>' +
        '</a>' +

        '<ul class="right" style=" margin-top: -10px;">' +
            //Carrito de Compras Siempre va
            '<li>' +
                '<a style="color: white;" href="MiCarrito.html">' +
                    'Mi Carrito ' +
                    '(<span id="miCarrito">0</span>)' +
                '</a>' +
            '</li>';

                var objCliente = sessionStorage.getItem("objCliente");
                //Esto parte varía si ha iniciado sesión o no
                if(sessionStorage.objCliente == null)                    
                {
                    barra += '' +
                    '<li>' +
                        '<a style="color: white;" href="LoginExterno.html">' +
                            'Ingrese' +
                        '</a>' +
                    '</li>' +
                    '<li>' +
                        '<a style="color: white;" href="registro.html">' +
                            'Regístrese' +
                        '</a>' +
                    '</li>' +
                    '<li>' +
                        '<a style="color: white;" href="recuperarClave.html">' +
                            'Recuperar Clave' +
                        '</a>' +
                    '</li>' + 
                    '<li>' +
                        '<a style="color: white;" href="LoginInterno.html">' +
                            'Intranet' +
                        '</a>' +
                    '</li>';
                }
                else
                {
                    objCliente = JSON.parse(objCliente);

                    barra += '' +                
                    '<li>' +
                    '<a class="dropdown-button" href="#!" data-activates="opcionesCliente" style="color: white;">' +
                        '<span id="nombreCliente">' +
                            objCliente.nombre + " " + objCliente.apellido_p + " " + objCliente.apellido_m + "&nbsp;&nbsp;" +
                        '</span>' +
                        '<i class="fas fa-angle-double-down" style="line-height: 0px !important;"></i>' +
                    '</a>' +
                    '</li>';
                }

                barra += '' + 
                '</ul>';

                barra += '' +
            //Categorías (Siempre va)
            '<ul class="side-nav grey lighten-2" id="nav-mobile">' +
                '<li>' +
                    '<ul class="collapsible collapsible-accordion" id="barraNavegacion">' +
                        '<li class="bold">' +
                            '<a class="collapsible-header waves-effect waves-teal" href="Inicio.html">' +
                                '<i aria-hidden="true" class="fa fa-home"></i>' +
                                'Inicio' +
                            '</a>' +
                        '</li>' +
                    '</ul>' +                
                '</li>' +
            '</ul>' +            
        '</div>' +
    '</nav>';

    document.write(barra);
}

function abrirCategoria(idCategoria)
{
    sessionStorage.setItem("idCategoria", idCategoria);
    window.location = "Productos.html";
}