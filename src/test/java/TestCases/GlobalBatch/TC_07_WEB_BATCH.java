package TestCases.GlobalBatch;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.mozilla.javascript.regexp.SubString;
import org.openqa.selenium.WebDriver;
import CentaJava.Core.Datasources;
import CentaJava.Core.DriverManager;
import CentaJava.Core.Reports;
import Pasos.Generales.Acceso;
import Pasos.Generales.NavSuperiorBatch;
import Pasos.Generales.PrePostCondi;
import Pasos.GlobalBatch.TC_07_BA_PASOS;
import Pasos.Generales.GBatch;
import Repositories.Repo_WebRepository;
import Tools.dbWorker;
import io.restassured.path.json.JsonPath;

public class TC_07_WEB_BATCH {
	WebDriver driver;

	public boolean Test(Datasources data,Reports report, DriverManager DM, int iteration,String name, String configEntidad, String cuentas_generales) {
		//validation 
		boolean result = true;
		try {	
			//Separador
			System.out.println("\r\n##################################################################################################################################################################################################################"
							 + "##################################################################################################################################################################################################################\r\n");
			
			System.out.println("Configuring TC_07_WEB_BATCH\r\n");

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
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			GBatch batch = new GBatch();
			
			// SET VARIABLES
			String[] rtaParam = new String[2];			
			// Verificacion de procesamiento de registros
			String registrosOK = "1"; // Cantidad de registros correctos esperados	
			String verifQuery = "";	
			// PRESENTACION_PROCESADA = 2 en la tabla AUTORIZACION
			String PRESENTACION_PROCESADA = "2";
			String rtaAutorizacion = "";		
			String idIpmFileMax = "";
			String idAutoEmisorLogMax = "";
			// Datos esperados en cuenta moneda
			String SALDO = "630";
			String DISPONIBLE = "630";
			String IMPORTE_COMPRAS = "370";
			String IMPORTE_PEND_COMPRAS = "0";		
			//Datos de la cuenta que se obtienen del archivo json
			String NRO_TARJETA_ENCRIPTADA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.nro_tarjeta_encriptada");
			String NRO_TARJETA_ENMASCARADA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.nro_tarjeta_enmascarada");
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			String ID_ENTIDAD = JsonPath.from(configEntidad).get("ENTIDAD.id_entidad");			
			//Obtengo la URL de GO desde la variable configEntidad que contiene todos los accesos a la entidad
			String URL_GBATCH = JsonPath.from(configEntidad).get("GBATCH.url_gbatch");
			String fechaAutorizacionIPM = "";
			int rtaPrePostCondiciones = 0;
			boolean respuestaVerificacion;
						
			System.out.println("\r\n##[command] : ------ Initializating test: " + name + " ------\r\n");		
			
			//PRECONDICIONES ESQUEMA DE LA ENTIDAD
			report.AddLine("--- Ejecutando PreCondicion ---");
			System.out.println("--- Ejecutando PreCondicion ---\r\n");
			
			//Se eliminan los registros de la tabla IPM para no tener conflicto y posteriormente insertar un registro
			
			report.AddLine("Se eliminan registros en la tablas IPM e IPM_FILE");
			System.out.println("Se eliminan registros en la tablas IPM e IPM_FILE\r\n");
			
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE IPM WHERE ID_IPM_FILE IN (SELECT ID_IPM_FILE FROM IPM_FILE  WHERE FILE_ID = '0022108040000001180300002')", configEntidad);
			ppCondi.preCondicionBD(report, rtaPrePostCondiciones);		
			
			//Se eliminan los registros de la tabla IPM_FILE para no tener conflicto y posteriormente insertar un registro
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE IPM_FILE WHERE FILE_ID = '0022108040000001180300002'", configEntidad);
			ppCondi.preCondicionBD(report, rtaPrePostCondiciones);
			
			//Se insertan registros en la tabla IPM_FILE para obtener el ID_IPM_FILE para luego utilizarlo en el insert de IPM
			report.AddLine("Se realiza precondicion en tabla IPM_FILE ingresando un registro para luego ser procesado.");
			System.out.println("Se realiza precondicion en tabla IPM_FILE ingresando un registro para luego ser procesado.\r\n");
			
			rtaPrePostCondiciones = oraResp.oraInsert(report, "Insert into IPM_FILE (ID_ENTIDAD,FILE_ID,TIPO_ARCHIVO,NOMBRE_ARCHIVO,MENSAJES_INFORMADOS,MENSAJES_ACEPTADOS,ID_AUDITORIA,ROW_VERSION,ROW_FECHA,ID_ESTADO_TIPO_CONFIRM,ID_ESTADO_CONFIRM,ID_ESTADO_TIPO,ID_ESTADO,FECHA_CARGA) \r\n"
					+ "values ('" + ID_ENTIDAD + "','0022108040000001180300002','IN','TT112T0.2021-08-06-06-50-11_000.ipm','3',null,null,null,null,'40',null,'67','2',to_date('13/10/2021 14:22:55','DD/MM/RRRR HH24:MI:SS'))", configEntidad);
			ppCondi.preCondicionBD(report, rtaPrePostCondiciones);
			
			idIpmFileMax = oraResp.oraOneQuery(report, "SELECT MAX (ID_IPM_FILE) FROM IPM_FILE", configEntidad);
			
			report.AddLine("Se realiza precondicion en tabla IPM ingresando un registro para luego ser procesado.");
			System.out.println("Se realiza precondicion en tabla IPM ingresando un registro para luego ser procesado.\r\n");	
			
			//Obtengo la fecha actual
			LocalDate fechaActual = LocalDate.now();

			//Grabo la fecha actual como string y saco espacios
			fechaAutorizacionIPM = fechaActual.toString().replace(" ", "");
			
					
			//Separo la fecha por los guiones para separar el anio, mes y dia
			String[] fechaSeparada = fechaAutorizacionIPM.split("-");
				
			fechaAutorizacionIPM = fechaSeparada[0].replace("20", "") + fechaSeparada[1] + fechaSeparada[2] + "000000";
			
			report.AddLine("Fecha para inrgesar en el registro IPM: " + fechaAutorizacionIPM);
			System.out.println("Fecha para inrgesar en el registro IPM: " + fechaAutorizacionIPM + "\r\n");
			
			//Se inserta un registro en la tabla IPM para ser procesado y posteriormente eliminado para no generar datos basura en el esquema.
			rtaPrePostCondiciones = oraResp.oraInsert(report, "Insert into IPM (ID_ENTIDAD,ID_CONSUMO,ID_IPM_ORIGEN,ID_IPM_FILE,TIPO_MENSAJE,NRO_TARJETA,COD_PROCESAMIENTO,IMPORTE_OPERACION,IMPORTE_RECONCILIACION,IMPORTE_EMISOR,TASA_RECONCILIACION,TASA_EMISOR,FECHA_HORA_LOCAL,FECHA_VTO_TARJETA,CONDICION_PUNTO_SERVICIO,NRO_SECUENCIA_TARJETA,CODIGO_FUNCION,CODIGO_MOTIVO,MCC,IMPORTE_ORIGINAL,DATOS_REFERENCIA_ADQUIRIENTE,ICA_ADQUIRIENTE,ICA_REENVIO,NRO_REFER_RECUPERACION,CODIGO_APROBACION,CODIGO_SERVICIO,TERMINAL,COD_COMERCIO,DESCRIPCION_COMERCIO,DATOS_ADICIONALES,COD_MONEDA_OPERACION,COD_MONEDA_RECONCILIACION,COD_MONEDA_EMISOR,MONTOS_ADICIONALES,DATOS_ICC,DATOS_ADICIONALES2,CICLO_VIDA_TRANSACCION,NUMERO_MENSAJE,DATO_REGISTRO,FECHA_ACCION,ICA_ENTIDAD_DESTINO,ICA_ENTIDAD_ORIGEN,DATOS_REFERENCIA_EMISOR,ENTIDAD_RECEPTOR,IMPORTE_CONVERSION_MONEDA,DATOS_ADICIONALES3,DATOS_ADICIONALES4,DATOS_ADICIONALES5,FECHA_LIMITE,ID_ESTADO_TIPO,ID_ESTADO,ID_AUDITORIA,ROW_VERSION,ROW_FECHA,ID_ESTADO_TIPO_CONFIRM,ID_ESTADO_CONFIRM,ID_ORIGEN,NRO_CUOTA,CHIP_ADQUIRENTE,CHIP_EMISOR,INTERCHANGE_RATE,UNIT_FEE,TRX_FACE_TO_FACE,TRX_ECOMMERCE,TRX_PAGO,MENSAJE_REVERSO,ID_IPM_REVERSO,PROCESADO,ID_AUTORIZACION,PRESENTACION_PARCIAL,AUTORIZACION_PROCESADA,OBSERVACION,TRX_DEBITO_AUTOMATICO,IMPORTE_INTERCHANGE,CRITERIO_MAPEO,CODIGO_PROMOCION_CASHBACK,ID_IPM_FILE_ORIGEN,COMPENSADO) "
					+ "values ('" + ID_ENTIDAD + "',null,null," + idIpmFileMax + ",'1240','" + NRO_TARJETA_ENCRIPTADA + "','000000','000000037000','000000037000','000000037000','90839057','85168069','" + fechaAutorizacionIPM + "','2311','000040109000',null,'200',null,'7995',null,'05234670168001602681317',null,'001518',null,'307507',null,'23946499','687555537877464','NETFLIX.COM    \\C.  PROLONGACION AVDA MEDI0   \\Stockholm\\04006        NLD','0002003MRG0003003MRG0023003CT60052003910014603600290141000000000235784000000000019601480124100840203220158030MCC10403017520030203    NNNNNN015906718066      000913201000                1US00000001M20030203200302010165001M0177002YY01910012','032','032','032',null,null,null,' MCC0113330824','2',null,null,'018066','001518',null,'018066','000000007614',null,null,null,null,'57','3',null,null,null,'40',null,'8',null,'0',null,null,null,'0','1','0','0',null,'0',null,'0','0',null,'0',null,null,null,null,'0')\r\n"
					+ "", configEntidad);
			
		
			ppCondi.preCondicionBD(report, rtaPrePostCondiciones);
			
			report.AddLine("Se realiza precondicion en tabla IPM para que los otros registros queden como ya procesados.");
			System.out.println("Se realiza precondicion en tabla IPM para que los otros registros queden como ya procesados.\r\n");

			rtaPrePostCondiciones = oraResp.oraUpdate(report, "UPDATE IPM SET PROCESADO = 1 WHERE ID_IPM NOT IN (SELECT MAX (ID_IPM) FROM IPM)", configEntidad); 
			ppCondi.preCondicionBD(report, rtaPrePostCondiciones);
			
			report.AddLine("Se realiza precondicion en tabla CUENTAS_MONEDA de la cuenta " + ID_CUENTA);
			System.out.println("Se realiza precondicion en tabla CUENTAS_MONEDA de la cuenta " + ID_CUENTA + "\r\n");
			
			rtaPrePostCondiciones = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA\r\n"
					+ "SET SALDO = 1000, DISPONIBLE = 630, DISPONIBLE_ADELANTO = 1000,\r\n"
					+ "IMPORTE_COMPRAS = 0, IMPORTE_ADELANTOS = 0, IMPORTE_PEND_COMPRAS = 370, \r\n"
					+ "IMPORTE_PEND_ADELANTOS = 0, IMPORTE_AJUSTES = 0, IMPORTE_AJUSTES_PEND = 0,\r\n"
					+ "IMPORTE_PAGOS = 1000\r\n"
					+ "WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_MONEDA IN (32)", configEntidad);			
			ppCondi.preCondicionBD(report, rtaPrePostCondiciones);
			
			report.AddLine("Se eliminan registros en la tablas AUTORIZACION que tengan el TID = 'MCC0113330824  '");
			System.out.println("Se eliminan registros en la tablas AUTORIZACION que tengan el TID = 'MCC0113330824  '\r\n");
			
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE AUTORIZACION WHERE TID = 'MCC0113330824  '", configEntidad);
			ppCondi.preCondicionBD(report, rtaPrePostCondiciones);		
			
			report.AddLine("Se realiza precondicion en tabla AUTORIZACION_EMISOR_LOG ingresando un registro de la cuenta " + ID_CUENTA + " para luego ser procesado.");
			System.out.println("Se realiza precondicion en tabla AUTORIZACION_EMISOR_LOG ingresando un registro de la cuenta " + ID_CUENTA + " para luego ser procesado.\r\n");
			
			//Se inserta un registro en la tabla AUTORIZACION para ser procesado y posteriormente eliminado para no generar datos basura en el esquema.
			rtaPrePostCondiciones = oraResp.oraInsert(report, "Insert into AUTORIZACION_EMISOR_LOG (ID_AUTORIZACION_EMISOR,ID_ENTIDAD,IN_ID_ORIGEN,IN_ID_TIPO_RED,IN_TIPO_MENSAJE,IN_NRO_TARJETA2,IN_COD_PROCESAMIENTO,IN_IMPORTE_OPERACION,IN_IMPORTE_LIQUIDACION,IN_IMPORTE_EMISOR,IN_FECHA_HORA_TRANSMISION,IN_TASA_LIQUIDACION,IN_TASA_EMISOR,IN_NRO_TRACE,IN_HORA_LOCAL,IN_FECHA_LOCAL,IN_FECHA_VTO_TARJETA,IN_FECHA_LIQUIDACION,IN_FECHA_CONVERSION,IN_MCC,IN_MODO_INGRESO_TERMINAL,IN_NRO_SECUENCIA_TARJETA,IN_COD_CAPTURA_PIN_TERMINAL,IN_IMPORTE_CARGO_TRANSACCION,IN_ICA,IN_ICA_REENVIO,IN_TRACK2,IN_NRO_REFER_RECUPERACION,IN_COD_AUTORIZACION,IN_TERMINAL,IN_COD_COMERCIO_EXTERNO,IN_COMERCIO_43,IN_TRACK1,IN_DATOS_ADICIONALES_048,IN_COD_MONEDA_OPERACION,IN_COD_MONEDA_LIQUIDACION,IN_COD_MONEDA_EMISOR,IN_IMPORTES_ADICIONALES_54,IN_DATOS_ICC,IN_CODIGO_MOTIVO_AVISO_60,IN_DATOS_PUNTO_SERVICIO,IN_DATOS_RED_INTERMEDIARIA,IN_DATOS_RED,IN_DATOS_MENSAJE_ORIGINAL,IN_IMPORTE_REEMPLAZO,IN_NRO_CUENTA1,IN_NRO_CUENTA2,IN_DATOS_REFERENCIA_MONEYSEND,IN_DATOS_ADICIONALES_112,IN_DATOS_REGISTRO,IN_COD_PROCESO_STANDIN,IN_DATOS_MIEMBRO,IN_DATOS_INICIADOR_MENSAJE,IN_NRO_TICKET,IN_PLAN_EXTERNO,IN_CUOTAS,OUT_COD_AUTORIZACION,OUT_COD_RESPUESTA,OUT_DATOS_ADICIONALES_44,OUT_DATOS_ADICIONALES_48,OUT_IMPORTES_ADICIONALES_54,OUT_DATOS_ADICIONALES_112,OUT_MENSAJE_TICKET,OUT_DATOS_MIEMBRO,OUT_ID_RESPUESTA_INTERNA,OUT_OBSERVACION,FECHA_TRANSACCION_INI,FECHA_TRANSACCION_FIN,ID_TIPO_AUTORIZACION_EMISOR,PRESENTACION_PROCESADA,ID_CONSULTA_ORIGINAL,ID_AUTORIZACION_ORIGINAL,ANULAR_REVERSAR,ID_AUTO_EMI_ANULAR_REVERSAR,ID_AUTO_EMI_ORIGEN,PRODUCTO,TIPO_PRODUCTO,INGRESO_PIN,PIN_VALIDO,CVC_BANDA_VALIDO,CVC_EMBOZADO_VALIDO,EMV_VALIDO,EXPONENTE_IMPORTE_OPERACION,EXPONENTE_IMPORTE_LIQUIDACION,EXPONENTE_IMPORTE_EMISOR,CODIGO_SERVICIO,IN_CODIGO_RED_61,IN_DATOS_MENSAJE_ORIGINAL_62,IN_FECHA_CAPTURA,IN_DATOS_ADICIONALES_63,OUT_DATOS_ADICIONALES_63,OUT_PIN,OUT_NRO_REFER_RECUPERACION,OUT_COD_MONEDA_LIQUIDACION,OUT_COD_MONEDA_OPERACION,OUT_IMPORTE_LIQUIDACION,OUT_IMPORTE_OPERACION,IN_COD_PAIS_CUENTA_PRIMARIA,IN_FORZAR,IN_NRO_TARJETA,CVC_CHIP_VALIDO,ID_MARCA,IN_COD_RESPUESTA,IN_ESTADO_SENTINEL,OFFSET) \r\n"
					+ "values (null,'" + ID_ENTIDAD + "','12','1','0100',null,'000000','000000037000','000000037000','000000037000','0824103339','61000000','61000000','000005','103339','0824','2410','0824','0824','7995','010',null,null,null,'009685',null,null,null,null,null,'687555537877464','Comercio Argentina                   ARG',null,'U','032','032','032',null,null,null,'203000000010084090210',null,'MCC011333',null,null,null,null,null,null,null,null,null,null,null,null,'1','307507','00',null,null,null,null,null,null,'20000','Operacion aprobada',to_timestamp('24/08/2021 10:33:39,000000000 AM','DD/MM/RRRR HH12:MI:SSXFF AM'),to_timestamp('13/10/2021 03:25:16,553547000 PM','DD/MM/RRRR HH12:MI:SSXFF AM'),'1','0',null,null,null,null,null,null,'P',null,'0','1','1','99','2','2','2',null,null,null,null,'MCC011333','MCC011333',null,null,null,null,null,null,'032','0','" + NRO_TARJETA_ENMASCARADA + "',null,'1',null,'-1',null)", configEntidad);
			ppCondi.preCondicionBD(report, rtaPrePostCondiciones);
			
			
			idAutoEmisorLogMax = oraResp.oraOneQuery(report, "SELECT MAX(ID_AUTORIZACION_EMISOR)\r\n"
					+ "FROM AUTORIZACION_EMISOR_LOG WHERE IN_NRO_TARJETA = '" + NRO_TARJETA_ENCRIPTADA + "'", configEntidad);
			
			report.AddLine("Se realiza precondicion en tabla AUTORIZACION ingresando un registro de la cuenta " + ID_CUENTA + " para luego ser procesado.");
			System.out.println("Se realiza precondicion en tabla AUTORIZACION ingresando un registro de la cuenta " + ID_CUENTA + " para luego ser procesado.\r\n");
			
			//Se inserta un registro en la tabla AUTORIZACION para ser procesado y posteriormente eliminado para no generar datos basura en el esquema.
			rtaPrePostCondiciones = oraResp.oraInsert(report, "Insert into AUTORIZACION (ID_ENTIDAD,NRO_TARJETA,COD_AUTO_ENTIDAD,COD_AUTO_EMISOR,ID_MONEDA_ORIGEN,IMPORTE_ORIGEN,FECHA_AUTORIZACION,COTIZACION,ID_MONEDA,IMPORTE,IMPORTE_SIN_DTO,ID_ESTADO_TIPO,ID_ESTADO,FECHA_ANULACION,ID_USUARIO_ANULACION,ID_COMERCIO,COMERCIO_DESCRIPCION,CUOTAS,TASA_SOCIO,IMPORTE_CUOTAS,MCC,ID_RUBRO,ID_SUBRUBRO,TCC,COD_COMERCIO_EXTERNO,TERMINAL_POS,ID_COD_MOVIMIENTO,ID_GRUPO_TRANSACCION,ID_MODELO_TRANSACCION,ID_AUTORIZACION_ADQUIRENTE,ID_CODIGO_DEVOLUCION,IMPORTE_DEVOLUCION,ID_RESPUESTA_MC,FECHA_RELACION,OBSERVACION,MODO_INGRESO,FECHA_INFORMADA,COD_RESPUESTA,ID_RESPUESTA_INTERNA,ID_MOD_FINANC_COMERCIO,ID_PAIS_EMISOR,COD_PAIS_COMERCIO,COD_PAIS_EMISOR,PRESENTACION_PROCESADA,IMPORTE_TOTAL_PRESENTADO,ID_ORIGEN,COEFICIENTE_DIFERENCIA_CAMBIO,ID_CUENTA,ID_ADICIONAL,ID_MONEDA_LIQUIDACION,IMPORTE_LIQUIDACION,ID_AUTORIZACION_EMISOR,COD_SUB_COMERCIO,TRN_EXTERNAL_ID,ID_REGLA_FRAUDE,ID_MARCA,NRO_TARJETA_ENMASCARADA,DEBITO_AUTOMATICO,PORCENTAJE_DEVOLUCION,ECOMMERCE,TID,COD_RESPUESTA_POS,ES_CASHBACK,ID_AUTORIZACION_ORIGINAL,FECHA_DIFERIMIENTO,IMPORTE_CONVERTIDO,DIAS_DIFERIMIENTO,ES_PROPINA,MONTO_ACUM_DEVOLUCIONES) \r\n"
					+ "values ('" + ID_ENTIDAD + "','" + NRO_TARJETA_ENCRIPTADA + "','307507',null,'32','370',TRUNC (SYSDATE),'1','32','370','0','41','0',null,null,null,'Comercio Argentina                   ARG','1',null,'370','7995',null,null,'U','687555537877464',null,'1','1',null,null,null,'0',null,to_date('13/10/2021 15:36:36','DD/MM/RRRR HH24:MI:SS'),'Operacion aprobada SENTINEL: OK','01',TRUNC (SYSDATE),'00','20000',null,null,'ARG','ARG','0','0','12','1',"
							+ "'" + ID_CUENTA + "','0','32','370','" + idAutoEmisorLogMax + "',null,null,null,'1','" + NRO_TARJETA_ENMASCARADA + "','0',null,'0','MCC0113330824  ',null,'0',null,null,null,null,'0','0')\r\n"
					+ "", configEntidad);
			ppCondi.preCondicionBD(report, rtaPrePostCondiciones);
			
			//Se abre el driver con la URL de GO
			driver.get(URL_GBATCH);

			// Login a la pagina principal de Global Batch
			report.AddLine("Acceso a la pagina de Global Batch");
			System.out.println("Acceso a la pagina de Global Batch\r\n");
			acceso.loginUsernameBatch(data, report, DM, iteration, name, repo, configEntidad);
			
			// Hacemos click en procesos
			nav.procesos(data, report, DM, iteration, name, repo);
			
			// Primera pagina del test
			tc.pagina1(data, report, DM, iteration, name, repo, configEntidad);

			// Segunda pagina
			tc.pagina2(data, report, DM, iteration, name, repo);

			// Tercera pagina
			tc.pagina3(data, report, DM, iteration, name, repo);
						
			// Tiempo necesario para que se ejecute el proceso y se creen los registros en la BDD
			Thread.sleep(20000);			
			
			report.AddLine("Se procede a realizar las verificaciones.");
			System.out.println("Se procede a realizar las verificaciones.\r\n");
				
			// VERIFICACIONES EN EL ESQUEMA DE LA ENTIDAD
			// Validaciones CUENTAS_MONEDA
			verifQuery = "SELECT SALDO,DISPONIBLE,IMPORTE_COMPRAS,IMPORTE_PEND_COMPRAS\r\n"
					+ "FROM CUENTAS_MONEDA \r\n"
					+ "WHERE ID_CUENTA = " + ID_CUENTA;
			respuestaVerificacion = batch.validacionCuentasMoneda(report, null, verifQuery, SALDO, DISPONIBLE, IMPORTE_COMPRAS, IMPORTE_PEND_COMPRAS, configEntidad);
			
			if (!respuestaVerificacion) {
				report.AddLineAssertionError("ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA");
				System.out.println("##[warning] : ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA\r\n");
				result = false;
			}	
			
			// Validacion en la tabla AUTORIZACION
			//Se agrega tiempo de espera para que no falle la consulta en la tabla autorizacion.
			Thread.sleep(5000);
			
			verifQuery = "SELECT PRESENTACION_PROCESADA \r\n"
					+ "FROM AUTORIZACION \r\n"
					+ "WHERE ID_AUTORIZACION = (SELECT MAX(ID_AUTORIZACION) FROM AUTORIZACION WHERE ID_CUENTA = " + ID_CUENTA + " AND TID = 'MCC0113330824  ')";
			rtaAutorizacion = oraResp.oraOneQuery(report, verifQuery, configEntidad);
			
			if (rtaAutorizacion.equals(PRESENTACION_PROCESADA)) {
				report.AddLine("LA AUTORIZACION SE PRESENTO/RELACIONO CORRECTAMENTE EN LA TABLA AUTORIZACION: PRESENTACION_PROCESADA = " + rtaAutorizacion);
				System.out.println("##[section] : LA AUTORIZACION SE PRESENTO/RELACIONO CORRECTAMENTE EN LA TABLA AUTORIZACION: PRESENTACION_PROCESADA =  " + rtaAutorizacion + "\r\n");
			} else {
				report.AddLineAssertionError("ERROR: LA AUTORIZACION NO PRESENTO/RELACIONO CORRECTAMENTE EN LA TABLA AUTORIZACION. SE ESPERABA:  PRESENTACION_PROCESADA = " + PRESENTACION_PROCESADA + " PERO SE OBTUVO PRESENTACION_PROCESADA = " + rtaAutorizacion);
				System.out.println("##[warning] : ERROR: LA AUTORIZACION NO PRESENTO/RELACIONO CORRECTAMENTE EN LA TABLA AUTORIZACION. SE ESPERABA:  PRESENTACION_PROCESADA = " + PRESENTACION_PROCESADA + " PERO SE OBTUVO PRESENTACION_PROCESADA = " + rtaAutorizacion + "\r\n");
				result = false;
			}
			
			//Se agrega tiempo de espera para que no falle la consulta en las tablas de consumos.
			Thread.sleep(5000);
			
			// Consumos -  Verifica que existan registros en las tablas de consumos
			verifQuery = "SELECT COUNT(*) \r\n"
					+ "FROM CONSUMOS C \r\n"
					+ "INNER JOIN CONSUMOS_CUOTAS CC ON C.ID_CONSUMO = CC.ID_CONSUMO\r\n"
					+ "INNER JOIN CONSUMOS_DATOS_ADICIONALES CDA ON C.ID_CONSUMO = CDA.ID_CONSUMO\r\n"
					+ "WHERE C.ID_CUENTA = " + ID_CUENTA;
			
			rtaAutorizacion = oraResp.oraOneQuery(report, verifQuery, configEntidad);
			respuestaVerificacion = batch.validacionConsumos(report, rtaAutorizacion);	
			
			if (!respuestaVerificacion) {
				report.AddLineAssertionError("ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA");
				System.out.println("##[warning] : ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA\r\n");
				result = false;
			}			
			
			// VERIFICACIONES EN EL "ESQUEMA PARAMETRIA"
			// Validaciones PROCESOS_EJECUCIONES
			System.out.println("Se realizan las validaciones en la BD:\r\n");
			report.AddLine("Se realizan las validaciones en la BD:");
			
			verifQuery = "SELECT ID_ESTADO,OBJETOS_PROCESADOS_OK FROM PROCESOS_EJECUCIONES \r\n"
					+ "WHERE ID = (SELECT MAX (ID) FROM PROCESOS_EJECUCIONES WHERE ID_PROCESO = (SELECT ID FROM PROCESOS \r\n"
					+ "WHERE ID_PROCESO_DESCRIPCION = (SELECT ID FROM PROCESOS_DESCRIPCIONES WHERE NOMBRE = 'Presentaciones Mastercard')\r\n"
					+ "AND ID_ENTIDAD = " + ID_ENTIDAD + "))";
			rtaParam = oraResp.oraTwoQueryParam(report, verifQuery,configEntidad);
			
			//El metodo verifProcesosEjec verifica el estado y cantidad de registros procesados
			respuestaVerificacion = batch.verifProcesosEjec(report, null, rtaParam, registrosOK);
			
			if (!respuestaVerificacion) {
				report.AddLineAssertionError("ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA");
				System.out.println("##[warning] : ERROR EN VALIDACION: CANTIDAD DE REGISTROS PROCESADOS DIFIERE DE LA CANTIDAD ESPERADA\r\n");
				result = false;
			}
			
			// POSTCONDICIONES
			report.AddLine("Se realiza rollback en las tablas IPM e IPM_FILE eliminando los registros creado previamente.");
			System.out.println("Se realiza rollback en las tablas IPM e IPM_FILE eliminando los registros creado previamente.\r\n");
			
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE IPM WHERE ID_IPM IN (SELECT MAX (ID_IPM) FROM IPM)", configEntidad);
			ppCondi.postCondicionBD(report, rtaPrePostCondiciones);
			
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE IPM_FILE WHERE FILE_ID = '0022108040000001180300002'", configEntidad);
			ppCondi.postCondicionBD(report, rtaPrePostCondiciones);
			
			report.AddLine("Se realiza rollback en tabla CUENTAS_MONEDA.");
			System.out.println("Se realiza rollback en tabla CUENTAS_MONEDA.\r\n");
			
			rtaPrePostCondiciones = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA\r\n"
					+ "SET SALDO = 1000, DISPONIBLE = 630, DISPONIBLE_ADELANTO = 1000,\r\n"
					+ "IMPORTE_COMPRAS = 0, IMPORTE_ADELANTOS = 0, IMPORTE_PEND_COMPRAS = 370, \r\n"
					+ "IMPORTE_PEND_ADELANTOS = 0, IMPORTE_AJUSTES = 0, IMPORTE_AJUSTES_PEND = 0,\r\n"
					+ "IMPORTE_PAGOS = 1000\r\n"
					+ "WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_MONEDA IN (32)", configEntidad);
			ppCondi.postCondicionBD(report, rtaPrePostCondiciones);
			
			
			report.AddLine("Se realiza rollback en tabla CONSUMOS_CUOTAS.");
			System.out.println("Se realiza rollback en tabla CONSUMOS_CUOTAS.\r\n");
			
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE CONSUMOS_CUOTAS WHERE ID_CONSUMO IN (SELECT ID_CONSUMO FROM CONSUMOS WHERE ID_CUENTA = " + ID_CUENTA + ")", configEntidad);
			ppCondi.postCondicionBD(report, rtaPrePostCondiciones);
			
