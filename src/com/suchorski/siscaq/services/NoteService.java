package com.suchorski.siscaq.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.models.Note;
import com.suchorski.siscaq.models.Status;
import com.suchorski.siscaq.models.User;

public class NoteService extends AbstractService {
	
	public static void insert(long idProcess, long idStatus, long idUser, String note) throws SQLException, DatabaseException {
		String sql = "INSERT INTO note (creation_date, id_process, id_status, id_user, note) VALUES (?, ?, ?, ?, ?)";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setDate(1, new Date(new java.util.Date().getTime()));
			ps.setLong(2, idProcess);
			ps.setLong(3, idStatus);
			ps.setLong(4, idUser);
			ps.setString(5, note);
			if (ps.executeUpdate() == 0) {
				throw new DatabaseException("Informação não adicionada");
			}
		}
	}
	
	public static List<Note> listByProcessId(long idProcess) throws SQLException, DatabaseException {
		String sql = "SELECT * FROM note n JOIN status s ON n.id_status = s.id JOIN user u ON n.id_user = u.id WHERE id_process = ?";
		try (Connection c = getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setLong(1, idProcess);
				try (ResultSet rs = ps.executeQuery()) {
					List<Note> notes = new ArrayList<Note>();
					while (rs.next()) {
						notes.add(new Note(rs.getLong("n.id"), new Status(rs.getLong("s.id"), rs.getString("s.description"), rs.getLong("s.days"), null), new User(rs.getLong("u.id"), rs.getString("u.cpf"), rs.getString("u.display_name"), rs.getDate("u.creation_date"), null), new Date(rs.getTimestamp("creation_date").getTime()), rs.getString("n.note")));
					}
					return notes;
				}
			}
		}
	}

}
