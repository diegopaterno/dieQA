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
import Pasos.GlobalOnLine.TC_06_GO_PASOS;

public class TC_06_WEB_GO {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad, String cuentas_generales) {
		//validation 
		boolean result = true;
		try {		
			
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
               + "##################################################################################################################################################################################################################\r\n");		
			
			System.out.println("Configuring TC_06_WEB_GO\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();
			TC_06_GO_PASOS tc = new TC_06_GO_PASOS();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			

			//SET VARIABLES
			String ID_PRODUCTO = JsonPath.from(cuentas_generales).get("PARAMETRIA_CUENTAS.id_producto");
			String PRODdesc = "";
			String res = "";
			int rta = 0;
			int rta1 = 0;
			
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GO = JsonPath.from(configEntidad).get("GO.url_go");
			
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");			
			
			// PRECONDICION
			PRODdesc = oraResp.oraOneQuery(report, "SELECT DESCRIPCION FROM PRODUCTOS WHERE ID_PRODUCTO = " + ID_PRODUCTO, configEntidad);
			ppCondi.preCondicionBD(report, PRODdesc);
			
			// Seteamos al producto el estado
			rta = oraResp.oraUpdate(report, "UPDATE PRODUCTOS SET ID_ESTADO = 0 WHERE ID_PRODUCTO = " + ID_PRODUCTO, configEntidad);
			ppCondi.preCondicionBD(report, rta);
		
			//Se abre el driver con la URL de GO
			driver.get(URL_GO);

			//ADD THE STEPS BELOW
			//LOGIN
			report.AddLine("Acceso a la pagina de Global Online con las credenciales");
			acceso.loginGlobalOnLine(data, report, DM, iteration, name, repo, configEntidad);

			//NAVEGACION DEL MENU SIDEBAR
			menu.navegacion_lvl4(data, report, DM, iteration, name, repo, "Emisión", "Socios", "Parametría", "Productos");

			//PÁGINA 1
			tc.pagina1(data, report, DM, iteration, name, repo, PRODdesc);

			//PÁGINA 2
			tc.pagina2(data, report, DM, iteration, name, repo);

			//ADD VALIDATIONS AT THE END
			tc.validar(data, report, DM, iteration, name, repo);

			// Esperamos a que se grabe el registro en la base de datos
			Thread.sleep(3000);
					
			// VALIDACION BACKEND
			report.AddLine("Se busca el id_estado del producto.");
			System.out.println("Se busca el id_estado del producto. \r\n");
			
			res = oraResp.oraOneQuery(report, "select id_estado from productos where descripcion = '" + PRODdesc + "'", configEntidad);
			
			if (res.equals("0")) {
				report.AddLineAssertionError("Error: El ID_ESTADO del producto en la BD es: " + res + " - Se esperaba: 0");
				System.out.println("##[warning] :Error: El ID_ESTADO del producto en la BD es: " + res + " - Se esperaba: 0 " + "\r\n");
				result = false;
				
			} else {
				report.AddLine("Validacion correcta: ID_ESTADO del producto en la BD es: " + res);
				System.out.println("##[section] : Validacion correcta: ID_ESTADO del producto en la BD es: " + res + "\r\n");
				
			}		
			
			
			//POSTCONDICION 
			rta1 = oraResp.oraUpdate(report, "UPDATE PRODUCTOS SET ID_ESTADO = 0 WHERE ID_PRODUCTO = " + ID_PRODUCTO, configEntidad);
			
			if (rta1 > 0) {
				report.AddLine("Se realizo el update del producto de forma exitosa");
				System.out.println("##[section] : Se realizo el update del producto de forma exitosa \r\n");

			} else {
				report.AddLine("ERROR: No se logro realizar el update de producto");
				System.out.println("##[warning] : ERROR: No se logro realizar el update de producto \r\n");

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
			String ID_PRODUCTO = JsonPath.from(cuentas_generales).get("PARAMETRIA_CUENTAS.id_producto");
			int rta1 = 0;
			dbWorker oraResp = new dbWorker();
			
			//ROLLBACK
			rta1 = oraResp.oraUpdate(report, "UPDATE PRODUCTOS SET ID_ESTADO = 0 WHERE ID_PRODUCTO = " + ID_PRODUCTO, configEntidad);
			
			if (rta1 > 0) {
				report.AddLine("Se realizo el update del producto de forma exitosa");
				System.out.println("##[section] : Se realizo el update del producto de forma exitosa \r\n");

			} else {
				report.AddLine("ERROR: No se logro realizar el update de producto");
				System.out.println("##[warning] : ERROR: No se logro realizar el update de producto \r\n");

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