var objUsuario;
var arreglo_idProducto = [];

$(function()
{
	validarUsuario();
    eventos();
    cargarCombos();
	listarProductos();
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
    $('#btnCrearProducto').click(function()
    {
        $('#txtProductoC').val('');
        $('#txtPrecioC').val('');
        $('#cboCategoriaC').val( $('#cboCategoriaC option:first').val() );
        $('#cboMarcaC').val( $('#cboMarcaC option:first').val() );

        $('#modalCrearProducto').modal('open');
    });

    $('#formularioCrearProducto').submit(function(e)
    {
        e.preventDefault();

        var idCategoriaBean             = new Object();
        idCategoriaBean['idCategoria']  = $('#cboCategoriaC').val();
        var idMarcaBean                 = new Object();
        idMarcaBean['idMarca']          = $('#cboMarcaC').val();
        var idUsuarioBean               = new Object();
        idUsuarioBean['idUsuario']      = objUsuario.idUsuario;

        var data = 
        {
            descripcion: $('#txtProductoC').val(),
            precio: $('#txtPrecioC').val(),
            idCategoriaBean: idCategoriaBean,
            idMarcaBean: idMarcaBean,
            idUsuarioBean: idUsuarioBean
        };

        data = JSON.stringify(data);

        $.ajax(
        {
            type: "POST",
            url: IP + "/producto/createProducto",
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

    $('#formularioActualizarProducto').submit(function(e)
    {
        e.preventDefault();

        var idCategoriaBean             = new Object();
        idCategoriaBean['idCategoria']  = $('#cboCategoriaA').val();
        var idMarcaBean                 = new Object();
        idMarcaBean['idMarca']          = $('#cboMarcaA').val();
        var idUsuarioBean               = new Object();
        idUsuarioBean['idUsuario']      = objUsuario.idUsuario;

        var data = 
        {
            idProducto: $('#txtIdProductoA').val(),
            descripcion: $('#txtProductoA').val(),
            precio: $('#txtPrecioA').val(),
            idCategoriaBean: idCategoriaBean,
            idMarcaBean: idMarcaBean,
            idUsuarioBean: idUsuarioBean
        };

        data = JSON.stringify(data);

        $.ajax(
        {
            type: "POST",
            url: IP + "/producto/updateProducto",
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

    $('#btnModalAsignarDescuento').click(function()
    {
        var contador = 0;
        arreglo_idProducto = [];
        $.each($('#tbProducto tbody').find("tr"), function(i, tr)
        {
            var td       = $(tr).find("td")[5];
            var checkbox = $(td).find("input")[0];

            var td_data    = $(tr).find("td")[7];
            var p_data   = $(td_data).find("p")[0];
            var data     = $(p_data).html();
            data         = JSON.parse(data);

            if(checkbox != undefined)
                if( $(checkbox).is(":checked") )
                {
                    arreglo_idProducto.push(data.idProducto);
                    contador++;
                }

        });

        if(contador == 0)
            Materialize.toast("Elija un producto", 1200);
        else
        {
            var url = IP + '/descuento/readDescuentoActivo';
            
            $.get(url, function(respuesta)
            {
                $('#tbDescuento tbody').empty();

                $.each(respuesta, function(i, x)
                {
                    var tr =    ''  +
                                '<tr>' +
                                    '<td>' +
                                        (i+1) +
                                        '<p style="display: none;">' +
                                            JSON.stringify(x) +
                                        '</p>' +
                                    '</td>' +
                                    '<td>' + x.descripcion  + '</td>' +
                                    '<td>' + x.porcentaje   + '</td>' +
                                    ' <td>' +
                                        '<p>' +
                                            '<input name="descuento" type="radio" id="chk_descuento_' + i + '" />' +
                                            '<label for="chk_descuento_' + i + '"></label>' +
                                        '</p>' +
                                    '</td>';


                    $('#tbDescuento').append(tr);
                });
            });

            $('#modalAsignarDescuento').modal('open');
        }
    });

    $('#btnAsignarDescuento').click(function()
    {
        var contador = 0;
        var idDescuento = 0;
        $.each($('#tbDescuento tbody').find("tr"), function(i, tr)
        {
            var td       = $(tr).find("td")[3];
            var checkbox = $(td).find("input")[0];
            var p_data   = $(tr).find("p")[0];
            var data     = $(p_data).html();
            data         = JSON.parse(data);

            if(checkbox != undefined)
                if( $(checkbox).is(":checked") )
                {
                    idDescuento = data.idDescuento;
                    contador++;
                }
        });

        if(contador == 0)
            Materialize.toast("No ha seleccionado un descuento", 1200);
        else
        {
            var data = JSON.stringify(arreglo_idProducto);

            $.ajax(
            {
                type: "POST",
                url: IP + "/descuento/asignarDescuentoProducto/" + idDescuento,
                data: data,
                dataType: 'json',
                contentType: "application/json",
                success: function(respuesta)
                {
                    $('#modalAsignarDescuento').modal('close');
                },
                error: function(e1, e2, e3)
                {
                    Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
                }
            });
        }
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

function listarProductos()
{
    $('#tbProducto tbody').empty();

	var url = IP + '/producto/readProducto';
	
    $.get(url, function(respuesta)
    {
        $.each(respuesta, function(i, x)
        {
            var tr =    ''  +
                        '<tr>' +
                            '<td>' + (i+1)  						            + '</td>' +
							'<td>' + x.idCategoriaBean.descripcion              + '</td>' +
							'<td>' + x.idMarcaBean.descripcion  	            + '</td>' +
                            '<td>' + x.descripcion                              + '</td>' +
                            '<td>' + 'S/. ' + parseFloat( x.precio ).toFixed(2) + '</td>' +
                            '<td>' +
                                '<p style="margin: 0px;">' +
                                    '<input type="checkbox" id="chk_' + i + '" />' +
                                    '<label for="chk_' + i + '"></label>' +
                                '</p>' +
                            '</td>' +
                            '<td>' +                                
                                '<a href="javascript:;" onclick="desasignarDescuento(' + x.idProducto + ')" style="padding: -100px;">' +
                                    '<i class="fa fa-times" aria-hidden="true" style="font-size: 25px;"></i>' +
                                '</a>' +
                            '</td>' +
                            '<td>' + 
                                '<a onclick="verDetalle(this)" href="javascript:;">Ver Detalle</a>' +
                                '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
                                '<a onclick="modalActualizar(this)" href="javascript:;">'+
                                    '<i class="fa fa-history" aria-hidden="true"></i>' +
                                    '<p style="display: none;">' + JSON.stringify(x) + '</p>' +
                                '</a>' +
                                '&nbsp;&nbsp;' +
            					'<a onclick="eliminarProducto(' + x.idProducto + ')" href="javascript:;">'+
            						'<i class="fa fa-trash" aria-hidden="true"></i>' +
            					'</a>' +
            				'</td>' +
                        '</tr>';


            $('#tbProducto').append(tr);
        });
    });
}

function verDetalle(source)
{
    var td    = $(source).parents("td")[0];
    var p     = $(td).find("p")[0];
    var datos = $(p).html();
    datos     = JSON.parse(datos);

    sessionStorage.setItem( "objProducto", JSON.stringify(datos) );
    window.location = "MantenerDetalleProducto.html";
}

function modalActualizar(source)
{
    var td    = $(source).parents("td")[0];
    var p     = $(td).find("p")[0];
    var datos = $(p).html();
    datos     = JSON.parse(datos);

    $('#txtIdProductoA').val( datos.idProducto );
    $('#txtProductoA').val( datos.descripcion );
    $('#txtPrecioA').val( datos.precio );
    $('#cboCategoriaA').val( datos.idCategoriaBean.idCategoria );
    $('#cboMarcaA').val( datos.idMarcaBean.idMarca );

    Materialize.updateTextFields();

    $('#modalActualizarProducto').modal('open');
}

function eliminarProducto(idProducto)
{    
    var rpta = confirm("¿Desea eliminar este producto?");
    if( rpta )
    {
        $.ajax(
        {
            type: "POST",
            url: IP + "/producto/deleteProducto/" + idProducto,
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

function desasignarDescuento(idProducto)
{
    var rpta = confirm("Desea quitar descuento a todos los colores de este producto?");
    if( !rpta )
        return;

    $.ajax(
    {
        type: "POST",
        url: IP + "/descuento/desAsignarDescuentoProducto/" + idProducto,
        dataType: 'json',
        data: {},
        contentType:"application/json",
        success: function(respuesta)
        {
            if(respuesta.error)
                Materialize.toast(respuesta.mensaje, 5000);
            else
                Materialize.toast("Se quitó descuento", 5000);
        },
        error: function(e1, e2, e3)
        {
            Materialize.toast('Ocurrió un error en la conexión con el servidor. Revise la consola para más detalles (F12)', 5000);
        }
    });
}