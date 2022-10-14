package JavaClassStudent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
	public static Scanner sc = new Scanner(System.in);
	public static final int INPUT = 1, UPDATE = 2, DELETE = 3, SEARCH = 4, OUTPUT = 5;
	public static final int SORT = 6, EXIT = 7;

	public static void main(String[] args) {
		DBConnection dbConn = new DBConnection();
		dbConn.connect();

		boolean stopFlag = false;
		while (!stopFlag) {

			int num = displayMenu();
			switch (num) {
			case INPUT:
				privateAcademyInputData();
				break;
			case UPDATE:
				privateAcademyUpDate();
				break;
			case DELETE:
				privateAcademyDelete();
				break;
			case SEARCH:
				privateAcademySearch();
				break;
			case OUTPUT:
				privateAcademyOutput();
				break;
			case SORT:
				privateAcademySort();
				break;
			case EXIT:
				System.out.println("프로그램이 종료 됩니다. Have a nice day~~");
				stopFlag = true;
				break;
			default:
				System.out.println("숫자 1 ~ 7번까지 다시 입력바랍니다.");
				break;
			}
		}
		System.out.println("프로그램 종료");
	}
	private static void privateAcademyInputData() {
		try {
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			String pattern = null;
			System.out.print("[Academy]_등록번호 입력: ");
			String no = sc.nextLine();
			boolean value = checkInputPattern(no, 2);
			if (!value)
				return;

			System.out.print("Name:  ");
			String name = sc.nextLine();
			value = checkInputPattern(name, 3);
			if (!value)
				return;
			
			String gender = null;
			System.out.print("1.Male, 2.Female: ");
			int genders = sc.nextInt();
			value = checkInputPattern(String.valueOf(genders), 4);
			if (!value)
				return;
			switch(genders) {
			case 1:
				gender = "남자";
				break;
			case 2:
				gender = "여자";
				break;
			}
			
			System.out.print("출생년도4자리 입력: ");
			int year = sc.nextInt();
			value = checkInputPattern(String.valueOf(year), 2);
			if (!value)
				return;
			
			sc.nextLine();
			System.out.print("생년월일4자리 입력: ");
			String monthday = sc.nextLine();
			value = checkInputPattern(monthday, 2);
			if (!value)
				return;

			String dateOfBirth = year + monthday;
			
		
			int age = dbConn.ageFunction(year);

			System.out.print("핸드폰 번호를 입력: ");
			String phone = sc.nextLine();
			value = checkInputPattern(phone, 5);
			if (!value)
				return;
			Student student = new Student(no, name, gender, dateOfBirth, age, phone);

			
			int insertRe = dbConn.insert(student);
			if (insertRe == -1) {
				System.out.println("삽입 [실패]입니다!");
			} else {
				System.out.println("삽입 [성공]입니다!" + "리턴값: 1");
			}
			dbConn.close();
		} catch (InputMismatchException e) {
			System.out.println("입력타입이 맞지 않습니다. 재입력 바랍니다." + e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("데이타베이스 입력에러" + e.getMessage());
		}
	}

	private static void privateAcademyUpDate() {
		List<Student> list = new ArrayList<Student>();
		System.out.print("수정할 등로번호 입력: ");
		String no = sc.nextLine();
		boolean value = checkInputPattern(no, 2);
		if (!value)
			return;
		DBConnection dbConn = new DBConnection();
		dbConn.connect();
		list = dbConn.selectSerach(no, 1);
		if (list.size() <= 0) {
			System.out.println("검색 된 정보가 없습니다." + list.size());
			return;
		}
		for (Student student : list) {
			System.out.println(student);
		}

		Student listStudent = list.get(0);
		String pattern = null;
		boolean stopFlag = false;
		int num = 0;
		listStudent.setNo(no);

		num = UpdateMenu();
		switch (num) {
			case 1:
				System.out.print("변경 할 Name:  ");
				String name = sc.nextLine();
				value = checkInputPattern(name, 3);
				if (!value)
					return;
				listStudent.setName(name);
				break;
			case 2:
				String gender = null;
				System.out.print("1.Male, 2.Female: ");
				int genders = sc.nextInt();
				value = checkInputPattern(String.valueOf(genders), 4);
				if (!value)
					return;
				switch(genders) {
				case 1:
					gender = "남자";
					break;
				case 2:
					gender = "여자";
					break;
				}
				listStudent.setGender(gender);
				break;
			case 3:
				System.out.print("변경 할 출생년도4자리 입력: ");
				int year = sc.nextInt();
				value = checkInputPattern(String.valueOf(year), 2);
				if (!value)
					return;
				sc.nextLine();
				System.out.print("변경 할 생년월일4자리 입력: ");
				String monthday = sc.nextLine();
				value = checkInputPattern(monthday, 2);
				if (!value)
					return;
				
				String dateOfBirth = year + monthday;
				
				int age = dbConn.ageFunction(year);
				
				listStudent.setDateOfBirth(dateOfBirth);
				listStudent.setAge(age);
				break;
			case 4:
				System.out.print("변경 할 핸드폰 번호를 입력: ");
				String phone = sc.nextLine();
				value = checkInputPattern(phone, 5);
				if (!value)
					return;
				listStudent.setPhone(phone);
				break;

			default:
				System.out.println("숫자 1 ~ 6번까지 다시 입력바랍니다.");
				break;
			}
		listStudent.getAge();		
		int returnUpdate = dbConn.update(listStudent, num);
		if (returnUpdate == -1) {
			System.out.println("수정할정보가 없습니다." + returnUpdate);
			return;
		}
		System.out.println("학생정보가 수정되었습니다." + returnUpdate);
		dbConn.close();
	}

	private static void privateAcademyDelete() {
		try {
			System.out.println("삭제 할 등록번호 입력: ");
			String no = sc.nextLine();
			boolean value = checkInputPattern(no, 2);
			if (!value) return;
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			int deleteRe = dbConn.delete(no);
			if (deleteRe == -1) {
				System.out.println("삭제 [실패]입니다!" + deleteRe);
			}
			if (deleteRe == 0) {
				System.out.println("삭제 할 번호가 없습니다" + deleteRe);
			} else {
				System.out.println("삭제 [성공]입니다!" + "리턴값: " + deleteRe);
			}
	
			dbConn.close();
		}catch (InputMismatchException e) {
			System.out.println("입력타입이 맞지 않습니다. 재입력 바랍니다." + e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("데이타베이스 삭제에러" + e.getMessage());
		} 
	}
	private static void privateAcademySearch() {
		List<Student> list = new ArrayList<Student>();
		try {
			System.out.print("검색할 이름 입력: ");
			String name = sc.nextLine();
			boolean value = checkInputPattern(name, 3);
			if (!value)
				return;
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			list = dbConn.selectSerach(name, 2);
			if (list.size() <= 0) {
				System.out.println("검색한 학생정보가 없습니다." + list.size());
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbConn.close();
		} catch (InputMismatchException e) {
			System.out.println("입력타입이 맞지 않습니다. 재입력 바랍니다." + e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("데이타베이스 삭제에러" + e.getMessage());
		}
	}

	private static void privateAcademyOutput() {
		List<Student> list = new ArrayList<Student>();
		try {
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			list = dbConn.select();
			if (list.size() <= 0) {
				System.out.println("List에 정보가 없습니다. ListSize: " + list.size());
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbConn.close();
		} catch (Exception e) {
			System.out.println("Output List Error" + e.getMessage());
		}
		return;

	}

	private static void privateAcademySort() {
		List<Student> list = new ArrayList<Student>();
		try {
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			System.out.print("정렬기준 선택 [1번: 학생번호] or [2번: 이 름] 선택: ");
			int type = sc.nextInt();
			boolean value = checkInputPattern(String.valueOf(type), 4);
			if (!value)
				return;
			list = dbConn.selectOrderBy(type);
			if (list.size() <= 0) {
				System.out.println("보여줄 리스트가 없습니다." + list.size());
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbConn.close();
		} catch (Exception e) {
			System.out.println("데이타베이스 정렬 에러" + e.getMessage());
		}
		return;
	}

	public static int displayMenu() {
		int num = -1;
		try {
			System.out.println("1.입력, 2.수정, 3.삭제, 4.검색, 5.출력, 6.정렬, 7.종료 \n 입력바랍니다 >>>>>>>");
			num = sc.nextInt();
			boolean value = checkInputPattern(String.valueOf(num), 1);
		} catch (InputMismatchException e) {
			System.out.println("InputMismatch [숫자]로 다시 입력 바랍니다");
			num = -1;
		} finally {
			sc.nextLine();
		}
		return num;
	}

	private static int UpdateMenu() {
		int num = -1;
		try {
			System.out.println("1.이름, 2.성별, 3.생년월일, 4.전화번호 \n 입력바랍니다 >>>>>>>");
			num = sc.nextInt();
			boolean value = checkInputPattern(String.valueOf(num), 1);
		} catch (InputMismatchException e) {
			System.out.println("InputMismatch [숫자]로 다시 입력 바랍니다");
			num = -1;
		} finally {
			sc.nextLine();
		}
		return num;
	}
	private static boolean checkInputPattern(String data, int patternType) {
		final int DISPLAY = 1, REGISTRATION = 2, NAMES = 3, SORTS = 4, PHONES = 5;
		String pattern = null;
		boolean regex = false;

		String message = null;
		switch (patternType) {
		case DISPLAY:
			pattern = "^[1-7]*$";
			message = "InputMismatch [숫자]로 다시 입력 바랍니다";
			break;
		case REGISTRATION:
			pattern = "^[0-9]{4}$";
			message = "잘못 입력하셨습니다 다시 입력 바랍니다.";
			break;
		case NAMES:
			pattern = "^[가-힣]{2,4}$";
			message = "이름 재입력 요망";
			break;
		case SORTS:
			pattern = "^[1-2]$";
			message = "정렬타입 재입력 요망";
			break;
		case PHONES :
			pattern = "^010[.-]?[0-9]{4}[.-]?[0-9]{4}$";
			message = "핸드폰번호 재입력 요망";
			break;
		}
		regex = Pattern.matches(pattern, data);
		if (!regex) {
			System.out.println(message);
			return false;
		}
		return regex;
	}
}
