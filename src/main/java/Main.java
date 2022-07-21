import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Main{
    public static void main(String[]args) {
        String mappingName = "VVDF.xlsx";
        File mappings = new File(Main.class.getClassLoader().getResource(mappingName).getFile());
        System.out.println(mappings.getPath());
        Scanner sc = new Scanner(System.in);
        System.out.println("Número de secciones para input: ");
        int sectionsIn = sc.nextInt();
        List<Integer> sectionInRows = new ArrayList<>();
        List<String> sectionInNames = new ArrayList<>();
        for(int i=0; i< sectionsIn; i++){
            System.out.println("Ingrese número de fila de la sección "+(i+1)+": ");
            Integer sec = sc.nextInt();
            sc.nextLine();
            sectionInRows.add(sec);
            System.out.println("Ingrese número de fila final de la sección "+(i+1)+": ");
            Integer secFin = sc.nextInt();
            sc.nextLine();
            sectionInRows.add(secFin);
            System.out.println("Ingrese nombre de la cabecera: ");
            String name = sc.nextLine();
            sectionInNames.add(name);
        }


        System.out.println("Número de secciones para output: ");
        int sectionsOut = sc.nextInt();
        List<Integer> sectionOutRows = new ArrayList<>();
        List<String> sectionOutNames = new ArrayList<>();
        for(int i=0; i< sectionsOut; i++){
            System.out.println("Ingrese número de fila de la sección "+(i+1)+": ");
            Integer sec = sc.nextInt();
            sc.nextLine();
            sectionOutRows.add(sec);
            System.out.println("Ingrese número de fila final de la sección "+(i+1)+": ");
            Integer secFin = sc.nextInt();
            sc.nextLine();
            sectionOutRows.add(secFin);
            System.out.println("Ingrese nombre de la cabecera: ");
            String name = sc.nextLine();
            sectionOutNames.add(name);
        }

        try{
            FileInputStream fis = new FileInputStream(mappings);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet inputSheet = wb.getSheetAt(1);
            XSSFSheet outputSheet = wb.getSheetAt(2);
            System.out.println(inputSheet.getSheetName());
            System.out.println(outputSheet.getSheetName());
            List<WBDP> input = new ArrayList<>();
            List<WBDP> output = new ArrayList<>();


            for(int i=0; i<sectionInRows.size()/2; i++){
                input.add(new WBDP(sectionInNames.get(i), 0, true));
                for(int j=sectionInRows.get(2*i);j<sectionInRows.get(2*i+1)+1;j++){
                    Row row = inputSheet.getRow(j-1);
                    String name = row.getCell(0).getStringCellValue();
                    double length = (int) row.getCell(2).getNumericCellValue();
                    System.out.println("Nombre: " + name + "\t" + length);
                    input.add(new WBDP(name.trim(), (int)length));
                }

            }
            for(int i=0; i<sectionOutRows.size()/2; i++){
                output.add(new WBDP(sectionOutNames.get(i), 0, true));
                for(int j=sectionOutRows.get(2*i);j<sectionOutRows.get(2*i+1)+1;j++){
                    Row row = outputSheet.getRow(j-1);
                    String name = row.getCell(0).getStringCellValue();
                    double length = (int) row.getCell(2).getNumericCellValue();
                    System.out.println("Nombre: " + name + "\t" + length);
                    output.add(new WBDP(name.trim(), (int)length));
                }

            }
            StringBuilder DLEntrada = createDL(input);
            BufferedWriter writer = new BufferedWriter(new FileWriter("DLEntrada.xml"));
            writer.write(DLEntrada.toString());
            writer.close();

            StringBuilder DLSalida = createDL(output);
            BufferedWriter writer2 = new BufferedWriter(new FileWriter("DLSalida.xml"));
            writer2.write(DLSalida.toString());
            writer2.close();

            StringBuilder FLEntrada = createFL(input);
            BufferedWriter writer3 = new BufferedWriter(new FileWriter("FLEntrada.xml"));
            writer3.write(FLEntrada.toString());
            writer3.close();

            StringBuilder FLSalida = createFL(output);
            BufferedWriter writer4 = new BufferedWriter(new FileWriter("FLSalida.xml"));
            writer4.write(FLSalida.toString());
            writer4.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static StringBuilder createDL(List<WBDP> input){
        StringBuilder output = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
        output.append("<trama type=\"RE\" description=\"Descripcion de la trama\">\n");
        for(int i=0;i<input.size();i++){
            if(input.get(i).isHeader()){
                if(i!=0)
                    output.append("\t</element>\n");
                output.append("\t<element name=\""+input.get(i).getName()+"\" type=\"RE\" occurrences=\"1\">\n");
            }else{
                output.append("\t\t<recordelement name=\""+input.get(i).getName()+"\" lenght=\""+
                        input.get(i).getLength()+"\" type=\"ER\" default_value=\" \" filler_caracter=\" \" alignment=\"L\" " +
                        "occurrences=\"1\" real_value=\" \" visual_value=\" \" />\n");
            }
        }
        output.append("\t</element>\n");
        output.append("</trama>\n");
        return output;
    }

    public static StringBuilder createFL(List<WBDP> input){
        StringBuilder output = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
        output.append("<formlayout>\n");
        for(int i=0;i<input.size();i++){
            if(input.get(i).isHeader()){
                output.append("\t<inputelement bind=\""+input.get(i).getName()+"\" title=\""+input.get(i).getName()+"\" type=\"PNL\" rows=\"100\" style=\"GRID\" />\n");
            }else{
                output.append("\t<inputelement bind=\""+input.get(i).getName()+"\" title=\""+input.get(i).getName()+"\" type=\"txt\"/>\n");
            }
        }
        output.append("</formlayout>\n");
        return output;
    }
}
