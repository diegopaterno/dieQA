package opi.wolfgang;

import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.jdom2.JDOMException;
import org.openqa.selenium.WebDriver;

import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.Acceso;
import Pasos.Generales.NavSuperiorBatch;
import Pasos.Generales.PrePostCondi;
import Pasos.GlobalBatch.TC_07_BA_PASOS;
import Repositories.Repo_WebRepository;
import Tools.dbWorker;
import Tools.opiWorker;
import Tools.sshWorker;
import Tools.xmlWorker;
import io.restassured.path.json.JsonPath;
import com.google.gson.*;

public class AUT_OPI_OPERACIONES_ERROR {
	private List<String> Status = new ArrayList<String>();
	private String esquema;
	List<dbWorker> results = new ArrayList<>();
	//private String Status;
	WebDriver driver;
	
    String opi;
	
	public AUT_OPI_OPERACIONES_ERROR(String opi) {
		this.opi=opi;
	}
	
	public boolean COMPRA(Reports report,String name, String configEntidad, String entidad, String TCFilesPath) {

		System.out.println("\r\n##################################################################################################################################################################################################################"
				+ "##################################################################################################################################################################################################################\r\n");
		System.out.println("Configuring "+name+ "\r\n");

 

		//SET INSTANCES
		sshWorker opiCmd = new sshWorker();
		dbWorker oraResp = new dbWorker();
		PrePostCondi ppCondi = new PrePostCondi();

 

		//Set Variables
		boolean result = true;
		String res = "";
		
		//String[] res = new String[2];
		//Set Reusltados Esperados
		String idAutorizacionEmisor = "0000000";
		int rtaQuery;
		String ID_CUENTA = "10001454";
		String ID_CUENTA_EXT = "10001468";

		try {		
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Preparación del archivo JSON del TC
			System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			System.out.println(TCFilesPath);

			String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));

