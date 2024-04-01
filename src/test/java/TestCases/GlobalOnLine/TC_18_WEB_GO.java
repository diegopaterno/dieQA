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
import Pasos.GlobalOnLine.TC_18_GO_PASOS;

public class TC_18_WEB_GO {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad, String cuentas_generales) {
		//validation 
		boolean result = true;
		try {			
			
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
			               + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_18_WEB_GO\r\n");


			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();
			TC_18_GO_PASOS tc = new TC_18_GO_PASOS();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();

			//SET VARIABLES
			int rta1 = 0;
			int rta2 = 0;
			int rta3 = 0;
			int rta4 = 0;
			int rta5 = 0;
			int rtaP = 0;
			String resp[] = new String[2];
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_2.cuenta_virtual");
			String ID_TARJETA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_2.id_tarjeta");
			String ID_TARJETA_VIGENTE = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_2.id_tarjeta");
			
			
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GO = JsonPath.from(configEntidad).get("GO.url_go");
			
			// PRECONDICION
			rtaP = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_TARJETA = "+ ID_TARJETA, configEntidad);
			ppCondi.preCondicionBD(report, rtaP);
			
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");

			//Se abre el driver con la URL de GO
			driver.get(URL_GO);

			//ADD THE STEPS BELOW
			//LOGIN
			report.AddLine("Acceso a la pagina de Global Online con las credenciales");
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
			
			// VALIDACION BACKEND
			resp = oraResp.oraTwoQuery(report, "SELECT ID_ESTADO, SOPORTE_FISICO FROM TARJETAS WHERE ID_CUENTA = "+ ID_CUENTA +" AND ID_TARJETA NOT IN ("+ ID_TARJETA +")", configEntidad);
			
			if (resp[0].equals("0") && resp[1].equals("1")) {
				report.AddLine("Validacion correcta de ID_ESTADO y SOPORTE_FISICO");
				System.out.println("##[section] : Validacion correcta de ID_ESTADO y SOPORTE_FISICO \r\n");
				
			} else {
				report.AddLineAssertionError("Error. ID_ESTADO ["+resp[0]+"] o SOPORTE_FISICO ["+resp[1]+"] incorrecto.");
				System.out.println("##[warning] : Error. ID_ESTADO ["+resp[0]+"] o SOPORTE_FISICO ["+resp[1]+"] incorrecto. \r\n");
				result = false;
			}
				
			//ROLLBACK
			rta1 = oraResp.oraDelete(report, "DELETE TARJETAS_HISTORIAL WHERE ID_TARJETA IN (SELECT ID_TARJETA FROM TARJETAS WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_TARJETA NOT IN ("+ ID_TARJETA +"))", configEntidad);
			rta2 = oraResp.oraUpdate(report, "UPDATE SOCIOS SET ID_TARJETA_VIGENTE = NULL WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			rta3 = oraResp.oraDelete(report, "DELETE TARJETAS WHERE ID_CUENTA = " + ID_CUENTA + " AND  ID_TARJETA NOT IN ("+ ID_TARJETA +")", configEntidad);
			rta4 = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_TARJETA = " + ID_TARJETA, configEntidad);
			rta5 = oraResp.oraUpdate(report, "UPDATE SOCIOS SET ID_TARJETA_VIGENTE = " + ID_TARJETA_VIGENTE + " WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			
			
			if (rta1 > 0 && rta2 > 0 && rta3 > 0 && rta4 > 0 && rta5 > 0) {
				report.AddLine("Rollback exitoso! Se realizo el borrado de TARJETAS_HISTORIAL, TARJETAS y update de SOCIOS y TARJETAS correctamente. ");
				System.out.println("##[section] :  Rollback exitoso! Se realizo el borrado de TARJETAS_HISTORIAL, TARJETAS y update de SOCIOS y TARJETAS correctamente. \r\n");
			}	else {
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
			int rta1 = 0;
			int rta2 = 0;
			int rta3 = 0;
			int rta4 = 0;
			int rta5 = 0;
			dbWorker oraResp = new dbWorker();
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_2.cuenta_virtual");
			String ID_TARJETA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_2.id_tarjeta");
			String ID_TARJETA_VIGENTE = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_2.id_tarjeta");
			
			//ROLLBACK
			rta1 = oraResp.oraDelete(report, "DELETE TARJETAS_HISTORIAL WHERE ID_TARJETA IN (SELECT ID_TARJETA FROM TARJETAS WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_TARJETA NOT IN ("+ ID_TARJETA +"))", configEntidad);
			rta2 = oraResp.oraUpdate(report, "UPDATE SOCIOS SET ID_TARJETA_VIGENTE = NULL WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			rta3 = oraResp.oraDelete(report, "DELETE TARJETAS WHERE ID_CUENTA = " + ID_CUENTA + " AND  ID_TARJETA NOT IN ("+ ID_TARJETA +")", configEntidad);
			rta4 = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_TARJETA = " + ID_TARJETA, configEntidad);
			rta5 = oraResp.oraUpdate(report, "UPDATE SOCIOS SET ID_TARJETA_VIGENTE = " + ID_TARJETA_VIGENTE + " WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			
			
			if (rta1 > 0 && rta2 > 0 && rta3 > 0 && rta4 > 0 && rta5 > 0) {
				report.AddLine("Rollback exitoso! Se realizo el borrado de TARJETAS_HISTORIAL, TARJETAS y update de SOCIOS y TARJETAS correctamente. ");
				System.out.println("##[section] :  Rollback exitoso! Se realizo el borrado de TARJETAS_HISTORIAL, TARJETAS y update de SOCIOS y TARJETAS correctamente. \r\n");
			}	else {
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