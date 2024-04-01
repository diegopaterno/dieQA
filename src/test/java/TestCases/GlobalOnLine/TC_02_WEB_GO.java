package TestCases.GlobalOnLine;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;
import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Repositories.Repo_WebRepository;
import Pasos.Generales.Acceso;
import Pasos.Generales.NavLateral;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;
import Pasos.GlobalOnLine.TC_02_GO_PASOS;

public class TC_02_WEB_GO {
	WebDriver driver;

	public boolean Test(Datasources data, Reports report, DriverManager DM, int iteration, String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {
			
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
			               + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_02_WEB_GO\r\n");
			
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();
			TC_02_GO_PASOS tc = new TC_02_GO_PASOS();
			dbWorker oraResp = new dbWorker();
			
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");

			//SET VARIABLES
			int rta = 0;
			String res = "";	
			String MDL_desc = "Modelo de limite para pruebas automaticas";
			
			
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GO = JsonPath.from(configEntidad).get("GO.url_go");
			
			//Se abre el driver con la URL de GO
			driver.get(URL_GO);

			//ADD THE STEPS BELOW
			//LOGIN
			
			acceso.loginGlobalOnLine(data, report, DM, iteration, name, repo, configEntidad);
			
			//NAVEGACION DEL MENU SIDEBAR
			menu.navegacion_lvl4(data, report, DM, iteration, name, repo, "Emisión", "Socios", "Parametría", "Modelos de Límites");

			//PAGINA 1
			tc.pagina1(data, report, DM, iteration, name, repo);

			//PAGINA 2
			tc.pagina2(data, report, DM, iteration, name, repo, MDL_desc);

			//ADD VALIDATIONS AT THE END
			//PAGINA DE VALIDACION
			tc.pagina3(data, report, DM, iteration, name, repo, MDL_desc);
			
			//VALIDACION BACKEND
			
			res = oraResp.oraOneQuery(report, "select id_modelo_limite from modelos_limite where descripcion = '" + MDL_desc + "'",configEntidad);
			
			if (res.equals("")) {
			
				report.AddLineAssertionError("Error. Modelo de limite no encontrado.");
				System.out.println("##[warning] : Error. Modelo de limite no encontrado. \r\n");
				result = false;
				
			} else {
				report.AddLine("Modelo de limite encontrado. Se realiza el borrado");
				System.out.println("##[section] : Modelo de limite encontrado. Se realiza el borrado \r\n");
				
			}			
						
			//ROLLBACK
			rta = oraResp.oraDelete(report, "delete modelos_limite where descripcion = '" + MDL_desc + "'", configEntidad);
			
			if (rta > 0) {
				report.AddLine("Borrado exitoso del Modelo de limite");
				System.out.println("##[section] : Borrado exitoso del Modelo de limite \r\n");
			}else {
				report.AddLine("Error!! Al realizar el borrado");
				System.out.println("##[warning] : Error!! Al realizar el borrado \r\n");
				
			}
			
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
				
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
			              + "##################################################################################################################################################################################################################\r\n");


		} catch(Exception e) {
			e.printStackTrace();
			report.AddLineAssertionError(e.getMessage());	
			
			
			//Se agrega de nuevo ROLLBACK para que no quede la base inconsistente si falla el TestCase  
			
			int rta = 0;
			dbWorker oraResp = new dbWorker();
			String MDL_desc = "Modelo de limite para pruebas automaticas";
			
			
			//ROLLBACK
			rta = oraResp.oraDelete(report, "delete modelos_limite where descripcion = '" + MDL_desc + "'", configEntidad);
			
			if (rta > 0) {
				report.AddLine("Borrado exitoso del Modelo de limite");
				System.out.println("##[section] : Borrado exitoso del Modelo de limite \r\n");
			}else {
				report.AddLine("Error!! Al realizar el borrado");
				System.out.println("##[warning] : Error!! Al realizar el borrado \r\n");
				
			}	
			result = false;
		}
			
		//Try to erase the driver
			
		try {
			driver.quit();
		} catch(Exception e2) {
			
		//Return the test result
			
		}
		return result;
	}
}
