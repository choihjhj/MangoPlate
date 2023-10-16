package com.my.mango.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.my.exception.FindException;
import com.my.mango.dto.Reviews;
import com.my.mango.sql.MyConnection;

public class ReviewsRepository {


	Scanner sc = new Scanner(System.in);
	/*리뷰작성 */
	public void writeReview(Reviews review) {
		Connection conn = null;
		try {
			conn = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
		PreparedStatement pstmt = null;

		try {
			String InsertSQL = "INSERT INTO reviews(review_number, business_number, user_id, comments, comments_date, rating) VALUES (review_seq.NEXTVAL,?,?,?,SYSDATE,?)";

			pstmt = conn.prepareStatement(InsertSQL);
			pstmt.setInt(1, review.getBusiness_number());
			pstmt.setString(2, review.getUser_id());
			pstmt.setString(3, review.getComments());
			pstmt.setInt(4, review.getRating());

			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("리뷰가 성공적으로 추가되었습니다.");
			} else {
				System.out.println("리뷰 추가에 실패했습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}  finally {
			MyConnection.close(null, pstmt, conn);
		}
	}
	/*리뷰작성 전 사업자 번호 획득*/
	public int selectedBusinessnumber(String searchName) throws FindException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int selectedBusinessnumber = 0;
		try {
			conn = MyConnection.getConnection(); 
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return selectedBusinessnumber;
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
				throw new FindException("조회할 목록이 없습니다.");
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
				return selectedBusinessnumber;
			}

			if (selectedRowNum < 1 || selectedRowNum > rowNum) {
				System.out.println("유효하지 않은 선택입니다.");
				return selectedBusinessnumber;
			}

			rs.absolute(selectedRowNum); // 선택한 rownum으로 커서 이동

			return selectedBusinessnumber = rs.getInt(7); // 선택한 음식점의 사업자번호

		} catch (SQLException e) {
			e.printStackTrace();
			return selectedBusinessnumber;
		}  finally {
			
			MyConnection.close(rs, pstmt, conn);
			
		}
	}
	/*작성후기목록조회 repository*/
	public void ReviewAll(String userId) {

		Connection conn = null;
		try {
			conn = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}


		PreparedStatement pstmt=null; 
		ResultSet rs = null;
		try {
			String selectPageSQL="SELECT r.user_id 작성자아이디, rt.restaurants_name 음식점이름, comments 내용, TO_CHAR(comments_date,'YY/MM/DD') 작성일, rating 별점 \r\n"
					+ "FROM reviews r JOIN restaurants rt ON(r.business_number=rt.business_number) \r\n"
					+ "WHERE r.user_id=?"
					+ "ORDER BY 작성일 DESC";
			pstmt=conn.prepareStatement(selectPageSQL);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(!rs.next()) {
				System.out.println("작성한 후기가 없습니다.");
				return;
			}
			System.out.printf("%-10s %-10s %-5s %-10s %5s\n","작성자아이디","음식점이름","내용","작성일","별점");
			do {

				String id=rs.getString(1);
				String name=rs.getString(2);
				String comment=rs.getString(3);
				String date=rs.getString(4);
				int rating=rs.getInt(5);
				System.out.printf("%-12s %-10s%-5s %-10s %5d\n",id,name,comment,date,rating);

			}while (rs.next());	
		} catch (SQLException e) {
			e.printStackTrace();
		}  finally {
			MyConnection.close(rs, pstmt, conn);
		}


	}
}
