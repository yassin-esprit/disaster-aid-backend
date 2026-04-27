package tn.esprit.disasteraidbackend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Creates the target PostgreSQL database if it doesn't already exist.
 * Runs before Spring starts so the configured DataSource finds the DB ready.
 * PostgreSQL JDBC has no equivalent of MySQL's createDatabaseIfNotExist=true.
 */
final class DatabaseBootstrap {

    private DatabaseBootstrap() {}

    static void ensureDatabaseExists() {
        String url = env("DB_URL", "jdbc:postgresql://localhost:5432/disaster_aid");
        String user = env("DB_USERNAME", "postgres");
        String pass = env("DB_PASSWORD", "postgres");

        if (!url.startsWith("jdbc:postgresql://")) {
            return;
        }

        try {
            int lastSlash = url.lastIndexOf('/');
            int qMark = url.indexOf('?', lastSlash);
            String dbName = qMark > 0 ? url.substring(lastSlash + 1, qMark) : url.substring(lastSlash + 1);
            String params = qMark > 0 ? url.substring(qMark) : "";
            String adminUrl = url.substring(0, lastSlash + 1) + "postgres" + params;

            try (Connection conn = DriverManager.getConnection(adminUrl, user, pass)) {
                try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM pg_database WHERE datname = ?")) {
                    ps.setString(1, dbName);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return;
                        }
                    }
                }
                try (Statement st = conn.createStatement()) {
                    st.executeUpdate("CREATE DATABASE \"" + dbName.replace("\"", "\"\"") + "\"");
                    System.out.println("[bootstrap] Created PostgreSQL database: " + dbName);
                }
            }
        } catch (Exception e) {
            System.err.println("[bootstrap] Could not auto-create database (continuing anyway): " + e.getMessage());
        }
    }

    private static String env(String key, String fallback) {
        String v = System.getenv(key);
        if (v != null && !v.isBlank()) return v;
        v = System.getProperty(key);
        return (v != null && !v.isBlank()) ? v : fallback;
    }
}
