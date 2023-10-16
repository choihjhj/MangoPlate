package test;

import java.util.Scanner;

import com.my.exception.ConnectionException;
import com.my.exception.LoginException;
import com.my.mango.service.Service;

public class MangoUser {
	public Scanner sc = new Scanner(System.in);
	public Service serviceCall = new Service();
	public void mangoplate() {
		String opt1;
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("                              Welcome MANGOPLATE  hj버전                 ");

		while (true) {
			System.out.println("---------------------------------------------------------------------------------");
			System.out.print("1. 회원가입 / 2. 로그인 / 3. 종료: ");
			opt1 = sc.nextLine();

			if (opt1.equals("1")) {
				// 1. 회원가입
				serviceCall.signupService();
			} else if (opt1.equals("2")) {
				// 2. 로그인
				String opt2;
				while (true) {
					System.out.println("---------------------------------------------------------------------------------");
					System.out.print("1. 사용자모드 / 2. 사업자모드 / 0. 뒤로가기 : ");
					opt2 = sc.nextLine();
					if (opt2.equals("1")) {
						// 1. 사용자모드
						try {
							serviceCall.userloginService();
						} catch (LoginException |ConnectionException e) {
							System.out.println(e.getMessage());
							break;
						}
						System.out.println("로그인 성공!");
						System.out.println("---------------------------------------------------------------------------------");
						System.out.println("                               사용자모드 작업 화면                   ");
						while (true) {
							System.out.println("---------------------------------------------------------------------------------");
							System.out.print("1. 맛집추천및검색 / 2. 음식점후기작성 / 3.작성후기목록조회 / 4.뒤로가기 : ");
							String opt3 = sc.nextLine();

							if (opt3.equals("1")) {
								serviceCall.restaurnatViewService(); /*맛집추천및검색 service 함수*/
							} else if (opt3.equals("2")) {
								serviceCall.writeReviewService(); /*음식점후기작성 service 함수*/
							} else if (opt3.equals("3")) {
								serviceCall.searchMyreviewService();  /*작성후기목록조회 service 함수*/
							} else if (opt3.equals("4")) {
								break;
							} else {
								System.out.println("잘못입력하셨습니다.");
							}
						}
					} else if (opt2.equals("2")) {
						// 2. 사업자모드
						try {
							serviceCall.businessloginService();
						} catch (LoginException |ConnectionException e) {
							System.out.println(e.getMessage());
							break;
						}
						System.out.println("로그인 성공!");
						System.out.println("---------------------------------------------------------------------------------");
						System.out.println("                               사업자모드 작업 화면                   ");
						while (true) {
							System.out.println("---------------------------------------------------------------------------------");
							System.out.print("1.음식점목록조회 / 2.음식점등록 / 3.음식점정보변경 / 4.메뉴가격추가및변경 / 5.뒤로가기 : ");
							String opt4 = sc.nextLine();
							if (opt4.equals("1")) {
								serviceCall.restaurantAllService(); /*음식점목록조회 service 함수*/
							}else if(opt4.equals("2")){
								serviceCall.RestaurantInsertService();  /*음식점등록 service 함수*/
							} else if (opt4.equals("3")) {
								serviceCall.restaurantUpdateService();  /*음식점정보변경 service 함수*/
							} else if (opt4.equals("4")) {
								serviceCall.reviseMenuPriceService();  /*메뉴가격추가및변경 service 함수*/
							} else if (opt4.equals("5")) {
								break;
							} else {
								System.out.println("잘못입력하셨습니다.");
							}
						}
					} else if(opt2.equals("0")) {
						// 0. 뒤로가기
						break;
					}
					else {
						System.out.println("잘못입력하셨습니다.");
					}
				}
			} else if (opt1.equals("3")) {
				// 3. 종료
				System.out.println("MANGOPLATE를 종료합니다.");				
				return;
			} else {
				System.out.println("유효한 선택이 아닙니다.");
			}
		}

	}
	public static void main(String[] args) {
		MangoUser user1 = new MangoUser();
		user1.mangoplate();

	}

}











