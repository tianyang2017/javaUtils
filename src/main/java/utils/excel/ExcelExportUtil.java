package utils.excel;



import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.ImmutableList;

import utils.DateUtil;
import utils.beans.ChnMedicineDic;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : tianyang
 * @description :excel 注解导出工具类
 * @date :2018年10月31日
 */
public class ExcelExportUtil {

    //导出.xlsx 防止单表行数限制
    private static final  String XLSX=".xlsx";

    //转换中统一日期格式
    private static final String DATEFORMAT="yyyy-MM-dd HH:mm:ss";
    
    //转换中统一日期格式
    private static final String DATEFORMAT1="yyyy-MM-dd";

    /***
     * @description: 输出excel到指定文件目录
     * @param objectList bean集合
     * @param clazz  bean类型
     * @param outPath 输出路径
     * @param excelName 输出文件名称
     * @param sheetName 输出sheet名称
     * @param titleName 表格标题
     * @param titleRows 表格标题所占行数
     * @param titles 表头添加信息
     * @param zj 表尾添加信息
     * @param isStyle 内容是否加样式
     * @return : java.lang.String 返回文件的真实路径
   */
   public static String beanListToExcelFile(List<?> objectList,Class clazz,  String outPath, String excelName,
                                            String sheetName, String titleName, Integer titleRows,List<String> titles, Map<String,String> zj, boolean isStyle){
       try {
           String fullPath=outPath + excelName + XLSX;
           FileOutputStream fileOutputStream = new FileOutputStream(new File(fullPath));
           beanListToExcelStream(objectList,clazz, fileOutputStream,sheetName, titleName, titleRows, titles, zj, isStyle);
           return fullPath;
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
       return null;
   }

   /***
    * @description:  excel 到输出流
    * @param objectList bean集合
    * @param clazz  bean类型
    * @param outputStream 输出流
    * @param sheetName  表名称
    * @param titleName  表头名称
    * @param titleRows 表头所在行数
    * @param titles 表头添加信息
    * @param zj 表尾添加信息
    * @param isStyle 内容是否加样式
    * @return : void
    * isStyle 内容是否加样式
  */
  public static void beanListToExcelStream(List<?> objectList,Class clazz, OutputStream outputStream, 
		  String sheetName, String titleName, Integer titleRows, List<String> titles, 
		  Map<String,String> zj, boolean isStyle){

      List<Object> AllDataList = ListBeanToList(objectList, clazz);
      //获取头信息
      List<String> headlist = (List<String>)AllDataList.get(0);
      //获取主体数据信息
      List<List<Object>> dataList = (List<List<Object>>)AllDataList.get(1);

      //执行Excel写入逻辑
      try {
      	
      	XSSFWorkbook workbook=new XSSFWorkbook();
          XSSFSheet sheet=workbook.createSheet(sheetName);

          //全局总列数
          int commonColSize = headlist.size();
          for (int i=0;i<commonColSize;i++){
              sheet.autoSizeColumn(i);
          }
          
          //头所在的行数 受到是否有title的影响 默认是0   界面是从1 开始，但是程序是从0开始，所以传入的值要减去1
          titleRows = titleRows!=null ? titleRows-1:0;
          int headRowNum=0;//表头
          int dataRowNum=1;//数据

          //生成标题行 
          if (titleName!=null){
          	XSSFRow titleRow=sheet.createRow(headRowNum);
              XSSFCellStyle titleCellStyle=getColumnTopStyle(workbook);
              for (int i = 0; i < commonColSize; i++) {
              	XSSFCell titleCell=titleRow.createCell(i);
          		titleCell.setCellStyle(titleCellStyle);
				}

              XSSFCell titleCell=titleRow.createCell(0);
              sheet.addMergedRegion(new CellRangeAddress(headRowNum,titleRows,0,commonColSize-1));

              titleCell.setCellStyle(titleCellStyle);
              titleCell.setCellValue(titleName);
           	
              headRowNum=1+titleRows;//表头
              dataRowNum=2+titleRows;//数据
          }
          
          // 表头添加信息
          if (titles != null && titles.size() > 0){
          	
          	XSSFRow titleRow=sheet.createRow(headRowNum);
          	XSSFCellStyle titleCellStyle=getColumnTopStyle(workbook);
          	for (int i = 0; i < commonColSize; i++) {
          		XSSFCell titleCell=titleRow.createCell(i);
          		titleCell.setCellStyle(titleCellStyle);
				}
          	
          	int cell = commonColSize/titles.size();
          	int cellstart = 0;
              int cellend = cell-1;
              for (int i = 0; i < titles.size(); i++) {
              	XSSFCell titleCell=titleRow.createCell(cellstart);
                  sheet.addMergedRegion(new CellRangeAddress(headRowNum,headRowNum,cellstart,cellend));
                  cellstart = cellend+1;
                  if(i == (titles.size()-2)) {
                  	cellend = commonColSize-1;
                  }else {
                  	cellend += cell;
                  }
                  
                  titleCell.setCellStyle(titleCellStyle);
                  titleCell.setCellValue(titles.get(i));
				}
          	
              headRowNum=1+headRowNum;//表头
              dataRowNum=1+dataRowNum;//数据
          }
          
          // 表尾添加信息
          if(zj != null) {
          	Integer row = new Integer(zj.get("row"));
          	XSSFRow titleRow=sheet.createRow(row.intValue());
          	XSSFCellStyle titleCellStyle=getColumnTopStyle(workbook);
              XSSFCell hj=titleRow.createCell(3);
              sheet.addMergedRegion(new CellRangeAddress(row,row,3,3));
              hj.setCellStyle(titleCellStyle);
              hj.setCellValue("合计");
              
              XSSFCell cfts=titleRow.createCell(7);
              sheet.addMergedRegion(new CellRangeAddress(row,row,7,7));
              String ts = (String)zj.get("cfts");
              cfts.setCellStyle(titleCellStyle);	
              cfts.setCellValue(ts);
              
              XSSFCell zje=titleRow.createCell(10);
              sheet.addMergedRegion(new CellRangeAddress(row,row,10,commonColSize-1));
              String je = (String)zj.get("zje");
              zje.setCellStyle(titleCellStyle);
              zje.setCellValue(je);
              
          }
          

          //定义excel列头
          XSSFRow headRow = sheet.createRow(headRowNum);
          for (int i=0;i<commonColSize;i++){
              XSSFCell cell = headRow.createCell(i);
              cell.setCellType(XSSFCell.CELL_TYPE_STRING);
              cell.setCellStyle(getStyle(workbook));
              cell.setCellValue(headlist.get(i));
          }

          //写入excel数据
          for (int i=0;i<dataList.size();i++){
              List<Object> rowData=dataList.get(i);
              XSSFRow dataRow=sheet.createRow(dataRowNum+i);
              for (int j=0;j<commonColSize;j++){
                  XSSFCell dataCell = dataRow.createCell(j);
                  if(isStyle) {
                  	dataCell.setCellStyle(getStyle(workbook));
                  }
                  //判断写入数据的类型，并设置写入数据单元格的格式
                  Object object = rowData.get(j);
                  setCellTypePlus(dataCell,object);
              }
          }

          // 让列宽随着导出的列长自动适应
          for (int colNum = 0; colNum < commonColSize; colNum++) {
              int columnWidth = sheet.getColumnWidth(colNum) / 256;
         
              for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                  XSSFRow currentRow;
                  // 当前行未被使用过
                  if (sheet.getRow(rowNum) == null) {
                      currentRow = sheet.createRow(rowNum);
                  } else {
                      currentRow = sheet.getRow(rowNum);
                  }
                  if (currentRow.getCell(colNum) != null) {
                      XSSFCell currentCell = currentRow.getCell(colNum);
                      if (currentCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                          int length = currentCell.getStringCellValue()
                                  .getBytes().length;
                          if (columnWidth < length) {
                              columnWidth = length;
                          }
                      }
                  }
              }
              if (colNum == 0) {
                  sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
              } else {
                  sheet.setColumnWidth(colNum, columnWidth < 50 ? ((columnWidth + 4) * 256) : 50 * 256);
              }
          }
        	workbook.write(outputStream);

      }catch (Exception e){
          e.printStackTrace();
      }
  }

