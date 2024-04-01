package apis.wolfgang.impuestos;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import CentaJava.Core.Reports;
import Pasos.Generales.AutorizadorInterno;
import Repositories.Repo_Variables;
import Tools.apiWorker;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;

public class AlicuotaTDF {
	private List<String> Status = new ArrayList<String>();

	public boolean alicuotaTDF( Reports report, String name,String configEntidad, String entidad, String TCFilesPath) {
		// Validation var
		boolean result = true;

		try {
			// Separador
			System.out.println(
					"\r\n##################################################################################################################################################################################################################"
							+ "##################################################################################################################################################################################################################\r\n");

			System.out.println("Configuring " + name + "\r\n");

			// variables
			String bodyData = "";
			String token = "";
			String respBody = "";
			String jsonFile = "";
			String jsonFileV = "";
			String endPoint = "http://AutorizadorGw.api.qa.global.globalprocessing.net.ar/api/emision/autorizacion";
			// instancias
			apiWorker apiWorker = new apiWorker();
			dbWorker oraResp = new dbWorker();
			Repo_Variables repoVar = new Repo_Variables();
			AutorizadorInterno AI = new AutorizadorInterno();
			// configuracion de base de datos
			
			//configuro base de datos
			//CONFIGURACIÓN DATABASE
			oraResp.setUser(JsonPath.from(configEntidad).get("DB.user"));
			oraResp.setPass(JsonPath.from(configEntidad).get("DB.pass"));
			oraResp.setEntidad(JsonPath.from(configEntidad).get("ENTIDAD.id_entidad"));
			oraResp.setHost(JsonPath.from(configEntidad).get("DB.host"));
			jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));
            JsonParser parser = new JsonParser();
			JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			System.out.println("JSON Object: ");
			System.out.println(jsonObject);
			JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");
			System.out.println("VALIDACIONES: ");
			System.out.println(validaciones);
			// Obtengo la fecha actual
			String DE7 = AI.DE7();
			
			System.out.println("esta es la fecha para armar el DE7 : " + DE7);
			
			// seteo de ambiente
			AI.setEndpoint(report, repoVar, endPoint);
			System.out.println("<------ Initializating Test: " + name + " ------>\r\n");

			token = AI.getToken(report, apiWorker, configEntidad);
			System.out.println(token);			

