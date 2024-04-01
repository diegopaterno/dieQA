package opi.wolfgang;

import static org.hamcrest.Matchers.equalTo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import Tools.sshWorker;
import Tools.xmlWorker;
import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import com.google.gson.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class AUT_OPI_OPERACIONES {
	private List<String> Status = new ArrayList<String>();
	private String esquema;
	List<dbWorker> results = new ArrayList<>();
	//private String Status;
	WebDriver driver;
	String opi;
	
	public AUT_OPI_OPERACIONES(String opi) {
		this.opi=opi;
	}
	
	
	
	public static boolean COMPRA(Reports report,String name, String configEntidad, String entidad, String TCFilesPath) {

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
			/*if (!opiCmd.getOpiStatus(configEntidad, opi)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi), equalTo(true));
			}*/

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
				//result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones, configEntidad);
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
	public boolean CompraLemonQA(Reports report,String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22) {

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
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//seteo disponible de cuenta
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 1000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			//seteo disponible de cuenta
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE_ADELANTO = 1000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonQA(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
					+ "where ID_TARJETA= 26 OR ID_TARJETA = 1",  configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//borrar debitos automaticos para que no fallen los siguientes casos de prueba
			//oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
				//+ "where ID_TARJETA= 26 OR ID_TARJETA = 1",  configEntidad);
			
			//oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
				//	+ "where ID_TARJETA=26",  configEntidad);

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
			xmlFile = xmlFile.replace("{{DE22}}", DE22);
			/*
			 * // obtengo el valor del DE38 de la ejecucion  
						String DE38 = JsonPath.from(respBody).getString("DE38");				
						// obtengo el body para la devolucion
						bodyData2 = new String(Files.readString(Paths.get(TCFilesPath + "/TXT/TC.txt")));					
						System.out.println("este es el body 2 " + bodyData2);
						// inserto el valor del DE38 en el body para la devolucion				
						bodyData2 = bodyData2.replace("{{DE38}}", DE38);			
						System.out.println("este es el body para generar la devolucion :" + bodyData2);	
			 * */

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
			String ambiente="qa";
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, ambiente);
			Thread.sleep(5000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, ambiente);
			Thread.sleep(5000);
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
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
							
				/*if(!res.equals("57")) {
					oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
							+ "where ID_TARJETA= 26 OR ID_TARJETA = 1",  configEntidad);
					ppCondi.preCondicionBD(report, rtaQuery);
				}else {
					continue;
				}*/
				
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
	/*************************compra OPI "opiVisaAutoQA"***************************************************/
	public boolean CompraLemonUAT(Reports report,String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22) {

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
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//seteo disponible de cuenta
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 1000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			//seteo disponible de cuenta
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE_ADELANTO = 1000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			oraResp.oraDeleteLemonUAT(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
					+ "where ID_TARJETA= 19 OR ID_TARJETA = 1",  configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//borrar debitos automaticos para que no fallen los siguientes casos de prueba
			//oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
				//+ "where ID_TARJETA= 26 OR ID_TARJETA = 1",  configEntidad);
			
			//oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
				//	+ "where ID_TARJETA=26",  configEntidad);

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
			xmlFile = xmlFile.replace("{{DE22}}", DE22);
			/*
			 * // obtengo el valor del DE38 de la ejecucion  
						String DE38 = JsonPath.from(respBody).getString("DE38");				
						// obtengo el body para la devolucion
						bodyData2 = new String(Files.readString(Paths.get(TCFilesPath + "/TXT/TC.txt")));					
						System.out.println("este es el body 2 " + bodyData2);
						// inserto el valor del DE38 en el body para la devolucion				
						bodyData2 = bodyData2.replace("{{DE38}}", DE38);			
						System.out.println("este es el body para generar la devolucion :" + bodyData2);	
			 * */

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
			
			String ambiente="uat";
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, ambiente);
			Thread.sleep(5000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, ambiente);
			Thread.sleep(5000);
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
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
							
				/*if(!res.equals("57")) {
					oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
							+ "where ID_TARJETA= 26 OR ID_TARJETA = 1",  configEntidad);
					ppCondi.preCondicionBD(report, rtaQuery);
				}else {
					continue;
				}*/
				
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
           // idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacionEmisor").getAsString();
            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacion").getAsString();
            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);



			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);

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
	
	/*************************compra OPI "opiVisaAutoQA"***************************************************/
	public boolean compraTotalCoinUAT(Reports report,String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22) {

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
		//String ID_CUENTA = "2";
		//String ID_CUENTA_EXT = "10015647";

		try {		
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//seteo disponible de cuenta
			rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 1000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			//seteo disponible de cuenta
			rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE_ADELANTO = 1000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			//rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			//rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			
			oraResp.oraDeleteTotalCoinUAT(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
					+ "where ID_TARJETA= 39",  configEntidad);
				//	+ "where ID_TARJETA= 39 OR ID_TARJETA = 1",  configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//borrar debitos automaticos para que no fallen los siguientes casos de prueba
			//oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
				//+ "where ID_TARJETA= 26 OR ID_TARJETA = 1",  configEntidad);
			
			//oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
				//	+ "where ID_TARJETA=26",  configEntidad);

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
			xmlFile = xmlFile.replace("{{DE22}}", DE22);
			/*
			 * // obtengo el valor del DE38 de la ejecucion  
						String DE38 = JsonPath.from(respBody).getString("DE38");				
						// obtengo el body para la devolucion
						bodyData2 = new String(Files.readString(Paths.get(TCFilesPath + "/TXT/TC.txt")));					
						System.out.println("este es el body 2 " + bodyData2);
						// inserto el valor del DE38 en el body para la devolucion				
						bodyData2 = bodyData2.replace("{{DE38}}", DE38);			
						System.out.println("este es el body para generar la devolucion :" + bodyData2);	
			 * */

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
			oraResp.setUser(JsonPath.from(configEntidad).getString("DBUAT.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DBUAT.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DBUAT.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_uat"));


			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");

			//CAMBIO DE ENTIDAD OPI
			
			//String ambiente="uat";
			//opiCmd.cambioAmbiente(report, configEntidad, "opiemimcaut", entidad, ambiente);
			//Thread.sleep(5000);
			//opiCmd.cambioKeycloak(report, configEntidad, "opiemimcaut", entidad, ambiente);
			//Thread.sleep(5000);
			//opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
			//Thread.sleep(1000);
			//opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
			//Thread.sleep(5000);

			System.out.println("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### FINALIZA EJECUCION DE PRECONDICIONES #####");

			//Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatusTotalCoinUAT(configEntidad)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatusTotalCoinUAT(configEntidad), equalTo(true));
			}

			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");

			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlTotalCoinUAT(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				//opiCmd.getOpiStatus(configEntidad);
				//Thread.sleep(5000);
				
				for (int i = 0; i < 10; i++) {			
				res = opiCmd.executeXmlTotalCoinUAT(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				System.out.println("ejecucion numero : " + i);
							
				/*if(!res.equals("57")) {
					oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
							+ "where ID_TARJETA= 26 OR ID_TARJETA = 1",  configEntidad);
					ppCondi.preCondicionBD(report, rtaQuery);
				}else {
					continue;
				}*/
				
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
           // idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacionEmisor").getAsString();
            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacion").getAsString();
            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);



			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);

			if (res.equals("00") || res.equals("55"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				result = validacionGralTotalCoinUAT(oraResp, report, idAutorizacionEmisor, entidad, validaciones);
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
	public boolean compra(Reports report,String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22, String DE2, String ambiente) {

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
		//String ID_CUENTA = "2";
		//String ID_CUENTA_EXT = "10015647";

		try {		
			System.out.println("ESTE ES EL AMBIENTE DE PRUEBA SETEADO: " + ambiente.toUpperCase());
			
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");
			
			
			System.out.println("ESTE ES EL SWITCH DESDE DONDE EJECUTAMOS: " + opi.toUpperCase());
			
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			//rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			///ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			//rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//seteo disponible de cuenta
			//rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 1000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 100000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			//seteo disponible de cuenta
			//rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE_ADELANTO = 1000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE_ADELANTO = 100000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			//rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			//rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			
			oraResp.oraDelete(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
				//	+ "where ID_TARJETA= 39",  configEntidad);
					+ "where ID_TARJETA= 39 OR ID_TARJETA = 1 OR ID_TARJETA = 26 OR ID_TARJETA = 19",  configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//borrar debitos automaticos para que no fallen los siguientes casos de prueba
			//oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
				//+ "where ID_TARJETA= 26 OR ID_TARJETA = 1",  configEntidad);
			
			//oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
				//	+ "where ID_TARJETA=26",  configEntidad);

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
			xmlFile = xmlFile.replace("{{DE22}}", DE22);
			xmlFile = xmlFile.replace("{{DE2}}", DE2);
			/*
			 * // obtengo el valor del DE38 de la ejecucion  
						String DE38 = JsonPath.from(respBody).getString("DE38");				
						// obtengo el body para la devolucion
						bodyData2 = new String(Files.readString(Paths.get(TCFilesPath + "/TXT/TC.txt")));					
						System.out.println("este es el body 2 " + bodyData2);
						// inserto el valor del DE38 en el body para la devolucion				
						bodyData2 = bodyData2.replace("{{DE38}}", DE38);			
						System.out.println("este es el body para generar la devolucion :" + bodyData2);	
			 * */

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
			//oraResp.setUser(JsonPath.from(configEntidad).getString("DBUAT.user"));
			//oraResp.setPass(JsonPath.from(configEntidad).getString("DBUAT.pass"));
			//oraResp.setHost(JsonPath.from(configEntidad).getString("DBUAT.host"));
			//oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_uat"));


			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");

			//CAMBIO DE ambiente OPI 
			
			//String ambiente="uat";
			opiCmd.cambioAmbiente(report, configEntidad, opi, entidad, ambiente);
			Thread.sleep(5000);
			opiCmd.cambioKeycloak(report, configEntidad, opi, entidad, ambiente);
			Thread.sleep(5000);
			opiCmd.stopOpiCmd(report, configEntidad, opi);
			Thread.sleep(5000);
			opiCmd.startOpiCmd(report, configEntidad, opi);
			Thread.sleep(5000);
			

			System.out.println("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### FINALIZA EJECUCION DE PRECONDICIONES #####");

			//Se valida el funcionamiento de OPI
			//if (!opiCmd.getOpiStatusTotalCoinUAT(configEntidad)) {
			if (!opiCmd.getOpiStatus(configEntidad, opi)) {	
				opiCmd.stopOpiCmd(report, configEntidad, opi);
				Thread.sleep(1000);
				opiCmd.startOpiCmd2(report, configEntidad, opi);
				Thread.sleep(5000);	
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi), equalTo(true));
				
			}

			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");

			//Enviar a OPI el archivo del tc
			//opiCmd.sshSendCmdCreateXmlTotalCoinUAT(report, "xmlBaseAux.xml", configEntidad, opi, xmlFile);
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, opi, xmlFile);
			String idAutorizacionEmisorJosnString="";

			//	opiCmd.stopOpiCmd(report, configEntidad, opi);
			//	Thread.sleep(5000);
			//	opiCmd.startOpiCmd2(report, configEntidad, opi);
			//	Thread.sleep(5000);
				//opiCmd.getOpiStatus(configEntidad);
				//Thread.sleep(5000);
				
				for (int i = 0; i < 10; i++) {			
				//res = opiCmd.executeXmlTotalCoinUAT(report, "xmlBaseAux.xml", configEntidad, opi);
				res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, opi);
				Thread.sleep(5000);
				System.out.println("ejecucion numero : " + i);
							
				/*if(!res.equals("57")) {
					oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
							+ "where ID_TARJETA= 26 OR ID_TARJETA = 1",  configEntidad);
					ppCondi.preCondicionBD(report, rtaQuery);
				}else {
					continue;
				}*/
				
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91") && !res.equals("57")) {
					
					
				if (!idAutorizacionEmisorJosnString.equals("0")) {
						//opiCmd.stopOpiCmd(report, configEntidad, opi);
						//Thread.sleep(1000);
						//opiCmd.startOpiCmd2(report, configEntidad, opi);
						Thread.sleep(8000);	
						//idAutorizacionEmisorJosnString=oraResp.oraOneQuery(report, "SELECT ID_AUTORIZACION FROM AUTORIZACION WHERE ID_AUTORIZACION = (SELECT MAX(ID_AUTORIZACION) from AUTORIZACION)", configEntidad);
						idAutorizacionEmisor =oraResp.oraOneQuery(report, "SELECT ID_AUTORIZACION FROM AUTORIZACION WHERE ID_AUTORIZACION = (SELECT MAX(ID_AUTORIZACION) from AUTORIZACION)", configEntidad);
						//idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, opi);
					System.out.println("***********ingrese al ifFFFFFF");
						}
					break;
				}			
				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
				}
            //System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
           // JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
           // System.out.println("SE PARSEA STRING A JSON");
           // idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacionEmisor").getAsString();
           // idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacion").getAsString();
            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);



			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);

			if (res.equals("00") || res.equals("55"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				//result = validacionGralTotalCoinUAT(oraResp, report, idAutorizacionEmisor, entidad, validaciones);
				result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones,  configEntidad);
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
	public boolean trxIpm(Datasources data,Reports report, DriverManager DM,int iteration, String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22, String DE2, String ambiente) {

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
		String idAutorizacionEmisor = "0000000";
		int rtaQuery;
		//String ID_CUENTA = "2";
		//String ID_CUENTA_EXT = "10015647";
		esquema = JsonPath.from(configEntidad).getString("ENTIDAD.esquema_db");
		String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCHUAT.url_gbatch");

		try {		
			System.out.println("ESTE ES EL AMBIENTE DE PRUEBA SETEADO: " + ambiente.toUpperCase());
			
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");
			
			
			System.out.println("ESTE ES EL SWITCH DESDE DONDE EJECUTAMOS: " + opi.toUpperCase());
			
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			//rtaQuery = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			//rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			///ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			//rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			//rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			
			//seteo disponible de cuenta
			//rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 1000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			//rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 100000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			//seteo disponible de cuenta
			//rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE_ADELANTO = 1000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			//rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE_ADELANTO = 100000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			//rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			//rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			
			//oraResp.oraDelete(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
				//	+ "where ID_TARJETA= 39",  configEntidad);
			//		+ "where ID_TARJETA= 39 OR ID_TARJETA = 1 OR ID_TARJETA = 26",  configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			
			//borrar debitos automaticos para que no fallen los siguientes casos de prueba
			//oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
				//+ "where ID_TARJETA= 26 OR ID_TARJETA = 1",  configEntidad);
			
			//oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
				//	+ "where ID_TARJETA=26",  configEntidad);

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
			xmlFile = xmlFile.replace("{{DE22}}", DE22);
			xmlFile = xmlFile.replace("{{DE2}}", DE2);
			/*
			 * // obtengo el valor del DE38 de la ejecucion  
						String DE38 = JsonPath.from(respBody).getString("DE38");				
						// obtengo el body para la devolucion
						bodyData2 = new String(Files.readString(Paths.get(TCFilesPath + "/TXT/TC.txt")));					
						System.out.println("este es el body 2 " + bodyData2);
						// inserto el valor del DE38 en el body para la devolucion				
						bodyData2 = bodyData2.replace("{{DE38}}", DE38);			
						System.out.println("este es el body para generar la devolucion :" + bodyData2);	
			 * */

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
			//oraResp.setUser(JsonPath.from(configEntidad).getString("DBUAT.user"));
			//oraResp.setPass(JsonPath.from(configEntidad).getString("DBUAT.pass"));
			//oraResp.setHost(JsonPath.from(configEntidad).getString("DBUAT.host"));
			//oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_uat"));


			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");

			//CAMBIO DE ambiente OPI 
			
			//String ambiente="uat";
			opiCmd.cambioAmbiente(report, configEntidad, opi, entidad, ambiente);
			Thread.sleep(5000);
			opiCmd.cambioKeycloak(report, configEntidad, opi, entidad, ambiente);
			Thread.sleep(5000);
			opiCmd.stopOpiCmd(report, configEntidad, opi);
			Thread.sleep(5000);
			opiCmd.startOpiCmd(report, configEntidad, opi);
			Thread.sleep(5000);
			

			System.out.println("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			
			//Se valida el funcionamiento de OPI
			//if (!opiCmd.getOpiStatusTotalCoinUAT(configEntidad)) {
			if (!opiCmd.getOpiStatus(configEntidad, opi)) {	
				opiCmd.stopOpiCmd(report, configEntidad, opi);
				Thread.sleep(1000);
				opiCmd.startOpiCmd2(report, configEntidad, opi);
				Thread.sleep(5000);	
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi), equalTo(true));
				
			}

			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");

			//Enviar a OPI el archivo del tc
			//opiCmd.sshSendCmdCreateXmlTotalCoinUAT(report, "xmlBaseAux.xml", configEntidad, opi, xmlFile);
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, opi, xmlFile);
			String idAutorizacionEmisorJosnString="";

			//	opiCmd.stopOpiCmd(report, configEntidad, opi);
			//	Thread.sleep(5000);
			//	opiCmd.startOpiCmd2(report, configEntidad, opi);
			//	Thread.sleep(5000);
				//opiCmd.getOpiStatus(configEntidad);
				//Thread.sleep(5000);
				
				for (int i = 0; i < 10; i++) {			
				//res = opiCmd.executeXmlTotalCoinUAT(report, "xmlBaseAux.xml", configEntidad, opi);
				res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, opi);
				Thread.sleep(5000);
				System.out.println("ejecucion numero : " + i);
							
				/*if(!res.equals("57")) {
					oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
							+ "where ID_TARJETA= 26 OR ID_TARJETA = 1",  configEntidad);
					ppCondi.preCondicionBD(report, rtaQuery);
				}else {
					continue;
				}*/
				
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91") && !res.equals("57")) {
					
					
				if (!idAutorizacionEmisorJosnString.equals("0")) {
						//opiCmd.stopOpiCmd(report, configEntidad, opi);
						//Thread.sleep(1000);
						//opiCmd.startOpiCmd2(report, configEntidad, opi);
						Thread.sleep(8000);	
						//idAutorizacionEmisorJosnString=oraResp.oraOneQuery(report, "SELECT ID_AUTORIZACION FROM AUTORIZACION WHERE ID_AUTORIZACION = (SELECT MAX(ID_AUTORIZACION) from AUTORIZACION)", configEntidad);
						idAutorizacionEmisor =oraResp.oraOneQuery(report, "SELECT ID_AUTORIZACION FROM AUTORIZACION WHERE ID_AUTORIZACION = (SELECT MAX(ID_AUTORIZACION) from AUTORIZACION)", configEntidad);
						//idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, opi);
					System.out.println("***********ingrese al ifFFFFFF");
						}
					break;
				}			
				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
				}
            //System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
           // JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
           // System.out.println("SE PARSEA STRING A JSON");
           // idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacionEmisor").getAsString();
           // idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacion").getAsString();
            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);

			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);

		/*	if (res.equals("00") || res.equals("55"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				//result = validacionGralTotalCoinUAT(oraResp, report, idAutorizacionEmisor, entidad, validaciones);
				result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones,  configEntidad);
			} else {
				report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "00");
				System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "00" + "\r\n");
				result = false;
			}
*/
			
 

			System.out.println("##### FIN DE EJECUCION OPI #####");
			report.AddLine("##### FIN DE EJECUCION OPI #####");
			/***************EJECUCION IPM**********************************************/
			// en esta linea tenemos que hacer el update del ipm de credito
			int rtaPrePostCondiciones = 0;
			String ID_IPM = "11619";
			//usar el ipm generico 11625
			// Ejecutar la consulta SQL
          /*  String sqlQuery = "SELECT COD_AUTO_ENTIDAD, COMERCIO_DESCRIPCION, MODO_INGRESO, A.ID_AUTORIZACION_EMISOR, "
                    + "A.ID_COD_MOVIMIENTO, A.ID_ESTADO, A.ID_CUENTA,A.FECHA_AUTORIZACION, A.IMPORTE, a.nro_tarjeta "
                    + "FROM AUTORIZACION A "
                    + "WHERE ID_AUTORIZACION IN (12117)";*/
            
            		String COD_AUTO_ENTIDAD = oraResp.oraOneQuery(report, "SELECT COD_AUTO_ENTIDAD FROM AUTORIZACION WHERE ID_AUTORIZACION = " + idAutorizacionEmisor, configEntidad);
			
			//rtaPrePostCondiciones = 
					oraResp.oraUpdate(report, "UPDATE IPM\r\n"
					+ "SET CODIGO_APROBACION = " + COD_AUTO_ENTIDAD + ", IMPORTE_OPERACION = '000000100000', COD_MONEDA_OPERACION = '032', \r\n"
					+ "IMPORTE_RECONCILIACION = '000000100000', COD_MONEDA_RECONCILIACION = '840', IMPORTE_EMISOR = '000000100000', COD_MONEDA_EMISOR = '032',\r\n"
					+ "FECHA_HORA_LOCAL = '220304100001', CICLO_VIDA_TRANSACCION = 'MCC0000003330221' ,\r\n"
					+ "PROCESADO = 0, NRO_TARJETA = 'DCCA2764CC849EB15DFC5325C170A789', FECHA_VTO_TARJETA = '2902',\r\n"
					+ "COD_COMERCIO = '687555537877464', DESCRIPCION_COMERCIO = 'NETFLIX.COMG           AMS           ARG',\r\n"
					+ "ID_CONSUMO = NULL, OBSERVACION = NULL, ID_AUTORIZACION = null, CRITERIO_MAPEO = null, COD_PROCESAMIENTO = '000000',\r\n"
					+ "AUTORIZACION_PROCESADA = 0,  PRESENTACION_PARCIAL = 0, MCC = '4899', CONDICION_PUNTO_SERVICIO = '000040S09000'\r\n"
					+ "WHERE ID_IPM IN  " + ID_IPM,configEntidad);
			
			/**********************EJECUCION PRESENTACION********************************/

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
			tc.pagina1Presentacion(data, report, DM, iteration, name, repo, configEntidad);

			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);

			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);
						
			// Tiempo necesario para que se ejecute el proceso y se creen los registros en la BDD
			Thread.sleep(30000);
			
			/*************obtengo el id_consumo*********************************/
			String ID_CONSUMO=oraResp.oraOneQuery(report,"SELECT ID_CONSUMO FROM IPM WHERE ID_IPM = " + ID_IPM, configEntidad);
			String CRITERIO_MAPEO=oraResp.oraOneQuery(report,"SELECT CRITERIO_MAPEO FROM IPM WHERE ID_IPM = " + ID_IPM, configEntidad);
			// String sqlQueryV = "SELECT ID_CONSUMO, CRITERIO_MAPEO FROM IPM WHERE ID_IPM = ('11573')";
	        // ResultSet resultSetV = stmt.executeQuery(sqlQueryV);
	         // Procesar los resultados
	      //   while (resultSetV.next()) {
	             // Obtener los valores de cada columna por nombre
	            // String ID_CONSUMO = resultSetV.getString("ID_CONSUMO");
	            // String CRITERIO_MAPEO = resultSetV.getString("CRITERIO_MAPEO");
	           
	             // Imprimir las variables con el nombre de cada campo y su valor
	             System.out.println("ID_CONSUMO: " + ID_CONSUMO);
	             System.out.println("CRITERIO_MAPEO: " + CRITERIO_MAPEO);
	             System.out.println("SE VERIFICA IMPACTO DE DATOS DE : " + ID_IPM);
	             Thread.sleep(30000);
			
			/***********************EJECUCION T2001**************************************/
			
			
			
			//driver.get(URL_GBATCH);
			
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			// Login a la pagina principal de Global Batch
			//report.AddLine("Acceso a la pagina de Global Batch");
			//System.out.println("Acceso a la pagina de Global Batch\r\n");
			//acceso.loginUsernameBatch1(DM, repo);
			// Hacemos click en procesos
			nav.procesos(data, report, DM, iteration, name, repo);
			
			// Primera pagina del test
			tc.pagina1t2001(data, report, DM, iteration, name, repo, configEntidad);

			// Segunda pagina
			tc.pagina2Fecha(data, report, DM, iteration, name, repo);

			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);
			
			Thread.sleep(18000);
			/********************************************************************
			 * REALIZO UNA VERIFICACION
			 *****************************************************************/
		//	Connection conn;
			
			//CONFIGURACIÓN DATABASE
		//	oraResp.setUser(JsonPath.from(configEntidad).get("DBLEMONQA.user"));
		//	oraResp.setPass(JsonPath.from(configEntidad).get("DBLEMONQA.pass"));
		//	oraResp.setEntidad(JsonPath.from(configEntidad).get("ENTIDAD.id_entidad_peru_visa_qa"));
		//	oraResp.setHost(JsonPath.from(configEntidad).get("DBLEMONQA.host"));
		//	String user = JsonPath.from(configEntidad).get("DBLEMONQA.user");
		//	String pass = JsonPath.from(configEntidad).get("DBLEMONQA.pass");
		//	String host = JsonPath.from(configEntidad).get("DBLEMONQA.host");
		//	String SID = JsonPath.from(configEntidad).get("DBLEMONQA.SID");
		//	String strCon = "jdbc:oracle:thin:@" + host + "/" + SID;
		//	String user = JsonPath.from(configEntidad).get("DB.user");
			//String pass = JsonPath.from(configEntidad).get("DB.pass");
		//	String host = JsonPath.from(configEntidad).get("DB.host");
			//String SID = JsonPath.from(configEntidad).get("DB.SID");
			
			//String strCon = "jdbc:oracle:thin:@" + host + "/" + SID;
			
			//Connection conn;
			//conn = DriverManager.getConnection(strCon,user,pass);
		//	conn = java.sql.DriverManager.getConnection(strCon,user,pass);
		//	Statement stmt=conn.createStatement();
		//	stmt=conn.createStatement();
			// Ejecutar la consulta SQL
        //    String sqlQueryV = "SELECT ID_CONSUMO, ID_AUTORIZACION FROM CTF_VISA WHERE ID_CTF = ('11')";
        // ResultSet resultSetV = stmt.executeQuery(sqlQueryV);
        // ResultSet resultSet = stmt.executeQuery(sqlQuery);
         // Procesar los resultados
        // while (resultSetV.next()) {
             // Obtener los valores de cada columna por nombre
         //    String ID_CONSUMO = resultSetV.getString("ID_CONSUMO");
         //    String ID_AUTORIZACION = resultSetV.getString("ID_AUTORIZACION");
         //    String ID_CTF = "11";
             // Imprimir las variables con el nombre de cada campo y su valor
          //   System.out.println("ID_CONSUMO: " + ID_CONSUMO);
          //   System.out.println("CRITERIO_MAPEO: " + ID_AUTORIZACION);
          //   System.out.println("SE VERIFICA IMPACTO DE DATOS DE : " + ID_CTF);
      //   }
			
         //resultSet.close();
      //   stmt.close();
			
			
			
			//obtengo el archivo del remoto
			//String filePath = "\\GPWF-AQ-QA-01\\E:\\app\\GlobalBatch\\CTF_Saliente\\TV111_700_2024319_105846_0.CTF";
			//\\GPWF-AQ-QA-01\GlobalBatch\CTF_Entrante_Emisor     \\GPWF-AQ-QA-01\GlobalBatch\CTF_Entrante_Emisor  C:\Users\dpaterno\Desktop
			//String filePath = System.getProperty("user.home") + "\\Desktop\\D1T.txt";   \\GPWF-AQ-QA-01\GlobalBatch\Automation\IPM_Entrante
			// String filePath = System.getProperty("user.home") + "\\Desktop\\nuevo.txt";
			
			// Crear un objeto Robot para manejar el cuadro de diálogo de Windows
	      //  Robot robot = new Robot();
	        
	     //   robot.keyPress(KeyEvent.VK_WINDOWS);
	     //   robot.keyPress(KeyEvent.VK_R);
	    //    
	     // Liberar las teclas presionadas
	     //   robot.keyRelease(KeyEvent.VK_R);
	    //    robot.keyRelease(KeyEvent.VK_WINDOWS);
	        
	     // Esperar un breve momento para que aparezca el cuadro de diálogo
	      //  Thread.sleep(1000);
	        
	      //  String urlOrigen= "10.77.2.16";
	       // String destino= "C:\\Users\\dpaterno\\eclipse-workspace\\QA-Automation-EM\\TC_Datos";
	        //String nombre="TT112T0.2021-08-06-06-48-11_000.ipm.txt";
	       // String nombre="1922_prueba7.TXT.txt";
	        //Path destinoPath = Paths.get(destino, nombre);
	       
	        try {
	            // Paso 1: Crear la conexión
	            URL url = new URL("http://10.77.2.16"); // Especifica el protocolo http://

	            // Paso 2: Verificar la conexión
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
	                // Paso 3: Crear el InputStream
	                InputStream inputStream = connection.getInputStream();
	                System.out.println("La conexión fue EXITOSA!! NO NO FUE rechazada.");
	               //                      \\GPWF-AQ-QA-01\GlobalBatch\Automation\IPM_Entrante
	                String filePath = "\\\\GPWF-AQ-UAT-01\\GlobalBatch\\Automation\\T2001 - Transacciones Diarias"; 
	               // String filePath = "\\GPWF-AQ-QA-01\\Applications\\QA\\GlobalBatch\\2023.06.06.1-Dev"; \\GPWF-AQ-QA-01\GlobalBatch\Automation\IPM_Entrante
	               // String filePath = "\\\\GPWF-AQ-QA-01\\GlobalBatch\\Automation\\T2001 - Transacciones Diarias"; \\GPWF-AQ-QA-01\GlobalBatch\Automation\IPM_Entrante
	                                  //\\\\GPWF-AQ-QA-01\\GlobalBatch\\Automation\\T2001 - Transacciones Diarias
	               // File directorio = new File(filePath);
	             // System.out.println();
	               // if (archivo.exists()) {
	                List<String> nombresArchivos = obtenerNombresArchivos(filePath);
	                if (!nombresArchivos.isEmpty()) {
	                    System.out.println("Archivos en el directorio:");
	                    for (String nombreArchivo : nombresArchivos) {
	                        System.out.println(nombreArchivo);
	                    }
	                } else {
	                    System.out.println("El directorio está vacío o no es válido.");
	                }
	                
	                if (!filePath.isEmpty()) {
	    	        	System.out.println("aca esta el archivo");
	    	        }else {
	    	        	System.out.println("no hay archivo");
	    	        }
	             //   List<String> nombresArchivos = new ArrayList<>();
	                File directorio = new File(filePath);
	                
	                String rutaDirectorioDestino = "C:\\Users\\dpaterno\\eclipse-workspace\\QA-Automation-EM\\TC_Datos";
	                
	                copiarArchivosAlProyecto(filePath, rutaDirectorioDestino);
	                
	                //String rutaDirectorio = "ruta_del_directorio";
	                String rutaArchivoDestino = "./TC_Datos/ipm.txt";

	                leerUltimoArchivoYCopiar(filePath, rutaArchivoDestino);
                    
	                
	                if (directorio.isDirectory()) {
	                    String[] archivos = directorio.list();
	                    return archivos != null && archivos.length == 0;
	                   
	                    
	                   // String destinationDirectory = "/QA-Automation-EM/TC_Datos";
	                    //String rutaDirectorioOrigen = "\\\\GPWF-AQ-QA-01\\GlobalBatch\\Automation\\T2001 - Transacciones Diarias";
	                   
	                   
	                } else {
	                    System.out.println("La ruta especificada no es un directorio.");
	                    return false;
	                }
	                // Hacer algo con el InputStream, por ejemplo leer datos
	                // ...
	            } else {
	                System.out.println("La conexión fue rechazada.");
	            }

	            // Cerrar la conexión cuando hayamos terminado
	            connection.disconnect();
	        } catch (MalformedURLException e) {
	            System.err.println("URL mal formada: " + e.getMessage());
	        } catch (IOException e) {
	            System.err.println("Error de entrada/salida: " + e.getMessage());
	        }
	    
	      
	        
	        
	        
	       /* String origen = "\\GPWF-AQ-QA-01\\GlobalBatch\\Automation\\IPM_Entrante";
	        String destino= "C:\\Users\\dpaterno\\eclipse-workspace\\QA-Automation-EM\\TC_Datos";
	        String nombre="TT112T0.2021-08-06-06-48-11_000.ipm.txt";
	        
	        File archivo = new File(origen, nombre);
	        
	        if (archivo.exists()) {
	        	System.out.println("aca esta el archivo");
	        }else {
	        	System.out.println("no hay archivo");
	        }*/
	        
	     // Escribir el texto "\\GPWF-AQ-QA-01\GlobalBatch"  \\GPWF-AQ-QA-01\GlobalBatch\Automation\T2001 - Transacciones Diarias
	      //  String text = "\\GPWF-AQ-QA-01\\GlobalBatch";
	       // String text = "\\GPWF-AQ-QA-01\\GlobalBatch\\Automation\\T2001 - Transacciones Diarias\\T2001D_08_105_20240202_1506.txt";
	       /* for (char c : text.toCharArray()) {
	            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
	            if (KeyEvent.CHAR_UNDEFINED == keyCode) {
	                throw new RuntimeException("No se puede escribir el carácter: " + c);
	            }
	            System.out.println(" entro en el else "+
	            c );*/
	            //robot.keyPress(keyCode);
	          //  robot.keyRelease(keyCode);
	            // Esperar un breve momento entre cada carácter para evitar problemas de entrada rápida
	           // Thread.sleep(50);
	            //Path path = Paths.get(filePath);

		       
	        //}
	          
	        
	     // Esperar un tiempo antes de presionar "Enter"
	       // Thread.sleep(1000);
	        // Presionar la tecla "Enter"
	       // robot.keyPress(KeyEvent.VK_ENTER);
	     //   Thread.sleep(1000);
	        // Presionar la tecla "Enter"
	     ///   robot.keyPress(KeyEvent.VK_ENTER);
	     //   Thread.sleep(500);
	        
	     //   robot.keyRelease(KeyEvent.VK_ENTER);
	       
	     //   robot.keyRelease(KeyEvent.VK_ENTER);
						
			// Leer el archivo desde el escritorio
	       // String filePath = "\\GPWF-AQ-QA-01\\GlobalBatch\\Automation\\T2001 - Transacciones Diarias\\T2001D_08_105_20240202_1506.txt";
	       // String fileName = "T2001D_08_105_20240202_1506.txt"; \\GPWF-AQ-QA-01\\GlobalBatch\\Automation\\T2001 - Transacciones Diarias\\T2001D_08_105_20240202_1506.txt
	       /* String filePath = "./TT112T0.2021-08-06-06-48-11_000.ipm.txt";
	        try {
	            File file = new File(filePath);
	            BufferedReader br = new BufferedReader(new FileReader(file));
	            String line;
	            while ((line = br.readLine()) != null) {
	                System.out.println(line); // Aquí puedes hacer lo que necesites con cada línea del archivo
	            }
	            br.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	       
	       // Path ruta = Paths.get(filePath);
	        
	        // Abrir una conexión a la URL
            URL url = new URL("10.77.2.16");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
	        //String filePath = ("\\GPWF-AQ-QA-01\\GlobalBatch\\Automation\\T2001 - Transacciones Diarias\\"
	        		//+ "T2001D_08_105_20240202_1506.txt");
	       // String fileName = "TT112T0.2021-08-06-06-48-11_000.ipm.txt"
	        		//+ "";
			//Path path = Paths.get(filePath);
	       
		
			
			
	       /* 
	        if (Files.exists(path)) {
	            // El archivo existe, puedes proceder a realizar las operaciones que desees con él

	        	System.out.println("Archivo copiado exitosamente.");
	            // Por ejemplo, puedes copiar el archivo a otro directorio
	            String destinationDirectory = "/QA-Automation-EM/TC_Datos";
	            try {
	                Files.copy(path, Paths.get(destinationDirectory + File.separator + fileName));
	                System.out.println("Archivo copiado exitosamente.");
	            } catch (IOException e) {
	                System.out.println("Error al copiar el archivo: " + e.getMessage());
	            }
	      }
	        try {
	            File file = new File(filePath);
	            BufferedReader br = new BufferedReader(new FileReader(file));
	            String line;
	            while ((line = br.readLine()) != null) {
	                System.out.println(line); // Aquí puedes hacer lo que necesites con cada línea del archivo
	            }
	            br.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	       
	        
	        /*
	         *  // También puedes leer el contenido del archivo
            try {
                byte[] fileContent = Files.readAllBytes(path);
                System.out.println("Contenido del archivo:");
                System.out.println(new String(fileContent)); // Esto imprimirá el contenido del archivo en la consola
            } catch (IOException e) {
                System.out.println("Error al leer el archivo: " + e.getMessage());
            }

            // Y muchas otras operaciones que desees realizar con el archivo
        
	        
	        try {
	            File file = new File(filePath);
	            BufferedReader br = new BufferedReader(new FileReader(file));
	            String line;
	            while ((line = br.readLine()) != null) {
	                System.out.println(line); // Aquí puedes hacer lo que necesites con cada línea del archivo
	            }
	            br.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }*/
			
			
			
			
 

						
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
	public boolean altaCuentaEmbozado(Datasources data,Reports report, DriverManager DM,int iteration, String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22, String DE2, String ambiente) {

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
		String idAutorizacionEmisor = "0000000";
		int rtaQuery;
		//String ID_CUENTA = "2";
		//String ID_CUENTA_EXT = "10015647";
		esquema = JsonPath.from(configEntidad).getString("ENTIDAD.esquema_db");
		String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCHUAT.url_gbatch");

		try {		
			/*System.out.println("ESTE ES EL AMBIENTE DE PRUEBA SETEADO: " + ambiente.toUpperCase());
			
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");
			
			
			System.out.println("ESTE ES EL SWITCH DESDE DONDE EJECUTAMOS: " + opi.toUpperCase());
			
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");
			*/
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			//rtaQuery = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			//rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			///ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			//rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			//rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			
			//seteo disponible de cuenta
			//rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 1000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			//rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 100000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			//seteo disponible de cuenta
			//rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE_ADELANTO = 1000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			//rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE_ADELANTO = 100000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			//rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			//rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			
			//oraResp.oraDelete(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
				//	+ "where ID_TARJETA= 39",  configEntidad);
			//		+ "where ID_TARJETA= 39 OR ID_TARJETA = 1 OR ID_TARJETA = 26",  configEntidad);
			//ppCondi.preCondicionBD(report, rtaQuery);
			
			//borrar debitos automaticos para que no fallen los siguientes casos de prueba
			//oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
				//+ "where ID_TARJETA= 26 OR ID_TARJETA = 1",  configEntidad);
			
			//oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
				//	+ "where ID_TARJETA=26",  configEntidad);

			//Preparación del archivo JSON del TC
			//System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			//report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			//System.out.println(TCFilesPath);

			//String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));

			//Preparación del archivo XML del TC
			//System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			//report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			//System.out.println(TCFilesPath);
			//String xmlFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			//xmlFile = xmlFile.replace("{{DE22}}", DE22);
			//xmlFile = xmlFile.replace("{{DE2}}", DE2);
			/*
			 * // obtengo el valor del DE38 de la ejecucion  
						String DE38 = JsonPath.from(respBody).getString("DE38");				
						// obtengo el body para la devolucion
						bodyData2 = new String(Files.readString(Paths.get(TCFilesPath + "/TXT/TC.txt")));					
						System.out.println("este es el body 2 " + bodyData2);
						// inserto el valor del DE38 en el body para la devolucion				
						bodyData2 = bodyData2.replace("{{DE38}}", DE38);			
						System.out.println("este es el body para generar la devolucion :" + bodyData2);	
			 * */

			//System.out.println("JSON FILE:");
			//System.out.println(jsonFile);
			//System.out.println("XMLFile: ");
			//System.out.println(xmlFile);

			//JsonParser parser = new JsonParser();

			//JsonObject jsonObject = parser.parse(jsonFile).getAsJsonObject();
			//System.out.println("JSON Object: ");
			//System.out.println(jsonObject);
			//JsonArray validaciones = jsonObject.getAsJsonArray("VALIDACIONESDB");

			//System.out.println("VALIDACIONES: ");
			//System.out.println(validaciones);

			//SET dbWorker
			//oraResp.setUser(JsonPath.from(configEntidad).getString("DBUAT.user"));
			//oraResp.setPass(JsonPath.from(configEntidad).getString("DBUAT.pass"));
			//oraResp.setHost(JsonPath.from(configEntidad).getString("DBUAT.host"));
			//oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_uat"));


			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");

			//CAMBIO DE ambiente OPI 
			
			//String ambiente="uat";
			//opiCmd.cambioAmbiente(report, configEntidad, opi, entidad, ambiente);
			//Thread.sleep(5000);
			//opiCmd.cambioKeycloak(report, configEntidad, opi, entidad, ambiente);
			//Thread.sleep(5000);
			//opiCmd.stopOpiCmd(report, configEntidad, opi);
			//Thread.sleep(5000);
		//	opiCmd.startOpiCmd(report, configEntidad, opi);
			//Thread.sleep(5000);
			

			System.out.println("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			
			//Se valida el funcionamiento de OPI
			//if (!opiCmd.getOpiStatusTotalCoinUAT(configEntidad)) {
			if (!opiCmd.getOpiStatus(configEntidad, opi)) {	
				opiCmd.stopOpiCmd(report, configEntidad, opi);
				Thread.sleep(1000);
				opiCmd.startOpiCmd2(report, configEntidad, opi);
				Thread.sleep(5000);	
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi), equalTo(true));
				
			}

			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");

			//Enviar a OPI el archivo del tc
			//opiCmd.sshSendCmdCreateXmlTotalCoinUAT(report, "xmlBaseAux.xml", configEntidad, opi, xmlFile);
			//opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, opi, xmlFile);
			String idAutorizacionEmisorJosnString="";

			//	opiCmd.stopOpiCmd(report, configEntidad, opi);
			//	Thread.sleep(5000);
			//	opiCmd.startOpiCmd2(report, configEntidad, opi);
			//	Thread.sleep(5000);
				//opiCmd.getOpiStatus(configEntidad);
				//Thread.sleep(5000);
				
				for (int i = 0; i < 10; i++) {			
				//res = opiCmd.executeXmlTotalCoinUAT(report, "xmlBaseAux.xml", configEntidad, opi);
				res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, opi);
				Thread.sleep(5000);
				System.out.println("ejecucion numero : " + i);
							
				/*if(!res.equals("57")) {
					oraResp.oraDeleteLemonQA(report, "DELETE FROM DEBITOS_AUTOMATICOS \r\n"
							+ "where ID_TARJETA= 26 OR ID_TARJETA = 1",  configEntidad);
					ppCondi.preCondicionBD(report, rtaQuery);
				}else {
					continue;
				}*/
				
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91") && !res.equals("57")) {
					
					
				if (!idAutorizacionEmisorJosnString.equals("0")) {
						//opiCmd.stopOpiCmd(report, configEntidad, opi);
						//Thread.sleep(1000);
						//opiCmd.startOpiCmd2(report, configEntidad, opi);
						Thread.sleep(8000);	
						//idAutorizacionEmisorJosnString=oraResp.oraOneQuery(report, "SELECT ID_AUTORIZACION FROM AUTORIZACION WHERE ID_AUTORIZACION = (SELECT MAX(ID_AUTORIZACION) from AUTORIZACION)", configEntidad);
						idAutorizacionEmisor =oraResp.oraOneQuery(report, "SELECT ID_AUTORIZACION FROM AUTORIZACION WHERE ID_AUTORIZACION = (SELECT MAX(ID_AUTORIZACION) from AUTORIZACION)", configEntidad);
						//idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, opi);
					System.out.println("***********ingrese al ifFFFFFF");
						}
					break;
				}			
				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
				}
            //System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
           // JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
           // System.out.println("SE PARSEA STRING A JSON");
           // idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacionEmisor").getAsString();
           // idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacion").getAsString();
            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);

			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);

		/*	if (res.equals("00") || res.equals("55"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				//result = validacionGralTotalCoinUAT(oraResp, report, idAutorizacionEmisor, entidad, validaciones);
				result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones,  configEntidad);
			} else {
				report.AddLineAssertionError("FAIL<br>DE39: " + res + " Se esperaba: " + "00");
				System.out.println("##[warning] : FAIL : \r\nDE39: " + res + " Se esperaba: " + "00" + "\r\n");
				result = false;
			}
*/
			
 

			System.out.println("##### FIN DE EJECUCION OPI #####");
			report.AddLine("##### FIN DE EJECUCION OPI #####");
			/***************EJECUCION IPM**********************************************/
			// en esta linea tenemos que hacer el update del ipm de credito
			int rtaPrePostCondiciones = 0;
			String ID_IPM = "11619";
			//usar el ipm generico 11625
			// Ejecutar la consulta SQL
          /*  String sqlQuery = "SELECT COD_AUTO_ENTIDAD, COMERCIO_DESCRIPCION, MODO_INGRESO, A.ID_AUTORIZACION_EMISOR, "
                    + "A.ID_COD_MOVIMIENTO, A.ID_ESTADO, A.ID_CUENTA,A.FECHA_AUTORIZACION, A.IMPORTE, a.nro_tarjeta "
                    + "FROM AUTORIZACION A "
                    + "WHERE ID_AUTORIZACION IN (12117)";*/
            
            		String COD_AUTO_ENTIDAD = oraResp.oraOneQuery(report, "SELECT COD_AUTO_ENTIDAD FROM AUTORIZACION WHERE ID_AUTORIZACION = " + idAutorizacionEmisor, configEntidad);
			
			//rtaPrePostCondiciones = 
					oraResp.oraUpdate(report, "UPDATE IPM\r\n"
					+ "SET CODIGO_APROBACION = " + COD_AUTO_ENTIDAD + ", IMPORTE_OPERACION = '000000100000', COD_MONEDA_OPERACION = '032', \r\n"
					+ "IMPORTE_RECONCILIACION = '000000100000', COD_MONEDA_RECONCILIACION = '840', IMPORTE_EMISOR = '000000100000', COD_MONEDA_EMISOR = '032',\r\n"
					+ "FECHA_HORA_LOCAL = '220304100001', CICLO_VIDA_TRANSACCION = 'MCC0000003330221' ,\r\n"
					+ "PROCESADO = 0, NRO_TARJETA = 'DCCA2764CC849EB15DFC5325C170A789', FECHA_VTO_TARJETA = '2902',\r\n"
					+ "COD_COMERCIO = '687555537877464', DESCRIPCION_COMERCIO = 'NETFLIX.COMG           AMS           ARG',\r\n"
					+ "ID_CONSUMO = NULL, OBSERVACION = NULL, ID_AUTORIZACION = null, CRITERIO_MAPEO = null, COD_PROCESAMIENTO = '000000',\r\n"
					+ "AUTORIZACION_PROCESADA = 0,  PRESENTACION_PARCIAL = 0, MCC = '4899', CONDICION_PUNTO_SERVICIO = '000040S09000'\r\n"
					+ "WHERE ID_IPM IN  " + ID_IPM,configEntidad);
			
			/**********************EJECUCION PRESENTACION********************************/

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
			tc.pagina1Presentacion(data, report, DM, iteration, name, repo, configEntidad);

			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);

			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);
						
			// Tiempo necesario para que se ejecute el proceso y se creen los registros en la BDD
			Thread.sleep(30000);
			
			/*************obtengo el id_consumo*********************************/
			String ID_CONSUMO=oraResp.oraOneQuery(report,"SELECT ID_CONSUMO FROM IPM WHERE ID_IPM = " + ID_IPM, configEntidad);
			String CRITERIO_MAPEO=oraResp.oraOneQuery(report,"SELECT CRITERIO_MAPEO FROM IPM WHERE ID_IPM = " + ID_IPM, configEntidad);
			// String sqlQueryV = "SELECT ID_CONSUMO, CRITERIO_MAPEO FROM IPM WHERE ID_IPM = ('11573')";
	        // ResultSet resultSetV = stmt.executeQuery(sqlQueryV);
	         // Procesar los resultados
	      //   while (resultSetV.next()) {
	             // Obtener los valores de cada columna por nombre
	            // String ID_CONSUMO = resultSetV.getString("ID_CONSUMO");
	            // String CRITERIO_MAPEO = resultSetV.getString("CRITERIO_MAPEO");
	           
	             // Imprimir las variables con el nombre de cada campo y su valor
	             System.out.println("ID_CONSUMO: " + ID_CONSUMO);
	             System.out.println("CRITERIO_MAPEO: " + CRITERIO_MAPEO);
	             System.out.println("SE VERIFICA IMPACTO DE DATOS DE : " + ID_IPM);
	             Thread.sleep(30000);
			
			/***********************EJECUCION T2001**************************************/
			
			
			
			//driver.get(URL_GBATCH);
			
			//acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			// Login a la pagina principal de Global Batch
			//report.AddLine("Acceso a la pagina de Global Batch");
			//System.out.println("Acceso a la pagina de Global Batch\r\n");
			//acceso.loginUsernameBatch1(DM, repo);
			// Hacemos click en procesos
			nav.procesos(data, report, DM, iteration, name, repo);
			
			// Primera pagina del test
			tc.pagina1t2001(data, report, DM, iteration, name, repo, configEntidad);

			// Segunda pagina
			tc.pagina2Fecha(data, report, DM, iteration, name, repo);

			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);
			
			Thread.sleep(18000);
			/********************************************************************
			 * REALIZO UNA VERIFICACION
			 *****************************************************************/
		//	Connection conn;
			
			//CONFIGURACIÓN DATABASE
		//	oraResp.setUser(JsonPath.from(configEntidad).get("DBLEMONQA.user"));
		//	oraResp.setPass(JsonPath.from(configEntidad).get("DBLEMONQA.pass"));
		//	oraResp.setEntidad(JsonPath.from(configEntidad).get("ENTIDAD.id_entidad_peru_visa_qa"));
		//	oraResp.setHost(JsonPath.from(configEntidad).get("DBLEMONQA.host"));
		//	String user = JsonPath.from(configEntidad).get("DBLEMONQA.user");
		//	String pass = JsonPath.from(configEntidad).get("DBLEMONQA.pass");
		//	String host = JsonPath.from(configEntidad).get("DBLEMONQA.host");
		//	String SID = JsonPath.from(configEntidad).get("DBLEMONQA.SID");
		//	String strCon = "jdbc:oracle:thin:@" + host + "/" + SID;
		//	String user = JsonPath.from(configEntidad).get("DB.user");
			//String pass = JsonPath.from(configEntidad).get("DB.pass");
		//	String host = JsonPath.from(configEntidad).get("DB.host");
			//String SID = JsonPath.from(configEntidad).get("DB.SID");
			
			//String strCon = "jdbc:oracle:thin:@" + host + "/" + SID;
			
			//Connection conn;
			//conn = DriverManager.getConnection(strCon,user,pass);
		//	conn = java.sql.DriverManager.getConnection(strCon,user,pass);
		//	Statement stmt=conn.createStatement();
		//	stmt=conn.createStatement();
			// Ejecutar la consulta SQL
        //    String sqlQueryV = "SELECT ID_CONSUMO, ID_AUTORIZACION FROM CTF_VISA WHERE ID_CTF = ('11')";
        // ResultSet resultSetV = stmt.executeQuery(sqlQueryV);
        // ResultSet resultSet = stmt.executeQuery(sqlQuery);
         // Procesar los resultados
        // while (resultSetV.next()) {
             // Obtener los valores de cada columna por nombre
         //    String ID_CONSUMO = resultSetV.getString("ID_CONSUMO");
         //    String ID_AUTORIZACION = resultSetV.getString("ID_AUTORIZACION");
         //    String ID_CTF = "11";
             // Imprimir las variables con el nombre de cada campo y su valor
          //   System.out.println("ID_CONSUMO: " + ID_CONSUMO);
          //   System.out.println("CRITERIO_MAPEO: " + ID_AUTORIZACION);
          //   System.out.println("SE VERIFICA IMPACTO DE DATOS DE : " + ID_CTF);
      //   }
			
         //resultSet.close();
      //   stmt.close();
			
			
			
			//obtengo el archivo del remoto
			//String filePath = "\\GPWF-AQ-QA-01\\E:\\app\\GlobalBatch\\CTF_Saliente\\TV111_700_2024319_105846_0.CTF";
			//\\GPWF-AQ-QA-01\GlobalBatch\CTF_Entrante_Emisor     \\GPWF-AQ-QA-01\GlobalBatch\CTF_Entrante_Emisor  C:\Users\dpaterno\Desktop
			//String filePath = System.getProperty("user.home") + "\\Desktop\\D1T.txt";   \\GPWF-AQ-QA-01\GlobalBatch\Automation\IPM_Entrante
			// String filePath = System.getProperty("user.home") + "\\Desktop\\nuevo.txt";
			
			// Crear un objeto Robot para manejar el cuadro de diálogo de Windows
	      //  Robot robot = new Robot();
	        
	     //   robot.keyPress(KeyEvent.VK_WINDOWS);
	     //   robot.keyPress(KeyEvent.VK_R);
	    //    
	     // Liberar las teclas presionadas
	     //   robot.keyRelease(KeyEvent.VK_R);
	    //    robot.keyRelease(KeyEvent.VK_WINDOWS);
	        
	     // Esperar un breve momento para que aparezca el cuadro de diálogo
	      //  Thread.sleep(1000);
	        
	      //  String urlOrigen= "10.77.2.16";
	       // String destino= "C:\\Users\\dpaterno\\eclipse-workspace\\QA-Automation-EM\\TC_Datos";
	        //String nombre="TT112T0.2021-08-06-06-48-11_000.ipm.txt";
	       // String nombre="1922_prueba7.TXT.txt";
	        //Path destinoPath = Paths.get(destino, nombre);
	       
	        try {
	            // Paso 1: Crear la conexión
	            URL url = new URL("http://10.77.2.16"); // Especifica el protocolo http://

	            // Paso 2: Verificar la conexión
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
	                // Paso 3: Crear el InputStream
	                InputStream inputStream = connection.getInputStream();
	                System.out.println("La conexión fue EXITOSA!! NO NO FUE rechazada.");
	               //                      \\GPWF-AQ-QA-01\GlobalBatch\Automation\IPM_Entrante
	                String filePath = "\\\\GPWF-AQ-UAT-01\\GlobalBatch\\Automation\\T2001 - Transacciones Diarias"; 
	               // String filePath = "\\GPWF-AQ-QA-01\\Applications\\QA\\GlobalBatch\\2023.06.06.1-Dev"; \\GPWF-AQ-QA-01\GlobalBatch\Automation\IPM_Entrante
	               // String filePath = "\\\\GPWF-AQ-QA-01\\GlobalBatch\\Automation\\T2001 - Transacciones Diarias"; \\GPWF-AQ-QA-01\GlobalBatch\Automation\IPM_Entrante
	                                  //\\\\GPWF-AQ-QA-01\\GlobalBatch\\Automation\\T2001 - Transacciones Diarias
	               // File directorio = new File(filePath);
	             // System.out.println();
	               // if (archivo.exists()) {
	                List<String> nombresArchivos = obtenerNombresArchivos(filePath);
	                if (!nombresArchivos.isEmpty()) {
	                    System.out.println("Archivos en el directorio:");
	                    for (String nombreArchivo : nombresArchivos) {
	                        System.out.println(nombreArchivo);
	                    }
	                } else {
	                    System.out.println("El directorio está vacío o no es válido.");
	                }
	                
	                if (!filePath.isEmpty()) {
	    	        	System.out.println("aca esta el archivo");
	    	        }else {
	    	        	System.out.println("no hay archivo");
	    	        }
	             //   List<String> nombresArchivos = new ArrayList<>();
	                File directorio = new File(filePath);
	                
	                String rutaDirectorioDestino = "C:\\Users\\dpaterno\\eclipse-workspace\\QA-Automation-EM\\TC_Datos";
	                
	                copiarArchivosAlProyecto(filePath, rutaDirectorioDestino);
	                
	                //String rutaDirectorio = "ruta_del_directorio";
	                String rutaArchivoDestino = "./TC_Datos/ipm.txt";

	                leerUltimoArchivoYCopiar(filePath, rutaArchivoDestino);
                    
	                
	                if (directorio.isDirectory()) {
	                    String[] archivos = directorio.list();
	                    return archivos != null && archivos.length == 0;
	                   
	                    
	                   // String destinationDirectory = "/QA-Automation-EM/TC_Datos";
	                    //String rutaDirectorioOrigen = "\\\\GPWF-AQ-QA-01\\GlobalBatch\\Automation\\T2001 - Transacciones Diarias";
	                   
	                   
	                } else {
	                    System.out.println("La ruta especificada no es un directorio.");
	                    return false;
	                }
	                // Hacer algo con el InputStream, por ejemplo leer datos
	                // ...
	            } else {
	                System.out.println("La conexión fue rechazada.");
	            }

	            // Cerrar la conexión cuando hayamos terminado
	            connection.disconnect();
	        } catch (MalformedURLException e) {
	            System.err.println("URL mal formada: " + e.getMessage());
	        } catch (IOException e) {
	            System.err.println("Error de entrada/salida: " + e.getMessage());
	        }
	    
	      
	        
	        
	        
	       /* String origen = "\\GPWF-AQ-QA-01\\GlobalBatch\\Automation\\IPM_Entrante";
	        String destino= "C:\\Users\\dpaterno\\eclipse-workspace\\QA-Automation-EM\\TC_Datos";
	        String nombre="TT112T0.2021-08-06-06-48-11_000.ipm.txt";
	        
	        File archivo = new File(origen, nombre);
	        
	        if (archivo.exists()) {
	        	System.out.println("aca esta el archivo");
	        }else {
	        	System.out.println("no hay archivo");
	        }*/
	        
	     // Escribir el texto "\\GPWF-AQ-QA-01\GlobalBatch"  \\GPWF-AQ-QA-01\GlobalBatch\Automation\T2001 - Transacciones Diarias
	      //  String text = "\\GPWF-AQ-QA-01\\GlobalBatch";
	       // String text = "\\GPWF-AQ-QA-01\\GlobalBatch\\Automation\\T2001 - Transacciones Diarias\\T2001D_08_105_20240202_1506.txt";
	       /* for (char c : text.toCharArray()) {
	            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
	            if (KeyEvent.CHAR_UNDEFINED == keyCode) {
	                throw new RuntimeException("No se puede escribir el carácter: " + c);
	            }
	            System.out.println(" entro en el else "+
	            c );*/
	            //robot.keyPress(keyCode);
	          //  robot.keyRelease(keyCode);
	            // Esperar un breve momento entre cada carácter para evitar problemas de entrada rápida
	           // Thread.sleep(50);
	            //Path path = Paths.get(filePath);

		       
	        //}
	          
	        
	     // Esperar un tiempo antes de presionar "Enter"
	       // Thread.sleep(1000);
	        // Presionar la tecla "Enter"
	       // robot.keyPress(KeyEvent.VK_ENTER);
	     //   Thread.sleep(1000);
	        // Presionar la tecla "Enter"
	     ///   robot.keyPress(KeyEvent.VK_ENTER);
	     //   Thread.sleep(500);
	        
	     //   robot.keyRelease(KeyEvent.VK_ENTER);
	       
	     //   robot.keyRelease(KeyEvent.VK_ENTER);
						
			// Leer el archivo desde el escritorio
	       // String filePath = "\\GPWF-AQ-QA-01\\GlobalBatch\\Automation\\T2001 - Transacciones Diarias\\T2001D_08_105_20240202_1506.txt";
	       // String fileName = "T2001D_08_105_20240202_1506.txt"; \\GPWF-AQ-QA-01\\GlobalBatch\\Automation\\T2001 - Transacciones Diarias\\T2001D_08_105_20240202_1506.txt
	       /* String filePath = "./TT112T0.2021-08-06-06-48-11_000.ipm.txt";
	        try {
	            File file = new File(filePath);
	            BufferedReader br = new BufferedReader(new FileReader(file));
	            String line;
	            while ((line = br.readLine()) != null) {
	                System.out.println(line); // Aquí puedes hacer lo que necesites con cada línea del archivo
	            }
	            br.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	       
	       // Path ruta = Paths.get(filePath);
	        
	        // Abrir una conexión a la URL
            URL url = new URL("10.77.2.16");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
	        //String filePath = ("\\GPWF-AQ-QA-01\\GlobalBatch\\Automation\\T2001 - Transacciones Diarias\\"
	        		//+ "T2001D_08_105_20240202_1506.txt");
	       // String fileName = "TT112T0.2021-08-06-06-48-11_000.ipm.txt"
	        		//+ "";
			//Path path = Paths.get(filePath);
	       
		
			
			
	       /* 
	        if (Files.exists(path)) {
	            // El archivo existe, puedes proceder a realizar las operaciones que desees con él

	        	System.out.println("Archivo copiado exitosamente.");
	            // Por ejemplo, puedes copiar el archivo a otro directorio
	            String destinationDirectory = "/QA-Automation-EM/TC_Datos";
	            try {
	                Files.copy(path, Paths.get(destinationDirectory + File.separator + fileName));
	                System.out.println("Archivo copiado exitosamente.");
	            } catch (IOException e) {
	                System.out.println("Error al copiar el archivo: " + e.getMessage());
	            }
	      }
	        try {
	            File file = new File(filePath);
	            BufferedReader br = new BufferedReader(new FileReader(file));
	            String line;
	            while ((line = br.readLine()) != null) {
	                System.out.println(line); // Aquí puedes hacer lo que necesites con cada línea del archivo
	            }
	            br.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	       
	        
	        /*
	         *  // También puedes leer el contenido del archivo
            try {
                byte[] fileContent = Files.readAllBytes(path);
                System.out.println("Contenido del archivo:");
                System.out.println(new String(fileContent)); // Esto imprimirá el contenido del archivo en la consola
            } catch (IOException e) {
                System.out.println("Error al leer el archivo: " + e.getMessage());
            }

            // Y muchas otras operaciones que desees realizar con el archivo
        
	        
	        try {
	            File file = new File(filePath);
	            BufferedReader br = new BufferedReader(new FileReader(file));
	            String line;
	            while ((line = br.readLine()) != null) {
	                System.out.println(line); // Aquí puedes hacer lo que necesites con cada línea del archivo
	            }
	            br.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }*/
			
			
			
			
 

						
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
	/*************************compra OPI "opiVisaAutoQA multiambiente"***************************************************/

	public  List<String> obtenerNombresArchivos(String rutaDirectorio) {
        List<String> nombresArchivos = new ArrayList<>();
        File directorio = new File(rutaDirectorio);

        // Verificar si el directorio existe y es un directorio válido
        if (!directorio.exists() || !directorio.isDirectory()) {
            System.out.println("El directorio especificado no existe o no es un directorio válido.");
            return nombresArchivos;
        }

        // Obtener los nombres de los archivos dentro del directorio
        String[] archivos = directorio.list();
        if (archivos != null) {
            for (String nombreArchivo : archivos) {
                nombresArchivos.add(nombreArchivo);
            }
        }

        return nombresArchivos;
    }
	
	@Step("Se accede al server y se copia los datos generados en el archivo T2001")
	public static void copiarArchivosAlProyecto(String rutaDirectorioOrigen, String rutaDirectorioDestino) {
        File directorioOrigen = new File(rutaDirectorioOrigen);
        File directorioDestino = new File(rutaDirectorioDestino);

        // Verificar si el directorio origen y el directorio destino existen
        if (!directorioOrigen.exists() || !directorioDestino.exists()) {
            System.out.println("El directorio origen o el directorio destino no existen.");
            return;
        }

        // Verificar si el directorio origen y el directorio destino son directorios válidos
        if (!directorioOrigen.isDirectory() || !directorioDestino.isDirectory()) {
            System.out.println("La ruta del directorio origen o del directorio destino no es válida.");
            return;
        }

        // Obtener los archivos en el directorio origen
        File[] archivos = directorioOrigen.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                try {
                    // Copiar el archivo al directorio destino
                    Path origenPath = archivo.toPath();
                    Path destinoPath = new File(directorioDestino, archivo.getName()).toPath();
                    Files.copy(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Archivo copiado: " + archivo.getName());
                } catch (IOException e) {
                    System.out.println("Error al copiar el archivo: " + archivo.getName());
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("El directorio origen está vacío.");
        }
    }
	
	
	public static void leerUltimoArchivoYCopiar(String rutaDirectorio, String rutaArchivoDestino) {
        File directorio = new File(rutaDirectorio);

        // Verificar si el directorio existe y es un directorio válido
        if (!directorio.exists() || !directorio.isDirectory()) {
            System.out.println("El directorio especificado no existe o no es un directorio válido.");
            return;
        }

        // Obtener la lista de archivos en el directorio
        File[] archivos = directorio.listFiles();
        if (archivos == null || archivos.length == 0) {
            System.out.println("El directorio está vacío.");
            return;
        }

        // Encontrar el último archivo en el directorio
        File ultimoArchivo = archivos[0];
        for (int i = 1; i < archivos.length; i++) {
            if (archivos[i].lastModified() > ultimoArchivo.lastModified()) {
                ultimoArchivo = archivos[i];
            }
        }

        // Leer los datos del último archivo y copiarlos al archivo destino
        try (BufferedReader reader = new BufferedReader(new FileReader(ultimoArchivo));
             BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivoDestino))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                writer.write(linea);
                writer.newLine();
            }
            System.out.println("Datos del último archivo copiados correctamente al archivo destino.");

        } catch (IOException e) {
            System.out.println("Error al leer o copiar archivos.");
            e.printStackTrace();
        }
    }
	
	public boolean CompraLemonAmbiente(Reports report,String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22, String ambiente) {

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
			rtaQuery = oraResp.oraUpdateLemonAmbiente(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad, ambiente);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonAmbiente(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad, ambiente);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//seteo disponible de cuenta
			rtaQuery = oraResp.oraUpdateLemonAmbiente(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 1000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad, ambiente);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonAmbiente(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad, ambiente);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonAmbiente(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad, ambiente);
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
			xmlFile = xmlFile.replace("{{DE22}}", DE22);
			/*
			 * // obtengo el valor del DE38 de la ejecucion  
						String DE38 = JsonPath.from(respBody).getString("DE38");				
						// obtengo el body para la devolucion
						bodyData2 = new String(Files.readString(Paths.get(TCFilesPath + "/TXT/TC.txt")));					
						System.out.println("este es el body 2 " + bodyData2);
						// inserto el valor del DE38 en el body para la devolucion				
						bodyData2 = bodyData2.replace("{{DE38}}", DE38);			
						System.out.println("este es el body para generar la devolucion :" + bodyData2);	
			 * */

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
			//oraResp.setUser(JsonPath.from(configEntidad).getString("DBLEMONQA.user"));
			//oraResp.setPass(JsonPath.from(configEntidad).getString("DBLEMONQA.pass"));
			//oraResp.setHost(JsonPath.from(configEntidad).getString("DBLEMONQA.host"));
			//oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_peru_visa_qa"));


			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");

			//CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, ambiente);
			Thread.sleep(5000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, ambiente);
			Thread.sleep(5000);
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

				//opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				//Thread.sleep(5000);
				//opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				//Thread.sleep(5000);
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
				result = validacionGralLemonAmbiente(oraResp, report, idAutorizacionEmisor, entidad, validaciones, ambiente);
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
	public boolean autorizacionParcial(Reports report,String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22, String DE2, String ambiente) {

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
			System.out.println("ESTE ES EL AMBIENTE DE PRUEBA SETEADO: " + ambiente.toUpperCase());
			
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");
			
			
			System.out.println("ESTE ES EL SWITCH DESDE DONDE EJECUTAMOS: " + opi.toUpperCase());
			
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");
			
			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//seteo disponible de cuenta
			rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 500 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
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
			xmlFile = xmlFile.replace("{{DE22}}", DE22);
			/*
			 * // obtengo el valor del DE38 de la ejecucion  
						String DE38 = JsonPath.from(respBody).getString("DE38");				
						// obtengo el body para la devolucion
						bodyData2 = new String(Files.readString(Paths.get(TCFilesPath + "/TXT/TC.txt")));					
						System.out.println("este es el body 2 " + bodyData2);
						// inserto el valor del DE38 en el body para la devolucion				
						bodyData2 = bodyData2.replace("{{DE38}}", DE38);			
						System.out.println("este es el body para generar la devolucion :" + bodyData2);	
			 * */

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
//CAMBIO DE ENTIDAD OPI
			
			
			opiCmd.cambioAmbiente(report, configEntidad, opi, entidad, ambiente);
			Thread.sleep(5000);
			opiCmd.cambioKeycloak(report, configEntidad, opi, entidad, ambiente);
			Thread.sleep(5000);
			opiCmd.stopOpiCmd(report, configEntidad, opi);
			Thread.sleep(1000);
			opiCmd.startOpiCmd2(report, configEntidad, opi);
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
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, opi, xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, opi);
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, opi);
				Thread.sleep(5000);
				//opiCmd.getOpiStatus(configEntidad);
				//Thread.sleep(5000);
				
				for (int i = 0; i < 10; i++) {			
				res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, opi);
				Thread.sleep(5000);
				System.out.println("ejecucion numero : " + i);

				
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91") && !res.equals("57")) {

					if (!idAutorizacionEmisorJosnString.equals("0")) {
						idAutorizacionEmisor =oraResp.oraOneQuery(report, "SELECT ID_AUTORIZACION FROM AUTORIZACION WHERE ID_AUTORIZACION = (SELECT MAX(ID_AUTORIZACION) from AUTORIZACION)", configEntidad);
						
					//idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, opi);
					System.out.println("***********ingrese al ifFFFFFF");
						}
					break;
				}				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
				}
           // System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
           // JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
           // System.out.println("SE PARSEA STRING A JSON");
          //  idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacion").getAsString();
            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);



			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);

			if (res.equals("10") || res.equals("55"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones,  configEntidad);
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
	public boolean autorizacionParcialLemonUAT(Reports report,String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22) {

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
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//seteo disponible de cuenta
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 500 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
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
			xmlFile = xmlFile.replace("{{DE22}}", DE22);
			/*
			 * // obtengo el valor del DE38 de la ejecucion  
						String DE38 = JsonPath.from(respBody).getString("DE38");				
						// obtengo el body para la devolucion
						bodyData2 = new String(Files.readString(Paths.get(TCFilesPath + "/TXT/TC.txt")));					
						System.out.println("este es el body 2 " + bodyData2);
						// inserto el valor del DE38 en el body para la devolucion				
						bodyData2 = bodyData2.replace("{{DE38}}", DE38);			
						System.out.println("este es el body para generar la devolucion :" + bodyData2);	
			 * */

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

			String ambiente="uat";
			//CAMBIO DE ENTIDAD OPI
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, ambiente);
			Thread.sleep(5000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, ambiente);
			Thread.sleep(5000);
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
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

			if (res.equals("10") || res.equals("55"))
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
	/*************************compra OPI "opiVisaAutoQA"***************************************************/
	public boolean consultaASI(Reports report,String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22, String DE2, String ambiente) {

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
			rtaQuery = oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//seteo disponible de cuenta
			rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 1000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
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
			xmlFile = xmlFile.replace("{{DE22}}", DE22);
			/*
			 * // obtengo el valor del DE38 de la ejecucion  
						String DE38 = JsonPath.from(respBody).getString("DE38");				
						// obtengo el body para la devolucion
						bodyData2 = new String(Files.readString(Paths.get(TCFilesPath + "/TXT/TC.txt")));					
						System.out.println("este es el body 2 " + bodyData2);
						// inserto el valor del DE38 en el body para la devolucion				
						bodyData2 = bodyData2.replace("{{DE38}}", DE38);			
						System.out.println("este es el body para generar la devolucion :" + bodyData2);	
			 * */

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
			
			opiCmd.cambioAmbiente(report, configEntidad, opi, entidad, ambiente);
			Thread.sleep(5000);
			opiCmd.cambioKeycloak(report, configEntidad, opi, entidad, ambiente);
			Thread.sleep(5000);
			opiCmd.stopOpiCmd(report, configEntidad, opi);
			Thread.sleep(1000);
			opiCmd.startOpiCmd2(report, configEntidad, opi);
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
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, opi, xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, opi);
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, opi);
				Thread.sleep(5000);
				//opiCmd.getOpiStatus(configEntidad);
				//Thread.sleep(5000);
				
				for (int i = 0; i < 10; i++) {			
				res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, opi);
				Thread.sleep(5000);
				System.out.println("ejecucion numero : " + i);

				
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91") && !res.equals("57")) {

					if (!idAutorizacionEmisorJosnString.equals("0")) {
						 idAutorizacionEmisor =oraResp.oraOneQuery(report, "SELECT ID_AUTORIZACION FROM AUTORIZACION WHERE ID_AUTORIZACION = (SELECT MAX(ID_AUTORIZACION) from AUTORIZACION)", configEntidad);
							
				            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
						
						System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
					//idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad,opi);
					System.out.println("***********ingrese al ifFFFFFF");
						}
					break;
				}				
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
				}
            System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
          //  JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
          //  System.out.println("SE PARSEA STRING A JSON");
          //  idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacionEmisor").getAsString();
            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);



			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);

			if (res.equals("00") || res.equals("55"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				//result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones);
				result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones,  configEntidad);
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
	public boolean ConsultaASILemonUAT(Reports report,String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22) {

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
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//seteo disponible de cuenta
			rtaQuery = oraResp.oraUpdateLemonUAT(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 1000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
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
			xmlFile = xmlFile.replace("{{DE22}}", DE22);
			/*
			 * // obtengo el valor del DE38 de la ejecucion  
						String DE38 = JsonPath.from(respBody).getString("DE38");				
						// obtengo el body para la devolucion
						bodyData2 = new String(Files.readString(Paths.get(TCFilesPath + "/TXT/TC.txt")));					
						System.out.println("este es el body 2 " + bodyData2);
						// inserto el valor del DE38 en el body para la devolucion				
						bodyData2 = bodyData2.replace("{{DE38}}", DE38);			
						System.out.println("este es el body para generar la devolucion :" + bodyData2);	
			 * */

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

			String ambiente="uat";
			//CAMBIO DE ENTIDAD OPI
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, ambiente);
			Thread.sleep(5000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, ambiente);
			Thread.sleep(5000);
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
	/*************************compra OPI "opiVisaAutoQA"***************************************************/
	public boolean ConsultaASITotalCoinUAT(Reports report,String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22) {

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
			rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);
			
			//seteo disponible de cuenta
			rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE CUENTAS_MONEDA SET DISPONIBLE = 1000000 WHERE ID_CUENTA = " + cuentaNumero , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
			rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
			ppCondi.preCondicionBD(report, rtaQuery);

			//Seteo la cuenta en estado activa para que no falle el TC
			rtaQuery = oraResp.oraUpdateTotalCoinUAT(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA_EXT , configEntidad);
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
			xmlFile = xmlFile.replace("{{DE22}}", DE22);
			/*
			 * // obtengo el valor del DE38 de la ejecucion  
						String DE38 = JsonPath.from(respBody).getString("DE38");				
						// obtengo el body para la devolucion
						bodyData2 = new String(Files.readString(Paths.get(TCFilesPath + "/TXT/TC.txt")));					
						System.out.println("este es el body 2 " + bodyData2);
						// inserto el valor del DE38 en el body para la devolucion				
						bodyData2 = bodyData2.replace("{{DE38}}", DE38);			
						System.out.println("este es el body para generar la devolucion :" + bodyData2);	
			 * */

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
			oraResp.setUser(JsonPath.from(configEntidad).getString("DBUAT.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DBUAT.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DBUAT.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_uat"));


			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			//PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");

			//String ambiente="uat";
			//CAMBIO DE ENTIDAD OPI
			//opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, ambiente);
			//Thread.sleep(5000);
			//opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, ambiente);
			//Thread.sleep(5000);
			///opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			//Thread.sleep(1000);
			//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "compras");
			//Thread.sleep(5000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
			Thread.sleep(5000);

			System.out.println("##### FINALIZA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### FINALIZA EJECUCION DE PRECONDICIONES #####");

			//Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatusTotalCoinUAT(configEntidad)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatusTotalCoinUAT(configEntidad), equalTo(true));
			}

			//EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");

			//Enviar a OPI el archivo del tc
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut", xmlFile);
			String idAutorizacionEmisorJosnString="";

				opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
				Thread.sleep(5000);
				//opiCmd.getOpiStatus(configEntidad);
				//Thread.sleep(5000);
				
				for (int i = 0; i < 10; i++) {			
				res = opiCmd.executeXmlTotalCoinUAT(report, "xmlBaseAux.xml", configEntidad, "opiemimcaut");
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
            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacion").getAsString();
            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);



			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);

			if (res.equals("00") || res.equals("55"))
			{
				report.AddLine("Ejecucion Correcta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
				//VALIDACIONES
				report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
				System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
				result = validacionGralTotalCoinUAT(oraResp, report, idAutorizacionEmisor, entidad, validaciones);
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


	/*********************FIN COMPRA OPI VISA LEMON***********************************************************/
	/******************************INICIO REVERSO***************************************/
	public boolean reversoLemonQA(Reports report, String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22) {

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
			xmlTcFile = xmlTcFile.replace("{{DE22}}", DE22);

			// Preparación del archivo XML de PRECONDICION del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
			String xmlPreFile = Files.readString(Paths.get(TCFilesPath + "/XML/PRE.xml"));
			xmlPreFile = xmlPreFile.replace("{{DE22}}", DE22);
			
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
			oraResp.setUser(JsonPath.from(configEntidad).getString("DBLEMONQA.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DBLEMONQA.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DBLEMONQA.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_peru_visa_qa"));

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			// PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");

			// CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(10000);

			// Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatusLemonQA(configEntidad)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatusLemonQA(configEntidad),
						equalTo(true));
			}

			// EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");

			// EJECUCION OPI
			System.out.println("##### INICIO EJECUCION PRECONDICION #####");
			report.AddLine("##### INICIO EJECUCION PRECONDICION #####");

			// Enviar a OPI el archivo XML de la PRECONDICION
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlPreFile);

			//xmlTcFile = execPreCondicion(report, configEntidad, opiCmd, xmlPreFile, xmlTcFile);

			

			for (int i = 0; i < 3; i++) {
				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(1000);
				//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "reversos");
				//Thread.sleep(1000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(10000);
				
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91")) {
					break;
				}
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
			}
			

			String idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiVisaQaAuto");
			
			 System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
	            JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
	            System.out.println("SE PARSEA STRING A JSON");
	            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacion").getAsString();
	            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
			
			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);

			
			// Enviar a OPI el archivo del TC
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlTcFile);
			
			//for(int i = 0; i < 3; i++) {
				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(1000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);				
				for(int i = 0; i < 10; i++) {			
					res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
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
				
			String idAutorizacionEmisorJosnString2 = opiCmd.getIdAutorizacionEmisor2(report, configEntidad, "opiVisaQaAuto");	
			
			 System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString2);
	            JsonObject idAutoEmiObj2 = parser.parse(idAutorizacionEmisorJosnString2).getAsJsonObject();
	            System.out.println("SE PARSEA STRING A JSON");
	            idAutorizacionEmisor = idAutoEmiObj2.get("IdAutorizacion").getAsString();
	            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
			
			System.out.println("idAutorizacion obtenido: " + idAutorizacionEmisor);
			
			if (res.equals("00"))
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
			
	
			
			
		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}
		
