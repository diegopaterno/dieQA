package TestCases.GlobalBatch;

import org.openqa.selenium.WebDriver;
import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.Acceso;
import Pasos.Generales.GBatch;
import Pasos.Generales.NavSuperiorBatch;
import Pasos.Generales.PrePostCondi;
import Pasos.GlobalBatch.TC_04_BA_PASOS;
import Repositories.Repo_WebRepository;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;

public class TC_04_WEB_BATCH {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad) {
		//validation 
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_04_WEB_BA\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_04_BA_PASOS tc = new TC_04_BA_PASOS();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			GBatch batch = new GBatch();
			
			// SET VARIABLES
			// Verificacion.
			int rtaPrePostCondi = 0;
			boolean respuestaVerificacion;
			String[] rtaParam = new String[2];
			String rtaAut = "";
			String verifQuery = "";
			String verifQueryParam = "";
			String queryInsertOUT_T3022 = "";
			String ID_ENTIDAD = JsonPath.from(configEntidad).get("ENTIDAD.id_entidad");			
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCH.url_gbatch");
				
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			
			//PRECONDICION
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");
			
			//Se actualizan todos los registros para que estes procesados
			rtaPrePostCondi = oraResp.oraUpdate(report, "UPDATE OUT_T3022 SET INFORMADO = 1", configEntidad);
			ppCondi.preCondicionBD(report, rtaPrePostCondi);
				
			report.AddLine("Se inserta un registro en la tabla OUT_T3022 para luego ser procesado");
			System.out.println("Se inserta un registro en la tabla OUT_T3022 para luego ser procesado\r\n");
				
			queryInsertOUT_T3022 = "Insert into OUT_T3022 (ID_ENTIDAD,TIPO_REGISTRO,TIPO_OPERACION,ID_SUCURSAL,ID_MONEDA,COMPROBANTE,FECHA_MOVIMIENTO,ID_CUENTA,IMPORTE,CONCEPTO_AJUSTE,FECHA_APLICACION,FECHA_CIERRE,ORIGEN_COBRANZA_AJUSTE,NRO_ENVIO,CODIGO_ERROR,DESCRIPCION_ERROR,ID_PRODUCTO,NRO_CAJA,USUARIO,INFORMADO,CANTIDAD_CUOTAS,SOBRE_SALDO,FINANCIABLE,AFECTACION) \r\n"
					+ "values ('" + ID_ENTIDAD + "','2','2','00001','032','0123456789','20220105','0000000049','000000000010021','02105','20220105','20220131','0',null,null,null,'1','0',null,'0','1',null,null,null)";
				
			rtaPrePostCondi = oraResp.oraInsert(report, queryInsertOUT_T3022, configEntidad);
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

			// Tiempo de espera necesario para que se ejecute el proceso y se creen los registros en la BDD
			Thread.sleep(12000);
			
			//ADD VALIDATIONS AT THE END
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
			
			// VERIFICACION ESQUEMA PARAMETRIA
			// Se verifica la cantidad de registros que se procesaron y el estado debe ser 0
			verifQueryParam = "SELECT ID_ESTADO,OBJETOS_PROCESADOS_OK FROM PROCESOS_EJECUCIONES \r\n"
					+ "WHERE ID = (SELECT MAX (ID) FROM PROCESOS_EJECUCIONES WHERE ID_PROCESO = (SELECT ID FROM PROCESOS \r\n"
					+ "WHERE ID_PROCESO_DESCRIPCION = (SELECT ID FROM PROCESOS_DESCRIPCIONES WHERE NOMBRE = 'ProcesoAjustes/Cobranza T3022')\r\n"
					+ "AND ID_ENTIDAD = " + ID_ENTIDAD + "))";
			
			// rtaParam[0] es el ID_ESTADO y rtaParam[1] es OBJETOS_PROCESADOS_OK y se compara con la cantidad de registros que se espera procesar en rtaPrePostCondiciones
			rtaParam = oraResp.oraTwoQueryParam(report, verifQueryParam, configEntidad);
			respuestaVerificacion = batch.verifProcesosEjec(report, null, rtaParam, String.valueOf(rtaPrePostCondi));
			
			if (!respuestaVerificacion) {
				report.AddLineAssertionError("ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA");
				System.out.println("##[warning] : ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA\r\n");
				//Si falla la validacion, falla el TC
				result = false;
			}		
								
			// VERIFICACION ESQUEMA 8118
			verifQuery = "SELECT COUNT (ID_OUT_T3022) FROM OUT_T3022 WHERE ID_OUT_T3022 = (SELECT MAX (ID_OUT_T3022) FROM OUT_T3022 WHERE INFORMADO = 1 AND COMPROBANTE = '0123456789')";
			rtaAut = oraResp.oraOneQuery(report, verifQuery, configEntidad);
			
			// "rtaPrePostCondiciones" y COUNT de OUT_T3022 tiene que ser iguales
			respuestaVerificacion = batch.verifCantRegistros(report, null, String.valueOf(rtaPrePostCondi), rtaAut);
			
			if (!respuestaVerificacion) {
				report.AddLineAssertionError("ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA");
				System.out.println("##[warning] : ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA\r\n");
				//Si falla la validacion, falla el TC
				result = false;
			}
					
			// POSTCONDICION ESQUEMA DE ENTIDAD
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
			
			rtaPrePostCondi = oraResp.oraDelete(report, "DELETE OUT_T3022 WHERE ID_OUT_T3022 = (SELECT MAX (ID_OUT_T3022) FROM OUT_T3022 WHERE INFORMADO = 1 AND COMPROBANTE = '0123456789')", configEntidad);
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
			
			
			// POSTCONDICION ESQUEMA DE ENTIDAD
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
						
			rtaPrePostCondi = oraResp.oraDelete(report, "DELETE OUT_T3022 WHERE ID_OUT_T3022 = (SELECT MAX (ID_OUT_T3022) FROM OUT_T3022 WHERE INFORMADO = 1 AND COMPROBANTE = '0123456789')", configEntidad);
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









