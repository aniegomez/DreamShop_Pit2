var objUsuario;

$(function()
{
	validarUsuario();
    eventos();
	listarCategorias();
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
    $('#btnCrearCategoria').click(function()
    {
        $('#txtDescripcionC').val('');
        $('#modalCrearCategoria').modal('open');
    });

    $('#formularioCrearCategoria').submit(function(e)
    {
        e.preventDefault();

        var idUsuarioBean               = new Object();
        idUsuarioBean['idUsuario']      = objUsuario.idUsuario;

        var data = 
        {
            descripcion: $('#txtDescripcionC').val(),
            idUsuarioBean: idUsuarioBean
        };

        data = JSON.stringify(data);

        $.ajax(
        {
            type: "POST",
            url: IP + "/categoria/createCategoria",
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

    $('#formularioActualizarCategoria').submit(function(e)
    {
        e.preventDefault();

        var data = 
        {
            idCategoria: $('#txtIdCategoriaA').val(),
            descripcion: $('#txtDescripcionA').val()
        };

        data = JSON.stringify(data);		

        $.ajax(
        {
            type: "POST",
            url: IP + "/categoria/updateCategoria",
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

function listarCategorias()
{
	var url = IP + '/categoria/readCategoria';
	
    $.get(url, function(respuesta)
    {
        $.each(respuesta, function(i, x)
        {
            var tr =    ''  +
                        '<tr>' +
                            '<td>' + (i+1)  		+ '</td>' +
							'<td>' + x.descripcion	+ '</td>' +
                            '<td>' + 
                                '<a onclick="modalActualizar(this)" href="javascript:;">'+
                                    '<i class="fa fa-history" aria-hidden="true"></i>' +
                                    '<p style="display: none;">' + JSON.stringify(x) + '</p>' +
                                '</a>' +
                                '&nbsp;&nbsp;' +
            					'<a onclick="eliminarCategoria(' + x.idCategoria + ')" href="javascript:;">'+
            						'<i class="fa fa-trash" aria-hidden="true"></i>' +
            					'</a>' +
            				'</td>' +
                        '</tr>';


            $('#tbCategoria').append(tr);
        });
    });
}

function modalActualizar(source)
{
    var td    = $(source).parents("td")[0];
    var p     = $(td).find("p")[0];
    var datos = $(p).html();
    datos     = JSON.parse(datos);

    $('#txtIdCategoriaA').val( datos.idCategoria );
    $('#txtDescripcionA').val( datos.descripcion );    

    Materialize.updateTextFields();

    $('#modalActualizarCategoria').modal('open');
}

function eliminarCategoria(idCategoria)
{    
    var rpta = confirm("¿Desea eliminar esta categoría?");
    if( rpta )
    {
        $.ajax(
        {
            type: "POST",
            url: IP + "/categoria/deleteCategoria/" + idCategoria,
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