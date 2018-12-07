package projects.solucionescolabora.com.metodoasesores.Modelos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import projects.solucionescolabora.com.metodoasesores.Consultores.ConsultorVerPDFActivity;

/**
 * Created by rodrigocanepacruz on 08/11/18.
 */

public class TemplatePDF {

    private Context context;
    private File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private BaseColor baseColor = new BaseColor(7,45,74,255);
    private Font fTitle = new Font(Font.FontFamily.HELVETICA, 22,Font.BOLD);
    private Font fEspecialidad = new Font(Font.FontFamily.HELVETICA, 16,Font.ITALIC);
    private Font fDatos = new Font(Font.FontFamily.HELVETICA, 14,Font.NORMAL, BaseColor.DARK_GRAY);
    private Font fDatosTabla = new Font(Font.FontFamily.HELVETICA, 12,Font.NORMAL, BaseColor.DARK_GRAY);
    private Font fSubTitle = new Font(Font.FontFamily.HELVETICA, 16,Font.NORMAL);
    private Font fText = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE);
    private Font fHigh = new Font(Font.FontFamily.HELVETICA, 20,Font.BOLD, baseColor);
    private Font fHigh2 = new Font(Font.FontFamily.HELVETICA, 14,Font.NORMAL, baseColor);
    private Font fTextoNormal = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK);


    public TemplatePDF(Context context) {
        this.context = context;
    }

    public void openDocument(){
        createFile();
        try{
            //document = new Document(PageSize.A4);
            document = new Document(new Rectangle(595 , 842), 0, 0, 0, 0);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();


        } catch (Exception e){
            Log.e("openDocument", e.toString());
        }
    }

    public void createFile(){
        File folder = new File(Environment.getExternalStorageDirectory().toString(), "MetodoAsesoresPDF");

        if(!folder.exists())
            folder.mkdirs();
        pdfFile = new File(folder, "TemplatePDF.pdf");
    }

    public void closeDocument(){
        document.close();
    }

    public void addMetaData(String title, String subject, String author){
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);
    }

    public void addTitles(String title, String subtitle, String date){

        try{
            paragraph = new Paragraph();
            addChild(new Paragraph(title, fTitle));
            addChild(new Paragraph(subtitle, fSubTitle));
            addChild(new Paragraph("Generado: " + date, fHigh2));
            paragraph.setSpacingAfter(10);
            document.add(paragraph);

        } catch (Exception e){
            Log.e("addTitles", e.toString());
        }

    }

    public void addSections(String title){

        try{
            paragraph = new Paragraph();
            addChild(new Paragraph(title, fHigh));
            paragraph.setSpacingAfter(20);
            paragraph.setSpacingBefore(20);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

        } catch (Exception e){
            Log.e("addSections", e.toString());
        }

    }

    private void addChild(Paragraph childParagraph){
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }

    public void addParagraph(String text){
        try{
            paragraph = new Paragraph(text, fTextoNormal);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(5);
            document.add(paragraph);
        } catch (Exception e){
            Log.e("addParagraph", e.toString());
        }

    }

    public void addImage(Context context) {

        try {
            // get input stream
            /*File imgFile = new File("sdcard/Pictures/SignaturePad/Signature_.jpg");

            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                image.scaleToFit(250, 150);
                image.setAlignment(Element.ALIGN_CENTER);
                document.add(image);
            }*/
            InputStream ims = context.getAssets().open("header.png");
            Bitmap bmp = BitmapFactory.decodeStream(ims);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleToFit(document.getPageSize().getWidth(), 75);
            document.add(image);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void createTableReconocimientos(String[]header, ArrayList<String[]> clients){

        try{
            paragraph = new Paragraph();
            paragraph.setFont(fText);
            PdfPTable pdfPTable = new PdfPTable(3);
            pdfPTable.setWidthPercentage(90);
            pdfPTable.setWidths(new int[]{1, 2, 1});
            pdfPTable.setSpacingBefore(10);
            PdfPCell pdfPCell;
            int indexC = 0;
            while (indexC<header.length){
                pdfPCell = new PdfPCell(new Phrase(header[indexC++], fText));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setBackgroundColor(baseColor);
                pdfPTable.addCell(pdfPCell);
            }

            for(int indexR=0; indexR<clients.size(); indexR++){
                String[] row = clients.get(indexR);
                for(indexC = 0; indexC < header.length; indexC++){
                    pdfPCell = new PdfPCell(new Phrase(row[indexC], fDatosTabla));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    // pdfPCell.setFixedHeight(30);
                    pdfPTable.addCell(pdfPCell);
                }
            }

            paragraph.add(pdfPTable);
            document.add(paragraph);
        } catch (Exception e){
            Log.e("createTable", e.toString());
        }

    }

    public void createTableWith2cell(String[]header, ArrayList<String[]>clients){

        try{
            paragraph = new Paragraph();
            paragraph.setFont(fText);
            PdfPTable pdfPTable = new PdfPTable(2);
            pdfPTable.setWidthPercentage(90);
            pdfPTable.setWidths(new int[]{2, 1});
            pdfPTable.setSpacingBefore(10);
            PdfPCell pdfPCell;
            int indexC = 0;
            while (indexC<header.length){
                pdfPCell = new PdfPCell(new Phrase(header[indexC++], fText));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setBackgroundColor(baseColor);
                pdfPTable.addCell(pdfPCell);
            }

            for(int indexR=0; indexR<clients.size(); indexR++){
                String[] row = clients.get(indexR);
                for(indexC = 0; indexC < header.length; indexC++){
                    pdfPCell = new PdfPCell(new Phrase(row[indexC], fDatosTabla));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    // pdfPCell.setFixedHeight(30);
                    pdfPTable.addCell(pdfPCell);
                }
            }

            paragraph.add(pdfPTable);
            document.add(paragraph);
        } catch (Exception e){
            Log.e("createTable", e.toString());
        }

    }

    public void createTableWith2SameLengthcell(String[]header, ArrayList<String[]>clients){

        try{
            paragraph = new Paragraph();
            paragraph.setFont(fText);
            PdfPTable pdfPTable = new PdfPTable(2);
            pdfPTable.setWidthPercentage(90);
            pdfPTable.setWidths(new int[]{1, 1});
            pdfPTable.setSpacingBefore(10);
            PdfPCell pdfPCell;
            int indexC = 0;
            while (indexC<header.length){
                pdfPCell = new PdfPCell(new Phrase(header[indexC++], fText));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setBackgroundColor(baseColor);
                pdfPTable.addCell(pdfPCell);
            }

            for(int indexR=0; indexR<clients.size(); indexR++){
                String[] row = clients.get(indexR);
                for(indexC = 0; indexC < header.length; indexC++){
                    pdfPCell = new PdfPCell(new Phrase(row[indexC], fDatosTabla));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    // pdfPCell.setFixedHeight(30);
                    pdfPTable.addCell(pdfPCell);
                }
            }

            paragraph.add(pdfPTable);
            document.add(paragraph);
        } catch (Exception e){
            Log.e("createTable", e.toString());
        }

    }

    public void createTableWith4cell(String[]header, ArrayList<String[]>clients){

        try{
            paragraph = new Paragraph();
            paragraph.setFont(fText);
            PdfPTable pdfPTable = new PdfPTable(4);
            pdfPTable.setWidthPercentage(90);
            pdfPTable.setWidths(new int[]{1, 1, 1, 1});
            pdfPTable.setSpacingBefore(10);
            PdfPCell pdfPCell;
            int indexC = 0;
            while (indexC<header.length){
                pdfPCell = new PdfPCell(new Phrase(header[indexC++], fText));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setBackgroundColor(baseColor);
                pdfPTable.addCell(pdfPCell);
            }

            for(int indexR=0; indexR<clients.size(); indexR++){
                String[] row = clients.get(indexR);
                for(indexC = 0; indexC < header.length; indexC++){
                    pdfPCell = new PdfPCell(new Phrase(row[indexC], fDatosTabla));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    // pdfPCell.setFixedHeight(30);
                    pdfPTable.addCell(pdfPCell);
                }
            }

            paragraph.add(pdfPTable);
            document.add(paragraph);
        } catch (Exception e){
            Log.e("createTable", e.toString());
        }

    }

    public void createTableWithTheSameLength(String[]header, ArrayList<String[]>clients){

        try{
            paragraph = new Paragraph();
            paragraph.setFont(fText);
            PdfPTable pdfPTable = new PdfPTable(4);
            pdfPTable.setWidthPercentage(90);
            pdfPTable.setWidths(new int[]{1, 1, 2, 1});
            pdfPTable.setSpacingBefore(10);
            PdfPCell pdfPCell;
            int indexC = 0;
            while (indexC<header.length){
                pdfPCell = new PdfPCell(new Phrase(header[indexC++], fText));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setBackgroundColor(baseColor);
                pdfPTable.addCell(pdfPCell);
            }

            for(int indexR=0; indexR<clients.size(); indexR++){
                String[] row = clients.get(indexR);
                for(indexC = 0; indexC < header.length; indexC++){
                    pdfPCell = new PdfPCell(new Phrase(row[indexC], fDatosTabla));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    // pdfPCell.setFixedHeight(30);
                    pdfPTable.addCell(pdfPCell);
                }
            }

            paragraph.add(pdfPTable);
            document.add(paragraph);
        } catch (Exception e){
            Log.e("createTable", e.toString());
        }

    }

    public void createGraficaFoto(Context context, Bitmap bitmap){

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleToFit(600, 600);
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void createTableWithFoto(Context context, Bitmap bitmap, String nombre, String especialidad, String datos){

        try {
            // get input stream
            /*File imgFile = new File("sdcard/Pictures/SignaturePad/Signature_.jpg");

            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                image.scaleToFit(250, 150);
                image.setAlignment(Element.ALIGN_CENTER);
                document.add(image);
            }*/
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleToFit(100, 115);
            //document.add(image);

            try{
                paragraph = new Paragraph();
                paragraph.setFont(fText);
                PdfPTable pdfPTable = new PdfPTable(2);
                PdfPTable pdfPTableTexto = new PdfPTable(1);

                pdfPTableTexto.addCell(createTextCellNombre(nombre));
                pdfPTableTexto.addCell(createTextCellEspecialidad(especialidad));
                pdfPTableTexto.addCell(createTextCellDatosExtra(datos));

                pdfPTable.setWidthPercentage(90);
                pdfPTable.setWidths(new int[]{2, 5});
                pdfPTable.setSpacingBefore(30);
                pdfPTable.addCell(image);

                pdfPTable.addCell(pdfPTableTexto);


                paragraph.add(pdfPTable);
                document.add(paragraph);
            } catch (Exception e){
                Log.e("createTable", e.toString());
            }


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public PdfPCell createTextCellNombre(String text) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text);
        p.setFont(fTitle);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setPaddingLeft(20);
        cell.setPaddingRight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public PdfPCell createTextCellEspecialidad(String text) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text);
        p.setFont(fEspecialidad);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setPaddingLeft(20);
        cell.setPaddingRight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public PdfPCell createTextCellDatosExtra(String text) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text);
        p.setFont(fDatos);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setPaddingLeft(20);
        cell.setPaddingRight(20);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public void viewPDF(String tipo){
        Intent intent = new Intent(context, ConsultorVerPDFActivity.class);
        intent.putExtra("path", pdfFile.getAbsolutePath());
        intent.putExtra("tipo", tipo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
