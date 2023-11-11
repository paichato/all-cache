package net.javaguides.postgresql.tutorial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RetrieveRecordsExample {

    private final static String url = "jdbc:postgresql://localhost/mydb";
    private final static String user = "postgres";
    private final static String password = "root";

    private static final String QUERY = "select id,name,email,country,password from Users where id =?";
    private static final String SELECT_ALL_QUERY = "select * from users";

       @RequestMapping(value = "/user/{userId}", produces = { "text/html; charset=utf-8" })
    @ResponseBody
    public String getuserData(HttpServletResponse response,
                             @PathVariable("userId") String userId) {
        long startTime = System.nanoTime();
        String userData = jedis.get(userId);
        boolean isCached = true;
        if (userData == null) {
            userData = getDbUserData(userId);
            isCached = false;
        }

        response.addHeader("X-Response-Time", getResponseTime(System.nanoTime() - startTime, 1_000_000) );
        response.addHeader("Access-Control-Expose-Headers", "X-Response-Time");
        return String.format("{\"userId\":\"%s\",\"info\":\"%s\",\"cached\":%s}", userId, userData, isCached);
    }

    public static String getResponseTime(long num, double divisor) {
        DecimalFormat df = new DecimalFormat("#.###");
        return df.format(num / divisor) + "ms";
    }

    public void getUserById() {
        // using try-with-resources to avoid closing resources (boiler plate
        // code)

        // Step 1: Establishing a Connection
        try (Connection connection = DriverManager.getConnection(url, user, password);
            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY);) {
            preparedStatement.setInt(1, 1);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                String password = rs.getString("password");
                System.out.println(id + "," + name + "," + email + "," + country + "," + password);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

     private String getDbUserData(String userId) {
        try {

Connection connection = DriverManager.getConnection(url, user, password);
            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
            preparedStatement.setInt(1, 1);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();
             while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                String password = rs.getString("password");

                System.out.println(id + "," + name + "," + email + "," + country + "," + password);
                try{
                    JSONObject json= new JSONObject();
                    json.put("id",id);
                    json.put("name",name);
                    String userData = JSONObject json;
                jedis.setex(id, 3600, userData);
                return userData;
                }catch(Exception e){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
            }

    }

     }
}