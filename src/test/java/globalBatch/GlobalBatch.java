package globalBatch;

import org.openqa.selenium.WebDriver;

import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.Acceso;
import Pasos.Generales.NavSuperiorBatch;
import Pasos.GlobalBatch.TC_07_BA_PASOS;
import Repositories.Repo_WebRepository;
import io.restassured.path.json.JsonPath;

public class GlobalBatch {

	private String esquema;
	WebDriver driver;
	public boolean procesoT1001UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();
			
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCHUAT.url_gbatch");
			
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");

			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);

			//ADD THE STEPS BELOW
			
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatchUAT(data, report, DM, iteration, name, repo, configEntidad);
			
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);
					
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);
			
			// Primera pagina del test
			tc.procesoT1001UAT(data, report, DM, iteration, name, repo, configEntidad);
			
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);

			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);
			
			Thread.sleep(18000);
					
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
	
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");

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
	
	public boolean procesoT1010UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCHUAT.url_gbatch");		
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			//ADD THE STEPS BELOW
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatchUAT(data, report, DM, iteration, name, repo, configEntidad);		
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);				
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);		
			// Primera pagina del test
			tc.procesoT1010UAT(data, report, DM, iteration, name, repo, configEntidad);		
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);
			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);	
			Thread.sleep(18000);				
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
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
	public boolean procesoT1027UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCHUAT.url_gbatch");		
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			//ADD THE STEPS BELOW
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatchUAT(data, report, DM, iteration, name, repo, configEntidad);		
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);				
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);		
			// Primera pagina del test
			tc.procesoT1027UAT(data, report, DM, iteration, name, repo, configEntidad);		
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);
			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);	
			Thread.sleep(18000);				
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
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
	public boolean procesoT7001UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCHUAT.url_gbatch");		
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			//ADD THE STEPS BELOW
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatchUAT(data, report, DM, iteration, name, repo, configEntidad);		
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);				
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);		
			// Primera pagina del test
			tc.procesoT7001UAT(data, report, DM, iteration, name, repo, configEntidad);		
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);
			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);	
			Thread.sleep(18000);				
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
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
	public boolean procesoT2001UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCHUAT.url_gbatch");		
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			//ADD THE STEPS BELOW
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatchUAT(data, report, DM, iteration, name, repo, configEntidad);		
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);				
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);		
			// Primera pagina del test
			tc.procesoT2001UAT(data, report, DM, iteration, name, repo, configEntidad);		
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);
			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);	
			Thread.sleep(18000);				
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
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
	public boolean procesoT3022UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCHUAT.url_gbatch");		
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			//ADD THE STEPS BELOW
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatchUAT(data, report, DM, iteration, name, repo, configEntidad);		
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);				
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);		
			// Primera pagina del test
			tc.procesoT3022UAT(data, report, DM, iteration, name, repo, configEntidad);		
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);
			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);	
			Thread.sleep(18000);				
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
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
	public boolean procesoT2000UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCHUAT.url_gbatch");		
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			//ADD THE STEPS BELOW
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatchUAT(data, report, DM, iteration, name, repo, configEntidad);		
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);				
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);		
			// Primera pagina del test
			tc.procesoT2000UAT(data, report, DM, iteration, name, repo, configEntidad);		
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);
			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);	
			Thread.sleep(18000);				
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
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
	public boolean procesoT3052UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {

		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCHUAT.url_gbatch");		
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			//ADD THE STEPS BELOW
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatchUAT(data, report, DM, iteration, name, repo, configEntidad);		
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);				
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);		
			// Primera pagina del test
			tc.procesoT3052UAT(data, report, DM, iteration, name, repo, configEntidad);		
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);
			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);	
			Thread.sleep(18000);				
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
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
	public boolean procesoT1025UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {

		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCHUAT.url_gbatch");		
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			//ADD THE STEPS BELOW
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatchUAT(data, report, DM, iteration, name, repo, configEntidad);		
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);				
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);		
			// Primera pagina del test
			tc.procesoT1025UAT(data, report, DM, iteration, name, repo, configEntidad);		
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);
			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);	
			Thread.sleep(18000);				
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
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
	
	/**********************************QA********************************************************/
	public boolean procesoT1001QA(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();
			
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCH.url_gbatch");
			
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");

			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);

			//ADD THE STEPS BELOW
			
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);
					
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);
			
			// Primera pagina del test
			tc.procesoT1001QA(data, report, DM, iteration, name, repo, configEntidad);
			
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);

			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);
			
			Thread.sleep(18000);
					
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
	
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");

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
	public boolean procesoT1010QA(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCH.url_gbatch");		
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			//ADD THE STEPS BELOW
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);		
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);				
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);		
			// Primera pagina del test
			tc.procesoT1010QA(data, report, DM, iteration, name, repo, configEntidad);		
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);
			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);	
			Thread.sleep(18000);				
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
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
	public boolean procesoT1027QA(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCH.url_gbatch");		
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			//ADD THE STEPS BELOW
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);		
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);				
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);		
			// Primera pagina del test
			tc.procesoT1027QA(data, report, DM, iteration, name, repo, configEntidad);		
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);
			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);	
			Thread.sleep(18000);				
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
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
	public boolean procesoT7001QA(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCH.url_gbatch");		
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			//ADD THE STEPS BELOW
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);		
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);				
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);		
			// Primera pagina del test
			tc.procesoT7001QA(data, report, DM, iteration, name, repo, configEntidad);		
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);
			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);	
			Thread.sleep(18000);				
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
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
	public boolean procesoT2001QA(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCH.url_gbatch");		
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			//ADD THE STEPS BELOW
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);		
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);				
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);		
			// Primera pagina del test
			tc.procesoT2001QA(data, report, DM, iteration, name, repo, configEntidad);		
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);
			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);	
			Thread.sleep(18000);				
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
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
	public boolean procesoT3022QA(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCH.url_gbatch");		
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			//ADD THE STEPS BELOW
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);		
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);				
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);		
			// Primera pagina del test
			tc.procesoT3022QA(data, report, DM, iteration, name, repo, configEntidad);		
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);
			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);	
			Thread.sleep(18000);				
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
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
	public boolean procesoT2000QA(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCH.url_gbatch");		
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			//ADD THE STEPS BELOW
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);		
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);				
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);		
			// Primera pagina del test
			tc.procesoT2000QA(data, report, DM, iteration, name, repo, configEntidad);		
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);
			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);	
			Thread.sleep(18000);				
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
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
	public boolean procesoT3052QA(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {			
			System.out.println("Configuring");
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCH.url_gbatch");		
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			//ADD THE STEPS BELOW
			// Login a la página principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch");
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);		
			// Se prueba la navegacion por los menues
			// Ruta "/"
			nav.inicio(data, report, DM, iteration, name, repo);				
			// Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);		
			// Primera pagina del test
			tc.procesoT3052QA(data, report, DM, iteration, name, repo, configEntidad);		
			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);
			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);	
			Thread.sleep(18000);				
			// Cerramos la sesion
			//acceso.logoutBA(data, report, DM, iteration, name, repo);
			//ADD VALIDATIONS AT THE END
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
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
	/****************************************FIN BATCH QA****************************************************/
