package TestCases.GlobalBackend;

import java.util.Locale;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.Acceso;
import Pasos.Generales.NavLateral;
import Pasos.Generales.PrePostCondi;
import Pasos.GlobalBackend.TC_03_GBACKEND_PASOS;
import Repositories.Repo_WebRepository;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;

public class TC_03_WEB_GBACKEND {
	WebDriver driver;


	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {	
			
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_03_WEB_GBACKEND\r\n");
			
			Locale Local = Locale.getDefault();
			System.out.println("Muestro valor de variable Local: " + Local);
			Locale.setDefault(new Locale("es", "AR"));
			System.out.println("Muestro el nuevo valor de variable Local: " + Local);
			
			
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);
			
			
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//PRUEBA DE CAMBIO DE RESOLUCION
			DesiredCapabilities dc=new DesiredCapabilities();    
			dc.setCapability("screen-resolution","1280x1024");

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();
			TC_03_GBACKEND_PASOS tc = new TC_03_GBACKEND_PASOS();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();

			//SET VARIABLES

			String cotizCompra = "8";
			String cotizVenta = "9";
			int rtaPrePostCondi = 0;
			
			String[] res = new  String[2];
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBACKEND = JsonPath.from(configEntidad).get("GBACKEND.url_gbackend");
			
			System.out.println("##[command] :------ Initializating test: " + name + " ------\r\n");

			// PRECONDICONES
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");

			//BORRADO DE REGISTRO SI EXISTIESE
			rtaPrePostCondi = oraResp.oraDeleteParam(report, "DELETE COTIZACIONES WHERE FECHA = TRUNC (SYSDATE)", configEntidad);
			ppCondi.preCondicionBD(report, rtaPrePostCondi);

			// SE INSERTA REGISTRO
			rtaPrePostCondi = oraResp.oraInsertParam(report, "Insert into COTIZACIONES\r\n"
					+ "(FECHA,ID_MONEDA_ORIGEN,ID_MONEDA_DESTINO,COTIZACION_COMPRA,COTIZACION_VENDE,ID_ESTADO_TIPO,ID_ESTADO,ID_AUDITORIA,ROW_VERSION,ROW_FECHA,ID_ESTADO_TIPO_CONFIRM,ID_ESTADO_CONFIRM)\r\n"
					+ "values (TRUNC (SYSDATE) ,'840','32',16,17,'4','0',null,null,null,'40',null)", configEntidad);
			ppCondi.preCondicionBD(report, rtaPrePostCondi);

			// Tiempo para que se grabe el registro
			report.AddLine("Tiempo de espera de 5 segundos agregado para que el registro se plasme en el frontend");
			System.out.println("Tiempo de espera de 5 segundos agregado para que el registro se plasme en el frontend\r\n");
			Thread.sleep(5000);

			//SET THE URL
			driver.get(URL_GBACKEND);

			//ADD THE STEPS BELOW
			// LOGIN
			report.AddLine("Acceso a la pagina de Global Backend");
			System.out.println("Acceso a la pagina de Global Backend\r\n");
			acceso.loginUsernameGBackEnd(data, report, DM, iteration, name, repo,configEntidad);

			// NAVEGACION DEL MENU SIDEBAR
			menu.navegacion_lvl2(data, report, DM, iteration, name, repo, "Parametría", " Cotización");

			// MODIFICACION DE COTIZACION
			//PAGINA 1
			tc.pagina1(data, report, DM, iteration, name, repo);

			//PAGINA 2
			tc.pagina2(data, report, DM, iteration, name, repo, cotizCompra, cotizVenta);

			//PAGINA DE VALIDACION
			//tc.validacionEdicion(data, report, DM, iteration, name, repo);

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

			//ROLLBACK
			//BORRADO DE REGISTRO SI EXISTIESE

			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			rtaPrePostCondi = oraResp.oraDeleteParam(report, "DELETE COTIZACIONES WHERE FECHA = TRUNC (SYSDATE)", configEntidad);
			ppCondi.validaRollBackDelete(report, rtaPrePostCondi);

			System.out.println("##[command] :------ Finished test: " + name + " ------\r\n");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");


		} catch(Exception e) {
			e.printStackTrace();
			report.AddLineAssertionError(e.getMessage());	
			
            //Se agrega de nuevo ROLLBACK para que no quede la base inconsistente si falla el TestCase  
			
			//SET VARIABLES 
			int rtaPrePostCondi = 0;
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			
			//ROLLBACK
			//BORRADO DE REGISTRO SI EXISTIESE

			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			rtaPrePostCondi = oraResp.oraDeleteParam(report, "DELETE COTIZACIONES WHERE FECHA = TRUNC (SYSDATE)", configEntidad);
			ppCondi.validaRollBackDelete(report, rtaPrePostCondi);
			
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