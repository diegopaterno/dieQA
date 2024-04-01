package TestCases.ApiPrepaga;

import static org.hamcrest.Matchers.*;

import java.io.FileWriter;
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

public class TC_16_API_PP{
	WebDriver driver;

	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
		//Validation var
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");

			System.out.println("Configuring TC_16_API_PP\r\n");

			//SET VARIABLES
			String path = "./API_Requests/PrePagas/TC_16_Editar_Datos_De_Cuenta.txt";
			String token = "";
			String bodyData = "";
			String resp = "";
			int rta = 0;
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_3.cuenta_virtual");
			String ID_PRODUCTO = JsonPath.from(cuentas_generales).get("PARAMETRIA_CUENTAS.id_producto");
		    String ID_SUCURSAL_PRECONDI = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.id_sucursal_original");
		    String ID_GRUPO_AFINIDAD_PRECONDI = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.id_grupo_afinidad_original"); 
		    String ID_SUCURSAL_NUEVO = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.id_sucursal_cambio");
		    String ID_GRUPO_AFINIDAD_NUEVO = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.id_grupo_afinidad_cambio");
		    String jsonData = "";
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

			rta = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_SUCURSAL = " + ID_SUCURSAL_PRECONDI + ", ID_GRUPO_AFINIDAD = " + ID_GRUPO_AFINIDAD_PRECONDI + ",ID_POSICION_IMPOSITIVA = 1,\r\n"
					+ "CUENTA_EXTERNA = " + ID_CUENTA + ",ENTREGA_TARJETA = 2 WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
			ppCondi.preCondicionBD(report, rta);

			rta = oraResp.oraUpdate(report, "UPDATE PERSONAS SET ID_TIPO_DOCUMENTO = 1,SEXO = 'F',ESTADO_CIVIL = 0,\r\n"
					+ "FECHA_NACIMIENTO = to_date('13/10/1988 00:00:00','DD/MM/RRRR HH24:MI:SS'), \r\n"
					+ "MAIL = 'rmendoza@lol.com',APELLIDO = 'Mendoza',NOMBRE = 'Roma',\r\n"
					+ "ID_PAIS_NACIMIENTO = 32 WHERE ID_PERSONA = (SELECT ID_PERSONA FROM SOCIOS WHERE ID_CUENTA = " + ID_CUENTA + ")", configEntidad);
			ppCondi.preCondicionBD(report, rta);
			
			//Actualizo archivo json con los datos de la sucursal y grupo de afinidad
			
			jsonData =  new String(Files.readAllBytes(Paths.get(path)));
			
			report.AddLine("Archivo json actual: " + jsonData);
			System.out.println("Archivo json actual: \r\n" + jsonData + "\r\n");
			
			//Leo el json actual para obtener el id de sucursal y el id de grupo de afinidad
			int sucursalActual = JsonPath.from(jsonData).get("SucursalEmisora");
			report.AddLine("El ID de la Sucursal del archivo actual es: " + sucursalActual);
			System.out.println("El ID de la Sucursal del archivo actual es: " + sucursalActual + "\r\n");
			
			int grupoAfinidadActual = JsonPath.from(jsonData).get("GrupoAfinidad");
			report.AddLine("El ID del Grupo de Afinidad del archivo actual es: " + grupoAfinidadActual);
			System.out.println("El ID del Grupo de Afinidad del archivo actual es: " + grupoAfinidadActual + "\r\n");
			
			//Reemplazo los valores actuales por los valores nuevos
			String jsonDataModif = jsonData.replace("\"SucursalEmisora\": " + sucursalActual ,"\"SucursalEmisora\": " + ID_SUCURSAL_NUEVO);			
			jsonDataModif = jsonDataModif.replace("\"GrupoAfinidad\": " + grupoAfinidadActual ,"\"GrupoAfinidad\": " + ID_GRUPO_AFINIDAD_NUEVO);
			
			report.AddLine("Se procede a modificar el archivo json actual");
			System.out.println("Se procede a modificar el archivo json actual\r\n");
			report.AddLine("Archivo json actualizado: " + jsonDataModif);
			System.out.println("Archivo json actualizado: \r\n\r\n" + jsonDataModif + "\r\n");
			
			//Grabo en el archivo el nuevo json generado
			FileWriter archivoNuevo = new FileWriter(path);
			archivoNuevo.write(jsonDataModif);
			archivoNuevo.close();
			
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

			//PUT - Editar Datos de cuenta -Datos principales y Titular
			