return result;

}
	/***************************FIN REVERSO*/
	/******************************INICIO REVERSO***************************************/
	public boolean reversoLemonUAT(Reports report, String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22) {

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
			xmlTcFile = xmlTcFile.replace("{{DE22}}", DE22);

			// Preparación del archivo XML de PRECONDICION del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
			String xmlPreFile = Files.readString(Paths.get(TCFilesPath + "/XML/PRE.xml"));
			xmlPreFile = xmlPreFile.replace("{{DE22}}", DE22);
			
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
			oraResp.setUser(JsonPath.from(configEntidad).getString("DBLEMONUAT.user"));
			oraResp.setPass(JsonPath.from(configEntidad).getString("DBLEMONUAT.pass"));
			oraResp.setHost(JsonPath.from(configEntidad).getString("DBLEMONUAT.host"));
			oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_peru_visa_uat"));

			System.out.println("##[command] : <------ Initializating Test: " + name + " ------>\r\n");
			report.AddLine("<------ Initializating Test: " + name + " ------>");

			// PRECONDICIONES
			System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
			report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");

			// CAMBIO DE ENTIDAD OPI
			opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(1000);
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
			Thread.sleep(10000);

			// Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatusLemonQA(configEntidad)) {
				MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatusLemonQA(configEntidad),
						equalTo(true));
			}

			// EJECUCION OPI
			System.out.println("##### INICIO EJECUCION OPI #####");
			report.AddLine("##### INICIO EJECUCION OPI #####");

			// EJECUCION OPI
			System.out.println("##### INICIO EJECUCION PRECONDICION #####");
			report.AddLine("##### INICIO EJECUCION PRECONDICION #####");

			// Enviar a OPI el archivo XML de la PRECONDICION
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlPreFile);

			//xmlTcFile = execPreCondicion(report, configEntidad, opiCmd, xmlPreFile, xmlTcFile);

			

			for (int i = 0; i < 3; i++) {
				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(1000);
				//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "reversos");
				//Thread.sleep(1000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(10000);
				
				res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91")) {
					break;
				}
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
			}
			

			
			String idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiVisaQaAuto");
			
			 System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
	            JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
	            System.out.println("SE PARSEA STRING A JSON");
	            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacion").getAsString();
	            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
			
			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);

			
			// Enviar a OPI el archivo del TC
			opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlTcFile);
			
			//for(int i = 0; i < 3; i++) {
				opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(1000);
				opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
				Thread.sleep(5000);				
				for(int i = 0; i < 10; i++) {			
					res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
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
				
			String idAutorizacionEmisorJosnString2 = opiCmd.getIdAutorizacionEmisor2(report, configEntidad, "opiVisaQaAuto");	
			
			 System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString2);
	            JsonObject idAutoEmiObj2 = parser.parse(idAutorizacionEmisorJosnString2).getAsJsonObject();
	            System.out.println("SE PARSEA STRING A JSON");
	            idAutorizacionEmisor = idAutoEmiObj2.get("IdAutorizacion").getAsString();
	            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
			
			System.out.println("idAutorizacion obtenido: " + idAutorizacionEmisor);
			
			if (res.equals("00"))
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
			
	
			
			
		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("##[warning] : Error:\r\n" + e);
			result = false;
		}
		
