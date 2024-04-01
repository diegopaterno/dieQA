package TestSuites;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Repositories.Repo_Variables;
import apis.wolfgang.AUTORIZADOR_API;
import io.restassured.path.json.JsonPath;
import opi.wolfgang.AUT_OPI_OPERACIONES;

public class ApiDelAutorizador {

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

	/********************* Mi Código *****************************/

	@Test(priority = 1)
	// void TC_1_AUT_OPI_MASTERCARD() {
	void opiPrepagaCompraLocalManual() {
		// DEFINITIONS
		if (repoVar == null) {
			try {
				initAll();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		AUTORIZADOR_API AUTORIZADOR_API = new AUTORIZADOR_API();

		// SET INDIVIDUAL DATASOURCE

		// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_AUT_OPI_MASTERCARD_" + entidad + "Prepaga - Compra Local - Manual";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
		String statusCodeEsperado = "200";
		String NRO_TARJETA = "5522681341261779";
		String VENCIMIENTO = "2504";

		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);

		// SET THE EXECUTION PLAN
		status = AUTORIZADOR_API.compralocal(report, name, configEntidad, entidad, TCFilesPath, endPoint,
				statusCodeEsperado, NRO_TARJETA, VENCIMIENTO);

		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);

		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	/****************** Fin de mi código ************************/

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
	}

	@AfterSuite(alwaysRun = true)
	static void tearDownAll() {
		System.out.println("Execution finished");
		report.saveGeneralReport();
	}

}

