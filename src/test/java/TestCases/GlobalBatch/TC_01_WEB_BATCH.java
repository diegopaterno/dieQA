package TestCases.GlobalBatch;

import org.openqa.selenium.WebDriver;

import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.Acceso;
import Pasos.Generales.NavSuperiorBatch;
import Repositories.Repo_WebRepository;
import io.restassured.path.json.JsonPath;

public class TC_01_WEB_BATCH {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCH.url_gbatch");
			
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");

			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);

			//ADD THE STEPS BELOW
			
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);
					
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);
					
			// Cerramos la sesion
			acceso.logoutBA(data, report, DM, iteration, name, repo);
	
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");

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