			//Preparación del archivo XML del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			System.out.println(TCFilesPath);
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));

			System.out.println("JSON FILE:");
			System.out.println(jsonFile);
			System.out.println("XMLFile: ");
			System.out.println(xmlFile);

			JsonParser parser = new JsonParser();

			JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			System.out.println("JSON Object: ");
			System.out.println(jsonObject);
			JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");

			System.out.println("VALIDACIONES: ");
			System.out.println(validaciones);

			//SET dbWorker
			oraResp.setUser(JsonPath.from(configEntidad).getString("DB.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DB.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DB.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad"));


			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");

			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
			Thread.sleep(1000);
			//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "compras");
			//Thread.sleep(5000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
			Thread.sleep(5000);

			System.out.println("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### FINALIZA EJECUCION DE PRECONDICIONES #####");

			//Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatus(configEntidad, opi)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi), equalTo(true));
			}

			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");

			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				//opiCmd.getOpiStatus(configEntidad, opi);

				//Thread.sleep(5000);
				
				for (int i = 0; i < 10; i++) {			
				res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				System.out.println("ejecucion numero : " + i);

				
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91") && !res.equals("57")) {

					if (!idAutorizacionEmisorJosnString.equals("0")) {
					idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiemimcaut");
					System.out.println("***********ingrese al ifFFFFFF");
						}
					break;
				}				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
				}
            System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
            JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
            System.out.println("SE PARSEA STRING A JSON");
            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacionEmisor").getAsString();
            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);



			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);

			if (res.equals("00") || res.equals("55"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones, configEntidad);
			} else {
				report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "00");
				System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "00" + "\r\n");
				result = false;
			}

 

			System.out.println("##### FIN DE EJECUCION OPI #####");
			report.AddLine("##### FIN DE EJECUCION OPI #####");

 

						
			//POSTCONDICIONES

 

			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");

 

 

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}

 

		return result;
	}
	
	/*************************compra OPI "opiVisaAutoQA"***************************************************/
	public boolean CompraLemonFondosInsuficientes(Reports report,String name, String configEntidad, String entidad, String TCFilesPath) {	
		System.out.println("\r\n##################################################################################################################################################################################################################"
				+ "##################################################################################################################################################################################################################\r\n");
		System.out.println("Configuring "+name+ "\r\n");

		//SET INSTANCES
		sshWorker opiCmd = new sshWorker();
		dbWorker oraResp = new dbWorker();
		PrePostCondi ppCondi = new PrePostCondi();

		//Set Variables
		boolean result = true;
		String res = "";
		
		//String[] res = new String[2];
		//Set Reusltados Esperados
		String idAutorizacionEmisor = "0000000";
		int rtaQuery;
		String ID_CUENTA = "2";
		String ID_CUENTA_EXT = "10015647";

		try {		
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Preparación del archivo JSON del TC
			System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			System.out.println(TCFilesPath);

			String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));

			//Preparación del archivo XML del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			System.out.println(TCFilesPath);
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));

			System.out.println("JSON FILE:");
			System.out.println(jsonFile);
			System.out.println("XMLFile: ");
			System.out.println(xmlFile);

			JsonParser parser = new JsonParser();

			JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			System.out.println("JSON Object: ");
			System.out.println(jsonObject);
			JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");

			System.out.println("VALIDACIONES: ");
			System.out.println(validaciones);

			//SET dbWorker
			oraResp.setUser(JsonPath.from(configEntidad).getString("DBLEMONQA.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DBLEMONQA.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DBLEMONQA.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_peru_visa_qa"));


			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");

			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(5000);

			System.out.println("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### FINALIZA EJECUCION DE PRECONDICIONES #####");

			//Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatusLemonQA(configEntidad)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatusLemonQa(configEntidad), equalTo(true));
			}

			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");

			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				//opiCmd.getOpiStatus(configEntidad);
				//Thread.sleep(5000);
				
				for (int i = 0; i < 10; i++) {			
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				System.out.println("ejecucion numero : " + i);

				
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91") && !res.equals("57")) {

					if (!idAutorizacionEmisorJosnString.equals("0")) {
					idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiVisaQaAuto");
					System.out.println("***********ingrese al ifFFFFFF");
						}
					break;
				}				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
				}
            System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
            JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
            System.out.println("SE PARSEA STRING A JSON");
            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacion").getAsString();
            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);



			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);

			if (res.equals("00") || res.equals("55") || res.equals("51") || res.equals("5 "))
			{
				//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
				rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 123456789 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
				ppCondi.preCondicionBD(report, rtaQuery);
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				result = validacionGralLemonQA(oraResp, report, idAutorizacionEmisor, entidad, validaciones);
			} else {
				report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "00");
				System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "00" + "\r\n");
				result = false;
			}

 

			System.out.println("##### FIN DE EJECUCION OPI #####");
			report.AddLine("##### FIN DE EJECUCION OPI #####");

 

						
			//POSTCONDICIONES

 

			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");

 

 

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}

 

		return result;
	}
	/*************************compra OPI "opiVisaAutoQA"***************************************************/
	public boolean CompraLemonFondosInsuficientesUAT(Reports report,String name, String configEntidad, String entidad, String TCFilesPath) {	
		System.out.println("\r\n##################################################################################################################################################################################################################"
				+ "##################################################################################################################################################################################################################\r\n");
		System.out.println("Configuring "+name+ "\r\n");

		//SET INSTANCES
		sshWorker opiCmd = new sshWorker();
		dbWorker oraResp = new dbWorker();
		PrePostCondi ppCondi = new PrePostCondi();

		//Set Variables
		boolean result = true;
		String res = "";
		
		//String[] res = new String[2];
		//Set Reusltados Esperados
		String idAutorizacionEmisor = "0000000";
		int rtaQuery;
		String ID_CUENTA = "2";
		String ID_CUENTA_EXT = "10015647";

		try {		
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Preparación del archivo JSON del TC
			System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			System.out.println(TCFilesPath);

			String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));

			//Preparación del archivo XML del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			System.out.println(TCFilesPath);
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));

			System.out.println("JSON FILE:");
			System.out.println(jsonFile);
			System.out.println("XMLFile: ");
			System.out.println(xmlFile);

			JsonParser parser = new JsonParser();

			JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			System.out.println("JSON Object: ");
			System.out.println(jsonObject);
			JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");

			System.out.println("VALIDACIONES: ");
			System.out.println(validaciones);

			//SET dbWorker
			oraResp.setUser(JsonPath.from(configEntidad).getString("DBLEMONUAT.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DBLEMONUAT.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DBLEMONUAT.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_peru_visa_uat"));


			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");

			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(5000);

			System.out.println("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### FINALIZA EJECUCION DE PRECONDICIONES #####");

			//Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatusLemonQA(configEntidad)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatusLemonQa(configEntidad), equalTo(true));
			}

			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");

			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				//opiCmd.getOpiStatus(configEntidad);
				//Thread.sleep(5000);
				
				for (int i = 0; i < 10; i++) {
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				System.out.println("ejecucion numero : " + i);

				
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91") && !res.equals("57")) {

					if (!idAutorizacionEmisorJosnString.equals("0")) {
					idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiVisaQaAuto");
					System.out.println("***********ingrese al ifFFFFFF");
						}
					break;
				}				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
				}
            System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
            JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
            System.out.println("SE PARSEA STRING A JSON");
            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacion").getAsString();
            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);



			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);

			if (res.equals("00") || res.equals("55") || res.equals("51") || res.equals("5 ") || res.equals("30"))
			{
				//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
				rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 123456789 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
				ppCondi.preCondicionBD(report, rtaQuery);
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				result = validacionGralLemonUAT(oraResp, report, idAutorizacionEmisor, entidad, validaciones);
			} else {
				report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "30");
				System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "30" + "\r\n");
				result = false;
			}

 

			System.out.println("##### FIN DE EJECUCION OPI #####");
			report.AddLine("##### FIN DE EJECUCION OPI #####");

 

						
			//POSTCONDICIONES

 

			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");

 

 

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}

 

		return result;
	}

	/*********************FIN COMPRA OPI VISA LEMON***********************************************************/
	
	/*********************compra con tarjeta dada de baja*********************************************/ // Reports report,String name, String configEntidad
	public boolean compraConTarjetaDadaDeBaja(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String entidad, String TCFilesPath ) {
		boolean result = true;
		try {
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");		
			//Set Variables
			String ID_ESTADO = "1";
			String dispInicial = "10000";
			String pendCompras = "0";
			String ajustePend = "0";
			String ID_CUENTA = "2";//JsonPath.from(cuentas_generales).get("CUENTA_LEMONQA_1.cuenta_fisica");
			String de39 = "14";
			String idEstado = "1";
			String disponible = "10000";
			String impPendComp = "0";
			String impAjustePend = "0";
			String res;
			boolean validaRes;
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			//Instancias
			sshWorker opiCmd = new sshWorker();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			opiWorker opiAcciones = new opiWorker();

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");
			//PECONDICIONES
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");
			opiAcciones.preCondicionLemonQA(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, ID_ESTADO, configEntidad);
			
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(5000);


			//res = opiCmd.sshSendCmd(report, "TC_17_OPI.xml", configEntidad);
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				//opiCmd.getOpiStatus(configEntidad);
				//Thread.sleep(5000);
				
				//for (int i = 0; i < 10; i++) {			
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				//System.out.println("ejecucion numero : " + i);


			if (res.equals(de39))
			{
				report.AddLine("Ejecucion Correcta<br>DE39:" + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39:" + res + "\r\n");
			} else {
				report.AddLineAssertionError("FAIL<br>DE39:" + res + " Se esperaba: " + de39);
				System.out.println("##[warning] : FAIL:\r\nDE39:" + res + " Se esperaba: " + de39 + "\r\n");
				result = false;
			}
			//	}
			
			//VALIDACIONES
			validaRes = opiAcciones.validacionAutorizacionLemonQA(report, oraResp, idEstado, disponible, impPendComp, impAjustePend, ID_CUENTA, configEntidad);
			if (!validaRes) {
				report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
				System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
				result = false;
			} else {
				report.AddLine("Ejecucion Correcta<br>Se cumplieron todas las validaciones");
				System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
			}
			
			//POSTCONDICIONES
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			opiAcciones.postCondicionLemonQA(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, ID_ESTADO, configEntidad);	

			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");
		

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}

		return result;

	}
	/*********************compra con tarjeta dada de baja*********************************************/ // Reports report,String name, String configEntidad
	public boolean compraConTarjetaDadaDeBajaUAT(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String entidad, String TCFilesPath ) {
		boolean result = true;
		try {
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");		
			//Set Variables
			String ID_ESTADO = "1";
			String dispInicial = "10000";
			String pendCompras = "0";
			String ajustePend = "0";
			String ID_CUENTA = "2";//JsonPath.from(cuentas_generales).get("CUENTA_LEMONQA_1.cuenta_fisica");
			String de39 = "14";
			String idEstado = "1";
			String disponible = "10000";
			String impPendComp = "0";
			String impAjustePend = "0";
			String res;
			boolean validaRes;
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			//Instancias
			sshWorker opiCmd = new sshWorker();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			opiWorker opiAcciones = new opiWorker();

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");
			//PECONDICIONES
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");
			opiAcciones.preCondicionLemonUAT(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, ID_ESTADO, configEntidad);
			
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(5000);


			//res = opiCmd.sshSendCmd(report, "TC_17_OPI.xml", configEntidad);
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				//opiCmd.getOpiStatus(configEntidad);
				//Thread.sleep(5000);
				
				//for (int i = 0; i < 10; i++) {			
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				//System.out.println("ejecucion numero : " + i);


			if (res.equals(de39))
			{
				report.AddLine("Ejecucion Correcta<br>DE39:" + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39:" + res + "\r\n");
			} else {
				report.AddLineAssertionError("FAIL<br>DE39:" + res + " Se esperaba: " + de39);
				System.out.println("##[warning] : FAIL:\r\nDE39:" + res + " Se esperaba: " + de39 + "\r\n");
				result = false;
			}
			//	}
			
			//VALIDACIONES
			validaRes = opiAcciones.validacionAutorizacionLemonUAT(report, oraResp, idEstado, disponible, impPendComp, impAjustePend, ID_CUENTA, configEntidad);
			if (!validaRes) {
				report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
				System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
				result = false;
			} else {
				report.AddLine("Ejecucion Correcta<br>Se cumplieron todas las validaciones");
				System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
			}
			
			//POSTCONDICIONES
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			opiAcciones.postCondicionLemonUAT(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, ID_ESTADO, configEntidad);	

			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");
		

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}

		return result;

	}
	/****************fin compra con tarjeta dada de baja*********************************************/
	/*********************compra con tarjeta vencida*********************************************/ // Reports report,String name, String configEntidad
	public boolean compraConTarjetaVencida(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String entidad, String TCFilesPath ) {
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_18_OPI\r\n");

			//Set Variables
			String dispInicial = "10000";
			String pendCompras = "0";
			String ajustePend = "0";
			String ID_CUENTA = "2";
			String de39 = "54";
			String ID_ESTADO = "1";
			String disponible = "10000";
			String impPendComp = "0";
			String impAjustePend = "0";
			String res;
			String fechaVencimientoRollback = "30/09/2024','DD/MM/YYYY";
			boolean validaRes;
			int rtaQuery = 0;
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			//Instancias
			sshWorker opiCmd = new sshWorker();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			opiWorker opiAcciones = new opiWorker();

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PECONDICIONES
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");

			opiAcciones.preCondicionCMyVtoLemonQA(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			
			//EJECUCION OPI
			//res = opiCmd.sshSendCmd(report, "TC_18_OPI.xml", configEntidad);
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(5000);


			//res = opiCmd.sshSendCmd(report, "TC_17_OPI.xml", configEntidad);
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				//opiCmd.getOpiStatus(configEntidad);
				//Thread.sleep(5000);
				
				//for (int i = 0; i < 10; i++) {			
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				//System.out.println("ejecucion numero : " + i);


			if (res.equals(de39))
			{
				report.AddLine("Ejecucion Correcta<br>DE39:" + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39:" + res + "\r\n");
			} else {
				report.AddLineAssertionError("FAIL<br>DE39:" + res + " Se esperaba: " + de39);
				System.out.println("##[warning] : FAIL:\r\nDE39:" + res + " Se esperaba: " + de39 + "\r\n");
				result = false;
			}
			
			//VALIDACIONES
			validaRes = opiAcciones.validacionAutorizacionLemonQA(report, oraResp, ID_ESTADO, disponible, impPendComp, impAjustePend, ID_CUENTA, configEntidad);
			if (!validaRes) {
				report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
				System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
				result = false;
			} else {
				report.AddLine("Ejecucion Correcta<br>Se cumplieron todas las validaciones");
				System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
			}
			
			//POSTCONDICIONES
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			opiAcciones.postCondicionVtoLemonQA(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad, fechaVencimientoRollback);
			
			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");
		

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}
		
		return result;
	}
	/*********************compra con tarjeta vencida*********************************************/ // Reports report,String name, String configEntidad
	public boolean compraConTarjetaVencidaUAT(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String entidad, String TCFilesPath ) {
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_18_OPI\r\n");

			//Set Variables
			String dispInicial = "10000";
			String pendCompras = "0";
			String ajustePend = "0";
			String ID_CUENTA = "2";
			String de39 = "54";
			String ID_ESTADO = "1";
			String disponible = "10000";
			String impPendComp = "0";
			String impAjustePend = "0";
			String res;
			String fechaVencimientoRollback = "30/09/2024','DD/MM/YYYY";
			boolean validaRes;
			int rtaQuery = 0;
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			//Instancias
			sshWorker opiCmd = new sshWorker();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			opiWorker opiAcciones = new opiWorker();

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PECONDICIONES
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");

			opiAcciones.preCondicionCMyVtoLemonUAT(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			
			//EJECUCION OPI
			//res = opiCmd.sshSendCmd(report, "TC_18_OPI.xml", configEntidad);
			//CAMBIO DE ENTIDAD OPI
			
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(5000);


			//res = opiCmd.sshSendCmd(report, "TC_17_OPI.xml", configEntidad);
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				//opiCmd.getOpiStatus(configEntidad);
				//Thread.sleep(5000);
				
				//for (int i = 0; i < 10; i++) {			
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				//System.out.println("ejecucion numero : " + i);


			if (res.equals(de39))
			{
				report.AddLine("Ejecucion Correcta<br>DE39:" + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39:" + res + "\r\n");
			} else {
				report.AddLineAssertionError("FAIL<br>DE39:" + res + " Se esperaba: " + de39);
				System.out.println("##[warning] : FAIL:\r\nDE39:" + res + " Se esperaba: " + de39 + "\r\n");
				result = false;
			}
			
			//VALIDACIONES
			validaRes = opiAcciones.validacionAutorizacionLemonUAT(report, oraResp, ID_ESTADO, disponible, impPendComp, impAjustePend, ID_CUENTA, configEntidad);
			if (!validaRes) {
				report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
				System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
				result = false;
			} else {
				report.AddLine("Ejecucion Correcta<br>Se cumplieron todas las validaciones");
				System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
			}
			
			//POSTCONDICIONES
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			opiAcciones.postCondicionVtoLemonUAT(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad, fechaVencimientoRollback);
			
			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");
		

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}
		
		return result;
	}
		/****************fin compra con tarjeta vencia*********************************************/
	/*********************compra bin inexistente*********************************************/ // Reports report,String name, String configEntidad
	public boolean compraConBinInexistente(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String entidad, String TCFilesPath ) {
		boolean result = true;
		try {
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			//Set Variables
			String dispInicial = "10000";
			String pendCompras = "0";
			String ajustePend = "0";
			String ID_CUENTA = "2";
			String de39 = "57";
			String idEstado = "1";
			String disponible = "10000";
			String impPendComp = "0";
			String impAjustePend = "0";
			String res;
			boolean validaRes;
			int rtaQuery = 0;
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			//Instancias
			sshWorker opiCmd = new sshWorker();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			opiWorker opiAcciones = new opiWorker();

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PECONDICIONES
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");

			opiAcciones.preCondicionCMLemonQA(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//EJECUCION OPI
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(5000);
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);		
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
			if (res.equals(de39))
			{
				report.AddLine("Ejecucion Correcta<br>DE39:" + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39:" + res + "\r\n");
			} else {
				report.AddLineAssertionError("FAIL<br>DE39:" + res + " Se esperaba: " + de39);
				System.out.println("##[warning] : FAIL:\r\nDE39:" + res + " Se esperaba: " + de39 + "\r\n");
				result = false;
			}
			
			//VALIDACIONES
			validaRes = opiAcciones.validacionBD_CM_AutorizacionLemonQA(report, oraResp, idEstado, disponible, impPendComp, impAjustePend, ID_CUENTA, configEntidad);
			if (!validaRes) {
				report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
				System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
				result = false;
			} else {
				report.AddLine("Ejecucion Correcta<br>Se cumplieron todas las validaciones");
				System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
			}
			
			//POSTCONDICIONES
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			opiAcciones.postCondicionCMLemonQA(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");
		

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}
		return result;
	}
	/*********************compra bin inexistente*********************************************/ // Reports report,String name, String configEntidad
	public boolean compraConBinInexistenteUAT(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String entidad, String TCFilesPath ) {
		boolean result = true;
		try {
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			//Set Variables
			String dispInicial = "10000";
			String pendCompras = "0";
			String ajustePend = "0";
			String ID_CUENTA = "2";
			String de39 = "57";
			String idEstado = "1";
			String disponible = "10000";
			String impPendComp = "0";
			String impAjustePend = "0";
			String res;
			boolean validaRes;
			int rtaQuery = 0;
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			//Instancias
			sshWorker opiCmd = new sshWorker();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			opiWorker opiAcciones = new opiWorker();

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PECONDICIONES
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");

			opiAcciones.preCondicionCMLemonUAT(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//EJECUCION OPI
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(5000);
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);		
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
			if (res.equals(de39))
			{
				report.AddLine("Ejecucion Correcta<br>DE39:" + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39:" + res + "\r\n");
			} else {
				report.AddLineAssertionError("FAIL<br>DE39:" + res + " Se esperaba: " + de39);
				System.out.println("##[warning] : FAIL:\r\nDE39:" + res + " Se esperaba: " + de39 + "\r\n");
				result = false;
			}
			
			//VALIDACIONES
			validaRes = opiAcciones.validacionBD_CM_AutorizacionLemonUAT(report, oraResp, idEstado, disponible, impPendComp, impAjustePend, ID_CUENTA, configEntidad);
			if (!validaRes) {
				report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
				System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
				result = false;
			} else {
				report.AddLine("Ejecucion Correcta<br>Se cumplieron todas las validaciones");
				System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
			}
			
			//POSTCONDICIONES
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			opiAcciones.postCondicionCMLemonUAT(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");
		

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}
		return result;
	}
		/****************fin compra bin inexistente*********************************************/
	/*********************compra vencimiento difiere****************************************/ // Reports report,String name, String configEntidad
	public boolean compraVencimientoDifiere(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String entidad, String TCFilesPath ) {
		boolean result = true;
		try {
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_20_OPI\r\n");

			//Set Variables
			String dispInicial = "10000";
			String pendCompras = "0";
			String ajustePend = "0";
			String ID_CUENTA = "2";
			String de39 = "57";
			String idEstado = "1";
			String disponible = "10000";
			String impPendComp = "0";
			String impAjustePend = "0";
			String fechaVencimientoRollback = "30/11/2025','DD/MM/YYYY";
			String res;
			boolean validaRes;
			int rtaQuery = 0;
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			//Instancias
			sshWorker opiCmd = new sshWorker();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			opiWorker opiAcciones = new opiWorker();

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PECONDICIONES
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");

			opiAcciones.preCondicionCMLemonQA(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//EJECUCION OPI
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(5000);
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);		
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);

			if (res.equals(de39))
			{
				report.AddLine("Ejecucion Correcta<br>DE39:" + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39:" + res + "\r\n");
			} else {
				report.AddLineAssertionError("FAIL<br>DE39:" + res + " Se esperaba: " + de39);
				System.out.println("##[warning] : FAIL:\r\nDE39:" + res + " Se esperaba: " + de39 + "\r\n");
				result = false;
			}
			
			//VALIDACIONES
			validaRes = opiAcciones.validacionBD_CM_AutorizacionLemonQA(report, oraResp, idEstado, disponible, impPendComp, impAjustePend, ID_CUENTA, configEntidad);
			if (!validaRes) {
				report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
				System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
				result = false;
			} else {
				report.AddLine("Ejecucion Correcta<br>Se cumplieron todas las validaciones");
				System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
			}
			
			//POSTCONDICIONES
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
			
			opiAcciones.postCondicionVtoLemonQA(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad, fechaVencimientoRollback);
			

			opiAcciones.postCondicionCMLemonQA(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");
		

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}
		return result;
	}
	/*********************compra vencimiento difiere****************************************/ // Reports report,String name, String configEntidad
	public boolean compraVencimientoDifiereUAT(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String entidad, String TCFilesPath ) {
		boolean result = true;
		try {
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_20_OPI\r\n");

			//Set Variables
			String dispInicial = "10000";
			String pendCompras = "0";
			String ajustePend = "0";
			String ID_CUENTA = "2";
			String de39 = "57";
			String idEstado = "1";
			String disponible = "10000";
			String impPendComp = "0";
			String impAjustePend = "0";
			String fechaVencimientoRollback = "30/11/2025','DD/MM/YYYY";
			String res;
			boolean validaRes;
			int rtaQuery = 0;
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			//Instancias
			sshWorker opiCmd = new sshWorker();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			opiWorker opiAcciones = new opiWorker();

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PECONDICIONES
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");

			opiAcciones.preCondicionCMLemonUAT(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//EJECUCION OPI
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(5000);
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);		
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);

			if (res.equals(de39))
			{
				report.AddLine("Ejecucion Correcta<br>DE39:" + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39:" + res + "\r\n");
			} else {
				report.AddLineAssertionError("FAIL<br>DE39:" + res + " Se esperaba: " + de39);
				System.out.println("##[warning] : FAIL:\r\nDE39:" + res + " Se esperaba: " + de39 + "\r\n");
				result = false;
			}
			
			//VALIDACIONES
			validaRes = opiAcciones.validacionBD_CM_AutorizacionLemonUAT(report, oraResp, idEstado, disponible, impPendComp, impAjustePend, ID_CUENTA, configEntidad);
			if (!validaRes) {
				report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
				System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
				result = false;
			} else {
				report.AddLine("Ejecucion Correcta<br>Se cumplieron todas las validaciones");
				System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
			}
			
			//POSTCONDICIONES
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");
			
			opiAcciones.postCondicionVtoLemonUAT(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad, fechaVencimientoRollback);
			

			opiAcciones.postCondicionCMLemonUAT(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");
		

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}
		return result;
	}
		/****************fin vencimineto difiere**************************************/
	/*********************compra cuenta inactiva**************************************/ // Reports report,String name, String configEntidad
	public boolean compraCuentaInactiva(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String entidad, String TCFilesPath ) {
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_21_OPI\r\n");

			//Set Variables
			String dispInicial = "10000";
			String pendCompras = "0";
			String ajustePend = "0";
			String ID_CUENTA = "2";
			String de39 = "57";
			String idEstado = "1";
			String disponible = "10000";
			String impPendComp = "0";
			String impAjustePend = "0";
			String fechaVencimientoRollback = "30/11/2025','DD/MM/YYYY";
			String res;
			boolean validaRes;
			int rtaQuery = 0;
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			//Instancias
			sshWorker opiCmd = new sshWorker();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			opiWorker opiAcciones = new opiWorker();

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PECONDICIONES
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");

			opiAcciones.preCondicionCMLemonQA(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);		
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS SET ID_ESTADO = 1 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//EJECUCION OPI
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(5000);
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);		
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);


			if (res.equals(de39))
			{
				report.AddLine("Ejecucion Correcta<br>DE39:" + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39:" + res + "\r\n");
			} else {
				report.AddLineAssertionError("FAIL<br>DE39:" + res + " Se esperaba: " + de39);
				System.out.println("##[warning] : FAIL:\r\nDE39:" + res + " Se esperaba: " + de39 + "\r\n");
				result = false;
			}
			
			//VALIDACIONES
			validaRes = opiAcciones.validacionBD_CM_AutorizacionLemonQA(report, oraResp, idEstado, disponible, impPendComp, impAjustePend, ID_CUENTA, configEntidad);
			if (!validaRes) {
				report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
				System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
				result = false;
			} else {
				report.AddLine("Ejecucion Correcta<br>Se cumplieron todas las validaciones");
				System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
			}
			
			//POSTCONDICIONES
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			opiAcciones.postCondicionCMLemonQA(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.postCondicionBD(report, rtaQuery);
			
			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");
		

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}
		return result;
		}
	/*********************compra cuenta inactiva**************************************/ // Reports report,String name, String configEntidad
	public boolean compraCuentaInactivaUAT(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String entidad, String TCFilesPath ) {
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_21_OPI\r\n");

			//Set Variables
			String dispInicial = "10000";
			String pendCompras = "0";
			String ajustePend = "0";
			String ID_CUENTA = "2";
			String de39 = "57";
			String idEstado = "1";
			String disponible = "10000";
			String impPendComp = "0";
			String impAjustePend = "0";
			String fechaVencimientoRollback = "30/11/2025','DD/MM/YYYY";
			String res;
			boolean validaRes;
			int rtaQuery = 0;
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			//Instancias
			sshWorker opiCmd = new sshWorker();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			opiWorker opiAcciones = new opiWorker();

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PECONDICIONES
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");

			opiAcciones.preCondicionCMLemonUAT(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);		
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 1 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//EJECUCION OPI
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(5000);
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);		
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);


			if (res.equals(de39))
			{
				report.AddLine("Ejecucion Correcta<br>DE39:" + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39:" + res + "\r\n");
			} else {
				report.AddLineAssertionError("FAIL<br>DE39:" + res + " Se esperaba: " + de39);
				System.out.println("##[warning] : FAIL:\r\nDE39:" + res + " Se esperaba: " + de39 + "\r\n");
				result = false;
			}
			
			//VALIDACIONES
			validaRes = opiAcciones.validacionBD_CM_AutorizacionLemonUAT(report, oraResp, idEstado, disponible, impPendComp, impAjustePend, ID_CUENTA, configEntidad);
			if (!validaRes) {
				report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
				System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
				result = false;
			} else {
				report.AddLine("Ejecucion Correcta<br>Se cumplieron todas las validaciones");
				System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
			}
			
			//POSTCONDICIONES
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			opiAcciones.postCondicionCMLemonUAT(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.postCondicionBD(report, rtaQuery);
			
			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");
		

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}
		return result;
		}
			/****************fin cuenta inactiva************************************/
	/*********************compra cuenta inactiva**************************************/ // Reports report,String name, String configEntidad
	public boolean compraCampoMontoNulo(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String entidad, String TCFilesPath, String de39 ) {
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_21_OPI\r\n");

			//Set Variables
			String dispInicial = "10000";
			String pendCompras = "0";
			String ajustePend = "0";
			String ID_CUENTA = "2";
			//String de39 = "30";
			String idEstado = "1";
			String disponible = "10000";
			String impPendComp = "0";
			String impAjustePend = "0";
			String fechaVencimientoRollback = "30/11/2025','DD/MM/YYYY";
			String res;
			boolean validaRes;
			int rtaQuery = 0;
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			//Instancias
			sshWorker opiCmd = new sshWorker();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			opiWorker opiAcciones = new opiWorker();

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PECONDICIONES
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");

			opiAcciones.preCondicionCMLemonQA(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);		
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS SET ID_ESTADO = 1 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//EJECUCION OPI
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(5000);
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlFile);
			String idAutorizacionEmisorJosnString="";

				String ambiente="qa";
				opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, ambiente);
				Thread.sleep(5000);
				opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, ambiente);
				Thread.sleep(5000);
				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);		
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);


			if (res.equals(de39))
			{
				report.AddLine("Ejecucion Correcta<br>DE39:" + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39:" + res + "\r\n");
			} else {
				report.AddLineAssertionError("FAIL<br>DE39:" + res + " Se esperaba: " + de39);
				System.out.println("##[warning] : FAIL:\r\nDE39:" + res + " Se esperaba: " + de39 + "\r\n");
				result = false;
			}
			
			//VALIDACIONES
			validaRes = opiAcciones.validacionBD_CM_AutorizacionLemonQA(report, oraResp, idEstado, disponible, impPendComp, impAjustePend, ID_CUENTA, configEntidad);
			if (!validaRes) {
				report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
				System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
				result = false;
			} else {
				report.AddLine("Ejecucion Correcta<br>Se cumplieron todas las validaciones");
				System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
			}
			
			//POSTCONDICIONES
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			opiAcciones.postCondicionCMLemonQA(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.postCondicionBD(report, rtaQuery);
			
			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");
		

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}
		return result;
	}
	/*********************compra cuenta inactiva**************************************/ // Reports report,String name, String configEntidad
	public boolean compraCampoMontoNuloUAT(Reports report, DriverManager DM, int iteration, String name, String configEntidad, String entidad, String TCFilesPath, String de39 ) {
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_21_OPI\r\n");

			//Set Variables
			String dispInicial = "10000";
			String pendCompras = "0";
			String ajustePend = "0";
			String ID_CUENTA = "2";
			//de39 = "5";
			String idEstado = "1";
			String disponible = "10000";
			String impPendComp = "0";
			String impAjustePend = "0";
			String fechaVencimientoRollback = "30/11/2025','DD/MM/YYYY";
			String res;
			boolean validaRes;
			int rtaQuery = 0;
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			//Instancias
			sshWorker opiCmd = new sshWorker();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			opiWorker opiAcciones = new opiWorker();

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PECONDICIONES
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");

			opiAcciones.preCondicionCMLemonUAT(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);		
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 1 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//EJECUCION OPI
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(5000);
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);		
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);


			if (res.equals(de39))
			{
				report.AddLine("Ejecucion Correcta<br>DE39:" + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39:" + res + "\r\n");
			} else {
				report.AddLineAssertionError("FAIL<br>DE39:" + res + " Se esperaba: " + de39);
				System.out.println("##[warning] : FAIL:\r\nDE39:" + res + " Se esperaba: " + de39 + "\r\n");
				result = false;
			}
			
			//VALIDACIONES
			validaRes = opiAcciones.validacionBD_CM_AutorizacionLemonUAT(report, oraResp, idEstado, disponible, impPendComp, impAjustePend, ID_CUENTA, configEntidad);
			if (!validaRes) {
				report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
				System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
				result = false;
			} else {
				report.AddLine("Ejecucion Correcta<br>Se cumplieron todas las validaciones");
				System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
			}
			
			//POSTCONDICIONES
			report.AddLine("--- Ejecutando PostCondicion ---");
			System.out.println("--- Ejecutando PostCondicion ---\r\n");

			opiAcciones.postCondicionCMLemonUAT(oraResp, ppCondi, report, dispInicial, pendCompras, ajustePend, ID_CUENTA, configEntidad);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.postCondicionBD(report, rtaQuery);
			
			System.out.println("##[command] : <------ Finished Test: " + name + " ------>\r\n");
			report.AddLine("<------ Finished Test: " + name + " ------>");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");
		

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = true;
		}
		return result;
	}
		/****************fin cuenta inactiva************************************/
	/********************* CTF OPI VISA LEMON***********************************************************/
	public boolean CompraCTFLemonQA(Datasources data, Reports report,DriverManager DM,int iteration,String name, String configEntidad, String entidad, String TCFilesPath) {

		System.out.println("\r\n##################################################################################################################################################################################################################"
				+ "##################################################################################################################################################################################################################\r\n");
		System.out.println("Configuring "+name+ "\r\n");
		//SET INSTANCES
		sshWorker opiCmd = new sshWorker();
		dbWorker oraResp = new dbWorker();
		PrePostCondi ppCondi = new PrePostCondi();
		
		//SELECT THE DRIVER AND PATH
		driver = DM.CreateDriver(DM.CHROME);

		report.SetDriver(driver);

		//SET THE REPOSITORIES TO USE
		Repo_WebRepository repo = new Repo_WebRepository();
		repo.setDriver(driver);

		//SET STEPS INSTANCES
		Acceso acceso = new Acceso();
		NavSuperiorBatch nav = new NavSuperiorBatch();
		TC_07_BA_PASOS tc = new TC_07_BA_PASOS();

		//Set Variables
		boolean result = true;
		String res = "";
		
		//String[] res = new String[2];
		//Set Reusltados Esperados
		int rtaPrePostCondiciones = 0;
		int rtaPrePostCondiciones2 = 0;
		int rtaPrePostCondiciones3 = 0;
		int rtaPrePostCondiciones4 = 0;
		int rtaPrePostCondiciones5 = 0;
		int rtaPrePostCondiciones6 = 0;
		int rtaPrePostCondiciones7 = 0;
		String idAutorizacionEmisor = "0000000";
		String mti100 = "0000000";
		String DE2 = "0000000";
		String DE4 = "0000000";
		String DE13 = "0000000";
		String DE43 = "0000000";
		String DE51 = "0000000";
		
		int rtaQuery;
		String ID_CUENTA = "2";
		String ID_CUENTA_EXT = "10015647";
		
		esquema = JsonPath.from(configEntidad).getString("ENTIDAD.esquema_db");
		String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCH.url_gbatch");
	

		try {		
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Preparación del archivo JSON del TC
			System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			System.out.println(TCFilesPath);

			String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));

			//Preparación del archivo XML del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			System.out.println(TCFilesPath);
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));

			System.out.println("JSON FILE:");
			System.out.println(jsonFile);
			System.out.println("XMLFile: ");
			System.out.println(xmlFile);

			JsonParser parser = new JsonParser();

			JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			System.out.println("JSON Object: ");
			System.out.println(jsonObject);
			JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");

			System.out.println("VALIDACIONES: ");
			System.out.println(validaciones);

			//SET dbWorker
			oraResp.setUser(JsonPath.from(configEntidad).getString("DBLEMONQA.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DBLEMONQA.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DBLEMONQA.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_peru_visa_qa"));


			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");

			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "compras");
			//Thread.sleep(5000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(5000);

			System.out.println("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### FINALIZA EJECUCION DE PRECONDICIONES #####");

			//Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatusLemonQA(configEntidad)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatusLemonQa(configEntidad), equalTo(true));
			}

			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");

			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				//opiCmd.getOpiStatus(configEntidad);
				//Thread.sleep(5000);
				
				for (int i = 0; i < 10; i++) {			
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				System.out.println("ejecucion numero : " + i);

				
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91") && !res.equals("57")) {

					if (!idAutorizacionEmisorJosnString.equals("0")) {
					idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiVisaQaAuto");
					mti100 = opiCmd.getMti100(report, configEntidad, "opiVisaQaAuto");
					System.out.println("***********ingrese al ifFFFFFF");
					System.out.println("***********XML PARA OBTENER DATOS : " + mti100);
						}
					break;
				}				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
				}
            System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
            JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
            JsonObject mti100Obj = parser.parse(mti100).getAsJsonObject();
            System.out.println("SE PARSEA STRING A JSON");
            //DE ACA SE OBTIENE EL RESPONSE BODY PARA OBTENER DATOS 
            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacionEmisor").getAsString();
            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
            //  9999999999 obtener pan - monto - (DE13 que se envía) – posición de la 58 a la 62
            // Monto de la transacción – Moneda de la Transaccion - – Comercio – Posición 92 a 133
            DE2 = mti100Obj.get("DE2").getAsString();
            DE4 = mti100Obj.get("DE4").getAsString();
            DE13 = mti100Obj.get("DE13").getAsString();
            DE43 = mti100Obj.get("DE43").getAsString();
            DE51 = mti100Obj.get("DE51").getAsString();
            System.out.println("DE2: " + DE2);
            System.out.println("DE4: " + DE4);
            System.out.println("DE13: " + DE13);
            System.out.println("DE43: " + DE43);
            System.out.println("DE51: " + DE51);
			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
			//String TCR_DATA = "0500"+DE2+"000Z  24110872259002121103876100873910228"+DE4+"604000000100000604"+DE43+" 519900000     1008N"+COD_AUTO_ENTIDAD+"6 2 0722600";
			String COD_AUTO_ENTIDAD = oraResp.oraOneQueryLemonQA(report,"SELECT COD_AUTO_ENTIDAD FROM AUTORIZACION WHERE ID_AUTORIZACION_EMISOR = " + idAutorizacionEmisor, configEntidad);
			System.out.println("este es el DE38 : " + COD_AUTO_ENTIDAD);
			String TCR_DATA = "'"+"0500"+DE2+"000Z  24110872259002121103876100873910228"+DE4+"604000000100000604"+DE43+" 519900000     1008N"+COD_AUTO_ENTIDAD+"6 2 0722600"+"'";
			//String TCR_DATA = "0500"+DE2+"000Z  24110872259002121103876100873910228"+DE4+"604000000100000604"+DE43+" 519900000     1008N"+COD_AUTO_ENTIDAD+"6 2 0722600";
			System.out.println("este es el tcr_data : " + TCR_DATA);
			// se comienza con la CREACION del CTF
			//oraResp.oraUpdateLemonQA1(report, "UPDATE CTF_VISA SET ID_CONSUMO = NULL, ID_AUTORIZACION = NULL, PROCESADO = 0, AUTORIZACION_PROCESADA = 0, OBSERVACION = NULL, TCR_DATA = " + TCR_DATA + " WHERE ID_CTF IN (11)",configEntidad);
			rtaPrePostCondiciones = oraResp.oraUpdateLemonQA1(report, "UPDATE CTF_VISA\r\n"
					+ "SET ID_CONSUMO = NULL, ID_AUTORIZACION = NULL, PROCESADO = 0, \r\n"
					+ "AUTORIZACION_PROCESADA = 0, OBSERVACION = NULL, TCR_DATA = " + TCR_DATA + "\r\n"
					+ "WHERE ID_CTF IN (11)",configEntidad);
			rtaPrePostCondiciones2 = oraResp.oraUpdateLemonQA1(report, "UPDATE CTF_VISA\r\n"
					+ "SET ID_CONSUMO = NULL, ID_AUTORIZACION = NULL, PROCESADO = '0', \r\n"
					+ "AUTORIZACION_PROCESADA = '0', OBSERVACION = NULL\r\n"
					+ "WHERE ID_CTF IN (12)",configEntidad);
			rtaPrePostCondiciones3 = oraResp.oraUpdateLemonQA1(report, "UPDATE CTF_VISA\r\n"
					+ "SET ID_CONSUMO = NULL, ID_AUTORIZACION = NULL, PROCESADO = '0', \r\n"
					+ "AUTORIZACION_PROCESADA = '0', OBSERVACION = NULL\r\n"
					+ "WHERE ID_CTF IN (13)",configEntidad);
			rtaPrePostCondiciones4 = oraResp.oraUpdateLemonQA1(report, "UPDATE CTF_VISA\r\n"
					+ "SET ID_CONSUMO = NULL, ID_AUTORIZACION = NULL, PROCESADO = '0', \r\n"
					+ "AUTORIZACION_PROCESADA = '0', OBSERVACION = NULL\r\n"
					+ "WHERE ID_CTF IN (14)",configEntidad);
			rtaPrePostCondiciones5 = oraResp.oraUpdateLemonQA1(report, "UPDATE CTF_VISA\r\n"
					+ "SET ID_CONSUMO = NULL, ID_AUTORIZACION = NULL, PROCESADO = '0', \r\n"
					+ "AUTORIZACION_PROCESADA = '0', OBSERVACION = NULL\r\n"
					+ "WHERE ID_CTF IN (15)",configEntidad);
			rtaPrePostCondiciones6 = oraResp.oraUpdateLemonQA1(report, "UPDATE CTF_VISA\r\n"
					+ "SET ID_CONSUMO = NULL, ID_AUTORIZACION = NULL, PROCESADO = '0', \r\n"
					+ "AUTORIZACION_PROCESADA = '0', OBSERVACION = NULL\r\n"
					+ "WHERE ID_CTF IN (16)",configEntidad);
			rtaPrePostCondiciones7 = oraResp.oraUpdateLemonQA1(report, "UPDATE CTF_VISA\r\n"
					+ "SET ID_CONSUMO = NULL, ID_AUTORIZACION = NULL, PROCESADO = '0', \r\n"
					+ "AUTORIZACION_PROCESADA = '0', OBSERVACION = NULL\r\n"
					+ "WHERE ID_CTF IN (17) ",configEntidad);
			
			// SE EJECUTA PROCESO BATCH
			
			//Se abre el driver con la URL de GO
			//driver.get("https://v2batch.web.qa.global.globalprocessing.net.ar/");
			driver.get(URL_GBATCH);
			
			acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			// Login a la pagina principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch\r\n");
			//acceso.loginUsernameBatch1(DM, repo);
			// Hacemos click en procesos
			nav.procesos(data, report, DM, iteration, name, repo);
			
			// Primera pagina del test
			tc.pagina1LemonQA(data, report, DM, iteration, name, repo, configEntidad);

			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);

			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);
			
			Thread.sleep(18000);
			/********************************************************************
			 * REALIZO UNA VERIFICACION
			 *****************************************************************/
			Connection conn;
			
			//CONFIGURACIÓN DATABASE
			oraResp.setUser(JsonPath.from(configEntidad).get("DBLEMONQA.user"));
			oraResp.setPass(JsonPath.from(configEntidad).get("DBLEMONQA.pass"));
			oraResp.setEntidad(JsonPath.from(configEntidad).get("ENTIDAD.id_entidad_peru_visa_qa"));
			oraResp.setHost(JsonPath.from(configEntidad).get("DBLEMONQA.host"));
			String user = JsonPath.from(configEntidad).get("DBLEMONQA.user");
			String pass = JsonPath.from(configEntidad).get("DBLEMONQA.pass");
			String host = JsonPath.from(configEntidad).get("DBLEMONQA.host");
			String SID = JsonPath.from(configEntidad).get("DBLEMONQA.SID");
			String strCon = "jdbc:oracle:thin:@" + host + "/" + SID;
		//	String user = JsonPath.from(configEntidad).get("DB.user");
			//String pass = JsonPath.from(configEntidad).get("DB.pass");
		//	String host = JsonPath.from(configEntidad).get("DB.host");
			//String SID = JsonPath.from(configEntidad).get("DB.SID");
			
			//String strCon = "jdbc:oracle:thin:@" + host + "/" + SID;
			
			//Connection conn;
			//conn = DriverManager.getConnection(strCon,user,pass);
			conn = java.sql.DriverManager.getConnection(strCon,user,pass);
			Statement stmt=conn.createStatement();
			stmt=conn.createStatement();
			// Ejecutar la consulta SQL
            String sqlQueryV = "SELECT ID_CONSUMO, ID_AUTORIZACION FROM CTF_VISA WHERE ID_CTF = ('11')";
         ResultSet resultSetV = stmt.executeQuery(sqlQueryV);
        // ResultSet resultSet = stmt.executeQuery(sqlQuery);
         // Procesar los resultados
         while (resultSetV.next()) {
             // Obtener los valores de cada columna por nombre
             String ID_CONSUMO = resultSetV.getString("ID_CONSUMO");
             String ID_AUTORIZACION = resultSetV.getString("ID_AUTORIZACION");
             String ID_CTF = "11";
             // Imprimir las variables con el nombre de cada campo y su valor
             System.out.println("ID_CONSUMO: " + ID_CONSUMO);
             System.out.println("CRITERIO_MAPEO: " + ID_AUTORIZACION);
             System.out.println("SE VERIFICA IMPACTO DE DATOS DE : " + ID_CTF);
         }
			
         //resultSet.close();
         stmt.close();
			
			
			
			
			/********************************************************************
			 * FINALIZO VERIFICACION
			 *****************************************************************/
						
			// Tiempo necesario para que se ejecute el proceso y se creen los registros en la BDD
			Thread.sleep(30000);
			
			if (res.equals("00") || res.equals("55"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				result = validacionGralLemonQA(oraResp, report, idAutorizacionEmisor, entidad, validaciones);
			} else {
				report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "00");
				System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "00" + "\r\n");
				result = false;
			}

 

			System.out.println("##### FIN DE EJECUCION OPI #####");
			report.AddLine("##### FIN DE EJECUCION OPI #####");

 

						
			//POSTCONDICIONES

 

			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");

 

 

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}

 

		return result;
	}

	
	/*********************FIN CTF OPI VISA LEMON***********************************************************/
	
	/**********************OPI VISA LEMON CTF UAT******************************************/
	
	public boolean CompraCTFLemonUAT(Datasources data, Reports report,DriverManager DM,int iteration,String name, String configEntidad, String entidad, String TCFilesPath) {

		System.out.println("\r\n##################################################################################################################################################################################################################"
				+ "##################################################################################################################################################################################################################\r\n");
		System.out.println("Configuring "+name+ "\r\n");
		//SET INSTANCES
		sshWorker opiCmd = new sshWorker();
		dbWorker oraResp = new dbWorker();
		PrePostCondi ppCondi = new PrePostCondi();
		
		//SELECT THE DRIVER AND PATH
		driver = DM.CreateDriver(DM.CHROME);

		report.SetDriver(driver);

		//SET THE REPOSITORIES TO USE
		Repo_WebRepository repo = new Repo_WebRepository();
		repo.setDriver(driver);

		//SET STEPS INSTANCES
		Acceso acceso = new Acceso();
		NavSuperiorBatch nav = new NavSuperiorBatch();
		TC_07_BA_PASOS tc = new TC_07_BA_PASOS();

		//Set Variables
		boolean result = true;
		String res = "";
		
		//String[] res = new String[2];
		//Set Reusltados Esperados
		int rtaPrePostCondiciones = 0;
		int rtaPrePostCondiciones2 = 0;
		int rtaPrePostCondiciones3 = 0;
		int rtaPrePostCondiciones4 = 0;
		int rtaPrePostCondiciones5 = 0;
		int rtaPrePostCondiciones6 = 0;
		int rtaPrePostCondiciones7 = 0;
		String idAutorizacionEmisor = "0000000";
		String mti100 = "0000000";
		String DE2 = "0000000";
		String DE4 = "0000000";
		String DE13 = "0000000";
		String DE43 = "0000000";
		String DE51 = "0000000";
		
		int rtaQuery;
		String ID_CUENTA = "2";
		String ID_CUENTA_EXT = "10015647";
		
		esquema = JsonPath.from(configEntidad).getString("ENTIDAD.esquema_db");
		String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCHUAT.url_gbatch");
	

		try {		
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Preparación del archivo JSON del TC
			System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			System.out.println(TCFilesPath);

			String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));

			//Preparación del archivo XML del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			System.out.println(TCFilesPath);
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));

			System.out.println("JSON FILE:");
			System.out.println(jsonFile);
			System.out.println("XMLFile: ");
			System.out.println(xmlFile);

			JsonParser parser = new JsonParser();

			JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			System.out.println("JSON Object: ");
			System.out.println(jsonObject);
			JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");

			System.out.println("VALIDACIONES: ");
			System.out.println(validaciones);

			//SET dbWorker
			oraResp.setUser(JsonPath.from(configEntidad).getString("DBLEMONUAT.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DBLEMONUAT.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DBLEMONUAT.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_peru_visa_qa"));


			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");

			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiVisaQaAuto", entidad, "compras");
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(5000);

			System.out.println("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### FINALIZA EJECUCION DE PRECONDICIONES #####");

			//Se valida el funcionamiento de OPI
			/*if (!opiCmd.getOpiStatusLemonQA(configEntidad)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatusLemonQa(configEntidad), equalTo(true));
			}*/

			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");

			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				//opiCmd.getOpiStatus(configEntidad);
				//Thread.sleep(5000);
				
				for (int i = 0; i < 10; i++) {			
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);
				System.out.println("ejecucion numero : " + i);

				
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91") && !res.equals("57")) {

					if (!idAutorizacionEmisorJosnString.equals("0")) {
					idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiVisaQaAuto");
					mti100 = opiCmd.getMti100(report, configEntidad, "opiVisaQaAuto");
					System.out.println("***********ingrese al ifFFFFFF");
					System.out.println("***********XML PARA OBTENER DATOS : " + mti100);
						}
					break;
				}				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
				}
            System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
            JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
            JsonObject mti100Obj = parser.parse(mti100).getAsJsonObject();
            System.out.println("SE PARSEA STRING A JSON");
            //DE ACA SE OBTIENE EL RESPONSE BODY PARA OBTENER DATOS 
            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacionEmisor").getAsString();
            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
            //  9999999999 obtener pan - monto - (DE13 que se envía) – posición de la 58 a la 62
            // Monto de la transacción – Moneda de la Transaccion - – Comercio – Posición 92 a 133
            DE2 = mti100Obj.get("DE2").getAsString();
            DE4 = mti100Obj.get("DE4").getAsString();
            DE13 = mti100Obj.get("DE13").getAsString();
            DE43 = mti100Obj.get("DE43").getAsString();
            DE51 = mti100Obj.get("DE51").getAsString();
            System.out.println("DE2: " + DE2);
            System.out.println("DE4: " + DE4);
            System.out.println("DE13: " + DE13);
            System.out.println("DE43: " + DE43);
            System.out.println("DE51: " + DE51);
			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
			//String TCR_DATA = "0500"+DE2+"000Z  24110872259002121103876100873910228"+DE4+"604000000100000604"+DE43+" 519900000     1008N"+COD_AUTO_ENTIDAD+"6 2 0722600";
			String COD_AUTO_ENTIDAD = oraResp.oraOneQueryLemonUAT(report,"SELECT COD_AUTO_ENTIDAD FROM AUTORIZACION WHERE ID_AUTORIZACION_EMISOR = " + idAutorizacionEmisor, configEntidad);
			System.out.println("este es el DE38 : " + COD_AUTO_ENTIDAD);
			String TCR_DATA = "'"+"0500"+DE2+"000Z  24110872259002121103876100873910228"+DE4+"604000000100000604"+DE43+" 519900000     1008N"+COD_AUTO_ENTIDAD+"6 2 0722600"+"'";
			//String TCR_DATA = "0500"+DE2+"000Z  24110872259002121103876100873910228"+DE4+"604000000100000604"+DE43+" 519900000     1008N"+COD_AUTO_ENTIDAD+"6 2 0722600";
			System.out.println("este es el tcr_data : " + TCR_DATA);
			// se comienza con la CREACION del CTF
			//oraResp.oraUpdateLemonQA1(report, "UPDATE CTF_VISA SET ID_CONSUMO = NULL, ID_AUTORIZACION = NULL, PROCESADO = 0, AUTORIZACION_PROCESADA = 0, OBSERVACION = NULL, TCR_DATA = " + TCR_DATA + " WHERE ID_CTF IN (11)",configEntidad);
			rtaPrePostCondiciones = oraResp.oraUpdateLemonUAT1(report, "UPDATE CTF_VISA\r\n"
					+ "SET ID_CONSUMO = NULL, ID_AUTORIZACION = NULL, PROCESADO = 0, \r\n"
					+ "AUTORIZACION_PROCESADA = 0, OBSERVACION = NULL, TCR_DATA = " + TCR_DATA + "\r\n"
					+ "WHERE ID_CTF IN (11)",configEntidad);
			rtaPrePostCondiciones2 = oraResp.oraUpdateLemonUAT1(report, "UPDATE CTF_VISA\r\n"
					+ "SET ID_CONSUMO = NULL, ID_AUTORIZACION = NULL, PROCESADO = '0', \r\n"
					+ "AUTORIZACION_PROCESADA = '0', OBSERVACION = NULL\r\n"
					+ "WHERE ID_CTF IN (12)",configEntidad);
			rtaPrePostCondiciones3 = oraResp.oraUpdateLemonUAT1(report, "UPDATE CTF_VISA\r\n"
					+ "SET ID_CONSUMO = NULL, ID_AUTORIZACION = NULL, PROCESADO = '0', \r\n"
					+ "AUTORIZACION_PROCESADA = '0', OBSERVACION = NULL\r\n"
					+ "WHERE ID_CTF IN (13)",configEntidad);
			rtaPrePostCondiciones4 = oraResp.oraUpdateLemonUAT1(report, "UPDATE CTF_VISA\r\n"
					+ "SET ID_CONSUMO = NULL, ID_AUTORIZACION = NULL, PROCESADO = '0', \r\n"
					+ "AUTORIZACION_PROCESADA = '0', OBSERVACION = NULL\r\n"
					+ "WHERE ID_CTF IN (14)",configEntidad);
			rtaPrePostCondiciones5 = oraResp.oraUpdateLemonUAT1(report, "UPDATE CTF_VISA\r\n"
					+ "SET ID_CONSUMO = NULL, ID_AUTORIZACION = NULL, PROCESADO = '0', \r\n"
					+ "AUTORIZACION_PROCESADA = '0', OBSERVACION = NULL\r\n"
					+ "WHERE ID_CTF IN (15)",configEntidad);
			rtaPrePostCondiciones6 = oraResp.oraUpdateLemonUAT1(report, "UPDATE CTF_VISA\r\n"
					+ "SET ID_CONSUMO = NULL, ID_AUTORIZACION = NULL, PROCESADO = '0', \r\n"
					+ "AUTORIZACION_PROCESADA = '0', OBSERVACION = NULL\r\n"
					+ "WHERE ID_CTF IN (16)",configEntidad);
			rtaPrePostCondiciones7 = oraResp.oraUpdateLemonUAT1(report, "UPDATE CTF_VISA\r\n"
					+ "SET ID_CONSUMO = NULL, ID_AUTORIZACION = NULL, PROCESADO = '0', \r\n"
					+ "AUTORIZACION_PROCESADA = '0', OBSERVACION = NULL\r\n"
					+ "WHERE ID_CTF IN (17) ",configEntidad);
			
			// SE EJECUTA PROCESO BATCH
			
			//Se abre el driver con la URL de GO
			//driver.get("https://v2batch.web.qa.global.globalprocessing.net.ar/");
			driver.get(URL_GBATCH);
			
			acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			// Login a la pagina principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch\r\n");
			//acceso.loginUsernameBatch1(DM, repo);
			// Hacemos click en procesos
			nav.procesos(data, report, DM, iteration, name, repo);
			
			// Primera pagina del test
			tc.pagina1LemonUAT(data, report, DM, iteration, name, repo, configEntidad);

			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);

			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);
			
			Thread.sleep(18000);
			/********************************************************************
			 * REALIZO UNA VERIFICACION
			 *****************************************************************/
			Connection conn;
			
			//CONFIGURACIÓN DATABASE
			oraResp.setUser(JsonPath.from(configEntidad).get("DBLEMONUAT.user"));
			oraResp.setPass(JsonPath.from(configEntidad).get("DBLEMONUAT.pass"));
			oraResp.setEntidad(JsonPath.from(configEntidad).get("ENTIDAD.id_entidad_peru_visa_qa"));
			oraResp.setHost(JsonPath.from(configEntidad).get("DBLEMONUAT.host"));
			String user = JsonPath.from(configEntidad).get("DBLEMONUAT.user");
			String pass = JsonPath.from(configEntidad).get("DBLEMONUAT.pass");
			String host = JsonPath.from(configEntidad).get("DBLEMONUAT.host");
			String SID = JsonPath.from(configEntidad).get("DBLEMONUAT.SID");
			String strCon = "jdbc:oracle:thin:@" + host + "/" + SID;
		//	String user = JsonPath.from(configEntidad).get("DB.user");
			//String pass = JsonPath.from(configEntidad).get("DB.pass");
		//	String host = JsonPath.from(configEntidad).get("DB.host");
			//String SID = JsonPath.from(configEntidad).get("DB.SID");
			
			//String strCon = "jdbc:oracle:thin:@" + host + "/" + SID;
			
			//Connection conn;
			//conn = DriverManager.getConnection(strCon,user,pass);
			conn = java.sql.DriverManager.getConnection(strCon,user,pass);
			Statement stmt=conn.createStatement();
			stmt=conn.createStatement();
			// Ejecutar la consulta SQL
            String sqlQueryV = "SELECT ID_CONSUMO, ID_AUTORIZACION FROM CTF_VISA WHERE ID_CTF = ('11')";
         ResultSet resultSetV = stmt.executeQuery(sqlQueryV);
        // ResultSet resultSet = stmt.executeQuery(sqlQuery);
         // Procesar los resultados
         while (resultSetV.next()) {
             // Obtener los valores de cada columna por nombre
             String ID_CONSUMO = resultSetV.getString("ID_CONSUMO");
             String ID_AUTORIZACION = resultSetV.getString("ID_AUTORIZACION");
             String ID_CTF = "11";
             // Imprimir las variables con el nombre de cada campo y su valor
             System.out.println("ID_CONSUMO: " + ID_CONSUMO);
             System.out.println("CRITERIO_MAPEO: " + ID_AUTORIZACION);
             System.out.println("SE VERIFICA IMPACTO DE DATOS DE : " + ID_CTF);
         }
			
         //resultSet.close();
         stmt.close();
			
			
			
			
			/********************************************************************
			 * FINALIZO VERIFICACION
			 *****************************************************************/
						
			// Tiempo necesario para que se ejecute el proceso y se creen los registros en la BDD
			Thread.sleep(30000);
			
			if (res.equals("00") || res.equals("55"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				result = validacionGralLemonUAT(oraResp, report, idAutorizacionEmisor, entidad, validaciones);
			} else {
				report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "00");
				System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "00" + "\r\n");
				result = false;
			}

 

			System.out.println("##### FIN DE EJECUCION OPI #####");
			report.AddLine("##### FIN DE EJECUCION OPI #####");

 

						
			//POSTCONDICIONES

 

			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");

 

 

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}

 

		return result;
	}

	
	/*********************FIN CTF OPI VISA LEMON***********************************************************/
	
	/**********************FIN OPI VISA LEMON CTF UAT***************************/
	
	
