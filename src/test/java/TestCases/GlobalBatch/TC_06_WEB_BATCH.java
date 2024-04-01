package TestCases.GlobalBatch;

import org.openqa.selenium.WebDriver;
import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.Acceso;
import Pasos.Generales.GBatch;
import Pasos.Generales.NavSuperiorBatch;
import Pasos.Generales.PrePostCondi;
import Pasos.GlobalBatch.TC_06_BA_PASOS;
import Repositories.Repo_WebRepository;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;

public class TC_06_WEB_BATCH {
	WebDriver driver;
	
	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_06_WEB_BATCH\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_06_BA_PASOS tc = new TC_06_BA_PASOS();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			GBatch batch = new GBatch();
			
			// SET VARIABLES
			// Verificacion.
			String[] rtaParam = new String[2];
			String registrosOKParam = "3"; // Cantidad de registros correctos esperados en PARAM
			String registrosOK_IPM_FILE = "1"; // Cantidad de registros correctos esperados en IPM_FILE
			String registrosOK_IPM = "3"; // Cantidad de registros correctos esperados en IPM
			String rtaAut = "";
			String verifQuery = "";
			String ID_ENTIDAD = JsonPath.from(configEntidad).get("ENTIDAD.id_entidad");
			int rtaPrePostCondi = 0;
			boolean respuestaVerificacion;
			
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCH.url_gbatch");

			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			
			// PRECONDICION
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");
			
			report.AddLine("Se eliminan los registros de las tablas IPM e IPM_FILE con el FILE_ID: 0022108040000001180300001");
			System.out.println("Se eliminan los registros de las tablas IPM e IPM_FILE con el FILE_ID: 0022108040000001180300001\r\n");
			
			rtaPrePostCondi = oraResp.oraDelete(report, "DELETE IPM WHERE ID_IPM_FILE = (SELECT ID_IPM_FILE FROM IPM_FILE WHERE FILE_ID = 0022108040000001180300001)", configEntidad);
			ppCondi.preCondicionBD(report, rtaPrePostCondi);
			
			rtaPrePostCondi = oraResp.oraDelete(report, "DELETE IPM_FILE WHERE FILE_ID = 0022108040000001180300001", configEntidad);		
			ppCondi.preCondicionBD(report, rtaPrePostCondi);
			
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			
			//ADD THE STEPS BELOW
			
			// Login a la pagina principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch\r\n");
			acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			
			// Hacemos click en procesos
			nav.procesos(data, report, DM, iteration, name, repo);
			
			// Primera pagina del test
			tc.pagina1(data, report, DM, iteration, name, repo, configEntidad);

			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);

			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);
			
			// Tiempo necesario para que se ejecute el proceso y se creen los registros en la BDD
			Thread.sleep(18000);
						
			// VERIFICACION ESQUEMA PARAMETRIA
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
			
			verifQuery = "SELECT ID_ESTADO,OBJETOS_PROCESADOS_OK FROM PROCESOS_EJECUCIONES \r\n"
					+ "WHERE ID = (SELECT MAX (ID) FROM PROCESOS_EJECUCIONES WHERE ID_PROCESO = (SELECT ID FROM PROCESOS \r\n"
					+ "WHERE ID_PROCESO_DESCRIPCION = (SELECT ID FROM PROCESOS_DESCRIPCIONES WHERE NOMBRE = 'IPM Entrante Emisor')\r\n"
					+ "AND ID_ENTIDAD = " + ID_ENTIDAD + "))";
			
			// rtaParam[0] es el ID_ESTADO y rtaParam[1] es OBJETOS_PROCESADOS_OK
			rtaParam = oraResp.oraTwoQueryParam(report, verifQuery, configEntidad);
			
			respuestaVerificacion = batch.verifProcesosEjec(report, null, rtaParam, registrosOKParam);
			
			if (!respuestaVerificacion) {
				report.AddLineAssertionError("ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA");
				System.out.println("##[warning] : ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA\r\n");
				result = false;
			}
			
			// VERIFICACION ESQUEMA DE LA ENTIDA
		
			verifQuery = "SELECT COUNT(*) FROM IPM_FILE WHERE FILE_ID = 0022108040000001180300001";
			rtaAut = oraResp.oraOneQuery(report, verifQuery, configEntidad);
					
			//VALIDACION DE IPM_FILES (Debe poseer 1 registro)
			respuestaVerificacion = batch.verifCantRegistros(report, null, registrosOK_IPM_FILE, rtaAut);
			
			if (!respuestaVerificacion) {
				report.AddLineAssertionError("ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA");
				System.out.println("##[warning] : ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA\r\n");
				result = false;
			}
			
			// "OBJETOS_PROCESADOS_OK" y COUNT de IPM tiene que ser iguales (3 registros)
			verifQuery = "SELECT COUNT(*) FROM IPM WHERE ID_IPM_FILE = (SELECT ID_IPM_FILE FROM IPM_FILE WHERE FILE_ID = 0022108040000001180300001)";
			rtaAut = oraResp.oraOneQuery(report, verifQuery, configEntidad);
			
			respuestaVerificacion = batch.verifCantRegistros(report, null, registrosOK_IPM, rtaAut);
			
			if (!respuestaVerificacion) {
				report.AddLineAssertionError("ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA");
				System.out.println("##[warning] : ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA\r\n");
				result = false;
			}
						
			// POSTCONDICION
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
			
			//Se borran los registros en las tablas IPM e IPM_FILE para que no falle la proxima ejecicion
			rtaPrePostCondi = oraResp.oraDelete(report, "DELETE IPM WHERE ID_IPM_FILE = (SELECT ID_IPM_FILE FROM IPM_FILE WHERE FILE_ID = 0022108040000001180300001)", configEntidad);
			ppCondi.postCondicionBD(report, rtaPrePostCondi);
			
			rtaPrePostCondi = oraResp.oraDelete(report, "DELETE IPM_FILE WHERE FILE_ID = 0022108040000001180300001", configEntidad);		
			ppCondi.postCondicionBD(report, rtaPrePostCondi);
			
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
			
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
			
			// POSTCONDICION
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
						
			//Se borran los registros en las tablas IPM e IPM_FILE para que no falle la proxima ejecicion
			rtaPrePostCondi = oraResp.oraDelete(report, "DELETE IPM WHERE ID_IPM_FILE = (SELECT ID_IPM_FILE FROM IPM_FILE WHERE FILE_ID = 0022108040000001180300001)", configEntidad);
			ppCondi.postCondicionBD(report, rtaPrePostCondi);
						
			rtaPrePostCondi = oraResp.oraDelete(report, "DELETE IPM_FILE WHERE FILE_ID = 0022108040000001180300001", configEntidad);		
			ppCondi.postCondicionBD(report, rtaPrePostCondi);
			
			
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