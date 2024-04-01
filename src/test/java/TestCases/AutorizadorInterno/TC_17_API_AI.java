package TestCases.AutorizadorInterno;

import org.openqa.selenium.WebDriver;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.AutorizadorInterno;
import Pasos.Generales.PrePostCondi;
import Repositories.Repo_Variables;
import Tools.apiWorker;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;

public class TC_17_API_AI {
	WebDriver driver;
	
	// Devolución Total- Compra con cashback
		public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
			//Validation var
			boolean result = true;
			try {
				//Separador
				System.out.println("\r\n##################################################################################################################################################################################################################"
								 + "##################################################################################################################################################################################################################\r\n");
				
				System.out.println("Configuring TC_17_API_AI\r\n");
				
				//SET VARIABLES
				// Ruta donde se encuentra el archivo con el Body que se envia en el metodo Post
				int rtaRollback = 0;
				String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
				String ID_ESTADO_CUENTA = "0";
				String ID_ESTADO_TARJETA = "0";
				String path = "./API_Requests/Autorizador/TC_17_Devolucion_Total_con_cashback.txt";
				String token = "";
				String bodyData = "";
				String NRO_TARJETA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.nro_tarjeta");
				String VENCIMIENTO = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.vencimiento");
				String respBody = "";
				String de39 = ""; 
				int IdAutorizacion = 0;
				int IdAutorizacionCashback = 0;
				String respuesta1 = "";
				String[] rtaCant2 = new String[2];
				boolean res; 
				
				// Resultados esperados en la prueba
				String[] resulEsperado = new String[3];
				resulEsperado[0] = "1000";//DISPONIBLE
				resulEsperado[1] = "";
				resulEsperado[3] = "00";//DE39 EN RESPONSE
				
				
				//SET INSTANCES
				AutorizadorInterno AI = new AutorizadorInterno();
				dbWorker oraResp = new dbWorker();
				PrePostCondi ppCondi = new PrePostCondi();
				apiWorker apiResp = new apiWorker();
				Repo_Variables repoVar = new Repo_Variables();
				
				
				
				//PRECONDICION
				report.AddLine("--- Ejecutando PreCondicion ---");
				System.out.println("--- Ejecutando PreCondicion ---\r\n");
				
				AI.setEstadoCuentaTarjeta(report, ID_CUENTA, ID_ESTADO_CUENTA, ID_ESTADO_TARJETA, configEntidad);
				
				rtaRollback = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA\r\n"
						+ "SET SALDO = 1000, DISPONIBLE = 1000, DISPONIBLE_ADELANTO = 1000,\r\n"
						+ "IMPORTE_COMPRAS = 1000, IMPORTE_ADELANTOS = 1000, IMPORTE_PEND_COMPRAS = 1000, \r\n"
						+ "IMPORTE_PEND_ADELANTOS = 1000, IMPORTE_AJUSTES = 1000, IMPORTE_AJUSTES_PEND = 1000,\r\n"
						+ "IMPORTE_PAGOS = 1000\r\n"
						+ "WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
				
				ppCondi.preCondicionBD(report, rtaRollback);
				
				//GET TOKEN
				token = AI.getToken(report, apiResp,configEntidad);
			
				//GET POST BODY FROM FILE
				bodyData = AI.getBodyData(report, path);
				
				//Se actualiza el arhivo json y la variable bodyData con el nro de tarjeta del TC
				bodyData = AI.setJsonBodyDE35(report, path, bodyData, NRO_TARJETA, VENCIMIENTO);
				
				//POST - Devolución Local - Chip
				if (!bodyData.isEmpty() && !token.isEmpty())
				{
					
					respBody = AI.post(report, repoVar, apiResp, bodyData, token);
					
					//Se obtiene el valor del DE39 del response
					de39 = JsonPath.from(respBody).getString("DE39");
					report.AddLine("Valor del DE39 en el response: " + de39);
					System.out.println("Valor del DE39 en el response: " + de39 + "\r\n");
					
					
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
				
				//Se obtiene el valor del DISPONIBLE
				respuesta1 = oraResp.oraOneQuery(report, "select disponible from cuentas_moneda where id_cuenta=" + ID_CUENTA, configEntidad);
							
				//Se muestra el valor obtenido de la consulta
				report.AddLine("Valor del DISPONIBLE en la BD es: " + respuesta1);
				System.out.println("Valor del DISPONIBLE en la BD es: " + respuesta1 + "\r\n");
				
				//Se obtiene el valor  generada
				rtaCant2 = oraResp.oraTwoQuery(report, "select es_cashback, monto_acum_devoluciones from autorizacion where id_autorizacion in ('" + IdAutorizacion + "', '" + IdAutorizacionCashback + "')", configEntidad);
				
				//Se muestra el valor obtenido de la consulta
				report.AddLine("Valor del ES_CASHBACK y el MONTO ACUMULADO en la BD es de:" + rtaCant2);
				System.out.println("Valor del ES_CASHBACK y el MONTO ACUMULADO en la BD es de:" + rtaCant2 + "\r\n");
				
				//Validacion del DE39 del response con el valor esperado
				res = AI.validacionDE39EnResponse(report, de39, resulEsperado);
				if (!res) {
					//Si falla la validacion el resultado de la prueba es false
					result = false;
				}

				//ROLLBACK
				report.AddLine("--- Ejecutando PostCondicion ---");
				System.out.println("--- Ejecutando PostCondicion ---\r\n");
				
				rtaRollback = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA\r\n"
						+ "SET SALDO = 0, DISPONIBLE = 0, DISPONIBLE_ADELANTO = 0,\r\n"
						+ "IMPORTE_COMPRAS = 0, IMPORTE_ADELANTOS = 0, IMPORTE_PEND_COMPRAS = 0, \r\n"
						+ "IMPORTE_PEND_ADELANTOS = 0, IMPORTE_AJUSTES = 0, IMPORTE_AJUSTES_PEND = 0,\r\n"
						+ "IMPORTE_PAGOS = 0\r\n"
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
				result = false;
				}
			     return result;
			     	
     }


}