public boolean debitoAutomatico(Reports report,String name, String configEntidad, String entidad, String TCFilesPath) {
		
		System.out.println("\r\n##################################################################################################################################################################################################################"
				+ "##################################################################################################################################################################################################################\r\n");
		System.out.println("Configuring "+name+ "\r\n");

		//SET INSTANCES
		sshWorker opiCmd = new sshWorker();
		dbWorker oraResp = new dbWorker();

		//Set Variables
		boolean result = true;
		String res = "";
		//String[] res = new String[2];
		//Set Reusltados Esperados
		String idAutorizacionEmisor = "0000000";				
		
		try {		
			//Preparación del archivo JSON del TC
			System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			System.out.println(TCFilesPath);
			
			String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));
			
			//Preparación del archivo XML del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			System.out.println(TCFilesPath);
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			
			System.out.println("JSON FILE:");
			System.out.println(jsonFile);
			System.out.println("XMLFile: ");
			System.out.println(xmlFile);
			
			JsonParser parser = new JsonParser();
			
			JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			System.out.println("JSON Object: ");
			System.out.println(jsonObject);
			JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");
			
			System.out.println("VALIDACIONES: ");
			System.out.println(validaciones);
			
			//Set esquema Base de datos
			//esquema = JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad");

			//SET dbWorker
			oraResp.setUser(JsonPath.from(configEntidad).getString("DB.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DB.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DB.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad"));
			
			//se ejecuta una limpieza de debitos en caso de que el caso lo amerite
			
			oraResp.oraDelete(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
					+ "where ID_TARJETA = 1362",  configEntidad);
			
			oraResp.oraDelete(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
					+ "where id_tarjeta = 420",  configEntidad);
			
			
			//oraResp.oraDelete(report, xmlFile, configEntidad)
			
			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");
			
			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");
					
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
			Thread.sleep(1000);
			//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "compras");
			//Thread.sleep(5000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
			Thread.sleep(5000);
			
			System.out.println("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			
			//Se valida el funcionamiento de OPI
			/*if (!opiCmd.getOpiStatus(configEntidad, opi)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi), equalTo(true));
			}
*/
			
			
			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");
			
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlFile);
			
			
				opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				opiCmd.getOpiStatus(configEntidad, opi);
				
				Thread.sleep(5000);
				for(int i = 0; i < 10; i++) {			
				res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				System.out.println("ejecucion numero : " + i);
				
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91")) {
		//		if(!res.equals("unconnected ISOChannel")) {	
					System.out.println("***********ingrese al ifFFFFFF");
					break;
				}				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
			}
			
			String idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiemimcaut");			
			
            System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
            JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
            System.out.println("SE PARSEA STRING A JSON");
            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacionEmisor").getAsString();
            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
			
			
			
			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
			
			if (res.equals("00") || res.equals("55"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones, configEntidad);
			} else {
				report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "00");
				System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "00" + "\r\n");
				result = false;
			}

			System.out.println("##### FIN DE EJECUCION OPI #####");
			report.AddLine("##### FIN DE EJECUCION OPI #####");

						
			//POSTCONDICIONES

			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");


		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}

		return result;
	}
	
