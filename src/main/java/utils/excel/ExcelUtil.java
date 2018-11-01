package utils.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import jxl.write.Number;


public class ExcelUtil {
	private static Object[] objects;
	public static void createDir(String path){
		new File(path).mkdirs();
	}
	public static WritableWorkbook createWorkBook(String outPath) throws IOException{
		return Workbook.createWorkbook(new File(outPath));
	}
	public static WritableCellFormat createStyle(WritableSheet sheet,String titleStr) throws RowsExceededException, WriteException{
		WritableFont bold = new WritableFont(WritableFont.ARIAL, 11,
				WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
		bold.setPointSize(14);
		WritableCellFormat titleFormate = new WritableCellFormat(bold);
		titleFormate.setBackground(jxl.format.Colour.LIGHT_GREEN); // 设置单元格的背景颜色
		titleFormate.setAlignment(jxl.format.Alignment.CENTRE);// 设置对齐方式
		titleFormate.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //设置边框
		titleFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		Label title = new Label(0,0,titleStr,titleFormate);
		sheet.setRowView(0, 600, false);
		sheet.addCell(title);
		sheet.mergeCells(0, 0, 3, 0); //合并单元格
		sheet.setColumnView(0, 15); // 设置列的宽度
		return titleFormate;
	};
	public static void createHead(WritableSheet sheet,String []head) throws RowsExceededException, WriteException{
		WritableFont headFont = new WritableFont(WritableFont.ARIAL, 11,
				WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
		WritableCellFormat headFormate = new WritableCellFormat(headFont);
		headFormate.setBackground(jxl.format.Colour.WHITE); // 设置单元格的背景颜色
		headFormate.setAlignment(jxl.format.Alignment.CENTRE);// 设置对齐方式
		headFormate.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //设置边框
		headFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		for(int x=0;x<head.length;x++){
			Label title = new Label(x,1,head[x],headFormate);
			sheet.addCell(title);
		}
	}
	public static void createData(WritableSheet sheet,Object [][] dataVar) throws RowsExceededException, WriteException{
		for(int y=0;y<dataVar.length;y++){
			createData(sheet,dataVar[y],y+2);
		}	
	}
	public static void createData(WritableSheet sheet,Object [] dataVar,int x) throws RowsExceededException, WriteException{
		WritableFont bodyFont = new WritableFont(WritableFont.ARIAL, 11,
				WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
		WritableCellFormat bodyFormate = new WritableCellFormat(bodyFont);
		bodyFormate.setBackground(jxl.format.Colour.WHITE); // 设置单元格的背景颜色
		bodyFormate.setAlignment(jxl.format.Alignment.CENTRE);// 设置对齐方式
		bodyFormate.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK); //设置边框
		bodyFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		for(int i=0;i<dataVar.length;i++){
			Object obj=dataVar[i];
			if(isNull(obj)){
				obj="";
			};
			
			if(obj instanceof Date){
				DateTime dateTime=new DateTime(i,x,(Date)obj,bodyFormate);
				sheet.addCell(dateTime);
			}else if(obj instanceof Double){
				Number number=new Number(i,x,(Double)obj,bodyFormate);
				sheet.addCell(number);
			}else if(obj instanceof Integer){
				Number number=new Number(i,x,(Integer)obj,bodyFormate);
				sheet.addCell(number);
			}else{
				Label data = new Label(i,x,obj+"",bodyFormate);
				sheet.addCell(data);
			}
		}	
	}
	public static boolean isNull(Object obj){
		if(null==obj){
			return true;
		}
		return false;
	}
	public static void closeWorkBook(WritableWorkbook workbook) throws Exception{
		if(null!=workbook){
			try {
				if(null!=workbook){
					workbook.close();
					workbook=null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}
	}
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		String outPath="D:\\workspace\\zhongzhi\\javaUtils\\test\\test.xls";
		WritableWorkbook workbook=null;
		String title="学生明细表";
		String []head={"姓名","性别","年龄","班级"};
		Object [][]data={{"张三","男","18","1年级"},{"张红","女","17","1年级"}};
		try{
			workbook =createWorkBook(outPath);
			WritableSheet sheet = workbook.createSheet(title,0);
			createStyle( sheet, title);
			createHead(sheet,head);
			createData(sheet,data);
			//写入数据
			workbook.write();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("create excel djmx exception"+e.getMessage());
		}finally{
			try {
				//关闭文件 
				ExcelUtil.closeWorkBook(workbook);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException("close workbook djmx exception"+e.getMessage());
			}
		}

	}
}