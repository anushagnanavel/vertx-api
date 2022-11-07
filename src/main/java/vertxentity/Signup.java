package vertxentity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import io.vertx.core.json.JsonObject;

	/**
	 * this class creates a signupentity
	 */
	@Entity
	@Table(name = "Signup")


	public class Signup  implements Serializable{
		@Id
		   @Column(name = "uuid")
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy = "uuid")


		 private String uuid;


	    @Column(name = "user_name", nullable=false)
	    private String name;

	    @Column(name = "email", nullable=false)
	    private String email;

	    @Column(name = "password", nullable=false)
	    private String password;




	    public String getId() {
	        return uuid;
	    }
	    public void setId(String id ) {
	        this.uuid = id ;

	    }


	    public String getName() {
	        return name;
	    }
	    public void setName(String name ) {
	        this.name = name ;

	    }

	    public String getEmail() {
	        return email;
	    }
	    public void setEmail(String email ) {
	        this.email = email ;

	    }




	    public String getPassword() {
	    	return password;
	    }
	    public void setPassword(String password) {
	        this.password = password;
	    }


	    public String toJsonString(){
            return String.valueOf(JsonObject.mapFrom(this));
        }
}

