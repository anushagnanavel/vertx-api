package vertxrepository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import vertxentity.Signup;





public class SignupDao {


	    private static SignupDao instance;
	    protected EntityManager entityManager;
	    public static SignupDao getInstance(){
	        if (instance == null){
	            instance = new SignupDao();
	        }
	        return instance;
	    }
	    private SignupDao() {
	        entityManager = getEntityManager();
	    }
	    private EntityManager getEntityManager() {
	        EntityManagerFactory factory = Persistence.createEntityManagerFactory("crudHibernatePU");
	        if (entityManager == null) {
	            entityManager = factory.createEntityManager();
	        }
	        return entityManager;
	    }



	    public Signup getByName(String name) {
	    	  Object result = entityManager.find(Signup.class, name);
	          if (result != null) {
	              return (Signup) result;
	          } else {
	              return null;
	          }
	      }











	    public Signup getByName(RoutingContext context,String name)
		  {



		     try{Object result = entityManager.createQuery( "SELECT s FROM Signup s WHERE s.name LIKE :user_name")
		    	        .setParameter("user_name", name)
		        	        .getSingleResult();
		      if (result != null) {
		    	  System.out.print("success");
		          return (Signup) result;
		      }
		      }
		      catch(NoResultException nre){
		    	  System.out.print("fail");
		    	  sendError("Login failed", context.response(),400);
		      }
		     return null;


		  }



	    /* forgot password condition*/

	    public void updateSignup( String Name, String Paaword) {
	        String SQL = "UPDATE Signup "
	                + "SET password = ? "
	                + "WHERE email = ?";







	    }






	   @SuppressWarnings("unchecked")
	    public List<Signup> findAll() {
	        return entityManager.createQuery("FROM " + Signup.class.getName()).getResultList();
	    }

	    /* persist only used post call */
	    public void persist(Signup signup) {
	        try {
	            entityManager.getTransaction().begin();
	            entityManager.persist(signup);
	            entityManager.getTransaction().commit();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            entityManager.getTransaction().rollback();
	        }
	    }













private void sendError(String errorMessage, HttpServerResponse response,int code) {
    JsonObject jo = new JsonObject();
    jo.put("errorMessage", errorMessage);

    response
            .setStatusCode(400)
            .setStatusCode(code)
            .putHeader("content-type", "application/json; charset=utf-8")
            .end(Json.encodePrettily(jo));
}

}

