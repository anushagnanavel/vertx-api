package vertxentity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

public class Singnupuuid {



	 @Id
	    @GeneratedValue(generator = "uuid")
	    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	    @Column(name = "uuid", columnDefinition = "VARCHAR(255)")
	    private UUID id;

}
