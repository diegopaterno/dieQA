package TestCases.GlobalBatch;

import java.time.LocalDate;

import org.apache.poi.util.SystemOutLogger;
import org.mozilla.javascript.regexp.SubString;
import org.openqa.selenium.WebDriver;
import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.Acceso;
import Pasos.Generales.GBatch;
import Pasos.Generales.NavSuperiorBatch;
import Pasos.Generales.PrePostCondi;
import Pasos.GlobalBatch.TC_02_BA_PASOS;
import Repositories.Repo_WebRepository;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;

public class TC_02_WEB_BATCH {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad, String cuentas_generales) {
		
		//validation 
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_02_WEB_BA\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavSuperiorBatch nav = new NavSuperiorBatch();
			TC_02_BA_PASOS tc = new TC_02_BA_PASOS();
			dbWorker oraResp = new dbWorker();
			GBatch batch = new GBatch();
			PrePostCondi ppCondi = new PrePostCondi();
		
			// SET VARIABLES
			// Verificacion.
			String[] rtaParam = new String[2];
					
			// Verificacion que el proceso haya ejecutado correctamente 13 registros.
			// Tienen que ser igual a la cantidad a procesar en la tabla AUTORIZACIONES.
			String cantidadRegistrosEsperados = ""; 
			String verifQuery = "";
			String fechaAutorizacionGB = "";
			String NRO_TARJETA_ENCRIPTADA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.nro_tarjeta_encriptada");
			String NRO_TARJETA_ENMASCARADA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.nro_tarjeta_enmascarada");
			String ID_ENTIDAD = JsonPath.from(configEntidad).get("ENTIDAD.id_entidad");			
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCH.url_gbatch");
			int registrosInsert;
			int respuestaDelete;
			boolean respuestaVerificacion;
			
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			
			//PRECONDICION
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");
			
			//Obtengo la fecha actual
			LocalDate fechaActual = LocalDate.now();
			
			//Grabo la fecha actual como string para luego usarla para lanzar el proceso
			fechaAutorizacionGB = String.valueOf(fechaActual);
			
			//Saco los guiones para poder ser utilizado desde GlobalBatch
			fechaAutorizacionGB = fechaAutorizacionGB.replace("-", "");
			
			report.AddLine("Fecha para lanzar el proceso T7001D en Global Batch: " + fechaAutorizacionGB);
			System.out.println("Fecha para lanzar el proceso T7001D en Global Batch: " + fechaAutorizacionGB + "\r\n");

			//Se inserta 3 registros con fecha SYSDATE para luego ser porcesados por el T7001D
			
			report.AddLine("Se insertan registros para ser procesados: " );
			System.out.println("Se insertan registros para ser procesados\r\n");
			
