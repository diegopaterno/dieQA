package Pasos.GlobalBatch;

import java.util.ArrayList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Repositories.Repo_WebRepository;
import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;

public class TC_07_BA_PASOS {
	WebDriver driver;

	public void pagina1(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
		
		String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCH.descripcion");	

		report.AddLine("Se carga el nombre del proceso en el input: Presentaciones Mastercard");
		System.out.println("Se carga el nombre del proceso en el input: Presentaciones Mastercard\r\n");
		repo.get_obj_inputById("nombre_proceso").sendKeys("Presentaciones Mastercard");
		
		report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
		System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
		repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

		report.AddLine("Screenshoot de pagina");
		System.out.println("Screenshoot de pagina\r\n");
		report.Screenshot(name);
		
		report.AddLine("Se hace click en el boton Buscar");
		System.out.println("Se hace click en el boton Buscar\r\n");
		repo.get_obj_buscar().click();
		
		Thread.sleep(1000);
		
		report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
		System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
		repo.get_obj_lanzar().click();
		
		
	}
	@Step("Se ejecuta el proceso de generacion de archivo T2001 Transacciones Diarias")
public void pagina1t2001(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
		
		String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCHUAT.descripcion");	

		report.AddLine("Se carga el nombre del proceso en el input: T2001");
		System.out.println("Se carga el nombre del proceso en el input: T2001\r\n");
		repo.get_obj_inputById("nombre_proceso").sendKeys("T2001");
		
		report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
		System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
		repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

		report.AddLine("Screenshoot de pagina");
		System.out.println("Screenshoot de pagina\r\n");
		report.Screenshot(name);
		
		report.AddLine("Se hace click en el boton Buscar");
		System.out.println("Se hace click en el boton Buscar\r\n");
		repo.get_obj_buscar().click();
		
		Thread.sleep(2000);
		
		report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
		System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
		repo.get_obj_lanzar().click();
		
		
	}
	
