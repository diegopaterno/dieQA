package globalBatch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.Acceso;
import Pasos.Generales.NavSuperiorBatch;
import Pasos.GlobalBatch.TC_07_BA_PASOS;
import Repositories.Repo_Variables;
import Repositories.Repo_WebRepository;
import apis.wolfgang.API_OPERACIONES;
import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;

public class GlobalBatchEndToEnd {
	
	

	private String esquema;
	WebDriver driver;
	
	static DriverManager DM;
	static Datasources data;
	static Reports report;
	static Repo_Variables repoVar;
	static String path;
	static String path_2;
	static String configEntidad;
	static String cuentas_generales;
	public boolean altaCuentaEmbozado(Datasources data, Reports report, DriverManager DM,int iteration, String name, String configEntidad, String entidad, String producto, String baseURL, String TCFilesPath, String endPoint, String statusCodeEsperado) throws InterruptedException {
		                             //Datasources data,Reports report, DriverManager DM,int iteration, String name, String configEntidad, String entidad, String TCFilesPath, String cuentaNumero, String DE22, String DE2, String ambiente  
		boolean result = true;  
		
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
		
		 
		//generar numero aleatorio para ingresar a la web y generar un cuil para alta
		
		try {
			String numeroDNI = generarNumero();
			Thread.sleep(5000);
			//generar un cuil nuevo para alta de cuenta
			String numeroCuil = calcularCuit(numeroDNI);
			Thread.sleep(5000);
			//String numeroCuilSinGuion = eliminarGuionMedio(numeroCuil); 
			String numeroCuilSinGuion = quitarGuiones(numeroCuil);
			System.out.println("numero final : " + numeroCuilSinGuion);
			//dar de alta una cuenta fisica
			API_OPERACIONES API_OPERACIONES = new API_OPERACIONES();
			API_OPERACIONES.altaCuentaFisicaSinRollBack(report, name, configEntidad, entidad, TCFilesPath, endPoint, statusCodeEsperado, numeroDNI, numeroCuilSinGuion);
			Thread.sleep(5000);
			//ingresar a GB y ejecutar proceso de embozado
			GlobalBatch globalBatch = new GlobalBatch();
			globalBatch.procesoT1025UAT(data, report, DM, 0, name, configEntidad);
			Thread.sleep(5000);
			//tomar archivo de salida
			obteneArchivoT1025();
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	@Step("PASO 1: genero un DNI para ingresar un cuil nuevo siempre y que no falle el TC")
	public static String generarNumero() {
        // Generar los primeros 5 dígitos aleatoriamente
        Random rand = new Random();
        int primerosCincoDigitos = rand.nextInt(90000) + 10000;
        System.out.println("este es el primer numero de salida : " + primerosCincoDigitos);
        // Generar los últimos 3 dígitos de forma que sean distintos
        int ultimoTresDigitos = rand.nextInt(900) + 100;
        while (tieneDigitosRepetidos(ultimoTresDigitos)) {
            ultimoTresDigitos = rand.nextInt(900) + 100;
        }

        // Construir el número final
        String numero = Integer.toString(primerosCincoDigitos) + ultimoTresDigitos;
        System.out.println("este es el primer numero de salida : " + numero);
        return numero;
    }

    public static boolean tieneDigitosRepetidos(int numero) {
        // Convertir el número en una cadena
        String numeroStr = Integer.toString(numero);

        // Verificar si hay dígitos repetidos
        for (int i = 0; i < numeroStr.length(); i++) {
            for (int j = i + 1; j < numeroStr.length(); j++) {
                if (numeroStr.charAt(i) == numeroStr.charAt(j)) {
                    return true;
                }
            }
        }
        return false;
    }
    @Step("PASO 2: en esta paso generamos un nro de CUIL")
    public String calcularCuit(String numero) {
        // Establecer la propiedad del sistema para el controlador de Chrome
       // System.setProperty("webdriver.chrome.driver", "/ruta/al/controlador/chromedriver");

        // Inicializar el WebDriver
       // WebDriver driver = new ChromeDriver();

        // Abrir la página web
        driver.get("http://www0.unsl.edu.ar/~jolguin/cuit.php?tipo=Varones&dni=3299008&cuit=20-3299009-9&Calcula=Calcula");

        try {
            // Encontrar el campo de entrada donde escribir el número
           /// WebElement inputNumero = driver.findElement(By.id("//input[@name='dni']"));
            WebElement inputNumero = driver.findElement(By.xpath("//input[@name='dni']"));
            Thread.sleep(5000);
            inputNumero.clear();
            Thread.sleep(5000);
            // Escribir el número en el campo de entrada
            inputNumero.sendKeys(numero);
            Thread.sleep(5000);
            // Encontrar y hacer clic en el botón de calcular
            WebElement botonCalcular = driver.findElement(By.xpath("//input[@value='Calcula']"));
            Thread.sleep(5000);
            botonCalcular.click();
            Thread.sleep(5000);
            // Esperar un poco para que se procese la acción
           

            // Encontrar el campo de entrada donde está el resultado
            WebElement inputResultado = driver.findElement(By.xpath("//input[@name='cuit']"));
            Thread.sleep(5000);
            // Obtener el texto del campo de entrada que contiene el resultado
            String resultado = inputResultado.getAttribute("value");
            Thread.sleep(5000);
            resultado.replace("-", "");
            Thread.sleep(5000);
            System.out.println("este va a ser el cuil a utilizar en el body : " + resultado);
            // Cerrar el navegador
            driver.quit();

            // Devolver el resultado obtenido
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String eliminarGuionMedio(String numero) {
        // Eliminar el guion medio de la cadena
    	numero.replace("-", "");
    	numero.replace("-", "");
    	//numero.split(numero)
    	System.out.println("numero final : " + numero);
        return numero;
        
    }
    public static String quitarGuiones(String numero) {
        StringBuilder sb = new StringBuilder();
        
        // Iterar sobre cada carácter de la cadena
        for (int i = 0; i < numero.length(); i++) {
            char c = numero.charAt(i);
            
            // Si el carácter no es un guion, agregarlo al StringBuilder
            if (c != '-') {
                sb.append(c);
            }
        }
        
        // Convertir el StringBuilder a una cadena y devolverla
        return sb.toString();
    }

    @Step("PASO 5: OBTENER ARCHIVO DE SALIDA PARA VALIDAR DATOS")
    public boolean obteneArchivoT1025() {
    	boolean result = true;
    	try {
            // Paso 1: Crear la conexión
            //URL url = new URL("http://10.77.2.16"); // Especifica el protocolo http://
            URL url = new URL("http://10.80.0.175"); // Especifica el protocolo http://

            // Paso 2: Verificar la conexión
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Paso 3: Crear el InputStream
                InputStream inputStream = connection.getInputStream();
                System.out.println("La conexión fue EXITOSA!! NO NO FUE rechazada.");
               //                      \\GPWF-AQ-QA-01\GlobalBatch\Automation\IPM_Entrante
                String filePath = "\\\\GPWF-AQ-UAT-01\\GlobalBatch\\Automation\\Embozado Tarjetas"; 
                //E:\\App\GlobalBatch\Automation\Embozado Tarjetas
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
                String rutaArchivoDestino = "./TC_Datos/t1025d.txt";

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
    	return result;
    }
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
   /* public static String generarCuil() {
        // Generar el número como se explicó anteriormente
        return "tu_numero_generado";
    }*/
}
