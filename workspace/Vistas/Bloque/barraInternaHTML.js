var IP = "http://127.0.0.1:9090";
var objUsuario;
var listaPagina;
var barra;

mostrarBarraNavegacion();

function mostrarBarraNavegacion()
{
    var usuario = '' +
    '<ul id="opcionesUsuario" class="dropdown-content">' +
    '<li>' + '<a href="javascript:;" id="miRol"></a>' + '</li>' +
    '<li class="divider"></li>' +
    '<li><a id="salir" href="javascript:;">Salir</a></li>' +
    '</ul>';

    objUsuario  = sessionStorage.getItem("objUsuario");
    listaPagina = sessionStorage.getItem("listaPagina");

    barra = usuario +
    '<nav class="cyan darken-1" style="border-top-left-radius: 5px; border-top-right-radius: 5px; height: 40px;">' +
        '<div class="nav-wrapper" style="height: 100%;">';
            
            if(objUsuario != null)
                barra += '' +
                '<a class="button-collapse" data-activates="nav-mobile" href="javascript:;" style="font-size: 20px; display: block; height: 100%; margin-top: -10px;">' +
                    '<i class="fa fa-bars"></i>' +
                '</a>';

            barra += '' +
            '<ul class="right" style=" margin-top: -10px;">';

            
            //Esto parte varía si ha iniciado sesión o no
            if(objUsuario == null)
            {                
                barra += '' +
                '<li>' +
                    '<a style="color: white;" href="Inicio.html">' +
                        'Extranet' +
                    '</a>' +
                '</li>' +
            '</ul>';
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
                '</li>' +
            '</ul>';
            }

            barra += '' +
            '<ul class="side-nav grey lighten-2" id="nav-mobile">' +
                '<li>' +
                    '<ul class="collapsible collapsible-accordion" id="barraNavegacion">' +
                        '<li class="bold">' +
                            '<a class="collapsible-header waves-effect waves-teal" href="MenuPrincipal.html">' +
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

    if(objUsuario != null)
        $('#miRol').html( objUsuario.idRolBean.descripcion.toUpperCase() );

        
    if(listaPagina != null && objUsuario != null)
    {
        listaPagina = JSON.parse(listaPagina);
        construirMenu();
    }
    else if(listaPagina == null && objUsuario != null)
    {
        var url = IP + "/pagina/readPaginaPorRol/" + objUsuario.idRolBean.idRol;
        $.get(url, function(x)
        {
            listaPagina = x;
            sessionStorage.setItem("listaPagina", JSON.stringify(x));
            construirMenu();
        });
    }
}

function construirMenu()
{
    $.each(listaPagina, function(i, x)
    {        
        var barra = '' +
        '<li class="bold">' +
            '<a class="collapsible-header waves-effect waves-teal" href="' + x.url + '">' +
                '<i aria-hidden="true" class="fa fa-home"></i>' +
                x.descripcion +
            '</a>' +
        '</li>';

        $('#barraNavegacion').append(barra);
    });
}