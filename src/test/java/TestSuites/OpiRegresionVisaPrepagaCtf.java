package TestSuites;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
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
import io.restassured.path.json.JsonPath;
import opi.wolfgang.AUT_OPI_OPERACIONES;

public class OpiRegresionVisaPrepagaCtf {
	static

	// Init
	DriverManager DM;
	static Datasources data;
	static Reports report;
	static Repo_Variables repoVar;
	static String path;
	static String path_2;
	//static String configEntidad;
	static String entidad;
	static String datosEntradaSegunTc;
	static String configEntidad;
	static String cuentas_generales;
	static String opi;
	static AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES;

	@BeforeSuite
	static void initAll() throws IOException {
		DM = new DriverManager();
		data = new Datasources();
		report = new Reports();
		repoVar = new Repo_Variables();
		path = "./Datasources/config_entidad.json";
		//configEntidad = new String(Files.readAllBytes(Paths.get(path)));
		configEntidad = new String(Files.readAllBytes(Paths.get(path)));
		entidad = JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_peru_visa_qa");
		path_2 = "./Datasources/cuentas_generales.json";
		cuentas_generales = new String(Files.readAllBytes(Paths.get(path_2)));
		opi=JsonPath.from(configEntidad).getString("OPI_CONFIG.opi");
		AUT_OPI_OPERACIONES=new AUT_OPI_OPERACIONES(opi);
	}

	@BeforeClass
	void init() throws IOException {

	}

	@BeforeMethod

	@BeforeTest
	void beforeTest() {

	}
	@AfterTest
	void afterTest() {

	}
	/*************inicio*************/
	
	@Test(priority=1)
	void opiPrepagaCompraLocalManualCtfYPresentacion() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - Manual - CTF ENTRANTE - PRESENTACIONES VISA LEMON PERU QA";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.CompraCTFLemonQA(data, report, DM, x, name, configEntidad, entidad, TCFilesPath);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@AfterSuite
	static void tearDownAll() {
		System.out.println("Execution finished");
		report.saveGeneralReport();
	}
}
