package JavaClassStudent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBConnection {
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet rs = null;

	public void connect() {
		Properties properties = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("C:\\JAVA_TEST\\JavaClass.Student\\src\\JavaClassStudent\\db.properties");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("FileInputStream Error" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Properties Error" + e.getMessage());
		}
		try {
			Class.forName(properties.getProperty("driver"));
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("userid"), properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("Class.forName load Error" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Connection Error" + e.getMessage());
		}
	}

	public int insert(Student student) {
		PreparedStatement ps = null;
		int insertRe = -1;
		String insertQuery = "insert into academytbl(no,name,gender,dateOfBirth,age,phone)" + " values(?,?,?,?,?,?)";
		try {
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, student.getNo());
			ps.setString(2, student.getName());
			ps.setString(3, student.getGender());
			ps.setString(4, student.getDateOfBirth());
			ps.setInt(5, student.getAge());
			ps.setString(6, student.getPhone());
			
			insertRe = ps.executeUpdate(); 
		} catch (SQLException e) {
			System.out.println("InsertReturnValue Error" + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception Error" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PreparedStatement Close Error" + e.getMessage());
			}
		}
		return insertRe;
	}
	public int update(Student student, int type) {
	      PreparedStatement ps = null;
	      int updateRe = -1;
	      String inserQuery = null;
	      try {
	         switch (type) {
	         case 1 :
	            inserQuery = "UPDATE Academytbl SET name = ? where no = ? ";
	            ps = connection.prepareStatement(inserQuery);
	            ps.setString(1, student.getName());
	            ps.setString(2, student.getNo());
	            break;
	         case 2 :
	            inserQuery = "UPDATE Academytbl SET gender = ? where no = ? ";
	            ps = connection.prepareStatement(inserQuery);
	            ps.setString(1, student.getGender());
	            ps.setString(2, student.getNo());
	            break;
	         case 3 :
	        	 inserQuery = "UPDATE Academytbl SET dateOfBirth = ?, age = ? where no = ? ";
	            ps = connection.prepareStatement(inserQuery);
	            ps.setString(1, student.getDateOfBirth());
	            ps.setInt(2, student.getAge());
	            ps.setString(3, student.getNo());
	            break;
	         case 4 :
	            inserQuery = "UPDATE Academytbl SET phone = ? where no = ? ";
	            ps = connection.prepareStatement(inserQuery);
	            ps.setString(1, student.getPhone());
	            ps.setString(2, student.getNo());
	            break;
	         }
	         
	         updateRe = ps.executeUpdate();
	         
	      } catch (SQLException e) {
	         System.out.println("updateReturnValue Error" + e.getMessage());
	   
	      } catch (Exception e) {
	         System.out.println("Exception Error" + e.getMessage());
	      } finally {
	         try {
	            if (ps != null) {
	               ps.close();
	            }
	         } catch (SQLException e) {
	            System.out.println("PreparedStatement Close Error" 
	   + e.getMessage());
	         }
	      }
	      return updateRe;
	   }

	public int delete(String no) {
		PreparedStatement ps = null;
		int deleteRe = -1;
		String deleteQuery = "delete from Academytbl where no = ?";
		try {
			ps = connection.prepareStatement(deleteQuery); 
			ps.setString(1, no);

			deleteRe = ps.executeUpdate(); 
		} catch (Exception e) {
			System.out.println("Delete Error" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PreparedStatement Close Error" + e.getMessage());
			}
		}
		return deleteRe;
	}
	public List<Student> selectSerach(String data, int type) {
		List<Student> list = new ArrayList<Student>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String searchQuery = "select * from Academytbl where";
		try {
			switch(type) {
			case 1: 
				searchQuery += " no like ? ";
				break;
			case 2:
				searchQuery += " name like ? ";
				break;
			default : System.out.println("잘못된 입력 타입");
				return list;	
			}
			ps = connection.prepareStatement(searchQuery);
			ps.setString(1, "%" + data + "%");
			rs = ps.executeQuery();
			if(!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			while(rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				String gender = rs.getString("gender");
				String dateOfBirth = rs.getString("dateOfBirth");
				int age = rs.getInt("age");
				String phone = rs.getString("phone");
				list.add(new Student(no, name, gender, dateOfBirth, age, phone));
			}
			
		}catch(Exception e) {
			System.out.println("Search Error" + e.getMessage());
		}finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PreparedStatement Close Error" + e.getMessage());
			}
		}
		return list;
	}
	public List<Student> selectOrderBy(int type) {
		List<Student> list = new ArrayList<Student>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String orderByQuery = "CALL ORDER_BY_DATA('"+type+"')";
		try {
			ps = connection.prepareStatement(orderByQuery);			        
			rs = ps.executeQuery(); 
			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			while (rs.next()) {
				
				String no = rs.getString("no");
				String name = rs.getString("name");
				String gender = rs.getString("gender");
				String dateOfBirth = rs.getString("dateOfBirth");
				int age = rs.getInt("age");
				String phone = rs.getString("phone");
				list.add(new Student(no, name, gender, dateOfBirth, age, phone));
			}
			
		} catch (Exception e) {
			System.out.println("Select Error" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PreparedStatement Close Error" + e.getMessage());
			}
		}
		return list;
	}
	public List<Student> select() {
		List<Student> list = new ArrayList<Student>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String selectQuery = "select * from Academytbl";
		
		try {
			ps = connection.prepareStatement(selectQuery);
			rs = ps.executeQuery(selectQuery);
			if(!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			while(rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				String gender = rs.getString("gender");
				String dateOfBirth = rs.getString("dateOfBirth");
				int age = rs.getInt("age");
				String phone = rs.getString("phone");
				list.add(new Student(no,name,gender,dateOfBirth,age,phone));
			}
		} catch (Exception e) {
			System.out.println("DBConnection Select Error" + e.getMessage());
		}finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PreparedStatement Close Error" + e.getMessage());
			}
		}
		return list;
	}
	
	public int ageFunction(int year) {
    	PreparedStatement ps = null;
    	ResultSet rs = null;
   		int age = 0;
    	String ageQuery = "SELECT GetAgeFunc('"+year+"') as GetAgeFunc";
    	try {
       	ps = connection.prepareStatement(ageQuery);
        	rs = ps.executeQuery(ageQuery);
        	if(!(rs != null || rs.isBeforeFirst())) {
          		return age;
       	}
       	if(rs.next()) {
         		age = rs.getInt("getAgeFunc");
        	}
     	} catch (Exception e) {
       		System.out.println("DBConnection ageFunction Error" + e.getMessage());
    	}finally {
        	try {
          		if (ps != null) {
             		ps.close();
           		}
        	} catch (SQLException e) {
           		System.out.println("PreparedStatement Close Error" + e.getMessage());
       	}
    	}
    	return age;
 	 }
	public int deleteTrigger(String data) {
		PreparedStatement ps = null;
		int triggerRe = -1;
		try {
			String triggerQuery = "DELETE FROM DeleteAcademytbl WHERE no = ?";
			ps = connection.prepareStatement(triggerQuery);
			ps.setString(1, "%" + data + "%");
			triggerRe = ps.executeUpdate();
	
		} catch (SQLException e) {
			System.out.println("DBConnection deleteTrigger Error" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PreparedStatement Insert Selct Error : " + e.getMessage());
			}
		}
		return triggerRe;
	}

	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println("Statement or ResultSet Close Error" + e.getMessage());
		}
	}
}