			registrosInsert = oraResp.oraInsert(report, "Insert into AUTORIZACION (ID_ENTIDAD,NRO_TARJETA,COD_AUTO_ENTIDAD,COD_AUTO_EMISOR,ID_MONEDA_ORIGEN,IMPORTE_ORIGEN,FECHA_AUTORIZACION,COTIZACION,ID_MONEDA,IMPORTE,IMPORTE_SIN_DTO,ID_ESTADO_TIPO,ID_ESTADO,FECHA_ANULACION,ID_USUARIO_ANULACION,ID_COMERCIO,COMERCIO_DESCRIPCION,CUOTAS,TASA_SOCIO,IMPORTE_CUOTAS,MCC,ID_RUBRO,ID_SUBRUBRO,TCC,COD_COMERCIO_EXTERNO,TERMINAL_POS,ID_COD_MOVIMIENTO,ID_GRUPO_TRANSACCION,ID_MODELO_TRANSACCION,ID_AUTORIZACION_ADQUIRENTE,ID_CODIGO_DEVOLUCION,IMPORTE_DEVOLUCION,ID_RESPUESTA_MC,FECHA_RELACION,OBSERVACION,MODO_INGRESO,FECHA_INFORMADA,COD_RESPUESTA,ID_RESPUESTA_INTERNA,ID_MOD_FINANC_COMERCIO,ID_PAIS_EMISOR,COD_PAIS_COMERCIO,COD_PAIS_EMISOR,PRESENTACION_PROCESADA,IMPORTE_TOTAL_PRESENTADO,ID_ORIGEN,COEFICIENTE_DIFERENCIA_CAMBIO,ID_CUENTA,ID_ADICIONAL,ID_MONEDA_LIQUIDACION,IMPORTE_LIQUIDACION,ID_AUTORIZACION_EMISOR,COD_SUB_COMERCIO,TRN_EXTERNAL_ID,ID_REGLA_FRAUDE,ID_MARCA,NRO_TARJETA_ENMASCARADA,DEBITO_AUTOMATICO,PORCENTAJE_DEVOLUCION,ECOMMERCE,TID,COD_RESPUESTA_POS,ES_CASHBACK,ID_AUTORIZACION_ORIGINAL,FECHA_DIFERIMIENTO,IMPORTE_CONVERTIDO,DIAS_DIFERIMIENTO,ES_PROPINA,MONTO_ACUM_DEVOLUCIONES) "
					+ "values ('" + ID_ENTIDAD + "','" + NRO_TARJETA_ENCRIPTADA + "','127198',null,'32','345,98',TRUNC (SYSDATE),'1','32',null,'0','41','1',null,null,null,'Comercio Argentina                   ARG','1',null,'0','7995',null,null,'U','687555537877464','Reg00004','1','1',null,null,null,'0',null,to_date('03/01/2022 00:00:00','DD/MM/RRRR HH24:MI:SS'),'Vencimiento informado difiere del registrado','07',to_date('12/10/2021 11:03:45','DD/MM/RRRR HH24:MI:SS'),'57','20021',null,null,'ARG','ARG','0','0','12','1','31','0','32','345,98','10572',null,null,null,'1','" + NRO_TARJETA_ENMASCARADA + "','0',null,'0','MCC0113NS0824  ',null,'0',null,null,null,null,'0','0')", configEntidad);
			ppCondi.preCondicionBD(report, registrosInsert);
			
			registrosInsert = oraResp.oraInsert(report, "Insert into AUTORIZACION (ID_ENTIDAD,NRO_TARJETA,COD_AUTO_ENTIDAD,COD_AUTO_EMISOR,ID_MONEDA_ORIGEN,IMPORTE_ORIGEN,FECHA_AUTORIZACION,COTIZACION,ID_MONEDA,IMPORTE,IMPORTE_SIN_DTO,ID_ESTADO_TIPO,ID_ESTADO,FECHA_ANULACION,ID_USUARIO_ANULACION,ID_COMERCIO,COMERCIO_DESCRIPCION,CUOTAS,TASA_SOCIO,IMPORTE_CUOTAS,MCC,ID_RUBRO,ID_SUBRUBRO,TCC,COD_COMERCIO_EXTERNO,TERMINAL_POS,ID_COD_MOVIMIENTO,ID_GRUPO_TRANSACCION,ID_MODELO_TRANSACCION,ID_AUTORIZACION_ADQUIRENTE,ID_CODIGO_DEVOLUCION,IMPORTE_DEVOLUCION,ID_RESPUESTA_MC,FECHA_RELACION,OBSERVACION,MODO_INGRESO,FECHA_INFORMADA,COD_RESPUESTA,ID_RESPUESTA_INTERNA,ID_MOD_FINANC_COMERCIO,ID_PAIS_EMISOR,COD_PAIS_COMERCIO,COD_PAIS_EMISOR,PRESENTACION_PROCESADA,IMPORTE_TOTAL_PRESENTADO,ID_ORIGEN,COEFICIENTE_DIFERENCIA_CAMBIO,ID_CUENTA,ID_ADICIONAL,ID_MONEDA_LIQUIDACION,IMPORTE_LIQUIDACION,ID_AUTORIZACION_EMISOR,COD_SUB_COMERCIO,TRN_EXTERNAL_ID,ID_REGLA_FRAUDE,ID_MARCA,NRO_TARJETA_ENMASCARADA,DEBITO_AUTOMATICO,PORCENTAJE_DEVOLUCION,ECOMMERCE,TID,COD_RESPUESTA_POS,ES_CASHBACK,ID_AUTORIZACION_ORIGINAL,FECHA_DIFERIMIENTO,IMPORTE_CONVERTIDO,DIAS_DIFERIMIENTO,ES_PROPINA,MONTO_ACUM_DEVOLUCIONES) "
					+ "values ('" + ID_ENTIDAD + "','" + NRO_TARJETA_ENCRIPTADA + "','370318',null,'32','-345,98',TRUNC (SYSDATE),'1','32','-345,98','0','41','0',null,null,null,'Comercio Argentina                   ARG','1',null,'-345,98','7995',null,null,'U','687555537877464','Reg00004','2','1',null,null,null,'0',null,to_date('03/01/2022 00:00:00','DD/MM/RRRR HH24:MI:SS'),'Operacion aprobada SENTINEL: OK','07',to_date('12/10/2021 11:03:45','DD/MM/RRRR HH24:MI:SS'),'00','20000',null,null,'ARG','ARG','0','0','12','1','21','0','32','-345,98','10577',null,null,null,'1','" + NRO_TARJETA_ENMASCARADA + "','0',null,'0','MCC0113NS0824  ',null,'0',null,null,null,null,'0','0')", configEntidad);
			ppCondi.preCondicionBD(report, registrosInsert);
			
