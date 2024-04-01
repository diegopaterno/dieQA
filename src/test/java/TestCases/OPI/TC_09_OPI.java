package TestCases.OPI;

import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.MatcherAssert;

import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.PrePostCondi;
import Tools.dbWorker;
import Tools.opiWorker;
import Tools.sshWorker;
import io.restassured.path.json.JsonPath;

public class TC_09_OPI {

	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
		//Validation var
		boolean result = true;
		try {
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_09_OPI\r\n");

			//Set Variables
			//Datos para generar autorizacion de precondicion
			String dispInicial = "4891.6336";
			String pendCompras = "3087.48";
			String ajustePend = "2020.8864";
			String tid = "MCC0113UM1018  ";
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			//Resultados esperados
			String de39 = "00";
			String idEstado = "2";
			String disponibleEsperado = "4891.6336";
			String impPendCompEsperado = "3087.48";
			String impAjustePendEsperado = "2020.8864";
			String res;
			//XML de la autorizacion de la preconcidion
			String XML = "TC_08_OPI.xml";
			boolean validaRes;			
			boolean respuestaPreCondi;
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

			//PRECONDICION PARA GENERAR AUTORIZACION NECESARIA PARA EL REVERSO
			respuestaPreCondi = opiAcciones.preCondicionParaReversosOPIDevoluciones(report, XML, dispInicial, pendCompras, ajustePend, ID_CUENTA, tid, configEntidad);
			
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			if(respuestaPreCondi) {
				report.AddLine("La Devolución Parcial Ecommer como precondicion al reverso se genero correctamente");
				System.out.println("##[section] : La Devolución Parcial Ecommer como precondicion al reverso se genero correctamente\r\n");
			}
			else {
				report.AddLine("La Devolución Parcial Ecommer como precondicion al reverso no se genero");
				System.out.println("##[warning] : La Devolución Parcial Ecommer como precondicion al reverso no se genero\r\n");
				result = false;
			}

			report.AddLine("Ejecutando Reverso de la Devolución Parcial Ecommer generada previamente");
			System.out.println("Ejecutando Reverso de la Devolución Parcial Ecommer generada previamente\r\n");
			
			//EJECUCION OPI
			res = opiCmd.sshSendCmd(report, "TC_09_OPI.xml", configEntidad);

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
			validaRes = opiAcciones.validacionBD_CM_Autorizacion(report, oraResp, idEstado, disponibleEsperado, impPendCompEsperado, impAjustePendEsperado, ID_CUENTA, configEntidad);
			if (!validaRes) {
				report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
				System.out.println("##[warning] : FAIL<br>No se cumplieron todas las validaciones\r\n");
				result = false;
			} else {
				report.AddLine("Ejecucion Correcta<br>Se cumplieron todas las validaciones");
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