public boolean compraConCashback(Reports report,String name, String configEntidad, String entidad, String TCFilesPath) {
		
		System.out.println("\r\n##################################################################################################################################################################################################################"
				+ "##################################################################################################################################################################################################################\r\n");
		System.out.println("Configuring "+name+ "\r\n");

		//SET INSTANCES
		sshWorker opiCmd = new sshWorker();
		dbWorker oraResp = new dbWorker();

		//Set Variables
		boolean result = true;
		String res = "";
		//String[] res = new String[2];
		//Set Reusltados Esperados
		String idAutorizacionEmisor = "0000000";				
		
		try {		
			//Preparación del archivo JSON del TC
			System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			System.out.println(TCFilesPath);
			
			String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));
			
			//Preparación del archivo XML del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			System.out.println(TCFilesPath);
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			
			System.out.println("JSON FILE:");
			System.out.println(jsonFile);
			System.out.println("XMLFile: ");
			System.out.println(xmlFile);
			
			JsonParser parser = new JsonParser();
			
			JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			System.out.println("JSON Object: ");
			System.out.println(jsonObject);
			JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");
			
			System.out.println("VALIDACIONES: ");
			System.out.println(validaciones);
			
			//Set esquema Base de datos
			//esquema = JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad");

			//SET dbWorker
			oraResp.setUser(JsonPath.from(configEntidad).getString("DB.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DB.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DB.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad"));
			
			//se ejecuta una limpieza de debitos en caso de que el caso lo amerite
			
			/*oraResp.oraDelete(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
					+ "where ID_TARJETA = 1362 and id_tarjeta = 420",  configEntidad);*/
			
			
			//oraResp.oraDelete(report, xmlFile, configEntidad)
			
			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");
			
			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");
					
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
			Thread.sleep(1000);
			//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "compras");
			//Thread.sleep(5000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
			Thread.sleep(5000);
			
			System.out.println("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			
			//Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatus(configEntidad, opi)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi), equalTo(true));
			}
			
			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");
			
			
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlFile);
			
			
				opiCmd.stopOpiCmd(report, configEntidad, opi);
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, opi);
				Thread.sleep(5000);
				opiCmd.getOpiStatus(configEntidad, opi);
				Thread.sleep(5000);
				for(int i = 0; i < 10; i++) {			
				res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				System.out.println("ejecucion numero : " + i);
				
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91")) {
		//		if(!res.equals("unconnected ISOChannel")) {	
					System.out.println("***********ingrese al ifFFFFFF");
					break;
				}				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
			}
			
			String idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiemimcaut");			
			
            System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
            JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
            System.out.println("SE PARSEA STRING A JSON");
            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacionEmisor").getAsString();
            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
			
			
			
			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
			
			
			//SE COMIENZA A VALIDAR
			
			if (res.equals("00") || res.equals("55"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones, configEntidad);
			} else {
				report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "00");
				System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "00" + "\r\n");
				result = false;
			}

			System.out.println("##### FIN DE EJECUCION OPI #####");
			report.AddLine("##### FIN DE EJECUCION OPI #####");

						
			//POSTCONDICIONES

			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");


		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}

		return result;
	}
	/**********************COMPRA SERVICIO DIGITAL ****************/
	
	
