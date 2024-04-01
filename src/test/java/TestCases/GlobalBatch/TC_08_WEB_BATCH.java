package TestCases.GlobalBatch;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;
import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.Acceso;
import Pasos.Generales.GBatch;
import Pasos.Generales.NavSuperiorBatch;
import Pasos.Generales.PrePostCondi;
import Pasos.GlobalBatch.TC_08_BA_PASOS;
import Repositories.Repo_WebRepository;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;

public class TC_08_WEB_BATCH {
	WebDriver driver;


	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad, String cuentas_generales) {
		//validation 
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_08_WEB_BATCH\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_08_BA_PASOS tc = new TC_08_BA_PASOS();
			dbWorker oraResp = new dbWorker();
			GBatch batch = new GBatch();
			PrePostCondi ppCondi = new PrePostCondi();
			
			// SET VARIABLES
			String dni = "33037194";
			
			//String filehash = "A94F833B25CD3B80C6031CCC83BCE9592DE5C6B646419057CBA12F254B793CDC";
			
			// Verificacion param de procesamiento de registros
			String registrosOK = "1"; // Cantidad de registros correctos esperados
			String[] rtaParam;
			
			// Verficacion
			String verifQuery = "";
			String rtaAut = "";
			String querySelecCuenta = "";
			String nroCuenta = "";
			String FILEHASH = "";
			String PROCESOS_EJECUCIONES_FH = "";
			int rtaPROCESOS_EJECUCIONES_FH = 0;//hola
			boolean borradoOK = false;
			boolean respuestaVerificacion;
			String ID_ENTIDAD = JsonPath.from(configEntidad).get("ENTIDAD.id_entidad");	
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCH.url_gbatch");
			
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			
			// PRECONDICIONES	
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");
			
			// ESQUEMA DE LA ENTIDAD
			// Seleccionamos el numero de cuenta con el DNI
			querySelecCuenta = "select c.id_cuenta\r\n"
					+ "from personas p\r\n"
					+ "inner join socios s on s.id_persona = p.id_persona\r\n"
					+ "inner join cuentas c on s.id_cuenta = c.id_cuenta\r\n"
					+ "inner join tarjetas t on c.id_cuenta = t.id_cuenta\r\n"
					+ "inner join tarjetas_historial th on th.id_tarjeta = t.id_tarjeta\r\n"
					+ "where p.nro_documento = " + dni;
			
			// Almacenamos el numero de cuenta del dni
			nroCuenta = oraResp.oraOneQuery(report, querySelecCuenta, configEntidad);
			
			// En el caso que encuentre un nro de cuenta asosiado al DNI se procede a correr el borrado
			if (nroCuenta != "") {
				
				report.AddLine("El DNI: " + dni + " tiene asociada la cuenta numero: " + nroCuenta);
				System.out.println("El DNI: " + dni + " tiene asociada la cuenta numero: " + nroCuenta + "\r\n");
				
				report.AddLine("Se procede a borrar la cuenta Nro: " + nroCuenta);
				System.out.println("Se procede a borrar la cuenta Nro: " + nroCuenta + "\r\n");
				
			// Se borra la cuenta y devuelve como resultado si todas las querys de borrado se ejecutaron correctamente
				borradoOK = oraResp.oraBorrarCuenta(report, nroCuenta, dni, configEntidad, cuentas_generales);
				
				if (borradoOK == true) {
					report.AddLine("Eliminacion exitosa de cuenta");
					System.out.println("##[section] : Eliminacion exitosa de cuenta \r\n");
				} else {
					report.AddLine("Eliminacion de cuenta con errores! (X)");
					System.out.println("##[warning] : Eliminacion de cuenta con errores! (X) \r\n");
				}

			} else {
				report.AddLine("El DNI: " + dni + " No tiene asociada ninguna cuenta.");
				System.out.println("##[warning] : El DNI: " + dni + " No tiene asociada ninguna cuenta.\r\n");
			}
				
			// ESQUEMA PARAM	
			
			FILEHASH = oraResp.oraOneQueryParam(report, "SELECT FILEHASH \r\n"
					+ "FROM PROCESOS_EJECUCIONES_FH \r\n"
					+ "WHERE ID_PROCESO_EJECUCION = (SELECT MAX(ID_PROCESO_EJECUCION) FROM PROCESOS_EJECUCIONES_FH)\r\n"
					+ "AND NOMBRE_ARCHIVO IN ('R4001D_20211014_10.TXT')", configEntidad);
			ppCondi.preCondicionBD(report, FILEHASH);
			
			PROCESOS_EJECUCIONES_FH = "DELETE PROCESOS_EJECUCIONES_FH WHERE FILEHASH = "+ "'" + FILEHASH + "'";
			rtaPROCESOS_EJECUCIONES_FH = oraResp.oraDeleteParam(report, PROCESOS_EJECUCIONES_FH, configEntidad);
			
			if (rtaPROCESOS_EJECUCIONES_FH != 0) {
				report.AddLine("PROCESOS_EJECUCIONES_FH borrado exitoso.");
				System.out.println("##[section] : PROCESOS_EJECUCIONES_FH borrado exitoso.\r\n");
			} else {
				report.AddLine("PROCESOS_EJECUCIONES_FH sin registros para borrar con el Filehash = " + FILEHASH);
				System.out.println("##[warning] : PROCESOS_EJECUCIONES_FH sin registros para borrar con el Filehash = : " + FILEHASH + "\r\n");
			}	
			
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
			Thread.sleep(22000);					
			
			// VERIFICACIONES EN EL ESQUEMA PARAMETRIA
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
				
			// Validaciones PROCESOS_EJECUCIONES
			verifQuery = "SELECT ID_ESTADO,OBJETOS_PROCESADOS_OK FROM PROCESOS_EJECUCIONES \r\n"
					+ "WHERE ID = (SELECT MAX (ID) FROM PROCESOS_EJECUCIONES WHERE ID_PROCESO = (SELECT ID FROM PROCESOS \r\n"
					+ "WHERE ID_PROCESO_DESCRIPCION = (SELECT ID FROM PROCESOS_DESCRIPCIONES WHERE NOMBRE = 'Novedades Socios - R4001')\r\n"
					+ "AND ID_ENTIDAD = " + ID_ENTIDAD + "))";
			rtaParam = oraResp.oraTwoQueryParam(report, verifQuery, configEntidad);
						
			respuestaVerificacion = batch.verifProcesosEjec(report, null, rtaParam, registrosOK);
			
			if (!respuestaVerificacion) {
				report.AddLineAssertionError("ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA");
				System.out.println("##[warning] : ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA\r\n");
				result = false;
			}
						
			// VERIFICACIONES EN EL ESQUEMA DE LA ENTIDAD			
			// Se verifica si se creo la cuenta correctamente
			verifQuery = "select count (c.id_cuenta) cuenta_creada\r\n"
					+ "from personas p\r\n"
					+ "inner join socios s on s.id_persona = p.id_persona\r\n"
					+ "inner join cuentas c on s.id_cuenta = c.id_cuenta\r\n"
					+ "inner join tarjetas t on c.id_cuenta = t.id_cuenta\r\n"
					+ "inner join tarjetas_historial th on th.id_tarjeta = t.id_tarjeta\r\n"
					+ "where p.nro_documento = " + dni;
			
			rtaAut = oraResp.oraOneQuery(report, verifQuery, configEntidad);
						
			if (rtaAut.equals("0")) {
				report.AddLineAssertionError("ERROR: No se creo ninguna cuenta asosiada al DNI: " + dni );
				System.out.println("##[warning] : ERROR:No se creo ninguna cuenta asosiada al DNI: " + dni + "\r\n");
				result = false;
			} else {
				report.AddLine("VALIDACION DE CREACION DE CUENTA EXITOSA CON DNI: " + dni);
				System.out.println("##[section] : VALIDACION DE CREACION DE CUENTA EXITOSA CON DNI: " + dni + "\r\n");
			}			
						
			// POSTCONDICIONES
			// ESQUEMA DE LA ENTIDAD			
			// Seleccionamos el numero de cuenta con el DNI
			querySelecCuenta = "select c.id_cuenta\r\n"
					+ "from personas p\r\n"
					+ "inner join socios s on s.id_persona = p.id_persona\r\n"
					+ "inner join cuentas c on s.id_cuenta = c.id_cuenta\r\n"
					+ "inner join tarjetas t on c.id_cuenta = t.id_cuenta\r\n"
					+ "inner join tarjetas_historial th on th.id_tarjeta = t.id_tarjeta\r\n"
					+ "where p.nro_documento = " + dni;
			
			// Almacenamos el numero de cuenta del dni
			nroCuenta = oraResp.oraOneQuery(report, querySelecCuenta, configEntidad);
			
			report.AddLine("Se realiza rollback de la cuenta creada.");
			System.out.println("Se realiza rollback de la cuenta creada.\r\n");
			
			// En el caso que encuentre un nro de cuenta asosiado al DNI se procede a correr el borrado
			if (nroCuenta != "") {
				
				report.AddLine("El DNI: " + dni + " tiene asociada la cuenta numero: " + nroCuenta);
				System.out.println("El DNI: " + dni + " tiene asociada la cuenta numero: " + nroCuenta + "\r\n");
				
				report.AddLine("Se procede a borrar la cuenta Nro: " + nroCuenta);
				System.out.println("Se procede a borrar la cuenta Nro: " + nroCuenta + "\r\n");
				
				// Se borra la cuenta y devuelve como resultado si todas las querys de borrado se ejecutaron correctamente
				borradoOK = oraResp.oraBorrarCuenta(report, nroCuenta, dni, configEntidad, cuentas_generales);
				
				if (borradoOK == true) {
					report.AddLine("Eliminacion exitosa de cuenta");
					System.out.println("##[section] : Eliminacion exitosa de cuenta\r\n");
				} else {
					report.AddLine("Eliminacion de cuenta con errores! (X)");
					System.out.println("##[warning] : Eliminacion de cuenta con errores! (X)\r\n");
				}
			} else {
				report.AddLine("El DNI: " + dni + " No tiene asociada ninguna cuenta.");
				System.out.println("##[warning] : El DNI: " + dni + " No tiene asociada ninguna cuenta.\r\n");
			}
			
			//POSTCONDICIONES ESQUEMA PARAM
			report.AddLine("Se realiza rollback de la tabla PROCESOS_EJECUCIONES_FH.");
			System.out.println("Se realiza rollback de la tabla PROCESOS_EJECUCIONES_FH.\r\n");
			
			FILEHASH = oraResp.oraOneQueryParam(report, "SELECT FILEHASH \r\n"
					+ "FROM PROCESOS_EJECUCIONES_FH \r\n"
					+ "WHERE ID_PROCESO_EJECUCION = (SELECT MAX(ID_PROCESO_EJECUCION) FROM PROCESOS_EJECUCIONES_FH)\r\n"
					+ "AND NOMBRE_ARCHIVO IN ('R4001D_20211014_10.TXT')", configEntidad);			
			
			PROCESOS_EJECUCIONES_FH = "DELETE PROCESOS_EJECUCIONES_FH WHERE FILEHASH = "+ "'" + FILEHASH + "'";
			rtaPROCESOS_EJECUCIONES_FH = oraResp.oraDeleteParam(report, PROCESOS_EJECUCIONES_FH, configEntidad);
			
			if (rtaPROCESOS_EJECUCIONES_FH != 0) {
				report.AddLine("Registro en la tabla PROCESOS_EJECUCIONES_FH se borro correctamente.");
				System.out.println("##[section] : Registro en la tabla PROCESOS_EJECUCIONES_FH se borro correctamente.\r\n");
			} else {
				report.AddLine("PROCESOS_EJECUCIONES_FH sin registros borrados. FH: " + FILEHASH);
				System.out.println("##[warning] : PROCESOS_EJECUCIONES_FH sin registros borrados. FH: " + FILEHASH + "\r\n");
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

	private void WaitTime(int i) {
		try
		{
			Thread.sleep(i);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	} 
}