package com.dreamteam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.dreamteam.bean.ClienteBean;
import com.dreamteam.bean.EstadoBean;
import com.dreamteam.bean.EstadoCivilBean;
import com.dreamteam.bean.TipoDocumentoBean;
import com.dreamteam.bean.UsuarioBean;
import com.dreamteam.controller.ClienteController;
import com.dreamteam.controller.UsuarioController;
import com.dreamteam.utilitarios.EmailService;
import com.dreamteam.utilitarios.Metodos;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DreamShopApplicationTests
{
	@Autowired
	ClienteController clienteController;

	@Autowired
	UsuarioController usuarioController;
	
	@Autowired
	public EmailService emailService;
		
	@Test
	public void cliente()
	{		
		Metodos.imprimir("createCliente -> " +
				clienteController.createCliente(ClienteBean.builder()
												.nombre("Gian Marco")
												.apellido_p("Carrasco")
												.apellido_m("Vásquez")
												.idTipoDocumentoBean(TipoDocumentoBean.builder().idTipoDocumento(1).build())
												.nro_documento("774662014")
												.correo("gianm_9622_30@hotmail.com")
												.clave("123456")
												.idEstadoCivilBean(EstadoCivilBean.builder().idEstadoCivil(1).build())
												.build())
		);
		
		System.out.println("READ INICIO");
		clienteController.readCliente().forEach(System.out::println);
		System.out.println("READ FIN");
		
		Metodos.imprimir("updateCliente -> " +
				clienteController.updateCliente(ClienteBean.builder()
												.idCliente(1)
												.nombre("Gian Marco")
												.apellido_p("Carrasco")
												.apellido_m("Vásquez")
												.idTipoDocumentoBean(TipoDocumentoBean.builder().idTipoDocumento(1).build())
												.nro_documento("77466201")
												.correo("gianm_96_30@hotmail.com")
												.clave("123456")
												.idEstadoCivilBean(EstadoCivilBean.builder().idEstadoCivil(1).build())
												.idEstadoBean(EstadoBean.builder().idEstado(1).build())
												.build())
		);		
		Metodos.imprimir("deleteCliente ->" + clienteController.deleteCliente(1).toString());
		Metodos.imprimir("getCliente -> " + clienteController.getCliente(1).toString());
		Metodos.imprimir("cambiarClave -> " + clienteController.cambiarClave( ClienteBean.builder().idCliente(1).clave("123").build() ) );
		Metodos.imprimir("recuperarClave -> " + clienteController.recuperarClave( ClienteBean.builder().correo("gianm_96_30@hotmail.com").build() ) );
	}

	@Test
	public void login()
	{
		System.out.println("LOGIN USUARIO-> "
				+ usuarioController.loginUsuario(UsuarioBean.builder()
					.usuario("admin")
					.clave("admin")
					.build()));
		
		System.out.println("LOGIN CLIENTE-> "
				+ clienteController.loginCliente(ClienteBean.builder()
					.correo("gianm_96_30@hotmail.com")
					.clave("123")
					.build()));
	}
}