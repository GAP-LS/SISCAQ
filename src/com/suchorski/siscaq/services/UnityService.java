package com.suchorski.siscaq.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.models.Unity;

public class UnityService extends AbstractService {

	public static List<Unity> list() throws SQLException, DatabaseException {
		String sql = "SELECT * FROM unity ORDER BY initials ASC";
		try (Connection c = getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				try (ResultSet rs = ps.executeQuery()) {
					List<Unity> units = new ArrayList<Unity>();
					while (rs.next()) {
						units.add(new Unity(rs.getLong("id"), rs.getString("initials")));
					}
					if (units.isEmpty()) {
						throw new DatabaseException("Nenhuma unidade está cadastrada");
					}
					return units;
				}
			}
		}
	}

}
