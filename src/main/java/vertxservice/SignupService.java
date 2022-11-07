
package vertxservice;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import vertxentity.Signup;
import vertxrepository.SignupDao;




public class SignupService {





	    private SignupDao signupDao = SignupDao.getInstance();


	    public void list(Handler<AsyncResult<List<Signup>>> handler){
	        Future<List<Signup>> future = Future.future();
	        future.setHandler(handler);
	    }
/*
        try {
	            List<Signup> result = signupDao.findAll();
	            future.complete(result);

	            System.out.println(org.hibernate.Version.getVersionString());
	        } catch (Throwable ex) {
	            future.fail(ex);
	        }


*/

/* SIGNUP CONDITION */



/* this condition is used to save function.once the condition is check then work save */

	   public void save(RoutingContext context,Signup newSignup, Handler<AsyncResult<Signup>> handler) {
		     Future<Signup> future = Future.future();
		     future.setHandler(handler);

		       try {

/* mandatory field condition */
	        	if(newSignup.getName().isEmpty() || newSignup.getPassword().isEmpty()|| newSignup.getEmail().isEmpty()) {

	                sendError("please fill the all the details", context.response(),400);
	        	}else {


/* already name exists condition */
	        		
	        		Signup Signup = signupDao.getByName ( context,newSignup.getName());
	        		if(newSignup. getName().equals(Signup. getName()))

	        		 {
		       			 System.out.print("same name");

		       			    	 sendError("name already exists", context.response(),400);

	        		 }



/* password validation condition*/


	        		else {
				    	 String regex = "^(?=.*[0-9])"
			                     + "(?=.*[a-z])(?=.*[A-Z])"
			                     + "(?=.*[@#$%^&+=])"
			                     + "(?=\\S+$).{8,20}$";

				    	 Pattern p = Pattern.compile(regex);
				    	 Matcher m = p.matcher(newSignup.getPassword());
				    	  if(m.matches()){
				    		  System.out.print("success");
//				    	  }
//
//
///* email validation condition */
//

				    		   String regexemail = "^(.+)@(.+)$";

				    		   Pattern P = Pattern.compile(regexemail);
				    		     Matcher M = P.matcher(newSignup.getEmail());
				    		     if(M.matches()) {
				    		    	 System.out.print("success");
				    		    	 signupDao.persist(newSignup);

				    		   //
				    		     }
/*condition not satisfied */
				    	  else{
			    			  sendError("validity of an email address", context.response(),400);
			    			  }} else {

						  sendError("Password must have length 8 characters,one Uppercase,one special character and one digit", context.response(),400);
				    	  }
				    		     }}
	        		future.complete();
	        		}catch (Throwable ex) {
						            future.fail(ex);
						            System.out.print("follow condition");

		         	    }




	   }



       /* this is LOGIN condition */


   	public void login(RoutingContext context,Signup newSignup, Handler<AsyncResult<Signup>> handler) {
		     Future<Signup> future = Future.future();
		     future.setHandler(handler);



     		 

     			 Signup signup = signupDao.getByName ( context,newSignup.getName());
     			if(newSignup. getName().equals(signup. getName()) && newSignup.getPassword().equals(signup.getPassword()));

     			     {
     			 System.out.print("your login success");

     	/* jeddis connection*/

     			  JedisPool jedisPool = new JedisPool ( "localhost", 6379);
     			 	


     			  

           		try (Jedis jedis = jedisPool.getResource()) {
           			String token = UUID.randomUUID().toString().toUpperCase();
           			JsonObject JsonObject = new JsonObject();
           			JsonObject.put("uuid", signup.getId());
           			JsonObject.put("name", signup.getName());
           			JsonObject.put("email",signup.getEmail());
           			JsonObject.put("password", signup.getPassword());
           		String	object  = JsonObject.toString();

           		/* KEYS,VALUES */

           			jedis.set(token, object);


           			jedis.expire(token, 60*60*24);

           			String value=jedis.get(token);
           			



           			System.out.print(token);
           				jedisPool.close();



           			sendSuccesss ( "loginsuccess",token,"expiredkey", context.response(),200);
           			future.complete(); 
           		}catch 
           		(Throwable ex) {

           		
     			    	 sendError("Login failed", context.response(),400);
     			    	 future.fail(ex);
				            System.out.print("follow condition");
           		 
           		
           		
     			     }
           		}
   	}
     			
     			     
     			 
     				
     			
     	
   	
     			public  void gettoken (RoutingContext context, String Authorization,String token,  Handler<AsyncResult<Signup>> handler) {
     				  Future<Signup> future = Future.future();
     			     future.setHandler(handler);

     			  try {
     				  if(token.equals(Authorization))

     				  
     				  
     				  {
     				  
     	     			 JedisPool jedisPool = new JedisPool ( "localhost", 6379);    	
     	     			 
     	     			 try(Jedis jedis = jedisPool.getResource()){
     	     				 System.out.print(token);
     		    	  String value=jedis.get(token); 
     		    	 System.out.print(value);
     		    	 JsonObject jsonObject = new JsonObject(value);

    		       			 System.out.print("same name");

    		       			    	// sendError("name already exists", context.response(),400);

     		    	
     		    	sendSuccessss (token,jsonObject,context.response(),200);
     		       
     		        future.complete(); }
     				  
     				  
     				  
     		       
     		      jedisPool.close();
     		     future.complete();
     				  }
     				  
     				  else {
     					 sendError("unauthorization", context.response(),401);
     					  
     				  }
	        		}catch (Throwable ex) {
     		    	  sendError("unauthorization", context.response(),401);
     		    	 System.out.print("follow condition");
						            future.fail(ex);
	        		
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
    private void sendSuccess(String successMessage, HttpServerResponse response,int code) {
        JsonObject jo = new JsonObject();
        jo.put("successMessage", successMessage);

        response
                .setStatusCode(200)
                .setStatusCode(code)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(jo));
    }

    private void sendSuccesss (String successmessage,String token,String expiremessage,HttpServerResponse response,int code) {
        JsonObject jo = new JsonObject();

        jo.put("successMessage",successmessage);


        jo.put("token",token);

        jo.put("expiremessage",expiremessage);





        response
                .setStatusCode(200)
                .setStatusCode(code)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(jo));
    }
     			 private void sendSuccessss( String token,JsonObject jsonObject, HttpServerResponse response,int code) {
     		        JsonObject jo = new JsonObject();
     		        jo.put( token,jsonObject);

     		        response
     		                .setStatusCode(200)
     		                .setStatusCode(code)
     		                .putHeader("content-type", "application/json; charset=utf-8")
     		                .end(Json.encodePrettily(jo));
     		    }

     			private void sendErrorr(String unauthorization, HttpServerResponse response,int code) {
     		        JsonObject jo = new JsonObject();
     		        jo.put("unauthorization", unauthorization);

     		        response
     		                .setStatusCode(400)
     		                .setStatusCode(code)
     		                .putHeader("content-type", "application/json; charset=utf-8")
     		                .end(Json.encodePrettily(jo));

     			}







     		  }


































































































































































































































































































































































































































































