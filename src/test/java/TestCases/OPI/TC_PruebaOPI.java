package TestCases.OPI;

import org.w3c.tidy.Report;

import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.PrePostCondi;
import Tools.dbWorker;
import Tools.sshWorker;

public class TC_PruebaOPI {

	public boolean Test(Reports report, DriverManager DM, int iteration, String name, String configEntidad) {
		//Validation var
		boolean result = true;
		try {			
			System.out.println("Configuring");
			
			//Set Variables
			String res;

			//Instancias
			sshWorker opiCmd = new sshWorker();
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			
			System.out.println("Initializating test");
		
			//Ejecucion de OPI
			res = opiCmd.sshSendCmd(report, "compraQAInterno.xml", configEntidad);
			
			if (res.equals("00"))
			{
				report.AddLine("Ejecucion Correcta<br>nDE39:" + res);
				System.out.println("Ejecucion Correcta\r\nDE39:" + res);
			} else {
				report.AddLine("FAIL<br>nDE39:" + res);
				System.out.println("FAIL\r\nDE39:" + res);
				result = false;
			}
			
		} catch (Exception e) {
			report.AddLine("Error:<br>" + e);
			System.out.println("Error:\r\n" + e);
			result = false;
		}
		
		return result;
	}
}