public boolean procesoT1001UAT2 (Datasources data, Reports report,DriverManager DM,int iteration,String name, String configEntidad, String entidad) {
	System.out.println("\r\n##################################################################################################################################################################################################################"
			+ "##################################################################################################################################################################################################################\r\n");
	System.out.println("Configuring "+name+ "\r\n");
	//Set Variables
			boolean result = true;
			esquema = JsonPath.from(configEntidad).getString("ENTIDAD.esquema_db");
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCHUAT.url_gbatch");
			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);
			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);

			report.SetDriver(driver);
			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_07_BA_PASOS tc = new TC_07_BA_PASOS();
					
			try {
				driver.get(URL_GBATCH);
				
				acceso.loginUsernameBatchUAT(data, report, DM, iteration, name, repo, configEntidad);
				report.AddLine("Acceso a la pagina de Global Batch");
				System.out.println("Acceso a la pagina de Global Batch\r\n");
				
				nav.procesos(data, report, DM, iteration, name, repo);
				
				// Primera pagina del test
				tc.pagina1LemonUAT(data, report, DM, iteration, name, repo, configEntidad);

				// Segunda pagina
				tc.pagina2(data, report, DM, iteration, name, repo);

				// Tercera pagina
				tc.pagina3(data, report, DM, iteration, name, repo);
				
				Thread.sleep(18000);
			}
			catch(Exception e) {
				report.AddLine("Error:<br>" + e);
				System.out.println("##[warning] : Error:\r\n" + e);
				result = false;
			}
	return result;
}

}
