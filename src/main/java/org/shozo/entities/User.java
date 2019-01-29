package org.shozo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="User")
@XmlRootElement
@NamedQueries({@NamedQuery(name=User.FIND_ALL,query="select u from User u order by u.lastName"),
	@NamedQuery(name=User.FIND_BY_LOGIN_PASSWORD,query="select u from User u where u.login =:login and u.password = :password"),
	@NamedQuery(name=User.COUNT_ALL,query="select COUNT(u) from User u")})
public class User {

    public static final String FIND_ALL = "User.findAll";
    public static final String COUNT_ALL = "User.countAll";
    public static final String FIND_BY_LOGIN_PASSWORD = "User.findByLoginAndPassword";
    
    @Id
    private String id;
    private String lastName;
    private String firstName;
    @Column(length = 10, nullable = false)
    private String login;
    @Column(length = 256, nullable = false)
    private String password;
    private String company;
    
    public User() {}
    
	public User(String id, String lastName, String firstName, String login, String password, String company) {
		super();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.login = login;
		this.password = password;
		this.company = company;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", lastName=" + lastName + ", firstName=" + firstName + ", login=" + login
				+ ", password=" + password + ", company=" + company + "]";
	}

    
    
    
}