	/**************************PRESENTACION PARA LA 632, ENTIDAD MASTERCARD EN UAT**********/
public void paginaUAT(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
		
		String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCHUAT.descripcion");	

		report.AddLine("Se carga el nombre del proceso en el input: Presentaciones Mastercard");
		System.out.println("Se carga el nombre del proceso en el input: Presentaciones Mastercard\r\n");
		repo.get_obj_inputById("nombre_proceso").sendKeys("Presentaciones Mastercard");
		
		report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
		System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
		repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

		report.AddLine("Screenshoot de pagina");
		System.out.println("Screenshoot de pagina\r\n");
		report.Screenshot(name);
		
		report.AddLine("Se hace click en el boton Buscar");
		System.out.println("Se hace click en el boton Buscar\r\n");
		repo.get_obj_buscar().click();
		
		Thread.sleep(1000);
		
		report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
		System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
		repo.get_obj_lanzar().click();
		
		
	}
@Step("Se genera el proceso de presentaciones")
public void pagina1Presentacion(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCHUAT.descripcion");	

	report.AddLine("Se carga el nombre del proceso en el input: Presentaciones Mastercard");
	System.out.println("Se carga el nombre del proceso en el input: Presentaciones Mastercard\r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("Presentaciones Mastercard");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
	
	/***********************************************************************/
	/********************************credito*********************************/
	
public void pagina1Credito(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
		
		String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCH.credito");	

		report.AddLine("Se carga el nombre del proceso en el input: Presentaciones Mastercard");
		System.out.println("Se carga el nombre del proceso en el input: Presentaciones Mastercard\r\n");
		repo.get_obj_inputById("nombre_proceso").sendKeys("Presentaciones Mastercard");
		
		report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
		System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
		repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

		report.AddLine("Screenshoot de pagina");
		System.out.println("Screenshoot de pagina\r\n");
		report.Screenshot(name);
		
		report.AddLine("Se hace click en el boton Buscar");
		System.out.println("Se hace click en el boton Buscar\r\n");
		repo.get_obj_buscar().click();
		
		Thread.sleep(1000);
		
		report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
		System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
		repo.get_obj_lanzar().click();
		
		
	}

	/*****************presentacion para banco alfin***********************/
public void pagina1LemonQA(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCH.lemonQA");	

	report.AddLine("Se carga el nombre del proceso en el input: Presentaciones Visa");
	System.out.println("Se carga el nombre del proceso en el input: Presentaciones Visa\r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("Presentaciones Visa");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
	
	/***********************************************************************/
/*****************presentacion para banco alfin UAT***********************/
public void pagina1LemonUAT(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCHUAT.lemonUAT");	

	report.AddLine("Se carga el nombre del proceso en el input: Presentaciones Visa");
	System.out.println("Se carga el nombre del proceso en el input: Presentaciones Visa\r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("Presentaciones Visa");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
	
	/***********************************************************************/
/*****************presentacion para banco alfin UAT***********************/
public void procesoT1001UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCHUAT.lemonUAT");	

	report.AddLine("Se carga el nombre del proceso en el input: T1001 - Novedades Socios ");
	System.out.println("Se carga el nombre del proceso en el input: T1001 - Novedades Socios\r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("T1001");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
public void procesoT1001QA(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCH.lemonQA");	

	report.AddLine("Se carga el nombre del proceso en el input: T1001 - Novedades Socios ");
	System.out.println("Se carga el nombre del proceso en el input: T1001 - Novedades Socios\r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("T1001");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}

public void procesoT1010UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCHUAT.lemonUAT");	

	report.AddLine("Se carga el nombre del proceso en el input: T1010 - Padron Socios ");
	System.out.println("Se carga el nombre del proceso en el input: T1010 - Padron Socios\r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("Padron");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
public void procesoT1010QA(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCH.lemonQA");	

	report.AddLine("Se carga el nombre del proceso en el input: T1010 - Padron Socios ");
	System.out.println("Se carga el nombre del proceso en el input: T1010 - Padron Socios\r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("Padron");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
public void procesoT1027UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCHUAT.lemonUAT");	

	report.AddLine("Se carga el nombre del proceso en el input: T1027 - Embozado de Tarjetas ");
	System.out.println("Se carga el nombre del proceso en el input: T1027 - Embozado de Tarjetas\r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("Embozado");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
public void procesoT1027QA(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCH.lemonQA");	

	report.AddLine("Se carga el nombre del proceso en el input: T1027 - Embozado de Tarjetas ");
	System.out.println("Se carga el nombre del proceso en el input: T1027 - Embozado de Tarjetas\r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("Embozado");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
public void procesoT7001UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCHUAT.lemonUAT");	

	report.AddLine("Se carga el nombre del proceso en el input: T7001 - AUTORIZACIONES ");
	System.out.println("Se carga el nombre del proceso en el input: T7001 - AUTORIZACIONES\r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("Autorizaciones");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
public void procesoT7001QA(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCH.lemonQA");	

	report.AddLine("Se carga el nombre del proceso en el input: T7001 - AUTORIZACIONES ");
	System.out.println("Se carga el nombre del proceso en el input: T7001 - AUTORIZACIONES\r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("Autorizaciones");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
public void procesoT2001UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCHUAT.lemonUAT");	

	report.AddLine("Se carga el nombre del proceso en el input: T2001 - TRANSACCIONES DIARIAS ");
	System.out.println("Se carga el nombre del proceso en el input: T2001 - TRANSACCIONES DIARIAS\r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("Transacciones");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
public void procesoT2001QA(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCH.lemonQA");	

	report.AddLine("Se carga el nombre del proceso en el input: T2001 - TRANSACCIONES DIARIAS ");
	System.out.println("Se carga el nombre del proceso en el input: T2001 - TRANSACCIONES DIARIAS\r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("Transacciones");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
public void procesoT3022UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCHUAT.lemonUAT");	

	report.AddLine("Se carga el nombre del proceso en el input: T3022 - Procesos Ajustes / Cobranzas ");
	System.out.println("Se carga el nombre del proceso en el input: T3022 - Procesos Ajustes / Cobranzas \r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("T3022");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
public void procesoT3022QA(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCH.lemonQA");	

	report.AddLine("Se carga el nombre del proceso en el input: T3022 - Procesos Ajustes / Cobranzas ");
	System.out.println("Se carga el nombre del proceso en el input: T3022 - Procesos Ajustes / Cobranzas \r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("T3022");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
public void procesoT2000UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCHUAT.lemonUAT");	

	report.AddLine("Se carga el nombre del proceso en el input: T2000 - TOTAL COMPENSACIONES ");
	System.out.println("Se carga el nombre del proceso en el input: T2000 - TOTAL COMPENSACIONES \r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("Totales");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
public void procesoT2000QA(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCH.lemonQA");	

	report.AddLine("Se carga el nombre del proceso en el input: T2000 - TOTAL COMPENSACIONES ");
	System.out.println("Se carga el nombre del proceso en el input: T2000 - TOTAL COMPENSACIONES \r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("Totales");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
public void procesoT3052UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCHUAT.lemonUAT");	

	report.AddLine("Se carga el nombre del proceso en el input: T3052 - CARGOS DIARIOS ");
	System.out.println("Se carga el nombre del proceso en el input: T3052 - CARGOS DIARIOS \r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("T3052");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
public void procesoT1025UAT(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCHUAT.descripcion");	

	report.AddLine("Se carga el nombre del proceso en el input: T1025 - EMBOZADO DE TARJETAS ");
	System.out.println("Se carga el nombre del proceso en el input: T1025 - EMBOZADO DE TARJETAS \r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("EMBOZADO");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
public void procesoT3052QA(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
	
	String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCH.lemonQA");	

	report.AddLine("Se carga el nombre del proceso en el input: T3052 - CARGOS DIARIOS ");
	System.out.println("Se carga el nombre del proceso en el input: T3052 - CARGOS DIARIOS \r\n");
	repo.get_obj_inputById("nombre_proceso").sendKeys("T3052");
	
	report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
	System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
	repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se hace click en el boton Buscar");
	System.out.println("Se hace click en el boton Buscar\r\n");
	repo.get_obj_buscar().click();
	
	Thread.sleep(1000);
	
	report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
	System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
	repo.get_obj_lanzar().click();
	
	
}
	
	/***********************************************************************/
	
public void pagina11(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo, String configEntidad) throws InterruptedException {
		
		String descripcionEntidad = JsonPath.from(configEntidad).get("GBATCH.credito");	

		report.AddLine("Se carga el nombre del proceso en el input: Presentaciones Mastercard");
		System.out.println("Se carga el nombre del proceso en el input: Presentaciones Mastercard\r\n");
		repo.get_obj_inputById("nombre_proceso").sendKeys("Presentaciones Mastercard");
		
		report.AddLine("Se seleciona la entidad correspondiente: " + descripcionEntidad);
		System.out.println("Se seleciona la entidad correspondiente: " + descripcionEntidad + "\r\n");
		repo.get_obj_selectOptionById("entidades", descripcionEntidad).click();

		report.AddLine("Screenshoot de pagina");
		System.out.println("Screenshoot de pagina\r\n");
		report.Screenshot(name);
		
		report.AddLine("Se hace click en el boton Buscar");
		System.out.println("Se hace click en el boton Buscar\r\n");
		repo.get_obj_buscar().click();
		
		Thread.sleep(1000);
		
		report.AddLine("Se hace click en el boton (icono) Lanzar proceso");
		System.out.println("Se hace click en el boton (icono) Lanzar proceso\r\n");
		repo.get_obj_lanzar().click();
		
		
	}

/***********************************************************************/
public void pagina2Fecha(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo) throws InterruptedException {

	
	
	
	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	report.Screenshot(name);
	
	report.AddLine("Se coloca la fecha del consumo");
	System.out.println("Se coloca la fecha del consumo\r\n");
	
	
	report.AddLine("Se hace click en el boton LANZAR");
	System.out.println("Se hace click en el boton LANZAR\r\n");
	repo.get_obj_buttonById("boton_confirmacion_lanzamiento").click();
	//repo.input_fecha_de_proceso().sendKeys("20240322");
	
	report.AddLine("Screenshoot de pagina");
	System.out.println("Screenshoot de pagina\r\n");
	Thread.sleep(300);
	report.Screenshot(name);
}


	
	/***********************************************************************/
	public void pagina2(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo) throws InterruptedException {

		
		report.AddLine("Screenshoot de pagina");
		System.out.println("Screenshoot de pagina\r\n");
		report.Screenshot(name);
		
		report.AddLine("Se hace click en el boton LANZAR");
		System.out.println("Se hace click en el boton LANZAR\r\n");
		repo.get_obj_buttonById("boton_confirmacion_lanzamiento").click();
		
		report.AddLine("Screenshoot de pagina");
		System.out.println("Screenshoot de pagina\r\n");
		Thread.sleep(300);
		report.Screenshot(name);
	}
	
	/**********************************************************/
	
public void pagina22(DriverManager DM, Repo_WebRepository repo) throws InterruptedException {

		
		//report.AddLine("Screenshoot de pagina");
		System.out.println("Screenshoot de pagina\r\n");
		//report.Screenshot(name);
		
		//report.AddLine("Se hace click en el boton LANZAR");
		System.out.println("Se hace click en el boton LANZAR\r\n");
		repo.get_obj_buttonById("boton_confirmacion_lanzamiento").click();
		
		//report.AddLine("Screenshoot de pagina");
		System.out.println("Screenshoot de pagina\r\n");
		Thread.sleep(300);
		//report.Screenshot(name);
	}
	
	
	
	public void pagina3(Datasources data,Reports report, DriverManager DM, int iteration,String name,Repo_WebRepository repo) throws InterruptedException {
				
		report.AddLine("Se hace click en el boton CONFIRMAR");
		System.out.println("Se hace click en el boton CONFIRMAR\r\n");
		repo.get_obj_buttonById("boton_lanzamiento").click();
		//Espero algunos segundos para sacar foto de la pantalla principal
		Thread.sleep(25000);
		report.Screenshot(name);
	}
	/*******************************************************************/
	
	public void pagina33(DriverManager DM, Repo_WebRepository repo) throws InterruptedException {
		
		//report.AddLine("Se hace click en el boton CONFIRMAR");
		System.out.println("Se hace click en el boton CONFIRMAR\r\n");
		repo.get_obj_buttonById("boton_lanzamiento").click();
		//Espero algunos segundos para sacar foto de la pantalla principal
		Thread.sleep(2000);
		//report.Screenshot(name);
	}

}