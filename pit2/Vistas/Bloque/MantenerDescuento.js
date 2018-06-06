var objUsuario;

$(function()
{
	validarUsuario();
    eventos();
    cargarCombos();
	listarDescuentos();
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
    $('#btnCrearDescuento').click(function()
    {
        $('#txtDescuentoC').val('');
        $('#txtPrecioC').val('');
        $('#cboCategoriaC').val( $('#cboCategoriaC option:first').val() );
        $('#cboMarcaC').val( $('#cboMarcaC option:first').val() );

        $('#modalCrearDescuento').modal('open');
    });

    $('#formularioCrearDescuento').submit(function(e)
    {
        e.preventDefault();

        var idUsuarioBean               = new Object();
        idUsuarioBean['idUsuario']      = objUsuario.idUsuario;

        var data = 
        {
            descripcion: $('#txtDescripcionC').val(),
            porcentaje: $('#txtPorcentajeC').val(),
            fe_inicio: $('#txtFe_InicioC').val(),
            fe_termino: $('#txtFe_TerminoC').val(),
            idUsuarioBean: idUsuarioBean
        };

        data = JSON.stringify(data);

        $.ajax(
        {
            type: "POST",
            url: IP + "/descuento/createDescuento",
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

    $('#formularioActualizarDescuento').submit(function(e)
    {
        e.preventDefault();

        var idUsuarioBean               = new Object();
        idUsuarioBean['idUsuario']      = objUsuario.idUsuario;

        var data = 
        {
        	idDescuento: $('#txtIdDescuentoA').val(),
            descripcion: $('#txtDescripcionA').val(),
            porcentaje: $('#txtPorcentajeA').val(),
            fe_inicio: $('#txtFe_InicioA').val(),
            fe_termino: $('#txtFe_TerminoA').val(),
            idUsuarioBean: idUsuarioBean
        };

        data = JSON.stringify(data);

        $.ajax(
        {
            type: "POST",
            url: IP + "/descuento/updateDescuento",
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

function cargarCombos()
{
    $.ajax(
    {
        type: "GET",
        url: IP + "/combo/readMarca",
        data: [],
        success: function(respuesta)
        {
            $("select[name*='cboMarca']").empty().append('<option value="" disabled selected>[Seleccione]</option>');

            respuesta.map(function(x, i)
            {
                $("select[name*='cboMarca']").append( new Option(x.descripcion,x.idMarca) );
            });
        },
        error: function(e1, e2, e3)
        {
            Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
        }
    });

    $.ajax(
    {
        type: "GET",
        url: IP + "/combo/readCategoria",
        data: [],
        success: function(respuesta)
        {
            $("select[name*='cboCategoria']").empty().append('<option value="" disabled selected>[Seleccione]</option>');

            respuesta.map(function(x, i)
            {
                $("select[name*='cboCategoria']").append( new Option(x.descripcion,x.idCategoria) );
            });
        },
        error: function(e1, e2, e3)
        {
            Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
        }
    });
}

function listarDescuentos()
{
	var url = IP + '/descuento/readDescuento';		

    $('#tbDescuento tbody').empty();

    $.get(url, function(respuesta)
    {    	
        $.each(respuesta, function(i, x)
        {
            var tr =    ''  +
                        '<tr>' +
                            '<td>' + (i+1)  						+ '</td>' +
							'<td>' + x.descripcion              	+ '</td>' +
							'<td>' + x.porcentaje  + '%'			+ '</td>' +
                            '<td>' + convertirFecha(x.fe_inicio)	+ '</td>' +
                            '<td>' + convertirFecha(x.fe_termino) 	+ '</td>' 	+
                            '<td>' + x.idEstadoBean.descripcion 	+ '</td>' +
                            '<td>' + 
                                '<a onclick="modalActualizar(this)" href="javascript:;">'+
                                    '<i class="fa fa-history" aria-hidden="true"></i>' +
                                    '<p style="display: none;">' + JSON.stringify(x) + '</p>' +
                                '</a>' +
                                '&nbsp;&nbsp;' +
            					'<a onclick="eliminarDescuento(' + x.idDescuento + ')" href="javascript:;">'+
            						'<i class="fa fa-trash" aria-hidden="true"></i>' +
            					'</a>' +
            				'</td>' +
                        '</tr>';


            $('#tbDescuento').append(tr);
        });
    });
}

function modalActualizar(source)
{
    var td    = $(source).parents("td")[0];
    var p     = $(td).find("p")[0];
    var datos = $(p).html();
    datos     = JSON.parse(datos);

    $('#txtIdDescuentoA').val( datos.idDescuento );
    $('#txtDescripcionA').val( datos.descripcion );
    $('#txtPorcentajeA').val( datos.porcentaje );
    $('#txtFe_InicioA').val( datos.fe_inicio );
    $('#txtFe_TerminoA').val( datos.fe_termino );
    $('#txtEstadoA').val( datos.idEstadoBean.descripcion );

    Materialize.updateTextFields();

    $('#modalActualizarDescuento').modal('open');
}

function eliminarDescuento(idDescuento)
{    
    var rpta = confirm("¿Desea eliminar este descuento?");
    if( rpta )
    {
        $.ajax(
        {
            type: "POST",
            url: IP + "/descuento/deleteDescuento/" + idDescuento,
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

function convertirFecha(fecha)
{
	var nueva = fecha.split("-");
	return nueva[2] + "-" + nueva[1] + "-" + nueva[0];
}