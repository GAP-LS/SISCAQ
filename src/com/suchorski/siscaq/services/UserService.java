package com.suchorski.siscaq.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.models.User;

public class UserService extends AbstractService {
	
	public static User getById(long id) throws SQLException, DatabaseException {
		String sql = "SELECT * FROM user WHERE id = ?";
		try (Connection c = getConnection(false)) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setLong(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						User u = new User(rs.getLong("id"), rs.getString("cpf"), rs.getString("display_name"), rs.getDate("creation_date"), LevelService.getById(c, rs.getLong("id_level")));
						c.commit();
						return u;
					}
					c.commit();
					throw new DatabaseException("Usuário não encontrado");
				}
			}
		}
	}
	
	public static User login(String cpf, String password) throws Exception {
		String displayName = ldapLogin(cpf, password);
		try (Connection c = getConnection(false)) {
			User u;
			try {
				u = getByCpf(c, cpf);
				updateDisplayName(c, cpf, displayName);
			} catch (DatabaseException e) {
				create(c, cpf, displayName);
				u = getByCpf(c, cpf);
			}
			c.commit();
			return u;
		}
	}
	
	public static List<User> list() throws SQLException, DatabaseException {
		String sql = "SELECT * FROM user ORDER BY SUBSTRING_INDEX(SUBSTRING_INDEX(display_name, ' ', 2), ' ', -1) ASC";
		try (Connection c = getConnection(false)) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				try (ResultSet rs = ps.executeQuery()) {
					List<User> users = new ArrayList<User>();
					while (rs.next()) {
						users.add(new User(rs.getLong("id"), rs.getString("cpf"), rs.getString("display_name"), rs.getDate("creation_date"), LevelService.getById(c, rs.getLong("id_level"))));
					}
					c.commit();
					if (users.isEmpty()) {
						throw new DatabaseException("Nenhum usuário foi encontrado");
					}
					return users;						
				}
			}
		}
	}
	
	public static List<User> listAdministrators() throws SQLException, DatabaseException {
		String sql = "SELECT * FROM user JOIN level ON user.id_level = level.id WHERE level >= 4 AND level <= 10 ORDER BY display_name ASC";
		try (Connection c = getConnection(false)) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				try (ResultSet rs = ps.executeQuery()) {
					List<User> users = new ArrayList<User>();
					while (rs.next()) {
						users.add(new User(rs.getLong("id"), rs.getString("cpf"), rs.getString("display_name"), rs.getDate("creation_date"), LevelService.getById(c, rs.getLong("id_level"))));
					}
					c.commit();
					return users;						
				}
			}
		}
	}
	
	public static List<User> listResponsibles() throws SQLException, DatabaseException {
		String sql = "SELECT id, display_name FROM user ORDER BY SUBSTRING_INDEX(SUBSTRING_INDEX(display_name, ' ', 2), ' ', -1) ASC";
		try (Connection c = getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				try (ResultSet rs = ps.executeQuery()) {
					List<User> responsibles = new ArrayList<User>();
					while (rs.next()) {
						responsibles.add(new User(rs.getLong("id"), rs.getString("display_name")));
					}
					if (responsibles.isEmpty()) {
						throw new DatabaseException("Nenhum usuário foi encontrado");
					}
					return responsibles;
				}
			}
		}
	}
	
	public static void updateLevel(long idUser, long idLevel) throws SQLException {
		String sql = "UPDATE user SET id_level = ? WHERE id = ?";
		try (Connection c = getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setLong(1, idLevel);
				ps.setLong(2, idUser);
				ps.executeUpdate();
			}
		}
	}
	
	private static void create(Connection c, String cpf, String displayName) throws SQLException, DatabaseException {
		String sql = "INSERT INTO user (cpf, display_name, id_level) VALUES (?, ?, ?)";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, cpf);
			ps.setString(2, displayName);
			ps.setLong(3, LevelService.getFirstLevelId(c));
			ps.executeUpdate();
		}
	}
	
	private static User getByCpf(Connection c, String cpf) throws SQLException, DatabaseException {
		String sql = "SELECT * FROM user WHERE cpf = ?";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, cpf);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new User(rs.getLong("id"), rs.getString("cpf"), rs.getString("display_name"), rs.getDate("creation_date"), LevelService.getById(c, rs.getLong("id_level")));
				}
				throw new DatabaseException("Usuário não encontrado");
			}
		}
	}
	
	private static void updateDisplayName(Connection c, String cpf, String displayName) throws SQLException {
		String sql = "UPDATE user SET display_name = ? WHERE cpf = ?";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, displayName);
			ps.setString(2, cpf);
			ps.executeUpdate();
		}
	}

}
