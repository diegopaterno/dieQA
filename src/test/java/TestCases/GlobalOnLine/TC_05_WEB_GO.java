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
import Pasos.GlobalOnLine.TC_05_GO_PASOS;

public class TC_05_WEB_GO {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad, String cuentas_generales) {
		//validation 
		boolean result = true;
		try {	

			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");


			System.out.println("Configuring TC_05_WEB_GO\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();
			TC_05_GO_PASOS tc = new TC_05_GO_PASOS();
			dbWorker oraResp = new dbWorker();

			//SET VARIABLES
			String ID_MODELO_LIMITE = JsonPath.from(cuentas_generales).get("PARAMETRIA_CUENTAS.id_modelo_limite");
			String MODELO_LIMITE_desc = "";
			String PROD_desc = "Test de producto Automatizado";
			int rta1 = 0;
			String res = "";
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GO = JsonPath.from(configEntidad).get("GO.url_go");

			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			
			
			//PRE_PRECONDICIONES
			//Modelo de limite
			MODELO_LIMITE_desc = oraResp.oraOneQuery(report, "SELECT DESCRIPCION FROM MODELOS_LIMITE WHERE ID_MODELO_LIMITE = " + ID_MODELO_LIMITE, configEntidad);
			
			
			//Se abre el driver con la URL de GO
			driver.get(URL_GO);

			//ADD THE STEPS BELOW
			//LOGIN
			report.AddLine("Acceso a la pagina de Global Online con las credenciales");
			acceso.loginGlobalOnLine(data, report, DM, iteration, name, repo, configEntidad);

			//NAVEGACION DEL MENU SIDEBAR
			menu.navegacion_lvl4(data, report, DM, iteration, name, repo, "Emisión", "Socios", "Parametría", "Productos");

			//PÁGINA 1
			tc.pagina1(data, report, DM, iteration, name, repo);

			//PÁGINA 2
			tc.pagina2(data, report, DM, iteration, name, repo, PROD_desc, MODELO_LIMITE_desc);

			//ADD VALIDATIONS AT THE END
			//PÁGINA DE VALIDACIÓN
			tc.pagina3(data, report, DM, iteration, name, repo);

			// Esperamos a que se grabe el registro en la base de datos
			Thread.sleep(3000);



			// VALIDACION BACKEND
			res = oraResp.oraOneQuery(report, "select ID_PRODUCTO from productos where descripcion = '"+PROD_desc+"'", configEntidad);

			if (res.equals("")) {
				report.AddLineAssertionError("Error. producto no encontrado.");
				System.out.println("##[warning] : Error. producto no encontrado. \r\n");
				result = false;

			} else {
				report.AddLine("Producto encontrado. Se realiza el borrado");
				System.out.println("##[section] :  Producto encontrado. Se realiza el borrado \r\n");

			}		

			//ROLLBACK
			rta1 = oraResp.oraDelete(report, "delete productos where id_producto = (select ID_PRODUCTO from productos where descripcion = '" + PROD_desc + "')", configEntidad);

			if (rta1 > 0) {
				report.AddLine("Borrado exitoso de Producto");
				System.out.println("##[section] : Borrado exitoso de Producto \r\n");

			} else {
				report.AddLine("ERROR: No se logro borrar el producto");
				System.out.println("##[warning] : ERROR: No se logro borrar el producto \r\n");

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
			String PROD_desc = "Test de producto Automatizado";
			dbWorker oraResp = new dbWorker();
			
			
			//ROLLBACK
			rta1 = oraResp.oraDelete(report, "delete productos where id_producto = (select ID_PRODUCTO from productos where descripcion = '" + PROD_desc + "')", configEntidad);

			if (rta1 > 0) {
				report.AddLine("Borrado exitoso de Producto");
				System.out.println("##[section] : Borrado exitoso de Producto \r\n");

			} else {
				report.AddLine("ERROR: No se logro borrar el producto");
				System.out.println("##[warning] : ERROR: No se logro borrar el producto \r\n");

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