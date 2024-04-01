package TestCases.GlobalOnLine;

import org.openqa.selenium.WebDriver;
import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Repositories.Repo_WebRepository;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;
import Pasos.Generales.Acceso;
import Pasos.Generales.NavLateral;
import Pasos.GlobalOnLine.TC_07_GO_PASOS;

public class TC_07_WEB_GO {
	WebDriver driver;
	
	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad, String cuentas_generales) {
		//validation 
		boolean result = true;
		try {				
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
                          + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_07_WEB_GO\r\n");

			//SELECT THE DRIVER AND PATH
			driver = DM.CreateDriver(DM.CHROME);
			report.SetDriver(driver);

			//SET THE REPOSITORIES TO USE
			Repo_WebRepository repo = new Repo_WebRepository();
			repo.setDriver(driver);

			//SET STEPS INSTANCES
			Acceso acceso = new Acceso();
			NavLateral menu = new NavLateral();
			TC_07_GO_PASOS tc = new TC_07_GO_PASOS();
			dbWorker oraResp = new dbWorker();

			//SET VARIABLES
			String ID_SUCURSAL = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.id_sucursal_cambio");
			String descripcion_sucursal = "";
			String ID_GRUPO_AFINIDAD = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.id_grupo_afinidad_cambio");
			String descripcion_ga = "";		
			String ID_PRODUCTO = JsonPath.from(cuentas_generales).get("PARAMETRIA_CUENTAS.id_producto");
			String descripcion_producto = "";	
			String dni = "34293772";
			String cuil = "20342937722";
			String nroCuenta = "";
			String idEstadoCuenta = "";
			boolean rtaBorrar = false;
			String cuentaCreada = "";
			
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GO = JsonPath.from(configEntidad).get("GO.url_go");
			
			System.out.println("##[command] : ------ Initializating test: " + name + " ------\r\n");
			
			//PRE-CONDICIONES, se buscan las descripciones de la sucursal, grupo de afinidad y producto
			//SUCURSAL
			descripcion_sucursal = oraResp.oraOneQuery(report, "SELECT DESCRIPCION FROM SUCURSALES_ENTIDAD WHERE ID_SUCURSAL = " + ID_SUCURSAL, configEntidad);
			
			//GRUPO_DE_AFINIDAD
		   descripcion_ga = oraResp.oraOneQuery(report, "SELECT DESCRIPCION FROM GRUPOS_AFINIDAD WHERE ID_GRUPO_AFINIDAD = " + ID_GRUPO_AFINIDAD, configEntidad);		
			
			//PRODUCTO
			descripcion_producto = oraResp.oraOneQuery(report, "SELECT DESCRIPCION FROM PRODUCTOS WHERE ID_PRODUCTO = " + ID_PRODUCTO, configEntidad);
		
			
			//Se abre el driver con la URL de GO
			driver.get(URL_GO);
			
     		//ADD THE STEPS BELOW
			//LOGIN
			report.AddLine("Acceso a la pagina de Global Online con las credenciales");
			acceso.loginGlobalOnLine(data, report, DM, iteration, name, repo, configEntidad);

			//NAVEGACION DEL MENU SIDEBAR
			menu.navegacion_lvl3(data, report, DM, iteration, name, repo, "Emisión", "Socios", "Cuentas");

			//PÁGINA 1
			tc.pagina1(data, report, DM, iteration, name, repo);

			//PÁGINA 2
			tc.pagina2(data, report, DM, iteration, name, repo, descripcion_sucursal, descripcion_ga, ID_PRODUCTO, descripcion_producto);

			//PÁGINA 3
			tc.pagina3(data, report, DM, iteration, name, repo, dni, cuil);

			//ADD VALIDATIONS AT THE END
			tc.validar(data, report, DM, iteration, name, repo);

			// Esperamos a que se grabe el registro en la base de datos
			Thread.sleep(3000);		
			
				
			// Validacion de creacion de cuenta
			report.AddLine("Verificamos la creacion de la cuenta con el dni.");
			System.out.println("Verificamos la creacion de la cuenta con el dni. \r\n");
			
			cuentaCreada = oraResp.oraOneQuery(report, "select count (c.id_cuenta) cuenta_creada\r\n"
					+ "from personas p\r\n"
					+ "inner join socios s on s.id_persona = p.id_persona\r\n"
					+ "inner join cuentas c on s.id_cuenta = c.id_cuenta\r\n"
					+ "inner join tarjetas t on c.id_cuenta = t.id_cuenta\r\n"
					+ "inner join tarjetas_historial th on th.id_tarjeta = t.id_tarjeta\r\n"
					+ "where p.nro_documento = " + dni, configEntidad);
			
			if (cuentaCreada.equals("1")) {
				
				report.AddLine("Cuenta creada correctamente en la BD.");
				System.out.println("##[section] : Cuenta creada correctamente en la BD.\r\n");
				
			} else {
				report.AddLineAssertionError("Error al crear la cuenta.");
				System.out.println("##[warning] : Error al crear la cuenta. \r\n");
				result = false;
			}
			
			
			// Almacenamos el numero de cuenta del dni
			nroCuenta = oraResp.oraOneQuery(report, "select c.id_cuenta\r\n"
					+ "from personas p\r\n"
					+ "inner join socios s on s.id_persona = p.id_persona\r\n"
					+ "inner join cuentas c on s.id_cuenta = c.id_cuenta\r\n"
					+ "inner join tarjetas t on c.id_cuenta = t.id_cuenta\r\n"
					+ "inner join tarjetas_historial th on th.id_tarjeta = t.id_tarjeta\r\n"
					+ "where p.nro_documento = " + dni, configEntidad);
						
			if (!nroCuenta.equals("")) {
				
				report.AddLine("Alta de cuenta exitosa. El DNI: " + dni + " tiene asociada la cuenta numero: " + nroCuenta);
				System.out.println("##[section] : Alta de cuenta exitosa. El DNI: " + dni + " tiene asociada la cuenta numero: " + nroCuenta + "\r\n");
			}
			else {
				report.AddLineAssertionError("ERROR: Fallo el alta de cuenta con el DNI: " + dni);
				System.out.println("##[warning] : ERROR: Fallo el alta de cuenta con el DNI: " + dni + "\r\n");
		        result = false;
			}
			
			// Validacion de id_estado
			report.AddLine("Buscamos el id_estado.");
			System.out.println("Buscamos el id_estado. \r\n");
			idEstadoCuenta = oraResp.oraOneQuery(report, "select id_estado from cuentas where id_cuenta = " + nroCuenta, configEntidad);
			
			if (idEstadoCuenta.equals("0")) {
				report.AddLine("Estado de la cuenta correcto.");
				System.out.println("##[section] : Estado de la cuenta correcto. \r\n");
			} else {
			report.AddLineAssertionError("Estado de la cuenta incorrecto. Se esperaba 0 pero se recibio :"+idEstadoCuenta);
				System.out.println("##[warning] : Estado de la cuenta incorrecto. Se esperaba 0 pero se recibio :"+idEstadoCuenta+ "\r\n");
			}
						
			
		
			//ROLLBACK
			// Borramos la cuenta
			report.AddLine("Se realiza rollback de la cuenta creada: " + nroCuenta);
			System.out.println("Se realiza rollback de la cuenta creada: " + nroCuenta + "\r\n");
						
			rtaBorrar = oraResp.oraBorrarCuenta(report, nroCuenta, dni, configEntidad, cuentas_generales);
						
						
			if(rtaBorrar) {
				report.AddLine("Eliminacion de los datos exitosa");
				System.out.println("##[section] : Eliminacion de los datos exitosa\r\n");
				
			} else {
				report.AddLineAssertionError("Error al eliminar la cuenta; realizar su eliminacion manual");
				System.out.println("##[warning] : Error al eliminar la cuenta; realizar su eliminación manual\r\n");
							
				result = false;
			}
			
			
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
			
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
              + "##################################################################################################################################################################################################################\r\n");
			
			
		} catch(Exception e) {
			e.printStackTrace();
			report.AddLineAssertionError(e.getMessage());	
			
			
            //Se agrega de nuevo ROLLBACK para que no quede la base inconsistente si falla el TestCase  
			
			//SET VARIABLES 
			String dni = "34293772";
			String nroCuenta = ""; 
			boolean rtaBorrar = false;
			dbWorker oraResp = new dbWorker();
			

			
			//ROLLBACK
			// Borramos la cuenta
			report.AddLine("Se realiza rollback de la cuenta creada: " + nroCuenta);
			System.out.println("Se realiza rollback de la cuenta creada: " + nroCuenta + "\r\n");
						
			rtaBorrar = oraResp.oraBorrarCuenta(report, nroCuenta, dni, configEntidad, cuentas_generales);
						
						
			if(rtaBorrar) {
				report.AddLine("Eliminacion de los datos exitosa");
				System.out.println("##[section] : Eliminacion de los datos exitosa\r\n");
				
			} else {
				report.AddLineAssertionError("Error al eliminar la cuenta; realizar su eliminacion manual");
				System.out.println("##[warning] : Error al eliminar la cuenta; realizar su eliminación manual\r\n");				
			}

			result = false;
		}
		//Try to erase the driver
		try {
			driver.quit();
		} catch(Exception e2) {
			//return the test result
		}
		return result;
	}
}