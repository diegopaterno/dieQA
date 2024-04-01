package TestCases.ApiPrepaga;

import static org.hamcrest.Matchers.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.WebDriver;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.PrePostCondi;
import Repositories.Repo_Variables;
import Tools.apiWorker;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class TC_15_API_PP{
	WebDriver driver;

	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
		//Validation var
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");

			System.out.println("Configuring TC_15_API_PP\r\n");

			//SET VARIABLES
			String path = "./API_Requests/PrePagas/TC_15_Reimpresion_Tarjeta_Virtual_a_Virtual.txt";
			String token = "";
			String bodyData = "";
			String[] resp = new String[2];
			int rta = 0;
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_2.cuenta_virtual");
			String ID_PRODUCTO = JsonPath.from(cuentas_generales).get("PARAMETRIA_CUENTAS.id_producto");
			String REFERENCIA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_2.referencia");
			String ID_TARJETA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_2.id_tarjeta");
			Response response;
			String respBody = "";

			//SET INSTANCES
			apiWorker apiResp = new apiWorker();
			dbWorker oraResp = new dbWorker();
			Repo_Variables repoVar = new Repo_Variables();
			PrePostCondi ppCondi = new PrePostCondi();

			//SET ENVIRONMENT
			//Obtengo los datos de la base url lel archivo configEntidad que contiene todos los accesos a la entidad
			String base_url = JsonPath.from(configEntidad).get("TOKENPREPAGA.base_url");
			repoVar.setBaseUri(base_url);

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PRECONDICION
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");
			rta = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_TARJETA = " + ID_TARJETA, configEntidad);
			ppCondi.preCondicionBD(report, rta);

			//GET TOKEN
			try {
				token = apiResp.GetAccessTokenPP(configEntidad);
			} catch (Exception E) {
				report.AddLineAssertionError("No se obtuvo un token.<br>Error: " + E);
				System.out.println("##[warning] : No se obtuvo un token.\r\nError: " + E + "\r\n");
			}

			//GET POST BODY FROM FILE
			try {
				bodyData = new String(Files.readAllBytes(Paths.get(path)));	
				
				//Se muestra el body en el reporte
				report.AddLine("Request body:<br>" + bodyData);
				
			} catch (Exception E) {
				report.AddLineAssertionError("Error al abrir el archivo.<br>Error: " + E);
				System.out.println("##[warning] : Error al abrir el archivo.\r\nError: " + E + "\r\n");
			}

			//PUT - Reimpresion de Tarjeta - Virtual a Virtual
			if (!bodyData.isEmpty() && !token.isEmpty())
			{
				response = apiResp.putMethod(repoVar, "/api/Productos/" + ID_PRODUCTO + "/Cuentas/" + ID_CUENTA + "/Tarjetas/" + REFERENCIA + "/", "Reimprimir", bodyData, token);
				//Se reporta el Status Code
				report.AddLine("Status Code: " + String.valueOf(response.getStatusCode()));
				System.out.println("Status Code: " + String.valueOf(response.getStatusCode()) + "\r\n");
				//Se valida el status code
				if(response.getStatusCode()!=200) {
					report.AddLineAssertionError("Fallo la validacion del StatusCode<br>Esperado: 200 - Obtenido: " + String.valueOf(response.getStatusCode()));
					System.out.println("##[warning] : Fallo la validacion del StatusCode:\r\nEsperado: 200 - Obtenido: " + String.valueOf(response.getStatusCode()) + "\r\n");
					MatcherAssert.assertThat("Validacion: Status code", String.valueOf(response.getStatusCode()), equalTo("200"));				
				}
				//Se obtiene el body del response
				respBody = response.getBody().asPrettyString();
				//Se muestra response en el reporte
				report.AddLine("Response: " + respBody);

			} else {
				System.out.println("##[warning] : No se obtuvo un token\r\n");
				report.AddLineAssertionError("No se obtuvo un token");
				result = false;
			}

			//ADD VALIDATIONS
			
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
			
			resp = oraResp.oraTwoQuery(report, "SELECT ID_ESTADO, SOPORTE_FISICO FROM TARJETAS WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_TARJETA NOT IN (" + ID_TARJETA + ")", configEntidad);

			if(!resp[0].equals("0")) {
				rollBack(report, oraResp, ppCondi, configEntidad, cuentas_generales);
				report.AddLineAssertionError("Error: El estado de la nueva tarjeta es: " + resp[0] + " Se esperaba el estado: 0");
				System.out.println("##[warning] : Error: El estado de la nueva tarjeta es: " + resp[0] + " Se esperaba el estado: 0");
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			} else {
				report.AddLine("Validacion exitosa:<br>El estado de la nueva tarjeta es: 0 y es igual al de la base de datos: " + resp[0]);
				System.out.println("##[section] : Validacion exitosa:\r\nEl estado de la nueva tarjeta es: 0 y es igual al de la base de datos: " + resp[0] + "\r\n");
			}

			if(!resp[1].equals("1")) {
				rollBack(report, oraResp, ppCondi, configEntidad, cuentas_generales);
				report.AddLineAssertionError("Error: El Soporte Fisico es: " + resp[1] + " Se esperaba: 1");
				System.out.println("##[warning]  Error: El Soporte Fisico es: " + resp[1] + " Se esperaba: 1");
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			} else {
				report.AddLine("Validacion exitosa:<br>El Soporte Fisico es: 1 y es igual al de la base de datos: " + resp[1]);
				System.out.println("##[section] : Validacion exitosa:\r\nEl Soporte Fisico es: 1 y es igual al de la base de datos: " + resp[1] + "\r\n");
			}

			//ROLLBACK
			report.AddLine("--- Ejecutando PostCondicion para eliminar los datos generados en la BD ---");
			System.out.println("--- Ejecutando PostCondicion para eliminar los datos generados en la BD ---\r\n");

			rollBack(report, oraResp, ppCondi, configEntidad, cuentas_generales);

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
		//return the test result
		return result;

	}

	private void rollBack(Reports report, dbWorker oraResp, PrePostCondi ppCondi, String configEntidad, String cuentas_generales) {
		//Variables
		int rta;
		String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_2.cuenta_virtual");
		String ID_TARJETA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_2.id_tarjeta");

		//Operacion 1
		rta = oraResp.oraDelete(report, "DELETE TARJETAS_HISTORIAL WHERE ID_TARJETA IN (SELECT ID_TARJETA FROM TARJETAS WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_TARJETA NOT IN (" + ID_TARJETA + "))", configEntidad);
		ppCondi.postCondicionBD(report, rta);
		//Operacion 2
		rta = oraResp.oraUpdate(report, "UPDATE SOCIOS SET ID_TARJETA_VIGENTE = NULL WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
		ppCondi.postCondicionBD(report, rta);
		//Operacion 3
		rta = oraResp.oraDelete(report, "DELETE TARJETAS WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_TARJETA NOT IN (" + ID_TARJETA + ")", configEntidad);
		ppCondi.postCondicionBD(report, rta);
		//Operacion 4
		rta = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_TARJETA = " + ID_TARJETA, configEntidad);
		ppCondi.postCondicionBD(report, rta);	
		//Operacion 5
		rta = oraResp.oraUpdate(report, "UPDATE SOCIOS SET ID_TARJETA_VIGENTE = 28 WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
		ppCondi.postCondicionBD(report, rta);	
	}

}
