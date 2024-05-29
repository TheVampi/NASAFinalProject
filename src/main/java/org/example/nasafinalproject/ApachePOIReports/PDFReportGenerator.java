package org.example.nasafinalproject.ApachePOIReports;

import org.example.nasafinalproject.Models.*;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;


public class PDFReportGenerator {

    JSONArray jsonArray;

    public void createPdf(String dest) throws IOException {

        PdfWriter writer = new PdfWriter(dest);



        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf, PageSize.A4.rotate());
        document.setMargins(20, 20, 20, 20);

        PdfFont font = PdfFontFactory.createFont(StandardFonts.COURIER);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.COURIER_BOLD);

        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 4, 2, 3, 5}))
                .useAllAvailableWidth();

        String apiKey= "ghuPTPXV9dFB9laeGBdY2laVEJf4BFVgUlcOwber";
        String urlString = "https://api.nasa.gov/planetary/apod?api_key=";
        String jsonString="";
        LocalDate today = LocalDate.now();
        LocalDate lastWeek = today.minusDays(7);

        Picture pic = new Picture();

        URL url = new URL(urlString+apiKey+"&start_date="+
                                                lastWeek.toString()+"&end_date="+today.toString()+
                                                "&thumbs=true");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream stream = connection.getInputStream();
        byte[] arrBytes = stream.readAllBytes();

        for(byte ch : arrBytes){
            jsonString+=(char)ch;
        }

        jsonArray = new JSONArray(jsonString);

        addHeaders(table, bold);

        addElements(table, font);
        document.add(table);
        document.close();
    }

    private void addHeaders(Table table, PdfFont bold){
        table.addHeaderCell(new Cell().add(new Paragraph("Date").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Title").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Type").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Author").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Image").setFont(bold)));
    }

    private void addElements (Table table, PdfFont font) throws MalformedURLException {

        for(Object object : jsonArray){
            table.addCell(new Cell().add(new Paragraph(((JSONObject)object).get("date").toString()).setFont(font)));
            table.addCell(new Cell().add(new Paragraph(((JSONObject)object).get("title").toString()).setFont(font)));
            table.addCell(new Cell().add(new Paragraph(((JSONObject)object).get("media_type").toString()).setFont(font)));

            try {
                table.addCell(new Cell().add(new Paragraph(((JSONObject)object).get("copyright").toString()).setFont(font)));
            }catch (Exception e){
                table.addCell(new Cell().add(new Paragraph("unknown").setFont(font)));
            }

            try{
                table.addCell(new Cell()
                        .add(new Paragraph()
                                .add(new Image(
                                        ImageDataFactory
                                                .create(
                                                        ((JSONObject)object)
                                                                .get("url")
                                                                .toString())).setAutoScale(true))));
            }catch (Exception e){
                table.addCell(new Cell()
                        .add(new Paragraph()
                                .add(new Image(
                                        ImageDataFactory
                                                .create(
                                                        ((JSONObject)object)
                                                                .get("thumbnail_url")
                                                                .toString())).setAutoScale(true))));
            }


        }

//        for(Object object : jsonArray){
//            table.addCell(new Cell().add(new Paragraph(((JSONObject)object).get("date").toString()).setFont(font)));
//            table.addCell(new Cell().add(new Paragraph(((JSONObject)object).get("title").toString()).setFont(font)));
//            table.addCell(new Cell().add(new Paragraph(((JSONObject)object).get("media_type").toString()).setFont(font)));
//
////            try {
////                table.addCell(new Cell().add(new Paragraph(((JSONObject)object).get("copyright").toString()).setFont(font)));
////            }catch (Exception e){
////                table.addCell(new Cell().add(new Paragraph("unknown").setFont(font)));
////            }
////
////            table.addCell(new Cell()
////                    .add(new Paragraph()
////                            .add(new Image(
////                                    ImageDataFactory
////                                            .create(
////                                                    ((JSONObject)object)
////                                                            .get("url")
////                                                            .toString())).setAutoScale(true))));
//            try{
//                table.addCell(new Cell()
//                        .add(new Paragraph()
//                                .add(new Image(
//                                        ImageDataFactory
//                                                .create(
//                                                        ((JSONObject)object)
//                                                                .get("url")
//                                                                .toString())).setAutoScale(true))));
//            }catch (Exception e){
//                table.addCell(new Cell()
//                        .add(new Paragraph()
//                                .add(new Image(
//                                        ImageDataFactory
//                                                .create(
//                                                        ((JSONObject)object)
//                                                                .get("thumbnail_url")
//                                                                .toString())).setAutoScale(true))));
//            }
//
//
//        }

    }


}
