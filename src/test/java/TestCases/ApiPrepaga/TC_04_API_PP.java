package TestCases.ApiPrepaga;

import java.nio.file.Files;
import java.nio.file.Paths;
import static org.hamcrest.Matchers.*;
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

public class TC_04_API_PP {
	WebDriver driver;

	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
		//Validation var
		boolean result = true;
		try {		
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_04_API_PP\r\n");

			//SET VARIABLES
			String path = "./API_Requests/PrePagas/TC_04_Crear_Carga_Local.txt";
			String token = "";
			String bodyData = "";
			String respBody = "";
			String rtaIdPago = "";
			int idPago = 0;
			int rta;
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			String ID_PRODUCTO = JsonPath.from(cuentas_generales).get("PARAMETRIA_CUENTAS.id_producto");
			String saldo = "10000";
			String disponible = "10000";
			String disponibleAdelanto = "10000";
			String importePagos = "10000";
			Response response;
			boolean res;

			//SET INSTANCES
			apiWorker apiResp = new apiWorker();
			dbWorker oraResp = new dbWorker();
			Repo_Variables repoVar = new Repo_Variables();
			PrePostCondi ppCondi = new PrePostCondi();
			opiWorker preCondiCM = new opiWorker();
			opiWorker validacionPago = new opiWorker();

			//SET ENVIRONMENT
			//Obtengo los datos de la base url lel archivo configEntidad que contiene todos los accesos a la entidad
			String base_url = JsonPath.from(configEntidad).get("TOKENPREPAGA.base_url");
			repoVar.setBaseUri(base_url);

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");
			
			//PRECONDICION
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");
			
			//Seteo la cuenta y la tarjeta en estado 0-Activa para que no falle el TC
			rta = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			ppCondi.preCondicionBD(report, rta);
			rta = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 3 WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			ppCondi.preCondicionBD(report, rta);
			
			preCondiCM.preCondicionCM_en_cero(oraResp, ppCondi, report, ID_CUENTA, configEntidad);
			
			//GET TOKEN
			//La configuracion de los datos está dentro del método
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

			//POST - Crear Carga Local
			if (!bodyData.isEmpty() && !token.isEmpty())
			{
				response = apiResp.postMethod(repoVar, "/api/Productos/" + ID_PRODUCTO + "/Cuentas/" + ID_CUENTA + "/Transacciones/","Cargas", bodyData, token);
			
			//Se reporta el Status Code
				report.AddLine("Status Code: " + String.valueOf(response.getStatusCode()));
				System.out.println("Status Code: " + String.valueOf(response.getStatusCode()) + "\r\n");
			
			//Se valida el status Code
			if(response.getStatusCode()!=201) {
				report.AddLineAssertionError("Fallo la validacion del StatusCode<br>Esperado: 201 - Obtenido: " + String.valueOf(response.getStatusCode()));
				System.out.println("##[warning] : Fallo la validacion del StatusCode:\r\nEsperado: 200 - Obtenido: " + String.valueOf(response.getStatusCode()) + "\r\n");
				MatcherAssert.assertThat("Validacion: Status code", String.valueOf(response.getStatusCode()), equalTo("201"));				
				}
				
			//Se obtiene el body del response
				respBody = response.getBody().asPrettyString();
			
			//Se muestra response en el reporte
				report.AddLine("Response: " + respBody);
				idPago = JsonPath.from(respBody).get("Data.CodigoConfirmacion");

			//Se reporta el ID de Cuenta
				report.AddLine("ID_PAGO del response: " + String.valueOf(idPago));
				System.out.println("ID_PAGO del response: " + String.valueOf(idPago) + "\r\n");
			}

			//DATABASE - VERIFICA ALTA
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
			
			if (idPago != 0) 
			{
				rtaIdPago = oraResp.oraOneQuery(report, "SELECT ID_PAGO FROM PAGOS WHERE ID_PAGO = " + String.valueOf(idPago), configEntidad);
				report.AddLine("ID_PAGO en la base de datos: " + rtaIdPago);
				System.out.println("ID_PAGO en la base de datos: " + rtaIdPago + "\r\n");
			} else {
				report.AddLineAssertionError("Error:<br>No se obtuvo el ID_PAGO");
				System.out.println("##[warning] : Error:<br>No se obtuvo el ID_PAGO\r\n");
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			}

			//ADD VALIDATIONS
			if (!rtaIdPago.equals(String.valueOf(idPago))) {
			
			//rollBack(report, oraResp, ppCondi, rtaIdPago);
				report.AddLineAssertionError("Error:<br>El ID_PAGO generado por la API Crear_Carga_Local es: " + idPago + " es distinto al de la base de datos: " + rtaIdPago);
				System.out.println("##[warning] : Error:<br>El ID_PAGO generado por la API Crear_Carga_Local es: " + idPago + " es distinto al de la base de datos: " + rtaIdPago);
			
			//Si falla la validacion el resultado de la prueba es false
				result = false;
			} else {
				report.AddLine("Validacion exitosa:<br>El ID_PAGO generado por la API Crear Carga Local es: " + idPago + " es igual al de la base de datos: " + rtaIdPago);
				System.out.println("##[section] : Validacion exitosa:\r\nEl ID_PAGO generado por la API Crear Carga Local es: " + idPago + " es igual al de la base de datos: " + rtaIdPago + "\r\n");				
			}
			
			res = validacionPago.validacionBD_CM_Pagos(report, oraResp, saldo, disponible, disponibleAdelanto, importePagos, ID_CUENTA, configEntidad);
			
			if (!res) {			
			//Si falla la validacion el resultado de la prueba es false
				result = false;
			}
			
			//ROLLBACK
			//rollBack(report, oraResp, ppCondi, rtaIdPago);
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
			rta = oraResp.oraDelete(report, "DELETE PAGOS WHERE ID_PAGO = " + rtaIdPago, configEntidad);
			ppCondi.validaRollBackDelete(report, rta);
			
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
			int rta;
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			String rtaIdPago = "";
			
			//ROLLBACK
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
			rta = oraResp.oraDelete(report, "DELETE PAGOS WHERE ID_PAGO = " + rtaIdPago, configEntidad);
			ppCondi.validaRollBackDelete(report, rta);
			
			
			result = false;
		}

		return result;
	}

}
