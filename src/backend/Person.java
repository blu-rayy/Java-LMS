package backend;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Person {
    private String id;                 
    private String name;               
    private String email;             
    private String phoneNumber;       
    private String registrationDate;   
    protected String userType;          
        private String username;          
        private String password;          
    
        public Person(String id, String name, String username, String email, String phoneNumber, String registrationDate, String password, String userType) {
            this.id = id;
            this.name = name;
            this.username = username;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.registrationDate = registrationDate != null ? registrationDate : new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            this.password = password;
            this.userType = userType;
        }
    
        public Person(){ 
        }
    
        // Getters and setters
        public String getID() {
            return id;
        }
    
        public void setID(String id) {
            this.id = id;
        }
    
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public String getUsername() {
            return username;
        }
    
        public void setUsername(String username) {
            this.username = username;
        }
    
        public String getEmail() {
            return email;
        }
    
        public void setEmail(String email) {
            this.email = email;
        }
    
        public String getPhoneNumber() {
            return phoneNumber;
        }
    
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    
        public String getRegistrationDate() {
            return registrationDate;
        }
    
        public void setRegistrationDate(String registrationDate) {
            this.registrationDate = registrationDate;
        }
    
        public String getUserType() {
            return userType;
        }
    
        public void setUserType(String userType) {
            this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean borrowBook(Book book) {
        return false;
    }

    public void returnBook(Book book) {
    }
}
