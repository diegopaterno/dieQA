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
 
public class TC_24_API_PP{
	WebDriver driver;

	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
		//Validation var
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_24_API_PP\r\n");
			
			//SET VARIABLES
			String path = "./API_Requests/PrePagas/TC_24_Editar_Datos_De_Cuenta_Domicilios.txt";
			String token = "";
			String bodyData = "";
			String resp = "";
			int rta = 0;
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_3.cuenta_virtual");
			String ID_PRODUCTO = JsonPath.from(cuentas_generales).get("PARAMETRIA_CUENTAS.id_producto");
			Response response;
			
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
			
			rta = oraResp.oraUpdate(report, "UPDATE DOMICILIOS\r\n"
					+ "SET ID_PAIS = 32, ID_PROVINCIA = 2, ID_CODIGO_POSTAL = 5728, DIRECCION = 'San Marco', NUMERO = 5896, PISO = 3, DEPTO = 'A',\r\n"
					+ "REFERENCIA = 'Oficinas GP', TELEFONO = '111561475812', LOCALIDAD = 'Ezeiza' \r\n"
					+ "WHERE ID_DOMICILIO IN ((SELECT ID_DOMICILIO_LEGAL FROM PERSONAS WHERE ID_PERSONA = (SELECT ID_PERSONA FROM SOCIOS WHERE ID_CUENTA = " + ID_CUENTA + ")), (SELECT ID_DOMICILIO_CORRESPONDENCIA FROM CUENTAS WHERE ID_CUENTA = " + ID_CUENTA + "))\r\n"
					+ "", configEntidad);
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
				report.AddLine("Error al abrir el archivo.<br>Error: " + E);
				System.out.println("##[warning] : Error al abrir el archivo.\r\nError: " + E + "\r\n");
			}

			//PUT - Cambio de estado Tarjeta - NormalHabilitada
			if (!bodyData.isEmpty() && !token.isEmpty())
			{
				response = apiResp.putMethod(repoVar, "/api/Productos/" + ID_PRODUCTO + "/Cuentas/" + ID_CUENTA, "", bodyData, token);
				//Se reporta el Status Code
				report.AddLine("Status Code: " + String.valueOf(response.getStatusCode()));
				System.out.println("Status Code: " + String.valueOf(response.getStatusCode()) + "\r\n");
				//Se valida el status code
				if(response.getStatusCode()!=200) {
					report.AddLineAssertionError("Fallo la validacion del StatusCode<br>Esperado: 200 - Obtenido: " + String.valueOf(response.getStatusCode()));
					System.out.println("##[warning] : Fallo la validacion del StatusCode:\r\nEsperado: 200 - Obtenido: " + String.valueOf(response.getStatusCode()) + "\r\n");
					MatcherAssert.assertThat("Validacion: Status code", String.valueOf(response.getStatusCode()), equalTo("200"));				
				}
			} else {
				System.out.println("##[warning] : No se obtuvo un token\r\n");
				report.AddLineAssertionError("No se obtuvo un token");
				result = false;
			}
			
			//ADD VALIDATIONS
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
			
			resp = oraResp.oraOneQuery(report, "SELECT COUNT (P.ID_PERSONA) FROM PERSONAS P \r\n"
					+ "INNER JOIN DOMICILIOS D ON D.ID_DOMICILIO = P.ID_DOMICILIO_LEGAL \r\n"
					+ "INNER JOIN CUENTAS C ON D.ID_DOMICILIO = C.ID_DOMICILIO_CORRESPONDENCIA \r\n"
					+ "WHERE  P.ID_PERSONA = (SELECT ID_PERSONA FROM SOCIOS WHERE ID_CUENTA = " + ID_CUENTA + ") \r\n"
					+ "AND D.ID_PROVINCIA = 4 \r\n"
					+ "AND D.ID_CODIGO_POSTAL = 8887 \r\n"
					+ "AND D.DIRECCION = 'Chopin' \r\n"
					+ "AND D.NUMERO = 24 \r\n"
					+ "AND D.PISO = 3 \r\n"
					+ "AND D.DEPTO = 'A' \r\n"
					+ "AND D.REFERENCIA = 'Oficinas GP' \r\n"
					+ "AND D.TELEFONO = 11147896325 \r\n"
					+ "AND D.LOCALIDAD = 'CABA'", configEntidad);
			
			if(!resp.equals("1")) {
				report.AddLineAssertionError("Error: No se modificaron los datos en las tablas PERSONAS y DOMICILIOS");
				System.out.println("##[warning] : Error: No se modificaron los datos en las tablas PERSONAS y DOMICILIOS\r\n");
				//Si falla la validacion el resultado de la prueba es false
				result = false;	
			} else {
				report.AddLine("Validacion exitosa:<br>Se modificaron correctamente los datos en las tablas PERSONAS y DOMICILIOS");
				System.out.println("##[section] : Validacion exitosa:\r\nSe modificaron correctamente los datos en las tablas PERSONAS y DOMICILIOS\r\n");
			}
			
			//ROLLBACK
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
			
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
		String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_3.cuenta_virtual");
		
		//Operacion 1
		rta = oraResp.oraUpdate(report, "UPDATE DOMICILIOS\r\n"
				+ "SET ID_PAIS = 32, ID_PROVINCIA = 2, ID_CODIGO_POSTAL = 5728, DIRECCION = 'San Marco', NUMERO = 5896, PISO = 3, DEPTO = 'A',\r\n"
				+ "REFERENCIA = 'Oficinas GP', TELEFONO = '111561475812', LOCALIDAD = 'Ezeiza' \r\n"
				+ "WHERE ID_DOMICILIO IN ((SELECT ID_DOMICILIO_LEGAL FROM PERSONAS WHERE ID_PERSONA = (SELECT ID_PERSONA FROM SOCIOS WHERE ID_CUENTA = " + ID_CUENTA + ")), (SELECT ID_DOMICILIO_CORRESPONDENCIA FROM CUENTAS WHERE ID_CUENTA = " + ID_CUENTA + "))\r\n"
				+ "", configEntidad);
		ppCondi.postCondicionBD(report, rta);
	}
	
}
