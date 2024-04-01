package TestCases.OPI;

import static org.hamcrest.Matchers.equalTo;

import java.sql.SQLException;

import org.hamcrest.MatcherAssert;

import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.PrePostCondi;
import Tools.dbWorker;
import Tools.opiWorker;
import Tools.sshWorker;
import io.restassured.path.json.JsonPath;

public class TC_04_OPI {
	//Metodos para utilizar color en la consola
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_GREEN = "\u001B[32m";	
	public static final String ANSI_CYAN = "\u001B[36m";
	
	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
		//Validation var
		boolean result = true;
		try {
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_04_OPI\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//Set Variables
			String dispInicial = "10000";
			String pendCompras = "0";
			String ajustePend = "0";
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			String de39 = "00";
			String idEstado = "0";
			String disponible = "6063.1";
			String impPendComp = "2386";
			String impAjustePend = "1550.9";
			String res;
			boolean validaRes;
			int rtaQuery = 0;

			//Instancias
			sshWorker opiCmd = new sshWorker();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			opiWorker opiAcciones = new opiWorker();

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PECONDICIONES
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");

			opiAcciones.preCondicion(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			
			//EJECUCION OPI
			res = opiCmd.sshSendCmd(report, "TC_04_OPI.xml", configEntidad);

			if (res.equals(de39))
			{
				report.AddLine("Ejecucion Correcta<br>DE39:" + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39:" + res + "\r\n");
			} else {
				report.AddLineAssertionError("FAIL<br>DE39:" + res + " Se esperaba: " + de39);
				System.out.println("##[warning] : FAIL:\r\nDE39:" + res + " Se esperaba: " + de39 + "\r\n");
				result = false;
			}
			
			//VALIDACIONES
			validaRes = opiAcciones.validacionBD_CM_Autorizacion(report, oraResp, idEstado, disponible, impPendComp, impAjustePend, ID_CUENTA, configEntidad);
			if (!validaRes) {
				report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
				System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
				result = false;
			} else {
				report.AddLine("Ejecucion Correcta:<br>Se cumplieron todas las validaciones");
				System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
			}
			
			//POSTCONDICIONES
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			opiAcciones.postCondicionCM(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");
		
		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}

		return result;
	}

}
