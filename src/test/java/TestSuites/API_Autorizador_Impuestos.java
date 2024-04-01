package TestSuites;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Repositories.Repo_Variables;
import apis.wolfgang.AUT_API_OPERACIONES;
import apis.wolfgang.impuestos.AlicuotaTDF;
import io.restassured.path.json.JsonPath;

public class API_Autorizador_Impuestos {

	
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
		entidad = JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_impuestos");

	}

	@BeforeClass(alwaysRun = true)
	void init() throws IOException {

	}

	@BeforeMethod(alwaysRun = true)

	@BeforeTest(alwaysRun = true)
	void beforeTest() {

	}

	@Test(priority = 1, groups = { "compra" })
	void TC_1_AlicuotaTierraDelFuego_RG_de_AREF929() {
		// DEFINITIONS
		
		AlicuotaTDF AlicuotaTDF = new AlicuotaTDF();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " SE VALIDA COBRO DE IMPUESTOS Alicuota Tierra del Fuego - RG de AREF 929 ";
		String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad + "/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AlicuotaTDF.alicuotaTDF(report, name, configEntidad, entidad, TCFilesPath);
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
