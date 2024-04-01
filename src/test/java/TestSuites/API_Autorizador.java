package TestSuites;

import org.testng.AssertJUnit;
import apis.wolfgang.AUT_API_OPERACIONES;

import org.testng.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Repositories.Repo_Variables;

import io.restassured.path.json.JsonPath;

public class API_Autorizador {

	static

	// Init
	DriverManager DM;
	static Datasources data;
	static Reports report;
	static Repo_Variables repoVar;
	static String path;
	static String configEntidad;
	static String entidad;
	static String datosEntradaSegunTc;

	@BeforeSuite(alwaysRun = true)
	static void initAll() throws IOException {
		DM = new DriverManager();
		data = new Datasources();
		report = new Reports();
		repoVar = new Repo_Variables();
		path = "./Datasources/config_entidad.json";
		configEntidad = new String(Files.readAllBytes(Paths.get(path)));
		entidad = JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad");

	}

	@BeforeClass(alwaysRun = true)
	void init() throws IOException {

	}

	@BeforeMethod(alwaysRun = true)

	@BeforeTest(alwaysRun = true)
	void beforeTest() {

	}

	@Test(priority = 1, groups = { "compra" })
	void TC_1_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " compra - local - manual";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	
	@Test(priority = 2, groups = { "compra" })
	void TC_2_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A178";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " compra - devolucion - local - manual";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String producto = "1";
		//String NRO_TARJETA = "5522681341261779";
		//String VENCIMIENTO = "2504";
		String baseURL = JsonPath.from(configEntidad).getString("TOKENPREPAGA.base_url");
		//String endPoint = baseURL + "/api/Productos/" + producto + "/Cuentas";
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	/*****************************************************************************
	 * REVERSO LUIS
	 * ***************************************************************************/
	@Test(priority = 1, groups = { "compra" })
	void TC_A196b_compraReversoApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A196";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " REVERSO PARCIAL - MANUAL - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}


	//@Test(priority = 1, groups = { "compra" })
	void TC_2_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "02";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " compra - local - banda";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_3_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "03";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " compra - local - codigo";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_7_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "07";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CODIGO - SERVICIO DIGITAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_8_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "08";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - BANDA - SERVICIO DIGITAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_9_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "09";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CODIGO - SERVICIO DIGITAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_10_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "10";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CHIP - SERVICIO DIGITAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_11_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "11";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - ECOMMERCE - SERVICIO DIGITAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_12_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "12";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CONTACTLESS - SERVICIO DIGITAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_19_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "19";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - MANUAL - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_20_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "20";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - BANDA- SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_21_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "21";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CODIGO - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_22_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "22";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CHIP - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_23_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "23";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - ECOMMERCE - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_30_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "30";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "DEBITO AUT -  - CONTACTLESS- TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_31_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "31";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "DEBITO AUT -  - MANUAL - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_32_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "32";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "DEBITO AUT -  - BANDA - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_33_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "33";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "DEBITO AUT -  - CODIGO- TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_34_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "34";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "DEBITO AUT -  - CHIP - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_35_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "35";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "DEBITO AUT -  - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_4_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "04";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " compra - local - chip";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_5_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "05";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " compra - local - ecommerce";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_6_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "06";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " compra - local - contactless";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_13_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "13";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " COMPRA -  - MANUAL - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_14_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "14";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " COMPRA -  - BANDA - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_15_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "15";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " COMPRA -  - CODIGO - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_16_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "16";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " COMPRA -  - CHIP - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_17_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "17";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " COMPRA -  - ECOMMERCE - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_18_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "18";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " COMPRA -  - CONTACLESS - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_24_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "24";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " COMPRA -  - CONTACTLESS - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_25_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "25";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - MANUAL - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_26_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "26";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - BANDA - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_27_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "27";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - CODIGO- TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_28_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "28";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - CHIP - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_29_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "29";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_36_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "36";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_50_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "50";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " CASHBACK -  - MANUAL - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_51_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "51";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " CASHBACK -  - BANDA - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_52_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "52";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " CASHBACK -  - CODIGO - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_53_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "53";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " CASHBACK -  - CHIP - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_54_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "54";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " CASHBACK -  - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_55_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "55";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " CASHBACK -  - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_56_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "56";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " CASHBACK -  - MANUAL - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_57_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "57";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " CASHBACK -  - BANDA - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_58_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "58";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " CASHBACK -  - CODIGO - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_59_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "59";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " CASHBACK -  - CHIP - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_60_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "60";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " CASHBACK -  - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_61_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "61";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " CASHBACK -  - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_86_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "86";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT - MANUAL - BIN EXT - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_87_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "87";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - BANDA - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_88_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "88";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - CODIGO- TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_89_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "89";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - CHIP - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_90_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "90";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_91_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "91";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - CONTACTLESS- TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_92_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "92";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - MANUAL - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_93_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "93";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - BANDA - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_94_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "94";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - CODIGO- TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_95_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "95";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - CHIP - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_96_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "96";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_97_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "97";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEBITO AUT -  - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_98_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "98";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " EXTRACCION / ADELANTO -  - MANUAL - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_99_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "99";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " EXTRACCION / ADELANTO -  -  - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A100_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A100";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " EXTRACCION / ADELANTO -  - BANDA - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A101_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A101";
		String name = "TC_" + nroTC + "_SAPI_OPERACIONES_MASTERCARD_" + entidad + " EXTRACCION / ADELANTO -  - CODIGO - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A102_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A102";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " EXTRACCION / ADELANTO -  - CHIP - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A103_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A103";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " EXTRACCION / ADELANTO -  - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A104_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A104";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " EXTRACCION / ADELANTO -  - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A105_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A105";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " EXTRACCION / ADELANTO -  - MANUAL - TARJETA LOCAL - POSNET EXT - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A106_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A106";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " EXTRACCION / ADELANTO -  - BANDA - TARJETA LOCAL - POSNET EXT - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A107_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A107";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " EXTRACCION / ADELANTO -  - CODIGO - TARJETA LOCAL - POSNET EXT - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A108_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A108";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " EXTRACCION / ADELANTO -  - CHIP - TARJETA LOCAL - POSNET EXT - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A109_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A109";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " EXTRACCION / ADELANTO -  - ECOMMERCE - TARJETA LOCAL - POSNET EXT - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A110_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A110";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " EXTRACCION / ADELANTO -  - CONTACTLESS - TARJETA LOCAL - POSNET EXT - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
		// Aquí comienzan los casos de Devolución total
	@Test(priority = 1, groups = { "compra" })
	void TC_A124_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A124";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - MANUAL - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A125_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A125";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - BANDA - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A126_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A126";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - CODIGO - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A127_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A127";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - CHIP - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A128_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A128";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A129_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A129";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A130_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A130";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - MANUAL - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A131_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A131";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - BANDA - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A132_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A132";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - CODIGO - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A133_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A133";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - CHIP - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A134_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A134";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A135_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A135";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A136_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A136";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - MANUAL - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A137_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A137";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - BANDA - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A138_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A138";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - CODIGO - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A139_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A139";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - CHIP - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A140_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A140";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - ECOMMERCE - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A141_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A141";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - CONTACTLESS - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A142_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A142";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - MANUAL - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A143_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A143";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - BANDA - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A144_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A144";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - CODIGO - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A145_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A145";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - CHIP - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A146_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A146";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - ECOMMERCE - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A147_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A147";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION - CONTACTLESS - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
		// Aqui comienzan los casos de Devolución parcial
	@Test(priority = 1, groups = { "compra" })
	void TC_A172_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A172";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - MANUAL - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A173_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A173";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - BANDA - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A174_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A174";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - CODIGO - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A175_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A175";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - CHIP - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A176_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A176";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL- ECOMMERCE - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A177_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A177";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A178_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A178";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - MANUAL - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A179_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A179";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - BANDA - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A180_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A180";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - CODIGO - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A181_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A181";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - CHIP - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A182_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A182";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A183_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A183";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A184_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A184";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - MANUAL - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A185_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A185";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - BANDA - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A186_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A186";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - CODIGO - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A187_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A187";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - CHIP - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A188_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A188";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - ECOMMERCE - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A189_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A189";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - CONTACTLESS - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A190_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A190";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - MANUAL - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A191_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A191";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - MANUAL - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A192_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A192";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - CODIGO - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A193_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A193";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - CHIP - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A194_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A194";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - ECOMMERCE - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A195_compraDevolucionApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A195";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " DEVOLUCION PARCIAL - CONTACTLESS - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraDevolucionApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	/***************************************************************************************/
	// Método REVERSO
	@Test(priority = 1, groups = { "compra" })
	void TC_A196_compraReversoApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A196";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " REVERSO PARCIAL - MANUAL - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A197_compraReversoApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A197";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " REVERSO PARCIAL - BANDA- TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A198_compraReversoApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A198";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " REVERSO PARCIAL - CODIGO - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A199_compraReversoApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A199";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " REVERSO PARCIAL - CHIP - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A200_compraReversoApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A200";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " REVERSO - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A201_compraReversoApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A201";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " REVERSO PARCIAL - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A202_compraReversoApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A202";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " REVERSO PARCIAL - MANUAL - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A203_compraReversoApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A203";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " REVERSO PARCIAL - BANDA- TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A204_compraReversoApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A204";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " REVERSO PARCIAL  - CODIGO - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A205_compraReversoApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A205";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " REVERSO PARCIAL - CHIP - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A206_compraReversoApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A206";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " REVERSO - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority = 1, groups = { "compra" })
	void TC_A207_compraReversoApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A207";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " REVERSO PARCIAL - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
				AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	/**************************************************************************************
	@AfterTest(alwaysRun = true)
	void afterTest() {

	}

	@AfterMethod
	@AfterClass(alwaysRun = true)
	void tearDown() {
		if (repoVar.getTipoTc().equals("API")) {
			report.addTestCaseToGeneralReport(repoVar.getResult(), repoVar.getDataMsg(), "");
			report.saveTestCaseReport(repoVar.getDataMsg());
		} else {
			System.out.println("El caso de prueba es: API");
		}
	}*/

	@Test(priority = 1, groups = { "compra" })
	void TC_37_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "37";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "EXTRACCION / ADELANTO -  - MANUAL - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba

		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_38_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "38";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "EXTRACCION / ADELANTO - BANDA - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_39_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "39";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "EXTRACCION / ADELANTO -  - BANDA - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_40_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "40";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "EXTRACCION / ADELANTO -  - CODIGO - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_41_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "41";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "EXTRACCION / ADELANTO -  - CODIGO - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_42_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "42";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "EXTRACCION / ADELANTO -  - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_43_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "43";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "EXTRACCION / ADELANTO -  - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_44_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "44";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "EXTRACCION / ADELANTO -  - MANUAL - TARJETA LOCAL - POSNET EXT - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_45_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "45";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "EXTRACCION / ADELANTO -  - BANDA - TARJETA LOCAL - POSNET EXT - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_46_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "46";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "EXTRACCION / ADELANTO -  - CODIGO - TARJETA LOCAL - POSNET EXT - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_47_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "47";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "EXTRACCION / ADELANTO -  - CHIP - TARJETA LOCAL - POSNET EXT - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_48_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "48";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "EXTRACCION / ADELANTO -  - ECOMMERCE - TARJETA LOCAL - POSNET EXT - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_62_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "62";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - MANUAL - COMERCIO LOCAL - BIN EXT - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_63_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "63";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - BANDA - COMERCIO LOCAL - BIN EXT - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_64_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "64";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CODIGO - COMERCIO LOCAL - BIN EXT - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_65_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "65";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CHIP - COMERCIO LOCAL - BIN EXT - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_66_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "66";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - ECOMMERCE - COMERCIO LOCAL - BIN EXT - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_67_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "67";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CONTACTLESS - COMERCIO LOCAL - BIN EXT - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_68_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "68";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - MANUAL - SERVICIO DIGITAL - BIN EXT - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_69_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "69";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - BANDA - SERVICIO DIGITAL - BIN EXT - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_70_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "70";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CODIGO - SERVICIO DIGITAL - BIN EXT- POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_71_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "71";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CHIP - SERVICIO DIGITAL - BIN EXT - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_72_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "72";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - ECOMMERCE - SERVICIO DIGITAL - BIN EXT - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_73_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "73";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - MANUAL - SERVICIO DIGITAL EXT - BIN EXT - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_74_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "74";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - MANUAL - SERVICIO DIGITAL EXT - BIN EXT - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_75_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "75";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - BANDA - SERVICIO DIGITAL EXT - BIN EXT - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_76_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "76";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CODIGO - SERVICIO DIGITAL EXT - BIN EXT - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_77_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "77";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CHIP - SERVICIO DIGITAL EXT - BIN EXT - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_78_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "78";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - ECOMMERCE - SERVICIO DIGITAL EXT - BIN EXT - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_79_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "79";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CONTACLESS - SERVICIO DIGITAL EXT - BIN EXT - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_80_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "80";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - MANUAL - SERVICIO DIGITAL EXT -  BIN EXT - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_81_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "73";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - BANDA- SERVICIO DIGITAL EXT -  BIN EXT - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_82_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "82";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CODIGO- SERVICIO DIGITAL EXT -  BIN EXT - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_83_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "83";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CHIP - SERVICIO DIGITAL EXT -  BIN EXT - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_84_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "84";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - ECOMMERCE - SERVICIO DIGITAL EXT -  BIN EXT - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_85_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "85";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "COMPRA -  - CONTACTLESS - SERVICIO DIGITAL EXT - BIN EXT - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	/**************************************
	 * ➤➤➤ CASHBACK | Posnet Local | Moneda Local ➤➤➤
	 **************************************/

	@Test(priority = 1, groups = { "compra" })
	void TC_111_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a111";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "CASHBACK -  - MANUAL - BIN EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_112_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a112";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "CASHBACK -  - BANDA - BIN EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_113_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a113";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "CASHBACK -  - CODIGO - BIN EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_114_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a114";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "CASHBACK -  - CHIP - BIN EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_115_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a115";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "CASHBACK -  - ECOMMERCE - BIN EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}


	@Test(priority = 1, groups = { "compra" })
	void TC_116_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a116";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "CASHBACK -  - CONTACTLESS - BIN EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}


	/**************************************
	 * ➤➤➤ CASHBACK | Posnet Local | Moneda Ext ➤➤➤
	 **************************************/


	@Test(priority = 1, groups = { "compra" })
	void TC_117_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a117";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "CASHBACK -  - MANUAL - BIN EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_119_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a119";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "CASHBACK -  - BANDA - BIN EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_120_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a120";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "CASHBACK -  - CODIGO - BIN EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_121_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a121";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "CASHBACK -  - CHIP - BIN EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_122_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a122";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "CASHBACK -  - ECOMMERCE - BIN EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_123_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a123";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad
				+ "CASHBACK -  - CONTACTLESS - BIN EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	
	/********Compra & Reverso | 
	 * Tarj. Local | 
	 * Posnet Local | 
	 * Moneda Local
	 **************************
	 */
	
	@Test(priority = 1, groups = { "compra" })
	void TC_148_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a148";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - MANUAL - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_149_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a149";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - BANDA- TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_150_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a150";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - CODIGO - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_151_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a151";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - CHIP - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_152_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a152";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_153_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a153";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	/********Compra & Reverso | 
	 * Tarj. Local | 
	 * Posnet Local | 
	 * Moneda Ext.
	 **************************
	 */
	
	@Test(priority = 1, groups = { "compra" })
	void TC_154_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a154";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - MANUAL - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(priority = 1, groups = { "compra" })
	void TC_155_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a155";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - BANDA- TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_156_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a156";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - CODIGO - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_157_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a157";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - CHIP - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_158_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a158";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_159_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a159";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	
	
	/***********  **  ** *          *  **  **  **  ***********
	 * Compra & Reverso | Tarj. Ext | Posnet Local | Moneda Local
	 * ******************************************************* *
	 **********************************************************/
	
	
	@Test(priority = 1, groups = { "compra" })
	void TC_160_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a160";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - MANUAL - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	
	
	
	@Test(priority = 1, groups = { "compra" })
	void TC_161_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a161";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - BANDA- TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_162_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a162";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - CODIGO - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	
	@Test(priority = 1, groups = { "compra" })
	void TC_163_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a163";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - CHIP - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_164_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a164";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - ECOMMERCE - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_165_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a165";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - CONTACTLESS - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	/***********  **  ** *          *  **  **  **  ***********
	 * Compra & Reverso | Tarj. Ext | Posnet Local | Moneda Ext.
	 * ******************************************************* *
	 **********************************************************/
	
	@Test(priority = 1, groups = { "compra" })
	void TC_166_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a166";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - MANUAL - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_167_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a167";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - BANDA- TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_168_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a168";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - CODIGO - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_169_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a169";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - CHIP - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_170_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a170";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - ECOMMERCE - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_171_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a171";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - CONTACTLESS - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_aPRUEBAReverso_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "aPRUEBAReverso";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO - MANUAL - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	/*****************************
	 * Compra & Reverso Parcial| Tarj. Ext | 
	 * Posnet Local | Moneda Local
	 *****************************/
	
	@Test(priority = 1, groups = { "compra" })
	void TC_208_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a208";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO PARCIAL - MANUAL - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_209_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a209";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO PARCIAL - BANDA- TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_210_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a210";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO PARCIAL - CODIGO - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_211_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a211";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO PARCIAL - CHIP - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_212_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a212";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO PARCIAL - ECOMMERCE - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_213_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a213";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO PARCIAL - CONTACTLESS - TARJETA EXT - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	
	/*****************************
	 * Compra & Reverso Parcial| Tarj. Ext | 
	 * Posnet Local | Moneda Ext.
	 *****************************/
	
	@Test(priority = 1, groups = { "compra" })
	void TC_214_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a214";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO PARCIAL - MANUAL - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_215_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a215";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO PARCIAL - BANDA- TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_216_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a216";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO PARCIAL - CODIGO - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_217x_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a217";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO PARCIAL - CHIP - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_217_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a217";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO PARCIAL - CHIP - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_218_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a218";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO PARCIAL - ECOMMERCE - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority = 1, groups = { "compra" })
	void TC_219_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "a219";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + "REVERSO PARCIAL - CONTACTLESS - TARJETA EXT - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/MASTERCARD/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.desa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_API_OPERACIONES.compraReversoApiAutorizador(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMASTERCARD x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	
	@AfterSuite(alwaysRun = true)
	static void tearDownAll() {
		System.out.println("Execution finished");
		report.saveGeneralReport();
	}
}
