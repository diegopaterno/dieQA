package TestCases.AutorizadorInterno;

import org.openqa.selenium.WebDriver;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.PrePostCondi;
import Repositories.Repo_Variables;
import Tools.apiWorker;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;
import Pasos.Generales.AutorizadorInterno;

public class TC_08_API_AI{
	WebDriver driver;
	
	// Compra Local - Contacless 
	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
		//Validation var
		boolean result = true;
		try {
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_08_API_AI\r\n");

			//SET VARIABLES
			// Ruta donde se encuentra el archivo con el Body que se envia en el metodo Post
			String path = "./API_Requests/Autorizador/TC_08_CompraLocalContacless345.98.txt";
			String token = "";
			String bodyData = "";
			String respuesta1 = "";
			String respuesta2 = "";
			String respBody = "";
			boolean res; 
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			String NRO_TARJETA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.nro_tarjeta");
			String VENCIMIENTO = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.vencimiento");
			String ID_ESTADO_CUENTA = "0";
			String ID_ESTADO_TARJETA = "0";
			String de39 = ""; 
			int idAutorizacionEmisor = 0;
			int IdAutorizacion = 0;
			int rtaRollback = 0;
			String[] rtaCant2 = new String[2];
			// Resultados esperados en la prueba
			String[] resulEsperado = new String[4];
			resulEsperado[0] = "970.02";//DISPONIBLE
			resulEsperado[1] = "929.98";//IMPORTE_PEND_COMPRAS
			resulEsperado[2] = "0";//ID_ESTADO de AUTORIZACION
			resulEsperado[3] = "00";//DE39 EN RESPONSE

			//SET INSTANCES
			apiWorker apiResp = new apiWorker();
			dbWorker oraResp = new dbWorker();
			Repo_Variables repoVar = new Repo_Variables();
			PrePostCondi ppCondi = new PrePostCondi();
			AutorizadorInterno AI = new AutorizadorInterno();
			
			//SET ENVIRONMENT
			AI.setBaseUrl(report, repoVar, configEntidad);

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PRECONDICION
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");
			
			AI.setEstadoCuentaTarjeta(report, ID_CUENTA, ID_ESTADO_CUENTA, ID_ESTADO_TARJETA, configEntidad);
			
			rtaRollback = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA\r\n"
					+ "SET SALDO = 1900, DISPONIBLE = 1316, DISPONIBLE_ADELANTO = 1900,\r\n"
					+ "IMPORTE_COMPRAS = 0, IMPORTE_ADELANTOS = 0, IMPORTE_PEND_COMPRAS = 584, \r\n"
					+ "IMPORTE_PEND_ADELANTOS = 0, IMPORTE_AJUSTES = 0, IMPORTE_AJUSTES_PEND = 0,\r\n"
					+ "IMPORTE_PAGOS = 1900\r\n"
					+ "WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			
			ppCondi.preCondicionBD(report, rtaRollback);
					
			//GET TOKEN
			token = AI.getToken(report, apiResp, configEntidad);
			
			//GET POST BODY FROM FILE
			bodyData = AI.getBodyData(report, path);
			
			//Se actualiza el arhivo json y la variable bodyData con el nro de tarjeta del TC
			bodyData = AI.setJsonBodyDE35(report, path, bodyData, NRO_TARJETA, VENCIMIENTO);
			
			//POST - Compra Local - Contacless 
			if (!bodyData.isEmpty() && !token.isEmpty())
			{
				
				respBody = AI.post(report, repoVar, apiResp, bodyData, token);
				
				//Se obtiene el valor del DE39 del response
				de39 = JsonPath.from(respBody).getString("DE39");
				report.AddLine("Valor del DE39 en el response: " + de39);
				System.out.println("Valor del DE39 en el response: " + de39 + "\r\n");
				
				//Se obtiene el valor del IdAutorizacionEmisor del response
				idAutorizacionEmisor = JsonPath.from(respBody).getInt("IdAutorizacionEmisor");
				report.AddLine("Valor del IdAutorizacionEmisor en el response: " + idAutorizacionEmisor);
				System.out.println("Valor del IdAutorizacionEmisor en el response: " + idAutorizacionEmisor + "\r\n");
				
				//Se obtiene el valor del IdAutorizacion del response
				IdAutorizacion = JsonPath.from(respBody).getInt("IdAutorizacion");
				report.AddLine("Valor del IdAutorizacion en el response: " + IdAutorizacion);
				System.out.println("Valor del IdAutorizacion en el response: " + IdAutorizacion + "\r\n");
				
			} else {
				System.out.println("No se obtuvo un token o el body no existe");
				report.AddLineAssertionError("No se obtuvo un token o el body no existe");
				result = false;
			}	
		
			//ADD VALIDATIONS
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
			
			//Se obtiene el valor del OUT_COD_RESPUESTA de la autorizacion generada
			respuesta1 = oraResp.oraOneQuery(report, "SELECT OUT_COD_RESPUESTA from AUTORIZACION_EMISOR_LOG WHERE ID_AUTORIZACION_EMISOR = " + idAutorizacionEmisor, configEntidad);
						
			//Se muestra el valor obtenido de la consulta
			report.AddLine("Valor del OUT_COD_RESPUESTA de la tabla AUTORIZACION_EMISOR_LOG en la BD: " + respuesta1);
			System.out.println("Valor del OUT_COD_RESPUESTA de la tabla AUTORIZACION_EMISOR_LOG en la BD: " + respuesta1 + "\r\n");
			
			//Se obtiene el valor del ID_ESTADO de la autorizacion generada
			respuesta2 = oraResp.oraOneQuery(report, "SELECT ID_ESTADO FROM AUTORIZACION WHERE ID_AUTORIZACION = " + IdAutorizacion, configEntidad);
			
			//Se muestra el valor obtenido de la consulta
			report.AddLine("Valor del ID_ESTADO de la tabla AUTORIZACION en la BD: " + respuesta2);
			System.out.println("Valor del ID_ESTADO de la tabla AUTORIZACION en la BD: " + respuesta2 + "\r\n");
			
			//Se obtiene el valor del DISPONIBLE e IMPORTE_PEND_COMPRAS de la BD
			rtaCant2 = oraResp.oraTwoQuery(report, "SELECT DISPONIBLE,IMPORTE_PEND_COMPRAS\r\n"
					+ "FROM CUENTAS_MONEDA \r\n"
					+ "WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			
			//Validacion del DE39 del response con el valor esperado
			res = AI.validacionDE39EnResponse(report, de39, resulEsperado);
			if (!res) {
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			}
			
			//Se valida los datos obtenidos de la BD con los resultados esperados
			//Validacion de DISPONILE e IMPORTE_PEND_COMPRAS
			res = AI.validacionCuentasMoneda(report, rtaCant2, resulEsperado);
			if (!res) {
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			}
			//Validacion del ID_ESTADO de la autorizacion
			res = AI.validacionAutorizacion(report, respuesta2, resulEsperado);
			if (!res) {
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			}			
			
			//ROLLBACK
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
			
			rtaRollback = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA\r\n"
					+ "SET SALDO = 1900, DISPONIBLE = 1316, DISPONIBLE_ADELANTO = 1900,\r\n"
					+ "IMPORTE_COMPRAS = 0, IMPORTE_ADELANTOS = 0, IMPORTE_PEND_COMPRAS = 584, \r\n"
					+ "IMPORTE_PEND_ADELANTOS = 0, IMPORTE_AJUSTES = 0, IMPORTE_AJUSTES_PEND = 0,\r\n"
					+ "IMPORTE_PAGOS = 1900\r\n"
					+ "WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			
			ppCondi.validaRollBackUpdate(report, rtaRollback);

			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");

		} catch (Exception e) {
			e.printStackTrace();
			report.AddLineAssertionError(e.getStackTrace()[0].toString());
			report.AddLineAssertionError(e.getMessage());
			
            //Se agrega de nuevo ROLLBACK para que no quede la base inconsistente si falla el TestCase  
			
			//SET VARIABLES 
			int rtaRollback = 0;
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");

			
			//ROLLBACK
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
			
			rtaRollback = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA\r\n"
					+ "SET SALDO = 1900, DISPONIBLE = 1316, DISPONIBLE_ADELANTO = 1900,\r\n"
					+ "IMPORTE_COMPRAS = 0, IMPORTE_ADELANTOS = 0, IMPORTE_PEND_COMPRAS = 584, \r\n"
					+ "IMPORTE_PEND_ADELANTOS = 0, IMPORTE_AJUSTES = 0, IMPORTE_AJUSTES_PEND = 0,\r\n"
					+ "IMPORTE_PAGOS = 1900\r\n"
					+ "WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			
			ppCondi.validaRollBackUpdate(report, rtaRollback);

			
			result = false;
		}

		return result;
	}

}
