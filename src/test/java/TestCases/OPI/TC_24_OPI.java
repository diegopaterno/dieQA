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

public class TC_24_OPI {

	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
		//Validation var
		boolean result = true;
		try {
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_24_OPI\r\n");

			//Set Variables
			String dispInicial = "10000";
			String pendCompras = "0";
			String ajustePend = "0";
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			String ID_PROVINCIA = "5";
			String ID_CODIGO_POSTAL = "5000";
			String ID_PROVINCIA_ROLLBACK = "15";
			String ID_CODIGO_POSTAL_ROLLBACK = "8300";
			//Resultados esperados
			String de39 = "00";
			String idEstado = "0";
			String disponible = "9207.854";
			String impPendCompras = "477.2";
			String impAjustePend = "314.946";
			String res;
			String resDomi[];
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
			
			resDomi = oraResp.oraTwoQuery(report, "SELECT ID_PROVINCIA, ID_CODIGO_POSTAL\r\n"
					+ "FROM DOMICILIOS WHERE ID_DOMICILIO = (SELECT ID_DOMICILIO FROM SUCURSALES_ENTIDAD WHERE ID_SUCURSAL = (SELECT ID_SUCURSAL FROM CUENTAS WHERE ID_CUENTA = " + ID_CUENTA + "))", configEntidad);
			
			ID_PROVINCIA_ROLLBACK = resDomi[0];
			
			ID_CODIGO_POSTAL_ROLLBACK = resDomi[1];
			
			System.out.println(ID_PROVINCIA_ROLLBACK);
			
			System.out.println(ID_CODIGO_POSTAL_ROLLBACK);
			
			
			//Se setea el domicilio legal de la cuenta segun correspponda aplicar los ajustes
			opiAcciones.setDomicilioLegal(oraResp, ppCondi, report, dispInicial, impPendCompras, impAjustePend, ID_CUENTA, configEntidad, ID_PROVINCIA, ID_CODIGO_POSTAL);
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//EJECUCION OPI
			res = opiCmd.sshSendCmd(report, "TC_24_OPI.xml", configEntidad);

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
			validaRes = opiAcciones.validacionBD_CM_Autorizacion(report, oraResp, idEstado, disponible, impPendCompras, impAjustePend, ID_CUENTA, configEntidad);
			if (!validaRes) {
				report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
				System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
				result = false;
			} else {
				report.AddLine("Ejecucion Correcta<br>Se cumplieron todas las validaciones");
				System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
			}
			
			//POSTCONDICIONES
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			opiAcciones.postCondicionCM(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			opiAcciones.setDomicilioLegal(oraResp, ppCondi, report, dispInicial, impPendCompras, impAjustePend, ID_CUENTA, configEntidad, ID_PROVINCIA_ROLLBACK, ID_CODIGO_POSTAL_ROLLBACK);
						
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