			jsonFileV = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));	
			bodyData = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TXT/TC.txt")));
			System.out.println(bodyData);
			bodyData = bodyData.replace("{{DE7}}", DE7);
			System.out.println("este es el body data con el data element 7 actual : "+bodyData);
			
			if (!bodyData.isEmpty() && !token.isEmpty()) {

				System.out.println("##### EJECUCION DE POST CON EL TOKEN Y LA DATA DEL BODY #####");
				report.AddLine("##### EJECUCION DE POST CON EL TOKEN Y LA DATA DEL BODY #####");
				respBody = AI.post(report, repoVar, apiWorker, bodyData, token);
				System.out.println(respBody);
				Thread.sleep(5000);
				String resultadoOperacion = JsonPath.from(respBody).getString("DE39");
				System.out.println("Este es el resultado="+ resultadoOperacion +  "¿Esta bien aquí?");

				if(resultadoOperacion.equals("00")){
					System.out.println("*********OPERACION_OK**********");
				} else {
					System.out.println("*********SE VUELVE A EJECUTAR**********");
					for (int z=0; z<10; z++) {
						respBody = AI.post(report, repoVar, apiWorker, bodyData, token);
						Thread.sleep(5000);
						break;
				}
					
			}
				String responseData = JsonPath.from(respBody).getString("IdAutorizacion"); 
	
                //SE COMIENZA A VALIDAR
				
				if (!responseData.isEmpty())
				{
					report.AddLine("Ejecucion Correcta<br>ID_AUTORIZACION_EMISOR: " + responseData);
					System.out.println("##[section] : Ejecucion Correcta\r\nID_AUTORIZACION_EMISOR: " + responseData + "\r\n");
					//VALIDACIONES
					
					result = validacionGral(oraResp, report, responseData, entidad, validaciones, configEntidad);
				} else {
					report.AddLineAssertionError("FAIL<br>ID_AUTORIZACION_EMISOR: " + responseData + " Se esperaba: un numero" );
					System.out.println("##[warning] : FAIL : \r\nID_AUTORIZACION_EMISOR: " + responseData + " Se esperaba: un numero");
					result = false;
				}
				
				
			} else {
				System.out.println("No se obtuvo un token o el body no existe");
				report.AddLineAssertionError("No se obtuvo un token o el body no existe\r\n");
				result = false;

				System.out.println("<------ Finished Test: " + name + " ------>\r\n");
				// Separador
				System.out.println(
						"##################################################################################################################################################################################################################"
								+ "##################################################################################################################################################################################################################\r\n");


			}

		}

		catch (Exception e) {
			e.printStackTrace();
			report.AddLineAssertionError(e.getStackTrace()[0].toString());
			report.AddLineAssertionError(e.getMessage());
			result = false;

		}

		return result;

	}
	private boolean validacionGral(dbWorker oraResp, Reports report, String responseData, String entidad,
			JsonArray validaciones, String configEntidad) throws SQLException {
		// Variables
		boolean result = true;
		String queryVerf;

		System.out.println("##### INICIO EJECUCION DE VALIDACIONES #####");
		report.AddLine("##### INICIO EJECUCION DE VALIDACIONES #####");

		for (JsonElement validacion : validaciones) {
			JsonObject validacionObject = validacion.getAsJsonObject();
			queryVerf = validacionObject.get("query").getAsString();
			// queryVerf = queryVerf.replace("{{esquema}}", esquema);
			queryVerf = queryVerf.replace("{{id_autorizacion_emisor}}", responseData);
			JsonArray camposEsperados = validacionObject.get("camposEsperados").getAsJsonArray();
			JsonArray resultadosEsperados = validacionObject.get("valoresEsperados").getAsJsonArray();

			System.out.println("Query: " + queryVerf);
			System.out.println("Campos Esperados: " + camposEsperados);
			System.out.println("ResultadosEsperados: " + resultadosEsperados);
			System.out.println("ResultadosEsperados Size: " + resultadosEsperados.size());
			System.out.println("ResultadosEsperados Size: " + resultadosEsperados.get(0));
			System.out.println("ResultadosEsperados Size: " + resultadosEsperados.get(0).getAsJsonArray().get(0));
			System.out.println();
			System.out.println();

			Validacion(oraResp, report, queryVerf, resultadosEsperados, entidad, configEntidad);
		}

		// Verificacion de todos los resultados obtenidos
		for (String state : Status)
			if (!state.equals("P")) {
				report.AddLineAssertionError("===== " + Status + " =====");
				;
				System.out.println("===== " + Status + " =====");
				result = false;
			}

		System.out.println("##### FIN DE EJECUCION DE VALIDACIONES #####");
		report.AddLine("##### FIN DE EJECUCION DE VALIDACIONES #####");

		return result;
	}

	private void Validacion(dbWorker oraResp, Reports report, String queryVerf, JsonArray resultadosEsperados,
			String entidad, String configEntidad) throws SQLException {

		String validaRes[][];

		validaRes = oraResp.executeQuery(queryVerf, configEntidad);
		

		String resultados[][] = new String[validaRes.length][validaRes[0].length];

		for (JsonElement fila : resultadosEsperados) {
			int i = 0;
			System.out.println("Fila: " + fila);
			for (JsonElement valor : fila.getAsJsonArray()) {
				int j = 0;
				System.out.println("Valor: " + valor.getAsString());
				resultados[i][j] = valor.getAsString();
				j++;
			}
			i++;
		}

		System.out.println();

		for (int i = 0; i < validaRes.length; i++) {
			for (int j = 0; j < validaRes[i].length; j++) {
				System.out.println("validaRes: " + validaRes[i][j] + " - resultadoEsperado: "
						+ resultadosEsperados.get(i).getAsJsonArray().get(j).getAsString());
				if (validaRes[i][j].equals(resultadosEsperados.get(i).getAsJsonArray().get(j).getAsString())) {
					report.AddLine("Ejecucion Correcta:<br>Se cumplieron todas las validaciones");
					System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
					Status.add("P");
				} else {
					report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
					System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
					Status.add("FAIL - Cantidad de resultados");
				}
			}
		}
	}


}
