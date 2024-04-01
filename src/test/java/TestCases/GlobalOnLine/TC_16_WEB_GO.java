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
import Pasos.GlobalOnLine.TC_16_GO_PASOS;

public class TC_16_WEB_GO {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad, String cuentas_generales) {
		//validation 
		boolean result = true;
		try {

			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
			               + "##################################################################################################################################################################################################################\r\n");
			
            System.out.println("Configuring TC_16_WEB_GO\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();
			TC_16_GO_PASOS tc = new TC_16_GO_PASOS();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
	

			// SET VARIABLES
			String dni = "33293772";
			int rta = 1;
			String resp = "";
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			
					
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GO = JsonPath.from(configEntidad).get("GO.url_go");
			
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");

			//Se abre el driver con la URL de GO
			driver.get(URL_GO);
			
			//PRECONDI
			rta = oraResp.oraUpdate(report, "UPDATE DOMICILIOS\r\n"
					+ "SET ID_PAIS = 32, ID_PROVINCIA = 2, ID_CODIGO_POSTAL = 1722, DIRECCION = 'Rioja', NUMERO = 956, PISO = 9, DEPTO = 'B', ENTRE_CALLES = 'Tucuman y Av Balvin', \r\n"
					+ "REFERENCIA = 'Edificio color amarillo', TELEFONO = '+541141615646', BARRIO = 'Merlo Centro', LOCALIDAD = 'Merlo'\r\n"
					+ "WHERE ID_DOMICILIO IN ((SELECT ID_DOMICILIO_LEGAL FROM PERSONAS WHERE ID_PERSONA = (SELECT ID_PERSONA FROM SOCIOS WHERE ID_CUENTA = " + ID_CUENTA + ")), (SELECT ID_DOMICILIO_CORRESPONDENCIA FROM CUENTAS WHERE ID_CUENTA = " + ID_CUENTA + "))\r\n", configEntidad);
			ppCondi.preCondicionBD(report, rta);
			
			
			//ADD THE STEPS BELOW
			//LOGIN
			report.AddLine("Acceso a la pagina de Global Online");
			acceso.loginGlobalOnLine(data, report, DM, iteration, name, repo, configEntidad);

			//NAVEGACION DEL MENU SIDEBAR
			menu.navegacion_lvl3(data, report, DM, iteration, name, repo, "Emisión", "Socios", "Cuentas");

			//PÁGINA 1
			tc.pagina1(data, report, DM, iteration, name, repo, ID_CUENTA);

			//PÁGINA 2
			tc.pagina2(data, report, DM, iteration, name, repo, dni);

			//ADD VALIDATIONS AT THE END
			//PÁGINA DE VALIDACIÓN
			tc.validar(data, report, DM, iteration, name, repo);
			
			// VALIDACION
			// Esperamos a que se grabe el registro en la base de datos
			report.AddLine("Se validan los datos modificados en la BD");
			System.out.println("Se validan los datos modificados en la BD\r\n");
			
			Thread.sleep(5000);
			
			resp = oraResp.oraOneQuery(report, "SELECT COUNT (P.ID_PERSONA)\r\n"
					+ "FROM PERSONAS P\r\n"
					+ "INNER JOIN DOMICILIOS D ON D.ID_DOMICILIO = P.ID_DOMICILIO_LEGAL\r\n"
					+ "WHERE  P.ID_PERSONA = (SELECT ID_PERSONA FROM SOCIOS WHERE ID_CUENTA = "+ ID_CUENTA +") \r\n"
					+ "AND P.ID_TIPO_DOCUMENTO = 1 \r\n"
					+ "AND P.ID_PAIS_NACIMIENTO = 276 \r\n"
					+ "AND P.SEXO = 'F' \r\n"
					+ "AND P.ESTADO_CIVIL = 1 \r\n"
					+ "AND P.FECHA_NACIMIENTO = TO_DATE ('01/01/2005 00:00:00','DD/MM/RRRR HH24:MI:SS') \r\n"
					+ "AND P.MAIL = 'selenium@gmail.com' \r\n"
					+ "AND P.APELLIDO = 'Modificado'\r\n"
					+ "AND P.NOMBRE = 'Selenium'\r\n"
					+ "AND D.ID_PROVINCIA = 1 \r\n"
					+ "AND D.ID_CODIGO_POSTAL = 1004 \r\n"
					+ "AND D.DIRECCION = 'San Martin'\r\n"
					+ "AND D.NUMERO = 536 \r\n"
					+ "AND D.PISO = 3 \r\n"
					+ "AND D.DEPTO = 'A' \r\n"
					+ "AND D.ENTRE_CALLES = 'Tucuman y Lavalle' \r\n"
					+ "AND D.REFERENCIA = 'Edificio GP' \r\n"
					+ "AND D.TELEFONO = 1145781254 \r\n"
					+ "AND D.BARRIO = 'San Nicolas' \r\n"
					+ "AND D.LOCALIDAD = 'C.A.B.A'", configEntidad);	
					
			if (resp.equals("1")){
				report.AddLine("Validacion exitosa:<br>Se modificarion correctamente los datos en la tabla PERSONAS y DOMICILIOS");
				System.out.println("##[section] : Validacion exitosa:\r\nSe modificarion correctamente los datos en la tabla PERSONAS  y DOMICILIOS\r\n");
				
			} else {
				report.AddLineAssertionError("ERROR:<br>No se modificarion los datos en la tabla PERSONAS  y DOMICILIOS");
				System.out.println("##[warning] : ERROR:\r\nNo se modificarion los datos en la tabla PERSONAS  y DOMICILIOS\r\n");
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			}			
			
			//ROLLBACK
			report.AddLine("Ejecutamos el rollback de la modificacion de la cuenta");
			System.out.println("Ejecutamos el rollback de la modificacion de la cuenta \r\n");
			
			rta = oraResp.oraUpdate(report, "UPDATE PERSONAS \r\n"
					+ "SET ID_TIPO_DOCUMENTO = 1, SEXO = 'M',ESTADO_CIVIL = 0,FECHA_NACIMIENTO = to_date('29/08/1988 00:00:00','DD/MM/RRRR HH24:MI:SS'),\r\n"
					+ "MAIL = 'mauroaramirez88@gmail.com', APELLIDO = 'Ramirez',NOMBRE = 'Mauro',ID_PAIS_NACIMIENTO = 32\r\n"
					+ "WHERE ID_PERSONA = (SELECT ID_PERSONA FROM SOCIOS WHERE ID_CUENTA = "+ ID_CUENTA +")", configEntidad);
			
			
			if (rta > 0) {
				report.AddLine("Rollback exitoso! Update realizado correctamente a la cuenta");
				System.out.println("##[section] : Rollback exitoso! Update realizado correctamente a la cuenta \r\n");
				
				
			}	else {
				report.AddLineAssertionError("Error. No se realizo el update de la modificacion de la cuenta");
				System.out.println("##[warning] : Error. No se realizo el update de la modificacion de la cuenta\r\n");

			}

			rta = oraResp.oraUpdate(report, "UPDATE DOMICILIOS\r\n"
					+ "SET ID_PAIS = 32, ID_PROVINCIA = 2, ID_CODIGO_POSTAL = 1722, DIRECCION = 'Rioja', NUMERO = 956, PISO = 9, DEPTO = 'B', ENTRE_CALLES = 'Tucuman y Av Balvin', \r\n"
					+ "REFERENCIA = 'Edificio color amarillo', TELEFONO = '+541141615646', BARRIO = 'Merlo Centro', LOCALIDAD = 'Merlo'\r\n"
					+ "WHERE ID_DOMICILIO IN ((SELECT ID_DOMICILIO_LEGAL FROM PERSONAS WHERE ID_PERSONA = (SELECT ID_PERSONA FROM SOCIOS WHERE ID_CUENTA = " + ID_CUENTA + ")), (SELECT ID_DOMICILIO_CORRESPONDENCIA FROM CUENTAS WHERE ID_CUENTA = " + ID_CUENTA + "))\r\n", configEntidad);

			if (rta > 0) {
				report.AddLine("Rollback exitoso! El update de cambio de domicilio se ejecuto correctamente");
				System.out.println("##[section] : Rollback exitoso! El update de cambio de domicilio se ejecuto correctamente\r\n");
				
				
			}	else {
				report.AddLineAssertionError("Error. No se realizo el update del domicilio");
				System.out.println("##[warning] : Error. No se realizo el update del domicilio\r\n");
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
			int rta = 1;
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			dbWorker oraResp = new dbWorker();
			
			//ROLLBACK
			report.AddLine("Ejecutamos el rollback de la modificacion de la cuenta");
			System.out.println("Ejecutamos el rollback de la modificacion de la cuenta \r\n");
			
			rta = oraResp.oraUpdate(report, "UPDATE PERSONAS \r\n"
					+ "SET ID_TIPO_DOCUMENTO = 1, SEXO = 'M',ESTADO_CIVIL = 0,FECHA_NACIMIENTO = to_date('29/08/1988 00:00:00','DD/MM/RRRR HH24:MI:SS'),\r\n"
					+ "MAIL = 'mauroaramirez88@gmail.com', APELLIDO = 'Ramirez',NOMBRE = 'Mauro',ID_PAIS_NACIMIENTO = 32\r\n"
					+ "WHERE ID_PERSONA = (SELECT ID_PERSONA FROM SOCIOS WHERE ID_CUENTA = "+ ID_CUENTA +")", configEntidad);
			
			
			if (rta > 0) {
				report.AddLine("Rollback exitoso! Update realizado correctamente a la cuenta");
				System.out.println("##[section] : Rollback exitoso! Update realizado correctamente a la cuenta \r\n");
				
				
			}	else {
				report.AddLineAssertionError("Error. No se realizo el update de la modificacion de la cuenta");
				System.out.println("##[warning] : Error. No se realizo el update de la modificacion de la cuenta\r\n");

			}

			rta = oraResp.oraUpdate(report, "UPDATE DOMICILIOS\r\n"
					+ "SET ID_PAIS = 32, ID_PROVINCIA = 2, ID_CODIGO_POSTAL = 1722, DIRECCION = 'Rioja', NUMERO = 956, PISO = 9, DEPTO = 'B', ENTRE_CALLES = 'Tucuman y Av Balvin', \r\n"
					+ "REFERENCIA = 'Edificio color amarillo', TELEFONO = '+541141615646', BARRIO = 'Merlo Centro', LOCALIDAD = 'Merlo'\r\n"
					+ "WHERE ID_DOMICILIO IN ((SELECT ID_DOMICILIO_LEGAL FROM PERSONAS WHERE ID_PERSONA = (SELECT ID_PERSONA FROM SOCIOS WHERE ID_CUENTA = " + ID_CUENTA + ")), (SELECT ID_DOMICILIO_CORRESPONDENCIA FROM CUENTAS WHERE ID_CUENTA = " + ID_CUENTA + "))\r\n", configEntidad);

			if (rta > 0) {
				report.AddLine("Rollback exitoso! El update de cambio de domicilio se ejecuto correctamente");
				System.out.println("##[section] : Rollback exitoso! El update de cambio de domicilio se ejecuto correctamente\r\n");
				
				
			}	else {
				report.AddLineAssertionError("Error. No se realizo el update del domicilio");
				System.out.println("##[warning] : Error. No se realizo el update del domicilio\r\n");
				result = false;
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