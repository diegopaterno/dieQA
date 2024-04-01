package TestCases.AutorizadorInterno;

import org.openqa.selenium.WebDriver;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.PrePostCondi;
import Repositories.Repo_Variables;
import Tools.apiWorker;
import Tools.dbWorker;
import Tools.opiWorker;
import io.restassured.path.json.JsonPath;
import Pasos.Generales.AutorizadorInterno;

public class TC_14_API_AI{
	WebDriver driver;
	
	// ERROR!! El DE39 del response es: 96 Se esperaba el DE39: 00
	// Consulta de Saldo 
	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
		//Validation var
		boolean result = true;
		try {
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_14_API_AI\r\n");

			//SET VARIABLES
			// Ruta donde se encuentra el archivo con el Body que se envia en el metodo Post
			String path = "./API_Requests/Autorizador/TC_14_ConsultaDeSaldo.txt";
			String token = "";
			String bodyData = "";
			String respBody = "";			
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			String NRO_TARJETA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.nro_tarjeta");
			String NRO_TARJETA_ENCRIPTADA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.nro_tarjeta_encriptada");
			String VENCIMIENTO = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.vencimiento");
			String ID_ESTADO_CUENTA = "0";
			String ID_ESTADO_TARJETA = "0";
			String de39 = "00";
			String idEstado = "0";
			opiWorker opiAcciones = new opiWorker();
			boolean validaRes;
			int rtaRollback = 0;
			PrePostCondi ppCondi = new PrePostCondi();

			//SET INSTANCES
			apiWorker apiResp = new apiWorker();
			dbWorker oraResp = new dbWorker();
			Repo_Variables repoVar = new Repo_Variables();
			AutorizadorInterno AI = new AutorizadorInterno();
			
			//SET ENVIRONMENT
			AI.setBaseUrl(report, repoVar, configEntidad);

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");
						
			//PRECONDICION
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");
			
			AI.setEstadoCuentaTarjeta(report, ID_CUENTA, ID_ESTADO_CUENTA, ID_ESTADO_TARJETA, configEntidad);
			
			rtaRollback = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA\r\n"
					+ "SET SALDO = 10575.6, DISPONIBLE = 10575.6, DISPONIBLE_ADELANTO = 10575.6,\r\n"
					+ "IMPORTE_COMPRAS = 0, IMPORTE_ADELANTOS = 0, IMPORTE_PEND_COMPRAS = 0, \r\n"
					+ "IMPORTE_PEND_ADELANTOS = 0, IMPORTE_AJUSTES = 0, IMPORTE_AJUSTES_PEND = 0,\r\n"
					+ "IMPORTE_PAGOS = 10575.6\r\n"
					+ "WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			ppCondi.preCondicionBD(report, rtaRollback);
						
			//GET TOKEN
			token = AI.getToken(report, apiResp, configEntidad);
			
			//GET POST BODY FROM FILE
			bodyData = AI.getBodyData(report, path);
			
			//Se actualiza el arhivo json y la variable bodyData con el nro de tarjeta del TC
			bodyData = AI.setJsonBody(report, path, bodyData, NRO_TARJETA, VENCIMIENTO);
			
			//POST - Consulta de Saldo -  Local
			if (!bodyData.isEmpty() && !token.isEmpty())
			{
				
				respBody = AI.post(report, repoVar, apiResp, bodyData, token);
				
				//Se obtiene el valor del DE39 del response
				de39 = JsonPath.from(respBody).getString("DE39");
				report.AddLine("Valor del DE39 en el response: " + de39);
				System.out.println("Valor del DE39 en el response: " + de39 + "\r\n");
				
			} else {
				System.out.println("No se obtuvo un token o el body no existe");
				report.AddLineAssertionError("No se obtuvo un token o el body no existe");
				result = false;
			}	
			
			//ADD VALIDATIONS	
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
			
			validaRes = opiAcciones.validacionConsultaSaldoLocal(report, oraResp, idEstado, ID_CUENTA, NRO_TARJETA_ENCRIPTADA, configEntidad);
			
			if (!validaRes) {
				report.AddLine("FAIL:<br>No se cumplieron todas las validaciones");
				System.out.println("FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			} else {
				report.AddLine("Ejecucion Correcta:<br>Se cumplieron todas las validaciones");
				System.out.println("\r\nEjecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
			}
						
			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");
						
		} catch (Exception e) {
			e.printStackTrace();
			report.AddLineAssertionError(e.getStackTrace()[0].toString());
			report.AddLineAssertionError(e.getMessage());
			result = false;
		}

		return result;
	}
	
}
