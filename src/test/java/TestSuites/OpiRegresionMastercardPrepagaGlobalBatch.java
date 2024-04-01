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
import apis.wolfgang.API_OPERACIONES;
import apis.wolfgang.AUT_API_OPERACIONES;
import dataProviders.ProveedorDeDatos;
import globalBatch.GlobalBatchEndToEnd;
import io.qameta.allure.Description;
import io.restassured.path.json.JsonPath;
import opi.wolfgang.AUT_OPI_OPERACIONES;
import opi.wolfgang.AUT_OPI_OPERACIONES_ERROR;

public class OpiRegresionMastercardPrepagaGlobalBatch {
	
	static DriverManager DM;
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
	static String ambiente;
	static AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES;
	API_OPERACIONES API_OPERACIONES;
	static GlobalBatchEndToEnd GlobalBatchEndToEnd;

	@BeforeSuite
	static void initAll() throws IOException {
		DM = new DriverManager();
		data = new Datasources();
		report = new Reports();
		repoVar = new Repo_Variables();
		path = "./Datasources/config_entidadTotalCoin.json";
		//configEntidad = new String(Files.readAllBytes(Paths.get(path)));
		configEntidad = new String(Files.readAllBytes(Paths.get(path)));
		entidad = JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad");
		path_2 = "./Datasources/cuentas_generales.json";
		cuentas_generales = new String(Files.readAllBytes(Paths.get(path_2)));
		opi=JsonPath.from(configEntidad).getString("OPI_CONFIG.opi");
		ambiente=JsonPath.from(configEntidad).getString("ENTIDAD.ambiente");
		AUT_OPI_OPERACIONES=new AUT_OPI_OPERACIONES(opi);
		//API_OPERACIONES = new API_OPERACIONES();
		GlobalBatchEndToEnd = new GlobalBatchEndToEnd();
		
		

	}

	
	/*************inicio*************/
	String DE2="5406154401277367";
	
	//@Test(dataProvider = "dataElement22MAS", dataProviderClass = ProveedorDeDatos.class)
	@Description("Prueba de compra - ipm - presentacion - revision de transacciones diarias - T2001")
	@Test()
	void opiMastercardGBT2001() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		// Nombre Real del TC
		boolean status = true;
		String nroTC = "001";
		String name = "TC_" + nroTC + "_AUT_OPI_Mastercard_TOTALCOIN_" + entidad + "_Prepaga - Compra ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/MASTERCARD/UAT/GB/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="29";
		String DE22="010";
		// SET INDIVIDUAL DATASOURCE
		data.setDataSourceType(data.CsvType);
		data.setDataSourceFile("testcase3.csv");
		int x = 1;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		status = AUT_OPI_OPERACIONES.trxIpm(data,report, DM, x,name, configEntidad,entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		//if (status == false) {
			//msg = "Fail;Fallo la ejecucion. TC: " + name;
		//}
		// Se aMastercard x jUnit el resultado de la prueba
		//AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Description("Prueba de compra - ipm - presentacion - revision de transacciones diarias - T2001")
	@Test()
	void opiMastercardGBEmbozado() throws InterruptedException {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		// Nombre Real del TC
		boolean status = true;
		String nroTC = "002";
		String name = "TC_" + nroTC + "_AUT_OPI_Mastercard_TOTALCOIN_" + entidad + "_Prepaga - Compra ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/MASTERCARD/UAT/GB/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="29";
		String DE22="010";
		// SET INDIVIDUAL DATASOURCE
		data.setDataSourceType(data.CsvType);
		data.setDataSourceFile("testcase3.csv");
		int x = 1;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		//repoVar.setResult(status);
		repoVar.setDataMsg(name);
		//status = AUT_OPI_OPERACIONES.altaCuentaEmbozado(data,report, DM, x,name, configEntidad,entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		//
		//String name = "TC_" + nroTC + "_API_OPERACIONES_MASTERCARD_" + entidad + " ALTA DE CUENTA PREPAGA TARJETA VIRTUAL";
	//	String TCFilesPath = "./TC_Files/API/" + entidad + "/TC_" + nroTC;
	//	String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String producto = "6";
		//String producto = "181";
		String baseURL = JsonPath.from(configEntidad).getString("TOKENPREPAGA.base_url");
		String endPoint = baseURL + "/api/Productos/" + producto + "/Cuentas";
		String statusCodeEsperado = "201";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		//repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		//status = API_OPERACIONES.post(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		//status = API_OPERACIONES.post(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado);
		//GlobalBatchEndToEnd GlobalBatchEndToEnd = new GlobalBatchEndToEnd();
		
		status = GlobalBatchEndToEnd.altaCuentaEmbozado(data, report, DM, x, name, configEntidad, entidad, producto, baseURL, TCFilesPath, endPoint, statusCodeEsperado);
		//
		// Configuracion de variables para el armado del reporte
		//repoVar.setResult(status);
	}
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
		//status = AUT_OPI_OPERACIONES.trxIpm(data,report, DM, x,name, configEntidad,entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
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

	

	/*********************FIN DE LA SUITE***********************************/
	@AfterClass
	void tearDown() {
		if (repoVar.getTipoTc().equals("API")) {
			report.addTestCaseToGeneralReport(repoVar.getResult(), repoVar.getDataMsg(), "");
			report.saveTestCaseReport(repoVar.getDataMsg());
		} else {
			System.out.println("El caso de prueba no es: API");
		}
	}

	@AfterSuite
	static void tearDownAll() {
		System.out.println("Execution finished");
		report.saveGeneralReport();
	}

}