public boolean servicioDigital(Reports report,String name, String configEntidad, String entidad, String TCFilesPath) {
		
		System.out.println("\r\n##################################################################################################################################################################################################################"
				+ "##################################################################################################################################################################################################################\r\n");
		System.out.println("Configuring "+name+ "\r\n");

		//SET INSTANCES
		sshWorker opiCmd = new sshWorker();
		dbWorker oraResp = new dbWorker();

		//Set Variables
		boolean result = true;
		String res = "";
		//String[] res = new String[2];
		//Set Reusltados Esperados
		String idAutorizacionEmisor = "0000000";				
		
		try {		
			//Preparación del archivo JSON del TC
			System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			System.out.println(TCFilesPath);
			
			String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));
			
			//Preparación del archivo XML del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			System.out.println(TCFilesPath);
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			
			System.out.println("JSON FILE:");
			System.out.println(jsonFile);
			System.out.println("XMLFile: ");
			System.out.println(xmlFile);
			
			JsonParser parser = new JsonParser();
			
			JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			System.out.println("JSON Object: ");
			System.out.println(jsonObject);
			JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");
			
			System.out.println("VALIDACIONES: ");
			System.out.println(validaciones);
			
			//Set esquema Base de datos
			//esquema = JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad");

			//SET dbWorker
			oraResp.setUser(JsonPath.from(configEntidad).getString("DB.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DB.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DB.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad"));
			
			
            //se ejecuta una limpieza de registro de impuestos
			
			
			oraResp.oraUpdate(report, "update acumulador_rg5272\r\n"
					+ "set acum_autorizaciones = 0,\r\n"
					+ "acum_autorizaciones_moneda_local = 0,\r\n"
					+ "acum_impuesto = 0,\r\n"
					+ "acum_presentaciones = 0,\r\n"
					+ "acum_impuesto_consumo = 0\r\n"
					+ "where id_cuenta = '10001468'", configEntidad);
			
			oraResp.oraUpdate(report, "update acumulador_rg5272\r\n"
					+ "set acum_autorizaciones = 0,\r\n"
					+ "acum_autorizaciones_moneda_local = 0,\r\n"
					+ "acum_impuesto = 0,\r\n"
					+ "acum_presentaciones = 0,\r\n"
					+ "acum_impuesto_consumo = 0\r\n"
					+ "where id_cuenta = '10001454'", configEntidad);
			
			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");
			
			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");
					
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
			Thread.sleep(1000);
			//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "compras");
			//Thread.sleep(5000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
			Thread.sleep(5000);
			
			System.out.println("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			
			//Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatus(configEntidad, opi)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi), equalTo(true));
			}
			
			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");
			
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlFile);
			
			
				opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				opiCmd.getOpiStatus(configEntidad, opi);
				Thread.sleep(5000);
				for(int i = 0; i < 10; i++) {			
				res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				System.out.println("ejecucion numero : " + i);
				
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91") && !res.equals("57")) {
		//		if(!res.equals("unconnected ISOChannel")) {	
					System.out.println("***********ingrese al ifFFFFFF");
					break;
				}				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
			}
			
			String idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiemimcaut");			
			
            System.out.println("LINEA ID AUTORIZACION ADQUIRENTE: " + idAutorizacionEmisorJosnString);
            JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
            System.out.println("SE PARSEA STRING A JSON");
            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacionEmisor").getAsString();
            System.out.println("ID AUTORIZACION ADQUIRENTE: " + idAutorizacionEmisor);
			
			
			
			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
			
			if (res.equals("00") || res.equals("55"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones, configEntidad);
			} else {
				report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "00");
				System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "00" + "\r\n");
				result = false;
			}

			System.out.println("##### FIN DE EJECUCION OPI #####");
			report.AddLine("##### FIN DE EJECUCION OPI #####");

						
			//POSTCONDICIONES

			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");


		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}

		return result;
	}
	
	/*********************FIN COMPRA SERVICIO DIGITAL *************/
	public boolean COMPRA_SALDO_INSUFICIENTE(Reports report,String name, String configEntidad, String entidad, String TCFilesPath) {
		
		System.out.println("\r\n##################################################################################################################################################################################################################"
				+ "##################################################################################################################################################################################################################\r\n");
		System.out.println("Configuring "+name+ "\r\n");

		//SET INSTANCES
		sshWorker opiCmd = new sshWorker();
		dbWorker oraResp = new dbWorker();

		//Set Variables
		boolean result = true;
		String res = "";
		//Set Reusltados Esperados
		String idAutorizacionEmisor = "0000000";				
		
		try {		
			//Preparación del archivo JSON del TC
			System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			System.out.println(TCFilesPath);
			
			String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));
			
			//Preparación del archivo XML del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			System.out.println(TCFilesPath);
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			
			System.out.println("JSON FILE:");
			System.out.println(jsonFile);
			System.out.println("XMLFile: ");
			System.out.println(xmlFile);
			
			JsonParser parser = new JsonParser();
			
			JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			System.out.println("JSON Object: ");
			System.out.println(jsonObject);
			JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");
			
			System.out.println("VALIDACIONES: ");
			System.out.println(validaciones);
			
			//Set esquema Base de datos
			//esquema = JsonPath.from(configEntidad).getString("ENTIDAD.esquema_db");

			//SET dbWorker
			oraResp.setUser(JsonPath.from(configEntidad).getString("DB.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DB.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DB.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad"));
			
			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");
			
			//PRECONDICIONES
			//System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			//report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");
					
			//CAMBIO DE ENTIDAD OPI
			//opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
			//Thread.sleep(1000);
			//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "compras");
			//Thread.sleep(1000);
			//opiCmd.startOpiCmd(report, configEntidad, "opiemimcaut");
			//Thread.sleep(1000);
			
			//System.out.println("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			//report.AddLine("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			
			//Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatus(configEntidad, opi)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi), equalTo(true));
			}
			
			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");
			
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlFile);
			/**********PAT*****************/
			opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
			Thread.sleep(5000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
			Thread.sleep(5000);
			opiCmd.getOpiStatus(configEntidad, opi);
			Thread.sleep(5000);
			for(int i = 0; i < 10; i++) {			
			res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
			Thread.sleep(5000);
			System.out.println("ejecucion numero : " + i);
			
			if(!res.equals("unconnected ISOChannel") && !res.equals("96")) {
		//	if(!res.equals("unconnected ISOChannel")) {
				System.out.println("***********ingrese al ifFFFFFF");
				break;
			}
			
			/**********PAT*****************/
			
			/*for(int i = 0; i < 3; i++) {
				opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
				Thread.sleep(1000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				opiCmd.getOpiStatus(configEntidad);
				Thread.sleep(5000);
				res = opiCmd.executeXml1(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
				if(!res.equals("91")) {
					break;
				}	*/			
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
			}
			
			idAutorizacionEmisor = opiCmd.getIdAutorizacionEmisorSinSaldo(report, configEntidad, "opiemimcaut");			
			
			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
			
			if (res.equals("51"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones, configEntidad);
			} else {
				report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "00");
				System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "00" + "\r\n");
				result = false;
			}

			System.out.println("##### FIN DE EJECUCION OPI #####");
			report.AddLine("##### FIN DE EJECUCION OPI #####");

						
			//POSTCONDICIONES

			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");


		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}

		return result;
	}

	/*********Methodo Anulacion****************/
	
	public boolean ANULACION(Reports report, String name, String configEntidad, String entidad, String TCFilesPath) {
		System.out.println(
				"\r\n##################################################################################################################################################################################################################"
						+ "##################################################################################################################################################################################################################\r\n");
		System.out.println("Configuring " + name + "\r\n");

		// SET INSTANCES
		sshWorker opiCmd = new sshWorker();
		dbWorker oraResp = new dbWorker();

		// Set Variables
		boolean result = true;
		String res = "";
		// Set Reusltados Esperados
		String idAutorizacionEmisor = "0000000";

		try {
			// Preparación del archivo JSON del TC
			System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");

			String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));

			// Preparación del archivo XML del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			String xmlTcFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));

			// Preparación del archivo XML de PRECONDICION del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
			String xmlPreFile = Files.readString(Paths.get(TCFilesPath + "/XML/PRE.xml"));

			System.out.println("JSON FILE:");
			System.out.println(jsonFile);
			System.out.println("XMLFile: ");
			System.out.println(xmlTcFile);
			System.out.println("XMLPreFile: ");
			System.out.println(xmlPreFile);

			JsonParser parser = new JsonParser();

			JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			System.out.println("JSON Object: ");
			System.out.println(jsonObject);
			JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");

			System.out.println("VALIDACIONES: ");
			System.out.println(validaciones);

			// Set esquema Base de datos
			//esquema = JsonPath.from(configEntidad).getString("ENTIDAD.esquema_db");

			// SET dbWorker
			oraResp.setUser(JsonPath.from(configEntidad).getString("DB.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DB.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DB.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad"));

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			// PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");

			// CAMBIO DE ENTIDAD OPI
			//opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
			//Thread.sleep(1000);
			//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "compras");
			//Thread.sleep(1000);
			//opiCmd.startOpiCmd(report, configEntidad, "opiemimcaut");
			//Thread.sleep(1000);

			// Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatus(configEntidad, opi)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi),
						equalTo(true));
			}

			// EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");

			// EJECUCION OPI
			System.out.println("##### INICIO EJECUCION PRECONDICION #####");
			report.AddLine("##### INICIO EJECUCION PRECONDICION #####");

			// Enviar a OPI el archivo XML de la PRECONDICION
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlPreFile);

			xmlTcFile = execPreCondicion(report, configEntidad, opiCmd, xmlPreFile, xmlTcFile);

			// Enviar a OPI el archivo del TC
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlTcFile);

			/***metodo propio*/
			opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
			Thread.sleep(5000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
			Thread.sleep(5000);
			opiCmd.getOpiStatus(configEntidad, opi);
			Thread.sleep(5000);
			for(int i = 0; i < 10; i++) {			
			res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
			Thread.sleep(5000);
			System.out.println("ejecucion numero : " + i);
			
			if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91")) {
	//		if(!res.equals("unconnected ISOChannel")) {	
				System.out.println("***********ingrese al ifFFFFFF");
				break;
			}				
			report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
			System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
		}
			/***fin metodo propio*/
			/*opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
			Thread.sleep(1000);
			//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "reversos");
			//Thread.sleep(1000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
			Thread.sleep(1000);

			// Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatus(configEntidad)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad),
						equalTo(true));
			}
			
			for (int i = 0; i < 3; i++) {
				opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
				Thread.sleep(1000);
				opiCmd.startOpiCmd(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
				if (!res.equals("91")) {
					break;
				}
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
			}*/

			idAutorizacionEmisor = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiemimcaut");			
			
			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
			
			if (res.equals("00"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones, configEntidad);
			} else {
				report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "00");
				System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "00" + "\r\n");
				result = false;
			}

			System.out.println("##### FIN DE EJECUCION OPI #####");
			report.AddLine("##### FIN DE EJECUCION OPI #####");

			// POSTCONDICIONES

			// Separador
			System.out.println(
					"##################################################################################################################################################################################################################"
							+ "##################################################################################################################################################################################################################\r\n");

		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}

		return result;
	}
	
	/*********Fin Methodo Anulacion****************/

	public boolean DEVOLUCION(Reports report,String name, String configEntidad, String entidad, String TCFilesPath) {

		System.out.println("\r\n##################################################################################################################################################################################################################"
				+ "##################################################################################################################################################################################################################\r\n");
		System.out.println("Configuring "+name+ "\r\n");

		//SET INSTANCES
		sshWorker opiCmd = new sshWorker();
		dbWorker oraResp = new dbWorker();

		//Set Variables
		boolean result = true;
		String res = "";
		//Set Reusltados Esperados
		String idAutorizacionEmisor = "0000000";				
		
		try {		
			//Preparación del archivo JSON del TC
			System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			
			String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));
			
			//Preparación del archivo XML del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			String xmlTcFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			
			//Preparación del archivo XML de PRECONDICION del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
			String xmlPreFile = Files.readString(Paths.get(TCFilesPath + "/XML/PRE.xml"));
			
			System.out.println("JSON FILE:");
			System.out.println(jsonFile);
			System.out.println("XMLFile: ");
			System.out.println(xmlTcFile);
			System.out.println("XMLPreFile: ");
			System.out.println(xmlPreFile);
			
			JsonParser parser = new JsonParser();
			
			JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			System.out.println("JSON Object: ");
			System.out.println(jsonObject);
			JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");
			
			System.out.println("VALIDACIONES: ");
			System.out.println(validaciones);
			
			//Set esquema Base de datos
			esquema = JsonPath.from(configEntidad).getString("ENTIDAD.esquema_db");

			//SET dbWorker
			oraResp.setUser(JsonPath.from(configEntidad).getString("DB.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DB.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DB.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad"));
			
			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");
			
			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");
					
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
			Thread.sleep(1000);
			//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "compras");
			//Thread.sleep(1000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
			Thread.sleep(1000);
			
			//Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatus(configEntidad, opi)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi), equalTo(true));
			}
			
			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");
			
			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION PRECONDICION #####");
			report.AddLine("##### INICIO EJECUCION PRECONDICION #####");
			
			//Enviar a OPI el archivo XML de la PRECONDICION
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlPreFile);
			
			xmlTcFile = execPreCondicion(report, configEntidad, opiCmd, xmlPreFile, xmlTcFile);
			
			//Enviar a OPI el archivo del TC
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlTcFile);
			
			for(int i = 0; i < 3; i++) {
				opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
				Thread.sleep(1000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				res = opiCmd.executeXml1(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
				if(!res.equals("91")) {
					break;
				}				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
			}
			
			idAutorizacionEmisor = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiemimcaut");			
			
			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
			
			if (res.equals("00"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionAdquirente: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionAdquirente: " + idAutorizacionEmisor + "\r\n");
				result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones, configEntidad);
			} else {
				report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "00");
				System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "00" + "\r\n");
				result = false;
			}

			System.out.println("##### FIN DE EJECUCION OPI #####");
			report.AddLine("##### FIN DE EJECUCION OPI #####");

						
			//POSTCONDICIONES

			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");


		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}

		return result;
	}
	
	/**********************DEVOLUCION - PRESENTACION************************/
	
	public boolean compraDevolucion(Reports report,String name, String configEntidad, String entidad, String TCFilesPath) {
		
		System.out.println("\r\n##################################################################################################################################################################################################################"
				+ "##################################################################################################################################################################################################################\r\n");
		System.out.println("*********************CONFIGURACION INICIAL "+name+ "\r\n");
		
		//SETEO INSTANCIA
				sshWorker opiCmd = new sshWorker();
				dbWorker oraResp = new dbWorker();
				
		//INICIO VARIABLES
				boolean result = true;
				String res = "";
		//SETEO RESULTADO ESPERADO
				String idAutorizacionEmisor = "0000000";
				
		//INICIO PASOS DENTRO DE UN CICLO TRY - CATCH PARA MANEJO DE ERRORES
				
				try {
					//Preparación del archivo JSON del TC
					System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
					report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
					
					String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));
					
					//Preparación del archivo XML del TC
					System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
					report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
					String xmlTcFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
					
					//Preparación del archivo XML de PRECONDICION del TC
					System.out.println("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
					report.AddLine("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
					String xmlPreFile = Files.readString(Paths.get(TCFilesPath + "/XML/PRE.xml"));
					
					System.out.println("JSON FILE:");
					System.out.println(jsonFile);
					System.out.println("XMLFile: ");
					System.out.println(xmlTcFile);
					System.out.println("XMLPreFile: ");
					System.out.println(xmlPreFile);
					
					JsonParser parser = new JsonParser();
					
					JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
					System.out.println("JSON Object: ");
					System.out.println(jsonObject);
					JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");
					
					System.out.println("VALIDACIONES: ");
					System.out.println(validaciones);
					
					//Set esquema Base de datos
					esquema = JsonPath.from(configEntidad).getString("ENTIDAD.esquema_db");

					//SET dbWorker
					oraResp.setUser(JsonPath.from(configEntidad).getString("DB.user"));
					oraResp.setPass(JsonPath.from(configEntidad).getString("DB.pass"));
					oraResp.setHost(JsonPath.from(configEntidad).getString("DB.host"));
					oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad"));
					
					System.out.println("**********************INICIA Test: " + name + " ------>\r\n");
					report.AddLine("**************************INICIA Test: " + name + " ------>");
					
					//PRECONDICIONES
					System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
					report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");
					
					// REALIZO STOP OPI
					opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
					Thread.sleep(1000);
					
					//REALIZO START OPI
					opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
					Thread.sleep(1000);
					
					//Se valida el funcionamiento de OPI
			/*		if (!opiCmd.getOpiStatus(configEntidad, opi)) {
						MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi), equalTo(true));
					}*/
					
					//EJECUCION OPI
					System.out.println("##### INICIO EJECUCION PRECONDICION #####");
					report.AddLine("##### INICIO EJECUCION PRECONDICION #####");
					
					oraResp.oraUpdate(report, "update acumulador_rg5272\r\n"
							+ "set acum_autorizaciones = 0,\r\n"
							+ "acum_autorizaciones_moneda_local = 0,\r\n"
							+ "acum_impuesto = 0,\r\n"
							+ "acum_presentaciones = 0,\r\n"
							+ "acum_impuesto_consumo = 0\r\n"
							+ "where id_cuenta = 10001454", configEntidad);
					
					oraResp.oraUpdate(report, "update acumulador_rg5272\r\n"
							+ "set acum_autorizaciones = 0,\r\n"
							+ "acum_autorizaciones_moneda_local = 0,\r\n"
							+ "acum_impuesto = 0,\r\n"
							+ "acum_presentaciones = 0,\r\n"
							+ "acum_impuesto_consumo = 0\r\n"
							+ "where id_cuenta = 10001468", configEntidad);
					
					//Enviar a OPI el archivo XML de la PRECONDICION
					
					opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlPreFile);
						
					//ejecuto el archivo enviado
					
					for(int i = 0; i < 3; i++) {
						opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
						Thread.sleep(1000);
						opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
						Thread.sleep(5000);
						res = opiCmd.executeXml1(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
						if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91")) {
						//if(!res.equals("91")) {
							break;
						}				
						report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
						System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
					}
					
					String idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiemimcaut");	
					
					 System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
			            JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
			            System.out.println("SE PARSEA STRING A JSON");
			            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacionEmisor").getAsString();
			            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
					
					System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
					
					//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
					//oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
					//ppCondi.preCondicionBD(report, rtaQuery);
					
					//SETEO LA AUTORIZACION COMO PRESENTADA
					oraResp.oraUpdate(report, "UPDATE AUTORIZACION \r\n"
							+ "SET PRESENTACION_PROCESADA = 2,\r\n"
							+ "FECHA_RELACION = '28/08/2023' \r\n"
							+ "where ID_AUTORIZACION_EMISOR = " + idAutorizacionEmisor, configEntidad);
					
					oraResp.oraUpdate(report, "UPDATE AJUSTES_SOCIOS \r\n"
							+ "SET ID_ESTADO = 2,\r\n"
							+ "ID_CONSUMO = '12876' \r\n"
							+ "where ID_AUTORIZACION = " + idAutorizacionEmisor, configEntidad);
					//Seteo la cuenta en estado activa para que no falle el TC
					//rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
					//ppCondi.preCondicionBD(report, rtaQuery);
					
					
					opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlTcFile);
					
					for(int i = 0; i < 3; i++) {
						opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
						Thread.sleep(1000);
						opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
						Thread.sleep(5000);
						res = opiCmd.executeXml1(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
						if(!res.equals("91")) {
							break;
						}				
						report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
						System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
					}
					
					String idAutorizacionEmisorJosnString2 = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiemimcaut");	
					
					 System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString2);
			            JsonObject idAutoEmiObj2 = parser.parse(idAutorizacionEmisorJosnString2).getAsJsonObject();
			            System.out.println("SE PARSEA STRING A JSON");
			            idAutorizacionEmisor = idAutoEmiObj2.get("IdAutorizacionEmisor").getAsString();
			            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
					
					System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
					
					if (res.equals("00"))
					{
						report.AddLine("Ejecucion Correcta<br>DE39: " + res);
						System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
						//VALIDACIONES
						report.AddLine("idAutorizacionAdquirente: " + idAutorizacionEmisor);
						System.out.println("##[section] : idAutorizacionAdquirente: " + idAutorizacionEmisor + "\r\n");
						result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones, configEntidad);
					} else {
						report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "00");
						System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "00" + "\r\n");
						result = false;
					}
					
			
					
					
				} catch (Exception e) {
					report.AddLine("Error:<br>" + e);
					System.out.println("##[warning] : Error:\r\n" + e);
					result = false;
				}
				
		return result;
		
	} 
	
	/**********************FIN DEVOLUCION - PRESENTACION********************/
	
	public boolean REVERSO(Reports report,String name, String configEntidad, String entidad, String TCFilesPath) {
		
		System.out.println("\r\n##################################################################################################################################################################################################################"
				+ "##################################################################################################################################################################################################################\r\n");
		System.out.println("Configuring "+name+ "\r\n");

		//SET INSTANCES
		sshWorker opiCmd = new sshWorker();
		dbWorker oraResp = new dbWorker();

		//Set Variables
		boolean result = true;
		String res = "";
		//String[] res = new String[2];
		//Set Reusltados Esperados
		String idAutorizacionEmisor = "0000000";				
		
		try {		
			//Preparación del archivo JSON del TC
			System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			System.out.println(TCFilesPath);
			
			String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));
			
			//Preparación del archivo XML del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			System.out.println(TCFilesPath);
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			
			System.out.println("JSON FILE:");
			System.out.println(jsonFile);
			System.out.println("XMLFile: ");
			System.out.println(xmlFile);
			
			JsonParser parser = new JsonParser();
			
			JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			System.out.println("JSON Object: ");
			System.out.println(jsonObject);
			JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");
			
			System.out.println("VALIDACIONES: ");
			System.out.println(validaciones);
			
			//Set esquema Base de datos
			//esquema = JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad");

			//SET dbWorker
			oraResp.setUser(JsonPath.from(configEntidad).getString("DB.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DB.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DB.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad"));
			
			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");
			
			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");
					
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
			Thread.sleep(1000);
			//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "compras");
			//Thread.sleep(5000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
			Thread.sleep(5000);
			
			System.out.println("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			
			//Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatus(configEntidad, opi)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi), equalTo(true));
			}
			
			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");
			
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlFile);
			
			
				opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				opiCmd.getOpiStatus(configEntidad, opi);
				Thread.sleep(5000);
				for(int i = 0; i < 10; i++) {			
				res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				System.out.println("ejecucion numero : " + i);
				
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91")) {
		//		if(!res.equals("unconnected ISOChannel")) {	
					System.out.println("***********ingrese al ifFFFFFF");
					break;
				}				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
			}
			
			idAutorizacionEmisor = opiCmd.getIdAutorizacionEmisorReverso(report, configEntidad, "opiemimcaut");			
			
			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
			
			if (res.equals("00"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones, configEntidad);
			} else {
				report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "00");
				System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "00" + "\r\n");
				result = false;
			}

			System.out.println("##### FIN DE EJECUCION OPI #####");
			report.AddLine("##### FIN DE EJECUCION OPI #####");

						
			//POSTCONDICIONES

			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");


		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}

		return result;
	}
	/**************/
	
	public boolean ANULACIONREVERSO(Reports report, String name, String configEntidad, String entidad, String TCFilesPath) {

		System.out.println(
				"\r\n##################################################################################################################################################################################################################"
						+ "##################################################################################################################################################################################################################\r\n");
		System.out.println("Configuring " + name + "\r\n");

		// SET INSTANCES
		sshWorker opiCmd = new sshWorker();
		dbWorker oraResp = new dbWorker();
		xmlWorker xmlWorker = new xmlWorker();

		// Set Variables
		boolean result = true;
		String res = "";
		// Set Reusltados Esperados
		String idAutorizacionEmisor = "0000000";

		// Set Reusltados Esperados
		String expectedDE39 = "00";

		try {
			// Preparación del archivo JSON del TC
			System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");

			String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));

			// Preparación del archivo XML del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			String xmlTcFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));

			// Preparación del archivo XML de PRECONDICION del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
			String xmlPreFile = Files.readString(Paths.get(TCFilesPath + "/XML/PRE.xml"));

			System.out.println("JSON FILE:");
			System.out.println(jsonFile);
			System.out.println("XMLFile: ");
			System.out.println(xmlTcFile);
			System.out.println("XMLPreFile: ");
			System.out.println(xmlPreFile);

			// Se instancia JsonParser.
			JsonParser parser = new JsonParser();

			JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			System.out.println("JSON Object: ");
			System.out.println(jsonObject);

			JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");
			//expectedDE39 = jsonObject.get("expectedDE39").getAsString();
			System.out.println("expectedDE39: " + expectedDE39);

			// Set esquema Base de datos
			//esquema = JsonPath.from(configEntidad).getString("ENTIDAD.esquema_db");

			// SET dbWorker
			oraResp.setUser(JsonPath.from(configEntidad).getString("DB.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DB.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DB.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad"));

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			// PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");

			// CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
			Thread.sleep(1000);
			//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "compras");
			//Thread.sleep(1000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
			Thread.sleep(10000);

			// Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatus(configEntidad, opi)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi),
						equalTo(true));
			}

			// EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");

			// EJECUCION OPI
			System.out.println("##### INICIO EJECUCION PRECONDICION #####");
			report.AddLine("##### INICIO EJECUCION PRECONDICION #####");

			// Enviar a OPI el archivo XML de la PRECONDICION
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlPreFile);

			//xmlTcFile = execPreCondicion(report, configEntidad, opiCmd, xmlPreFile, xmlTcFile);

			

			for (int i = 0; i < 3; i++) {
				opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
				Thread.sleep(1000);
				//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "reversos");
				//Thread.sleep(1000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
				Thread.sleep(10000);
				
				res = opiCmd.executeXml1(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91")) {
					break;
				}
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
			}
			

			String idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiemimcaut");
			
			 System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
	            JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
	            System.out.println("SE PARSEA STRING A JSON");
	            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacionEmisor").getAsString();
	            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
			
			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);

			
			// Enviar a OPI el archivo del TC
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlTcFile);
			
			//for(int i = 0; i < 3; i++) {
				opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
				Thread.sleep(1000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);				
				for(int i = 0; i < 10; i++) {			
					res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
					Thread.sleep(5000);
					System.out.println("ejecucion numero : " + i);
					
					if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91")) {
			//		if(!res.equals("unconnected ISOChannel")) {	
						System.out.println("***********ingrese al ifFFFFFF");
						break;
					}				
					report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
					System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
				}
				/*
				res = opiCmd.executeXml1(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91")) {
					break;
				}				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
			}
			*/
				
			String idAutorizacionEmisorJosnString2 = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiemimcaut");	
			
			 System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString2);
	            JsonObject idAutoEmiObj2 = parser.parse(idAutorizacionEmisorJosnString2).getAsJsonObject();
	            System.out.println("SE PARSEA STRING A JSON");
	            idAutorizacionEmisor = idAutoEmiObj2.get("IdAutorizacionEmisor").getAsString();
	            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
			
			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
			
			if (res.equals("00"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones, configEntidad);
			} else {
				report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "00");
				System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "00" + "\r\n");
				result = false;
			}
			
	
			
			
		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}
		
