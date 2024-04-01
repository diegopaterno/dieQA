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

public class TC_07_API_AI{
	WebDriver driver;

	// PRUEBA QUE REQUIERE LA EJECUCION DE DEL TC06 (Devolución Local - Chip)
	// Reverso Total - Devolucion Local 
	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
		//Validation var
		boolean result = true;
		try {
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");

			System.out.println("Configuring TC_07_API_AI\r\n");

			//SET VARIABLES
			// Ruta donde se encuentra el archivo con el Body que se envia en el metodo Post
			String path = "./API_Requests/Autorizador/TC_07_ReversoTotalDevolucionLocal_Chip214.00.txt";
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
			int rtaRollback1 = 0;
			String[] rtaCant2 = new String[2];
			// Resultados esperados en la prueba
			String[] resulEsperado = new String[4];
			resulEsperado[0] = "1316";//DISPONIBLE
			resulEsperado[1] = "584";//IMPORTE_PEND_COMPRAS
			resulEsperado[2] = "2";//ID_ESTADO de AUTORIZACION
			resulEsperado[3] = "00";//DE39 EN RESPONSE

			// Variables con datos para gnerar la transaccion de precondicion
			// Ruta donde se encuentra el archivo con el Body que se envia en el metodo Post para la precondicion
			String pathPreCondi = "./API_Requests/Autorizador/TC_06_DevoluciónLocal_Chip214.00.txt";
			String disponible_PreCondi = "1316";
			String importe_pend_compras_PreCondi = "584";
			boolean respuestaPreCondi;

			//SET INSTANCES
			apiWorker apiResp = new apiWorker();
			dbWorker oraResp = new dbWorker();
			Repo_Variables repoVar = new Repo_Variables();
			PrePostCondi ppCondi = new PrePostCondi();
			AutorizadorInterno AI = new AutorizadorInterno();

			//SET ENVIRONMENT
			AI.setBaseUrl(report, repoVar,configEntidad);

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PRECONDICION
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");
			
			AI.setEstadoCuentaTarjeta(report, ID_CUENTA, ID_ESTADO_CUENTA, ID_ESTADO_TARJETA, configEntidad);

			//PRECONDICION PARA GENERAR AUTORIZACION NECESARIA PARA EL REVERSO
			respuestaPreCondi = AI.preCondicionParaReversosDevoluciones(report, DM,pathPreCondi,disponible_PreCondi,importe_pend_compras_PreCondi,configEntidad, ID_CUENTA, NRO_TARJETA, VENCIMIENTO);

			if(respuestaPreCondi) {
				report.AddLine("La Devolucion Local como precondicion al reverso se genero correctamente");
				System.out.println("##[section] : La Devolucion Local como precondicion al reverso se genero correctamente\r\n");
			}
			else {
				report.AddLine("La Devolucion Local como precondicion al reverso no se genero");
				System.out.println("##[warning] : La Devolucion Local como precondicion al reverso no se genero\r\n");
				result = false;
			}

			report.AddLine("Ejecutando Reverso de la Devolucion Local generada previamente");
			System.out.println("Ejecutando Reverso de la Devolucion Local generada previamente\r\n");			

			//GET TOKEN
			token = AI.getToken(report, apiResp,configEntidad);

			//GET POST BODY FROM FILE
			bodyData = AI.getBodyData(report, path);
			
			//Se actualiza el arhivo json y la variable bodyData con el nro de tarjeta del TC
			bodyData = AI.setJsonBody(report, path, bodyData, NRO_TARJETA,VENCIMIENTO);

			//POST - Reverso Total - Devolucion Local -  Chip
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

			rtaRollback1 = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA\r\n"
					+ "SET SALDO = 1900, DISPONIBLE = 1530, DISPONIBLE_ADELANTO = 1900,\r\n"
					+ "IMPORTE_COMPRAS = 0, IMPORTE_ADELANTOS = 0, IMPORTE_PEND_COMPRAS = 370, \r\n"
					+ "IMPORTE_PEND_ADELANTOS = 0, IMPORTE_AJUSTES = 0, IMPORTE_AJUSTES_PEND = 0,\r\n"
					+ "IMPORTE_PAGOS = 1900\r\n"
					+ "WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);

			ppCondi.validaRollBackUpdate(report, rtaRollback1);

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
			int rtaRollback1 = 0;
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			
			//ROLLBACK
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			rtaRollback1 = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA\r\n"
					+ "SET SALDO = 1900, DISPONIBLE = 1530, DISPONIBLE_ADELANTO = 1900,\r\n"
					+ "IMPORTE_COMPRAS = 0, IMPORTE_ADELANTOS = 0, IMPORTE_PEND_COMPRAS = 370, \r\n"
					+ "IMPORTE_PEND_ADELANTOS = 0, IMPORTE_AJUSTES = 0, IMPORTE_AJUSTES_PEND = 0,\r\n"
					+ "IMPORTE_PAGOS = 1900\r\n"
					+ "WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);

			ppCondi.validaRollBackUpdate(report, rtaRollback1);
			
			
			result = false;
		}

		return result;
	}

}
