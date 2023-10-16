package com.my.mango.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.Scanner;

import com.my.mango.dto.Menus;
import com.my.mango.sql.MyConnection;

public class MenusRepository {
	Scanner sc=new Scanner(System.in);
	Menus menu1;
	/*사용자모드 메뉴추가*/
	public void MenupriceAdd(int selectedBusinessnumber) {
		Connection conn = null;
		try {
			conn = MyConnection.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
		String insertSQL = "INSERT INTO menus(menu_id,business_number,menu_name,menu_price) VALUES (menu_seq.NEXTVAL,?,?,?)";
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement(insertSQL);
			menu1=new Menus();
			menu1.setBusiness_number(selectedBusinessnumber);
			pstmt.setInt(1,menu1.getBusiness_number());
			System.out.print("추가할 메뉴명을 입력하세요 : ");
			menu1.setMenu_name(sc.nextLine());
			pstmt.setString(2,menu1.getMenu_name());
			System.out.print("가격을 입력하세요 : ");
			try {
				menu1.setMenu_price(Integer.parseInt(sc.nextLine()));	
			}catch (NumberFormatException e) {
				System.out.println("잘못된 입력으로 메뉴가격등록이 불가능합니다.");
				return;
			}
			pstmt.setInt(3,menu1.getMenu_price());

			int rowcnt=pstmt.executeUpdate(); //UPDATE등 할때 ,처리건수 int로 반환
			if(rowcnt==0) {
				System.out.println("잘못된 입력으로 메뉴가격등록이 불가능합니다.");
			}else {
				System.out.println("메뉴가격 추가에 성공하였습니다.");
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
	/*사용자모드 메뉴가격변경*/
	public void reviseMenuPrice(String userId) {
		boolean menupricecheck=false;


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
				System.out.println("등록된 음식점이 없습니다.");
				return;
			}
			System.out.printf("%-5s %-12s %-5s %-14s %-5s %-30s\n","No","사업자번호","종류","음식점이름","상태","주소");
			do {
				int rowNum = rs.getInt(1);
				int b_n = rs.getInt(2);
				String type = rs.getString(3);
				String name = rs.getString(4);
				String status = rs.getString(5);
				String address = rs.getString(6);
				System.out.printf("( %-2d ) : %-12d %-5s %-14s %-5s %-30s\n",
						rowNum, b_n, type, name, status, address);
			} while (rs.next());

			System.out.print("메뉴가격 추가/변경할 음식점의 번호(No)를 선택하세요 : ");
			int selectedBusinessnumber = 0;
			try {
				int selectedRowNum = Integer.parseInt(sc.nextLine());
				rs.absolute(selectedRowNum); // 선택한 rownum으로 커서 이동
				selectedBusinessnumber = rs.getInt(2); // 선택한 음식점의 사업자번호
			} catch (NumberFormatException e) {
				// 숫자가 아닌 값을 입력한 경우 예외 처리
				System.out.println("유효하지 않은 입력입니다. 숫자를 입력해주세요.");
				return;
			} catch (SQLException e) {
				// ResultSet.next가 호출되지 않은 경우 예외 처리
				System.out.println("존재하지 않는 번호.");
				return;
			}

			// 선택한 음식점의 메뉴와 가격 출력

			while(true) {
				String menuSelectSQL = "SELECT rownum, menus.menu_id, menus.menu_name, menus.menu_price FROM (SELECT * FROM menus WHERE business_number = ? ORDER BY menu_id) menus";
				pstmt = conn.prepareStatement(menuSelectSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				pstmt.setInt(1, selectedBusinessnumber);
				rs = pstmt.executeQuery();
				System.out.println("[메뉴 가격 목록]");
				System.out.println("---------------------------------------------------------------------------------");
				System.out.printf("%-5s %-20s %-10s\n", "No","메뉴", "가격");
				System.out.println("---------------------------------------------------------------------------------");
				if(!rs.next()) {
					System.out.println("등록된 메뉴가격 데이터가 없습니다.");
				}
				else {
					menupricecheck=true;
					do{
						int no=rs.getInt(1);
						String menu = rs.getString(3);
						int price = rs.getInt(4);
						System.out.printf("%-5d %-20s %-10d\n", no, menu, price);
					}while (rs.next());
				}

				while(true) {
					System.out.println("---------------------------------------------------------------------------------");
					System.out.print("1. 메뉴가격변경 / 2. 메뉴가격추가 / 0.뒤로가기 : ");
					String opt1=sc.nextLine();
					if(opt1.equals("1") && menupricecheck==true) {
						System.out.print("변경할 메뉴가격의 번호(No)를 선택하세요 : ");
						int RowNum = Integer.parseInt(sc.nextLine());
						rs.absolute(RowNum); // 선택한 rownum으로 커서 이동
						int selectedMenuId = rs.getInt(2); // 선택한 메뉴ID
						System.out.print("변경할 메뉴명을 입력하세요 : ");
						String menuUpdate=sc.nextLine();
						System.out.print("변경할 가격을 입력하세요 : ");
						int priceUpdate=Integer.parseInt(sc.nextLine());
						String updateSQL="UPDATE menus SET menu_name=?, menu_price=? WHERE menu_id=?";
						try {

							pstmt=conn.prepareStatement(updateSQL);

							pstmt.setString(1,menuUpdate);		
							pstmt.setInt(2,priceUpdate);
							pstmt.setInt(3, selectedMenuId);

							int rowcnt=pstmt.executeUpdate();
							if(rowcnt==0) {
								System.out.println("잘못된 입력으로 변경할 수 없습니다.");
							}
							else {
								System.out.println("메뉴 가격변경에 성공하였습니다.");
							}
							break;


						}catch (SQLException e) {
							e.printStackTrace();
						}

					}else if(opt1.equals("2")) {
						MenupriceAdd(selectedBusinessnumber);
						break;
					}else if(opt1.equals("0")) {
						return;
					}else if(opt1.equals("1") && menupricecheck==false){
						System.out.println("변경할 메뉴,가격 데이터가 없습니다. 메뉴,가격을 추가하세요.");
					}else {
						return;
					}
				}




			}

		}catch(NumberFormatException  e) {

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnection.close(rs, pstmt, conn);
		} 

	}

}
