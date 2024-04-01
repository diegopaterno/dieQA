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

	public class API_autorizador_VISA_LEMON_QA{

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
			entidad = JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_peru_visa_qa");

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
			String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " compra - local - manual";
			String TCFilesPath = "./TC_Files/API_AUTORIZADOR/" + entidad +"/VISA/QA/TC_" + nroTC;
			String msg = "True;Resultado de la ejecucion OK. TC: " + name;
			String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
			//http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion
			
			String statusCodeEsperado = "201";
			// Inicializacion de las Variables del Repositorio
			repoVar.setTipoTc("SSH");
			repoVar.setResult(status);
			repoVar.setDataMsg(name);
			// SET THE EXECUTION PLAN
			status = AUT_API_OPERACIONES.compraApiAutorizadorLemonQA(report, name, configEntidad, entidad, TCFilesPath, endPoint,
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
