package net.gicp.zlfkyo.复习;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;




public class 测试 {
	public static void main(String[] args) throws Exception{
		Object[] objects=new Object[5];
		//sql(); 
		显示三角形(9);
	}
	public static void 显示三角形(int row){
		for (int i = 1; i <=row; i++) {
			for (int j = 1; j <=(row*2-1) ; j++) {
				if (j<=(row-i)) {
					System.out.print(" ");
				}else if(j>(row-i)&&j<=(row-i)+(i*2-1)) {
					if (j==row) {
						System.out.print(i);
					}else {
						System.out.print(i-1);
					}
				}
			}
			System.out.println();
		}
	}

	private static void sql() throws SQLException {
		Connection conn=null;
		String sql="";
	   Statement st = conn.createStatement();
	   ResultSet rs=null;
//	      st.executeQuery(sql);
//	   PreparedStatement ps = conn.prepareStatement(sql);
//	   ps.executeQuery();
	   
	   st = conn.createStatement();
	   rs = st.executeQuery(sql);
	   System.out.println(rs.getInt("id") + "\t" +      rs.getString("name")  + rs.getInt("age"));

	}

}
