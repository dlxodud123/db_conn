package Ex01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

class DB_con{
	private Connection con;
	private PreparedStatement ps;
	private ResultSet rs;
	public DB_con() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("오라클 기능 사용 가능(드라이브 로드)");
			String url = "jdbc:oracle:thin:@localhost:1521:orcl";
			String id = "c##LTY";
			String pwd = "1213";
			con = DriverManager.getConnection(url, id, pwd);
			System.out.println("db 연결 성공 : " + con);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	public ArrayList<MemberDTO> select() {
		String sql = "select * from member_test";
		ArrayList<MemberDTO> arr = new ArrayList<>();
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setAge(rs.getInt("age"));
				
				arr.add(dto);
				
				/*
				System.out.println(rs.getString("id"));
				System.out.println(rs.getString("pwd"));
				System.out.println(rs.getString("name"));
				System.out.println(rs.getInt("age"));
				System.out.println("----------------");
				*/
			}
			
			/*
			System.out.println(rs.next());
			System.out.println(rs.getString("id"));
			System.out.println(rs.getString("pwd"));
			System.out.println(rs.getString("name"));
			System.out.println(rs.getInt("age"));
			
			System.out.println(rs.next());
			System.out.println(rs.getString("id"));
			System.out.println(rs.getString("pwd"));
			System.out.println(rs.getString("name"));
			System.out.println(rs.getInt("age"));
			
			System.out.println(rs.next());
			*/
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}
	public MemberDTO selectOne(String userId) {
		String sql = "select * from member_test where id='"+userId+"'";
		//System.out.println(sql);
		MemberDTO dto = null;
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) { // 한명이므로 반복 필요 x
				dto = new MemberDTO();
				dto.setAge(rs.getInt("age"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setId(rs.getString("id"));
//				System.out.println(rs.getString("id"));
//				System.out.println(rs.getString("pwd"));
//				System.out.println(rs.getString("name"));
//				System.out.println(rs.getInt("age"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dto;
	}
	public int delete(String delId) {
		String sql = "delete from member_test where id = ?";
		int result = 0;
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, delId);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public int insert(MemberDTO d) {
		String sql = "insert into member_test values (?, ?, ?, ?)";
		int result = 0;
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, d.getId());
			ps.setString(2, d.getPwd());
			ps.setString(3, d.getName());
			ps.setInt(4, d.getAge());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
public class MainClass {
	public static void main(String[] args) {
		DB_con db = new DB_con();
		Scanner sc = new Scanner(System.in);
		int num;
		while (true) {
			System.out.println("1. 모든 목록 보기");
			System.out.println("2. 특정 사용자 보기");
			System.out.println("3. 데이터 추가");
			System.out.println("4. 데이터 삭제");
			System.out.print(">>> : ");
			num = sc.nextInt();
			switch (num) {
			case 1:
				ArrayList<MemberDTO> arr = db.select();
				System.out.println("---main---");
				for (MemberDTO dto : arr) {
					System.out.println("id : " + dto.getId());
					System.out.println("pwd : " + dto.getPwd());
					System.out.println("name : " + dto.getName());
					System.out.println("age : " + dto.getAge());
					System.out.println("---------------------");
				}
				break;
			case 2:
				System.out.print("검색 id 입력 : ");
				String userId = sc.next();
				MemberDTO dto = db.selectOne(userId);
				//System.out.println("dto : " + dto);
				if (dto != null) {
					System.out.println("id : " + dto.getId());
					System.out.println("pwd : " + dto.getPwd());
					System.out.println("name : " + dto.getName());
					System.out.println("age : " + dto.getAge());
				}else {
					System.out.println("존재하지 않는 아이디 입니다.");
				}
				break;
			case 3:
				MemberDTO d = new MemberDTO();
				while (true) {
					System.out.println("가입할 id 입력 : ");
					d.setId(sc.next());
					MemberDTO dd = db.selectOne(d.getId());
					if (dd == null) {
						break;
					}else {
						System.out.println("존재하는 id, 다시입력하세요");
					}
				}
				System.out.println("가입할 pwd 입력 : ");
				d.setPwd(sc.next());
				System.out.println("가입할 name 입력 : ");
				d.setName(sc.next());
				System.out.println("가입할 age 입력 : ");
				d.setAge(sc.nextInt());
				
				int res = db.insert(d);
				if (res == 1) {
					System.out.println("가입 성공");
				}else {
					System.out.println("존재하는 id는 안됨");
				}
				
				break;
			case 4:
				System.out.print("삭제할 id 입력 : ");
				String delId = sc.next();
				int re = db.delete(delId);
				if (re == 1) {
					System.out.println("삭제 성공");
				}else {
					System.out.println("존재하는 id가 없음");
				}
				break;
			default:
				break;
			}
		}
	}
}
