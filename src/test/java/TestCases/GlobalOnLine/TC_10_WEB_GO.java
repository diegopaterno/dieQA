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
import Pasos.GlobalOnLine.TC_10_GO_PASOS;

public class TC_10_WEB_GO {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad, String cuentas_generales) {
		//validation 
		boolean result = true;
		try {	
			
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
			               + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_10_WEB_GO\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();		
			TC_10_GO_PASOS tc = new TC_10_GO_PASOS();
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
			
			// PRECONDICION			
            rta = oraResp.oraUpdate(report, "update cuentas set id_estado = 1 where id_cuenta =" + ID_CUENTA, configEntidad);
			
			ppCondi.preCondicionBD(report, rta);

			//Se abre el driver con la URL de GO
			driver.get(URL_GO);

			//ADD THE STEPS BELOW
			//PRECONDICION - ASEGURARSE QUE LA CUENTA EXISTE Y ESTE "BAJA"
			//LOGIN
			report.AddLine("Acceso a la pagina de Global Online con las credenciales");
			acceso.loginGlobalOnLine(data, report, DM, iteration, name, repo, configEntidad);

			//NAVEGACION DEL MENU SIDEBAR
			menu.navegacion_lvl3(data, report, DM, iteration, name, repo, "Emisión", "Socios", "Cuentas");

			//PÁGINA 1
			tc.pagina1(data, report, DM, iteration, name, repo, ID_CUENTA);

			//PÁGINA 2
			tc.pagina2(data, report, DM, iteration, name, repo);

			//ADD VALIDATIONS AT THE END
			tc.validar(data, report, DM, iteration, name, repo);
			
			// Esperamos a que se grabe el registro en la base de datos
			Thread.sleep(3000);
									
			// VALIDACION EN EL BACKEND						
			report.AddLine("Se busca el id_estado de la cuenta");
			System.out.println("Se busca el id_estado de la cuenta \r\n");

			resp = oraResp.oraOneQuery(report, "select id_estado from cuentas where id_cuenta =" + ID_CUENTA, configEntidad);
		
			if (resp.equals("0")) {
				report.AddLine("Se valida de forma exitosa que el estado de la cuenta esta ACTIVA");
				System.out.println("##[section] : Se valida de forma exitosa que el estado de la cuenta esta ACTIVA \r\n");
				
			} else {
				report.AddLineAssertionError("Error!!. La cuenta deberia estar con el estado 0 ACTIVA pero es igual a:" + resp);
				System.out.println("##[warning] : Error!!. La cuenta deberia estar con el estado 0 ACTIVA pero es igual a:" + resp + "\r\n");
				result = false;
			}
					
			
			//ROLLBACK
			rta = oraResp.oraUpdate(report, "update cuentas set id_estado = 0 where id_cuenta =" + ID_CUENTA, configEntidad);	
			rollback.validaRollBackUpdate(report, rta);
			
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
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			PrePostCondi rollback = new PrePostCondi();
			dbWorker oraResp = new dbWorker();
			
			//ROLLBACK
			rta = oraResp.oraUpdate(report, "update cuentas set id_estado = 0 where id_cuenta =" + ID_CUENTA, configEntidad);	
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