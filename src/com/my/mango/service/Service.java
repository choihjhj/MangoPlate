package com.my.mango.service;

import java.util.Scanner;

import com.my.exception.AddException;
import com.my.exception.ConnectionException;
import com.my.exception.FindException;
import com.my.exception.LoginException;
import com.my.exception.PkException;
import com.my.mango.dao.MenusRepository;
import com.my.mango.dao.RestaurantsRepository;
import com.my.mango.dao.ReviewsRepository;
import com.my.mango.dao.UsersRepository;
import com.my.mango.dto.Restaurants;
import com.my.mango.dto.Reviews;
import com.my.mango.dto.Users;

public class Service {

	Scanner sc = new Scanner(System.in);
	Users loginedUser=new Users();

	UsersRepository user_repository=new UsersRepository();
	RestaurantsRepository restaurant_repository=new RestaurantsRepository();
	MenusRepository menu_repository=new MenusRepository();
	ReviewsRepository review_repository=new ReviewsRepository();

	/*회원가입 service */
	public void signupService() {
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("                           회원가입 작업 화면                   ");
		System.out.println("---------------------------------------------------------------------------------");

		Users user=new Users();
		System.out.print("아이디 : ");
		user.setUser_id(sc.nextLine());
		System.out.print("비밀번호 : ");
		user.setPwd(sc.nextLine());
		System.out.print("이름 : ");
		user.setName(sc.nextLine());
		System.out.print("타입(사용자 or 사업자) : ");
		user.setUser_type(sc.nextLine().trim());
		System.out.print("핸드폰번호11자리('-'생략) : ");
		user.setPhone_number(sc.nextLine());

		try {
			user_repository.insert(user);
		} catch (PkException e) {
			System.out.println(e.getMessage());
		} catch (AddException e) {
			System.out.println(e.getMessage());
		}
	}

	/*사용자로그인 service*/
	public void userloginService() throws LoginException, ConnectionException {
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("                               사용자 로그인 작업 화면                   ");
		System.out.println("---------------------------------------------------------------------------------");
		System.out.print("아이디 : ");
		String id=sc.nextLine();
		System.out.print("비밀번호 : ");
		String pwd=sc.nextLine();


		
			loginedUser=user_repository.userLogin(id,pwd);
			if(loginedUser==null) {
				throw new ConnectionException("연결 실패!");
			}
	



	}

	/*사업자모드로그인 service*/
	public void businessloginService() throws LoginException, ConnectionException {
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("                               사업자 로그인 작업 화면                   ");
		System.out.println("---------------------------------------------------------------------------------");
		System.out.print("아이디 : ");
		String id=sc.nextLine();
		System.out.print("비밀번호 : ");
		String pwd=sc.nextLine();

		loginedUser=user_repository.bsLogin(id,pwd);
		if(loginedUser==null) {
			throw new ConnectionException("연결 실패!");
		}
	}
	
	/*사용자모드 음식점 조회 service*/
	public void restaurnatViewService() {
		sc = new Scanner(System.in);
		String userTypeInput = "";

		while(true) {   
			System.out.println("---------------------------------------------------------------------------------");
			System.out.println("                               맛집추천 및 검색 작업 화면                 ");
			System.out.println("---------------------------------------------------------------------------------");
			System.out.println("1. 한식");
			System.out.println("2. 중식");
			System.out.println("3. 일식");
			System.out.println("4. 양식");
			System.out.println("5. 분식");
			System.out.println("6. 이름검색");
			System.out.println("7. 전체조회");
			System.out.println("0. 뒤로가기");
			System.out.print("원하시는 카테고리를 선택하세요 : ");    	  
			userTypeInput = sc.nextLine();

			if(userTypeInput.equals("1") || userTypeInput.equals("2")||userTypeInput.equals("3") || userTypeInput.equals("4")|| userTypeInput.equals("5")|| userTypeInput.equals("7")) {
				String userOrderInput = "";
				System.out.print("1.후기순 추천 / 2. 별점순 추천 / 3.이름순 정렬 : ");
				userOrderInput= sc.nextLine();
				if(userOrderInput.equals("1")||userOrderInput.equals("2")||userOrderInput.equals("3")) {
					try {
						restaurant_repository.restaurantsView(userTypeInput, userOrderInput);
					} catch (FindException e) {
						System.out.println(e.getMessage());
					}
				} else {
					System.out.println("유효한 선택이 아닙니다");
					return;
				}
				
			}else if(userTypeInput.equals("6")) {
				System.out.print("검색할 음식점 이름을 입력하세요: ");
				String searchName = sc.nextLine();
				searchName = searchName.replaceAll("\\s", "");
				try {
					restaurant_repository.restaurantsNameView(searchName);
				} catch (FindException e) {
					System.out.println(e.getMessage());
				}	    		  
			}else if(userTypeInput.equals("0")) {
				return;
			}else {
				System.out.println("유효한 선택이 아닙니다.");   
			}
		}

	}
	
