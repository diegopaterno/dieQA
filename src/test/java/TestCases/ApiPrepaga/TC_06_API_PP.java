package TestCases.ApiPrepaga;

import static org.hamcrest.Matchers.equalTo;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.WebDriver;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.PrePostCondi;
import Repositories.Repo_Variables;
import Tools.apiWorker;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
 
public class TC_06_API_PP{
	WebDriver driver;

	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
		//Validation var
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_06_API_PP\r\n");
			
			//SET VARIABLES
			String token = "";
			String respBody = "";
			float saldoPesos = 0;
			String sSaldoPesos = "";
			String resp = "";
			int rta;
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			String ID_PRODUCTO = JsonPath.from(cuentas_generales).get("PARAMETRIA_CUENTAS.id_producto");
			Response response;
			
			//SET INSTANCES
			apiWorker apiResp = new apiWorker();
			dbWorker oraResp = new dbWorker();
			Repo_Variables repoVar = new Repo_Variables();
			PrePostCondi ppCondi = new PrePostCondi();
			
			//SET ENVIRONMENT
			//Obtengo los datos de la base url del archivo configEntidad que contiene todos los accesos a la entidad
			String base_url = JsonPath.from(configEntidad).get("TOKENPREPAGA.base_url");
			repoVar.setBaseUri(base_url);
			
			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");
			
			//PRECONDICION
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");
			rta = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA SET SALDO = 1000, DISPONIBLE = 1000, DISPONIBLE_ADELANTO = 1000 WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			ppCondi.preCondicionBD(report, rta);
			
			//GET TOKEN
			try {
				token = apiResp.GetAccessTokenPP(configEntidad);
			} catch (Exception E) {
				report.AddLineAssertionError("No se obtuvo un token.<br>Error: " + E);
				System.out.println("##[warning] : No se obtuvo un token.\r\nError: " + E + "\r\n");
			}

			//GET - Consulta de Saldo Local
			if (!token.isEmpty())
			{
				response = apiResp.getMethod(repoVar, "/api/Productos/" + ID_PRODUCTO + "/Cuentas/" + ID_CUENTA + "/", "Saldo", token);
			
			//Se reporta el Status Code
				report.AddLine("Status Code: " + String.valueOf(response.getStatusCode()));
				System.out.println("Status Code: " + String.valueOf(response.getStatusCode()));
				
			//Se valida el status code
			if(response.getStatusCode()!=200) {
				report.AddLineAssertionError("Fallo la validacion del StatusCode<br>Esperado: 200 - Obtenido: " + String.valueOf(response.getStatusCode()));
				System.out.println("##[warning] : Fallo la validacion del StatusCode:\r\nEsperado: 200 - Obtenido: " + String.valueOf(response.getStatusCode()) + "\r\n");
				MatcherAssert.assertThat("Validacion: Status code", String.valueOf(response.getStatusCode()), equalTo("200"));				
				}
				
			//Se obtiene el body del response
			respBody = response.getBody().asPrettyString();
				
			//Se muestra response en el reporte
			report.AddLine("Response: " + respBody);
				
			//Se obtienen datos y se informan
			saldoPesos = JsonPath.from(respBody).get("Data.SaldoPesos");
			report.AddLine("El saldo en pesos es: " + saldoPesos);
			System.out.println("El saldo en pesos es: " + saldoPesos + "\r\n");
			} else {
				System.out.println("##[warning] : No se obtuvo un token\r\n");
				report.AddLineAssertionError("No se obtuvo un token");
				result = false;
			}
						
			sSaldoPesos = String.valueOf(saldoPesos).replace(".0", "");
			report.AddLine("Saldo en pesos ajustado para verificar en la BD: " + sSaldoPesos);
			System.out.println("Saldo en pesos ajustado para verificar en la BD: " + sSaldoPesos + "\r\n");
			
			//ADD VALIDATIONS
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
			
			resp = oraResp.oraOneQuery(report, "SELECT SALDO FROM CUENTAS_MONEDA WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			
			if(!resp.equals(sSaldoPesos)) {
				report.AddLineAssertionError("Error: El saldo esperado es: " + sSaldoPesos + " Saldo encontrado en la base de datos: " + resp);
				System.out.println("##[warning] : Error: El saldo esperado es: " + sSaldoPesos + " Saldo encontrado en la base de datos: " + resp + "\r\n");
			
			//Si falla la validacion el resultado de la prueba es false
				result = false;
			} else {
				report.AddLine("Validacion exitosa:<br>El saldo en pesos es: " + sSaldoPesos + " y es igual al de la base de datos: " + resp);
				System.out.println("##[section] : Validacion exitosa:\r\nEl saldo en pesos es: " + sSaldoPesos + " y es igual al de la base de datos: " + resp + "\r\n");
			}
			
			
			//ROLLBACK
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
			rta = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA SET SALDO = 1000, DISPONIBLE = 1000, DISPONIBLE_ADELANTO = 1000 WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			ppCondi.postCondicionBD(report, rta);
			
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
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			
			//ROLLBACK
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
			rta = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA SET SALDO = 1000, DISPONIBLE = 1000, DISPONIBLE_ADELANTO = 1000 WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			ppCondi.postCondicionBD(report, rta);
			
			result = false;
		}
		//return the test result
		return result;

	}
	
}
