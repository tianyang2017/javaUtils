package utils.pdf;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import utils.DateUtil;
import utils.RMBUtils;
import utils.ResourcesUtil;
/**
 * PDF工具
 */
public class PdfUtils {
	public static void main(String[] args)  {
		Date now=new Date();
		LoanIouData iouData=new LoanIouData();
		iouData.setBorrower("张三");
		iouData.setLoanAmount("9000");
		List<LoanRepaymentPlan> planList =new ArrayList<LoanRepaymentPlan>();
		for (int i=0 ;i<3;i++){
			LoanRepaymentPlan e=new LoanRepaymentPlan();
			e.setNum("1");
			e.setRepaymentTime(DateUtil.getDateBefore(now, 90-3*i));
			e.setReturnAmount(RMBUtils.format("30000000"));
			planList.add(e);
		}
		List<TransferAccount> accountsList=new ArrayList<TransferAccount>();
		TransferAccount b=new TransferAccount();
		b.setCertPan("12114156543663774");
		b.setName("张三");
		b.setOpenBankName("中国银行快乐支行");
		accountsList.add(b);
		List<TransferAccount> interstAccountsList=new ArrayList<TransferAccount>();
		b.setCertPan("12114156543663774");
		b.setName("张三");
		b.setOpenBankName("中国银行快乐支行");
		interstAccountsList.add(b);
		String orderCode="财智金";
		
		String path=turnToPdf( iouData, planList, accountsList,  interstAccountsList,  orderCode);
		System.out.println(path);
	}

    /**
     * 生成
     * @param iouData 借据数据
     * @param planList 还款计划List
     * @param accountsList 收本金账户
     * @param interstAccountsList 收息账户
     * @param orderCode
     * @return
     */
    public static String turnToPdf(LoanIouData iouData,List<LoanRepaymentPlan> planList, 
    		List<TransferAccount> accountsList, List<TransferAccount> interstAccountsList, String orderCode){

//        String basePath = PropertiesUtil.getValue("pdf.path","/application.properties");
        String basePath =ResourcesUtil.getValue("uploadFilePath","pdf.path");
        basePath=Thread.currentThread().getContextClassLoader().getResource("").getPath()+basePath;
        File file = new File(basePath);
        if(!file.exists()){
             file.mkdirs();
        }
        String path = "";

        try {
            //创建文件
            Document document = new Document();
            //建立一个书写器
            path = basePath+orderCode+"_还款计划表.pdf";
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));

            //打开文件
            document.open();

            //中文字体,解决中文不能显示问题
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font font = new Font(bfChinese);//正常字体
            Font fontBold = new Font(bfChinese, 12, Font.BOLD);//正常加粗字体
            Font fontBig = new Font(bfChinese, 20);//大字体
            Font fontBigBold = new Font(bfChinese, 20, Font.BOLD);//加粗大字体

            //添加主题
            Paragraph theme = new Paragraph("还款计划表", fontBigBold);
            theme.setAlignment(Element.ALIGN_CENTER);
            document.add(theme);

            //借款信息
            Paragraph peopleInfo = new Paragraph("借款人："+iouData.getBorrower()+"    "+"借款金额："+iouData.getLoanAmount()+"万元", font);
            peopleInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(peopleInfo);

            //加入空行
            //Paragraph blankRow = new Paragraph(18f, " ");
            //document.add(blankRow);

            //TODO Table1 还款计划 : 3列的表.-------------------------------------
            PdfPTable table1 = new PdfPTable(3);
            table1.setWidthPercentage(90); // 宽度100%填充
            table1.setSpacingBefore(20f); // 前间距
            table1.setSpacingAfter(20f); // 后间距

            List<PdfPRow> listRow = table1.getRows();
            //设置列宽
            float[] columnWidths = {1f, 2f, 3f};
            table1.setWidths(columnWidths);

