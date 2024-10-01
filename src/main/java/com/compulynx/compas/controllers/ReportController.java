package com.compulynx.compas.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.compulynx.compas.security.AESsecure;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
@Controller
@RequestMapping("reports")
public class ReportController {
	
	@Autowired
	private Environment env;
    @SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	@GetMapping("/enrolledCustomers")
    public void getReports(
			@RequestParam(value="reportType")
			String reportType,
			@RequestParam(value="exportType")
			String exportType, 
    		@RequestParam("FromDt")
			@org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd")
    		Date FromDt,
    		@RequestParam(value="ToDt")
    		@org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd")
    		Date ToDt,
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException, IOException {
    	try {
    		String keyValue = env.getProperty("rportpath");
    		
    		Connection connection = null;
    		JasperReport jasperReport = null; //  (JasperReport) JRLoader.loadObject(keyValue);
    		JasperPrint jasperPrint = null;
    		JasperDesign jasperDesign = null;
    		Map<String, Object> parameters = new HashMap();
    		ServletOutputStream outstream = null;
			InitialContext initialContext = new InitialContext();
			Context context = (Context) initialContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) context.lookup("jdbc/compasDS");
			connection = ds.getConnection();
			 JRProperties.setProperty( JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX+"plsql"
                     ,"com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
			if (reportType.equalsIgnoreCase("BD")) {
				String fromDt=request.getParameter("FromDt");
				String toDt=request.getParameter("ToDt");
				String userId= request.getParameter("userId");
				 SimpleDateFormat df=new SimpleDateFormat("yyyy-mm-dd");  
				 Date fromr=df.parse(fromDt); 
				 Date tor=df.parse(toDt);
				 parameters.put("FROMDT", fromr);
				 parameters.put("TODT", tor);
				 parameters.put("USERID", userId);
				if (exportType.equalsIgnoreCase("P")) {
					jasperDesign = JRXmlLoader.load(keyValue
							+ "RptLogsTxn.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters,connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				}
				if (exportType.equalsIgnoreCase("C")) {
					System.out.println("calling servlet## Summary Csv");
					jasperDesign = JRXmlLoader.load(keyValue
							+ "RptLogsTxn_csv.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRCsvExporter exporter = getCommonCsvExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(
								JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
								Boolean.TRUE);
						exporter.setParameter(
								JRCsvExporterParameter.RECORD_DELIMITER, "\r\n");
						exporter.setParameter(
								JRCsvExporterParameter.JASPER_PRINT,
								jasperPrint);
						exporter.setParameter(
								JRCsvExporterParameter.OUTPUT_STREAM, baos);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + "ENROLLED CUSTOMERS"+".csv");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					} else {
						System.out.println("No data");
					}
				} else {
					System.out.println("calling servlet## Summary Excel");
					jasperDesign = JRXmlLoader.load(keyValue
							+ "RptLogsTxn_xlsx.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperReport.setProperty( "net.sf.jasperreports.query.executer.factory.plsql"
			                ,"com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(
								JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
								Boolean.FALSE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
								Boolean.FALSE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_IGNORE_GRAPHICS,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.JASPER_PRINT,
								jasperPrint);
						exporter.setParameter(
								JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + "ENROLLED CUSTOMERS"
										+ ".xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					} else {
						System.out.println("No data");
					}
				}
			}
			if (reportType.equalsIgnoreCase("CER")) {
				 String fromDt=request.getParameter("FromDt");
				 String toDt=request.getParameter("ToDt");
				 String enrolledTyp= request.getParameter("enrolledType");
				 SimpleDateFormat df=new SimpleDateFormat("yyyy-mm-dd");  
				 Date fromr=df.parse(fromDt); 
				 Date tor=df.parse(toDt);
				 parameters.put("FROMDT", fromr);
				 parameters.put("TODT", tor);
				 parameters.put("ENROLT", enrolledTyp);
				if (exportType.equalsIgnoreCase("P")) {
					jasperDesign = JRXmlLoader.load(keyValue
							+ "RptCustomerEnrolled.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters,connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				}
				if (exportType.equalsIgnoreCase("C")) {
					System.out.println("calling servlet## Summary Csv");
					jasperDesign = JRXmlLoader.load(keyValue
							+ "RptCashTxn_csv.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRCsvExporter exporter = getCommonCsvExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(
								JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
								Boolean.TRUE);
						exporter.setParameter(
								JRCsvExporterParameter.RECORD_DELIMITER, "\r\n");
						exporter.setParameter(
								JRCsvExporterParameter.JASPER_PRINT,
								jasperPrint);
						exporter.setParameter(
								JRCsvExporterParameter.OUTPUT_STREAM, baos);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + "ENROLLED CUSTOMERS"+".csv");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					} else {
						System.out.println("No data");
					}
				} else {
					System.out.println("calling servlet## Summary Excel");
					jasperDesign = JRXmlLoader.load(keyValue
							+ "RptEnrolledCustomers_xlsx.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperReport.setProperty( "net.sf.jasperreports.query.executer.factory.plsql"
			                ,"com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(
								JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
								Boolean.FALSE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
								Boolean.FALSE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_IGNORE_GRAPHICS,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.JASPER_PRINT,
								jasperPrint);
						exporter.setParameter(
								JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + "ENROLLED CUSTOMERS"
										+ ".xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					} else {
						System.out.println("No data");
					}
				}
			}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
	@SuppressWarnings("deprecation")
	private JRCsvExporter getCommonCsvExporter() {
		JRCsvExporter exporter = new JRCsvExporter();
		exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS,
				Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
				Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE,
				Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
				Boolean.FALSE);
		exporter.setParameter(
				JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				Boolean.TRUE);
		exporter.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE,
				Boolean.TRUE);
		return exporter;
	}
	
	@SuppressWarnings("deprecation")
	private JRXlsxExporter getCommonXlsxExporter() {
		JRXlsxExporter exporter = new JRXlsxExporter();
		exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS,
				Boolean.TRUE);
		
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
				Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE,
				Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
				Boolean.FALSE);
		exporter.setParameter(
				JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				Boolean.TRUE);
		exporter.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE,
				Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
		// exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
		// Boolean.TRUE);
		return exporter;
	}
}
