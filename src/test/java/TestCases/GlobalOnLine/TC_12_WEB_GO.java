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
import Pasos.Generales.PrePostCondi;
import Pasos.GlobalOnLine.TC_12_GO_PASOS;

public class TC_12_WEB_GO {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad, String cuentas_generales) {
		//validation 
		boolean result = true;
		try {	
			
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
			               + "##################################################################################################################################################################################################################\r\n");
			
            System.out.println("Configuring TC_12_WEB_GO\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();
			TC_12_GO_PASOS tc = new TC_12_GO_PASOS();
			dbWorker oraResp = new dbWorker();		
			PrePostCondi ppCondi = new PrePostCondi();
			PrePostCondi rollback = new PrePostCondi();

			//SET VARIABLES
			int rta = 0;
			String resp = "";
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
	
					
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GO = JsonPath.from(configEntidad).get("GO.url_go");
			
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");

			//Se abre el driver con la URL de GO
			driver.get(URL_GO);	
			
			// PRECONDICION
		    rta = oraResp.oraUpdate(report, "update tarjetas set id_estado = 0, SOPORTE_FISICO = 1 where id_cuenta =" + ID_CUENTA, configEntidad);
						
		    ppCondi.preCondicionBD(report, rta);

			//ADD THE STEPS BELOW
			//LOGIN
			report.AddLine("Acceso a la pagina de Global Online");
			acceso.loginGlobalOnLine(data, report, DM, iteration, name, repo, configEntidad);

			//NAVEGACION DEL MENU SIDEBAR
			menu.navegacion_lvl3(data, report, DM, iteration, name, repo, "Emisión", "Socios", "Tarjetas");

			//PÁGINA 1
			tc.pagina1(data, report, DM, iteration, name, repo, ID_CUENTA);

			//PÁGINA 2
			tc.pagina2(data, report, DM, iteration, name, repo);

			//ADD VALIDATIONS AT THE END
			tc.validar(data, report, DM, iteration, name, repo);
					
			// Esperamos a que se grabe el registro en la base de datos
			Thread.sleep(3000);
						
			// VALIDACION EN EL BACKEND					
			report.AddLine("Se busca el estado de tarjeta");
			System.out.println("Se busca el estado de tarjeta \r\n");
			
			resp = oraResp.oraOneQuery(report, "select id_estado from tarjetas where id_cuenta =" + ID_CUENTA, configEntidad);

			if (resp.equals("1")) {
				report.AddLine("Se valida de forma exitosa que el estado de la tarjeta este Dada de Baja");
				System.out.println("##[section] : Se valida de forma exitosa que el estado de la tarjeta este Dada de Baja \r\n");
				
			} else {
				report.AddLineAssertionError("Error!!. El estado de la tarjeta deberia ser 1 BAJA pero es igual a: "+resp);
				System.out.println("##[warning] : Error!!. El estado de la tarjeta deberia ser 1 BAJA pero es igual a:" +resp+ "\r\n");
				result = false;
			}		
		
			
			//ROLLBACK
			rta = oraResp.oraUpdate(report, "update tarjetas set id_estado = 3, SOPORTE_FISICO = 0 where id_cuenta =" + ID_CUENTA, configEntidad);
			
			rollback.validaRollBackUpdate(report, rta);
			
            System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");		
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
              + "##################################################################################################################################################################################################################\r\n");

		} catch(Exception e){
			e.printStackTrace();
			report.AddLineAssertionError(e.getMessage());	
					
            //Se agrega de nuevo ROLLBACK para que no quede la base inconsistente si falla el TestCase  
			
			//SET VARIABLES 
			int rta = 0;
			dbWorker oraResp = new dbWorker();
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			PrePostCondi rollback = new PrePostCondi();
			
			//ROLLBACK
			rta = oraResp.oraUpdate(report, "update tarjetas set id_estado = 3, SOPORTE_FISICO = 0 where id_cuenta =" + ID_CUENTA, configEntidad);
			
			rollback.validaRollBackUpdate(report, rta);
			
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