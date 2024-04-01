package TestCases.Infinitus.ApiOperaciones;

import static org.hamcrest.Matchers.equalTo;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.MatcherAssert;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.PrePostCondi;
import Repositories.Repo_Variables;
import Tools.BodyHelper;
import Tools.apiWorker;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;
import com.google.gson.*;
import io.restassured.response.Response;

public class API_OPERACIONES {
	
	private List<String> Status = new ArrayList<String>();
	//private String esquema;
     String opi;
	
	public API_OPERACIONES(String opi) {
		this.opi=opi;
	}
	
	
	public boolean POST(Reports report, DriverManager DM, int iteration, String name, String config_entidad2_infinitus, String TCFilesPath, String configEntidad) {
		
		System.out.println("\r\n##################################################################################################################################################################################################################"
				+ "##################################################################################################################################################################################################################\r\n");
		System.out.println("Configuring "+name+ "\r\n");
		
		
		//SET INSTANCES
		apiWorker apiResp = new apiWorker();
		dbWorker oraResp = new dbWorker();
		Repo_Variables repoVar = new Repo_Variables();
		//BodyHelper requestHelper = new BodyHelper();
		PrePostCondi ppCondi = new PrePostCondi();
		
		
		//SET VARIABLES
		boolean result = true;
		String path = "./API_Requests/Infinitus/Apis/TC_01/Body/TC_Prueba1.json";
		String token = "";
		String bodyData = "";
		String respBody = "";
		String rtaIdCta = "";
		//String soporteFisicoEsperado = "1";
		//String rtaSoporteFisico = "";
		//String doc; 
		String[] res = new String[2];
		//String f37Last6 = "0";
		//Set Reusltados Esperados
		//String de39 = "00";
		//String idAutorizacionAdquirente = "0000000";
		//String ID_PRODUCTO = JsonPath.from(cuentas_generales).get("PARAMETRIA_CUENTAS.id_producto");
		//boolean resp; 
		int idCta = 0; 
		//String idCta = "000000";
		Response response;
		
		
		try {
			
			String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));
			
			
			//Preparación del archivo .TXT del TC
			System.out.println("##### PREPARACION DEL ARCHIVO TXT DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO TXT DEL TC #####");
			System.out.println(TCFilesPath);
			String txtFile = Files.readString(Paths.get(TCFilesPath + "/Body/TC_Prueba1.json"));
			
			System.out.println("JSON FILE:");
			System.out.println(jsonFile);
			System.out.println("TXT File: ");
			System.out.println(txtFile);
			
			//JsonbObjectMapperFactory factory = Json.OBJECT_TYPE();
			
			JsonParser parser = new JsonParser();
			JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			System.out.println("JSON Object: ");
			System.out.println(jsonObject);
			JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");
			
			//JSONParser parser = new JSONParser();
			
			//Gson gson = new Gson();
			
			//JsonObject jsonObject = gson.fromJson(jsonFile, JsonObject.class);
			
			// Acceder a los elementos del JSON
		   // String valor1 = jsonObject.get("clave1").getAsString();
		    //int valor2 = jsonObject.get("clave2").getAsInt();

		    // Hacer algo con los valores obtenidos del JSON
		   // System.out.println("Valor1: " + valor1);
		   // System.out.println("Valor2: " + valor2);
			//JSONObject jsonObject = (JSONObject)parser.parse(jsonFile);
			//JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			//System.out.println("JSON Object: ");
			//System.out.println(jsonObject);
			//JSONArray validaciones = new JSONArray();
			//validaciones.add(jsonObject);
			
			//JSONArray validaciones = jsonObject.("VALIDACIONESDB");
			
			System.out.println("VALIDACIONES: ");
			System.out.println(validaciones);
			
			
			//SET ENVIRONMENT
			//Obtengo los datos de la base url lel archivo configEntidad que contiene todos los accesos a la entidad
			String base_url = JsonPath.from(config_entidad2_infinitus).get("CREARCUENTA.base_url");
			repoVar.setBaseUri(base_url);
			
			
			
			//Set esquema Base de datos
			//esquema = JsonPath.from(configEntidad).getString("ENTIDAD.esquema_db");
			
			//SET dbWorker
			oraResp.setUser(JsonPath.from(config_entidad2_infinitus).getString("DB.user"));
			oraResp.setPass(JsonPath.from(config_entidad2_infinitus).getString("DB.pass"));
			oraResp.setHost(JsonPath.from(config_entidad2_infinitus).getString("DB.host"));
			//oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad"));
			
			
			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");
			
			//EJECUCION DE METODO POST EN API
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");
			