			registrosInsert = oraResp.oraInsert(report, "Insert into AUTORIZACION (ID_ENTIDAD,NRO_TARJETA,COD_AUTO_ENTIDAD,COD_AUTO_EMISOR,ID_MONEDA_ORIGEN,IMPORTE_ORIGEN,FECHA_AUTORIZACION,COTIZACION,ID_MONEDA,IMPORTE,IMPORTE_SIN_DTO,ID_ESTADO_TIPO,ID_ESTADO,FECHA_ANULACION,ID_USUARIO_ANULACION,ID_COMERCIO,COMERCIO_DESCRIPCION,CUOTAS,TASA_SOCIO,IMPORTE_CUOTAS,MCC,ID_RUBRO,ID_SUBRUBRO,TCC,COD_COMERCIO_EXTERNO,TERMINAL_POS,ID_COD_MOVIMIENTO,ID_GRUPO_TRANSACCION,ID_MODELO_TRANSACCION,ID_AUTORIZACION_ADQUIRENTE,ID_CODIGO_DEVOLUCION,IMPORTE_DEVOLUCION,ID_RESPUESTA_MC,FECHA_RELACION,OBSERVACION,MODO_INGRESO,FECHA_INFORMADA,COD_RESPUESTA,ID_RESPUESTA_INTERNA,ID_MOD_FINANC_COMERCIO,ID_PAIS_EMISOR,COD_PAIS_COMERCIO,COD_PAIS_EMISOR,PRESENTACION_PROCESADA,IMPORTE_TOTAL_PRESENTADO,ID_ORIGEN,COEFICIENTE_DIFERENCIA_CAMBIO,ID_CUENTA,ID_ADICIONAL,ID_MONEDA_LIQUIDACION,IMPORTE_LIQUIDACION,ID_AUTORIZACION_EMISOR,COD_SUB_COMERCIO,TRN_EXTERNAL_ID,ID_REGLA_FRAUDE,ID_MARCA,NRO_TARJETA_ENMASCARADA,DEBITO_AUTOMATICO,PORCENTAJE_DEVOLUCION,ECOMMERCE,TID,COD_RESPUESTA_POS,ES_CASHBACK,ID_AUTORIZACION_ORIGINAL,FECHA_DIFERIMIENTO,IMPORTE_CONVERTIDO,DIAS_DIFERIMIENTO,ES_PROPINA,MONTO_ACUM_DEVOLUCIONES) "
					+ "values ('" + ID_ENTIDAD + "','" + NRO_TARJETA_ENCRIPTADA + "','571888',null,'32','15,56',TRUNC (SYSDATE),'1','32','15,56','0','41','2',null,null,null,'Comercio Argentina                   ARG','1',null,'15,56','7995',null,null,'U','687555537877464','Reg00004','1','1',null,null,null,'0',null,null,'Operacion aprobada SENTINEL: OK','90',to_date('12/10/2021 10:38:26','DD/MM/RRRR HH24:MI:SS'),'00','20000',null,null,'ARG','ARG','0','0','12','1','21','0','32','15,56','10586',null,null,null,'1','" + NRO_TARJETA_ENMASCARADA + "','0',null,'0','MCC0113NL1012  ',null,'0',null,null,null,null,'0','0')", configEntidad);
			ppCondi.preCondicionBD(report, registrosInsert);
			
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);
			//ADD THE STEPS BELOW
			
			// Login a la pagina principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch\r\n");
			acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			
			// Hacemos click en Procesos. Ruta "/Proceso"
			nav.procesos(data, report, DM, iteration, name, repo);
			
			// Primera pagina del test
			tc.pagina1(data, report, DM, iteration, name, repo, configEntidad);

			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo, fechaAutorizacionGB);

			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);
			
			//ADD VALIDATIONS AT THE END
			
			//Agregamos tiempo para que la bdd se actualice. 
			Thread.sleep(12000);	
			
			// ---- VERIFICACION ESQUEMA PARAMETRIA ----
			
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
			
			verifQuery = "SELECT ID_ESTADO,OBJETOS_PROCESADOS_OK FROM PROCESOS_EJECUCIONES \r\n"
					+ "WHERE ID = (SELECT MAX (ID) FROM PROCESOS_EJECUCIONES WHERE ID_PROCESO = (SELECT ID FROM PROCESOS \r\n"
					+ "WHERE ID_PROCESO_DESCRIPCION = (SELECT ID FROM PROCESOS_DESCRIPCIONES WHERE NOMBRE = 'Autorizaciones Realizadas T7001D')\r\n"
					+ "AND ID_ENTIDAD = " + ID_ENTIDAD + ") )";
			
			//En el array rtaParam[0] almacena el ID_ESTADO y rtaParam[1] la cantidad de OBJETOS_PROCESADOS_OK
			rtaParam = oraResp.oraTwoQueryParam(report, verifQuery, configEntidad);
			
			cantidadRegistrosEsperados = oraResp.oraOneQuery(report, "SELECT COUNT(*) FROM AUTORIZACION WHERE FECHA_AUTORIZACION = TRUNC (SYSDATE) AND NRO_TARJETA = '" + NRO_TARJETA_ENCRIPTADA + "'", configEntidad);
			
			//Metodo que compara la respuesta de param con el estado y registros esperados a ser procesados en el esquema de la entidad.
			respuestaVerificacion = batch.verifProcesosEjec(report, null, rtaParam, cantidadRegistrosEsperados);
			
			if (!respuestaVerificacion) {
				report.AddLineAssertionError("ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA");
				System.out.println("##[warning] : ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA\r\n");
				result = false;
			}
			
			//ROLLBACK
			
			report.AddLine("Se ejecuta Rollback. Se eliminan los registros insertados previamente.");
			System.out.println("Se ejecuta Rollback. Se eliminan los registros insertados previamente.\r\n");
			
			respuestaDelete = oraResp.oraDelete(report, "DELETE AUTORIZACION WHERE FECHA_AUTORIZACION = TRUNC (SYSDATE) AND NRO_TARJETA = '" + NRO_TARJETA_ENCRIPTADA + "'", configEntidad);
			ppCondi.postCondicionBD(report, respuestaDelete);
				
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");
		
		} catch(Exception e) {
			e.printStackTrace();
			report.AddLineAssertionError(e.getMessage());	
			
            //Se agrega de nuevo ROLLBACK para que no quede la base inconsistente si falla el TestCase  
			
			//SET VARIABLES 
			int respuestaDelete;
			PrePostCondi ppCondi = new PrePostCondi();
			dbWorker oraResp = new dbWorker();
			String NRO_TARJETA_ENCRIPTADA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.nro_tarjeta_encriptada");
			
            //ROLLBACK	
			report.AddLine("Se ejecuta Rollback. Se eliminan los registros insertados previamente.");
			System.out.println("Se ejecuta Rollback. Se eliminan los registros insertados previamente.\r\n");
			
			respuestaDelete = oraResp.oraDelete(report, "DELETE AUTORIZACION WHERE FECHA_AUTORIZACION = TRUNC (SYSDATE) AND NRO_TARJETA = '" + NRO_TARJETA_ENCRIPTADA + "'", configEntidad);
			ppCondi.postCondicionBD(report, respuestaDelete);
			
			
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