return result;

}
	/***************************FIN REVERSO*/
	/******************************INICIO REVERSO***************************************/
	public boolean reverso(Reports report, String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22, String DE2, String ambiente) {

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
            System.out.println("ESTE ES EL AMBIENTE DE PRUEBA SETEADO: " + ambiente.toUpperCase());
			
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");
			
			
			System.out.println("ESTE ES EL SWITCH DESDE DONDE EJECUTAMOS: " + opi.toUpperCase());
			
			System.out.println("\r\n##################################################################################################################################################################################################################"
					+ "##################################################################################################################################################################################################################\r\n");
			
			// Preparación del archivo JSON del TC
			System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");

			String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));
			
			// Preparación del archivo XML de PRECONDICION del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
			String xmlPreFile = Files.readString(Paths.get(TCFilesPath + "/XML/PRE.xml"));
			xmlPreFile = xmlPreFile.replace("{{DE22}}", DE22);

			// Preparación del archivo XML del TC
			System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
			String xmlTcFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
			xmlTcFile = xmlTcFile.replace("{{DE22}}", DE22);

			
			
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
			//opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
			//Thread.sleep(1000);
			//CAMBIO DE AMBIENTE 
			opiCmd.cambioAmbiente(report, configEntidad, opi, entidad, ambiente);
			Thread.sleep(8000);
			opiCmd.cambioKeycloak(report, configEntidad, opi, entidad, ambiente);
			Thread.sleep(8000);
			opiCmd.startOpiCmd2(report, configEntidad, opi);
			Thread.sleep(10000);

			// Se valida el funcionamiento de OPI
			if (!opiCmd.getOpiStatus(configEntidad, opi)) {
				opiCmd.stopOpiCmd(report, configEntidad, opi);
				Thread.sleep(1000);
				opiCmd.startOpiCmd2(report, configEntidad, opi);
				Thread.sleep(10000);
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
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, opi, xmlPreFile);

			//xmlTcFile = execPreCondicion(report, configEntidad, opiCmd, xmlPreFile, xmlTcFile);

			

			for (int i = 0; i < 3; i++) {
				//opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
				//Thread.sleep(1000);
				//opiCmd.cambioEntidadSimulador(report, configEntidad, "opiemimcaut", entidad, "reversos");
				//Thread.sleep(1000);
				//opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
				//Thread.sleep(10000);
				
				res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, opi);
				if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91")) {
					break;
				}
				report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
				System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
			}
			

			String idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, opi);
			
			 System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
	            JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
	            System.out.println("SE PARSEA STRING A JSON");
	            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacion").getAsString();
	            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
			
			System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);

			
			// Enviar a OPI el archivo del TC
			opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, opi, xmlTcFile);
			
			//for(int i = 0; i < 3; i++) {
				opiCmd.stopOpiCmd(report, configEntidad, opi);
				Thread.sleep(1000);
				opiCmd.startOpiCmd2(report, configEntidad, opi);
				Thread.sleep(5000);				
				for(int i = 0; i < 10; i++) {			
					res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, opi);
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
				
			String idAutorizacionEmisorJosnString2 = opiCmd.getIdAutorizacionEmisor2(report, configEntidad, opi);	
			
			 System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString2);
	            JsonObject idAutoEmiObj2 = parser.parse(idAutorizacionEmisorJosnString2).getAsJsonObject();
	            System.out.println("SE PARSEA STRING A JSON");
	            idAutorizacionEmisor = idAutoEmiObj2.get("IdAutorizacion").getAsString();
	            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
			
			System.out.println("idAutorizacion obtenido: " + idAutorizacionEmisor);
			
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
	/***************************FIN REVERSO*/
	/***************************COMPRA DEVOLUCION LEMON QA**********************/
