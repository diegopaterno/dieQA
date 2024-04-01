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

public class TC_18_API_AI {
	WebDriver driver;
	
	// Reverso Total - Reverso total de cashback sobre una devolucion
		public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
			//Validation var
			boolean result = true;
			try {
				//Separador
				System.out.println("\r\n##################################################################################################################################################################################################################"
						+ "##################################################################################################################################################################################################################\r\n");

				System.out.println("Configuring TC_18_API_AI\r\n");

				//SET VARIABLES
				// Ruta donde se encuentra el archivo con el Body que se envia en el metodo Post
				String path = "./API_Requests/Autorizador/TC_18_Reverso_total_cashback.txt";		
				String token = "";
				String bodyData = "";
				String respuesta1 = "";
				String respBody = "";
				boolean res; 
				String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
				String NRO_TARJETA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.nro_tarjeta");
				String VENCIMIENTO = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.vencimiento");
				String ID_ESTADO_CUENTA = "0";
				String ID_ESTADO_TARJETA = "0";
				String de39 = ""; 
				int IdAutorizacion = 0;
				int IdAutorizacionCashback = 0;
				int rtaRollback1 = 0;
				String[] rtaCant2 = new String[2];
				// Resultados esperados en la prueba
				String[] resulEsperado = new String[3];
				resulEsperado[0] = "1";//
				resulEsperado[1] = "";//
				resulEsperado[3] = "00";//DE39 EN RESPONSE

				// Variables con datos para gnerar la transaccion de precondicion
				// Ruta donde se encuentra el archivo con el Body que se envia en el metodo Post para la precondicion
				String pathPreCondi = "./API_Requests/Autorizador/TC_17_Devolucion_Total_con_cashback.txt";
				String disponible_PreCondi = "970.02";
				String importe_pend_compras_PreCondi = "929.98";
				boolean respuestaPreCondi;

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
				
				AI.setEstadoCuentaTarjeta(report, ID_CUENTA, ID_ESTADO_CUENTA, ID_ESTADO_TARJETA, configEntidad);
				
				//PRECONDICION
				report.AddLine("--- Ejecutando PreCondicion ---");
				System.out.println("--- Ejecutando PreCondicion ---\r\n");

				//PRECONDICION PARA GENERAR AUTORIZACION NECESARIA PARA EL REVERSO
				respuestaPreCondi = AI.preCondicionParaReversosDevoluciones(report, DM,pathPreCondi,disponible_PreCondi,importe_pend_compras_PreCondi, configEntidad, ID_CUENTA, NRO_TARJETA, VENCIMIENTO);

				if(respuestaPreCondi) {
					report.AddLine("La Devolucion Parcial como precondicion al reverso se genero correctamente");
					System.out.println("##[section] : La Devolucion Parcial como precondicion al reverso se genero correctamente\r\n");
				}
				else {
					report.AddLine("La Devolucion Parcial como precondicion al reverso no se genero");
					System.out.println("##[warning] : La Devolucion Parcial como precondicion al reverso no se genero\r\n");
					result = false;
				}

				report.AddLine("Ejecutando Reverso de la Devolucion Parcial generada previamente");
				System.out.println("Ejecutando Reverso de la Devolucion Parcial generada previamente\r\n");

				//GET TOKEN
				token = AI.getToken(report, apiResp, configEntidad);

				//GET POST BODY FROM FILE
				bodyData = AI.getBodyData(report, path);
				
				//Se actualiza el arhivo json y la variable bodyData con el nro de tarjeta del TC
				bodyData = AI.setJsonBody(report, path, bodyData, NRO_TARJETA, VENCIMIENTO);

				//POST - Reverso Total - Devolucion Parcial Local
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
				respuesta1 = oraResp.oraOneQuery(report, "select disponible from cuentas_moneda where id_cuenta =" + ID_CUENTA, configEntidad);

				//Se muestra el valor obtenido de la consulta
				report.AddLine("Valor del DISPONIBLE en la BD:" + respuesta1);
				System.out.println("Valor del DISPONIBLE en la BD:" + respuesta1 + "\r\n");

				//Se obtiene el valor del  ES_CASHBACK y el MONTO_ACUM_DEVOLUCIONES 
				rtaCant2 = oraResp.oraTwoQuery(report, "select es_cashback, monto_acum_devoluciones from autorizacion where id_autorizacion in ('" + IdAutorizacion + "','" + IdAutorizacionCashback + "')", configEntidad);

				//Se muestra el valor obtenido de la consulta
				report.AddLine("Valor del ES_CASHBACK y el MONTO_ACUM_DEVOLUCIONES en la BD es:" + rtaCant2);
				System.out.println("Valor del ES_CASHBACK y el MONTO_ACUM_DEVOLUCIONES en la BD es:" + rtaCant2 + "\r\n");


				//Validacion del DE39 del response con el valor esperado
				res = AI.validacionDE39EnResponse(report, de39, resulEsperado);
				if (!res) {
					//Si falla la validacion el resultado de la prueba es false
					result = false;
				}

				
				
				
				

				//ROLLBACK
				report.AddLine("--- Ejecutando PostCondicion ---");
				System.out.println("--- Ejecutando PostCondicion ---\r\n");

				rtaRollback1 = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA\r\n"
						+ "SET SALDO = 1900, DISPONIBLE = 970.02, DISPONIBLE_ADELANTO = 1900,\r\n"
						+ "IMPORTE_COMPRAS = 0, IMPORTE_ADELANTOS = 0, IMPORTE_PEND_COMPRAS = 929.98, \r\n"
						+ "IMPORTE_PEND_ADELANTOS = 0, IMPORTE_AJUSTES = 0, IMPORTE_AJUSTES_PEND = 0,\r\n"
						+ "IMPORTE_PAGOS = 1900\r\n"
						+ "WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
				ppCondi.validaRollBackUpdate(report, rtaRollback1);

				System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
				report.AddLine("<------ Finished Test: " + name + " ------>");

				//Separador
				System.out.println("##################################################################################################################################################################################################################"
						+ "##################################################################################################################################################################################################################\r\n");

			} 
	
			catch (Exception e) {
				e.printStackTrace();
				report.AddLineAssertionError(e.getStackTrace()[0].toString());
				report.AddLineAssertionError(e.getMessage());
				result = false;
				}
			     return result;
	
	
		}	
	
}
