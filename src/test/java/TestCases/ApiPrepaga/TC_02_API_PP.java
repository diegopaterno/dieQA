package TestCases.ApiPrepaga;

import java.nio.file.Files;
import java.nio.file.Paths;
import static org.hamcrest.Matchers.*;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.WebDriver;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.PrePostCondi;
import Repositories.Repo_Variables;
import Tools.BodyHelper;
import Tools.apiWorker;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class TC_02_API_PP {
	WebDriver driver;

	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String cuentas_generales) {
		//Validation var
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring");

			//SET VARIABLES
			String path = "./API_Requests/PrePagas/TC_02_Alta_Cuenta_Fisica.txt";
			String token = "";
			String bodyData = "";
			String respBody = "";
			String rtaIdCta = "";
			String soporteFisicoEsperado = "0";
			String rtaSoporteFisico = "";
			String doc; 
			String ID_PRODUCTO = JsonPath.from(cuentas_generales).get("PARAMETRIA_CUENTAS.id_producto");
			boolean resp;
			int idCta = 0; 
			Response response;

			//SET INSTANCES
			apiWorker apiResp = new apiWorker();
			dbWorker oraResp = new dbWorker();
			Repo_Variables repoVar = new Repo_Variables();
			BodyHelper requestHelper = new BodyHelper();
			PrePostCondi ppCondi = new PrePostCondi();

			//SET ENVIRONMENT
			//Obtengo los datos de la base url lel archivo configEntidad que contiene todos los accesos a la entidad
			String base_url = JsonPath.from(configEntidad).get("TOKENPREPAGA.base_url");
			repoVar.setBaseUri(base_url);

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");
			
			//GET TOKEN
			//La configuracion de los datos está dentro del método
			try {
				token = apiResp.GetAccessTokenPP(configEntidad);
			} catch (Exception E) {
				report.AddLineAssertionError("No se obtuvo un token.<br>Error: " + E);
				System.out.println("##[warning] : No se obtuvo un token.\r\n Error: " + E);
			}

			//GET POST BODY FROM FILE
			try {
				bodyData = new String(Files.readAllBytes(Paths.get(path)));	
				
				//Se muestra el body en el reporte
				report.AddLine("Request body:<br>" + bodyData);
				
			} catch (Exception E) {
				report.AddLineAssertionError("Error al abrir el archivo.<br>Error: " + E);
				System.out.println("##[warning] : Error al abrir el archivo.\r\nError: " + E);
			}

			//MODIFICO BODY (DNI y CUIL)
			bodyData = requestHelper.generadorBodyDocCuil(bodyData);

			//GET DOC FROM BODY
			doc = JsonPath.from(bodyData).get("Documento.Numero");
			report.AddLine("El nuevo documento es: " + doc);
			System.out.println("\r\nEl nuevo documento es: " + doc + "\r\n");

			//POST - Alta de cuenta Prepaga con Tarjeta Fisica - Datos validos
			if (!bodyData.isEmpty() && !token.isEmpty())
			{
			response = apiResp.postMethod(repoVar, "/api/Productos/" + ID_PRODUCTO + "/", "Cuentas", bodyData, token);
				
			//Se reporta el Status Code
			report.AddLine("Status Code: " + String.valueOf(response.getStatusCode()));
			System.out.println("Status Code: " + String.valueOf(response.getStatusCode()) + "\r\n");
				
			//Se valida el status Code
			if(response.getStatusCode()!=201) {
					report.AddLineAssertionError("Fallo la validacion del StatusCode<br>Esperado: 201 - Obtenido: " + String.valueOf(response.getStatusCode()));
					System.out.println("##[warning] : Fallo la validacion del StatusCode:\r\nEsperado: 200 - Obtenido: " + String.valueOf(response.getStatusCode()) + "\r\n");
					MatcherAssert.assertThat("Validacion del Status code", String.valueOf(response.getStatusCode()), equalTo("201"));
			}
				
			//Se obtiene el body del response
			respBody = response.getBody().asPrettyString();
				
			//Se muestra response en el reporte
			report.AddLine("Response: " + respBody);
			idCta = JsonPath.from(respBody).get("Data.IdCuenta");

			//Se reporta el ID de Cuenta
			report.AddLine("Se genero el ID_CUENTA:: " + String.valueOf(idCta));
			System.out.println("Se genero el ID_CUENTA:: " + String.valueOf(idCta) + "\r\n");
			}
			
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");

			//DATABASE - VERIFICA ALTA
			if (idCta != 0) 
			{
				rtaIdCta = oraResp.oraValidaAlta(report, doc, configEntidad);
				report.AddLine("Se dio de alta correctamente la cuenta: " + rtaIdCta + " impactando en las tablas CUENTAS, SOCIOS, PERSONAS, TARJETAS y TARJETAS_HISTORIAL");
				System.out.println("##[section] : Se dio de alta correctamente la cuenta: " + rtaIdCta + " impactando en las tablas CUENTAS, SOCIOS, PERSONAS, TARJETAS y TARJETAS_HISTORIAL\r\n");
			} else {
				report.AddLineAssertionError("Error: No se genero el alta de cuenta");
				System.out.println("##[warning] : Error: No se genero el alta de cuenta\r\n");
				
			//Si falla la validacion el resultado de la prueba es false
				result = false;
			}

			//ADD VALIDATIONS
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
			
			if (idCta != Integer.valueOf(rtaIdCta)) {
				report.AddLineAssertionError("Error<br> El id generado por la API AltaCuentaFisica: " + idCta + " es diferente al cargado en la base: " + rtaIdCta);
				System.out.println("##[warning] : Error:\r\n El id generado por la API AltaCuentaFisica: " + idCta + " es diferente al cargado en la base: " + rtaIdCta + "\r\n");
				//Si falla la validacion el resultado de la prueba es false
				result = false;
			} else {
				report.AddLine("Validacion exitosa:<br>El id generado por la API AltaCuentaFisica: " + idCta + " es igual al cargado en la base: " + rtaIdCta + "\r\n");
				System.out.println("##[section] : Validacion exitosa:\r\nEl id generado por la API AltaCuentaFisica: " + idCta + " es igual al cargado en la base: " + rtaIdCta + "\r\n");				
			}
			
			
			//DATABASE - VERIFICACION TIPO DE TARJETA
			rtaSoporteFisico = oraResp.oraValidaTipoTarjeta(report, String.valueOf(idCta), configEntidad);
			report.AddLine("Tipo de tarjeta en BD: " + rtaSoporteFisico);
			System.out.println("Tipo de tarjeta en BD: " + rtaSoporteFisico + "\r\n");

			
			//ADD VALIDATIONS
			if (soporteFisicoEsperado.equals(rtaSoporteFisico)) {
				report.AddLine("Validacion exitosa:<br>El SOPORTE_FISICO esperado es: " + soporteFisicoEsperado + " es igual al de la base de datos: " + rtaSoporteFisico + "\r\n");
				System.out.println("##[section] : Validacion exitosa:\r\nEl SOPORTE_FISICO esperado es: " + soporteFisicoEsperado + " es igual al de la base de datos: " + rtaSoporteFisico + "\r\n");	

			} else {
				report.AddLineAssertionError("Error:<br>El SOPORTE_FISICO esperado es: " + soporteFisicoEsperado + " es diferente al de la base de datos: " + rtaSoporteFisico);
				System.out.println("##[warning] : Error:\r\nEl SOPORTE_FISICO esperado es: " + soporteFisicoEsperado + " es diferente al de la base de datos: " + rtaSoporteFisico + "\r\n");
			
			//Si falla la validacion el resultado de la prueba es false
				result = false;
			}
			
			
			//ROLLBACK
			report.AddLine("Se procede a la eliminacion de los datos generados...");
			System.out.println("Se procede a la eliminacion de los datos generados...\r\n");
			
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
			resp = oraResp.oraBorrarCuenta(report, rtaIdCta, doc, configEntidad, cuentas_generales);
			ppCondi.rollBackBorrarId(report, rtaIdCta, doc, resp);
			
			/*if ((resp=oraResp.oraBorraId(rtaIdCta, doc))) {
				report.AddLine("Eliminacion del dato exitosa");
				System.out.println("Eliminacion del dato exitosa");
			} else {
				report.AddLineAssertionError("Error al eliminar la cuenta; realizar su eliminacion manual");
				report.AddLineAssertionError("Datos para eliminacion manual:<br>ID de CUENTA: " + rtaIdCta + "<br>DOCUMENTO: " + doc);
				System.out.println("Error al eliminar la cuenta; realizar su eliminacion manual");
				System.out.println("Datos para eliminacion manual:\r\nID de CUENTA: " + rtaIdCta + "\r\nDOCUMENTO: " + doc);
			}

			//GUARDO LOS CAMBIOS EN EL ARCHIVO
			/*report.AddLine("Se guarda el archivo con los nuevos datos.");
			System.out.println("Se guarda el archivo con los nuevos datos.\r\n");

			if (requestHelper.grabaBodyFile(path, bodyData)) {
				report.AddLine("Se guardo el archivo exitosamente");
				System.out.println("Se guardo el archivo exitosamente");
			} else {
				report.AddLine("Error al guardar el archivo");
				System.out.println("Error al guardar el archivo");
			}*/
			
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
			boolean resp;
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			String rtaIdCta = "";
			String doc; 
			String bodyData = "";
			
			//GET DOC FROM BODY
			doc = JsonPath.from(bodyData).get("Documento.Numero");
			report.AddLine("El nuevo documento es: " + doc);
			System.out.println("\r\nEl nuevo documento es: " + doc + "\r\n");

		
			//ROLLBACK
			report.AddLine("Se procede a la eliminacion de los datos generados...");
			System.out.println("Se procede a la eliminacion de los datos generados...\r\n");
			
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
			resp = oraResp.oraBorrarCuenta(report, rtaIdCta, doc, configEntidad, cuentas_generales);
			ppCondi.rollBackBorrarId(report, rtaIdCta, doc, resp);

			result = false;
		}

		return result;
	}

}
