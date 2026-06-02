1. The database executed the sql without the protection that PreparedStatement has, passing OR '1'=' which is always true.
The sql string read "SELECT id, name, program, gpa FROM student WHERE name LIKE %%' OR '1'='1%
Because the statement was true, it returned all the rows in the database.

2. If you ignore the return value, you will have no way of knowing if any rows are changed, and the program will display that it was successful.

3. The ID was assigned by the database because the id column was auto_increment, then the java file used getGeneratedKeys() to get the ID and return it.

4. I did not use Copilot or any other AI tool during this lab.