            //行1
            PdfPCell cells0[] = new PdfPCell[3];
            PdfPRow row0 = new PdfPRow(cells0);
            //单元格
            cells0[0] = new PdfPCell(new Paragraph("期数", fontBold));//单元格内容
            cells0[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
            cells0[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
            cells0[0].setFixedHeight(25f);
            cells0[0].setBackgroundColor(BaseColor.LIGHT_GRAY);
            cells0[1] = new PdfPCell(new Paragraph("日期", fontBold));
            cells0[1].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
            cells0[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
            cells0[1].setBackgroundColor(BaseColor.LIGHT_GRAY);
            cells0[2] = new PdfPCell(new Paragraph("金额（元）", fontBold));
            cells0[2].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
            cells0[2].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
            cells0[2].setBackgroundColor(BaseColor.LIGHT_GRAY);

            listRow.add(row0);

            //插入还款内容
            for (LoanRepaymentPlan plan : planList) {
                PdfPCell cells[] = new PdfPCell[3];
                PdfPRow row = new PdfPRow(cells);
                cells[0] = new PdfPCell(new Paragraph(String.valueOf(plan.getNum())));
                cells[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells[0].setFixedHeight(25f);
                cells[1] = new PdfPCell(new Paragraph(String.valueOf(DateUtil.formatDate(plan.getRepaymentTime(), "yyyy/MM/dd"))));
                cells[1].setFixedHeight(25f);
                cells[1].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells[2] = new PdfPCell(new Paragraph(String.valueOf(plan.getReturnAmount())));
                cells[2].setFixedHeight(25f);
                cells[2].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells[2].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                //把第单元格添加到集合（行）
                listRow.add(row);
            }


            //TODO Table2 账户列表 :  2列的表.------------------------------------------------------------------------

            PdfPTable table2 = new PdfPTable(2);
            table2.setWidthPercentage(90); // 宽度100%填充
            table2.setSpacingBefore(20f); // 前间距
            table2.setSpacingAfter(20f); // 后间距
            List<PdfPRow> listRow2 = table2.getRows();
            //设置列宽
            float[] columnWidths2 = {1f, 2f};
            table2.setWidths(columnWidths2);
            //TODO --第一部分
            PdfPCell cells2[] = new PdfPCell[2];
            PdfPRow row2 = new PdfPRow(cells2);
            cells2[0] = new PdfPCell(new Paragraph("还息账号信息", fontBold));//单元格内容
            cells2[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
            cells2[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
            cells2[0].setFixedHeight(25f);
            cells2[0].setBackgroundColor(BaseColor.LIGHT_GRAY);
            cells2[0].setColspan(2);//合并单元格
            listRow2.add(row2);

            for(TransferAccount interstAccount : interstAccountsList){
                PdfPCell cells3[] = new PdfPCell[2];
                PdfPRow row3 = new PdfPRow(cells3);
                cells3[0] = new PdfPCell(new Paragraph("还息账号", font));//单元格内容
                cells3[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells3[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells3[0].setFixedHeight(25f);

                cells3[1] = new PdfPCell(new Paragraph(interstAccount.getCertPan(), font));//单元格内容
                cells3[1].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells3[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells3[1].setFixedHeight(25f);

                listRow2.add(row3);

                PdfPCell cells4[] = new PdfPCell[2];
                PdfPRow row4 = new PdfPRow(cells4);
                cells4[0] = new PdfPCell(new Paragraph("还息户名", font));//单元格内容
                cells4[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells4[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells4[0].setFixedHeight(25f);

                cells4[1] = new PdfPCell(new Paragraph(interstAccount.getName(), font));//单元格内容
                cells4[1].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells4[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells4[1].setFixedHeight(25f);
                listRow2.add(row4);

                PdfPCell cells5[] = new PdfPCell[2];
                PdfPRow row5 = new PdfPRow(cells5);
                cells5[0] = new PdfPCell(new Paragraph("还息开户行", font));//单元格内容
                cells5[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells5[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells5[0].setFixedHeight(25f);

                cells5[1] = new PdfPCell(new Paragraph(interstAccount.getOpenBankName(), font));//单元格内容
                cells5[1].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells5[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells5[1].setFixedHeight(25f);

                listRow2.add(row5);
            }





            //TODO --第2部分
            PdfPCell cells6[] = new PdfPCell[2];
            PdfPRow row6 = new PdfPRow(cells6);
            cells6[0] = new PdfPCell(new Paragraph("还本金账号信息", fontBold));//单元格内容
            cells6[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
            cells6[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
            cells6[0].setFixedHeight(25f);
            cells6[0].setBackgroundColor(BaseColor.LIGHT_GRAY);
            cells6[0].setColspan(2);//合并单元格
            listRow2.add(row6);

            for(TransferAccount account : accountsList){

                PdfPCell cells7[] = new PdfPCell[2];
                PdfPRow row7 = new PdfPRow(cells7);
                cells7[0] = new PdfPCell(new Paragraph("还本金账号", font));//单元格内容
                cells7[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells7[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells7[0].setFixedHeight(25f);

                cells7[1] = new PdfPCell(new Paragraph(account.getCertPan(), font));//单元格内容
                cells7[1].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells7[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells7[1].setFixedHeight(25f);

                listRow2.add(row7);

                PdfPCell cells8[] = new PdfPCell[2];
                PdfPRow row8 = new PdfPRow(cells8);
                cells8[0] = new PdfPCell(new Paragraph("还本金户名", font));//单元格内容
                cells8[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells8[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells8[0].setFixedHeight(25f);

                cells8[1] = new PdfPCell(new Paragraph(account.getName(), font));//单元格内容
                cells8[1].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells8[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells8[1].setFixedHeight(25f);

                listRow2.add(row8);

                PdfPCell cells9[] = new PdfPCell[2];
                PdfPRow row9 = new PdfPRow(cells9);
                cells9[0] = new PdfPCell(new Paragraph("还本金开户行", font));//单元格内容
                cells9[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells9[0].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells9[0].setFixedHeight(25f);

                cells9[1] = new PdfPCell(new Paragraph(account.getOpenBankName(), font));//单元格内容
                cells9[1].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells9[1].setVerticalAlignment(Element.ALIGN_MIDDLE);//垂直居中
                cells9[1].setFixedHeight(25f);

                listRow2.add(row9);

            }


            //把表格添加到文件中
            document.add(table1);
            document.add(table2);

            //关闭文档
            document.close();
            //关闭书写器
            writer.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return path;
    }
}