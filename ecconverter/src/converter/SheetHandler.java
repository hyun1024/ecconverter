package converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.binary.XSSFBSheetHandler.SheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

public class SheetHandler implements SheetContentsHandler{
    private int currentCol = -1;
    private int currRowNum = 0;

    public List<List<String>> rows = new ArrayList<List<String>>();    //실제 엑셀을 파싱해서 담아지는 데이터
    public List<String> row = new ArrayList<String>();
    public List<String> header = new ArrayList<String>();

	@Override
	public void cell(String columnName, String value, XSSFComment var3) {
	      int iCol = (new CellReference(columnName)).getCol();
	        int emptyCol = iCol - currentCol - 1;

	        for (int i = 0; i < emptyCol; i++) {
	            row.add("");
	        }
	        currentCol = iCol;
	        row.add(value);
		
	}

	@Override
	public void endRow(int rowNum) {
        if (rowNum == 0) {
            header = new ArrayList<String>(row);
        } else {
            if (row.size() < header.size()) {
                for (int i = row.size(); i < header.size(); i++) {
                    row.add("");
                }
            }
            rows.add(new ArrayList<String>(row));
        }
        row.clear();
		
	}

	@Override
	public void startRow(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hyperlinkCell(String arg0, String arg1, String arg2, String arg3, XSSFComment arg4) {
		// TODO Auto-generated method stub
		
	}
	
	List<List<String>> getRows(){
		return rows;
	}
	
	List<String> getHeader(){
		return header;
	}

}