public boolean compraDevolucionLemonQA(Reports report,String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22) {
		
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
					xmlTcFile = xmlTcFile.replace("{{DE22}}", DE22);
					//Preparación del archivo XML de PRECONDICION del TC
					System.out.println("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
					report.AddLine("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
					String xmlPreFile = Files.readString(Paths.get(TCFilesPath + "/XML/PRE.xml"));
					xmlPreFile = xmlPreFile.replace("{{DE22}}", DE22);

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

					// SET dbWorker
					oraResp.setUser(JsonPath.from(configEntidad).getString("DBLEMONQA.user"));
					oraResp.setPass(JsonPath.from(configEntidad).getString("DBLEMONQA.pass"));
					oraResp.setHost(JsonPath.from(configEntidad).getString("DBLEMONQA.host"));
					oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_peru_visa_qa"));
					
					System.out.println("**********************INICIA Test: " + name + " ------>\r\n");
					report.AddLine("**************************INICIA Test: " + name + " ------>");
					
					//PRECONDICIONES
					System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
					report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");
					
					// REALIZO STOP OPI
					opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
					Thread.sleep(1000);
					//CAMBIO DE AMBIENTE 
					opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
					Thread.sleep(8000);
					opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "qa");
					Thread.sleep(8000);
					
					//REALIZO START OPI
					opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
					Thread.sleep(1000);
					
					//Se valida el funcionamiento de OPI
					if (!opiCmd.getOpiStatusLemonQA(configEntidad)) {
						MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatusLemonQA(configEntidad), equalTo(true));
					}
					
					//EJECUCION OPI
					System.out.println("##### INICIO EJECUCION PRECONDICION #####");
					report.AddLine("##### INICIO EJECUCION PRECONDICION #####");
					
					/*oraResp.oraUpdateLemonQA(report, "update acumulador_rg5272\r\n"
							+ "set acum_autorizaciones = 0,\r\n"
							+ "acum_autorizaciones_moneda_local = 0,\r\n"
							+ "acum_impuesto = 0,\r\n"
							+ "acum_presentaciones = 0,\r\n"
							+ "acum_impuesto_consumo = 0\r\n"
							+ "where id_cuenta = " + cuentaNumero, configEntidad);
					
					oraResp.oraUpdateLemonQA(report, "update acumulador_rg5272\r\n"
							+ "set acum_autorizaciones = 0,\r\n"
							+ "acum_autorizaciones_moneda_local = 0,\r\n"
							+ "acum_impuesto = 0,\r\n"
							+ "acum_presentaciones = 0,\r\n"
							+ "acum_impuesto_consumo = 0\r\n"
							+ "where id_cuenta = "+ cuentaNumero, configEntidad);*/
					
					//Enviar a OPI el archivo XML de la PRECONDICION
					
					opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlPreFile);
						
					//ejecuto el archivo enviado
					
					for(int i = 0; i < 3; i++) {
						opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
						Thread.sleep(1000);
						opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
						Thread.sleep(5000);
						res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
						if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91")) {
						//if(!res.equals("91")) {
							break;
						}				
						report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
						System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
					}
					
					String idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiVisaQaAuto");	
					
					 System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
			            JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
			            System.out.println("SE PARSEA STRING A JSON");
			            idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacion").getAsString();
			            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
					
					System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
					
					//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
					//oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
					//ppCondi.preCondicionBD(report, rtaQuery);
					
					//SETEO LA AUTORIZACION COMO PRESENTADA
					oraResp.oraUpdateLemonQA(report, "UPDATE AUTORIZACION \r\n"
							+ "SET PRESENTACION_PROCESADA = 2,\r\n"
							+ "FECHA_RELACION = SYSDATE \r\n"
							+ "where ID_AUTORIZACION = " + idAutorizacionEmisor, configEntidad);
					
					oraResp.oraUpdateLemonQA(report, "UPDATE AJUSTES_SOCIOS \r\n"
							+ "SET ID_ESTADO = 2,\r\n"
							+ "ID_CONSUMO = '12876' \r\n"
							+ "where ID_AUTORIZACION = " + idAutorizacionEmisor, configEntidad);
					//Seteo la cuenta en estado activa para que no falle el TC
					//rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
					//ppCondi.preCondicionBD(report, rtaQuery);
					
					
					opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlTcFile);
					
					for(int i = 0; i < 3; i++) {
						opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
						Thread.sleep(1000);
						opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
						Thread.sleep(5000);
						res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
						if(!res.equals("91")) {
							break;
						}				
						report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
						System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
					}
					
					String idAutorizacionEmisorJosnString2 = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiVisaQaAuto");	
					
					 System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString2);
			            JsonObject idAutoEmiObj2 = parser.parse(idAutorizacionEmisorJosnString2).getAsJsonObject();
			            System.out.println("SE PARSEA STRING A JSON");
			            idAutorizacionEmisor = idAutoEmiObj2.get("IdAutorizacion").getAsString();
			            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
					
					System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
					
					if (res.equals("00"))
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
				} catch (Exception e) {
					report.AddLine("Error:<br>" + e);
					System.out.println("##[warning] : Error:\r\n" + e);
					result = false;
				}
				
		return result;
		
	}
	/**************************FIN COMPRA DEVOLUCION LEMON QA*******************/
