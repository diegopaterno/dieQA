package TestCases.GlobalBackend;

import org.openqa.selenium.WebDriver;

import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.Acceso;
import Pasos.Generales.NavLateral;
import Pasos.Generales.NavSuperior;
import Repositories.Repo_WebRepository;
import io.restassured.path.json.JsonPath;

public class TC_01_WEB_GBACKEND {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {		
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_01_WEB_GBACKEND\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBACKEND = JsonPath.from(configEntidad).get("GBACKEND.url_gbackend");

			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");

			//SET THE URL
			driver.get(URL_GBACKEND);

			//ADD THE STEPS BELOW
			// LOGIN
			report.AddLine("Acceso a la pagina de Global Backend");
			System.out.println("Acceso a la pagina de Global Backend\r\n");
			acceso.loginUsernameGBackEnd(data, report, DM, iteration, name, repo, configEntidad);

			// NAVEGACION DEL MENU SIDEBAR
			menu.navegacion_lvl2(data, report, DM, iteration, name, repo, "Parametría", " Cotización");

			//ADD VALIDATIONS AT THE END
			
			if(report.validateObjectIsDisplayable(repo.get_obj_LogoGP())) {
				report.AddLine("Se ingreso correctamente a la web Global Backend. Se visualiza el logotipo de GP");
				System.out.println("##[section] : Se ingreso correctamente a la web Global Backend. Se visualiza el logotipo de GP\r\n");
			}
			else {
				report.AddLineAssertionError("Error: No se visualiza el logotipo de GP");
				System.out.println("##[warning] : Error: No se visualiza el logotipo de GP\r\n");
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			};
			
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
			              + "##################################################################################################################################################################################################################\r\n");


		} catch(Exception e) {
			e.printStackTrace();
			report.AddLineAssertionError(e.getMessage());			
			result = false;
		}
		//Try to erase the driver
		try {
			driver.quit();
		} catch(Exception e2) {
			//return the test result
		}
		return result;
	}

	private void WaitTime(int i) {
		try
		{
			Thread.sleep(i);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	} 
}