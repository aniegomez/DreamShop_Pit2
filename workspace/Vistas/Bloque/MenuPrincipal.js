var objUsuario;

$(function()
{	
	validarUsuario();
});

function validarUsuario()
{
	objUsuario = sessionStorage.getItem("objUsuario");
	if(objUsuario == null)
		window.location = "LoginInterno.html";
	else
		objUsuario = JSON.parse(objUsuario);
}