			//Preparación del archivo JSON del TC
			System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			System.out.println(TCFilesPath);
			
			
			//GET TOKEN
			//La configuracion de los datos está dentro del método
			try {
				token = apiResp.GetAccessTokenInfinitus(config_entidad2_infinitus);
				System.out.println("en este punto este es el token: *********" + token);
			} catch (Exception E) {
				report.AddLineAssertionError("No se obtuvo un token.<br>Error: " + E);
				System.out.println("##[warning] : No se obtuvo un token.\r\nError: " + E);
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
			
			//POST - Alta de cuenta Prepaga con Tarjeta Virtual - Datos validos
			if (!bodyData.isEmpty() && !token.isEmpty())
			{
				response = apiResp.postMethodInfinitus(repoVar, "/GP.Prepagas/Api/Productos/63/Cuentas",  bodyData, token);
				System.out.println(response + "respuesta del metodo post *************************************");
				
				//Se reporta el Status Code
				report.AddLine("Status Code: " + String.valueOf(response.getStatusCode()));
				System.out.println("Status Code: " + String.valueOf(response.getStatusCode()) + "\r\n");
				//Se valida el status Code
				if (response.getStatusCode()!=201) {
					report.AddLineAssertionError("Fallo la validacion del StatusCode<br>Esperado: 201 - Obtenido: " + String.valueOf(response.getStatusCode()));
					System.out.println("##[warning] : Fallo la validacion del StatusCode:\r\nEsperado: 200 - Obtenido: " + String.valueOf(response.getStatusCode()) + "\r\n");
					MatcherAssert.assertThat("Validacion del Status code", String.valueOf(response.getStatusCode()), equalTo("201"));
				}
				//Se obtiene el body del response
				respBody = response.getBody().asPrettyString();
				//Se muestra response en el reporte
				report.AddLine("Response: " + respBody);
				idCta = JsonPath.from(respBody).get("IdCuenta");
				//Se reporta el ID de Cuenta
				report.AddLine("Se genero el ID_CUENTA: " + String.valueOf(idCta));
				System.out.println("Se genero el ID_CUENTA: " + String.valueOf(idCta)+ "\r\n");
			}
			
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
			
			/****************************PAT******************************************/
			if (idCta == idCta)
			{
				String idCta2 = String.valueOf(idCta);
				report.AddLine("Ejecucion Correcta<br>ID_CUENTA : " + idCta2);
				System.out.println("##[section] : Ejecucion Correcta\r\nID_CUENTA : " + idCta2 + "\r\n");
				//VALIDACIONES
				report.AddLine("ID_CUENTA : " + idCta2);
				System.out.println("##[section] : ID_CUENTA : " + idCta2 + "\r\n");
				result = validacionGral(oraResp, report, idCta2, validaciones, configEntidad);
			} else {
				report.AddLineAssertionError("FAIL<br>ID_CUENTA: " + res[0] + " Se esperaba: " + idCta);
				System.out.println("##[warning] : FAIL : \r\nID_CUENTA: " + res[0] + " Se esperaba: " + idCta + "\r\n");
				result = false;
			}
			

			/****************************PAT****************************************/
			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");
		
			
			
		}
		catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}
		
		
		return result;
		
	}
	
	
	/**********************************************************************************************/
	
	private boolean validacionGral(dbWorker oraResp, Reports report, String idCta2, JsonArray validaciones, String configEntidad) throws SQLException {
		//Variables
		boolean result = true;
		String queryVerf;
		
		System.out.println("##### INICIO EJECUCION DE VALIDACIONES #####");
		report.AddLine("##### INICIO EJECUCION DE VALIDACIONES #####");
		
		for(JsonElement validacion : validaciones) {
			JsonObject validacionObject = validacion.getAsJsonObject();
			queryVerf = validacionObject.get("query").getAsString();
			//queryVerf = queryVerf.replace("{{esquema}}", esquema);
			queryVerf = queryVerf.replace("{{idCta}}", idCta2);
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
			
			Validacion(oraResp, report, queryVerf, resultadosEsperados, configEntidad);
			
			System.out.println("Se sale?");
		}


		//Verificacion de todos los resultados obtenidos
		for(String state : Status)
		if (!state.equals("P")) {
			report.AddLineAssertionError("===== " + Status + " =====");;
			System.out.println("===== " + Status + " =====");
			result = false;
		}

		System.out.println("##### FIN DE EJECUCION DE VALIDACIONES #####");
		report.AddLine("##### FIN DE EJECUCION DE VALIDACIONES #####");

		return result;
	}
	
	private void Validacion(dbWorker oraResp, Reports report , String queryVerf, JsonArray resultadosEsperados, String configEntidad) throws SQLException {
		
		String validaRes[][];
		
		validaRes = oraResp.executeQuery(queryVerf, configEntidad);
		
		String resultados[][] = new String[validaRes.length][validaRes[0].length];
		
		for(JsonElement fila : resultadosEsperados) {
			int i = 0;
			System.out.println("Fila: " + fila);
			for(JsonElement valor : fila.getAsJsonArray()) {
				int j = 0;
				System.out.println("Valor: " + valor.getAsString());
				resultados[i][j] = valor.getAsString();
				j++;
			}
			i++;
		}
		
		System.out.println();
		
		for(int i = 0; i < validaRes.length; i++) {
			for(int j = 0; j < validaRes[i].length; j++) {
				System.out.println("validaRes: " + validaRes[i][j] + " - resultadoEsperado: " + resultadosEsperados.get(i).getAsJsonArray().get(j).getAsString());
				if(validaRes[i][j].equals(resultadosEsperados.get(i).getAsJsonArray().get(j).getAsString())) {
					report.AddLine("Ejecucion Correcta:<br>Se cumplieron todas las validaciones");
					System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
					Status.add("P");
				}else {
					report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
					System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
					Status.add("FAIL - Cantidad de resultados");
				}
			}
		}
	}
	
	/************************************************************************************************/
	
}
