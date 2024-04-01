package TestCases.ApiPrepaga;

import static org.hamcrest.Matchers.equalTo;

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

public class TC_17_API_PP{
	WebDriver driver;

	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
		//Validation var
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");

			System.out.println("Configuring TC_17_API_PP\r\n");

			//SET VARIABLES
			String token = "";
			String respBody = "";
			int secuencia = 0;
			String referencia = "";
			String tipoTarjeta = "";
			String[] resp = new String[3];
			Response response;
			String tarjetaDenominacion = "";
			String estado = "";
			String tarjetaRenovacionFecha = "";
			int rta = 0;
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_3.cuenta_virtual");
			String ID_PRODUCTO = JsonPath.from(cuentas_generales).get("PARAMETRIA_CUENTAS.id_producto");
			//Resultados esperados
			String tarjetaDenominacionEsperado = "Roma Mendoza";
			String estadoEsperado = "NormalHabilitada";
			String tarjetaRenovacionFechaEsperado = "2024-10-31T00:00:00-03:00";
			
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

			rta = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0, SOPORTE_FISICO = 1, VENCIMIENTO = TO_DATE ('31/10/2024','DD/MM/YYYY') WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			ppCondi.preCondicionBD(report, rta);

			rta = oraResp.oraUpdate(report, "UPDATE SOCIOS SET NOMBRE_EMBOZADO = 'MENDOZA ROMA' WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			ppCondi.preCondicionBD(report, rta);			
			
			//GET TOKEN
			try {
				token = apiResp.GetAccessTokenPP(configEntidad);
			} catch (Exception E) {
				report.AddLineAssertionError("No se obtuvo un token.<br>Error: " + E);
				System.out.println("##[warning] : No se obtuvo un token.\r\nError: " + E + "\r\n");
			}

			//GET - Consulta de Tarjeta
			if (!token.isEmpty())
			{
				response = apiResp.getMethod(repoVar, "/Api/Productos/" + ID_PRODUCTO + "/Cuentas/" + ID_CUENTA + "/", "Tarjetas?tarjetaCompleta=false", token);
				//Se reporta el Status Code
				report.AddLine("Status Code: " + String.valueOf(response.getStatusCode()));
				System.out.println("Status Code: " + String.valueOf(response.getStatusCode()) + "\r\n");
				//Se valida el status code
				if(response.getStatusCode()!=200) {
					report.AddLineAssertionError("Fallo la validacion del StatusCode<br>Esperado: 200 - Obtenido: " + String.valueOf(response.getStatusCode()));
					System.out.println("##[warning] : Fallo la validacion del StatusCode:\r\nEsperado: 200 - Obtenido: " + String.valueOf(response.getStatusCode()) + "\r\n");
					MatcherAssert.assertThat("Validacion del Status code", String.valueOf(response.getStatusCode()), equalTo("200"));
				}
				//Se obtiene el body del response
				respBody = response.getBody().asPrettyString();
				//Se muestra response en el reporte
				report.AddLine("Response: " + respBody);

				//Se obtienen datos y se informan
				secuencia = JsonPath.from(respBody).get("Data.Resultado[0].Secuencia");
				report.AddLine("El Nro de Secuencia es: " + secuencia);
				System.out.println("El Nro de Secuencia es: " + secuencia + "\r\n");

				referencia = JsonPath.from(respBody).get("Data.Resultado[0].Referencia");
				report.AddLine("El Nro de Tarjeta Enmascarada es: " + referencia);
				System.out.println("El Nro de Tarjeta Enmascarada es: " + referencia + "\r\n");

				tipoTarjeta = JsonPath.from(respBody).get("Data.Resultado[0].TipoTarjeta");
				report.AddLine("El Soporte Físico es: " + tipoTarjeta);
				System.out.println("El Soporte Físico es: " + tipoTarjeta + "\r\n");
				
				tarjetaDenominacion = JsonPath.from(respBody).get("Data.Resultado[0].Denominacion");
				report.AddLine("El nombre de embozado es: " + tarjetaDenominacion);
				System.out.println("El nombre de embozado es: " + tarjetaDenominacion + "\r\n");
				
				estado = JsonPath.from(respBody).get("Data.Resultado[0].Estado");
				report.AddLine("El estado de la Tarjeta es: " + estado);
				System.out.println("El estado de la Tarjeta es: " + estado + "\r\n");
				
				tarjetaRenovacionFecha = JsonPath.from(respBody).get("Data.Resultado[0].Renovacion");
				report.AddLine("Fecha de vencimiento de la tarjeta: " + tarjetaRenovacionFecha);
				System.out.println("Fecha de vencimiento de la tarjeta: " + tarjetaRenovacionFecha + "\r\n");
				
			} else {
				System.out.println("##[warning] : No se obtuvo un token\r\n");
				report.AddLineAssertionError("No se obtuvo un token");
				result = false;
			}

			//ADD VALIDATIONS
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
			
			resp = oraResp.oraThreeQuery(report, "SELECT SUBSTR (NRO_TARJETA_ENMASCARADA,13,4) as NRO_TARJETA_ENMASCARADA, SECUENCIA, SOPORTE_FISICO FROM TARJETAS WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);

			if (!String.valueOf(secuencia).equals(resp[1])) {
				report.AddLineAssertionError("Error:<br>El Nro de Secuencia es: " + secuencia + "; se esperaba: " + resp[1]);
				System.out.println("##[warning] : Error:<br>El Nro de Secuencia es: " + secuencia + "; se esperaba: " + resp[1]);
				//Si falla la validacion el resultado de la prueba es false
				result = false;				
			} else {
				report.AddLine("Validacion exitosa:<br>El Nro de Secuencia es: " + secuencia + " y es igual al de la base: " + resp[1]);
				System.out.println("##[section] : Validacion exitosa:\r\n>El Nro de Secuencia es: " + secuencia + " es igual al de la base: " + resp[1] + "\r\n");
			}

			if (!referencia.equals(resp[0])) {
				report.AddLineAssertionError("Error:<br>El Nro de Tarjeta Enmascarada es: " + referencia + "; se esperaba: " + resp[0]);
				System.out.println("##[warning] : Error:<br>El Nro de Tarjeta Enmascarada es: " + referencia + "; se esperaba: " + resp[0]);
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			} else {
				report.AddLine("Validacion exitosa:<br>El Nro de Tarjeta Enmascarada es: " + referencia + " y es igual al de la base: " + resp[0]);
				System.out.println("##[section] : Validacion exitosa:\r\n>El Nro de Tarjeta Enmascarada es: " + referencia + " es igual al de la base: " + resp[0] + "\r\n");
			}

			if (!tipoTarjeta.equals(resp[2])) {
				report.AddLineAssertionError("Error:<br>El Soporte Físico es: " + tipoTarjeta + "; se esperaba: " + resp[2]);
				System.out.println("##[warning] : Error:<br>El Soporte Físico es: " + tipoTarjeta + "; se esperaba: " + resp[2]);
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			} else {
				report.AddLine("Validacion exitosa:<br>El Soporte Físico es: " + tipoTarjeta + " y es igual al de la base: " + resp[2]);
				System.out.println("##[section] : Validacion exitosa:\r\n>El Soporte Físico es: " + tipoTarjeta + " es igual al de la base: " + resp[2] + "\r\n");
			}	
			
			if (!tarjetaDenominacion.equals(tarjetaDenominacionEsperado)) {
				report.AddLineAssertionError("Error:<br>El nombre de embozado es: " + tarjetaDenominacion + "y se esperaba: " + tarjetaDenominacionEsperado);
				System.out.println("##[warning] : Error:<br>El nombre de embozado es: " + tarjetaDenominacion + "y se esperaba: " +tarjetaDenominacionEsperado + "\r\n");
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			} else {
				report.AddLine("Validacion exitosa:<br>El nombre de embozado es: " + tarjetaDenominacion + " y es igual al esperado: " +tarjetaDenominacionEsperado);
				System.out.println("##[section] : Validacion exitosa:\r\n>El nombre de embozado es: " + tarjetaDenominacion + " es igual al esperado: " + tarjetaDenominacionEsperado + "\r\n");
			}
			
			if (!estado.equals(estadoEsperado)) {
				report.AddLineAssertionError("Error:<br>El Estado de la tarjeta es: " + estado + "; se esperaba: " + estadoEsperado);
				System.out.println("##[warning] : Error:<br>El Estado de la tarjeta es: " + estado + "; se esperaba: " + estadoEsperado+ "\r\n");
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			} else {
				report.AddLine("Validacion exitosa:<br>El Estado de la tarjeta es: " + estado + " y es igual al resultado esperado: " + estadoEsperado);
				System.out.println("##[section] : Validacion exitosa:\r\n>El Estado de la tarjeta es: " + estado + " es igual al resultado esperado: " + estadoEsperado + "\r\n");
			}	
			
			if (!tarjetaRenovacionFecha.equals(tarjetaRenovacionFechaEsperado)) {
				report.AddLineAssertionError("Error:<br>La fecha de vencimiento es: " + tarjetaRenovacionFecha + "; se esperaba: " + tarjetaRenovacionFechaEsperado);
				System.out.println("##[warning] : Error:<br>La fecha de vencimiento es: " + tarjetaRenovacionFecha + "; se esperaba: " + tarjetaRenovacionFechaEsperado+ "\r\n");
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			} else {
				report.AddLine("Validacion exitosa:<br>La fecha de vencimiento es: " + tarjetaRenovacionFecha + " y es igual al resultado esperado: " + tarjetaRenovacionFechaEsperado);
				System.out.println("##[section] : Validacion exitosa:\r\n>La fecha de vencimiento es: " + tarjetaRenovacionFecha + " es igual al resultado esperado: " + tarjetaRenovacionFechaEsperado + "\r\n");
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
		String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_3.cuenta_virtual");
		String ID_TARJETA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_3.id_tarjeta");
		
		//Operacion 1
		rta = oraResp.oraDelete(report, "DELETE TARJETAS_HISTORIAL WHERE ID_TARJETA IN (SELECT ID_TARJETA FROM TARJETAS WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_TARJETA NOT IN (" + ID_TARJETA + "))", configEntidad);
		
		//Operacion 2
		rta = oraResp.oraUpdate(report, "UPDATE SOCIOS SET ID_TARJETA_VIGENTE = NULL WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
		
		//Operacion 3
		rta = oraResp.oraDelete(report, "DELETE TARJETAS WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_TARJETA NOT IN (" + ID_TARJETA + ")", configEntidad);
		
		//Operacion 4
		rta = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_TARJETA = " + ID_TARJETA, configEntidad);
	
		//Operacion 5
		rta = oraResp.oraUpdate(report, "UPDATE SOCIOS SET ID_TARJETA_VIGENTE = " + ID_TARJETA + " WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
	
	}
	
}