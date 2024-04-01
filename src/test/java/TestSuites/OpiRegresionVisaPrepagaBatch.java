package TestSuites;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.AssertJUnit;
//import org.testng.annotations.AfterClass;
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
//import TestCases.GlobalBatch.TC_01_WEB_BATCH;
//import Tools.msgWorker;
import globalBatch.GlobalBatch;
import io.restassured.path.json.JsonPath;
//import junit.framework.Assert;
//import opi.wolfgang.AUT_OPI_OPERACIONES;

public class OpiRegresionVisaPrepagaBatch {
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
	void opiPrepagaT1001NovedadesSociosUAT() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		GlobalBatch globalBatch = new GlobalBatch();
		
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_GLOBAL_BATCH_LEMON_UAT" + entidad + "PROCESO T1001 - NOVEDADES SOCIOS";
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		//Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("WEB");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		
		// SET THE EXECUTION PLAN
		status = globalBatch.procesoT1001UAT(data, report, DM, x, name + "_Iteracion_" + x, configEntidad);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=2)
	void opiPrepagaT1010PadronSociosUAT() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		GlobalBatch globalBatch = new GlobalBatch();
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "02";
		String name = "TC_" + nroTC + "_GLOBAL_BATCH_LEMON_UAT" + entidad + "PROCESO T1010 - PADRON SOCIOS";
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		//Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("WEB");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);	
		// SET THE EXECUTION PLAN
		status = globalBatch.procesoT1010UAT(data, report, DM, x, name + "_Iteracion_" + x, configEntidad);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=3)
	void opiPrepagaT1027TarjetasEmbozadasUAT() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		GlobalBatch globalBatch = new GlobalBatch();
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "03";
		String name = "TC_" + nroTC + "_GLOBAL_BATCH_LEMON_UAT" + entidad + "PROCESO T1027 - TARJETAS EMBOZADAS";
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		//Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("WEB");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);	
		// SET THE EXECUTION PLAN
		status = globalBatch.procesoT1027UAT(data, report, DM, x, name + "_Iteracion_" + x, configEntidad);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=4)
	void opiPrepagaT7001TransaccionesDiariasAutorizacionesUAT() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		GlobalBatch globalBatch = new GlobalBatch();
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "04";
		String name = "TC_" + nroTC + "_GLOBAL_BATCH_LEMON_UAT" + entidad + "PROCESO T7001 - TRANSACCIONES DIARIAS AUTORIZACIONES";
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		//Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("WEB");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);	
		// SET THE EXECUTION PLAN
		status = globalBatch.procesoT7001UAT(data, report, DM, x, name + "_Iteracion_" + x, configEntidad);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=5)
	void opiPrepagaT2001TransaccionesUAT() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		GlobalBatch globalBatch = new GlobalBatch();
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "05";
		String name = "TC_" + nroTC + "_GLOBAL_BATCH_LEMON_UAT" + entidad + "PROCESO T2001 - TRANSACCIONES DIARIAS";
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		//Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("WEB");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);	
		// SET THE EXECUTION PLAN
		status = globalBatch.procesoT2001UAT(data, report, DM, x, name + "_Iteracion_" + x, configEntidad);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=6)
	void opiPrepagaT3022ProcesosAjustesCobranzasUAT() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		GlobalBatch globalBatch = new GlobalBatch();
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "06";
		String name = "TC_" + nroTC + "_GLOBAL_BATCH_LEMON_UAT" + entidad + "PROCESO T3022 - PROCESOS AJUSTES / COBRANZAS";
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		//Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("WEB");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);	
		// SET THE EXECUTION PLAN
		status = globalBatch.procesoT3022UAT(data, report, DM, x, name + "_Iteracion_" + x, configEntidad);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=7)
	void opiPrepagaT2000TotalCompensacionesUAT() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		GlobalBatch globalBatch = new GlobalBatch();
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "07";
		String name = "TC_" + nroTC + "_GLOBAL_BATCH_LEMON_UAT" + entidad + "PROCESO T2000 - TOTAL COMPENSACIONES";
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		//Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("WEB");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);	
		// SET THE EXECUTION PLAN
		status = globalBatch.procesoT2000UAT(data, report, DM, x, name + "_Iteracion_" + x, configEntidad);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=8)
	void opiPrepagaT3052CargosDiariosUAT() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		GlobalBatch globalBatch = new GlobalBatch();
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "08";
		String name = "TC_" + nroTC + "_GLOBAL_BATCH_LEMON_UAT" + entidad + "PROCESO T3052 - CARGOS DIARIOS";
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		//Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("WEB");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);	
		// SET THE EXECUTION PLAN
		status = globalBatch.procesoT3052UAT(data, report, DM, x, name + "_Iteracion_" + x, configEntidad);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	/********************************QA***************************************************/
	@Test(priority=9)
	void opiPrepagaT1001NovedadesSociosQA() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		GlobalBatch globalBatch = new GlobalBatch();
		
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "09";
		String name = "TC_" + nroTC + "_GLOBAL_BATCH_LEMON_QA_" + entidad + "_PROCESO T1001 - NOVEDADES SOCIOS";
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		//Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("WEB");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		
		// SET THE EXECUTION PLAN
		status = globalBatch.procesoT1001QA(data, report, DM, x, name + "_Iteracion_" + x, configEntidad);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=10)
	void opiPrepagaT1010PadronSociosQA() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		GlobalBatch globalBatch = new GlobalBatch();
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "10";
		String name = "TC_" + nroTC + "_GLOBAL_BATCH_LEMON_QA_" + entidad + "_PROCESO T1010 - PADRON SOCIOS";
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		//Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("WEB");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);	
		// SET THE EXECUTION PLAN
		status = globalBatch.procesoT1010QA(data, report, DM, x, name + "_Iteracion_" + x, configEntidad);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=11)
	void opiPrepagaT1027TarjetasEmbozadasQA() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		GlobalBatch globalBatch = new GlobalBatch();
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "11";
		String name = "TC_" + nroTC + "_GLOBAL_BATCH_LEMON_QA" + entidad + "PROCESO T1027 - TARJETAS EMBOZADAS";
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		//Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("WEB");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);	
		// SET THE EXECUTION PLAN
		status = globalBatch.procesoT1027QA(data, report, DM, x, name + "_Iteracion_" + x, configEntidad);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=12)
	void opiPrepagaT7001TransaccionesDiariasAutorizacionesQA() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		GlobalBatch globalBatch = new GlobalBatch();
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "12";
		String name = "TC_" + nroTC + "_GLOBAL_BATCH_LEMON_QA" + entidad + "PROCESO T7001 - TRANSACCIONES DIARIAS AUTORIZACIONES";
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		//Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("WEB");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);	
		// SET THE EXECUTION PLAN
		status = globalBatch.procesoT7001QA(data, report, DM, x, name + "_Iteracion_" + x, configEntidad);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=13)
	void opiPrepagaT2001TransaccionesQA() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		GlobalBatch globalBatch = new GlobalBatch();
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "13";
		String name = "TC_" + nroTC + "_GLOBAL_BATCH_LEMON_QA" + entidad + "PROCESO T2001 - TRANSACCIONES DIARIAS";
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		//Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("WEB");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);	
		// SET THE EXECUTION PLAN
		status = globalBatch.procesoT2001QA(data, report, DM, x, name + "_Iteracion_" + x, configEntidad);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=14)
	void opiPrepagaT3022ProcesosAjustesCobranzasQA() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		GlobalBatch globalBatch = new GlobalBatch();
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "14";
		String name = "TC_" + nroTC + "_GLOBAL_BATCH_LEMON_QA_" + entidad + "_PROCESO T3022 - PROCESOS AJUSTES / COBRANZAS";
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		//Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("WEB");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);	
		// SET THE EXECUTION PLAN
		status = globalBatch.procesoT3022QA(data, report, DM, x, name + "_Iteracion_" + x, configEntidad);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=15)
	void opiPrepagaT2000TotalCompensacionesQA() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		GlobalBatch globalBatch = new GlobalBatch();
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "15";
		String name = "TC_" + nroTC + "_GLOBAL_BATCH_LEMON_QA_" + entidad + "_PROCESO T2000 - TOTAL COMPENSACIONES";
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		//Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("WEB");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);	
		// SET THE EXECUTION PLAN
		status = globalBatch.procesoT2000QA(data, report, DM, x, name + "_Iteracion_" + x, configEntidad);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=16)
	void opiPrepagaT3052CargosDiariosQA() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		GlobalBatch globalBatch = new GlobalBatch();
		// SET INDIVIDUAL DATASOURCE
				data.setDataSourceType(data.CsvType);
				data.setDataSourceFile("testcase3.csv");
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "16";
		String name = "TC_" + nroTC + "_GLOBAL_BATCH_LEMON_QA_" + entidad + "_PROCESO T3052 - CARGOS DIARIOS";
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		int x = 1;
		//Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("WEB");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);	
		// SET THE EXECUTION PLAN
		status = globalBatch.procesoT3052QA(data, report, DM, x, name + "_Iteracion_" + x, configEntidad);
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
