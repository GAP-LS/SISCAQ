package com.suchorski.siscaq.services;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class AbstractService {
	
	protected static Connection getConnection(boolean autoCommit) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(dbUrl(), dbUsername(), dbPassword());
			con.setAutoCommit(autoCommit);
			return con;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected static Connection getConnection() {
		return getConnection(true);
	}

	protected static int lastId(Connection c) throws SQLException {
		String LAST_ID_SQL = "SELECT LAST_INSERT_ID() LAST";
		try (PreparedStatement ps = (PreparedStatement) c.prepareStatement(LAST_ID_SQL); ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("LAST");
			} else {
				throw new SQLException();
			}
		}
	}

	protected static String ldapLogin(String username, String password) throws Exception {
		DirContext ctx = null;
		ldapCheck(username, password);
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, ldapDomain());
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, ldapServerLogin());
			env.put(Context.SECURITY_CREDENTIALS, ldapServerPassword());
			ctx = new InitialDirContext(env);
			SearchControls sc = new SearchControls();
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration<SearchResult> naming = ctx.search(ldapSearch(), ldapFilter(username), sc);
			if (naming.hasMore()) {
				SearchResult result = (SearchResult) naming.next();
				Attributes attrs = result.getAttributes();
				List<String> display = ldapDisplay();
				StringBuilder name = new StringBuilder();
				for (String s : display) {
					name.append(attrs.get(s).get() + " ");
				}
				return name.toString().trim();
			} else {
				throw new Exception("Usuário não encontrado");
			}
		} catch (AuthenticationException e) {
			throw new Exception("Credenciais do servidor inválidas. Favor entrar em contato com a seção de Informática!");
		} finally {
			if (ctx != null)
				ctx.close();
		}
	}
	
	private static void ldapCheck(String username, String password) throws Exception {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, ldapDomain());
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, ldapUserLogin(username));
			env.put(Context.SECURITY_CREDENTIALS, password);
			DirContext ctx = new InitialDirContext(env);
			ctx.close();
		} catch (AuthenticationException e) {
			throw new Exception("Credenciais inválidas");
		}
	}

	private static JSONObject getConfig() {
		try {
			String filename = AbstractService.class.getClassLoader().getResource("config.json").getPath();
			if (filename.matches("\\/[a-zA-Z]:\\/.+")) {
				filename = filename.substring(1); // /C:/ on windows systems
			}
			return new JSONObject(new String(Files.readAllBytes(Paths.get(filename))));
		} catch (Exception e) {
			return null;
		}
	}

	private static String dbIp() {
		return getConfig().getJSONObject("database").getString("ip");
	}

	private static int dbPort() {
		return getConfig().getJSONObject("database").getInt("port");
	}

	private static String dbUsername() {
		return getConfig().getJSONObject("database").getString("username");
	}

	private static String dbPassword() {
		return getConfig().getJSONObject("database").getString("password");
	}

	private static String dbDatabase() {
		return getConfig().getJSONObject("database").getString("database");
	}

	private static String dbUrl() {
		return "jdbc:mysql://" + dbIp() + ":" + dbPort() + "/" + dbDatabase();
	}

	private static String ldapDomain() {
		return getConfig().getJSONObject("ldap").getString("domain");
	}
	
	private static String ldapSearch() {
		return getConfig().getJSONObject("ldap").getString("search");
	}
	
	private static String ldapFilter(String username) {
		return String.format(getConfig().getJSONObject("ldap").getString("filter"), username);
	}

	private static String ldapServerLogin() {
		return getConfig().getJSONObject("ldap").getJSONObject("server").getString("username");
	}
	
	private static String ldapServerPassword() {
		return getConfig().getJSONObject("ldap").getJSONObject("server").getString("password");
	}
	
	private static String ldapUserLogin(String username) {
		return String.format(getConfig().getJSONObject("ldap").getJSONObject("user").getString("username"), username);
	}
	
	private static List<String> ldapDisplay() {
		List<String> display = new ArrayList<String>();
		JSONArray a = getConfig().getJSONObject("ldap").getJSONArray("display");
		for (int i = 0; i < a.length(); ++i) {
			display.add(a.getString(i));
		}
		return display;
	}

}
