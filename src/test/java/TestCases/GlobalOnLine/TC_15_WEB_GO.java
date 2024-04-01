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
import Pasos.GlobalOnLine.TC_15_GO_PASOS;

public class TC_15_WEB_GO {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad, String cuentas_generales) {
		//validation 
		boolean result = true;
		try {		
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
			               + "##################################################################################################################################################################################################################\r\n");
			
            System.out.println("Configuring TC_15_WEB_GO\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();
			TC_15_GO_PASOS tc = new TC_15_GO_PASOS();
			dbWorker oraResp = new dbWorker();
		
			
			//SET VARIABLES
			int rta = 0;
			String resp = "";
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			
	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GO = JsonPath.from(configEntidad).get("GO.url_go");
			
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");

			//Se abre el driver con la URL de GO
			driver.get(URL_GO);

			//ADD THE STEPS BELOW
			//LOGIN
			report.AddLine("Acceso a la pagina de GO");
			acceso.loginGlobalOnLine(data, report, DM, iteration, name, repo, configEntidad);

			//NAVEGACION DEL MENU SIDEBAR
			menu.navegacion_lvl3(data, report, DM, iteration, name, repo, "Emisión", "Socios", "Tarjetas");

			//PÁGINA 1
			tc.pagina1(data, report, DM, iteration, name, repo, ID_CUENTA);

			//PÁGINA 2
			tc.pagina2(data, report, DM, iteration, name, repo);

			//ADD VALIDATIONS AT THE END
			tc.validar(data, report, DM, iteration, name, repo);
			
			// VALIDACION EN EL BACKEND					
			report.AddLine("Se busca el estado de tarjetas.");
			System.out.println("Se busca el estado de tarjetas. \r\n");
			
			resp = oraResp.oraOneQuery(report, "SELECT ID_ESTADO FROM TARJETAS WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			
			if (resp.equals("0")) {
				report.AddLine("Se valida que se haya habilitado la tarjeta fisica con exito");
				System.out.println("##[section] : Se valida que se haya habilitado la tarjeta fisica con exito \r\n");
				
			} else {
				report.AddLineAssertionError("Error!!. El estado deberia ser 0 ACTIVA pero es igual a : "+resp);
				System.out.println("##[warning] : Error!!. El estado deberia ser 0 ACTIVA pero es igual a : " +resp+ "\r\n");
				result = false;
			}
					
			//ROLLBACK
			rta = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 3 WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			
			if (rta > 0) {
				report.AddLine("Rollback exitoso! El estado de la tarjeta quedo en 3 EMBOZADO");
				System.out.println("##[section] : Rollback exitoso! El estado de la tarjeta quedo en 3 EMBOZADO \r\n");
			}	else {
				report.AddLineAssertionError("Error. No se logro realizar el update en tarjetas ");
				System.out.println("##[warning] : Error. No se logro realizar el update en tarjetas \r\n");
				result = false;
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
			int rta = 0;
			dbWorker oraResp = new dbWorker();
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			
			
			//ROLLBACK
			rta = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 3 WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			
			if (rta > 0) {
				report.AddLine("Rollback exitoso! El estado de la tarjeta quedo en 3 EMBOZADO");
				System.out.println("##[section] : Rollback exitoso! El estado de la tarjeta quedo en 3 EMBOZADO \r\n");
			}	else {
				report.AddLineAssertionError("Error. No se logro realizar el update en tarjetas ");
				System.out.println("##[warning] : Error. No se logro realizar el update en tarjetas \r\n");
				result = false;
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