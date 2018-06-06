var IP = "http://127.0.0.1:9090";

mostrarBarraNavegacion();

function mostrarBarraNavegacion()
{
    var usuario = '' + 
    '<ul id="opcionesUsuario" class="dropdown-content">' +
    '<li>' + '<a href="javascript:;" id="miRol"></a>' + '</li>' +
    '<li class="divider"></li>' +
    // '<li><a id="miCuenta" href="javascript:;">Mi Cuenta</a></li>' +
    // '<li><a id="misFavoritos" href="javascript:;">Mis Favoritos</a></li>' +
    // '<li class="divider"></li>' +
    // '<li><a id="misPedidos" href="javascript:;">Mis Pedidos</a></li>' +
    // '<li class="divider"></li>' +
    // '<li><a id="cambiarClave" href="javascript:;">Cambiar Clave</a></li>' +
    // '<li class="divider"></li>' +
    '<li><a id="salir" href="javascript:;">Salir</a></li>' +
    '</ul>';

    var objUsuario = sessionStorage.getItem("objUsuario");

    var barra = usuario +
    '<nav style="background-color: green; border-top-left-radius: 5px; border-top-right-radius: 5px; height: 40px;">' +
        '<div class="nav-wrapper" style="height: 100%;">';
            
            if(sessionStorage.objUsuario != null)
                barra += '' +
                '<a class="button-collapse" data-activates="nav-mobile" href="javascript:;" style="font-size: 20px; display: block; height: 100%; margin-top: -10px;">' +
                    '<i class="fa fa-bars"></i>' +
                '</a>';

            barra += '' +
            '<ul class="right" style=" margin-top: -10px;">';

            
            //Esto parte varía si ha iniciado sesión o no
            if(sessionStorage.objUsuario == null)
            {                
                barra += '' +
                    '<li>' +
                        '<a style="color: white;" href="Inicio.html">' +
                        'Extranet' +
                        '</a>' +
                    '</li>';
            }
            else
            {
                objUsuario = JSON.parse(objUsuario);
                var objEmpleado = objUsuario.idEmpleadoBean;

                barra += '' +
                    '<li>' +
                        '<a class="dropdown-button" href="#!" data-activates="opcionesUsuario" style="color: white;">' +
                            '<span id="nombreUsuario">' +
                                objEmpleado.nombre + " " + objEmpleado.apellido_p + " " + objEmpleado.apellido_m +
                                "&nbsp;&nbsp;" +
                            '</span>' +
                            '<i class="fas fa-angle-double-down" style="line-height: 0px !important;"></i>' +
                        '</a>' +
                    '</li>';                
            }

            barra += '' + 
            '</ul>' +
            '<ul class="side-nav grey lighten-2" id="nav-mobile">' +
                '<li>' +
                    '<ul class="collapsible collapsible-accordion" id="barraNavegacion">' +
                        '<li class="bold">' +
                            '<a class="collapsible-header waves-effect waves-teal" href="MenuPrincipal.html">' +
                                '<i aria-hidden="true" class="fa fa-home"></i>' +
                                'Inicio' +
                            '</a>' +
                        '</li>' +
                        '<li class="bold">' +
                            '<a class="collapsible-header waves-effect waves-teal" href="MantenerEmpleado.html">' +
                                '<i aria-hidden="true" class="fa fa-home"></i>' +
                                'Mantenimiento Empleado' +
                            '</a>' +
                        '</li>' +
                        '<li class="bold">' +
                            '<a class="collapsible-header waves-effect waves-teal" href="MantenerCategoria.html">' +
                                '<i aria-hidden="true" class="fa fa-home"></i>' +
                                'Mantenimiento Categoria' +
                            '</a>' +
                        '</li>' +
                        '<li class="bold">' +
                            '<a class="collapsible-header waves-effect waves-teal" href="MantenerProducto.html">' +
                                '<i aria-hidden="true" class="fa fa-home"></i>' +
                                'Mantenimiento Producto' +
                            '</a>' +
                        '</li>' +
                        '<li class="bold">' +
                            '<a class="collapsible-header waves-effect waves-teal" href="MantenerDescuento.html">' +
                                '<i aria-hidden="true" class="fa fa-home"></i>' +
                                'Mantenimiento Descuento' +
                            '</a>' +
                        '</li>' +
                        '<li class="bold">' +
                            '<a class="collapsible-header waves-effect waves-teal" href="Pedidos.html">' +
                                '<i aria-hidden="true" class="fa fa-home"></i>' +
                                'Pedidos' +
                            '</a>' +
                        '</li>' +
                        '<li class="bold">' +
                            '<a class="collapsible-header waves-effect waves-teal" href="Clientes.html">' +
                                '<i aria-hidden="true" class="fa fa-home"></i>' +
                                'Clientes' +
                            '</a>' +
                        '</li>' +
                    '</ul>' +
                '</li>' +
            '</ul>' + 
        '</div>' +
    '</nav>';

    document.write(barra);

    if(sessionStorage.objUsuario != null)
        $('#miRol').html( objUsuario.idRolBean.descripcion.toUpperCase() );
}