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

public class TC_16_API_AI{
	WebDriver driver;
	
	// Compra con cashback
		public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
	
	//Validation var
	   boolean result = true;
	   try {	
	
    //Separador
	 System.out.println("\r\n##################################################################################################################################################################################################################"
								 + "##################################################################################################################################################################################################################\r\n");
				
	 System.out.println("Configuring TC_16_API_AI\r\n");

	
		//SET VARIABLES
		// Ruta donde se encuentra el archivo con el Body que se envia en el metodo Post
		String path = "./API_Requests/Autorizador/TC_16_Compra_con_cashback.txt";
		String token = "";
		String bodyData = "";
		String[] rtaCant3 = new String[3];
		String[] rtaCant2 = new String[2];
		String respBody = "";
		boolean[] res = new boolean[5]; 
		String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
		String NRO_TARJETA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.nro_tarjeta");
		String VENCIMIENTO = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.vencimiento");
		String ID_ESTADO_CUENTA = "0";
		String ID_ESTADO_TARJETA = "0";
		String de39 = ""; 
		int IdAutorizacion = 0;
		int IdAutorizacionCashback = 0;
		int rtaRollback = 0;
		
		// Resultados esperados en la prueba
		String[] resulEsperado = new String[6];
		resulEsperado[0] = "250";//Importe_origen
		resulEsperado[1] = "50";//Importe
		resulEsperado[2] = "";//IdAutorizacionCashback
		resulEsperado[3] = "00";//DE39 EN RESPONSE
		resulEsperado[4] = "1";
		resulEsperado[5] = "";//IdAutorizacion

		
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
				+ "SET SALDO = 1000, DISPONIBLE = 1000, DISPONIBLE_ADELANTO = 1000,\r\n"
				+ "IMPORTE_COMPRAS = 1000, IMPORTE_ADELANTOS = 1000, IMPORTE_PEND_COMPRAS = 1000, \r\n"
				+ "IMPORTE_PEND_ADELANTOS = 1000, IMPORTE_AJUSTES = 1000, IMPORTE_AJUSTES_PEND = 1000,\r\n"
				+ "IMPORTE_PAGOS = 1000\r\n"
				+ "WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
		
		ppCondi.preCondicionBD(report, rtaRollback);
		
		//GET TOKEN
		token = AI.getToken(report, apiResp, configEntidad);
		
		//GET POST BODY FROM FILE
		bodyData = AI.getBodyData(report, path);
		
		//Se actualiza el arhivo json y la variable bodyData con el nro de tarjeta del TC
		bodyData = AI.setJsonBody(report, path, bodyData, NRO_TARJETA, VENCIMIENTO);
		
		//POST - Compra Local - Forzada
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
			
			IdAutorizacionCashback = IdAutorizacion + 1;
			report.AddLine("Valor del IdAutorizacionCashback es:" + IdAutorizacionCashback);
			System.out.println("Valor del IdAutorizacionCashback es:" + IdAutorizacionCashback + "\r\n");
			resulEsperado[2] = String.valueOf(IdAutorizacionCashback);
			
			
		 } else {
			System.out.println("No se obtuvo un token o el body no existe");
			report.AddLineAssertionError("No se obtuvo un token o el body no existe");
			result = false;
		}	
		

		//ADD VALIDATIONS
		System.out.println("Se realizan las validaciones en la BD:\r\n");
		report.AddLine("Se realizan las validaciones en la BD:");
		
		//Se obtiene el valor del campo IMPORTE con la compra y el cashback
		rtaCant3 = oraResp.oraThreeQuery (report, "SELECT IMPORTE_ORIGEN, IMPORTE, ID_AUTORIZACION FROM AUTORIZACION WHERE ID_AUTORIZACION IN ('" + IdAutorizacion + "', '" + IdAutorizacionCashback + "')", configEntidad);
				
				
		//Se muestra el valor obtenido de la consulta
		report.AddLine("Se Verificar que el campo IMPORTE_ORIGEN sea 250, el IMPORTE del cashback de 50 y el ID_AUTORIZACION:" + resulEsperado[0] + ", " + resulEsperado[1] + ", " + resulEsperado[2]);
		System.out.println("Se Verificar que el campo IMPORTE_ORIGEN sea 250, el IMPORTE del cashback de 50 y el ID_AUTORIZACION:" + resulEsperado[0] + ", " + resulEsperado[1] + ", " + resulEsperado[2] + "\r\n");
		
		//Se valida los datos obtenidos de la BD con los resultados esperados
		//Validacion que el valor del campo IMPORTE con la compra y el cashback
		res[0] = AI.validacionesRespBase (report, rtaCant3[0], resulEsperado[0]);

		//Se valida los datos obtenidos de la BD con los resultados esperados
		//Validacion que el valor del campo IMPORTE con la compra y el cashback
		res[1] = AI.validacionesRespBase (report, rtaCant3[1], resulEsperado[1]);
		
		//Se valida los datos obtenidos de la BD con los resultados esperados
		//Validacion que el valor del campo IMPORTE con la compra y el cashback
		res[2] = AI.validacionesRespBase (report, rtaCant3[2], resulEsperado[2]);
		
		
		//Validacion del DE39 del response con el valor esperado
		res[0] = AI.validacionDE39EnResponse(report, de39, resulEsperado);
		if (!res[0]) {
			//Si falla la validacion el resultado de la prueba es false
			result = false;
		} else { 
			
			//Se obtiene el campo de ES_CASHBACK y tiene que tener el campo asociado ID_AUTORIZACIO_ORIGINAL
			rtaCant2 = oraResp.oraTwoQuery(report, "SELECT ES_CASHBACK, ID_AUTORIZACION_ORIGINAL FROM AUTORIZACION WHERE ID_AUTORIZACION =" + IdAutorizacionCashback, configEntidad);
			resulEsperado[5] = String.valueOf(IdAutorizacion);
			
			
			//Se muestra el valor obtenido de la consulta
			report.AddLine("Se Verificar que el campo ES_CASHBACK = 1 y que tiene que tener el campo asociado ID_AUTORIZACIO_ORIGINAL:" + resulEsperado[4] + ", " + resulEsperado[5]);
			System.out.println("Se Verificar que el campo ES_CASHBACK = 1 y que tiene que tener el campo asociado ID_AUTORIZACIO_ORIGINAL:" + resulEsperado[4] + ", " + resulEsperado[5] + "\r\n");
									
			
			//Validamos que el campo de ES_CASHBACK y tiene que tener el campo asociado ID_AUTORIZACIO_ORIGINAL
			res[3] = AI.validacionesRespBase (report, rtaCant2[0], resulEsperado[4]);
					
			//Validamos que el campo de ES_CASHBACK y tiene que tener el campo asociado ID_AUTORIZACIO_ORIGINAL
			res[4] = AI.validacionesRespBase (report, rtaCant2[1], resulEsperado[5]);
			
			for (boolean rta:res) {
				if (!rta)
					result = false;
			}
			
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
	   


	   
		
		
		




	
	