/***************************COMPRA DEVOLUCION LEMON QA**********************/
public boolean compraDevolucionLemonUAT(Reports report,String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22) {
		
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
					xmlTcFile = xmlTcFile.replace("{{DE22}}", DE22);
					//Preparación del archivo XML de PRECONDICION del TC
					System.out.println("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
					report.AddLine("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
					String xmlPreFile = Files.readString(Paths.get(TCFilesPath + "/XML/PRE.xml"));
					xmlPreFile = xmlPreFile.replace("{{DE22}}", DE22);

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

					// SET dbWorker
					oraResp.setUser(JsonPath.from(configEntidad).getString("DBLEMONUAT.user"));
					oraResp.setPass(JsonPath.from(configEntidad).getString("DBLEMONUAT.pass"));
					oraResp.setHost(JsonPath.from(configEntidad).getString("DBLEMONUAT.host"));
					oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad_peru_visa_uat"));
					
					System.out.println("**********************INICIA Test: " + name + " ------>\r\n");
					report.AddLine("**************************INICIA Test: " + name + " ------>");
					
					//PRECONDICIONES
					System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
					report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");
					
					// REALIZO STOP OPI
					opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
					Thread.sleep(1000);
					//CAMBIO DE AMBIENTE 
					opiCmd.cambioAmbiente(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
					Thread.sleep(8000);
					opiCmd.cambioKeycloak(report, configEntidad, "opiVisaQaAuto", entidad, "uat");
					Thread.sleep(8000);
					
					//REALIZO START OPI
					opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
					Thread.sleep(1000);
					
					//Se valida el funcionamiento de OPI
					if (!opiCmd.getOpiStatusLemonQA(configEntidad)) {
						MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatusLemonQA(configEntidad), equalTo(true));
					}
					
					//EJECUCION OPI
					System.out.println("##### INICIO EJECUCION PRECONDICION #####");
					report.AddLine("##### INICIO EJECUCION PRECONDICION #####");
					
					/*oraResp.oraUpdateLemonQA(report, "update acumulador_rg5272\r\n"
							+ "set acum_autorizaciones = 0,\r\n"
							+ "acum_autorizaciones_moneda_local = 0,\r\n"
							+ "acum_impuesto = 0,\r\n"
							+ "acum_presentaciones = 0,\r\n"
							+ "acum_impuesto_consumo = 0\r\n"
							+ "where id_cuenta = " + cuentaNumero, configEntidad);
					
					oraResp.oraUpdateLemonQA(report, "update acumulador_rg5272\r\n"
							+ "set acum_autorizaciones = 0,\r\n"
							+ "acum_autorizaciones_moneda_local = 0,\r\n"
							+ "acum_impuesto = 0,\r\n"
							+ "acum_presentaciones = 0,\r\n"
							+ "acum_impuesto_consumo = 0\r\n"
							+ "where id_cuenta = "+ cuentaNumero, configEntidad);*/
					
					//Enviar a OPI el archivo XML de la PRECONDICION
					
					opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlPreFile);
						
					//ejecuto el archivo enviado
					
					for(int i = 0; i < 3; i++) {
						opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
						Thread.sleep(1000);
						opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
						Thread.sleep(5000);
						res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
						if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91")) {
						//if(!res.equals("91")) {
							break;
						}				
						report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
						System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
					}
					
					String idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiVisaQaAuto");	
					
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
					oraResp.oraUpdateLemonUAT(report, "UPDATE AUTORIZACION \r\n"
							+ "SET PRESENTACION_PROCESADA = 2,\r\n"
							+ "FECHA_RELACION = '28/08/2023' \r\n"
							+ "where ID_AUTORIZACION_EMISOR = " + idAutorizacionEmisor, configEntidad);
					
					oraResp.oraUpdateLemonUAT(report, "UPDATE AJUSTES_SOCIOS \r\n"
							+ "SET ID_ESTADO = 2,\r\n"
							+ "ID_CONSUMO = '12876' \r\n"
							+ "where ID_AUTORIZACION = " + idAutorizacionEmisor, configEntidad);
					//Seteo la cuenta en estado activa para que no falle el TC
					//rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
					//ppCondi.preCondicionBD(report, rtaQuery);
					
					
					opiCmd.sshSendCmdCreateXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto", xmlTcFile);
					
					for(int i = 0; i < 3; i++) {
						opiCmd.stopOpiCmd(report, configEntidad, "opiVisaQaAuto");
						Thread.sleep(1000);
						opiCmd.startOpiCmd2(report, configEntidad, "opiVisaQaAuto");
						Thread.sleep(5000);
						res = opiCmd.executeXmlLemonQA(report, "xmlBaseAux.xml", configEntidad, "opiVisaQaAuto");
						if(!res.equals("91")) {
							break;
						}				
						report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
						System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
					}
					
					String idAutorizacionEmisorJosnString2 = opiCmd.getIdAutorizacionEmisor(report, configEntidad, "opiVisaQaAuto");	
					
					 System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString2);
			            JsonObject idAutoEmiObj2 = parser.parse(idAutorizacionEmisorJosnString2).getAsJsonObject();
			            System.out.println("SE PARSEA STRING A JSON");
			            idAutorizacionEmisor = idAutoEmiObj2.get("IdAutorizacion").getAsString();
			            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
					
					System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
					
					if (res.equals("00"))
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
				} catch (Exception e) {
					report.AddLine("Error:<br>" + e);
					System.out.println("##[warning] : Error:\r\n" + e);
					result = false;
				}
				
		return result;
		
	}
	/**************************FIN COMPRA DEVOLUCION LEMON QA*******************/
