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
import Pasos.GlobalOnLine.TC_08_GO_PASOS;

public class TC_08_WEB_GO {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad, String cuentas_generales) {
		//validation 
		boolean result = true;
		try {
			
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
			               + "##################################################################################################################################################################################################################\r\n");
			
            System.out.println("Configuring TC_08_WEB_GO\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);


			//SET VARIABLES
			int rta = 0;
			String ID_CUENTA_res = "";
		    String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
		    String ID_SUCURSAL_original = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.id_sucursal_original");
		    String ID_GRUPO_AFINIDAD_original = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.id_grupo_afinidad_original"); 
		    String ID_SUCURSAL_cambio = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.id_sucursal_cambio");
		    String ID_GRUPO_AFINIDAD_cambio = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.id_grupo_afinidad_cambio");
		    String ID_CUENTA_EXTERNA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.id_cuenta_externa");
		  
			
			
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();
			TC_08_GO_PASOS tc = new TC_08_GO_PASOS();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			
			
			//SET ENVIRONMENT
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GO = JsonPath.from(configEntidad).get("GO.url_go");

			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");

			//Se abre el driver con la URL de GO
			driver.get(URL_GO);

			//ADD THE STEPS BELOW
			//PRECONDICION - NOS ASEGURAMOS DE QUE ESTÉ EN EL ESTADO CORRECTO !!
			 rta = oraResp.oraUpdate(report, "update cuentas set id_estado = 0 where id_cuenta = " + ID_CUENTA, configEntidad);
				
			 ppCondi.preCondicionBD(report, rta);                   
				
			//LOGIN
			report.AddLine("Acceso a la pagina de Global Online con las credenciales");
			acceso.loginGlobalOnLine(data, report, DM, iteration, name, repo, configEntidad);

			//NAVEGACION DEL MENU SIDEBAR
			menu.navegacion_lvl3(data, report, DM, iteration, name, repo, "Emisión", "Socios", "Cuentas");

			//PÁGINA 1
			tc.pagina1(data, report, DM, iteration, name, repo, ID_CUENTA);

			//PÁGINA 2
			tc.pagina2(data, report, DM, iteration, name, repo, ID_SUCURSAL_cambio, ID_GRUPO_AFINIDAD_cambio);
						
			//ADD VALIDATIONS AT THE END
			tc.validar(data, report, DM, iteration, name, repo);
			
			Thread.sleep(5000);
						     
			
			// VARIFICACION EN LA BDD	
			report.AddLine("Se realiza la verificacion de la cuenta: " + ID_CUENTA);
			System.out.println("Se realiza la verificacion de la cuenta:" + ID_CUENTA + "\r\n");
			ID_CUENTA_res = oraResp.oraOneQuery(report, "SELECT COUNT (*)\r\n"
					+ "FROM CUENTAS \r\n"
					+ "WHERE ID_CUENTA =" + ID_CUENTA + " AND ID_SUCURSAL = " + ID_SUCURSAL_cambio + " AND ID_GRUPO_AFINIDAD = " + ID_GRUPO_AFINIDAD_cambio + " AND ID_POSICION_IMPOSITIVA = 3 \r\n"
					+ "AND CUENTA_EXTERNA = 'TC08GO' AND ENTREGA_TARJETA = 1", configEntidad);
			
			if (ID_CUENTA_res.equals("1")) {
				report.AddLine("Verificacion de la modificacion de la cuenta EXITOSO.");
				System.out.println("##[section] : Verificacion de la modificacion de la cuenta EXITOSO.\r\n");
				
			} else {
				report.AddLineAssertionError("Error! No se logro la modificacion de la cuenta : " + ID_CUENTA);
				System.out.println("##[warning] : Error! No se logro la modificacion de la cuenta: " + ID_CUENTA + "\r\n");
				result = false;	
			}
			
			
			//ROLLBACK			
			rta = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_SUCURSAL = " + ID_SUCURSAL_original + ", ID_GRUPO_AFINIDAD =  " + ID_GRUPO_AFINIDAD_original + ", ID_POSICION_IMPOSITIVA = 5, CUENTA_EXTERNA = " + ID_CUENTA_EXTERNA + ", ENTREGA_TARJETA = 2 WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			
			if (rta > 0) {
				report.AddLine("Rollback realizado exitosamente");
				System.out.println("##[section] : Rollback realizado exitosamente \r\n");
			} else {
				report.AddLine("Error! No se logró realizar el rollback");
				System.out.println("##[warning] :Error! No se logró realizar el rollback\r\n");
				
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
			String ID_SUCURSAL_original = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.id_sucursal_original");
			String ID_GRUPO_AFINIDAD_original = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.id_grupo_afinidad_original"); 
			String ID_CUENTA_EXTERNA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.id_cuenta_externa");
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			

			//ROLLBACK			
			rta = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_SUCURSAL = " + ID_SUCURSAL_original + ", ID_GRUPO_AFINIDAD =  " + ID_GRUPO_AFINIDAD_original + ", ID_POSICION_IMPOSITIVA = 5, CUENTA_EXTERNA = " + ID_CUENTA_EXTERNA + ", ENTREGA_TARJETA = 2 WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			
			if (rta > 0) {
				report.AddLine("Rollback realizado exitosamente");
				System.out.println("##[section] : Rollback realizado exitosamente \r\n");
			} else {
				report.AddLine("Error! No se logró realizar el rollback");
				System.out.println("##[warning] :Error! No se logró realizar el rollback\r\n");
				
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