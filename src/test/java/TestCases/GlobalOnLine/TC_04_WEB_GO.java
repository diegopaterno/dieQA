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
import Pasos.GlobalOnLine.TC_04_GO_PASOS;

public class TC_04_WEB_GO {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
               + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_04_WEB_GO\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();
			TC_04_GO_PASOS tc = new TC_04_GO_PASOS();
			dbWorker oraResp = new dbWorker();
	

			//SET VARIABLES
			String GDA_desc = "Test grupo de afinidad";
			int rta1 = 0;
			String res = "";
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GO = JsonPath.from(configEntidad).get("GO.url_go");	

			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");

			//Se abre el driver con la URL de GO
			driver.get(URL_GO);

			//ADD THE STEPS BELOW
			//ASEGURARSE QUE EL PRODUCTO EXISTE Y ESTA "ACTIVO".
			//LOGIN
			report.AddLine("Acceso a la pagina de Global Online con las credenciales");
			acceso.loginGlobalOnLine(data, report, DM, iteration, name, repo, configEntidad);

			//NAVEGACION DEL MENU SIDEBAR
			menu.navegacion_lvl4(data, report, DM, iteration, name, repo, "Emisión", "Socios", "Parametría", "Grupos de Afinidad");

			//PAGINA 1
			tc.pagina1(data, report, DM, iteration, name, repo);

			//PAGINA 2
			tc.pagina2(data, report, DM, iteration, name, repo, GDA_desc);

			//ADD VALIDATIONS AT THE END
			//PAGINA DE VALIDACION
			tc.pagina3(data, report, DM, iteration, name, repo);
			
			// Esperamos a que se grabe el registro en la base de datos
			Thread.sleep(3000);
			
			
			// VALIDACION BACKEND			
			res = oraResp.oraOneQuery(report, "select ID_GRUPO_AFINIDAD from grupos_afinidad where descripcion = '" + GDA_desc + "'", configEntidad);
			
			if (res.equals("")) {
				report.AddLineAssertionError("Error. grupos_afinidad no encontrado.");
				System.out.println("##[warning] : Error. grupos_afinidad no encontrado. \r\n");
				result = false;
				
			} else {
				report.AddLine("grupos_afinidad encontrado. Se realiza el borrado");
				System.out.println("##[section] : grupos_afinidad encontrado. Se realiza el borrado \r\n");
			}
			
			
			//ROLLBACK	
			rta1 = oraResp.oraDelete(report, "delete grupos_afinidad where descripcion = '" + GDA_desc + "'", configEntidad);
			
			if (rta1 > 0) {
				report.AddLine("Borrado exitoso de grupos_afinidad");
				System.out.println("##[section] : Borrado exitoso de grupos_afinidad \r\n");
				
			} else {
				report.AddLine("ERROR: NO se logró borrar el grupos_afinidad");
				System.out.println("##[warning] : ERROR: NO se logró borrar el grupos_afinidad \r\n");
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
			dbWorker oraResp = new dbWorker();
			String GDA_desc = "Test grupo de afinidad";
			
			//ROLLBACK	
			rta1 = oraResp.oraDelete(report, "delete grupos_afinidad where descripcion = '" + GDA_desc + "'", configEntidad);
			
			if (rta1 > 0) {
				report.AddLine("Borrado exitoso de grupos_afinidad");
				System.out.println("##[section] : Borrado exitoso de grupos_afinidad \r\n");
				
			} else {
				report.AddLine("ERROR: NO se logró borrar el grupos_afinidad");
				System.out.println("##[warning] : ERROR: NO se logró borrar el grupos_afinidad \r\n");
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

	private void WaitTime(int i) {
		try
		{
			Thread.sleep(i);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	} 
}