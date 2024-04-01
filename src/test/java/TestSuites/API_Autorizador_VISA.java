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

public class API_Autorizador_VISA {

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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - MANUAL - COMERCIO LOCAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_2_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "02";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - BANDA - COMERCIO LOCAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - CODIGO - COMERCIO LOCAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - CHIP - COMERCIO LOCAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_5_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "05";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - ECOMMERCE - COMERCIO LOCAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_6_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "06";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - CONTACTLESS - COMERCIO LOCAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - MANUAL - SERVICIO DIGITAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - BANDA - SERVICIO DIGITAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - CODIGO - SERVICIO DIGITAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - CHIP - SERVICIO DIGITAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - ECOMMERCE - SERVICIO DIGITAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - CONTACTLESS - SERVICIO DIGITAL - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_13_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "13";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - MANUAL - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_14_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "14";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - BANDA - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_15_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "15";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - CODIGO - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_16_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "16";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - CHIP - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_17_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "17";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - ECOMMERCE - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_18_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "18";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - CONTACLESS - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - MANUAL - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - BANDA- SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - CODIGO - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - CHIP - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - ECOMMERCE - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_24_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "24";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " COMPRA -  - CONTACTLESS - SERVICIO DIGITAL EXT - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	

	
	//DEBITO - MONEDA LOCAL //
	
	@Test(priority = 1, groups = { "compra" })
	void TC_25_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "25";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " DEBITO AUT -  - MANUAL - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		void TC_26_compraApiAutorizador() {
			// DEFINITIONS
			AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
			// Nombre Real del TC
			boolean status = false;
			String nroTC = "26";
			String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " DEBITO AUT -  - BANDA - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
			String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		void TC_27_compraApiAutorizador() {
			// DEFINITIONS
			AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
			// Nombre Real del TC
			boolean status = false;
			String nroTC = "27";
			String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " DEBITO AUT -  - CODIGO- TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
			String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		void TC_28_compraApiAutorizador() {
			// DEFINITIONS
			AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
			// Nombre Real del TC
			boolean status = false;
			String nroTC = "28";
			String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + "DEBITO AUT -  - CHIP - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
			String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		void TC_29_compraApiAutorizador() {
			// DEFINITIONS
			AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
			// Nombre Real del TC
			boolean status = false;
			String nroTC = "29";
			String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + "DEBITO AUT -  - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
			String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
			String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + "DEBITO AUT -  - CONTACTLESS- TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
			String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		
		
		/*VISA - Débito Aut | Tarjeta Local | 
		Postnet Local | Moneda Externa */
		
		@Test(priority = 1, groups = { "compra" })
		void TC_31_compraApiAutorizador() {
			// DEFINITIONS
			AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
			// Nombre Real del TC
			boolean status = false;
			String nroTC = "31";
			String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + "DEBITO AUT -  - MANUAL - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
			String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
			String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + "DEBITO AUT -  - BANDA - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
			String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
			String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + "DEBITO AUT -  - CODIGO- TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
			String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
			String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + "DEBITO AUT -  - CHIP - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
			String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
			String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + "DEBITO AUT -  - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
			String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		void TC_36_compraApiAutorizador() {
			// DEFINITIONS
			AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
			// Nombre Real del TC
			boolean status = false;
			String nroTC = "36";
			String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + "DEBITO AUT -  - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL - MONEDA EXT";
			String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO - BANDA - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO -  - CODIGO - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO -  - CHIP - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO -  - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO -  - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO -  - MANUAL - TARJETA LOCAL - POSNET EXT - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO -  - BANDA - TARJETA LOCAL - POSNET EXT - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO -  - CODIGO - TARJETA LOCAL - POSNET EXT - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO -  - CHIP - TARJETA LOCAL - POSNET EXT - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO -  - ECOMMERCE - TARJETA LOCAL - POSNET EXT - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_49_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "49";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO -  - CONTACTLESS - TARJETA LOCAL - POSNET EXT - MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_50_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "50";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " CASHBACK -  - MANUAL - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_51_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "51";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " CASHBACK -  - BANDA - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_52_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "52";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " CASHBACK -  - CODIGO - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_53_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "53";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " CASHBACK -  - CHIP - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_54_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "54";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " CASHBACK -  - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_55_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "55";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " CASHBACK -  - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL- MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_56_compraApiAutorizadorInternacionalNoPermitida() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "56";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " CASHBACK -  - MANUAL - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_57_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "57";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " CASHBACK -  - BANDA - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_58_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "58";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " CASHBACK -  - CODIGO - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_59_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "59";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " CASHBACK -  - CHIP - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_60_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "60";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " CASHBACK -  - ECOMMERCE - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_61_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "61";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " CASHBACK -  - CONTACTLESS - TARJETA LOCAL - POSNET LOCAL- MONEDA EXT";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_A105_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A105";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO -  - MANUAL - TARJETA LOCAL - POSNET EXT - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_A106_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A106";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO -  - BANDA - TARJETA LOCAL - POSNET EXT - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_A107_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A107";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO -  - CODIGO - TARJETA LOCAL - POSNET EXT - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_A108_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A108";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO -  - CHIP - TARJETA LOCAL - POSNET EXT - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_A109_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A109";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO -  - ECOMMERCE - TARJETA LOCAL - POSNET EXT - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	void TC_A110_compraApiAutorizador() {
		// DEFINITIONS
		AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "A110";
		String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " EXTRACCION / ADELANTO -  - CONTACTLESS - TARJETA LOCAL - POSNET EXT - MONEDA LOCAL";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/VISA/TC_" + nroTC;
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
	
	@AfterSuite(alwaysRun = true)
	static void tearDownAll() {
		System.out.println("Execution finished");
		report.saveGeneralReport();
	}
}
