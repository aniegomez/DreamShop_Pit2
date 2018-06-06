var objUsuario;

$(function()
{
	validarUsuario();
    eventos();
    cargarCombos();
	listarEmpleados();
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

function cargarCombos()
{
    $.when(
        $.ajax(
        {
            type: "GET",
            url: IP + "/combo/readTipoDocumento",
            data: [],
            success: function(respuesta)
            {
                respuesta.map(function(x, i)
                {
                    $('.idTipoDocumento').append( new Option(x.descripcion,x.idTipoDocumento) );
                });
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
            }
        }),

        $.ajax(
        {
            type: "GET",
            url: IP + "/combo/readEstadoCivil",
            data: [],
            success: function(respuesta)
            {
                respuesta.map(function(x, i)
                {
                    $('.idEstadoCivil').append( new Option(x.descripcion,x.idEstadoCivil) );
                });
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
            }
        }),

        $.ajax(
        {
            type: "GET",
            url: IP + "/combo/readRol",
            data: [],
            success: function(respuesta)
            {
                respuesta.map(function(x, i)
                {
                    $('.idRol').append( new Option(x.descripcion,x.idRol) );
                });
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
            }
        })
    ).then(function(data){        });
}

function eventos()
{
    $('#btnCrearEmpleado').click(function()
    {
        $('#txtDescripcionC').val('');
        $('#modalCrearEmpleado').modal('open');
    });

    $('#formularioCrearEmpleado').submit(function(e)
    {
        e.preventDefault();

        var idTipoDocumentoBean                 = new Object();
        idTipoDocumentoBean['idTipoDocumento']  = $('#idTipoDocumentoC').val();

        var idEstadoCivilBean                   = new Object();
        idEstadoCivilBean['idEstadoCivil']      = $('#idEstadoCivilC').val();

        var idUsuarioBean                       = new Object();
        idUsuarioBean['idUsuario']              = objUsuario.idUsuario;

        var data = 
        {
            apellido_p:             $('#apellido_pC').val(),
            apellido_m:             $('#apellido_mC').val(),
            nombre:                 $('#nombreC').val(),
            idTipoDocumentoBean:    idTipoDocumentoBean,
            nro_documento:          $('#nrodocumentoC').val(),
            correo:                 $('#correoC').val(),
            idEstadoCivilBean:      idEstadoCivilBean,
            idUsuarioBean:          idUsuarioBean
        };

        console.log(data);
        data = JSON.stringify(data);

        $.ajax(
        {
            type: "POST",
            url: IP + "/empleado/createEmpleado",
            dataType: 'json',
            data: data,
            contentType:"application/json",
            success: function(respuesta)
            {
                if(respuesta.error)
                    Materialize.toast(respuesta.mensaje, 5000);
                else
                    location.reload();
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
            }
        });
    });

    $('#formularioActualizarEmpleado').submit(function(e)
    {
        e.preventDefault();

        var idTipoDocumentoBean                 = new Object();
        idTipoDocumentoBean['idTipoDocumento']  = $('#idTipoDocumentoA').val();

        var idEstadoCivilBean                   = new Object();
        idEstadoCivilBean['idEstadoCivil']      = $('#idEstadoCivilA').val();

        var idEstadoBean                        = new Object();
        idEstadoBean['idEstado']                = $('#idEstadoA').val();

        var usu_creaBean                       = new Object();
        usu_creaBean['idUsuario']              = objUsuario.idUsuario;

        var data = 
        {
            idEmpleado:             $('#idEmpleadoA').val(),
            apellido_p:             $('#apellido_pA').val(),
            apellido_m:             $('#apellido_mA').val(),
            nombre:                 $('#nombreA').val(),
            idTipoDocumentoBean:    idTipoDocumentoBean,
            nro_documento:          $('#nrodocumentoA').val(),
            correo:                 $('#correoA').val(),
            idEstadoCivilBean:      idEstadoCivilBean,
            idEstadoBean:           idEstadoBean,
            usu_creaBean:           usu_creaBean            
        };

        console.log(data);

        data = JSON.stringify(data);

        $.ajax(
        {
            type: "POST",
            url: IP + "/usuario/updateEmpleado",
            dataType: 'json',
            data: data,
            contentType:"application/json",
            success: function(respuesta)
            {
                if(respuesta.error)
                    Materialize.toast(respuesta.mensaje, 5000);
                else
                    location.reload();
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
            }
        });
    });

    $('#formularioCrearUsuario').submit(function(e)
    {
        e.preventDefault();

        var idEmpleadoBean              = new Object();
        idEmpleadoBean['idEmpleado']    = $('#idEmpleadoUC').val();

        var idRolBean                   = new Object();
        idRolBean['idRol']              = $('#idRolC').val();

        var usu_creaBean                = new Object();
        usu_creaBean['idUsuario']       = objUsuario.idUsuario;

        var data = 
        {
            usuario:        $('#usuarioC').val(),
            clave:          $('#claveC').val(),
            idRolBean:      idRolBean,
            idEmpleadoBean: idEmpleadoBean,
            usu_creaBean:   usu_creaBean
        };

        data = JSON.stringify(data);

        $.ajax(
        {
            type: "POST",
            url: IP + "/usuario/createUsuario",
            dataType: 'json',
            data: data,
            contentType:"application/json",
            success: function(respuesta)
            {
                if(respuesta.error)
                    Materialize.toast(respuesta.mensaje, 5000);
                else
                    location.reload();
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
            }
        });
    });

    $('#formularioActualizarUsuario').submit(function(e)
    {
        e.preventDefault();

        var idEmpleadoBean              = new Object();
        idEmpleadoBean['idEmpleado']    = $('#idEmpleadoUA').val();

        var idRolBean                   = new Object();
        idRolBean['idRol']              = $('#idRolA').val();

        var usu_creaBean                = new Object();
        usu_creaBean['idUsuario']       = objUsuario.idUsuario;

        var data = 
        {
            idUsuario:      $('#idUsuarioA').val(),
            usuario:        $('#usuarioA').val(),
            clave:          $('#claveA').val(),
            idRolBean:      idRolBean,
            idEmpleadoBean: idEmpleadoBean,
            usu_creaBean:   usu_creaBean
        };

        data = JSON.stringify(data);

        $.ajax(
        {
            type: "POST",
            url: IP + "/usuario/updateUsuario",
            dataType: 'json',
            data: data,
            contentType:"application/json",
            success: function(respuesta)
            {
                if(respuesta.error)
                    Materialize.toast(respuesta.mensaje, 5000);
                else
                    location.reload();
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
            }
        });
    });
}

function listarEmpleados()
{
	var url = IP + '/empleado/readEmpleado';
	
    $.get(url, function(respuesta)
    {
        $.each(respuesta, function(i, x)
        {
            var tr =    ''  +
                        '<tr>' +
                            '<td>' + (i+1)  		+ '</td>' +
							'<td>' + x.apellido_p + ' ' + x.apellido_m + ' ' + x.nombre	                    + '</td>' +
                            '<td>' + x.idTipoDocumentoBean.descripcion + ': ' + x.nro_documento             + '</td>' +
                            '<td>' + x.idEstadoCivilBean.descripcion                                        + '</td>' +
                            '<td>' + ( (x.correo == null) ? '' : x.correo )                                 + '</td>' +
                            '<td>' + ( (x.idUsuarioBean.usuario == null)
                                        ?   '<a href="javascript:;" onclick="agregarUsuario(' + x.idEmpleado + ')">' +
                                                '<i class="fa fa-plus" aria-hidden="true"></i>' +
                                            '</a>'
                                        :   '<a href="javascript:;" onclick="actualizarUsuario(this)">' +
                                                x.idUsuarioBean.usuario ) +
                                                '<p style="display: none;">' + JSON.stringify(x) + '</p>' +
                                            '</a>' +
                            '</td>' +
                            '<td>' + x.idEstadoBean.descripcion                                             + '</td>' +
                            '<td>' + 
                                '<a onclick="modalActualizar(this)" href="javascript:;">'+
                                    '<i class="fa fa-history" aria-hidden="true"></i>' +
                                    '<p style="display: none;">' + JSON.stringify(x) + '</p>' +
                                '</a>' +
                                '&nbsp;&nbsp;' +
            					'<a onclick="eliminarEmpleado(' + x.idEmpleado + ')" href="javascript:;">'+
            						'<i class="fa fa-trash" aria-hidden="true"></i>' +
            					'</a>' +
            				'</td>' +
                        '</tr>';


            $('#tbEmpleado').append(tr);
        });
    });
}

function modalActualizar(source)
{
    var td    = $(source).parents("td")[0];
    var p     = $(td).find("p")[0];
    var datos = $(p).html();
    datos     = JSON.parse(datos);

    $('#idEmpleadoA').val( datos.idEmpleado );
    $('#apellido_pA').val( datos.apellido_p );
    $('#apellido_mA').val( datos.apellido_m );
    $('#nombreA').val( datos.nombre );
    $('#idTipoDocumentoA').val(datos.idTipoDocumentoBean.idTipoDocumento).change();
    $('#nrodocumentoA').val( datos.nro_documento );
    $('#correoA').val( datos.correo );
    $('#idEstadoCivilA').val(datos.idEstadoCivilBean.idEstadoCivil).change();
    $('#idEstadoA').val(datos.idEstadoBean.idEstado).change();

    Materialize.updateTextFields();

    $('#modalActualizarEmpleado').modal('open');
}

function eliminarEmpleado(idEmpleado)
{    
    var rpta = confirm("¿Desea eliminar este empleado?");
    if( rpta )
    {
        $.ajax(
        {
            type: "POST",
            url: IP + "/empleado/deleteEmpleado/" + idEmpleado,
            dataType: 'json',
            data: {},
            contentType:"application/json",
            success: function(respuesta)
            {
                if(respuesta.error)
                    Materialize.toast(respuesta.mensaje, 5000);
                else
                    location.reload();
            },
            error: function(e1, e2, e3)
            {
                Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
            }
        });    
    }
}

function agregarUsuario(idEmpleado)
{
    $('#idEmpleadoUC').val(idEmpleado);
    $('#modalCrearUsuario').modal("open");
}

function actualizarUsuario(source)
{    
    var td    = $(source).parents("td")[0];
    var p     = $(td).find("p")[0];
    var datos = $(p).html();
    datos     = JSON.parse(datos);

    $('#idEmpleadoUA').val(datos.idEmpleado);
    $('#idUsuarioA').val(datos.idUsuarioBean.idUsuario);
    $('#nombreEmpleadoA').html( datos.apellido_p + ' ' + datos.apellido_m + ' ' + datos.nombre);
    $('#usuarioA').val(datos.idUsuarioBean.usuario);
    $('#idRolA').val(datos.idUsuarioBean.idRolBean.idRol).change();

    $('#modalActualizarUsuario').modal("open");
}