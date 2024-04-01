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

public class OpiRegresionVisaPrepaga {
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
	static AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR;

	@BeforeSuite
	static void initAll() throws IOException {
		DM = new DriverManager();
		data = new Datasources();
		report = new Reports();
		repoVar = new Repo_Variables();
		path = "./Datasources/config_entidad_peru_qa.json";
		//configEntidad = new String(Files.readAllBytes(Paths.get(path)));
		configEntidad = new String(Files.readAllBytes(Paths.get(path)));
		entidad = JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad");
		path_2 = "./Datasources/cuentas_generales.json";
		cuentas_generales = new String(Files.readAllBytes(Paths.get(path_2)));
		opi=JsonPath.from(configEntidad).getString("OPI_CONFIG.opi");
		ambiente=JsonPath.from(configEntidad).getString("ENTIDAD.ambiente");
		AUT_OPI_OPERACIONES=new AUT_OPI_OPERACIONES(opi);
		AUT_OPI_OPERACIONES_ERROR=new AUT_OPI_OPERACIONES_ERROR(opi);

	}

	/*@BeforeClass
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
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaConsumoLocal(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "001";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - Manual";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2="";
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
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(priority=1)
	void opiVisaCompraSaldoInsuficiente() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - Saldo Insuficiente";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.CompraLemonFondosInsuficientes(report, name, configEntidad, entidad, TCFilesPath);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);

		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=1)
	void opiVisaTarjetaBaja() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - Saldo Insuficiente";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraConTarjetaDadaDeBaja(report, DM, 0, name, configEntidad, entidad, TCFilesPath);
		//status = TC17_OPI.Test(report, DM, 0, name, configEntidad, cuentas_generales);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);

		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=1)
	void opiVisaTarjetaVencida() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - Tarjeta Vencida";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraConTarjetaVencida(report, DM, 0, name, configEntidad, entidad, TCFilesPath);
		//status = TC17_OPI.Test(report, DM, 0, name, configEntidad, cuentas_generales);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);

		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=1)
	void opiVisaBinInexistente() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - Bin Inexistente";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraConBinInexistente(report, DM, 0, name, configEntidad, entidad, TCFilesPath);
		//status = TC17_OPI.Test(report, DM, 0, name, configEntidad, cuentas_generales);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);

		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=1)
	void opiVisaVencimientoInformadoDifiereDelRegistrado() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - vencimiento informado difiere del registrado";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraVencimientoDifiere(report, DM, 0, name, configEntidad, entidad, TCFilesPath);
		//status = TC17_OPI.Test(report, DM, 0, name, configEntidad, cuentas_generales);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);

		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=1)
	void opiVisaCuentaInactiva() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - cuenta inactiva";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraCuentaInactiva(report, DM, 0, name, configEntidad, entidad, TCFilesPath);
		//status = TC17_OPI.Test(report, DM, 0, name, configEntidad, cuentas_generales);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);

		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=1)
	void opiVisaCampoMontoNulo() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "02";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - CAMPO MONTO NULO";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String de39 = "30";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraCampoMontoNulo(report, DM, 0, name, configEntidad, entidad, TCFilesPath, de39);
		//status = TC17_OPI.Test(report, DM, 0, name, configEntidad, cuentas_generales);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);

		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=1)
	void opiVisaMontoExcedeDisponible() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "03";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - CAMPO MONTO excede disponible en cuenta";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String de39 = "57";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraCampoMontoNulo(report, DM, 0, name, configEntidad, entidad, TCFilesPath, de39);
		//status = TC17_OPI.Test(report, DM, 0, name, configEntidad, cuentas_generales);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);

		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=1)
	void opiVisaCampoTarjetaNull() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "04";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - CAMPO tarjeta null";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String de39 = "57";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraCampoMontoNulo(report, DM, 0, name, configEntidad, entidad, TCFilesPath, de39);
		//status = TC17_OPI.Test(report, DM, 0, name, configEntidad, cuentas_generales);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);

		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=1)
	void opiVisaCampoModoIngresoInvalido() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "05";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - CAMPO modo de ingreso invalido";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String de39 = "30";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraCampoMontoNulo(report, DM, 0, name, configEntidad, entidad, TCFilesPath, de39);
		//status = TC17_OPI.Test(report, DM, 0, name, configEntidad, cuentas_generales);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);

		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=1)
	void opiVisaPanDifiereEnDE2YDE35() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "06";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - EL PAN DIFIERE EN EL DE2 Y EL DE35";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String de39 = "5";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraCampoMontoNulo(report, DM, 0, name, configEntidad, entidad, TCFilesPath, de39);
		//status = TC17_OPI.Test(report, DM, 0, name, configEntidad, cuentas_generales);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);

		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=1)
	void opiVisaComercioInvalido() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "07";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - comercio invalido";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String de39 = "57";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraCampoMontoNulo(report, DM, 0, name, configEntidad, entidad, TCFilesPath, de39);
		//status = TC17_OPI.Test(report, DM, 0, name, configEntidad, cuentas_generales);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);

		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(priority=1)
	void opiVisaCodigoMonedaInvalido() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "08";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - codigo moneda invalido";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String de39 = "FAIL";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraCampoMontoNulo(report, DM, 0, name, configEntidad, entidad, TCFilesPath, de39);
		//status = TC17_OPI.Test(report, DM, 0, name, configEntidad, cuentas_generales);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);

		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	// -- COMPRA
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class )
	void opiVisaCompraManualQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "001";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - MONDO INGRESO MANUAL - BANDA - CODIGO - CHIP - ECOMMERCE - CONTACTLES";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA = "2";
		String DE2 ="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);// agregar como parametro el DE22 que viene como parametro en el test, que a su vez es provisto por el dataprovider
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaCompraManualBinExtQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "007";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - MONDO INGRESO MANUAL - BANDA - CODIGO - CHIP - ECOMMERCE - CONTACTLES - BinExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA = "27";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		String DE2 ="";
		// SET THE EXECUTION PLAN
		//status = AUT_OPI_OPERACIONES.CompraLemonQA(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA,DE22);
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaCompraBandaServDigitalQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "014";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - Banda - ServDigital";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA = "2";
		String DE2 ="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		//status = AUT_OPI_OPERACIONES.CompraLemonQA(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaCompraManualServDigitalBinExtQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "019";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - Manual - BinExt - ServDigital";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="27";
		String DE2 ="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		//status = AUT_OPI_OPERACIONES.CompraLemonQA(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaCompraManualServDigitalExtQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "025";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - Manual - ServDigital Ext";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA = "2";
		String DE2 ="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		//status = AUT_OPI_OPERACIONES.CompraLemonQA(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaCompraManualServDigitalExtBinExtQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "031";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - Manual - BinExt - ServDigital Ext";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		String ID_CUENTA = "27";
		String DE2 ="";
		// SET THE EXECUTION PLAN
		//status = AUT_OPI_OPERACIONES.CompraLemonQA(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaDebitoManualQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "037";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Debito - Manual";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		String ID_CUENTA="2";
		String DE2 ="";
		// SET THE EXECUTION PLAN
		//status = AUT_OPI_OPERACIONES.CompraLemonQA(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaDebitoManualBinExtQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "043";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Debito - Manual - BinExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="27";
		String DE2 ="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		//status = AUT_OPI_OPERACIONES.CompraLemonQA(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaDebitoMonedaExtManualQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "049";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Debito MonedaExt - Manual";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2 ="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		//status = AUT_OPI_OPERACIONES.CompraLemonQA(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaDebitoMonedaExtManualBinExtQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "055";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Debito MonedaExt - Manual - BinExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="27";
		String DE2 ="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		//status = AUT_OPI_OPERACIONES.CompraLemonQA(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaExtraccionAdelantoManualQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "061";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - ExtraccionAdelanto - Manual";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2 ="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		//status = AUT_OPI_OPERACIONES.CompraLemonQA(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaExtraccionAdelantoManualBinExtQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "067";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - ExtraccionAdelanto - Manual - BinExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="27";
		String DE2 ="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		//status = AUT_OPI_OPERACIONES.CompraLemonQA(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaCashbackManualQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "073";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Cashback - Manual";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2 ="";
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
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaCashbackMonedaExtManualQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "079";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Cashback MonedaExt - Manual";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2 ="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		//status = AUT_OPI_OPERACIONES.CompraLemonQA(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaReversoManualQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "085";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - reverso ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2 ="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		//status = AUT_OPI_OPERACIONES.reversoLemonQA(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		status = AUT_OPI_OPERACIONES.reverso(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaReversoParcialManualQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "091";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - reverso Parcial";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2 ="";
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
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaReversoManualBinExtQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "097";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - reverso BinExt - Contactless";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="27";
		String DE2 ="";
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
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaReversoBinExtParcialManualQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "103";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - reverso Parcial BinExt- Contactless";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="27";
		String DE2 ="";
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
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaCompraDevolucionManualQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "109";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Devolucion Local - Manual";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2 ="";
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
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaCompraDevolucionManualMonedaExtQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "115";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Devolucion Local - MonedaExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2 ="";
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
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaCompraDevolucionManualBinExtQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "121";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Devolucion Local BinExt- Manual";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="27";
		String DE2 ="";
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
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaCompraDevolucionManualMonedaExtBinExtQA(String DE22) {	             
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "127";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Devolucion Local BinExt- MonedaExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="27";
		String DE2 ="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compraDevolucion(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22,DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaCompraDevolucionParcialManualQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "133";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Devolucion Parcial Local - Manual";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2 ="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compraDevolucion(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22,DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaCompraDevolucionParcialManualMonedaExtQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "139";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Devolucion Parcial Local - MonedaExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2 ="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compraDevolucion(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22,DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaCompraDevolucionParcialManualBinExtQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "145";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Devolucion Parcial Local BinExt- Manual";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="27";
		String DE2 ="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compraDevolucion(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22,DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaCompraDevolucionParcialManualMonedaExtBinExtQA(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "151";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Devolucion Parcial Local BinExt- MonedaExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="27";
		String DE2 ="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compraDevolucion(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22,DE2, ambiente);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
		@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
		void opiVisaConsultaASIManualQA(String DE22) {	
			// DEFINITIONS
			if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
			//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
			// Nombre Real del TC
			boolean status = false;
			String nroTC = "157";
			String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - ConsultaASI Local - Manual";
			String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
			String msg = "True;Resultado de la ejecucion OK. TC: " + name;
			String ID_CUENTA="2";
			String DE2 ="";
			// Inicializacion de las Variables del Repositorio
			repoVar.setTipoTc("SSH");
			repoVar.setResult(status);
			repoVar.setDataMsg(name);
			// SET THE EXECUTION PLAN	
			status = AUT_OPI_OPERACIONES.consultaASI(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
			// Configuracion de variables para el armado del reporte
			repoVar.setResult(status);
			// Verificamos si la ejecucion falla y guardamos el status Gral.
			if (status == false) {
				msg = "Fail;Fallo la ejecucion. TC: " + name;
			}
			// Se avisa x jUnit el resultado de la prueba
			AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
		}
	
		@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
		void opiVisaConsultaASIManualBinExtQA(String DE22) {	
			// DEFINITIONS
			if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
			//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
			// Nombre Real del TC
			boolean status = false;
			String nroTC = "163";
			String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - ConsultaASI Local - Manual - BinExt";
			String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
			String msg = "True;Resultado de la ejecucion OK. TC: " + name;
			String ID_CUENTA="27";
			String DE2 ="";
			// Inicializacion de las Variables del Repositorio
			repoVar.setTipoTc("SSH");
			repoVar.setResult(status);
			repoVar.setDataMsg(name);
			// SET THE EXECUTION PLAN
			status = AUT_OPI_OPERACIONES.consultaASI(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
			// Configuracion de variables para el armado del reporte
			repoVar.setResult(status);
			// Verificamos si la ejecucion falla y guardamos el status Gral.
			if (status == false) {
				msg = "Fail;Fallo la ejecucion. TC: " + name;
			}
			// Se avisa x jUnit el resultado de la prueba
			AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
		}
		
		@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
		void opiVisaDirectOCTManualQA(String DE22) {	
			// DEFINITIONS
			if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
			//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
			// Nombre Real del TC
			boolean status = false;
			String nroTC = "169";
			String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - DirectOCT Local - Manual";
			String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
			String msg = "True;Resultado de la ejecucion OK. TC: " + name;
			String ID_CUENTA="2";
			String DE2 ="";
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
			// Se avisa x jUnit el resultado de la prueba
			AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
		}
	
		@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
		void opiVisaDirectOCTManualBinExtQA(String DE22) {	
			// DEFINITIONS
			if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
			//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
			// Nombre Real del TC
			boolean status = false;
			String nroTC = "175";
			String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - DirectOCT Local - Manual - BinExt";
			String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
			String msg = "True;Resultado de la ejecucion OK. TC: " + name;
			String ID_CUENTA="27";
			String DE2 ="";
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
			// Se avisa x jUnit el resultado de la prueba
			AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
		}
	
				@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
				void opiVisaDirectAFTManualQA(String DE22) {	
					// DEFINITIONS
					if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
					//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "181";
					String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - DirectAFT Local - Manual";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
					String msg = "True;Resultado de la ejecucion OK. TC: " + name;
					String ID_CUENTA="2";
					String DE2 ="";
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
					// Se avisa x jUnit el resultado de la prueba
					AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
				}			
				@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
				void opiVisaDirectAFTManualBinExtQA(String DE22) {	
					// DEFINITIONS
					if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
					//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "187";
					String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - DirectAFT Local - Manual - BinExt";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
					String msg = "True;Resultado de la ejecucion OK. TC: " + name;
					String ID_CUENTA="27";
					String DE2 ="";
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
					// Se avisa x jUnit el resultado de la prueba
					AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
				}
			
	// -- CASOS GLOBALES  PRIMER CASO 6-12-23
				//@Test(priority = 1, groups = { "compra" })
				void TC_1_compraApiAutorizador() {
					// DEFINITIONS
					AUT_API_OPERACIONES AUT_API_OPERACIONES = new AUT_API_OPERACIONES();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "193";
					String name = "TC_" + nroTC + "_API_OPERACIONES_VISA_" + entidad + " compra - local - manual";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
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
				
				// -- VISA DIRECT OCT - MONEDA EXT
				@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
				void opiVisaDirectOCTMonedaExtQA(String DE22) {	
					// DEFINITIONS
					if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
					//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "194";
					String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - DirectOCT Local - Manual";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
					String msg = "True;Resultado de la ejecucion OK. TC: " + name;
					String ID_CUENTA="2";
					String DE2 ="";
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
					// Se avisa x jUnit el resultado de la prueba
					AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
				}
				@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
				void opiVisaDirectOCTManualBinExtMonedaExtQA(String DE22) {	
					// DEFINITIONS
					if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
					//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "195";
					String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - DirectOCT Local - Manual - BinExt";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
					String msg = "True;Resultado de la ejecucion OK. TC: " + name;
					String ID_CUENTA="27";
					String DE2 ="";
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
					// Se avisa x jUnit el resultado de la prueba
					AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
				}
				// -- VISA DIRECT AFT - MONEDA EXTRANJERA
				@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
				void opiVisaDirectAFTMonedaExtQA(String DE22) {	
					// DEFINITIONS
					if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
					//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "196";
					String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - DirectAFT Local - Manual";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
					String msg = "True;Resultado de la ejecucion OK. TC: " + name;
					String ID_CUENTA="2";
					String DE2 ="";
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
					// Se avisa x jUnit el resultado de la prueba
					AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
				}
				@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
				void opiVisaDirectAFTMonedaExtBinExtQA(String DE22) {	
					// DEFINITIONS
					if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
					//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "197";
					String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - DirectAFT Local - Manual - BinExt";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
					String msg = "True;Resultado de la ejecucion OK. TC: " + name;
					String ID_CUENTA="27";
					String DE2 ="";
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
					// Se avisa x jUnit el resultado de la prueba
					AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
				}
				@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
				void opiVisaAutorizacionParcial(String DE22) {	
					// DEFINITIONS
					if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
					//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "198";
					String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - Manual";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
					String msg = "True;Resultado de la ejecucion OK. TC: " + name;
					String ID_CUENTA="2";
					String DE2 ="";
					// Inicializacion de las Variables del Repositorio
					repoVar.setTipoTc("SSH");
					repoVar.setResult(status);
					repoVar.setDataMsg(name);
					// SET THE EXECUTION PLAN
					status = AUT_OPI_OPERACIONES.autorizacionParcial(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
					// Configuracion de variables para el armado del reporte
					repoVar.setResult(status);
					// Verificamos si la ejecucion falla y guardamos el status Gral.
					if (status == false) {
						msg = "Fail;Fallo la ejecucion. TC: " + name;
					}
					// Se avisa x jUnit el resultado de la prueba
					AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
				}
				@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
				void opiVisaAutorizacionParcialBinExt(String DE22) {	
					// DEFINITIONS
					if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
					//AUT_OPI_OPERACIONES AUT_OPI_OPERACIONES = new AUT_OPI_OPERACIONES();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "199";
					String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - Manual";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/QA/PREPAGA/TC_" + nroTC;
					String msg = "True;Resultado de la ejecucion OK. TC: " + name;
					String ID_CUENTA="27";
					String DE2 ="";
					// Inicializacion de las Variables del Repositorio
					repoVar.setTipoTc("SSH");
					repoVar.setResult(status);
					repoVar.setDataMsg(name);
					// SET THE EXECUTION PLAN
					status = AUT_OPI_OPERACIONES.autorizacionParcial(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
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
