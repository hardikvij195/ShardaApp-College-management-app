package com.hvtechnologies.shardaapp;

import android.content.res.AssetManager;
import android.inputmethodservice.Keyboard;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

*/





import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Font;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class Export_Data extends AppCompatActivity {

    TextView File_Txt ;

    Button Add_Stud , Add_Teach , Add_Dean , Add_File , Add_Stud_File , Add_Teacher_File , Add_Dean_File  , Export_Structure_Data;


    DatabaseReference ClassRef ;

    private static String[] columns = {"School Name", "School Id" ,"Department Name", "Department Id", "Year Name" , "Year Id" , "Class Name" ,"Class Id" , "Group Name" , "Group Id"};


    String a = "" ;

    public List<StructureClassGroupWise> GroupsWiseClasses =  new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export__data);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Export_Structure_Data = (Button)findViewById(R.id.Export_Str_Data);
        File_Txt = (TextView)findViewById(R.id.textViewFile);
        Add_Stud = (Button)findViewById(R.id.Add_Studs);
        Add_Teach = (Button)findViewById(R.id.Add_Teachers);
        Add_Dean = (Button)findViewById(R.id.Add_Deans);
        Add_File = (Button)findViewById(R.id.Select_File);
        Add_Stud_File = (Button)findViewById(R.id.Add_Students_File);
        Add_Teacher_File = (Button)findViewById(R.id.Add_Teachers_File);
        Add_Dean_File = (Button)findViewById(R.id.Add_Deans_File);


        Add_Stud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Add_Students_1();

            }
        });


        Export_Structure_Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //GetData();

                if(!GroupsWiseClasses.isEmpty()){


                    try {
                       FillData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BiffException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(Export_Data.this , "File -- "  , Toast.LENGTH_SHORT).show();

                }else {

                    Toast.makeText(Export_Data.this , "No File -- "  , Toast.LENGTH_SHORT).show();

                }

                //Toast.makeText(Export_Data.this , "Sch -- "  , Toast.LENGTH_SHORT).show();


            }
        });



        ClassRef = FirebaseDatabase.getInstance().getReference("Structure/") ;
        ClassRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        if (dataSnapshot1.exists()) {

                            String SchId = dataSnapshot1.getKey();
                            String SchName = dataSnapshot1.child("Name").getValue().toString();

                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                                if (dataSnapshot2.exists()) {

                                    String DeptId = dataSnapshot2.getKey();

                                    if(!DeptId.equals("Name")){

                                        String DeptName = dataSnapshot2.child("Name").getValue().toString();
                                        File_Txt.setText(a.concat(SchName +" - " + SchId + " - " + DeptName + " - " + DeptId  ));

                                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {

                                            if(dataSnapshot3.exists()){

                                                String YrId = dataSnapshot3.getKey();

                                                if(!YrId.equals("Name")) {

                                                    String YrName = dataSnapshot3.child("Name").getValue().toString();

                                                    for (DataSnapshot dataSnapshot4 : dataSnapshot3.child("Classes").getChildren()) {

                                                        if(dataSnapshot3.exists()) {

                                                            String ClassId = dataSnapshot4.getKey();

                                                            if(!ClassId.equals("Name")) {

                                                                String ClName = dataSnapshot4.child("Name").getValue().toString();

                                                                for (DataSnapshot dataSnapshot5 : dataSnapshot4.child("Groups").getChildren()) {

                                                                    if(dataSnapshot5.exists()){

                                                                        String GrId = dataSnapshot5.getKey();
                                                                        String GrName = dataSnapshot5.child("Name").getValue().toString();




                                                                       // File_Txt.setText(a.concat(SchName + " - " + SchId + " - " + DeptName + " - " + DeptId + " - " + YrName
                                                                         //       + " - " + YrId + " - " + ClName + " - " + ClassId + " - " + GrId + " - " + GrName));



                                                                    }
                                                                }

                                                            }

                                                        }

                                                    }



                                                }

                                            }



                                        }



                                    }

                                    //String DeptName = dataSnapshot2.child("Name").getValue().toString();


                                    //Toast.makeText(Export_Data.this , "D -- " + DeptName , Toast.LENGTH_SHORT).show();



//                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {

  //                                      if (dataSnapshot3.exists()) {

    //                                        String YrId = dataSnapshot3.getKey();

                                            /*
                                            String YrName = dataSnapshot3.child("Name").getValue().toString();

                                            for (DataSnapshot dataSnapshot4 : dataSnapshot3.child("Classes").getChildren()) {


                                                if (dataSnapshot4.exists()) {


                                                    String ClassId = dataSnapshot4.getKey();
                                                    String ClassName = dataSnapshot4.child("Name").getValue().toString();

                                                    for (DataSnapshot dataSnapshot5 : dataSnapshot4.child("Groups").getChildren()) {

                                                        if (dataSnapshot5.exists()) {

                                                            String GroupId = dataSnapshot5.getKey();
                                                            String GroupName = dataSnapshot5.child("Name").getValue().toString();
                                                            GroupsWiseClasses.add(new StructureClassGroupWise(SchName, SchId, DeptName, DeptId, YrName, YrId, ClassName, ClassId, GroupName, GroupId));

                                                            //File_Txt.setText(a.concat(SchName));
                                                            Toast.makeText(Export_Data.this , "Sch -- " + SchName , Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                }
                                            }

                                            */

      //                                  }
        //                            }
                                }
                            }
                        }
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    public void FillData() throws IOException, BiffException {


        if (!GroupsWiseClasses.isEmpty()) {


            String Fnamexls = "sharda_struct" + ".xls";

            File sdCard = Environment.getExternalStorageDirectory();

            File directory = new File(sdCard.getAbsolutePath() + "/newfolder");
            directory.mkdirs();

            File file = new File(directory, Fnamexls);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook;


            try {


                int a = 1;
                workbook = Workbook.createWorkbook(file, wbSettings);
                WritableSheet sheet = workbook.createSheet("First Sheet", 0);

                //Label label = new Label(0, 2, "SECOND");
                //Label label1 = new Label(0, 1, "first");
                //Label label0 = new Label(0, 0, "HEADING");
                //Label label3 = new Label(1, 0, "Heading2");
                //Label label4 = new Label(1, 1, String.valueOf(a));


                try {

                    for ( int i = 0 ; i < columns.length ; i++){

                        Label labell = new Label(i, 0, columns[i]);
                        sheet.addCell(labell);

                    }

                    int k = 3;

                    for ( int j = 0 ; j < GroupsWiseClasses.size() ; j++){

                        int l = j+k ;

                        Label label0 = new Label(0, l, GroupsWiseClasses.get(j).getSchoolName());
                        Label label1 = new Label(1, l, GroupsWiseClasses.get(j).getSchoolId());
                        Label label2 = new Label(2, l, GroupsWiseClasses.get(j).getDepartmentName());
                        Label label3 = new Label(3, l, GroupsWiseClasses.get(j).getDepartmentId());
                        Label label4 = new Label(4, l, GroupsWiseClasses.get(j).getYearName());
                        Label label5 = new Label(5, l, GroupsWiseClasses.get(j).getYearId());
                        Label label6 = new Label(6, l, GroupsWiseClasses.get(j).getClassName());
                        Label label7 = new Label(7, l, GroupsWiseClasses.get(j).getClassId());
                        Label label8 = new Label(8, l, GroupsWiseClasses.get(j).getGroupName());
                        Label label9 = new Label(9, l, GroupsWiseClasses.get(j).getGroupId());

                        sheet.addCell(label0);
                        sheet.addCell(label1);
                        sheet.addCell(label2);
                        sheet.addCell(label3);
                        sheet.addCell(label4);
                        sheet.addCell(label5);
                        sheet.addCell(label6);
                        sheet.addCell(label7);
                        sheet.addCell(label8);
                        sheet.addCell(label9);

                    }

                } catch (RowsExceededException e) {

                    e.printStackTrace();

                } catch (WriteException e) {

                    e.printStackTrace();

                }
                workbook.write();

                try {
                    workbook.close();
                } catch (WriteException e) {

                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            /*

            // Create a Workbook

            Workbook workbook = Workbook.getWorkbook(is);


            // new HSSFWorkbook() for generating `.xls` file


            Sheet sheet = workbook.createSheet("All Group");
            // Create a Font for styling header cells

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.RED.getIndex());

            // Create a CellStyle with the font

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Create a Row
            Keyboard.Row headerRow = sheet.createRow(0);
            // Creating cells

            for (int i = 0; i < columns.length; i++) {

                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);

            }

            // Cell Style for formatting Date
            //CellStyle dateCellStyle = workbook.createCellStyle();
            //dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
            // Create Other rows and cells with employees data

            int rowNum = 2;
            for (StructureClassGroupWise classes : GroupsWiseClasses) {

                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(classes.getSchoolName());
                row.createCell(1).setCellValue(classes.getSchoolId());
                row.createCell(2).setCellValue(classes.getDepartmentName());
                row.createCell(3).setCellValue(classes.getDepartmentId());
                row.createCell(4).setCellValue(classes.getYearName());
                row.createCell(5).setCellValue(classes.getYearId());
                row.createCell(6).setCellValue(classes.getClassName());
                row.createCell(7).setCellValue(classes.getClassId());
                row.createCell(8).setCellValue(classes.getGroupName());
                row.createCell(9).setCellValue(classes.getGroupId());

            }

            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {

                sheet.autoSizeColumn(i);

            }

            */


        }


    }



    public void GetData() {



        /*



        */


        final String a = "" ;


        ClassRef = FirebaseDatabase.getInstance().getReference("Structure/") ;


        ClassRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        if (dataSnapshot1.exists()) {

                            String SchId = dataSnapshot1.getKey();
                            String SchName = dataSnapshot1.child("Name").getValue().toString();

                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                if (dataSnapshot2.exists()) {

                                    String DeptId = dataSnapshot2.getKey();
                                    String DeptName = dataSnapshot2.child("Name").getValue().toString();

                                    for (DataSnapshot dataSnapshot3 : dataSnapshot.getChildren()) {

                                        if (dataSnapshot3.exists()) {

                                            String YrId = dataSnapshot3.getKey();
                                            String YrName = dataSnapshot3.child("Name").getValue().toString();

                                            for (DataSnapshot dataSnapshot4 : dataSnapshot.child("Classes").getChildren()) {


                                                if (dataSnapshot4.exists()) {


                                                    String ClassId = dataSnapshot4.getKey();
                                                    String ClassName = dataSnapshot4.child("Name").getValue().toString();

                                                    for (DataSnapshot dataSnapshot5 : dataSnapshot.child("Groups").getChildren()) {

                                                        if (dataSnapshot5.exists()) {

                                                            String GroupId = dataSnapshot5.getKey();
                                                            String GroupName = dataSnapshot5.child("Name").getValue().toString();
                                                            GroupsWiseClasses.add(new StructureClassGroupWise(SchName, SchId, DeptName, DeptId, YrName, YrId, ClassName, ClassId, GroupName, GroupId));

                                                            //File_Txt.setText(a.concat(SchName));
                                                            Toast.makeText(Export_Data.this , "Sch -- " + SchName , Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    public void Add_Students_1() {

        try {

            /*
            InputStream myInput;
            // initialize asset manager
            AssetManager assetManager = getAssets();
            //  open excel sheet
            myInput = assetManager.open("Student_Login_Id_Creation_Creds.xls");
            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet1 = myWorkBook.getSheetAt(0);

            // We now need something to iterate through the cells.
            Iterator<Row> rowIter = mySheet1.rowIterator();

            int rowno =2;

            //File_Txt.append("\n");

            while (rowIter.hasNext()) {

                //Log.e(TAG, " row no "+ rowno );
                HSSFRow myRow = (HSSFRow) rowIter.next();

                if(rowno !=0) {

                    Iterator<Cell> cellIter = myRow.cellIterator();

                    int colno =0;

                    //String sno="", date="", det="";


                    while (cellIter.hasNext()) {

                        HSSFCell myCell = (HSSFCell) cellIter.next();

                        if (colno==0){

                      //      sno = myCell.toString();

                        }else if (colno==1){

                        //    date = myCell.toString();

                        }else if (colno==2){

                       //     det = myCell.toString();

                        }
                        colno++;


                        //Log.e(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());



                    }


                    //textView.append( sno + " -- "+ date+ "  -- "+ det+"\n");

                }

                rowno++;


            }

*/

        } catch (Exception e) {

            //Log.e(TAG, "error "+ e.toString());

        }
    }

}