	/*사용자모드 음식점 후기 작성 service*/
	public void writeReviewService() {
		ReviewsRepository reviewsRepository = new ReviewsRepository();
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("                               음식점 후기 작성 작업 화면                  ");
		System.out.println("---------------------------------------------------------------------------------");
		System.out.print("후기를 작성할 음식점 이름을 입력하세요 : ");
		String searchName = sc.nextLine();
		searchName = searchName.replaceAll("\\s", "");
		int businessNum=0;
		try {
			businessNum = reviewsRepository.selectedBusinessnumber(searchName);
		} catch (FindException e) {
			System.out.println(e.getMessage());
		}
		if (businessNum != 0) {
			System.out.println("---------------------------------------------------------------------------------");
			System.out.println("리뷰 내용을 작성하세요 : ");
			String comments = sc.nextLine();

			int rating=0;
			do {
				System.out.print("별점 (1~5)을 입력하세요 : ");
				try{
					rating = Integer.parseInt(sc.nextLine());
				} catch(NumberFormatException e){
					System.out.println("숫자만 입력 가능합니다.");
				}
				if (rating < 1 || rating > 10) {
					System.out.println("1~5점만 입력 가능합니다.");
				}
			} while (rating < 1 || rating > 10);

			Reviews review = new Reviews(businessNum, loginedUser.getUser_id(), comments, rating);
			reviewsRepository.writeReview(review);
		}
	}
	/*사업자모드음식점등록 service 함수*/
	public void RestaurantInsertService() {
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("                           음식점등록 작업 화면                   ");
		System.out.println("---------------------------------------------------------------------------------");

		Restaurants res=new Restaurants();
		System.out.print("사업자번호 : ");
		try {
			res.setBusiness_number(Integer.parseInt(sc.nextLine()));
		} catch (NumberFormatException e) {
			System.out.println("숫자만 입력하세요.");
			return;
			
		}
		System.out.print("음식점종류(한식 or 중식 or 일식 or 양식 or 분식) : ");
		res.setRestaurants_type(sc.nextLine());
		System.out.print("음식점이름 : ");
		res.setRestaurants_name(sc.nextLine());
		res.setStatus("영업");
		System.out.print("주소 : ");
		res.setAddress(sc.nextLine());
		res.setUser_id(loginedUser.getUser_id());

		restaurant_repository.restaurantAdd(res);

	}

	/*작성후기목록조회 service 함수*/
	public void searchMyreviewService() {
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("                            작성후기목록조회 작업 화면                   ");
		System.out.println("---------------------------------------------------------------------------------");

		review_repository.ReviewAll(loginedUser.getUser_id());	
	}

	/*음식점 목록 조회 service 함수*/
	public void restaurantAllService() {
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("                            음식점목록조회 작업 화면                   ");
		System.out.println("---------------------------------------------------------------------------------");
		try {
			restaurant_repository.restaurantAll(loginedUser.getUser_id());
		} catch (FindException e) {
			System.out.println(e.getMessage());
		}

	}

	/*음식점정보변경 service 함수*/
	public void restaurantUpdateService() {
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("                            음식점정보변경 작업 화면                   ");
		System.out.println("---------------------------------------------------------------------------------");

		try {
			restaurant_repository.Update(loginedUser.getUser_id());
		} catch (FindException e) {
			System.out.println(e.getMessage());
		}
	}
	/*메뉴가격추가및변경 service 함수*/
	public void reviseMenuPriceService() {
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("                            메뉴가격추가및변경 작업 화면                   ");
		System.out.println("---------------------------------------------------------------------------------");

		menu_repository.reviseMenuPrice(loginedUser.getUser_id());

	}
}
