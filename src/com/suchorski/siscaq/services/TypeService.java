package com.suchorski.siscaq.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.models.Type;

public class TypeService extends AbstractService {
	
	public static Type getById(Connection c, long id) throws SQLException, DatabaseException {
		String sql = "SELECT * FROM type WHERE id = ?";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Type(rs.getLong("id"), rs.getString("title"));
				}
				throw new DatabaseException("Tipo não encontrado");
			}
		}
	}
	
	public static List<Type> list() throws SQLException, DatabaseException {
		String sql = "SELECT * FROM type ORDER BY title ASC";
		try (Connection c = getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				try (ResultSet rs = ps.executeQuery()) {
					List<Type> levels = new ArrayList<Type>();
					while (rs.next()) {
						levels.add(new Type(rs.getLong("id"), rs.getString("title")));
					}
					if (levels.isEmpty()) {						
						throw new DatabaseException("Nenhum tipo foi encontrado");
					}
					return levels;
				}
			}
		}
	}

}
