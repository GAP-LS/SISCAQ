package com.suchorski.siscaq.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.models.Status;

public class StatusService extends AbstractService {
	
	public static List<Status> list() throws SQLException, DatabaseException {
		String sql = "SELECT * FROM status ORDER BY days ASC";
		try (Connection c = getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				try (ResultSet rs= ps.executeQuery()) {
					List<Status> status = new ArrayList<Status>();
					while (rs.next()) {
						status.add(new Status(rs.getLong("id"), rs.getString("description"), rs.getLong("days"), null));
					}
					if (status.isEmpty()) {
						throw new DatabaseException("Nenhum status foi encontrado");
					}
					return status;
				}
			}
		}
	}

	public static List<Status> listByProcessId(Connection c, long id) throws SQLException, DatabaseException {
		String sql = "SELECT s.id id, s.description description, s.days days, ps.date date FROM process_status ps RIGHT JOIN status s ON ps.id_status = s.id WHERE ps.id_process = ? UNION SELECT id, description, days, NULL FROM status WHERE id NOT IN (SELECT st.id FROM process_status pst RIGHT JOIN status st ON pst.id_status = st.id WHERE pst.id_process = ?) ORDER BY days ASC";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setLong(1, id);
			ps.setLong(2, id);
			try (ResultSet rs = ps.executeQuery()) {
				List<Status> status = new ArrayList<Status>();
				while (rs.next()) {
					status.add(new Status(rs.getLong("id"), rs.getString("description"), rs.getLong("days"), rs.getDate("date")));
				}
				if (status.isEmpty()) {
					throw new DatabaseException("Nenhum status foi encontrado");
				}
				return status;
			}
		}
	}

}