return result;

} 
	/*********************************************METODO STRESS************************************/
public boolean metodoStress(Reports report,String name, String configEntidad, String entidad, String TCFilesPath) {
		
		System.out.println("\r\n##################################################################################################################################################################################################################"
				+ "##################################################################################################################################################################################################################\r\n");
		System.out.println("Configuring "+name+ "\r\n");

		//SET INSTANCES
		sshWorker opiCmd = new sshWorker();
		dbWorker oraResp = new dbWorker();
		PrePostCondi ppCondi = new PrePostCondi();

		//Set Variables
		boolean result = true;
		String res = "";
		//String[] res = new String[2];
		//Set Reusltados Esperados
		String idAutorizacionEmisor = "0000000";
		int rtaQuery;
		String ID_CUENTA = "10001454";
		String ID_CUENTA_EXT = "10001468";
		
		try {		
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//Preparación del archivo JSON del TC
			System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			System.out.println(TCFilesPath);
			
			String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));
			
			//Preparación del archivo XML del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			System.out.println(TCFilesPath);
			String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			
			System.out.println("JSON FILE:");
			System.out.println(jsonFile);
			System.out.println("XMLFile: ");
			System.out.println(xmlFile);
			
			JsonParser parser = new JsonParser();
			
			JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			System.out.println("JSON Object: ");
			System.out.println(jsonObject);
			JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");
			
			System.out.println("VALIDACIONES: ");
			System.out.println(validaciones);
			
			//Set esquema Base de datos
			//esquema = JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad");

			//SET dbWorker
			oraResp.setUser(JsonPath.from(configEntidad).getString("DB.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DB.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DB.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad"));
			
			
			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");
			
			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");
					
			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
			Thread.sleep(1000);
			//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "compras");
			//Thread.sleep(5000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
			Thread.sleep(5000);
			
			System.out.println("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			
			//Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatus(configEntidad, opi)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi), equalTo(true));
			}
			
			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");
			
			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlFile);
			
			
				opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				opiCmd.getOpiStatus(configEntidad, opi);
				Thread.sleep(5000);
				for(int i = 0; i < 10; i++) {			
				res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				System.out.println("ejecucion numero : " + i);
				
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91") && !res.equals("57")) {
		//		if(!res.equals("unconnected ISOChannel")) {	
					System.out.println("***********ingrese al ifFFFFFF");
					break;
				}				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
			}
			
			String idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiemimcaut");			
			
            System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
            JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
            System.out.println("SE PARSEA STRING A JSON");
            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacionEmisor").getAsString();
            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
			
			
			
			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
			
			if (res.equals("00") || res.equals("55"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones, configEntidad);
			} else {
				report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "00");
				System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "00" + "\r\n");
				result = false;
			}

			System.out.println("##### FIN DE EJECUCION OPI #####");
			report.AddLine("##### FIN DE EJECUCION OPI #####");

						
			//POSTCONDICIONES

			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");


		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}

		return result;
	}
	
	/***********************************************************************************************/
	
	
	/*************/
	
	private String execPreCondicion(Reports report, String configEntidad, sshWorker opiCmd, String xmlPreFile, String xmlTcFile) throws IOException {
		//VARIABLES
		String[] res = new String[3];
		String DE37;
		String DE38;
		
		//Se obtienen los datos de la precondición
		
		try {
		
		for(int i = 0; i < 3; i++) {
			opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
			Thread.sleep(1000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
			Thread.sleep(5000);
			res = opiCmd.sshPreCondi(report, xmlPreFile, configEntidad, "opiemimcaut");
			if(!res[0].equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91")){//agrego para emision
			//if(!res[0].equals("91")) {
				break;
			}				
			report.AddLine("Ejecucion Incorrecta<br>DE39: " + res[0]);
			System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res[0] + "\r\n");
		}
		
		}catch(InterruptedException e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
		}

		//Se verifica la correcta ejecución y se generan los datos
		if (res[0].equals("00"))
		{
			DE37 = res[1];
			DE38 = res[2];
			report.AddLine("Ejecucion Correcta<br>DE39: " + res[0]);
			System.out.println("##[section] : Ejecucion Correcta\r\nDE37: " + DE37 + "\r\n");
			System.out.println("##[section] : Ejecucion Correcta\r\nDE38: " + DE38 + "\r\n");
			xmlTcFile = xmlTcFile.replace("{{DE37}}", DE37).replace("{{DE38}}", DE38);
		} else {
			report.AddLineAssertionError("FAIL<br>DE39: " + res[0] + " Se esperaba: " + "00");
			System.out.println("##[warning] : FAIL : \r\nDE39: " + res[0] + " Se esperaba: " + "00" + "\r\n");
			MatcherAssert.assertThat("Resultado DE39", res[0], equalTo("00"));
		}
	
		System.out.println("Contenido xmlBase:\r\n" + xmlTcFile);
		
		return xmlTcFile;
	}
	
	/*********validacion general 2*************/
	
	private boolean validacionGral2(dbWorker oraResp, sshWorker sshWorker, xmlWorker xmlWorker, Reports report,
			String configEntidad, String idAutorizacionAdquirente, String entidad, boolean applyDB, boolean applyMSG,
			JsonObject tcValidation) throws SQLException, JDOMException, IOException {

		// Variables
		boolean dataBaseResult = true;
		boolean messengerResult = true;

		if (applyMSG) {
			report.AddLine("Se obtienen validaciones de MENSAJERIA");
			System.out.println("##[section] : Se obtienen validaciones de MENSAJERIA");
			JsonArray messengerValidations = tcValidation.getAsJsonArray("VALIDACIONESMSG");
			System.out.println("VALIDACIONES MSG: " + messengerValidations);
			messengerResult = messengerValidations(messengerValidations, sshWorker, report, configEntidad, xmlWorker);
			System.out.println("messengerResult: " + messengerResult);
		} else {
			System.out.println("##### NO EXISTEN VALIDACIONES DE MENSAJERIA PARA ESTE CASO DE PRUEBA #####");
			report.AddLine("##### NO EXISTEN VALIDACIONES DE MENSAJERIA PARA ESTE CASO DE PRUEBA #####");
		}

		if (applyDB) {
			report.AddLine("Se obtienen validaciones de DB");
			System.out.println("##[section] : Se obtienen validaciones de DB");
			JsonArray dbValidations = tcValidation.getAsJsonArray("VALIDACIONESDB");
			System.out.println("VALIDACIONES DB: " + dbValidations);
			dataBaseResult = dbValidations(oraResp, report, idAutorizacionAdquirente, dbValidations, configEntidad);
			System.out.println("dataBaseResult: " + dataBaseResult);
		} else {
			System.out.println("##### NO EXISTEN VALIDACIONES DE BASE DE DATOS PARA ESTE CASO DE PRUEBA #####");
			report.AddLine("##### NO EXISTEN VALIDACIONES DE BASE DE DATOS PARA ESTE CASO DE PRUEBA #####");
		}

		System.out.println("Validations Result: " + (dataBaseResult && messengerResult));
		return dataBaseResult && messengerResult;
	}
	
	
	/*****************************************/
	
	
	/******************dbValidations*********/
	
	private boolean dbValidations(dbWorker oraResp, Reports report, String idAutorizacionAdquirente,
			JsonArray dbValidaciones, String configEntidad) throws SQLException {

		System.out.println("##### INICIAN VALIDACIONES DE BASE DE DATOS #####");
		report.AddLine("##### INICIAN VALIDACIONES DE BASE DE DATOS #####");

		// VARIABLES
		boolean result = true;
		List<String> Status = new ArrayList<String>();
		String queryVerf;
		String validaRes[][];
		int validationsIndex = 0;

		// ACCIONES POR CADA VALIDACION DE DB
		for (JsonElement validacion : dbValidaciones) {
			validationsIndex++;
			System.out.println("VALIDACION NRO: " + validationsIndex);

			JsonObject validacionObject = validacion.getAsJsonObject();
			queryVerf = validacionObject.get("query").getAsString();
			queryVerf = queryVerf.replace("{{esquema}}", esquema);
			queryVerf = queryVerf.replace("{{id_autorizacion_adquirente}}", idAutorizacionAdquirente);
			JsonArray camposEsperados = validacionObject.get("camposEsperados").getAsJsonArray();
			JsonArray resultadosEsperados = validacionObject.get("valoresEsperados").getAsJsonArray();

			System.out.println("Query: " + queryVerf);
			System.out.println("Campos Esperados: " + camposEsperados);
			System.out.println("ResultadosEsperados: " + resultadosEsperados);

			validaRes = oraResp.executeQuery(queryVerf, configEntidad);

			for (int i = 0; i < validaRes.length; i++) {
				for (int j = 0; j < validaRes[i].length; j++) {
					System.out.println("validaRes: " + validaRes[i][j] + " - resultadoEsperado: "
							+ resultadosEsperados.get(i).getAsJsonArray().get(j).getAsString());
					if (validaRes[i][j].equals(resultadosEsperados.get(i).getAsJsonArray().get(j).getAsString())) {
						report.AddLine("Ejecucion Correcta:<br>Se cumplieron todas las validaciones");
						System.out.println(
								"##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
						Status.add("P");
					} else {
						report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
						System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
						Status.add("FAIL - VALIDACION NRO: " + validationsIndex + " CAMPO: "
								+ camposEsperados.get(i).getAsString());
					}
				}
			}

		}

		// Verificacion de todos los resultados obtenidos
		for (String state : Status)
			if (!state.equals("P")) {
				report.AddLineAssertionError("===== " + Status + " =====");
				;
				System.out.println("===== " + Status + " =====");
				result = false;
			}

		System.out.println("##### FIN DE EJECUCION DE VALIDACIONES DATA BASE #####");
		report.AddLine("##### FIN DE EJECUCION DE VALIDACIONES DATA BASE #####");
		return result;
	}
	
	
	/***************************************/
	
 	private boolean validacionGral(dbWorker oraResp, Reports report, String idAutorizacionEmisor, String entidad, JsonArray validaciones, String configEntidad) throws SQLException {
		//Variables
		boolean result = true;
		String queryVerf;
		
		System.out.println("##### INICIO EJECUCION DE VALIDACIONES #####");
		report.AddLine("##### INICIO EJECUCION DE VALIDACIONES #####");
		
		for(JsonElement validacion : validaciones) {
			JsonObject validacionObject = validacion.getAsJsonObject();
			queryVerf = validacionObject.get("query").getAsString();
			//queryVerf = queryVerf.replace("{{esquema}}", esquema);
			queryVerf = queryVerf.replace("{{id_autorizacion_emisor}}", idAutorizacionEmisor);
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
	
	private void Validacion(dbWorker oraResp, Reports report , String queryVerf, JsonArray resultadosEsperados, String entidad, String configEntidad) throws SQLException {
		
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
	/*********************************VALIDACION LEMON QA********************************************/
	private boolean validacionGralLemonQA(dbWorker oraResp, Reports report, String idAutorizacionEmisor, String entidad, JsonArray validaciones) throws SQLException {
		//Variables
		boolean result = true;
		String queryVerf;
		
		System.out.println("##### INICIO EJECUCION DE VALIDACIONES #####");
		report.AddLine("##### INICIO EJECUCION DE VALIDACIONES #####");
		
		for(JsonElement validacion : validaciones) {
			JsonObject validacionObject = validacion.getAsJsonObject();
			queryVerf = validacionObject.get("query").getAsString();
			//queryVerf = queryVerf.replace("{{esquema}}", esquema);
			queryVerf = queryVerf.replace("{{id_autorizacion_emisor}}", idAutorizacionEmisor);
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
			
			ValidacionLemonQA(oraResp, report, queryVerf, resultadosEsperados, entidad);
			
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
	
	private void ValidacionLemonQA(dbWorker oraResp, Reports report , String queryVerf, JsonArray resultadosEsperados, String entidad) throws SQLException {
		
		String validaRes[][];
		
		
		validaRes = oraResp.executeQueryLemonQA(queryVerf);
		
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
	/***********************************************************************************************/
	
	/*********************************VALIDACION LEMON UAT********************************************/
	private boolean validacionGralLemonUAT(dbWorker oraResp, Reports report, String idAutorizacionEmisor, String entidad, JsonArray validaciones) throws SQLException {
		//Variables
		boolean result = true;
		String queryVerf;
		
		System.out.println("##### INICIO EJECUCION DE VALIDACIONES #####");
		report.AddLine("##### INICIO EJECUCION DE VALIDACIONES #####");
		
		for(JsonElement validacion : validaciones) {
			JsonObject validacionObject = validacion.getAsJsonObject();
			queryVerf = validacionObject.get("query").getAsString();
			//queryVerf = queryVerf.replace("{{esquema}}", esquema);
			queryVerf = queryVerf.replace("{{id_autorizacion_emisor}}", idAutorizacionEmisor);
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
			
			ValidacionLemonUAT(oraResp, report, queryVerf, resultadosEsperados, entidad);
			
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
	
	private void ValidacionLemonUAT(dbWorker oraResp, Reports report , String queryVerf, JsonArray resultadosEsperados, String entidad) throws SQLException {
		
		String validaRes[][];
		
		validaRes = oraResp.executeQueryLemonUAT(queryVerf);
		
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
	/***********************************************************************************************/
	private boolean messengerValidations(JsonArray validacionesMensajeria, sshWorker opiCmd, Reports report,
			String configEntidad, xmlWorker xmlWorker) throws JDOMException, IOException {

		System.out.println("##### INICIAN VALIDACIONES DE MENSAJERIA #####");
		report.AddLine("##### INICIAN VALIDACIONES DE MENSAJERIA #####");

		List<String> Status = new ArrayList<String>();
		boolean msgResult = true;
		int validationsIndex = 0;

		String stringXmlMessages = opiCmd.getMessages(report, configEntidad, "opiemimcaut");
		// String strinOutXmlMessages = opiCmd.getOutMessages(report, configEntidad,
		// "opiemimcaut");

		System.out.println("******* XML MESSAGES ***** \n" + stringXmlMessages + "\n");
		// System.out.println("******* OUT XML MESSAGES ***** \n" + strinOutXmlMessages
		// + "\n");

		String[] splitedXmlMessages = xmlWorker.multipleStringXmlSplitter(stringXmlMessages);
		System.out.println(splitedXmlMessages.length);
		String[] xmlMessages = new String[4];

		xmlMessages[0] = splitedXmlMessages[0];
		xmlMessages[1] = splitedXmlMessages[2];
		xmlMessages[2] = splitedXmlMessages[4];
		xmlMessages[3] = splitedXmlMessages[6];

		xmlWorker.setMessages(xmlMessages);

		for (JsonElement messageValidation : validacionesMensajeria) {
			validationsIndex++;
			System.out.println("VALIDACION NRO: " + validationsIndex);

			JsonObject msgValidationObj = messageValidation.getAsJsonObject();
			String mti = msgValidationObj.get("mti").getAsString();
			JsonArray expectedFields = msgValidationObj.get("camposValidaciones").getAsJsonArray();

			String[][] messageResponse = xmlWorker.getResponse(mti, expectedFields);

			for (int i = 0; i < messageResponse.length; i++) {
				for (int j = 0; j < messageResponse[i].length; j++) {
					System.out.println("MessageResponse: " + messageResponse[i][j]);
				}
			}

			for (int i = 0; i < messageResponse.length; i++) {
				System.out.println("Resultado obtenido DE" + messageResponse[i][0] + ": " + messageResponse[i][1]
						+ " - resultadoEsperado DE" + expectedFields.get(i).getAsJsonArray().get(0).getAsString() + ": "
						+ expectedFields.get(i).getAsJsonArray().get(1).getAsString());

				if (messageResponse[i][1].equals(expectedFields.get(i).getAsJsonArray().get(1).getAsString())) {
					report.AddLine("Ejecucion Correcta:<br>Se cumplieron todas las validaciones");
					System.out.println("##[section] : Ejecucion Correcta:\r\nSe cumplieron todas las validaciones\r\n");
					Status.add("P");
				} else {
					report.AddLineAssertionError("FAIL<br>No se cumplieron todas las validaciones");
					System.out.println("##[warning] : FAIL:\r\nNo se cumplieron todas las validaciones\r\n");
					Status.add("FAIL - VALIDACION NRO: " + validationsIndex + " DE: "
							+ expectedFields.get(i).getAsJsonArray().get(0).getAsString());
				}

			}

		}

		// Verificacion de todos los resultados obtenidos
		for (String state : Status)
			if (!state.equals("P")) {
				report.AddLineAssertionError("===== " + Status + " =====");
				;
				System.out.println("===== " + Status + " =====");
				msgResult = false;
			}

		System.out.println("##### FIN DE EJECUCION DE VALIDACIONES MENSAJERIA #####");
		report.AddLine("##### FIN DE EJECUCION DE VALIDACIONES MENSAJERIA #####");
		return msgResult;
	}
}
