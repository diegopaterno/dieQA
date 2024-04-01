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
import apis.wolfgang.AUT_API_OPERACIONES;
import dataProviders.ProveedorDeDatos;
import io.restassured.path.json.JsonPath;
import opi.wolfgang.AUT_OPI_OPERACIONES;
import opi.wolfgang.AUT_OPI_OPERACIONES_ERROR;

public class OpiRegresionMastercardPrepagaUAT632 {
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
	static String ambiente;
	static AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES;

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
	String DE2="5406154401277367";
	
	@Test(dataProvider = "dataElement22MAS", dataProviderClass = ProveedorDeDatos.class)
	void opiMastercardCasoFeliz(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "001";
		String name = "TC_" + nroTC + "_AUT_OPI_Mastercard_TOTALCOIN_" + entidad + "_Prepaga - Compra ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/MASTERCARD/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="29";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		//status = AUT_OPI_OPERACIONES.compraTotalCoinUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMastercard x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	// -- COMPRA
	@Test(dataProvider = "dataElement22MAS", dataProviderClass = ProveedorDeDatos.class)
	void opiMastercardCompraManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "007";
		String name = "TC_" + nroTC + "_AUT_OPI_Mastercard_TOTALCOIN_" + entidad + "_Prepaga - Compra Moneda Extranjera ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/MASTERCARD/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="29";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMastercard x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22MAS", dataProviderClass = ProveedorDeDatos.class)
	void opiMastercardCompraManualServDigitalUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
	//	AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "013";
		String name = "TC_" + nroTC + "_AUT_OPI_Mastercard_TOTALCOIN_" + entidad + "_Prepaga - Compra Local  - ServDigital";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/MASTERCARD/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="29";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMastercard x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	//---
	@Test(dataProvider = "dataElement22MAS", dataProviderClass = ProveedorDeDatos.class)
	 void opiMastercardCompraManualServDigitalExtUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "025";
		String name = "TC_" + nroTC + "_AUT_OPI_Mastercard_TOTALCOIN_" + entidad + "_Prepaga - Compra Local  - ServDigital Ext";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/MASTERCARD/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="29";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMastercard x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	//   INICIO DE PRUEBAS DE DEBITO AUTOMATICO
	@Test(dataProvider = "dataElement22MAS", dataProviderClass = ProveedorDeDatos.class)
	void opiMastercardDebitoManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "037";
		String name = "TC_" + nroTC + "_AUT_OPI_Mastercard_TOTALCOIN_" + entidad + "_Prepaga - Debito ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/MASTERCARD/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="29";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMastercard x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22MAS", dataProviderClass = ProveedorDeDatos.class)
	void opiMastercardDebitoMonedaExtManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "049";
		String name = "TC_" + nroTC + "_AUT_OPI_Mastercard_TOTALCOIN_" + entidad + "_Prepaga - Debito MonedaExt ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/MASTERCARD/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="29";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMastercard x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	// -- EXTRACCION ADELANTO
	@Test(dataProvider = "dataElement22MAS", dataProviderClass = ProveedorDeDatos.class)
	void opiMastercardExtraccionAdelantoManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "061";
		String name = "TC_" + nroTC + "_AUT_OPI_Mastercard_TOTALCOIN_" + entidad + "_Prepaga - ExtraccionAdelanto ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/MASTERCARD/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="29";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMastercard x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	// -- CASH BACK
	@Test(dataProvider = "dataElement22MAS", dataProviderClass = ProveedorDeDatos.class)
	void opiMastercardCashbackManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "073";
		String name = "TC_" + nroTC + "_AUT_OPI_Mastercard_TOTALCOIN_" + entidad + "_Prepaga - Cashback ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/MASTERCARD/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="29";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMastercard x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	//@Test(dataProvider = "dataElement22MAS", dataProviderClass = ProveedorDeDatos.class)
	void opiMastercardCashbackMonedaExtManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "079";
		String name = "TC_" + nroTC + "_AUT_OPI_Mastercard_TOTALCOIN_" + entidad + "_Prepaga - Cashback MonedaExt ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/MASTERCARD/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="29";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMastercard x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	// -- REVERSO
	@Test(dataProvider = "dataElement22MAS", dataProviderClass = ProveedorDeDatos.class)
	void opiMastercardReversoManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "085";
		String name = "TC_" + nroTC + "_AUT_OPI_Mastercard_TOTALCOIN_" + entidad + "_Prepaga - reverso ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/MASTERCARD/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="29";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.reverso(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMastercard x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22MAS", dataProviderClass = ProveedorDeDatos.class)
	void opiMastercardReversoParcialManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "091";
		String name = "TC_" + nroTC + "_AUT_OPI_Mastercard_TOTALCOIN_" + entidad + "_Prepaga - reverso Parcial";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/MASTERCARD/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="29";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.reverso(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMastercard x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	//  -- devolucion
	@Test(dataProvider = "dataElement22MAS", dataProviderClass = ProveedorDeDatos.class)
	void opiMastercardCompraDevolucionManualUAT(String DE22) {	                                         //*******
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "109";
		String name = "TC_" + nroTC + "_AUT_OPI_Mastercard_TOTALCOIN_" + entidad + "_Prepaga - Compra Devolucion Local ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/MASTERCARD/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="29";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compraDevolucion(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMastercard x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}	
//  -- devolucion parcial
	@Test(dataProvider = "dataElement22MAS", dataProviderClass = ProveedorDeDatos.class)
	void opiMastercardCompraDevolucionParcialManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "133";
		String name = "TC_" + nroTC + "_AUT_OPI_Mastercard_TOTALCOIN" + entidad + "Prepaga - Compra Devolucion Parcial Local";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/MASTERCARD/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="29";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compraDevolucion(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se aMastercard x jUnit el resultado de la prueba
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
