package com.my.mango.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;

import com.my.exception.AddException;
import com.my.exception.LoginException;
import com.my.exception.PkException;
import com.my.mango.dto.Users;
import com.my.mango.sql.MyConnection;

public class UsersRepository {


	/*회원가입 작업 repository*/
	public void insert(Users user) throws PkException, AddException{
		Connection conn = null;
		try {
			conn = MyConnection.getConnection(); //연결객체생성
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}

		String insertSQL="INSERT INTO users(user_id,pwd,name,create_date,user_type,phone_number) \r\n"
				+ "VALUES (?,?,?,SYSDATE,?,?)";
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement(insertSQL);
			pstmt.setString(1,user.getUser_id());
			pstmt.setString(2,user.getPwd());
			pstmt.setString(3,user.getName());
			pstmt.setString(4,user.getUser_type());
			pstmt.setString(5,user.getPhone_number());

			int rowcnt=pstmt.executeUpdate(); //UPDATE등 할때 ,처리건수 int로 반환
			if(rowcnt==0) {
				System.out.println("잘못된 입력으로 회원가입이 불가능합니다.");
			}else {
				System.out.println("회원가입에 성공하였습니다.");
			}

		}catch(SQLIntegrityConstraintViolationException e) {
			throw new PkException("이미 존재하는 id입니다");
		} catch (SQLException e) {
			throw new AddException("잘못된 형식의 입력으로 회원가입을 실패하셨습니다");

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

	/*사용자모드 로그인 작업 repository*/
	public Users userLogin(String id,String pwd) throws LoginException {
		Users user = new Users();
		Connection conn = null;
		try {
			conn = MyConnection.getConnection(); //연결객체생성
		} catch (ClassNotFoundException | SQLException e) {
			return user=null;
		}
		PreparedStatement pstmt=null; 
		ResultSet rs = null;
		try {
			String selectPageSQL="SELECT * FROM users WHERE user_id=? AND pwd=? AND user_type='사용자'\r\n";

			pstmt=conn.prepareStatement(selectPageSQL);
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			rs = pstmt.executeQuery();
			if(rs.next()) {

				user.setUser_id(rs.getString(1)); 
				user.setPwd(rs.getString(2)); 
				user.setName(rs.getString(3)); 
				user.setUser_type(rs.getString(5)); 
				user.setPhone_number(rs.getString(6)); 
				return user;


			}
			else {
				throw new LoginException("id가 존재하지 않거나 비밀번호가 일치하지 않음");
			}

		}catch(SQLSyntaxErrorException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.close(rs, pstmt, conn);
		}

		return user=null;


	}

	/*사업자모드 로그인 작업 repository*/
	public Users bsLogin(String id,String pwd) throws LoginException{
		Users user = new Users();
		Connection conn = null;
		try {
			conn = MyConnection.getConnection(); //연결객체생성
		} catch (ClassNotFoundException | SQLException e) {
			return user=null;
		}

		PreparedStatement pstmt=null; 
		ResultSet rs = null;
		try {
			String selectPageSQL="SELECT * FROM users WHERE user_id=? AND pwd=? AND user_type='사업자'\r\n";

			pstmt=conn.prepareStatement(selectPageSQL);
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			rs = pstmt.executeQuery();
			if(rs.next()) {

				user.setUser_id(rs.getString(1)); 
				user.setPwd(rs.getString(2)); 
				user.setName(rs.getString(3)); 
				user.setUser_type(rs.getString(5)); 
				user.setPhone_number(rs.getString(6)); 
				return user;


			}
			else {
				throw new LoginException("id가 존재하지 않거나 비밀번호가 일치하지 않음");
			}

		}catch(SQLSyntaxErrorException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.close(rs, pstmt, conn);
		}

		return user=null;



	}

}
