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
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class TC_22_API_PP{
	WebDriver driver;

	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
		//Validation var
		boolean result = true;
		try {
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_22_API_PP\r\n");

			//SET VARIABLES
			String path = "./API_Requests/PrePagas/TC_22_Cambio_PIN_Sin_verificacion.txt";
			String token = "";
			String bodyData = "";
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			String REFERENCIA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.referencia");
			String ID_PRODUCTO = JsonPath.from(cuentas_generales).get("PARAMETRIA_CUENTAS.id_producto");
			Response response;

			//SET INSTANCES
			apiWorker apiResp = new apiWorker();
			Repo_Variables repoVar = new Repo_Variables();

			//SET ENVIRONMENT
			//Obtengo los datos de la base url lel archivo configEntidad que contiene todos los accesos a la entidad
			String base_url = JsonPath.from(configEntidad).get("TOKENPREPAGA.base_url");
			repoVar.setBaseUri(base_url);

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");
			
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
				report.AddLine("Error al abrir el archivo.<br>Error: " + E);
				System.out.println("##[warning] : Error al abrir el archivo.\r\nError: " + E + "\r\n");
			}		

			//POST - Cambio de PIN sin verificacion
			if (!bodyData.isEmpty() && !token.isEmpty())
			{
				response = apiResp.postMethod(repoVar, "/Api/Productos/" + ID_PRODUCTO + "/Cuentas/" + ID_CUENTA + "/Tarjetas/" + REFERENCIA + "/", "Pin", bodyData, token);
				//Se reporta el Status Code
				report.AddLine("Status Code: " + String.valueOf(response.getStatusCode()) + " - El PIN se actualizo correctamente");
				System.out.println("\r\n##[section]: Status Code: " + String.valueOf(response.getStatusCode()) + " - El PIN se actualizo correctamente\r\n");
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
		//return the test result
		return result;

	}
}
