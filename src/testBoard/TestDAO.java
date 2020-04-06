package testBoard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TestDAO {
	private String url="jdbc:oracle:thin:@192.168.0.58:1521:xe";
	private String user="jsp",pwd="1234";
	private Connection con;
	private PreparedStatement ps;
	private ResultSet rs;
	
	public TestDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {e.printStackTrace();}
		
	}
	public ArrayList<TestDTO> list(int start){
		//String sql="select * from paging";
		//String sql="select * from paging order by num desc";
		String sql="select B.* from(select rownum rn,A.* from(select * from paging order by num desc)A)B where rn between ? and ?";
		ArrayList<TestDTO> listDto = new ArrayList<TestDTO>();
		try {

			if(start == 0)start=1;
			
			con = DriverManager.getConnection(url,user,pwd);
			ps = con.prepareStatement(sql);
			ps.setInt(1, (start - 1) * 3 + 1);
			ps.setInt(2,  3 * start);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				TestDTO dto = new TestDTO();
				dto.setNum(rs.getInt("num"));
				dto.setTitle(rs.getString("title"));
				dto.setCount(rs.getInt("count"));
				dto.setPdate(rs.getString("pdate"));
				listDto.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listDto;
	}
	public void insert(TestDTO dto) {
		String sql="insert into paging (num,title,pdate,count)"+
						"values(test_num.nextval,?,sysdate,0)";
		try {
			con=DriverManager.getConnection(url,user,pwd);
			ps=con.prepareStatement(sql);
			ps.setString(1, dto.getTitle());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void count(String num) {
		String sql="update paging set count = count+1 where num="+num;
		try {
			con = DriverManager.getConnection(url,user,pwd);
			ps = con.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int getTotalPage() {
		String sql="select count(*) from paging";
		int totPage=0;
		try {
			con=DriverManager.getConnection(url,user,pwd);
			ps=con.prepareStatement(sql);
			rs=ps.executeQuery();
			if(rs.next()) totPage=rs.getInt(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(totPage%3==0) {
			totPage=totPage/3;
		}else if(totPage%3>0) {
			totPage=totPage/3;
			totPage+=1;
		}
		return totPage;
	}
	
}
