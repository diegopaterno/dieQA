package TestCases.ApiPrepaga;

import static org.hamcrest.Matchers.*;
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
 
public class TC_23_API_PP{
	WebDriver driver;

	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
		//Validation var
		boolean result = true;
		try {
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_23_API_PP\r\n");
			
			//SET VARIABLES
			String token = "";
			String resp = "";
			int rta = 0;
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			String REFERENCIA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.referencia");
			String ID_PRODUCTO = JsonPath.from(cuentas_generales).get("PARAMETRIA_CUENTAS.id_producto");
			Response response;
			
			//SET INSTANCES
			apiWorker apiResp = new apiWorker();
			dbWorker oraResp = new dbWorker();
			Repo_Variables repoVar = new Repo_Variables();
			PrePostCondi ppCondi = new PrePostCondi();
			
			//SET ENVIRONMENT
			//Obtengo los datos de la base url lel archivo configEntidad que contiene todos los accesos a la entidad
			String base_url = JsonPath.from(configEntidad).get("TOKENPREPAGA.base_url");
			repoVar.setBaseUri(base_url);
			
			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");
			
			//PRECONDICION
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");
			
			rta = oraResp.oraUpdate(report, "UPDATE TARJETAS SET PIN_INVALIDOS = 3 WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			ppCondi.preCondicionBD(report, rta);

			//GET TOKEN
			try {
				token = apiResp.GetAccessTokenPP(configEntidad);
			} catch (Exception E) {
				report.AddLineAssertionError("No se obtuvo un token.<br>Error: " + E);
				System.out.println("##[warning] : No se obtuvo un token.\r\nError: " + E + "\r\n");
			}
			
			//PUT - Cambio de estado Tarjeta - NormalHabilitada
			if (!token.isEmpty())
			{
				response = apiResp.putMethod(repoVar, "/Api/Productos/" + ID_PRODUCTO + "/Cuentas/" + ID_CUENTA + "/Tarjetas/" + REFERENCIA + "/Pin/", "BlanquearReIntentos", token);
				//Se reporta el Status Code
				report.AddLine("Status Code: " + String.valueOf(response.getStatusCode()));
				System.out.println("Status Code: " + String.valueOf(response.getStatusCode()) + "\r\n");
				//Se valida el status code
				if(response.getStatusCode()!=200) {
					report.AddLineAssertionError("Fallo la validacion del StatusCode<br>Esperado: 200 - Obtenido: " + String.valueOf(response.getStatusCode()));
					System.out.println("##[warning] : Fallo la validacion del StatusCode:\r\nEsperado: 200 - Obtenido: " + String.valueOf(response.getStatusCode()) + "\r\n");
					MatcherAssert.assertThat("Validacion: Status code", String.valueOf(response.getStatusCode()), equalTo("200"));				
				}
			} else {
				System.out.println("##[warning] : No se obtuvo un token\r\n");
				report.AddLineAssertionError("No se obtuvo un token");
				result = false;
			}
					
			//ADD VALIDATIONS
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
			
			resp = oraResp.oraOneQuery(report, "SELECT PIN_INVALIDOS FROM TARJETAS WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			
			if(!resp.equals("0")) {
				report.AddLineAssertionError("Error: El PIN INVALIDO es: " + resp + "; se esperaba el estado: 0");
				System.out.println("##[warning] : Error: El PIN INVALIDO es: " + resp + "; se esperaba el estado: 0\r\n");
				//Si falla la validacion el resultado de la prueba es false
				result = false;	
			} else {
				report.AddLine("Validacion exitosa:<br>El campo PIN_INVALIDO es: " + resp + " - Se blanqueo correctamente los intentos fallidos de PIN");
				System.out.println("##[section] : Validacion exitosa: El campo PIN_INVALIDO es: " + resp + " - Se blanqueo correctamente los intentos fallidos de PIN\r\n");
			}
			
			//ROLLBACK
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
			
			rta = oraResp.oraUpdate(report, "UPDATE TARJETAS SET PIN_INVALIDOS = 0 WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			ppCondi.validaRollBackUpdate(report, rta);
			
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
			int rta = 0;
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			
			//ROLLBACK
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
			
			rta = oraResp.oraUpdate(report, "UPDATE TARJETAS SET PIN_INVALIDOS = 0 WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			ppCondi.validaRollBackUpdate(report, rta);
			
			result = false;
		}
		//return the test result
		return result;

	}
	
}
