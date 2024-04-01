package TestCases.ApiPrepaga;

import static org.hamcrest.Matchers.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.WebDriver;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.PrePostCondi;
import Repositories.Repo_Variables;
import Tools.apiWorker;
import Tools.dbWorker;
import Tools.opiWorker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class TC_12_API_PP{
	WebDriver driver;

	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
		//Validation var
		boolean result = true;
		try {
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");

			System.out.println("Configuring TC_12_API_PP\r\n");

			//SET VARIABLES
			String path = "./API_Requests/PrePagas/TC_12_Ingreso_de_Ajuste_Credito.txt";
			String token = "";
			String bodyData = "";
			String respBody = "";
			int comprobanteAjuste = 0;
			String[] rtaCant2 = new String[2];
			String[] rtaCant3 = new String[3];
			boolean res;
			int rta1 = 0;
			int rta2 = 0;
			String saldoEsperado = "1001.5";
			String disponibleEsperado = "1001.5";
			String importeAjusteEsperado = "-1.5";
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_1.cuenta_virtual");
			String ID_PRODUCTO = JsonPath.from(cuentas_generales).get("PARAMETRIA_CUENTAS.id_producto");
			Response response;

			//SET INSTANCES
			apiWorker apiResp = new apiWorker();
			dbWorker oraResp = new dbWorker();
			Repo_Variables repoVar = new Repo_Variables();
			PrePostCondi ppCondi = new PrePostCondi();
			opiWorker validacionAjus = new opiWorker();

			//SET ENVIRONMENT
			//Obtengo los datos de la base url lel archivo configEntidad que contiene todos los accesos a la entidad
			String base_url = JsonPath.from(configEntidad).get("TOKENPREPAGA.base_url");
			repoVar.setBaseUri(base_url);

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PRECONDICION
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");

			rta1 = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA\r\n"
					+ "SET SALDO = 1000, DISPONIBLE = 1000, DISPONIBLE_ADELANTO = 1000,\r\n"
					+ "IMPORTE_COMPRAS = 0, IMPORTE_ADELANTOS = 0, IMPORTE_PEND_COMPRAS = 0, \r\n"
					+ "IMPORTE_PEND_ADELANTOS = 0, IMPORTE_AJUSTES = 0, IMPORTE_AJUSTES_PEND = 0,\r\n"
					+ "IMPORTE_PAGOS = 1000\r\n"
					+ "WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_MONEDA IN (32)", configEntidad);
			ppCondi.preCondicionBD(report, rta1);	

			//GET TOKEN
			try {
				token = apiResp.GetAccessTokenPP(configEntidad);
			} catch (Exception E) {
				report.AddLineAssertionError("No se obtuvo un token.<br>Error: " + E);
				System.out.println("##[warning] : No se obtuvo un token.\r\nError: " + E + "\r\n");
			}

			//GET POST BODY FROM FILE
			try {
				bodyData = new String(Files.readAllBytes(Paths.get(path)));	
				
			//Se muestra el body en el reporte
			report.AddLine("Request body:<br>" + bodyData);
				
			} catch (Exception E) {
				report.AddLineAssertionError("Error al abrir el archivo.<br>Error: " + E);
				System.out.println("##[warning] : Error al abrir el archivo.\r\nError: " + E + "\r\n");
			}		

			//POST - Ingreso de Ajuste Debito
			if (!bodyData.isEmpty() && !token.isEmpty())
			{
				response = apiResp.postMethod(repoVar, "/api/Productos/" + ID_PRODUCTO + "/Cuentas/" + ID_CUENTA + "/", "IngresoDeAjuste", bodyData, token);
			
			//Se reporta el Status Code
			report.AddLine("Status Code: " + String.valueOf(response.getStatusCode()));
			System.out.println("Status Code: " + String.valueOf(response.getStatusCode()) + "\r\n");
				
			//Se valida el status code
			if(response.getStatusCode()!=201) {
					report.AddLineAssertionError("Fallo la validacion del StatusCode<br>Esperado: 201 - Obtenido: " + String.valueOf(response.getStatusCode()));
					System.out.println("##[warning] : Fallo la validacion del StatusCode:\r\nEsperado: 200 - Obtenido: " + String.valueOf(response.getStatusCode()) + "\r\n");
					MatcherAssert.assertThat("Validacion: Status code", String.valueOf(response.getStatusCode()), equalTo("201"));				
				}
			
			//Se obtiene el body del response
			respBody = response.getBody().asPrettyString();
				
			//Se muestra response en el reporte
			report.AddLine("Response: " + respBody);
			comprobanteAjuste = JsonPath.from(respBody).get("Data.ComprobanteAjusteSocio");
			
			//Se reporta el ID de Cuenta
			report.AddLine("Comprobante de Ajuste: " + String.valueOf(comprobanteAjuste));
			System.out.println("Comprobante de Ajuste: " + String.valueOf(comprobanteAjuste) + "\r\n");
			} else {
				System.out.println("##[warning] : No se obtuvo un token");
				report.AddLineAssertionError("No se obtuvo un token\r\n");
				result = false;
			}

			
			//ADD VALIDATIONS	
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
			
			//Validacion Saldo, Disponible e Importe de Ajuste
			rtaCant3 = oraResp.oraThreeQuery(report, "SELECT SALDO, DISPONIBLE,IMPORTE_AJUSTES FROM CUENTAS_MONEDA WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			res = validacionAjus.validacionCM_Saldo_Disponible_Importe_Ajustado(report, rtaCant3, saldoEsperado, disponibleEsperado, importeAjusteEsperado);
			if (!res) {
				//Si falla la validacion el resultado de la prueba es false
				result = false;				
			}

			//Validacion ID_ESTADO y COMPROBANTE AJUSTE
			rtaCant2 = oraResp.oraTwoQuery(report, "SELECT ID_AJUSTE, ID_ESTADO FROM AJUSTES_SOCIOS WHERE ID_AJUSTE = " + String.valueOf(comprobanteAjuste), configEntidad);
			res = validacionAjus.validacionAjusteSocios(report, rtaCant2, String.valueOf(comprobanteAjuste));
			if (!res) {
				//Si falla la validacion el resultado de la prueba es false
				result = false;	
			}

			//ROLLBACK
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			rta1 = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA\r\n"
					+ "SET SALDO = 1000, DISPONIBLE = 1000, DISPONIBLE_ADELANTO = 1000,\r\n"
					+ "IMPORTE_COMPRAS = 0, IMPORTE_ADELANTOS = 0, IMPORTE_PEND_COMPRAS = 0, \r\n"
					+ "IMPORTE_PEND_ADELANTOS = 0, IMPORTE_AJUSTES = 0, IMPORTE_AJUSTES_PEND = 0,\r\n"
					+ "IMPORTE_PAGOS = 1000\r\n"
					+ "WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_MONEDA IN (32)", configEntidad);
			ppCondi.validaRollBackUpdate(report, rta1);
			
			rta2 = oraResp.oraDelete(report, "DELETE AJUSTES_SOCIOS WHERE ID_AJUSTE = " + String.valueOf(comprobanteAjuste), configEntidad);
			ppCondi.validaRollBackDelete(report, rta2);
			
			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");

			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");


		} catch (Exception e) {
			e.printStackTrace();
			report.AddLineAssertionError(e.getStackTrace()[0].toString());
			report.AddLineAssertionError(e.getMessage());
			
            //Se agrega de nuevo ROLLBACK para que no quede la base inconsistente si falla el TestCase  
			
			//SET VARIABLES 
			int rta1 = 0;
			int rta2 = 0;
			int comprobanteAjuste = 0;
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_1.cuenta_virtual");
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();

			//ROLLBACK
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			rta1 = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA\r\n"
					+ "SET SALDO = 1000, DISPONIBLE = 1000, DISPONIBLE_ADELANTO = 1000,\r\n"
					+ "IMPORTE_COMPRAS = 0, IMPORTE_ADELANTOS = 0, IMPORTE_PEND_COMPRAS = 0, \r\n"
					+ "IMPORTE_PEND_ADELANTOS = 0, IMPORTE_AJUSTES = 0, IMPORTE_AJUSTES_PEND = 0,\r\n"
					+ "IMPORTE_PAGOS = 1000\r\n"
					+ "WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_MONEDA IN (32)", configEntidad);
			ppCondi.validaRollBackUpdate(report, rta1);
			
			rta2 = oraResp.oraDelete(report, "DELETE AJUSTES_SOCIOS WHERE ID_AJUSTE = " + String.valueOf(comprobanteAjuste), configEntidad);
			ppCondi.validaRollBackDelete(report, rta2);
			
			result = false;
		}
		//return the test result
		return result;

	}

}
