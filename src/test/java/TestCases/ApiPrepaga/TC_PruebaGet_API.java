package TestCases.ApiPrepaga;

import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.MatcherAssert;
import org.openqa.selenium.WebDriver;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Repositories.Repo_Variables;
import Tools.apiWorker;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
 
public class TC_PruebaGet_API {
	WebDriver driver;

	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad) {
		//Validation var
		boolean result = true;
		try {			
			System.out.println("Configuring");
			
			//SET VARIABLES
			String token = "";
			String respBody = "";
			String telCelu = "";
			String rta = "";
			Response response;
			
			//SET INSTANCES
			apiWorker apiResp = new apiWorker();
			dbWorker oraResp = new dbWorker();
			Repo_Variables repoVar = new Repo_Variables();
			
			//SET ENVIRONMENT
			//Obtengo los datos de la base url lel archivo configEntidad que contiene todos los accesos a la entidad
			String base_url = JsonPath.from(configEntidad).get("TOKENPREPAGA.base_url");
			repoVar.setBaseUri(base_url);
			
			System.out.println("Initializating test");

			//GET TOKEN
			try {
				token = apiResp.GetAccessTokenPP(configEntidad);
			} catch (Exception E) {
				System.out.println("No se obtuvo un token.\r\nError: " + E);
			}

			//GET RESPONSE
			if (!token.isEmpty())
			{
				response = apiResp.getMethod(repoVar, "/api/Productos/1/Cuentas/", "5", token);
				//Se reporta el response completo
				report.AddLine(response.getBody().asPrettyString()
						.replace(",", ",<br>")
						.replace("{", "{<br>")
						.replace("}", "}<br>")
						.replace("[", "[<br>")
						.replace("]", "]<br>"));
				System.out.println(response.getBody().asPrettyString());
				//Se reporta el Status Code
				report.AddLine("Status Code: " + String.valueOf(response.getStatusCode()));
				System.out.println("Status Code: " + String.valueOf(response.getStatusCode()));
				//Se obtiene el body del response
				respBody = response.getBody().asPrettyString();
				
				//Se obtienen datos y se informan
				telCelu = JsonPath.from(respBody).getString("Telefonos.Celular");
				report.AddLine("El celular es: " + telCelu);
				System.out.println("El celular es: " + telCelu);
			} else {
				System.out.println("No se obtuvo un token");
				report.AddLineAssertionError("No se obtuvo un token");
				result = false;
			}
			
			//ADD VALIDATIONS
			//MatcherAssert.assertThat(idCta, equalTo(Integer.valueOf(rtaIdCta)));
			
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