/***************************COMPRA DEVOLUCION LEMON QA**********************/
public boolean compraDevolucion(Reports report,String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22, String DE2, String ambiente) {
		
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
					System.out.println("ESTE ES EL AMBIENTE DE PRUEBA SETEADO: " + ambiente.toUpperCase());
					
					System.out.println("\r\n##################################################################################################################################################################################################################"
							+ "##################################################################################################################################################################################################################\r\n");
					
					
					System.out.println("ESTE ES EL SWITCH DESDE DONDE EJECUTAMOS: " + opi.toUpperCase());
					
					System.out.println("\r\n##################################################################################################################################################################################################################"
							+ "##################################################################################################################################################################################################################\r\n");
					
					//Preparación del archivo JSON del TC
					System.out.println("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
					report.AddLine("##### PREPARACION DEL ARCHIVO JSON DEL TC #####");
					
					String jsonFile = new String(Files.readAllBytes(Paths.get(TCFilesPath + "/TC.json")));
					
					//Preparación del archivo XML del TC
					System.out.println("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
					report.AddLine("##### PREPARACION DEL ARCHIVO XML DEL TC #####");
					String xmlTcFile = Files.readString(Paths.get(TCFilesPath + "/XML/TC.xml"));
					xmlTcFile = xmlTcFile.replace("{{DE2}}", DE2);
					xmlTcFile = xmlTcFile.replace("{{DE22}}", DE22);
					//Preparación del archivo XML de PRECONDICION del TC
					System.out.println("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
					report.AddLine("##### PREPARACION DEL ARCHIVO XML DE PRECONDICION #####");
					String xmlPreFile = Files.readString(Paths.get(TCFilesPath + "/XML/PRE.xml"));
					xmlPreFile = xmlPreFile.replace("{{DE2}}", DE2);
					xmlPreFile = xmlPreFile.replace("{{DE22}}", DE22);

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

					// SET dbWorker
					//oraResp.setUser(JsonPath.from(configEntidad).getString("DB.user"));
					//oraResp.setPass(JsonPath.from(configEntidad).getString("DB.pass"));
					//oraResp.setHost(JsonPath.from(configEntidad).getString("DB.host"));
					//oraResp.setEntidad(JsonPath.from(configEntidad).getString("ENTIDAD.id_entidad"));
					
					System.out.println("**********************INICIA Test: " + name + " ------>\r\n");
					report.AddLine("**************************INICIA Test: " + name + " ------>");
					
					//PRECONDICIONES
					System.out.println("##### INICIA EJECUCION DE PRECONDICIONES #####");
					report.AddLine("##### INICIA EJECUCION DE PRECONDICIONES #####");
					
					// REALIZO STOP OPI
					//opiCmd.stopOpiCmd(report, configEntidad, opi);
					//Thread.sleep(1000);
					//CAMBIO DE AMBIENTE 
					opiCmd.cambioAmbiente(report, configEntidad, opi, entidad, ambiente);
					Thread.sleep(8000);
					opiCmd.cambioKeycloak(report, configEntidad, opi, entidad, ambiente);
					Thread.sleep(8000);
					
					//REALIZO START OPI
					//opiCmd.startOpiCmd2(report, configEntidad, opi);
					//Thread.sleep(1000);
					
					//Se valida el funcionamiento de OPI
					if (!opiCmd.getOpiStatus(configEntidad, opi)) {
						opiCmd.stopOpiCmd(report, configEntidad, opi);
						Thread.sleep(1000);
						opiCmd.startOpiCmd2(report, configEntidad, opi);
						Thread.sleep(1000);
						MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi), equalTo(true));
					}
					
					//EJECUCION OPI
					System.out.println("##### INICIO EJECUCION PRECONDICION #####");
					report.AddLine("##### INICIO EJECUCION PRECONDICION #####");
					
					/*oraResp.oraUpdateLemonQA(report, "update acumulador_rg5272\r\n"
							+ "set acum_autorizaciones = 0,\r\n"
							+ "acum_autorizaciones_moneda_local = 0,\r\n"
							+ "acum_impuesto = 0,\r\n"
							+ "acum_presentaciones = 0,\r\n"
							+ "acum_impuesto_consumo = 0\r\n"
							+ "where id_cuenta = " + cuentaNumero, configEntidad);
					
					oraResp.oraUpdateLemonQA(report, "update acumulador_rg5272\r\n"
							+ "set acum_autorizaciones = 0,\r\n"
							+ "acum_autorizaciones_moneda_local = 0,\r\n"
							+ "acum_impuesto = 0,\r\n"
							+ "acum_presentaciones = 0,\r\n"
							+ "acum_impuesto_consumo = 0\r\n"
							+ "where id_cuenta = "+ cuentaNumero, configEntidad);*/
					
					//Enviar a OPI el archivo XML de la PRECONDICION
					
					opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, opi, xmlPreFile);
						
					//ejecuto el archivo enviado
					
					for(int i = 0; i < 3; i++) {
						//opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
						//Thread.sleep(1000);
						//opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
						//Thread.sleep(5000);
						res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, opi);
						if(!res.equals("unconnected ISOChannel") && !res.equals("96") && !res.equals("91")) {
						//if(!res.equals("91")) {
							break;
						}				
						report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
						System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
					}
					
					//String idAutorizacionEmisorJosnString = opiCmd.getIdAutorizacionEmisor(report, configEntidad, opi);	
					
					 //System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString);
			          //  JsonObject idAutoEmiObj = parser.parse(idAutorizacionEmisorJosnString).getAsJsonObject();
			          //  System.out.println("SE PARSEA STRING A JSON");
			          //  idAutorizacionEmisor = idAutoEmiObj.get("IdAutorizacion").getAsString();
			       
			            idAutorizacionEmisor =oraResp.oraOneQuery(report, "SELECT ID_AUTORIZACION FROM AUTORIZACION WHERE ID_AUTORIZACION = (SELECT MAX(ID_AUTORIZACION) from AUTORIZACION)", configEntidad);
						
			            System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
					
					System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
					
					
					
					//Seteo la tarjeta en estado normalhabilitada para que no falle el TC
					//oraResp.oraUpdate(report, "UPDATE TARJETAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
					//ppCondi.preCondicionBD(report, rtaQuery);
					
					//SETEO LA AUTORIZACION COMO PRESENTADA
					//oraResp.oraUpdate(report, "UPDATE AUTORIZACION \r\n"
						//	+ "SET PRESENTACION_PROCESADA = 2,\r\n"
						//	+ "FECHA_RELACION = SYSDATE \r\n"
						//	+ "where ID_AUTORIZACION = " + idAutorizacionEmisor, configEntidad);
					//oraResp.oraUpdate(report, "UPDATE AUTORIZACION SET FECHA_RELACION = SYSDATE where ID_AUTORIZACION = (SELECT MAX(ID_AUTORIZACION) from AUTORIZACION)", configEntidad);
					oraResp.oraUpdate(report, "UPDATE AUTORIZACION SET FECHA_RELACION = SYSDATE where ID_AUTORIZACION = " + idAutorizacionEmisor, configEntidad);
					/*oraResp.oraUpdateTotalCoinUAT(report, "UPDATE AUTORIZACION \r\n"
							+ "SET PRESENTACION_PROCESADA = 2,\r\n"
							+ "FECHA_RELACION = '28/08/2023' \r\n"
							+ "where ID_AUTORIZACION_EMISOR = " + idAutorizacionEmisor, configEntidad);
					
					oraResp.oraUpdateTotalCoinUAT(report, "UPDATE AJUSTES_SOCIOS \r\n"
							+ "SET ID_ESTADO = 2,\r\n"
							+ "ID_CONSUMO = '12876' \r\n"
							+ "where ID_AUTORIZACION = " + idAutorizacionEmisor, configEntidad);*/
					//Seteo la cuenta en estado activa para que no falle el TC
					//rtaQuery = oraResp.oraUpdate(report, "UPDATE CUENTAS SET ID_ESTADO = 0 WHERE ID_CUENTA = " + ID_CUENTA , configEntidad);
					//ppCondi.preCondicionBD(report, rtaQuery);
					
					
					opiCmd.sshSendCmdCreateXml(report, "xmlBaseAux.xml", configEntidad, opi, xmlTcFile);
					
					for(int i = 0; i < 3; i++) {
						opiCmd.stopOpiCmd(report, configEntidad, "opiemimcaut");
						Thread.sleep(1000);
						opiCmd.startOpiCmd2(report, configEntidad, "opiemimcaut");
						Thread.sleep(5000);
						res = opiCmd.executeXml(report, "xmlBaseAux.xml", configEntidad, opi);
						if(!res.equals("91")) {
							break;
						}				
						report.AddLine("Ejecucion Incorrecta<br>DE39: " + res);
						System.out.println("##[section] : Ejecucion Incorrecta\r\nDE39: " + res + "\r\n");
					}
					
					 idAutorizacionEmisor =oraResp.oraOneQuery(report, "SELECT ID_AUTORIZACION FROM AUTORIZACION WHERE ID_AUTORIZACION = (SELECT MAX(ID_AUTORIZACION) from AUTORIZACION)", configEntidad);
						
			        System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
					
					System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
					
					
					//String idAutorizacionEmisorJosnString2 = opiCmd.getIdAutorizacionEmisor(report, configEntidad, opi);	
					
					// System.out.println("LINEA ID AUTORIZACION EMISOR: " + idAutorizacionEmisorJosnString2);
			         //   JsonObject idAutoEmiObj2 = parser.parse(idAutorizacionEmisorJosnString2).getAsJsonObject();
			         //   System.out.println("SE PARSEA STRING A JSON");
			         //   idAutorizacionEmisor = idAutoEmiObj2.get("IdAutorizacion").getAsString();
			          //  System.out.println("ID AUTORIZACION EMISOR: " + idAutorizacionEmisor);
					
					//System.out.println("idAutorizacionEmisor obtenido: " + idAutorizacionEmisor);
					
					if (res.equals("00"))
					{
						report.AddLine("Ejecucion Correcta<br>DE39: " + res);
						System.out.println("##[section] : Ejecucion Correcta\r\nDE39: " + res + "\r\n");
						//VALIDACIONES
						report.AddLine("idAutorizacionEmisor: " + idAutorizacionEmisor);
						System.out.println("##[section] : idAutorizacionEmisor: " + idAutorizacionEmisor + "\r\n");
						//result = validacionGralTotalCoinUAT(oraResp, report, idAutorizacionEmisor, entidad, validaciones, opi);
				//		result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones, opi);
						result = validacionGral(oraResp, report, idAutorizacionEmisor, entidad, validaciones,  configEntidad);
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
	/**************************FIN COMPRA DEVOLUCION LEMON QA*******************/
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
					if (!opiCmd.getOpiStatus(configEntidad, opi)) {
						MatcherAssert.assertThat("OPI no se encuentra operativo ", opiCmd.getOpiStatus(configEntidad, opi), equalTo(true));
					}
					
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
			
			System.out.println("SE COMPLETA BLOQUE DE PRUEBAS CON EXITO!! TEST OK");
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
	/*********************************VALIDACION LEMON QA********************************************/
	private boolean validacionGralLemonAmbiente(dbWorker oraResp, Reports report, String idAutorizacionEmisor, String entidad, JsonArray validaciones, String ambiente) throws SQLException {
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
			
			ValidacionLemonAmbiente(oraResp, report, queryVerf, resultadosEsperados, entidad, ambiente);
			
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
private void ValidacionLemonAmbiente(dbWorker oraResp, Reports report , String queryVerf, JsonArray resultadosEsperados, String entidad, String ambiente) throws SQLException {
		
		String validaRes[][];
		
		
		validaRes = oraResp.executeQueryLemonAmbiente(queryVerf, ambiente);
		
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
	/*********************************VALIDACION LEMON UAT********************************************/
	private boolean validacionGralTotalCoinUAT(dbWorker oraResp, Reports report, String idAutorizacionEmisor, String entidad, JsonArray validaciones) throws SQLException {
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
			
			ValidacionTotalCoinUAT(oraResp, report, queryVerf, resultadosEsperados, entidad);
			
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
private void ValidacionTotalCoinUAT(dbWorker oraResp, Reports report , String queryVerf, JsonArray resultadosEsperados, String entidad) throws SQLException {
		
		String validaRes[][];
		
		validaRes = oraResp.executeQueryTotalCoinUAT(queryVerf);
		
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
