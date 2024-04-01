package TestCases.GlobalBatch;

import org.openqa.selenium.WebDriver;
import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.Acceso;
import Pasos.Generales.GBatch;
import Pasos.Generales.NavSuperiorBatch;
import Pasos.Generales.PrePostCondi;
import Pasos.GlobalBatch.TC_05_BA_PASOS;
import Repositories.Repo_WebRepository;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;

public class TC_05_WEB_BATCH {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad, String cuentas_generales) {
		//validation 
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println( "Configuring TC_05_WEB_BA\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_05_BA_PASOS tc = new TC_05_BA_PASOS();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			GBatch batch = new GBatch();
			
			// SET VARIABLES
			// Verificacion.
			String[] rtaParam = new String[2];
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_2.cuenta_fisica");
			String ID_ENTIDAD = JsonPath.from(configEntidad).get("ENTIDAD.id_entidad");
			String ID_GRUPO_AFINIDAD = "";
			String ID_PRODUCTO = "";
			String ID_SUCURSAL = "";
			String registrosOK = "1"; // Cantidad de registros correctos esperados
			String rtaAut = "";
			String verifQuery = "";
			int rtaPrePostCondi = 0;
			boolean respuestaVerificacion;
			
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCH.url_gbatch");
			
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
	
			//PRECONDICION ESQUEMA DE LA ENTIDAD
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");
			
			//Se setea el ID_ESTADO de la tarjeta en 2 (PENDIENTE_EMBOZADO) para que lo tome el proceso
			rtaPrePostCondi = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 2 WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			ppCondi.preCondicionBD(report, rtaPrePostCondi);
			
			//Se obtiene el ID_SUCURSAL de la cuenta para luego utilizarlo al lanzar el proceso 
			report.AddLine("Se obtiene el ID_SUCURSAL, ID_PRODUCTO y ID_GRUPO_AFINIDAD de la cuenta " +  ID_CUENTA + " para luego utilizarlo al lanzar el proceso");
			System.out.println("Se obtiene el ID_SUCURSAL, ID_PRODUCTO y ID_GRUPO_AFINIDAD de la cuenta " +  ID_CUENTA + " para luego utilizarlo al lanzar el proceso\r\n");
			
			ID_SUCURSAL = oraResp.oraOneQuery(report, "SELECT ID_SUCURSAL FROM CUENTAS WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			ppCondi.preCondicionBD(report, ID_SUCURSAL);
			//Se obtiene el ID_PRODUCTO de la cuenta para luego utilizarlo al lanzar el proceso
			ID_PRODUCTO = oraResp.oraOneQuery(report, "SELECT ID_PRODUCTO FROM CUENTAS WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			ppCondi.preCondicionBD(report, ID_PRODUCTO);
			//Se obtiene el ID_GRUPO_AFINIDAD de la cuenta para luego utilizarlo al lanzar el proceso
			ID_GRUPO_AFINIDAD = oraResp.oraOneQuery(report, "SELECT ID_GRUPO_AFINIDAD FROM CUENTAS WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			ppCondi.preCondicionBD(report, ID_GRUPO_AFINIDAD);
			
			report.AddLine("Se obtuvo los siguientes ID de la cuenta " + ID_CUENTA + ": ID_SUCURSAL = " + ID_GRUPO_AFINIDAD + " - ID_PRODUCTO = " +  ID_PRODUCTO + " - ID_GRUPO_AFINIDAD = " + ID_GRUPO_AFINIDAD);
			System.out.println("Se obtuvo los siguientes ID de la cuenta " + ID_CUENTA + ": ID_SUCURSAL = " + ID_GRUPO_AFINIDAD + " - ID_PRODUCTO = " +  ID_PRODUCTO + " - ID_GRUPO_AFINIDAD = " + ID_GRUPO_AFINIDAD + "\r\n");
			
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
			tc.pagina2(data, report, DM, iteration, name, repo, ID_GRUPO_AFINIDAD, ID_PRODUCTO, ID_SUCURSAL);

			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);
			
			// Tiempo necesario para que se ejecute el proceso y se creen los registros en la BDD
			// Por el momento se deja bastante tiempo de espera ya que el proceso es muy irregular en cuanto a tiempos de procesamiento
			Thread.sleep(20000);			
			
			//ADD VALIDATIONS AT THE END
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
			
			// VERIFICACION ESQUEMA PARAMETRIA			
			verifQuery = "SELECT ID_ESTADO,OBJETOS_PROCESADOS_OK FROM PROCESOS_EJECUCIONES \r\n"
					+ "WHERE ID = (SELECT MAX (ID) FROM PROCESOS_EJECUCIONES WHERE ID_PROCESO = (SELECT ID FROM PROCESOS \r\n"
					+ "WHERE ID_PROCESO_DESCRIPCION = (SELECT ID FROM PROCESOS_DESCRIPCIONES WHERE NOMBRE = 'Embozado Tarjetas')\r\n"
					+ "AND ID_ENTIDAD = " + ID_ENTIDAD + "))";
			
			// rtaParam[0] es el ID_ESTADO y rtaParam[1] es OBJETOS_PROCESADOS_OK
			rtaParam = oraResp.oraTwoQueryParam(report, verifQuery, configEntidad);
			respuestaVerificacion = batch.verifProcesosEjec(report, null, rtaParam, registrosOK);	
			
			if (!respuestaVerificacion) {
				report.AddLineAssertionError("ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA");
				System.out.println("##[warning] : ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA\r\n");
				result = false;
			}
						
			// VERIFICACION ESQUEMA DE LA ENTIDAD
			Thread.sleep(10000);
			verifQuery = "SELECT COUNT(*) FROM TARJETAS WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_ESTADO = 3";
			
			rtaAut = oraResp.oraOneQuery(report, verifQuery, configEntidad);
			
			// "OBJETOS_PROCESADOS_OK" y COUNT de TARJETAS tiene que ser iguales (2 registros)
			respuestaVerificacion = batch.verifCantRegistros(report, null, registrosOK, rtaAut);	
			
			if (!respuestaVerificacion) {
				report.AddLineAssertionError("ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA");
				System.out.println("##[warning] : ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA\r\n");
				result = false;
			}			
			
			// POSTCONDICION ESQUEMA DE LA ENTIDAD
			report.AddLine("Se realiza rollback de la tabla TARJETAS.");
			System.out.println("Se realiza rollback de la tabla TARJETAS.\r\n");
			
			rtaPrePostCondi = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 2 WHERE ID_CUENTA =" + ID_CUENTA, configEntidad);
			ppCondi.postCondicionBD(report, rtaPrePostCondi);
			
			System.out.println("##[command] :  ------ Finished test: " + name + " ------\r\n");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");			
			
		} catch(Exception e) {
			e.printStackTrace();
			report.AddLineAssertionError(e.getMessage());		
			
            //Se agrega de nuevo ROLLBACK para que no quede la base inconsistente si falla el TestCase  
			
			//SET VARIABLES
			int rtaPrePostCondi = 0;
			PrePostCondi ppCondi = new PrePostCondi();
			dbWorker oraResp = new dbWorker();
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_2.cuenta_fisica");
			
			
			// POSTCONDICION ESQUEMA DE LA ENTIDAD
            report.AddLine("Se realiza rollback de la tabla TARJETAS.");
	        System.out.println("Se realiza rollback de la tabla TARJETAS.\r\n");
						
			rtaPrePostCondi = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 2 WHERE ID_CUENTA =" + ID_CUENTA, configEntidad);
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