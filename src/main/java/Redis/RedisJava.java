package Redis;


import java.util.UUID;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
public class RedisJava  {
	 public static void main(String[] args) {


JedisPool jedisPool = new JedisPool ( "localhost", 6379);
// Get the pool and use the database



try (Jedis jedis = jedisPool.getResource()) {
 	String token = UUID.randomUUID().toString().toUpperCase();

jedis.set("value", token);
String value=jedis.get("value");
System.out.print(value);

}

// close the connection pool
jedisPool.close();}}