			report.AddLine("Se realiza rollback en tabla CONSUMOS_DATOS_ADICIONALES.");
			System.out.println("Se realiza rollback en tabla CONSUMOS_DATOS_ADICIONALES.\r\n");
			
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE CONSUMOS_DATOS_ADICIONALES WHERE ID_CONSUMO IN (SELECT ID_CONSUMO FROM CONSUMOS WHERE ID_CUENTA = " + ID_CUENTA + ")", configEntidad);
			ppCondi.postCondicionBD(report, rtaPrePostCondiciones);
			
			report.AddLine("Se realiza rollback en tabla CONSUMOS.");
			System.out.println("Se realiza rollback en tabla CONSUMOS.\r\n");
			
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE CONSUMOS WHERE ID_CUENTA IN (" + ID_CUENTA + ")", configEntidad);
			ppCondi.postCondicionBD(report, rtaPrePostCondiciones);
					
			report.AddLine("Se realiza rollback en tabla AUTORIZACION eliminando el registro creado previamente.");
			System.out.println("Se realiza rollback en tabla AUTORIZACION eliminando el registro creado previamente.\r\n");			
		
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE AUTORIZACION \r\n"
					+ "WHERE ID_AUTORIZACION = (SELECT MAX(ID_AUTORIZACION) FROM AUTORIZACION WHERE ID_CUENTA = " + ID_CUENTA + " AND TID = 'MCC0113330824  ')", configEntidad);	
			ppCondi.postCondicionBD(report, rtaPrePostCondiciones);
			
			report.AddLine("Se realiza rollback en tabla AUTORIZACION_EMISOR_LOG eliminando el registro creado previamente.");
			System.out.println("Se realiza rollback en tabla AUTORIZACION_EMISOR_LOG eliminando el registro creado previamente.\r\n");			
		
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE AUTORIZACION_EMISOR_LOG \r\n"
					+ "WHERE ID_AUTORIZACION_EMISOR = (SELECT MAX(ID_AUTORIZACION_EMISOR)FROM AUTORIZACION_EMISOR_LOG WHERE IN_NRO_TARJETA = '" + NRO_TARJETA_ENMASCARADA + "')", configEntidad);	
			ppCondi.postCondicionBD(report, rtaPrePostCondiciones);
			
			System.out.println("##[command] : ------ Finished test: " + name + " ------\r\n");
			
			//Separador
			System.out.println("##################################################################################################################################################################################################################"
					 + "##################################################################################################################################################################################################################\r\n");			
			
		} catch(Exception e) {
			e.printStackTrace();
			report.AddLineAssertionError(e.getMessage());		
			
			
		    //Se agrega de nuevo ROLLBACK para que no quede la base inconsistente si falla el TestCase  
			
			//SET VARIABLES
			int rtaPrePostCondiciones = 0;
			dbWorker oraResp = new dbWorker();
			PrePostCondi ppCondi = new PrePostCondi();
			String ID_CUENTA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.cuenta_fisica");
			String NRO_TARJETA_ENMASCARADA = JsonPath.from(cuentas_generales).get("CUENTA_FISICA_1.nro_tarjeta_enmascarada");
			
			
			// POSTCONDICIONES
			report.AddLine("Se realiza rollback en las tablas IPM e IPM_FILE eliminando los registros creado previamente.");
			System.out.println("Se realiza rollback en las tablas IPM e IPM_FILE eliminando los registros creado previamente.\r\n");
						
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE IPM WHERE ID_IPM IN (SELECT MAX (ID_IPM) FROM IPM)", configEntidad);
			ppCondi.postCondicionBD(report, rtaPrePostCondiciones);
						
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE IPM_FILE WHERE FILE_ID = '0022108040000001180300002'", configEntidad);
			ppCondi.postCondicionBD(report, rtaPrePostCondiciones);
						
			report.AddLine("Se realiza rollback en tabla CUENTAS_MONEDA.");
			System.out.println("Se realiza rollback en tabla CUENTAS_MONEDA.\r\n");
						
			rtaPrePostCondiciones = oraResp.oraUpdate(report, "UPDATE CUENTAS_MONEDA\r\n"
								+ "SET SALDO = 1000, DISPONIBLE = 630, DISPONIBLE_ADELANTO = 1000,\r\n"
								+ "IMPORTE_COMPRAS = 0, IMPORTE_ADELANTOS = 0, IMPORTE_PEND_COMPRAS = 370, \r\n"
								+ "IMPORTE_PEND_ADELANTOS = 0, IMPORTE_AJUSTES = 0, IMPORTE_AJUSTES_PEND = 0,\r\n"
								+ "IMPORTE_PAGOS = 1000\r\n"
								+ "WHERE ID_CUENTA = " + ID_CUENTA + " AND ID_MONEDA IN (32)", configEntidad);
			ppCondi.postCondicionBD(report, rtaPrePostCondiciones);
						
						
			report.AddLine("Se realiza rollback en tabla CONSUMOS_CUOTAS.");
			System.out.println("Se realiza rollback en tabla CONSUMOS_CUOTAS.\r\n");
						
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE CONSUMOS_CUOTAS WHERE ID_CONSUMO IN (SELECT ID_CONSUMO FROM CONSUMOS WHERE ID_CUENTA = " + ID_CUENTA + ")", configEntidad);
			ppCondi.postCondicionBD(report, rtaPrePostCondiciones);
						
			report.AddLine("Se realiza rollback en tabla CONSUMOS_DATOS_ADICIONALES.");
			System.out.println("Se realiza rollback en tabla CONSUMOS_DATOS_ADICIONALES.\r\n");
						
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE CONSUMOS_DATOS_ADICIONALES WHERE ID_CONSUMO IN (SELECT ID_CONSUMO FROM CONSUMOS WHERE ID_CUENTA = " + ID_CUENTA + ")", configEntidad);
			ppCondi.postCondicionBD(report, rtaPrePostCondiciones);
						
			report.AddLine("Se realiza rollback en tabla CONSUMOS.");
			System.out.println("Se realiza rollback en tabla CONSUMOS.\r\n");
						
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE CONSUMOS WHERE ID_CUENTA IN (" + ID_CUENTA + ")", configEntidad);
			ppCondi.postCondicionBD(report, rtaPrePostCondiciones);
								
			report.AddLine("Se realiza rollback en tabla AUTORIZACION eliminando el registro creado previamente.");
			System.out.println("Se realiza rollback en tabla AUTORIZACION eliminando el registro creado previamente.\r\n");			
					
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE AUTORIZACION \r\n"
								+ "WHERE ID_AUTORIZACION = (SELECT MAX(ID_AUTORIZACION) FROM AUTORIZACION WHERE ID_CUENTA = " + ID_CUENTA + " AND TID = 'MCC0113330824  ')", configEntidad);	
			ppCondi.postCondicionBD(report, rtaPrePostCondiciones);
						
			report.AddLine("Se realiza rollback en tabla AUTORIZACION_EMISOR_LOG eliminando el registro creado previamente.");
			System.out.println("Se realiza rollback en tabla AUTORIZACION_EMISOR_LOG eliminando el registro creado previamente.\r\n");			
					
			rtaPrePostCondiciones = oraResp.oraDelete(report, "DELETE AUTORIZACION_EMISOR_LOG \r\n"
								+ "WHERE ID_AUTORIZACION_EMISOR = (SELECT MAX(ID_AUTORIZACION_EMISOR)FROM AUTORIZACION_EMISOR_LOG WHERE IN_NRO_TARJETA = '" + NRO_TARJETA_ENMASCARADA + "')", configEntidad);	
			ppCondi.postCondicionBD(report, rtaPrePostCondiciones);
			
			
			
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