    /***
     * @description: beanlist 获取对应的导出到excel中的头信息集合，数据信息集合(嵌套集合，类似二维数组)
     * @param objectList bean 集合
     * @param clazz  bean 类型
     * @return : java.util.List<java.lang.Object> get(0)头信息 get(1) 数据信息嵌套集合
    */
    public static List<Object> ListBeanToList(List<?> objectList,Class clazz){

        List<Object> headListAndDataList=new ArrayList<>();

        Map<String,Method> nameAndMethodMap=new HashMap<>();

        //得到属性的名称和get方法
        Field[] fields = clazz.getDeclaredFields();
        for (Field field:fields){
            try {
                if (field.isAnnotationPresent(Excel.class)){
                        Excel annotation = field.getAnnotation(Excel.class);
                        String outName = annotation.outName();
                        //获得get方法
                        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                        Method readMethod=pd.getReadMethod();
                        nameAndMethodMap.put(outName,readMethod);
                       }
                    } catch (IntrospectionException e) {
                      e.printStackTrace();
                }

        }

        //bean to list headlist 只获取一次
        List<String> headList=new ArrayList<>();
        List<List<Object>> dataList=new ArrayList<>();

        for (int i=0;i<objectList.size();i++){

            List<Object> rowList=new ArrayList<>();
            Object object = objectList.get(i);
            Field[] declaredFields = object.getClass().getDeclaredFields();

            for (int j = 0; j < declaredFields.length; j++){
                try {
                    //存在需要导出的属性 获取它的值
                    if (declaredFields[j].isAnnotationPresent(Excel.class)){
                        //拿到输出头名称有且只拿一次
                        String outName = declaredFields[j].getAnnotation(Excel.class).outName();
                        if (i == 0) {
                            headList.add(outName);
                        }
                        //拿到值
                        Method readMethod = nameAndMethodMap.get(outName);
                        Object value = readMethod.invoke(object);
                        rowList.add(value);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
           dataList.add(rowList);
        }

        //返回
       headListAndDataList.add(headList);
       headListAndDataList.add(dataList);
       return headListAndDataList;
    }


    

   



    //设置单元和类型和值
    private static void setCellTypePlus(XSSFCell dataCell, Object object) {

        if (object instanceof Integer){                                   //Integer
            dataCell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
            dataCell.setCellValue((Integer)object);
        }else if (object instanceof  Double){                             //Double
            dataCell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
            dataCell.setCellValue((Double)object);
        }else if (object instanceof  Float){                              //Float
            dataCell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
            dataCell.setCellValue((Float)object);
        }else if (object instanceof Long){                                //Long
            dataCell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
            dataCell.setCellValue((Long)object);
        }else if (object instanceof String){                              //String
            dataCell.setCellType(XSSFCell.CELL_TYPE_STRING);
            dataCell.setCellValue((String)object);
        }else if (object instanceof Date){                                //Date
            dataCell.setCellType(XSSFCell.CELL_TYPE_STRING);
            String formatDate = DateUtil.formatDate((Date) object, DATEFORMAT);
            dataCell.setCellValue(formatDate);
        }else if (object instanceof Boolean){                             //Boolean
            dataCell.setCellType(XSSFCell.CELL_TYPE_BLANK);
            dataCell.setCellValue(String.valueOf(object));
        }else if (object == null){                                        //NULL
            dataCell.setCellType(XSSFCell.CELL_TYPE_BLANK);
            dataCell.setCellValue("");
        }else {
            dataCell.setCellValue(Cell.CELL_TYPE_STRING);
            dataCell.setCellValue("");
        }
    }


    // 表头单元格样式
    private static XSSFCellStyle getColumnTopStyle(XSSFWorkbook workbook) {

        // 设置字体
        XSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        return style;

    }

    //主体内容单元格样式
    private static XSSFCellStyle getStyle(XSSFWorkbook workbook) {
        // 设置字体
        XSSFFont font = workbook.createFont();
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        return style;
    }




    //测试
	public static void main(String[] args) {
		SimpleDateFormat df = new SimpleDateFormat(DATEFORMAT1);
		URL urlpath=Thread.currentThread().getContextClassLoader().getResource("");
		String outPath=urlpath.getPath().replace("target/classes", "test");
        String beginTime= df.format(new Date());
        String endTime= df.format(DateUtil.getDateBefore(new Date(), 1));
		//生成文件名相关数据
		//excel 到输出流
		//objectList bean集合
		//clazz  bean类型
		//outputStream 输出流
		//sheetName  表名称
		//titleName  表头名称
		///titleRows 表头所在行数
		//titles 副标题
		//zj 表尾添加信息
		//isStyle 内容是否加样式
       
		String excelName="中药处方明细表"+"  "+beginTime+" 至 "+endTime;
		String sheetName="中药处方明细表";
		String titleName="中药处方明细表";
		int titleRows=3;
		boolean isStyle=true;
		List<String> titles=ImmutableList.of(beginTime+" 至 "+endTime);
		Map<String,String> zj=null;
		List<ChnMedicineDic> lists =new ArrayList<ChnMedicineDic>();
		for(int i=0;i<2;i++){
			ChnMedicineDic  dic =new ChnMedicineDic();
			dic.setQytbdm("药品区域统编代码"+i);
			dic.setYpybdm("药品医保代码"+i);
			dic.setYptym("药品通用名"+i);
			dic.setYpbc("药品别称"+i);
			dic.setPzff("代煎"+i);
			dic.setBz("111"+i);
//			dic.setJldw("克"+i);
			lists.add(dic);
		}
		//返回数据封装到Excel中 不为空时候才生成
			String beanListToExcelFile = beanListToExcelFile(lists, ChnMedicineDic.class, outPath,
					excelName, sheetName, titleName, titleRows,titles,zj,isStyle);
    }
}
