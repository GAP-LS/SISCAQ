package com.suchorski.siscaq.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.models.Planning;

public class PlanningService extends AbstractService {
	
	public static void insert(String title) throws SQLException, DatabaseException {
		String sql = "INSERT INTO planning (title) VALUES (?)";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, title);
			if (ps.executeUpdate() == 0) {
				throw new DatabaseException("Plano não adicionado");
			}
		}
	}
	
	public static Planning getById(long id) throws SQLException, DatabaseException {
		String sql = "SELECT * FROM planning WHERE id = ?";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Planning(rs.getLong("id"), rs.getString("title"));
				}
				throw new DatabaseException("Plano não encontrado");
			}
		}
	}
	
	public static Planning getLast() throws SQLException, DatabaseException {
		String sql = "SELECT * FROM planning ORDER BY creation_date DESC LIMIT 1";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return new Planning(rs.getLong("id"), rs.getString("title"));
			}
			throw new DatabaseException("Nenhum plano existente. Procure a seção de informática!");
		}
	}

	public static Planning getById(Connection c, long id) throws SQLException, DatabaseException {
		String sql = "SELECT * FROM planning WHERE id = ?";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Planning(rs.getLong("id"), rs.getString("title"));
				}
				throw new DatabaseException("Plano não encontrado");
			}
		}
	}
	
	public static List<Planning> list() throws SQLException, DatabaseException {
		String sql = "SELECT * FROM planning ORDER BY creation_date DESC";
		try (Connection c = getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				try (ResultSet rs = ps.executeQuery()) {
					List<Planning> plannings = new ArrayList<Planning>();
					while (rs.next()) {
						plannings.add(new Planning(rs.getLong("id"), rs.getString("title")));
					}
					if (plannings.isEmpty()) {						
						throw new DatabaseException("Nenhum planejamento foi encontrado");
					}
					return plannings;
				}
			}
		}
	}
	
	public static void update(long id, String title) throws SQLException {
		String sql = "UPDATE planning set title = ? WHERE id = ?";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, title);
			ps.setLong(2, id);
			ps.executeUpdate();
		}
	}
	
	public static void deleteById(long id) throws SQLException, DatabaseException {
		String sql = "DELETE FROM planning WHERE id = ?";
		try (Connection c = getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setLong(1, id);
				ps.executeUpdate();
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

}
