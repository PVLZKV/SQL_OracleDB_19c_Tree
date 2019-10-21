import jdk.swing.interop.SwingInterOpUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class Main {

    public static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:orcl";

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("JDBC драйвер не найден!");
            System.exit(-1);
        }

        Connection conn = DriverManager.getConnection(JDBC_URL, "c##user", "password");
        Statement stmt = conn.createStatement();

        stmt.execute("DROP TABLE Linux");
        stmt.execute("CREATE TABLE Linux (Id NUMBER(2), ParentId NUMBER(2), Name VARCHAR2(30))");

        stmt.execute("INSERT ALL " +
                "INTO Linux (Id, ParentId, Name) VALUES (1, 0, 'debian')" +
                "INTO Linux (Id, ParentId, Name) VALUES (2, 1, 'ubuntu')" +
                "INTO Linux (Id, ParentId, Name) VALUES (3, 2, 'kubuntu')" +
                "INTO Linux (Id, ParentId, Name) VALUES (4, 2, 'lubuntu')" +
                "INTO Linux (Id, ParentId, Name) VALUES (5, 2, 'linux mint')" +
                "INTO Linux (Id, ParentId, Name) VALUES (6, 0, 'slackware')" +
                "INTO Linux (Id, ParentId, Name) VALUES (7, 6, 'slax')" +
                "INTO Linux (Id, ParentId, Name) VALUES (8, 7, 'wolvix')" +
                "INTO Linux (Id, ParentId, Name) VALUES (9, 7, 'slampp')" +
                "INTO Linux (Id, ParentId, Name) VALUES (10, 7, 'dnalinux')" +
                "INTO Linux (Id, ParentId, Name) VALUES (11, 6, 'suse')" +
                "INTO Linux (Id, ParentId, Name) VALUES (12, 11, 'linkat')" +
                "INTO Linux (Id, ParentId, Name) VALUES (13, 11, 'opensuse')" +
                "INTO Linux (Id, ParentId, Name) VALUES (14, 0, 'redhat') " +
                "INTO Linux (Id, ParentId, Name) VALUES (15, 14, 'fedora core')" +
                "INTO Linux (Id, ParentId, Name) VALUES (16, 15, 'sailfish os')" +
                "INTO Linux (Id, ParentId, Name) VALUES (17, 15, 'fedora')" +
                "SELECT * FROM dual");


        // Task 2 / Part 1 (output as a tree)
        ResultSet rs1 = stmt.executeQuery(
                "SELECT level, id, parentid, LPAD (' ', 2*(LEVEL-1)) || Name " +
                        "FROM Linux " +
                        "START WITH ParentId = 0 " +
                        "CONNECT BY PRIOR Id = ParentId"
        );
        // Print block
        System.out.println();
        System.out.println("Task 2 / Part 1 (output as a tree)");
        System.out.println("--------------------------------------------------");
        System.out.format("%5s | %2s | %8s | %8s%n", "Level", "Id", "ParentId", "TREE");
        System.out.println("--------------------------------------------------");
        while (rs1.next()) {
            int level = rs1.getInt(1);
            int id = rs1.getInt(2);
            int parentid = rs1.getInt(3);
            String lpad = rs1.getString(4);
            System.out.format("%3d %3s %2d %s %4d %5s %s%n", level, "|", id, "|", parentid, "|", lpad);
        }


        // Task 2 / Part 2 (sort by '%a%', output as a tree)
        ResultSet rs4 = stmt.executeQuery(
                "SELECT Level, Id, ParentId, LPAD (' ', 2*(LEVEL-1)) || Name " +

                        "FROM " +
                        "( " +
                        "SELECT * " +
                        "FROM Linux " +
                        "WHERE NOT (LEVEL = 1 AND CONNECT_BY_ISLEAF = 1) " +
                        "START WITH Name LIKE '%a%' " +
                        "CONNECT BY PRIOR ParentId = Id " +

                        "UNION " +

                        "SELECT * " +
                        "FROM (SELECT * FROM Linux WHERE ParentId <> 0) " +
                        "WHERE NOT (LEVEL = 1 AND CONNECT_BY_ISLEAF = 1) " +
                        "CONNECT BY PRIOR ParentId = Id " +
                        "AND (PRIOR Name LIKE '%a%' OR Name LIKE '%a%') " +
                        ") " +

                        "START WITH ParentId = 0 " +
                        "CONNECT BY PRIOR Id = ParentId"
        );
        // Print block
        System.out.println();
        System.out.println("Task 2 / Part 2.1 (inside out inside out WITH Name LIKE '%a%')");
        System.out.println("--------------------------------------------------");
        System.out.format("%5s | %2s | %8s | %8s%n", "Level", "Id", "ParentId", "TREE");
        System.out.println("--------------------------------------------------");
        while (rs4.next()) {
            int level = rs4.getInt(1);
            int id = rs4.getInt(2);
            int parentid = rs4.getInt(3);
            String lpad = rs4.getString(4);
            System.out.format("%3d %3s %2d %s %4d %5s %s%n", level, "|", id, "|", parentid, "|", lpad);
        }
    }
}