package TestCases.GlobalBackend;

import org.openqa.selenium.WebDriver;
import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.Acceso;
import Pasos.Generales.NavLateral;
import Pasos.Generales.PrePostCondi;
import Pasos.GlobalBackend.TC_02_GBACKEND_PASOS;
import Repositories.Repo_WebRepository;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;

public class TC_02_WEB_GBACKEND {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");

			System.out.println("Configuring TC_02_WEB_GBACKEND\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();
			TC_02_GBACKEND_PASOS tc = new TC_02_GBACKEND_PASOS();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();

			//SET VARIABLES
			String cotizCompra = "18";
			String cotizVenta = "19";
			int rtaPre = 0;
			String[] res = new  String[2];
			res[0] = "";
			res[1] = "";
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBACKEND = JsonPath.from(configEntidad).get("GBACKEND.url_gbackend");

			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");

			// Precondicion 
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");

			//BORRADO DE REGISTRO COTIZACIONES
			rtaPre = oraResp.oraDeleteParam(report, "DELETE COTIZACIONES WHERE FECHA = TRUNC (SYSDATE)", configEntidad);
			ppCondi.preCondicionBD(report, rtaPre);

			//SET THE URL
			driver.get(URL_GBACKEND);

			//ADD THE STEPS BELOW
			// LOGIN
			report.AddLine("Acceso a la pagina de Global Backend");
			System.out.println("Acceso a la pagina de Global Backend\r\n");
			acceso.loginUsernameGBackEnd(data, report, DM, iteration, name, repo, configEntidad);

			// NAVEGACION DEL MENU SIDEBAR
			menu.navegacion_lvl2(data, report, DM, iteration, name, repo, "Parametría", " Cotización");

			//PAGINA 1
			tc.pagina1(data, report, DM, iteration, name, repo);

			//PAGINA 2
			tc.pagina2(data, report, DM, iteration, name, repo, cotizCompra, cotizVenta);

			//ADD VALIDATIONS AT THE END
			//PAGINA DE VALIDACION
			tc.validacion(data, report, DM, iteration, name, repo);

			// Esperamos que se cargue el registro en la BDD
			Thread.sleep(8000);
			
			report.AddLine("--- Ejecutando validacion en la Base de Datos ---");
			System.out.println("--- Ejecutando validacion en la Base de Datos ---\r\n");
			
			//Consulto los montos de las cotizaciones que se grabaron en la BD
			res = oraResp.oraTwoQueryParam(report, "SELECT COTIZACION_COMPRA, COTIZACION_VENDE \r\n"
					+ "FROM COTIZACIONES \r\n"
					+ "WHERE FECHA = TRUNC (SYSDATE)", configEntidad);

			//Verifico que los datos en la base de datos sean iguales a los generados desde el Front
			if (res[0].equals(cotizCompra)) {
				report.AddLine("VALIDACION CORRECTA: El precio de compra de la cotizacion en la BD es: " + res[0] + " y es igual al generado desde el Front: " + cotizCompra);
				System.out.println("##[section] : VALIDACION CORRECTA: El precio de compra de la cotizacion en la BD es: " + res[0] + " y es igual al generado desde el Front: " + cotizCompra  + "\r\n");
			}
			else {
				report.AddLineAssertionError("VALIDACION INCORRECTA: El precio de compra de la cotizacion en la BD es: " + res[0] + " y difiere al generado desde el Front: " + cotizCompra);
				System.out.println("##[warning] : VALIDACION INCORRECTA: El precio de compra de la cotizacion en la BD es: " + res[0] + " y difiere al generado desde el Front: " + cotizCompra + "\r\n");
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			}

			if (res[1].equals(cotizVenta)) {
				report.AddLine("VALIDACION CORRECTA: El precio de venta de la cotizacion en la BD es: " + res[1] + " y es igual al generado desde el Front: " + cotizVenta);
				System.out.println("##[section] : VALIDACION CORRECTA: El precio de venta de la cotizacion en la BD es: " + res[1] + " y es igual al generado desde el Front: " + cotizVenta  + "\r\n");
			}
			else {
				report.AddLineAssertionError("VALIDACION INCORRECTA: El precio de venta de la cotizacion en la BD es: " + res[1] + " y difiere al generado desde el Front: " + cotizVenta);
				System.out.println("##[warning] : VALIDACION INCORRECTA: El precio de venta de la cotizacion en la BD es: " + res[1] + " y difiere al generado desde el Front: " + cotizVenta + "\r\n");
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			}

			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			//POST CONDICION
			//BORRADO DE REGISTRO COTIZACIONES
			rtaPre = oraResp.oraDeleteParam(report, "DELETE COTIZACIONES WHERE FECHA = TRUNC (SYSDATE)", configEntidad);
			ppCondi.validaRollBackDelete(report, rtaPre);

			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");

			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");


		} catch(Exception e) {
			e.printStackTrace();
			report.AddLineAssertionError(e.getMessage());
			
            //Se agrega de nuevo ROLLBACK para que no quede la base inconsistente si falla el TestCase  
			
			//SET VARIABLES 
			int rtaPre = 0;
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			
			//POST CONDICION
			//BORRADO DE REGISTRO COTIZACIONES
			rtaPre = oraResp.oraDeleteParam(report, "DELETE COTIZACIONES WHERE FECHA = TRUNC (SYSDATE)", configEntidad);
			ppCondi.validaRollBackDelete(report, rtaPre);
			
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