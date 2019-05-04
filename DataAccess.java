import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class DataAccess {	
	//  Database credentials
	private String connectionString;
	private String user;
	private String password;

	
	private Connection GetConnection() throws SQLException
	{
		System.out.println("Connecting to database...");
		return DriverManager.getConnection(this.connectionString, this.user, this.password);
	}
	
	public List<Movie> GetAllMovies() throws SQLException
	{
		List<Movie> movies = new ArrayList<Movie>();
		Connection connection = null;
		Statement stmt = null;
		
		try{
			connection = this.GetConnection();
	
			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = connection.createStatement();
			String sql;
			sql = "SELECT ID, Title, Year, Genre, Length FROM Movies ORDER BY Title";
			ResultSet rs = stmt.executeQuery(sql);
				
			//STEP 5: Extract data from result set
			while(rs.next()){
				Movie movie = new Movie();
				
				//Retrieve by column name
				movie.setID(rs.getInt("ID"));
				movie.setTitle(rs.getString("Title"));
		        movie.setYear(rs.getInt("Year"));
		        movie.setGenre(rs.getString("Genre"));
		        movie.setLength(rs.getInt("Length"));
		        
		        movies.add(movie);
		    }
			//STEP 6: Clean-up environment
			rs.close();
		}
		finally {
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}
			finally {
				if(connection!=null)
					connection.close();
			}//end finally try
		}//end try

		return movies;
	}

	public Movie GetMovieByID(String id) throws SQLException
	{
		Movie movie = new Movie();
		Connection connection = null;
		Statement stmt = null;
		
		try{
			connection = this.GetConnection();
	
			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = connection.createStatement();
			String sql;
			
			// Don't EVER concat sql into a string to insert parameters into a query!
			// Always use prepared query for security reasons to prevent SQL injections
			sql = "SELECT ID, Title, Year, Genre, Length FROM Movies WHERE ID = " + id;
			System.out.println("Execute SQL: " + sql);
			ResultSet rs = stmt.executeQuery(sql);
	
			//STEP 5: Extract data from result set
			while(rs.next()){
				movie = new Movie();
				
				//Retrieve by column name
				movie.setID(rs.getInt("ID"));
				movie.setTitle(rs.getString("Title"));
		        movie.setYear(rs.getInt("Year"));
		        movie.setGenre(rs.getString("Genre"));
		        movie.setLength(rs.getInt("Length"));
		    }
			//STEP 6: Clean-up environment
			rs.close();
		}
		finally {
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}
			finally {
				if(connection!=null)
					connection.close();
			}//end finally try
		}//end try

		return movie;
	}
	
	public void UpdateMovie(Movie movie) throws SQLException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		
		try{
			connection = this.GetConnection();
			
			// Deactivate auto-commit mode to be able to rollback in case something goes sour
			connection.setAutoCommit(false);
	
			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			String sql = "UPDATE Movies Set Title = ?, Year = ?, Genre = ?, Length = ? WHERE ID = ?";

			stmt = connection.prepareStatement(sql);

			stmt.setInt(5, movie.getID());
			stmt.setString(1, movie.getTitle());
			stmt.setInt(2, movie.getYear());
			stmt.setString(3, movie.getGenre());
			stmt.setInt(4, movie.getLength());
			
			stmt.executeUpdate();
			
			// Commit transaction
			connection.commit();
		}
		catch (SQLException ex)
		{
			try
			{
			connection.rollback();}
			catch (SQLException ex2)
			{
				
			}
			
			throw ex;
			
		}
		finally {
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}
			finally {
				if(connection!=null)
					connection.close();
			}//end finally try
		}//end try

	}
    public void mrPisulka(int cm){
        if(cm > 15)
            System.out.println("Your pisulka is normal")
        else 
            System.out.println("Your pisulka is small")

    }
	
	public void DeleteMovie(int id) throws SQLException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		
		try{
			connection = this.GetConnection();
			
			// Deactivate auto-commit mode to be able to rollback in case something goes sour
			connection.setAutoCommit(false);
	
			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			String sql = "DELETE FROM Movies WHERE ID = ?";

			stmt = connection.prepareStatement(sql);

			stmt.setInt(1, id);

			stmt.executeUpdate();
			
			// Commit transaction
			connection.commit();
		}
		catch (SQLException ex)
		{
			try
			{
			connection.rollback();
			}
			catch (SQLException ex2) {}
			
			throw ex;
		}
		finally {
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}
			finally {
				if(connection!=null)
					connection.close();
			}//end finally try
		}//end try

	}

	
	public void InsertMovie(Movie movie) throws SQLException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		
		try{
			connection = this.GetConnection();
			
			// Deactivate auto-commit mode to be able to rollback in case something goes sour
			connection.setAutoCommit(false);
	
			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			String sql = "INSERT INTO Movies (Title, Year, Genre, Length) VALUES (?, ?, ?, ?)";

			stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, movie.getTitle());
			stmt.setInt(2, movie.getYear());
			stmt.setString(3, movie.getGenre());
			stmt.setInt(4, movie.getLength());
			
			stmt.executeUpdate();
		    
		    // Get the generated ID and update the Movie object for further editing
		    ResultSet rs = stmt.getGeneratedKeys();
		    rs.next();
		    movie.setID(rs.getInt(1)); 
			
			// Commit transaction
			connection.commit();
			
			//STEP 6: Clean-up environment
			rs.close();
		}
		catch (SQLException ex)
		{
			try
			{
				connection.rollback();}
			catch (SQLException ex2)
			{
				
			}
			
			throw ex;
			
		}
		finally {
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}
			finally {
				if(connection!=null)
					connection.close();
			}//end finally try
		}//end try

	}
}
