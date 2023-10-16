package com.my.mango.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.Scanner;

import com.my.exception.FindException;
import com.my.mango.dto.Restaurants;
import com.my.mango.sql.MyConnection;

public class RestaurantsRepository {

	Scanner sc = new Scanner(System.in);
	PreparedStatement pstmt=null;
	ResultSet rs = null;
	Statement stmt = null;

	/*사업자모드 음식점등록 작업 repository*/
	public void restaurantAdd(Restaurants res) {
		Connection conn = null;
		try {
			conn = MyConnection.getConnection(); //연결객체생성
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
		String insertSQL = "INSERT INTO restaurants (business_number, restaurants_type, restaurants_name, address, user_id,status) VALUES (?, ?, ?, ?, ?,?)";
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement(insertSQL);
			pstmt.setInt(1,res.getBusiness_number());
			pstmt.setString(2,res.getRestaurants_type());
			pstmt.setString(3,res.getRestaurants_name());
			pstmt.setString(4,res.getAddress());
			pstmt.setString(5,res.getUser_id());
			pstmt.setString(6,res.getStatus());

			int rowcnt=pstmt.executeUpdate(); //UPDATE등 할때 ,처리건수 int로 반환
			if(rowcnt==0) {
				System.out.println("잘못된 입력으로 음식점등록이 불가능합니다.");
			}else {
				System.out.println("음식점등록에 성공하였습니다.");
			}

		}catch(SQLSyntaxErrorException e) {
			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();

		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}

	}
	/*사업자모드 음식점 조회 작업 repository*/
	public void restaurantAll(String userId) throws FindException {
		Connection conn = null;
		try {
			conn = MyConnection.getConnection(); //연결객체생성
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return ;
		}
		PreparedStatement pstmt=null; 
		ResultSet rs = null;
		try {
			String selectSQL="SELECT rownum, a.*FROM (SELECT * FROM restaurants WHERE restaurants.user_id=?) a";
			pstmt = conn.prepareStatement(selectSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				throw new FindException("등록된 음식점이 없습니다.");
			}

			System.out.printf("%-5s %-5s %-2s %-5s %-5s\n","No","사업자번호","종류","음식점이름","상태","주소");
			int rowNum=0;
			do {
				rowNum = rs.getInt(1);
				int b_n = rs.getInt(2);
				String type = rs.getString(3);
				String name = rs.getString(4);
				String status = rs.getString(5);
				String address=rs.getString(6);
				System.out.println("( "+rowNum + " ) :" 
						+ b_n + "-" + type +"-" + name +"-"+status+"-"+address);
			}while (rs.next());

			System.out.println();
			System.out.print("No 선택: ");
			int selectedRowNum;
			try{
				selectedRowNum = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("숫자를 입력해주세요.");
				return;
			}

			if (selectedRowNum < 1 || selectedRowNum > rowNum) {
				System.out.println("유효하지 않은 선택입니다.");
				return;
			}

			rs.absolute(selectedRowNum); // 선택한 rownum으로 커서 이동

			int selectedBusinessnumber = rs.getInt(2); // 선택한 음식점의 사업자번호

			// 선택한 음식점의 메뉴와 가격 출력
			String menuSelectSQL = "SELECT rownum, menus.menu_id, menus.menu_name, menus.menu_price FROM (SELECT * FROM menus WHERE business_number = ? ORDER BY menu_id) menus";
			PreparedStatement menuStmt = null;
			ResultSet menuRs = null;
			try {
				menuStmt = conn.prepareStatement(menuSelectSQL);
				menuStmt.setInt(1, selectedBusinessnumber);
				menuRs = menuStmt.executeQuery();
				System.out.println("[메뉴 가격 목록]");
				System.out.println("-------------------------------------------------------------------------------------------");
				System.out.printf("%-5s %-20s %-10s\n", "No","메뉴", "가격");
				System.out.println("-------------------------------------------------------------------------------------------");

				while (menuRs.next()) {
					int no=menuRs.getInt(1);
					String menu = menuRs.getString(3);
					int price = menuRs.getInt(4);
					System.out.printf("%-5d %-20s %-10d\n", no, menu, price);
				}
			}catch (SQLException e) {
				e.printStackTrace();
			} finally {
				MyConnection.close(menuRs, menuStmt,null); // 메뉴 관련 리소스 닫기
			}

			String reviewSelectSql = "SELECT review_number, user_id, comments, TO_CHAR(comments_date, 'YY/MM/DD'), rating\n"
					+ "FROM reviews\n"
					+ "WHERE business_number = ?";
			PreparedStatement reviewStmt = null;
			ResultSet reviewRs = null;
			try {
				reviewStmt = conn.prepareStatement(reviewSelectSql);
				reviewStmt.setInt(1, selectedBusinessnumber);
				reviewRs = reviewStmt.executeQuery();
				System.out.println("[전체 리뷰 목록]");
				System.out.println("---------------------------------------------------------------------------------------------------------");
				System.out.printf("%-10s %-20s %-50s %-12s %-8s\n", "리뷰 번호", "사용자 ID", "코멘트", "작성일자", "별점");

				while (reviewRs.next()) {
					int reviewNumber = reviewRs.getInt(1);
					String userId1 = reviewRs.getString(2);
					String comments = reviewRs.getString(3);
					String commentsDate = reviewRs.getString(4);
					int rating = reviewRs.getInt(5);

					System.out.printf("%-10d %-20s %-50s %-12s %-8d\n", reviewNumber, userId1, comments, commentsDate.toString(), rating);

				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				MyConnection.close(reviewRs, reviewStmt,null); // 리뷰 관련 리소스 닫기
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			MyConnection.close(rs, pstmt, conn);
		}
	}
	/*사업자모드 음식점 상태 변경*/
	public void Update (String userId) throws FindException {
		Connection conn = null;
		try {
			conn = MyConnection.getConnection(); //연결객체생성
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return ;
		}
		PreparedStatement pstmt=null; 
		ResultSet rs = null;

		try {
			String selectSQL="SELECT rownum, a.*FROM (SELECT * FROM restaurants WHERE restaurants.user_id=?) a";
			pstmt = conn.prepareStatement(selectSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(!rs.next()) {
				throw new FindException("등록된 음식점이 없습니다.");
			}
			System.out.printf("%-5s %-5s %-2s %-5s %-5s\n","No","사업자번호","종류","음식점이름","상태","주소");
			int rowNum=0;
			do {
				rowNum = rs.getInt(1);
				int b_n = rs.getInt(2);
				String type = rs.getString(3);
				String name = rs.getString(4);
				String status = rs.getString(5);
				String address=rs.getString(6);
				System.out.println("( "+rowNum + " ) :" 
						+ b_n + "-" + type +"-" + name +"-"+status+"-"+address);
			}while (rs.next());

			System.out.print("변경할 음식점의 번호(No)를 선택하세요 : ");
			int selectedRowNum;
			try{
				selectedRowNum = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("숫자를 입력해주세요.");
				return;
			}

			if (selectedRowNum < 1 || selectedRowNum > rowNum) {
				System.out.println("유효하지 않은 선택입니다.");
				return;
			}
			rs.absolute(selectedRowNum); // 선택한 rownum으로 커서 이동
			int selectedBusinessnumber = rs.getInt(2); // 선택한 음식점의 사업자번호

			while(true) {
				System.out.println("---------------------------------------------------------------------------------");
				System.out.print("1.종류변경 / 2.이름변경 / 3.상태변경 / 4.주소변경 / 0.뒤로가기 : ");
				String opt1=sc.nextLine();
				String upStr="";
				if(opt1.equals("1")) {
					upStr="restaurants_type";
				}else if(opt1.equals("2")) {
					upStr="restaurants_name";
				}else if(opt1.equals("3")) {
					upStr="status";
				}else if(opt1.equals("4")) {
					upStr="address";
				}else if(opt1.equals("0")) {
					return;
				}else {
					System.out.println("잘못입력하셨습니다.");
					return;
				}
				System.out.println("---------------------------------------------------------------------------------");
				System.out.print("변경 데이터 입력 : ");
				String change=sc.nextLine();

				String updateSQL="UPDATE restaurants SET "+ upStr +"=? WHERE business_number=?";
				PreparedStatement updateStmt = null;
				try {				
					updateStmt=conn.prepareStatement(updateSQL);
					updateStmt.setString(1,change);		
					updateStmt.setInt(2,selectedBusinessnumber);
					int rowcnt=updateStmt.executeUpdate();

					if(rowcnt==0) {
						System.out.println("수정할 데이터가 없습니다.");
					}else { 
						selectSQL="SELECT * FROM restaurants WHERE restaurants.business_number=?";
						pstmt = conn.prepareStatement(selectSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
						pstmt.setInt(1,selectedBusinessnumber);
						rs = pstmt.executeQuery();
						rs.next();
						String type = rs.getString(2);
						String name = rs.getString(3);
						String status = rs.getString(4);
						String address=rs.getString(5);
						System.out.println("["+type+"-"+name+"-"+status+"-"+address+"] 데이터 변경 완료!");
						System.out.println(rowcnt+"건의 데이터가 수정되었습니다.");

					}
				} catch (SQLException e){
					e.printStackTrace();
				} finally {
					if (updateStmt != null) {
						updateStmt.close();
					}
				}
			}
		} catch(NumberFormatException  e) {

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			MyConnection.close(rs, pstmt, conn);
		}

	}
	/*사용자모드 음식점 조회 작업 repository*/
	public void restaurantsView(String userTypeInput, String userOrderInput) throws FindException {
		Connection conn = null;
		String type="";
		try {
			conn = MyConnection.getConnection(); // 연결 객체 생성
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}

		if (userTypeInput.equals("1")) {
			type = "한식";
		} else if (userTypeInput.equals("2")) {
			type = "중식";
		} else if (userTypeInput.equals("3")) {
			type = "일식";
		} else if (userTypeInput.equals("4")) {
			type = "양식";
		} else if (userTypeInput.equals("5")) {
			type = "분식";
		} 


		try {
			String selectSQL = "SELECT 음식점이름, 음식점종류, 주소, NVL(평균별점, 0) AS 평균별점, NVL(후기수, 0) AS 후기수\r\n"
					+ "FROM (\r\n"
					+ "    SELECT restaurants_name AS 음식점이름,\r\n"
					+ "           restaurants_type AS 음식점종류,\r\n"
					+ "           address AS 주소,\r\n"
					+ "           AVG(rv.rating) AS 평균별점,\r\n"
					+ "           COUNT(rv.review_number) AS 후기수\r\n"
					+ "    FROM restaurants r\r\n"
					+ "    LEFT JOIN reviews rv ON r.business_number = rv.business_number\r\n";

			if (type != "") {
				selectSQL += "    WHERE r.restaurants_type = ? AND r.status = '영업'\r\n";
			} else {
				selectSQL += "    WHERE r.status = '영업'\r\n";
			}

			selectSQL += "    GROUP BY r.restaurants_name, r.restaurants_type, r.address\r\n"
					+ ") subquery"; 

			if (userOrderInput.equals("1")) { //후기순정렬
				selectSQL += " ORDER BY 후기수 DESC\r\n";
			} else if (userOrderInput.equals("2")) { //별점순정렬
				selectSQL += " ORDER BY 평균별점 DESC\r\n";
			} else { //이름순정렬
				selectSQL += " ORDER BY 음식점이름 ASC\r\n";
			}

			pstmt = conn.prepareStatement(selectSQL);

			// 매개변수 바인딩
			if (type != "") {
				pstmt.setString(1, type);
			}

			ResultSet rs = pstmt.executeQuery();

			if (!rs.next()) {
				throw new FindException("등록된 음식점이 없습니다");
			}
			System.out.println("-------------------------------------------------------------------------------------");
			System.out.printf("%-20s %-10s %-30s %-10s %-8s\n", "음식점 이름", "종류", "주소", "평균 별점", "후기 수");
			System.out.println("-------------------------------------------------------------------------------------");

			do {
				String restaurantName = rs.getString("음식점이름");
				String restaurantType = rs.getString("음식점종류");
				String address = rs.getString("주소");
				double avgRating = rs.getDouble("평균별점");
				int reviewCnt = rs.getInt("후기수");
				System.out.printf("%-20s %-10s %-30s %.2f %8d\n", restaurantName, restaurantType, address, avgRating, reviewCnt);
			} while (rs.next());

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.close(rs, pstmt, conn);
		}

	}
	/*사용자모드 음식점 이름 조회 작업 repository*/
	public void restaurantsNameView(String searchName) throws FindException {
		Connection conn = null;
		try {
			conn = MyConnection.getConnection(); //연결객체생성
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}

		try {
			String selectSQL = "SELECT *\r\n"
					+ "FROM (\r\n"
					+ "    SELECT rownum rn, 음식점이름, 음식점종류, 주소, 평균별점, 후기수, 사업자번호\r\n"
					+ "    FROM (\r\n"
					+ "        SELECT restaurants_name AS 음식점이름,\r\n"
					+ "               restaurants_type AS 음식점종류,\r\n"
					+ "               address AS 주소,\r\n"
					+ "               AVG(rv.rating) AS 평균별점,\r\n"
					+ "               COUNT(rv.review_number) AS 후기수,\r\n"
					+ "               r.business_number AS 사업자번호\r\n"
					+ "        FROM restaurants r\r\n"
					+ "        LEFT JOIN reviews rv ON r.business_number = rv.business_number\r\n"
					+ "        WHERE r.restaurants_name LIKE ? AND r.status = '영업'\r\n"
					+ "        GROUP BY r.restaurants_name, r.restaurants_type, r.address, r.business_number\r\n"
					+ "        ORDER BY 음식점이름 ASC\r\n"
					+ "    )\r\n"
					+ ")\r\n";

			pstmt = conn.prepareStatement(selectSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pstmt.setString(1, "%" + searchName + "%");
			rs = pstmt.executeQuery();
			if(!rs.next()) {
				throw new FindException("입력하신 조건에 맞는 음식점이 존재하지 않습니다.");
			}
			System.out.println("-------------------------------------------------------------------------------------------");
			System.out.printf("%-3s %-20s %-10s %-30s %-10s %-8s\n", "No", "음식점 이름", "종류", "주소", "평균 별점", "후기 수");
			System.out.println("-------------------------------------------------------------------------------------------");
			int rowNum=0;
			do {
				rowNum = rs.getInt(1);
				String restaurantName = rs.getString(2);
				String restaurantType = rs.getString(3);
				String address = rs.getString(4);
				int avgRating = rs.getInt(5);
				int reviewCnt = rs.getInt(6);         
				System.out.printf("%-3d %-20s %-10s %-30s %-10d %-8d\n", rowNum, restaurantName, restaurantType, address, avgRating, reviewCnt);
			} while (rs.next());
			System.out.println();
			System.out.print("No 선택 : ");
			int selectedRowNum;
			try{
				selectedRowNum = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("숫자를 입력해주세요.");
				return;
			}

			if (selectedRowNum < 1 || selectedRowNum > rowNum) {
				System.out.println("유효하지 않은 선택입니다.");
				return;
			}

			rs.absolute(selectedRowNum); // 선택한 rownum으로 커서 이동

			int selectedBusinessnumber = rs.getInt(7); // 선택한 음식점의 사업자번호

			// 선택한 음식점의 메뉴와 가격 출력
			String menuSelectSQL = "SELECT menu_name, menu_price FROM menus WHERE business_number =? ORDER BY menu_name";
			pstmt = conn.prepareStatement(menuSelectSQL);
			pstmt.setInt(1, selectedBusinessnumber);
			rs = pstmt.executeQuery();
			System.out.println("[메뉴 가격 목록]");
			System.out.println("-------------------------------------------------------------------------------------------");
			System.out.printf("%-20s %-10s\n", "메뉴", "가격");
			System.out.println("-------------------------------------------------------------------------------------------");

			while (rs.next()) {
				String menu = rs.getString(1);
				int price = rs.getInt(2);
				System.out.printf("%-20s %-10d\n", menu, price);
			}


			String reviewSelectSql = "SELECT review_number, user_id, comments, TO_CHAR(comments_date, 'YY/MM/DD'), rating\n"
					+ "FROM reviews\n"
					+ "WHERE business_number = ?";
			pstmt = conn.prepareStatement(reviewSelectSql);
			pstmt.setInt(1, selectedBusinessnumber);
			rs = pstmt.executeQuery();
			System.out.println("[전체 리뷰 목록]");
			System.out.println("---------------------------------------------------------------------------------------------------------");
			System.out.printf("%-10s %-20s %-50s %-12s %-8s\n", "리뷰 번호", "사용자 ID", "코멘트", "작성일자", "별점");

			while (rs.next()) {
				int reviewNumber = rs.getInt(1);
				String userId = rs.getString(2);
				String comments = rs.getString(3);
				String commentsDate = rs.getString(4);
				int rating = rs.getInt(5);

				System.out.printf("%-10d %-20s %-50s %-12s %-8d\n", reviewNumber, userId, comments, commentsDate.toString(), rating);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}  finally {
			MyConnection.close(rs, pstmt, conn);
		}
	}

}
