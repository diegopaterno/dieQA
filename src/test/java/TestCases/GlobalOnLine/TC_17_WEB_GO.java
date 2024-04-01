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
import Pasos.GlobalOnLine.TC_17_GO_PASOS;

public class TC_17_WEB_GO {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad, String cuentas_generales) {
		//validation 
		boolean result = true;
		try {			
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
			               + "##################################################################################################################################################################################################################\r\n");
			
            System.out.println("Configuring TC_17_WEB_GO\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();
			TC_17_GO_PASOS tc = new TC_17_GO_PASOS();
			dbWorker oraResp = new dbWorker();
	

			//SET VARIABLES
			int rta = 0;
			String resp[] = new String[2];
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			String ID_SUCURSAL = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.id_sucursal_original");
				
			
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GO = JsonPath.from(configEntidad).get("GO.url_go");
			
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");

			//Se abre el driver con la URL de GO
			driver.get(URL_GO);

			//ADD THE STEPS BELOW
			//LOGIN
			report.AddLine("Acceso a la pagina de Global Online con las credenciales");
			acceso.loginGlobalOnLine(data, report, DM, iteration, name, repo, configEntidad);

			//NAVEGACION DEL MENU SIDEBAR
			menu.navegacion_lvl3(data, report, DM, iteration, name, repo, "Emisión", "Movimientos", "Cargas");

			//PÁGINA 1
			tc.pagina1(data, report, DM, iteration, name, repo);

			//PÁGINA 2
			tc.pagina2(data, report, DM, iteration, name, repo, ID_CUENTA, ID_SUCURSAL);

			//ADD VALIDATIONS AT THE END
			tc.validar(data, report, DM, iteration, name, repo);
			
			// Esperamos a que se grabe el registro en la base de datos
			Thread.sleep(3000);
					
			
			// VALIDACION BACKEND
			resp = oraResp.oraTwoQuery(report, "SELECT IMPORTE,ID_ESTADO FROM PAGOS WHERE ID_CUENTA = "+ ID_CUENTA +" AND NRO_COMPROBANTE = 2589", configEntidad);
		
			if (resp[0].equals("1000") && resp[1].equals("0")) {
				report.AddLine("Validacion correcta de Pagos");
				System.out.println("##[section] : Validacion correcta de Pagos \r\n");
				
			} else {
				report.AddLineAssertionError("Error. IMPORTE ["+resp[0]+"] o ID_ESTADO ["+resp[1]+"] incorrecto.");
				System.out.println("##[warning] : Error. IMPORTE ["+resp[0]+"] o ID_ESTADO ["+resp[1]+"] incorrecto. \r\n");
				result = false;
			}			
			
			
			//ROLLBACK
			rta = oraResp.oraDelete(report, "DELETE PAGOS WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			
			if (rta > 0) {
				report.AddLine("Borrado exitoso de PAGOS");
				System.out.println("##[section] : Borrado exitoso de PAGOS \r\n");
			} 	else {
				report.AddLineAssertionError("Error. No se realizo el Rollback correctamente. ");
				System.out.println("##[warning] : Error. No se realizo el Rollback correctamente \r\n");
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
			rta = oraResp.oraDelete(report, "DELETE PAGOS WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			
			if (rta > 0) {
				report.AddLine("Borrado exitoso de PAGOS");
				System.out.println("##[section] : Borrado exitoso de PAGOS \r\n");
			} 	else {
				report.AddLineAssertionError("Error. No se realizo el Rollback correctamente. ");
				System.out.println("##[warning] : Error. No se realizo el Rollback correctamente \r\n");
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