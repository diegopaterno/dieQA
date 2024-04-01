package apis.wolfgang;

import java.nio.file.Files;
import java.nio.file.Paths;

import CentaJava.Core.Reports;
import Pasos.Generales.AutorizadorInterno;
import Repositories.Repo_Variables;
import Tools.apiWorker;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;

//AUTORIZADOR_API clase -> traigo operaciones del aut
public class AUTORIZADOR_API {
	

	public boolean compralocal(Reports report, String name, String configEntidad, String entidad, String TCFilesPath, String endPoint, String statusCodeEsperado, String NRO_TARJETA, String VENCIMIENTO) {
		// abro la variable resultado
		boolean result = true;

		try {

			// Seteo Variables
			String token = "";

			// instancio objeto
			apiWorker apiWorker = new apiWorker();
			dbWorker oraResp = new dbWorker();
			AutorizadorInterno AutI = new AutorizadorInterno();
			Repo_Variables repoVar = new Repo_Variables();
			AutorizadorInterno AI = new AutorizadorInterno();
			
			// config. db
			oraResp.setUser(JsonPath.from(configEntidad).getString("DB.user"));
			oraResp.setUser(JsonPath.from(configEntidad).getString("DB.pass"));
			oraResp.setUser(JsonPath.from(configEntidad).getString("DB.host"));

			// Seteo de ambiente
			AutI.setEndpoint(report, repoVar, endPoint);

			// Token
			token = AI.getToken(report, apiWorker, configEntidad);

			// Llamo los archivos (Json)
			String jsonFileV = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));

			// Valido
			String bodyData = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TXT/TC.txt")));			
			//Hago un post 
			
			String respBody = AutI.post2(report, repoVar, apiWorker, bodyData, token, configEntidad, statusCodeEsperado);
			
		} catch (Exception e) {
		
			e.printStackTrace();

						report.AddLineAssertionError(e.getStackTrace()[0].toString());

						report.AddLineAssertionError(e.getMessage());

						result = false;

		}
		return result;
	}

	// Método trae Token -> apiWorker
	private String getToken(Reports report, apiWorker apiWorker) {
		String token = "";

		try {
			token = apiWorker.GetAccessTokenA();
		} catch (Exception E) {
			report.AddLineAssertionError("No se obtuvo un token.\r\nError: " + E);
			System.out.println("No se obtuvo un token.\r\nError: " + E);
		}
		return token;
	}
}
