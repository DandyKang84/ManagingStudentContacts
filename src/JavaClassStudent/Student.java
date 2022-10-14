package JavaClassStudent;
import java.io.Serializable;
public class Student implements Comparable<Student>, Serializable{
	
	private String no;
	private String name;
	private String gender;
	private String dateOfBirth;
	private int age;
	private String phone;
	
	public Student(String no, String name, String gender, String dateOfBirth, 
int age, String phone) {
		super();
		this.no = no;
		this.name = name;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.age = age;
		this.phone = phone;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	public String toString() {
		return "등록번호: " + no + "\t이름: " + name + "\t성별: "+ gender + 
"\t생년월일" + dateOfBirth + "\t나이: " + age + "\t전화번호" + phone;
	}
	@Override
	public int hashCode() {
		return this.name.hashCode();
	} 
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Student)) return false;
		return this.name.equals(((Student)obj).name);
	} 
	
	@Override
	public int compareTo(Student mire) {
		return this.name.compareToIgnoreCase(mire.name);
	}
}
