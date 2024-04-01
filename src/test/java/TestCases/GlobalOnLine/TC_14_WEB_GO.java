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
import Pasos.GlobalOnLine.TC_14_GO_PASOS;

public class TC_14_WEB_GO {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad, String cuentas_generales) {
		//validation 
		boolean result = true;
		try {	
			
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
			               + "##################################################################################################################################################################################################################\r\n");
			
            System.out.println("Configuring TC_14_WEB_GO\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();
			TC_14_GO_PASOS tc = new TC_14_GO_PASOS();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
				
			// Datos esperados en cuenta moneda
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
	        String[] rtaAut8118a = new String[3];
			String verifQuery8118a = "";
			String SALDO = "1000";
			String DISPONIBLE = "1000";
			String DISPONIBLE_ADELANTO = "1000";
						
			
			// PRE CONDICIONES
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GO = JsonPath.from(configEntidad).get("GO.url_go");
			
			int rta2 = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA SET SALDO = 1000, DISPONIBLE = 1000, DISPONIBLE_ADELANTO =1000, IMPORTE_PAGOS = 1000 WHERE ID_CUENTA =" + ID_CUENTA, configEntidad);
			ppCondi.preCondicionBD(report, rta2);		

			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");

			//Se abre el driver con la URL de GO
			driver.get(URL_GO);

			//ADD THE STEPS BELOW
			//LOGIN
			report.AddLine("Acceso a la pagina de GO");
			acceso.loginGlobalOnLine(data, report, DM, iteration, name, repo, configEntidad);

			//NAVEGACION DEL MENU SIDEBAR
			menu.navegacion_lvl3(data, report, DM, iteration, name, repo, "Emisión", "Socios", "Cuentas");

			//PÁGINA 1
			tc.pagina1(data, report, DM, iteration, name, repo, ID_CUENTA);

			Thread.sleep(5000);
					
			//VALIDACION 
			// Validaciones CUENTAS_MONEDA
			report.AddLine("Corremos la validacion cuenta moneda");
			System.out.println("Corremos la validacion cuenta moneda\r\n");

			verifQuery8118a = "SELECT SALDO, DISPONIBLE, DISPONIBLE_ADELANTO FROM CUENTAS_MONEDA WHERE ID_CUENTA = "+ ID_CUENTA +" AND SALDO = 1000 AND DISPONIBLE = 1000 AND DISPONIBLE_ADELANTO = 1000";
			rtaAut8118a = oraResp.oraThreeQuery(report, verifQuery8118a, configEntidad);		
			
			if (rtaAut8118a[0].equals(SALDO)) {
				report.AddLine("VALIDACION CORRECTA: SALDO: $" + rtaAut8118a[0]);
				System.out.println("##[section] : VALIDACION CORRECTA: SALDO: $" + rtaAut8118a[0] + "\r\n");

			} else {
				report.AddLineAssertionError("ERROR: EL SALDO ESPERADO ES: $" + SALDO + " PERO SE OBTUVO $ " + rtaAut8118a[0]);
				System.out.println("##[warning] : ERROR: EL SALDO ESPERADO ES:  $"+SALDO+" PERO SE OBTUVO $" + rtaAut8118a[0] + "\r\n");
			}

			if (rtaAut8118a[1].equals(DISPONIBLE)) {
				report.AddLine("VALIDACION CORRECTA: DISPONIBLE: $" + rtaAut8118a[1]);
				System.out.println("##[section] : VALIDACION CORRECTA: DISPONIBLE: $" + rtaAut8118a[1] + "\r\n");

			} else {
				report.AddLineAssertionError("ERROR: EL DISPONIBLE ESPERADO ES: $" + DISPONIBLE + " PERO SE OBTUVO $" + rtaAut8118a[1]);
				System.out.println("##[warning] : ERROR: EL DISPONIBLE ESPERADO ES: $" + DISPONIBLE + " PERO SE OBTUVO $" + rtaAut8118a[1] + "\r\n");
			}

			if (rtaAut8118a[2].equals(DISPONIBLE_ADELANTO)) {
				report.AddLine("VALIDACION CORRECTA: DISPONIBLE_ADELANTO: $" + rtaAut8118a[2]);
				System.out.println("##[section] : VALIDACION CORRECTA: DISPONIBLE_ADELANTO: $" + rtaAut8118a[2] + "\r\n");
	
			} else {
				report.AddLineAssertionError("ERROR: EL DISPONIBLE_ADELANTO ESPERADO ES: $" + DISPONIBLE_ADELANTO + " PERO SE OBTUVO $" + rtaAut8118a[2]);
				System.out.println("##[warning] : ERROR: EL DISPONIBLE_ADELANTO ESPERADO ES: $" + DISPONIBLE_ADELANTO + " PERO SE OBTUVO $" + rtaAut8118a[2] + "\r\n");
			}
						
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
						
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
              + "##################################################################################################################################################################################################################\r\n");
			

		} catch(Exception e) {
			e.printStackTrace();
			report.AddLineAssertionError(e.getMessage());			
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