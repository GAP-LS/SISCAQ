package com.suchorski.siscaq.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.models.Regress;
import com.suchorski.siscaq.models.Status;
import com.suchorski.siscaq.models.User;

public class RegressService extends AbstractService {

	public static void insert(Connection c, long idProcess, long idStatus, long idUser, String description) throws SQLException, DatabaseException {
		String sql = "INSERT INTO regress (id_process, id_status, id_user, description) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setLong(1, idProcess);
			ps.setLong(2, idStatus);
			ps.setLong(3, idUser);
			ps.setString(4, description);
			if (ps.executeUpdate() == 0) {
				throw new DatabaseException("Regressão não efetuada");
			}
		}
	}

	public static List<Regress> listByProcessId(long idProcess) throws SQLException, DatabaseException {
		String sql = "SELECT * FROM regress r JOIN status s ON r.id_status = s.id JOIN user u ON r.id_user = u.id WHERE id_process = ?";
		try (Connection c = getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setLong(1, idProcess);
				try (ResultSet rs = ps.executeQuery()) {
					List<Regress> regresses = new ArrayList<Regress>();
					while (rs.next()) {
						regresses.add(new Regress(rs.getLong("r.id"), new Status(rs.getLong("s.id"), rs.getString("s.description"), rs.getLong("s.days"), null), new User(rs.getLong("u.id"), rs.getString("u.cpf"), rs.getString("u.display_name"), rs.getDate("u.creation_date"), null), new Date(rs.getTimestamp("date").getTime()), rs.getString("r.description")));
					}
					return regresses;
				}
			}
		}
	}

}
