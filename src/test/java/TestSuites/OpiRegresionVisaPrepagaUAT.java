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
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.path.json.JsonPath;
import opi.wolfgang.AUT_OPI_OPERACIONES;
import opi.wolfgang.AUT_OPI_OPERACIONES_ERROR;

@Epic("REGRESION - AUTORIZADOR VISA PERU")
@Feature("PRUEBAS DE COBERTURA DE TRX")
public class OpiRegresionVisaPrepagaUAT {
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
		path = "./Datasources/config_entidad_peru_uat.json";
		//configEntidad = new String(Files.readAllBytes(Paths.get(path)));
		configEntidad = new String(Files.readAllBytes(Paths.get(path)));
		entidad = JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad");
		path_2 = "./Datasources/cuentas_generales.json";
		cuentas_generales = new String(Files.readAllBytes(Paths.get(path_2)));
		opi=JsonPath.from(configEntidad).getString("OPI_CONFIG.opi");
		ambiente=JsonPath.from(configEntidad).getString("ENTIDAD.ambiente");
		AUT_OPI_OPERACIONES=new AUT_OPI_OPERACIONES(opi);
		AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR(opi);

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
	
	
	
	@Story("COMO usuario final\r\n "
		+ "QUIERO realizar consumos con los distintos valores en DE22\r\n  "
		+ "PARA probar todos los modos de ingreso posibles\r\n")
	@Description("SE PRUEBA CONSUMO LOCAL CON SUS VARIANTES EN DE22")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de consumo respecto de los diferentes modos de ingreso")
	void opiVisaConsumoLocal(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "001";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
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
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos con saldo insuficiente \r\n  "
			+ "PARA probar que la respuesta sea la esperada\r\n")
		@Description("SE PRUEBA CONSUMO CON SALDO INSUFICIENTE")
	@Test(description="CONSUMO CON SALDO INSUFICIENTE")
	void opiVisaCompraSaldoInsuficiente() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - Saldo Insuficiente";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.CompraLemonFondosInsuficientesUAT(report, name, configEntidad, entidad, TCFilesPath);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);

		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos con una tarjeta en estado dada de baja \r\n  "
			+ "PARA probar que la respuesta sea la esperada\r\n")
		@Description("SE PRUEBA CONSUMO CON TARJETA DADA DE BAJA")
	@Test(description="CONSUMO CON TARJETA DADA DE BAJA")
	void opiVisaTarjetaBaja() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - tarjeta dada de baja";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraConTarjetaDadaDeBajaUAT(report, DM, 0, name, configEntidad, entidad, TCFilesPath);
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
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos con una tarjeta en estado vencida\r\n  "
			+ "PARA probar que la respuesta sea la esperada\r\n")
		@Description("SE PRUEBA CONSUMO CON TARJETA VENCIDA")
	@Test(description="CONSUMO CON TARJETA VENCIDA")
	void opiVisaTarjetaVencida() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - Tarjeta Vencida";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraConTarjetaVencidaUAT(report, DM, 0, name, configEntidad, entidad, TCFilesPath);
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
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos con una tarjeta en estado inexistente \r\n  "
			+ "PARA probar que la respuesta sea la esperada\r\n")
		@Description("SE PRUEBA CONSUMO CON TARJETA INEXISTENTE")
	@Test(description="CONSUMO CON TARJETA INEXISTENTE")
	void opiVisaBinInexistente() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - Bin Inexistente";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraConBinInexistenteUAT(report, DM, 0, name, configEntidad, entidad, TCFilesPath);
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
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos donde el vencimiento no coincide con el registrado \n\n  "
			+ "PARA probar que la respuesta sea la esperada\n\n")
	@Description("SE PRUEBA CONSUMO DONDE EL VENCIMIENTO NO SEA EL INFORMADO EN LA BASE")
	@Test(description="CONSUMO DONDE EL VENCIMIENTO NO SEA EL INFORMADO EN LA BASE")
	void opiVisaVencimientoInformadoDifiereDelRegistrado() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - vencimiento informado difiere del registrado";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraVencimientoDifiereUAT(report, DM, 0, name, configEntidad, entidad, TCFilesPath);
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
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos donde la cuenta este inactiva \n\n  "
			+ "PARA probar que la respuesta sea la esperada\n\n")
	@Description("SE PRUEBA CONSUMO DONDE LA CUENTA ESTA EN ESTADO INACTIVA")
	@Test(description="CONSUMO DONDE LA CUENTA ESTA EN ESTADO INACTIVA")
	void opiVisaCuentaInactiva() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "01";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - cuenta inactiva";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraCuentaInactivaUAT(report, DM, 0, name, configEntidad, entidad, TCFilesPath);
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
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos donde el DE4 este mal configurado \n\n  "
			+ "PARA probar que la respuesta sea la esperada\n\n")
	@Description("SE PRUEBA CONSUMO DONDE DE4 SEA NULL")
	@Test(description="CONSUMO DONDE DE4 SEA NULL")
	void opiVisaCampoMontoNulo() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "02";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - CAMPO MONTO NULO";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String de39 = "30";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraCampoMontoNuloUAT(report, DM, 0, name, configEntidad, entidad, TCFilesPath, de39);
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
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos donde el monto de la operacion supera el disponible de la cuenta \n\n  "
			+ "PARA probar que la respuesta sea la esperada\n\n")
	@Description("SE PRUEBA CONSUMO DONDE EL MONTO DE LA OPERACION SUPERA EL DISPONIBLE")
	@Test(description="CONSUMO DONDE EL MONTO DE LA OPERACION SUPERA EL DISPONIBLE")
	void opiVisaMontoExcedeDisponible() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "03";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - CAMPO MONTO excede disponible en cuenta";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String de39 = "57";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraCampoMontoNuloUAT(report, DM, 0, name, configEntidad, entidad, TCFilesPath, de39);
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
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos donde el DE2 sea null \n\n  "
			+ "PARA probar que la respuesta sea la esperada\n\n")
	@Description("SE PRUEBA CONSUMO DONDE DE2 SEA NULL")
	@Test(description="CONSUMO DONDE DE2 SEA NULL")
	void opiVisaCampoTarjetaNull() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "04";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - CAMPO tarjeta null";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String de39 = "57";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraCampoMontoNuloUAT(report, DM, 0, name, configEntidad, entidad, TCFilesPath, de39);
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
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos donde el DE22 sea null \n\n  "
			+ "PARA probar que la respuesta sea la esperada\n\n")
	@Description("SE PRUEBA CONSUMO DONDE DE22 SEA NULL")
	@Test(description="CONSUMO DONDE DE22 SEA NULL")
	void opiVisaCampoModoIngresoInvalido() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "05";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - CAMPO modo de ingreso invalido";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String de39 = "30";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraCampoMontoNuloUAT(report, DM, 0, name, configEntidad, entidad, TCFilesPath, de39);
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
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos donde el valor de DE2 sea distinto en DE35 \n\n  "
			+ "PARA probar que la respuesta sea la esperada\n\n")
	@Description("SE PRUEBA CONSUMO DONDE DE2 POSEA UN VALOR DISTINTO EN DE35")
	@Test(description="CONSUMO DONDE DE2 POSEA UN VALOR DISTINTO EN DE35")
	void opiVisaPanDifiereEnDE2YDE35() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "06";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - EL PAN DIFIERE EN EL DE2 Y EL DE35";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String de39 = "5";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraCampoMontoNuloUAT(report, DM, 0, name, configEntidad, entidad, TCFilesPath, de39);
		//status = TC17_OPI.Test(report, DM, 0, name, configEntidad, cuentas_generales);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);

		// Verificamos si la ejecucion falla y guardamos el status Gral.
		//if (status == false) {
			//msg = "Fail;Fallo la ejecucion. TC: " + name;
		//}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos donde el valor de DE43 no sea válido \n\n  "
			+ "PARA probar que la respuesta sea la esperada\n\n")
	@Description("SE PRUEBA CONSUMO DONDE DE43 SEA INVALIDO")
	@Test(description="CONSUMO DONDE DE43 SEA INVALIDO")
	void opiVisaComercioInvalido() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "07";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - comercio invalido";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String de39 = "57";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraCampoMontoNuloUAT(report, DM, 0, name, configEntidad, entidad, TCFilesPath, de39);
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
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos donde el valor de DE20 no sea válido \n\n  "
			+ "PARA probar que la respuesta sea la esperada\n\n")
	@Description("SE PRUEBA CONSUMO DONDE DE20 SEA INVALIDO")
	@Test(description="CONSUMO DONDE DE20 SEA INVALIDO")
	void opiVisaCodigoMonedaInvalido() {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		
	// Nombre Real del TC
		boolean status = false;
		String nroTC = "08";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - codigo moneda invalido";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA_ERROR/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String de39 = "FAIL";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES_ERROR.compraCampoMontoNuloUAT(report, DM, 0, name, configEntidad, entidad, TCFilesPath, de39);
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
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos con los distintos valores en DE22\n\n  "
			+ "PARA probar todos los modos de ingreso posibles\n\n")
	@Description("SE PRUEBA CONSUMO LOCAL CON SUS VARIANTES EN DE22")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de consumo con moneda local respecto de los diferentes modos de ingreso")
	void opiVisaCompraManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "001";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2 = "";
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
	
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos con BIN EXTERNO\n\n  "
			+ "PARA probar todos los modos de ingreso posibles\n\n")
	@Description("SE PRUEBA CONSUMO LOCAL CON BIN EXTERNO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de consumo con bin externo respecto de los diferentes modos de ingreso")
	
	void opiVisaCompraManualBinExtUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "007";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - BinExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="20";
		String DE2 = "";
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
	
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos DE SERVICIO DIGITAL\n\n  "
			+ "PARA probar todos los modos de ingreso posibles\n\n")
	@Description("SE PRUEBA CONSUMO DE SERVICIO DIGITAL")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de consumo DE SERVICIO DIGITAL")
	
	void opiVisaCompraManualServDigitalUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "013";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local  - ServDigital";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
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
	
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos DE SERVICIO DIGITAL CON BIN EXTERNO\n\n  "
			+ "PARA probar todos los modos de ingreso posibles\n\n")
	@Description("SE PRUEBA CONSUMO DE SERVICIO DIGITAL CON BIN EXTERNO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de consumo DE SERVICIO DIGITAL CON BIN EXTERNO")
	
	void opiVisaCompraManualServDigitalBinExtUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "019";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local - BinExt - ServDigital";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="20";
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
	
	//---
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos DE SERVICIO DIGITAL EXTERNO\n\n  "
			+ "PARA probar todos los modos de ingreso posibles\n\n")
	@Description("SE PRUEBA CONSUMO DE SERVICIO DIGITAL EXTERNO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de consumo DE SERVICIO DIGITAL EXTERNO")
	
	 void opiVisaCompraManualServDigitalExtUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "025";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local  - ServDigital Ext";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
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
	
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar consumos DE SERVICIO DIGITAL EXTERNO CON BIN EXTERNO\n\n  "
			+ "PARA probar todos los modos de ingreso posibles\n\n")
	@Description("SE PRUEBA CONSUMO DE SERVICIO DIGITAL EXTERNO CON BIN EXTERNO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de consumo DE SERVICIO DIGITAL EXTERNO CON BIN EXTERNO")
	
	void opiVisaCompraManualServDigitalExtBinExtUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "031";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Local  - BinExt - ServDigital Ext";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="20";
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
	
	//   INICIO DE PRUEBAS DE DEBITO AUTOMATICO
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar TRANSACCIONES CON DEBITO AUTOMATICO\n\n  "
			+ "PARA probar todos los modos de ingreso posibles\n\n")
	@Description("SE PRUEBA TRANSACCIONES CON DEBITO AUTOMATICO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de TRANSACCIONES CON DEBITO AUTOMATICO")
	
	void opiVisaDebitoManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "037";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Debito ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
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
	
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar TRANSACCIONES CON DEBITO AUTOMATICO CON BIN EXTERNO\n\n  "
			+ "PARA probar todos los modos de ingreso posibles\n\n")
	@Description("SE PRUEBA TRANSACCIONES CON DEBITO AUTOMATICO CON BIN EXTERNO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de TRANSACCIONES CON DEBITO AUTOMATICO CON BIN EXTERNO")
	
	void opiVisaDebitoManualBinExtUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "043";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Debito  - BinExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="20";
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
	
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar TRANSACCIONES CON DEBITO AUTOMATICO CON MONEDA EXTRANJERA\n\n  "
			+ "PARA probar todos los modos de ingreso posibles\n\n")
	@Description("SE PRUEBA TRANSACCIONES CON DEBITO AUTOMATICO CON MONEDA EXTRANJERA")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de TRANSACCIONES CON DEBITO AUTOMATICO CON MONEDA EXTRANJERA")
	
	void opiVisaDebitoMonedaExtManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "049";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Debito MonedaExt ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		//status = AUT_OPI_OPERACIONES.CompraLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}

	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar TRANSACCIONES CON DEBITO AUTOMATICO CON MONEDA EXTRANJERA CON BIN EXTERNO\n\n  "
			+ "PARA probar todos los modos de ingreso posibles\n\n")
	@Description("SE PRUEBA TRANSACCIONES CON DEBITO AUTOMATICO CON MONEDA EXTRANJERA CON BIN EXTERNO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de TRANSACCIONES CON DEBITO AUTOMATICO CON MONEDA EXTRANJERA CON BIN EXTERNO")
	
	void opiVisaDebitoMonedaExtManualBinExtUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "055";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Debito MonedaExt  - BinExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		//status = AUT_OPI_OPERACIONES.CompraLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	// -- EXTRACCION ADELANTO
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar EXTRACCION / ADELANTO\n\n  "
			+ "PARA probar todos los modos de ingreso posibles\n\n")
	@Description("SE PRUEBA EXTRACCION / ADELANTO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de EXTRACCION / ADELANTO")
	
	void opiVisaExtraccionAdelantoManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "061";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - ExtraccionAdelanto ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		//status = AUT_OPI_OPERACIONES.CompraLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar EXTRACCION / ADELANTO CON BIN EXTERNO\n\n  "
			+ "PARA probar todos los modos de ingreso posibles\n\n")
	@Description("SE PRUEBA EXTRACCION / ADELANTO CON BIN EXTERNO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de EXTRACCION / ADELANTO CON BIN EXTERNO")
	
	void opiVisaExtraccionAdelantoManualBinExtUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "067";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - ExtraccionAdelanto  - BinExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="20";
		String DE2="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		//status = AUT_OPI_OPERACIONES.CompraLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	// -- CASH BACK
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar CASHBACK\n\n  "
			+ "PARA obtener efectivo mientras realizo transacciones\n\n")
	@Description("SE PRUEBA CASHBACK")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de CASHBACK")
	
	void opiVisaCashbackManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "073";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Cashback ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2="";
				
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		//status = AUT_OPI_OPERACIONES.CompraLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	//@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
	void opiVisaCashbackMonedaExtManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "079";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Cashback MonedaExt ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
		//status = AUT_OPI_OPERACIONES.CompraLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	// -- REVERSO
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar REVERSO\n\n  "
			+ "PARA reversar una trx\n\n")
	@Description("SE PRUEBA REVERSO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de REVERSO")
	
	void opiVisaReversoManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "085";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - reverso ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		//status = AUT_OPI_OPERACIONES.reversoLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
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
	
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar REVERSO PARCIAL\n\n  "
			+ "PARA reversar una trx\n\n")
	@Description("SE PRUEBA REVERSO PARCIAL")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de REVERSO PARCIAL")
	
	void opiVisaReversoParcialManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "091";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - reverso Parcial";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		//status = AUT_OPI_OPERACIONES.reversoLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
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
	
	// reverso bin externo
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar REVERSO BIN EXTERNO\n\n  "
			+ "PARA reversar una trx\n\n")
	@Description("SE PRUEBA REVERSO CON BIN EXTERNO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de REVERSO BIN EXTERNO")
	
	void opiVisaReversoManualBinExtUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "097";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - reverso BinExt ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2="";
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
	
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar REVERSO PARCIAL BIN EXTERNO\n\n  "
			+ "PARA reversar una trx\n\n")
	@Description("SE PRUEBA REVERSO PARCIAL CON BIN EXTERNO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de REVERSO PARCIAL BIN EXTERNO")
	
	void opiVisaReversoBinExtParcialManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "103";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - reverso Parcial BinExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2="";
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
	
	//  -- devolucion
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar COMPRA Y DEVOLUCION\n\n  "
			+ "PARA anular una trx\n\n")
	@Description("SE PRUEBA COMPRA Y DEVOLUCION")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de COMPRA Y DEVOLUCION")
	
	void opiVisaCompraDevolucionManualUAT(String DE22) {	                                         //*******
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "109";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Devolucion Local ";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compraDevolucion(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22,DE2, ambiente);
		//status = AUT_OPI_OPERACIONES.compraDevolucionLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar COMPRA Y DEVOLUCION con MONEDA EXTRANJERA\n\n  "
			+ "PARA anular una trx\n\n")
	@Description("SE PRUEBA COMPRA Y DEVOLUCION CON MONEDA EXTRANJERA")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de COMPRA Y DEVOLUCION CON MONEDA EXTRANJERA")
	
	void opiVisaCompraDevolucionManualMonedaExtUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "115";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Devolucion Local - MonedaExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compraDevolucion(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22,DE2, ambiente);
		//status = AUT_OPI_OPERACIONES.compraDevolucionLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	//  --  devolucion bin ext
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar COMPRA Y DEVOLUCION con BIN EXTERNO\n\n  "
			+ "PARA anular una trx\n\n")
	@Description("SE PRUEBA COMPRA Y DEVOLUCION CON BIN EXTERNO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de COMPRA Y DEVOLUCION CON BIN EXTERNO")
	
	void opiVisaCompraDevolucionManualBinExtUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "121";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Devolucion Local BinExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="20";
		String DE2="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compraDevolucion(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22,DE2, ambiente);
		//status = AUT_OPI_OPERACIONES.compraDevolucionLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar COMPRA Y DEVOLUCION con BIN EXTERNO MONEDA Y MONEDA EXTRANJERA\n\n  "
			+ "PARA anular una trx\n\n")
	@Description("SE PRUEBA COMPRA Y DEVOLUCION CON BIN EXTERNO Y MONEDA EXTRANJERA")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de COMPRA Y DEVOLUCION CON BIN EXTERNO Y MONEDA EXTRANJERA")
	
	void opiVisaCompraDevolucionManualMonedaExtBinExtUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "127";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Devolucion Local BinExt- MonedaExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="20";
		String DE2="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compraDevolucion(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22,DE2, ambiente);
		//status = AUT_OPI_OPERACIONES.compraDevolucionLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
