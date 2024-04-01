package TestCases.GlobalOnLine;

import org.openqa.selenium.WebDriver;
import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Repositories.Repo_WebRepository;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;
import Pasos.Generales.Acceso;
import Pasos.Generales.NavLateral;
import Pasos.GlobalOnLine.TC_03_GO_PASOS;

public class TC_03_WEB_GO {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {		
			
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
               + "##################################################################################################################################################################################################################\r\n");		
			
			System.out.println("Configuring TC_03_WEB_GO\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();
			TC_03_GO_PASOS tc = new TC_03_GO_PASOS();
			dbWorker oraResp = new dbWorker();
		

			//SET VARIABLES
			int rta1 = 0;
			int rta2 = 0;
			String res = "";
			String MDR_desc  = "Modelo de restriccion";
			
			
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GO = JsonPath.from(configEntidad).get("GO.url_go");
			
			
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			
			//LOGIN
			//Se abre el driver con la URL de GO
			driver.get(URL_GO);

			acceso.loginGlobalOnLine(data, report, DM, iteration, name, repo,configEntidad);

			//NAVEGACION DEL MENU SIDEBAR
			menu.navegacion_lvl4(data, report, DM, iteration, name, repo, "Emisión", "Socios", "Parametría", "Modelos de Restricciones");

			//PAGINA 1
			tc.pagina1(data, report, DM, iteration, name, repo);

			//PAGINA 2
			tc.pagina2(data, report, DM, iteration, name, repo, MDR_desc);

			//PAGINA DE VALIDACION
			tc.pagina3(data, report, DM, iteration, name, repo);

			// Esperamos a que se grabe el registro en la base de datos
			Thread.sleep(3000);
			
			
			// VALIDACION BACKEND			
			res = oraResp.oraOneQuery(report, "select id_mod_restriccion from modelos_restriccion where descripcion = '" + MDR_desc + "'", configEntidad);
				
			if (res.equals("")) {
				report.AddLineAssertionError("Error. Modelo de restriccion no encontrado.");
				System.out.println("##[warning] : Error. Modelo de restriccion no encontrado. \r\n");
				result = false;
			} else {
				report.AddLine("Validacion de Modelo de restriccion encontrado.");
				System.out.println("##[section] : Validacion de Modelo de restriccion encontrado. \r\n");
			}
			
			
			//ROLLBACK
			rta1 = oraResp.oraDelete(report, "delete modelos_restric_ramos where id_mod_restriccion = (select id_mod_restriccion from modelos_restriccion where descripcion = '" + MDR_desc + "')",configEntidad);
			rta2 = oraResp.oraDelete(report, "delete modelos_restriccion where descripcion = '" + MDR_desc + "'", configEntidad);
			
			
			if (rta1 > 0 && rta2 > 0) {
				report.AddLine("Borrado exitoso del Modelo de restriccion");
				System.out.println("##[section] : Borrado exitoso del Modelo de restriccion \r\n");
				
			} else {
				report.AddLine("Error. No se logró realizar el rollback");
				System.out.println("##[warning] : Error. No se logró realizar el rollback \r\n");
			}
		
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
		
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
              + "##################################################################################################################################################################################################################\r\n");		
		
		} catch(Exception e) {
			e.printStackTrace();
			report.AddLineAssertionError(e.getMessage());
			
			
             //Se agrega de nuevo ROLLBACK para que no quede la base inconsistente si falla el TestCase  
			
			//SET VARIABLES 
			int rta1 = 0;
			int rta2 = 0;
			dbWorker oraResp = new dbWorker();
			String MDR_desc  = "Modelo de restriccion";
			
			
			//ROLLBACK
			rta1 = oraResp.oraDelete(report, "delete modelos_restric_ramos where id_mod_restriccion = (select id_mod_restriccion from modelos_restriccion where descripcion = '" + MDR_desc + "')",configEntidad);
			rta2 = oraResp.oraDelete(report, "delete modelos_restriccion where descripcion = '" + MDR_desc + "'", configEntidad);		
			
			if (rta1 > 0 && rta2 > 0) {
				report.AddLine("Borrado exitoso del Modelo de restriccion");
				System.out.println("##[section] : Borrado exitoso del Modelo de restriccion \r\n");
				
			} else {
				report.AddLine("Error. No se logró realizar el rollback");
				System.out.println("##[warning] : Error. No se logró realizar el rollback \r\n");
			}	
			
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
}