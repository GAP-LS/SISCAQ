package com.suchorski.siscaq.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.models.Level;

public class LevelService extends AbstractService {
	
	public static Level getById(long id) throws SQLException, DatabaseException {
		String sql = "SELECT * FROM level WHERE id = ?";
		try (Connection c = getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setLong(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return new Level(rs.getLong("id"), rs.getLong("level"), rs.getString("description"));
					}
					throw new DatabaseException("Nível não encontrado");
				}
			}
		}
	}
	
	public static Level getById(Connection c, long id) throws SQLException, DatabaseException {
		String sql = "SELECT * FROM level WHERE id = ?";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Level(rs.getLong("id"), rs.getLong("level"), rs.getString("description"));
				}
				throw new DatabaseException("Nível não encontrado");
			}
		}
	}
	
	public static long getFirstLevelId(Connection c) throws SQLException, DatabaseException {
		String sql = "SELECT id FROM level ORDER BY level ASC LIMIT 1";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("id");
				}
				throw new DatabaseException("Nenhum nível encontrado");
			}
		}
	}
	
	public static List<Level> list() throws SQLException, DatabaseException {
		String sql = "SELECT * FROM level ORDER BY level ASC";
		try (Connection c = getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				try (ResultSet rs = ps.executeQuery()) {
					List<Level> levels = new ArrayList<Level>();
					while (rs.next()) {
						levels.add(new Level(rs.getLong("id"), rs.getLong("level"), rs.getString("description")));
					}
					if (levels.isEmpty()) {						
						throw new DatabaseException("Nenhum nível foi encontrado");
					}
					return levels;
				}
			}
		}
	}

}