//  -- devolucion parcial
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar COMPRA Y DEVOLUCION PARCIAL\n\n  "
			+ "PARA anular una trx\n\n")
	@Description("SE PRUEBA COMPRA Y DEVOLUCION PARCIAL")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de COMPRA Y DEVOLUCION PARCIAL")
	
	void opiVisaCompraDevolucionParcialManualUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "133";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Devolucion Parcial Local";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compraDevolucion(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22,DE2, ambiente);
		//status = AUT_OPI_OPERACIONES.compraDevolucionLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar COMPRA Y DEVOLUCION PARCIAL CON MONEDA EXTRANJERA\n\n  "
			+ "PARA anular una trx\n\n")
	@Description("SE PRUEBA COMPRA Y DEVOLUCION PARCIAL CON MONEDA EXTRANJERA")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de COMPRA Y DEVOLUCION PARCIAL CON MONEDA EXTRANJERA")
	
	void opiVisaCompraDevolucionParcialManualMonedaExtUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "139";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Devolucion Parcial Local - MonedaExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="2";
		String DE2="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compraDevolucion(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22,DE2, ambiente);
		//status = AUT_OPI_OPERACIONES.compraDevolucionLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	//  --  devolucion bin ext
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar COMPRA Y DEVOLUCION PARCIAL PARA BIN EXTERNO\n\n  "
			+ "PARA anular una trx\n\n")
	@Description("SE PRUEBA COMPRA Y DEVOLUCION PARCIAL PARA BIN EXTERNO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de COMPRA Y DEVOLUCION PARCIAL PARA BIN EXTERNO")
	
	void opiVisaCompraDevolucionParcialManualBinExtUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "145";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Devolucion Parcial Local BinExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="20";
		String DE2="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compraDevolucion(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22,DE2, ambiente);
		//status = AUT_OPI_OPERACIONES.compraDevolucionLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar COMPRA Y DEVOLUCION PARCIAL CON MONEDA EXTRANJERA PARA BIN EXTERNO\n\n  "
			+ "PARA anular una trx\n\n")
	@Description("SE PRUEBA COMPRA Y DEVOLUCION PARCIAL CON MONEDA EXTRANJERA PARA BIN EXTERNO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de COMPRA Y DEVOLUCION PARCIAL CON MONEDA EXTRANJERA PARA BIN EXTERNO")
	
	void opiVisaCompraDevolucionParcialManualMonedaExtBinExtUAT(String DE22) {	
		// DEFINITIONS
		if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
		////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
		// Nombre Real del TC
		boolean status = false;
		String nroTC = "151";
		String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - Compra Devolucion Parcial Local BinExt- MonedaExt";
		String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
		String msg = "True;Resultado de la ejecucion OK. TC: " + name;
		String ID_CUENTA="20";
		String DE2="";
		// Inicializacion de las Variables del Repositorio
		repoVar.setTipoTc("SSH");
		repoVar.setResult(status);
		repoVar.setDataMsg(name);
		// SET THE EXECUTION PLAN
		status = AUT_OPI_OPERACIONES.compraDevolucion(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22,DE2, ambiente);
		//status = AUT_OPI_OPERACIONES.compraDevolucionLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
		// Configuracion de variables para el armado del reporte
		repoVar.setResult(status);
		// Verificamos si la ejecucion falla y guardamos el status Gral.
		if (status == false) {
			msg = "Fail;Fallo la ejecucion. TC: " + name;
		}
		// Se avisa x jUnit el resultado de la prueba
		AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
	}
	
	// -- consulta ASI - CONSULTA DE SALDO
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar CONSULTAS ASI\n\n  "
			+ "PARA conocer disponible\n\n")
	@Description("SE PRUEBA CONSULTA ASI")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de CONSULTA ASI")
	
		void opiVisaConsultaASIManualUAT(String DE22) {	
			// DEFINITIONS
			if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
			////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
			// Nombre Real del TC
			boolean status = false;
			String nroTC = "157";
			String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - ConsultaASI Local ";
			String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
			String msg = "True;Resultado de la ejecucion OK. TC: " + name;
			String ID_CUENTA="2";
			String DE2="";
			// Inicializacion de las Variables del Repositorio
			repoVar.setTipoTc("SSH");
			repoVar.setResult(status);
			repoVar.setDataMsg(name);
			// SET THE EXECUTION PLAN
			status = AUT_OPI_OPERACIONES.consultaASI(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
			//status = AUT_OPI_OPERACIONES.ConsultaASILemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
			// Configuracion de variables para el armado del reporte
			repoVar.setResult(status);
			// Verificamos si la ejecucion falla y guardamos el status Gral.
			if (status == false) {
				msg = "Fail;Fallo la ejecucion. TC: " + name;
			}
			// Se avisa x jUnit el resultado de la prueba
			AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
		}
	
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar CONSULTAS ASI PARA BIN EXTERNO\n\n  "
			+ "PARA conocer disponible\n\n")
	@Description("SE PRUEBA CONSULTA ASI PARA BIN EXTERNO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de CONSULTA ASI PARA BIN EXTERNO")
	
		void opiVisaConsultaASIManualBinExtUAT(String DE22) {	
			// DEFINITIONS
			if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
			////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
			// Nombre Real del TC
			boolean status = false;
			String nroTC = "163";
			String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - ConsultaASI Local - BinExt";
			String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
			String msg = "True;Resultado de la ejecucion OK. TC: " + name;
			String ID_CUENTA="20";
			String DE2="";
			// Inicializacion de las Variables del Repositorio
			repoVar.setTipoTc("SSH");
			repoVar.setResult(status);
			repoVar.setDataMsg(name);
			// SET THE EXECUTION PLAN
			status = AUT_OPI_OPERACIONES.consultaASI(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
			//status = AUT_OPI_OPERACIONES.ConsultaASILemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
			// Configuracion de variables para el armado del reporte
			repoVar.setResult(status);
			// Verificamos si la ejecucion falla y guardamos el status Gral.
			if (status == false) {
				msg = "Fail;Fallo la ejecucion. TC: " + name;
			}
			// Se avisa x jUnit el resultado de la prueba
			AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
		}
	
	// -- VISA DIRECT OCT
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar OPERACIONES VISA DIRECT\n\n")
	@Description("SE PRUEBA VISA DIRECT OCT")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de VISA DIRECT OCT")
	
		void opiVisaDirectOCTManualUAT(String DE22) {	
			// DEFINITIONS
			if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
			////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
			// Nombre Real del TC
			boolean status = false;
			String nroTC = "169";
			String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - DirectOCT Local";
			String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
			String msg = "True;Resultado de la ejecucion OK. TC: " + name;
			String ID_CUENTA="2";
			String DE2="";
			// Inicializacion de las Variables del Repositorio
			repoVar.setTipoTc("SSH");
			repoVar.setResult(status);
			repoVar.setDataMsg(name);
			// SET THE EXECUTION PLAN
			status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
			//status = AUT_OPI_OPERACIONES.CompraLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
			// Configuracion de variables para el armado del reporte
			repoVar.setResult(status);
			// Verificamos si la ejecucion falla y guardamos el status Gral.
			if (status == false) {
				msg = "Fail;Fallo la ejecucion. TC: " + name;
			}
			// Se avisa x jUnit el resultado de la prueba
			AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
		}
	
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar OPERACIONES VISA DIRECT PARA BIN EXTERNO\n\n")
	@Description("SE PRUEBA VISA DIRECT OCT PARA BIN EXTERNO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de VISA DIRECT OCT PARA BIN EXTERNO")
	
		void opiVisaDirectOCTManualBinExtUAT(String DE22) {	
			// DEFINITIONS
			if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
			////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
			// Nombre Real del TC
			boolean status = false;
			String nroTC = "175";
			String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - DirectOCT Local - BinExt";
			String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
			String msg = "True;Resultado de la ejecucion OK. TC: " + name;
			String ID_CUENTA="20";
			String DE2="";
			// Inicializacion de las Variables del Repositorio
			repoVar.setTipoTc("SSH");
			repoVar.setResult(status);
			repoVar.setDataMsg(name);
			// SET THE EXECUTION PLAN
			status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
			//status = AUT_OPI_OPERACIONES.CompraLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
			// Configuracion de variables para el armado del reporte
			repoVar.setResult(status);
			// Verificamos si la ejecucion falla y guardamos el status Gral.
			if (status == false) {
				msg = "Fail;Fallo la ejecucion. TC: " + name;
			}
			// Se avisa x jUnit el resultado de la prueba
			AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
		}
	
		// -- VISA DIRECT AFT
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar OPERACIONES VISA DIRECT AFT \n\n")
	@Description("SE PRUEBA VISA DIRECT AFT ")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de VISA DIRECT AFT ")
	
				void opiVisaDirectAFTManualUAT(String DE22) {	
					// DEFINITIONS
					if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
					////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "181";
					String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - DirectAFT Local ";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
					String msg = "True;Resultado de la ejecucion OK. TC: " + name;
					String ID_CUENTA="2";
					String DE2="";
					// Inicializacion de las Variables del Repositorio
					repoVar.setTipoTc("SSH");
					repoVar.setResult(status);
					repoVar.setDataMsg(name);
					// SET THE EXECUTION PLAN
					status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
					//status = AUT_OPI_OPERACIONES.CompraLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
					// Configuracion de variables para el armado del reporte
					repoVar.setResult(status);
					// Verificamos si la ejecucion falla y guardamos el status Gral.
					if (status == false) {
						msg = "Fail;Fallo la ejecucion. TC: " + name;
					}
					// Se avisa x jUnit el resultado de la prueba
					AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
				}
				
	@Story("COMO usuario final\r\n "
			+ "QUIERO realizar OPERACIONES VISA DIRECT AFT PARA BIN EXTERNO\n\n")
	@Description("SE PRUEBA VISA DIRECT AFT PARA BIN EXTERNO")
	@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de VISA DIRECT AFT PARA BIN EXTERNO")
	
				void opiVisaDirectAFTManualBinExtUAT(String DE22) {	
					// DEFINITIONS
					if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
					////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "187";
					String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - DirectAFT Local - BinExt";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
					String msg = "True;Resultado de la ejecucion OK. TC: " + name;
					String ID_CUENTA="20";
					String DE2="";
					// Inicializacion de las Variables del Repositorio
					repoVar.setTipoTc("SSH");
					repoVar.setResult(status);
					repoVar.setDataMsg(name);
					// SET THE EXECUTION PLAN
					status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
					//status = AUT_OPI_OPERACIONES.CompraLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
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
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
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
				@Story("COMO usuario final\r\n "
						+ "QUIERO realizar OPERACIONES VISA DIRECT CON MONEDA EXTRANJERA\n\n")
				@Description("SE PRUEBA VISA DIRECT OCT CON MONEDA EXTRANJERA")
				@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de VISA DIRECT OCT CON MONEDA EXTRANJERA")
				
				void opiVisaDirectOCTMonedaExtUAT(String DE22) {	
					// DEFINITIONS
					if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
					////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "194";
					String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - DirectOCT Local ";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
					String msg = "True;Resultado de la ejecucion OK. TC: " + name;
					String ID_CUENTA="2";
					String DE2="";
					// Inicializacion de las Variables del Repositorio
					repoVar.setTipoTc("SSH");
					repoVar.setResult(status);
					repoVar.setDataMsg(name);
					// SET THE EXECUTION PLAN
					status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
					//status = AUT_OPI_OPERACIONES.CompraLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
					// Configuracion de variables para el armado del reporte
					repoVar.setResult(status);
					// Verificamos si la ejecucion falla y guardamos el status Gral.
					if (status == false) {
						msg = "Fail;Fallo la ejecucion. TC: " + name;
					}
					// Se avisa x jUnit el resultado de la prueba
					AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
				}
				@Story("COMO usuario final\r\n "
						+ "QUIERO realizar OPERACIONES VISA DIRECT PARA BIN EXTERNO EN MONEDA EXTRANJERA\n\n")
				@Description("SE PRUEBA VISA DIRECT OCT PARA BIN EXTERNO EN MONEDA EXTRANJERA")
				@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de VISA DIRECT OCT PARA BIN EXTERNO EN MONEDA EXTRANJERA")
				
				void opiVisaDirectOCTManualBinExtMonedaExtUAT(String DE22) {	
					// DEFINITIONS
					if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
					////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "195";
					String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - DirectOCT Local - BinExt";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
					String msg = "True;Resultado de la ejecucion OK. TC: " + name;
					String ID_CUENTA="20";
					String DE2="";
					// Inicializacion de las Variables del Repositorio
					repoVar.setTipoTc("SSH");
					repoVar.setResult(status);
					repoVar.setDataMsg(name);
					// SET THE EXECUTION PLAN
					status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
					//status = AUT_OPI_OPERACIONES.CompraLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
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
				@Story("COMO usuario final\r\n "
						+ "QUIERO realizar OPERACIONES VISA DIRECT  EN MONEDA EXTRANJERA\n\n")
				@Description("SE PRUEBA VISA DIRECT AFT  EN MONEDA EXTRANJERA")
				@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de VISA DIRECT AFT EN MONEDA EXTRANJERA")
				
				void opiVisaDirectAFTMonedaExtUAT(String DE22) {	
					// DEFINITIONS
					if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
					////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "196";
					String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - DirectAFT Local";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
					String msg = "True;Resultado de la ejecucion OK. TC: " + name;
					String ID_CUENTA="2";
					String DE2="";
					// Inicializacion de las Variables del Repositorio
					repoVar.setTipoTc("SSH");
					repoVar.setResult(status);
					repoVar.setDataMsg(name);
					// SET THE EXECUTION PLAN
					status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
					//status = AUT_OPI_OPERACIONES.CompraLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
					// Configuracion de variables para el armado del reporte
					repoVar.setResult(status);
					// Verificamos si la ejecucion falla y guardamos el status Gral.
					if (status == false) {
						msg = "Fail;Fallo la ejecucion. TC: " + name;
					}
					// Se avisa x jUnit el resultado de la prueba
					AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
				}
				@Story("COMO usuario final\r\n "
						+ "QUIERO realizar OPERACIONES VISA DIRECT PARA BIN EXTERNO EN MONEDA EXTRANJERA\n\n")
				@Description("SE PRUEBA VISA DIRECT AFT PARA BIN EXTERNO EN MONEDA EXTRANJERA")
				@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de VISA DIRECT AFT PARA BIN EXTERNO EN MONEDA EXTRANJERA")
				
				void opiVisaDirectAFTMonedaExtBinExtUAT(String DE22) {	
					// DEFINITIONS
					if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
					////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "197";
					String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - DirectAFT Local - BinExt";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
					String msg = "True;Resultado de la ejecucion OK. TC: " + name;
					String ID_CUENTA="20";
					String DE2="";
					// Inicializacion de las Variables del Repositorio
					repoVar.setTipoTc("SSH");
					repoVar.setResult(status);
					repoVar.setDataMsg(name);
					// SET THE EXECUTION PLAN
					status = AUT_OPI_OPERACIONES.compra(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22, DE2, ambiente);
					//status = AUT_OPI_OPERACIONES.CompraLemonUAT(report, name, configEntidad, entidad, TCFilesPath, ID_CUENTA, DE22);
					// Configuracion de variables para el armado del reporte
					repoVar.setResult(status);
					// Verificamos si la ejecucion falla y guardamos el status Gral.
					if (status == false) {
						msg = "Fail;Fallo la ejecucion. TC: " + name;
					}
					// Se avisa x jUnit el resultado de la prueba
					AssertJUnit.assertEquals("Resultado: " + msg.split(";")[1], "True", msg.split(";")[0]);
				}
				@Story("COMO usuario final\r\n "
						+ "QUIERO realizar trnsacciones de autorizaion parcial PARA poder abonar diferencia con otro medio de pago si el disponible de mi tarjeta no es suficiente\n\n")
				@Description("SE PRUEBA VISA AUTORIZACIONES PARCIALES")
				@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class, description = "debera generar diferentes tipos de VISA AUTORIZACIONES PARCIALES")
				
				void opiVisaAutorizacionParcial(String DE22) {	
					// DEFINITIONS
					if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
					////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "198";
					String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - AUTORIZACION PARCIAL";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
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
				//@Test(dataProvider = "dataElement22", dataProviderClass = ProveedorDeDatos.class)
				void opiVisaAutorizacionParcialBinExt(String DE22) {	
					// DEFINITIONS
					if(repoVar == null) {try {initAll();} catch (IOException e) {e.printStackTrace();}}
					////AUT_OPI_OPERACIONES_ERROR AUT_OPI_OPERACIONES_ERROR = new AUT_OPI_OPERACIONES_ERROR();
					// Nombre Real del TC
					boolean status = false;
					String nroTC = "199";
					String name = "TC_" + nroTC + "_AUT_OPI_VISA_LEMON" + entidad + "Prepaga - AUTORIZACION PARCIAL - BIN EXTERNO";
					String TCFilesPath = "./TC_Files/OPI/" + entidad + "/REGRESION/VISA/UAT/PREPAGA/TC_" + nroTC;
					String msg = "True;Resultado de la ejecucion OK. TC: " + name;
					String ID_CUENTA="20";
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