			if (!bodyData.isEmpty() && !token.isEmpty())
			{
				bodyData=bodyData.replace("{{IDCTAEXT}}", ID_CUENTA);
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
			
			resp = oraResp.oraOneQuery(report, "SELECT COUNT (*) FROM CUENTAS WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_SUCURSAL = " + ID_SUCURSAL_NUEVO + " AND ID_GRUPO_AFINIDAD = " + ID_GRUPO_AFINIDAD_NUEVO + " \r\n"
					+ "AND ID_POSICION_IMPOSITIVA = 1 AND CUENTA_EXTERNA = " + ID_CUENTA + " AND ENTREGA_TARJETA = 2", configEntidad);

			if(!resp.equals("1")) {
				report.AddLineAssertionError("Error: No se modificaron los datos en la tabla CUENTAS");
				System.out.println("##[warning] : Error: No se modificaron los datos en la tabla CUENTAS\r\n");
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			} else {
				report.AddLine("Validacion exitosa:<br>Se modificaron correctamente los datos en la tabla CUENTAS");
				System.out.println("##[section] : Validacion exitosa:\r\nSe modificaron correctamente los datos en la tabla CUENTAS\r\n");
			}

			resp = oraResp.oraOneQuery(report, "SELECT COUNT (P.ID_PERSONA) FROM PERSONAS P \r\n"
					+ "WHERE  P.ID_PERSONA = (SELECT ID_PERSONA FROM SOCIOS WHERE ID_CUENTA = " + ID_CUENTA + ") \r\n"
					+ "AND P.ID_TIPO_DOCUMENTO = 1  \r\n"
					+ "AND P.ID_PAIS_NACIMIENTO = 276 \r\n"
					+ "AND P.SEXO = 'F' \r\n"
					+ "AND P.ESTADO_CIVIL = 0 \r\n"
					+ "AND P.FECHA_NACIMIENTO = TO_DATE ('30/08/1988','DD/MM/YYYY')\r\n"
					+ "AND P.MAIL = 'TC16API@gmail.com' \r\n"
					+ "AND P.APELLIDO = 'NO BORRAR' \r\n"
					+ "AND P.NOMBRE = 'TC_16_API'", configEntidad);

			if(!resp.equals("1")) {
				report.AddLineAssertionError("Error: No se modificarion los datos en la tabla PERSONAS");
				System.out.println("##[warning] : Error: No se modificarion los datos en la tabla PERSONAS\r\n");
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			} else {
				report.AddLine("Validacion exitosa:<br>Se modificarion correctamente los datos en la tabla PERSONAS");
				System.out.println("##[section] : Validacion exitosa:\r\nSe modificarion correctamente los datos en la tabla PERSONAS\r\n");
			}

			//ROLLBACK
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			rollBack(report, oraResp, ppCondi, configEntidad, cuentas_generales, ID_SUCURSAL_PRECONDI, ID_GRUPO_AFINIDAD_PRECONDI);

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

	private void rollBack(Reports report, dbWorker oraResp, PrePostCondi ppCondi, String configEntidad, String cuentas_generales, String ID_SUCURSAL_PRECONDI, String ID_GRUPO_AFINIDAD_PRECONDI) {
		//Variables
		int rta;
		String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_VIRTUAL_3.cuenta_virtual");
		
		//Operacion 1
		rta = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_SUCURSAL = " + ID_SUCURSAL_PRECONDI + ", ID_GRUPO_AFINIDAD = " + ID_GRUPO_AFINIDAD_PRECONDI + ",ID_POSICION_IMPOSITIVA = 1,\r\n"
				+ "CUENTA_EXTERNA = " + ID_CUENTA + ",ENTREGA_TARJETA = 2 WHERE ID_CUENTA = " + ID_CUENTA, configEntidad);
		ppCondi.postCondicionBD(report, rta);

		//Operacion 2
		rta = oraResp.oraUpdate(report, "UPDATE PERSONAS SET ID_TIPO_DOCUMENTO = 1,SEXO = 'F',ESTADO_CIVIL = 0,\r\n"
				+ "FECHA_NACIMIENTO = to_date('13/10/1988 00:00:00','DD/MM/RRRR HH24:MI:SS'), \r\n"
				+ "MAIL = 'rmendoza@lol.com',APELLIDO = 'Mendoza',NOMBRE = 'Roma',\r\n"
				+ "ID_PAIS_NACIMIENTO = 32 WHERE ID_PERSONA = (SELECT ID_PERSONA FROM SOCIOS WHERE ID_CUENTA = " + ID_CUENTA + ")", configEntidad);
		ppCondi.postCondicionBD(report, rta);
	}

}
