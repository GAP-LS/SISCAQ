package com.suchorski.siscaq.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.exceptions.InfoException;
import com.suchorski.siscaq.models.Modality;
import com.suchorski.siscaq.models.Process;
import com.suchorski.siscaq.models.Unity;
import com.suchorski.siscaq.models.User;

public class ProcessService extends AbstractService {
	
	public static void insert(long unityId, String description, String nup, String pam, Date date, String responsibleTr, double value, String info, long idResponsible, long idType, long idPlanning, long idModality) throws SQLException, DatabaseException {
		String sql = "INSERT INTO process (id_unity, description, nup, pam, date, responsible_tr, value, info, id_responsible, id_type, id_planning, id_modality) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection c = getConnection(false)) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setLong(1, unityId);
				ps.setString(2, description);
				ps.setString(3, nup);
				ps.setString(4, pam);
				ps.setDate(5, date);
				ps.setString(6, responsibleTr);
				ps.setDouble(7, value);
				ps.setString(8, info);
				ps.setLong(9, idResponsible);
				ps.setLong(10, idType);
				ps.setLong(11, idPlanning);
				ps.setLong(12, idModality);
				ps.executeUpdate();
				c.commit();
			}
		}
	}
	
	public static Process getById(long id) throws SQLException, DatabaseException {
		String sql = "SELECT * FROM process p JOIN unity u ON p.id_unity = u.id JOIN planning pp ON p.id_planning = pp.id JOIN modality m ON p.id_modality = m.id JOIN user r ON p.id_responsible = r.id WHERE p.id = ?";
		try (Connection c = getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setLong(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return new Process(rs.getLong("p.id"), new Unity(rs.getLong("u.id"), rs.getString("u.initials")), rs.getString("p.description"), rs.getString("p.nup"), rs.getString("p.pam"), rs.getDate("p.creation_date"), rs.getDouble("p.value"), rs.getDate("p.date"), rs.getString("p.responsible_tr"), rs.getString("p.info"), new User(rs.getLong("r.id"), rs.getString("display_name")), TypeService.getById(c, rs.getLong("p.id_type")), PlanningService.getById(c, rs.getLong("pp.id")), new Modality(rs.getLong("m.id"), rs.getString("m.title")), StatusService.listByProcessId(c, rs.getLong("p.id")));
					}
					throw new DatabaseException("Processo não localizado");
				}
			}
		}
	}
	
	public static List<Process> list(String search, long idPlanning) throws SQLException, DatabaseException, InfoException {
		String sql;
		boolean hasSearch = !search.isEmpty();
		if (hasSearch) {
			sql = "SELECT * FROM process p JOIN unity u ON p.id_unity = u.id JOIN planning pp ON p.id_planning = pp.id JOIN modality m ON p.id_modality = m.id JOIN user r ON p.id_responsible = r.id WHERE pp.id = ? AND (u.initials LIKE ? OR p.description LIKE ? OR r.display_name LIKE ? OR DATE_FORMAT(p.date, '%d/%m/%Y') LIKE ? OR p.responsible_tr LIKE ?) ORDER BY date ASC";
		} else {
			sql = "SELECT * FROM process p JOIN unity u ON p.id_unity = u.id JOIN planning pp ON p.id_planning = pp.id JOIN modality m ON p.id_modality = m.id JOIN user r ON p.id_responsible = r.id WHERE pp.id = ? ORDER BY date ASC";
		}
		try (Connection c = getConnection(false)) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setLong(1, idPlanning);
				if (hasSearch) {
					search = "%" + search + "%";
					ps.setString(2, search);
					ps.setString(3, search);
					ps.setString(4, search + " %");
					ps.setString(5, search);
					ps.setString(6, search);
				}
				try (ResultSet rs = ps.executeQuery()) {
					List<Process> processes = new ArrayList<Process>();
					while (rs.next()) {
						processes.add(new Process(rs.getLong("p.id"), new Unity(rs.getLong("u.id"), rs.getString("u.initials")), rs.getString("p.description"), rs.getString("p.nup"), rs.getString("p.pam"), rs.getDate("p.creation_date"), rs.getDouble("p.value"), rs.getDate("p.date"), rs.getString("p.responsible_tr"), rs.getString("p.info"), new User(rs.getLong("r.id"), rs.getString("display_name")), TypeService.getById(c, rs.getLong("p.id_type")), PlanningService.getById(c, rs.getLong("p.id_planning")), new Modality(rs.getLong("m.id"), rs.getString("m.title")), StatusService.listByProcessId(c, rs.getLong("p.id"))));
					}
					c.commit();
					if (processes.isEmpty()) {
						throw new InfoException("Nenhum processo foi encontrado");
					}
					return processes;						
				}
			}
		}
	}
	
	public static void update(Process process) throws SQLException {
		String sql = "UPDATE process SET id_unity = ?, description = ?, nup = ?, pam = ?, date = ?, responsible_tr = ?, value = ?, info = ?, id_responsible = ?, id_type = ?, id_modality = ? WHERE id = ?";
		try (Connection c = getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setLong(1, process.getUnity().getId());
				ps.setString(2, process.getDescription());
				ps.setString(3, process.getNup());
				ps.setString(4, process.getPam());
				ps.setDate(5, process.getDate());
				ps.setString(6, process.getResponsibleTr());
				ps.setDouble(7, process.getValue());
				ps.setString(8, process.getInfo());
				ps.setLong(9, process.getResponsible().getId());
				ps.setLong(10, process.getType().getId());
				ps.setLong(11, process.getModality().getId());
				ps.setLong(12, process.getId());
				ps.executeUpdate();
			}
		}
	}
	
	public static void forward(long id, Date date) throws SQLException {
		String sql = "INSERT INTO process_status (date, id_process, id_status) VALUES (?, ?, (SELECT s.id FROM status s WHERE s.days > ? ORDER BY s.days ASC LIMIT 1))";
		try (Connection c = getConnection(false)) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setDate(1, date);
				ps.setLong(2, id);
				ps.setLong(3, idToForward(c, id));
				ps.executeUpdate();
				c.commit();
			}
		}
	}
	
	private static long idToForward(Connection c, long id) throws SQLException {
		String sql = "SELECT st.days days FROM process_status pst JOIN status st ON pst.id_status = st.id WHERE pst.id_process = ? ORDER BY days DESC LIMIT 1";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("days");
				}
				return -1;
			}
		}
	}

	public static void backward(long id, long idUser, String description) throws SQLException, DatabaseException {
		String sql = "DELETE FROM process_status WHERE id_process = ? AND id_status = ?";
		try (Connection c = getConnection(false)) {
			long idStatus = getLastStatus(c, id);
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setLong(1, id);
				ps.setLong(2, idStatus);
				if (ps.executeUpdate() > 0) {
					RegressService.insert(c, id, idStatus, idUser, description);
				}
				c.commit();
			}
		}
	}
	
	public static void deleteById(long id) throws SQLException, DatabaseException {
		String sql = "DELETE FROM process WHERE id = ?";
		try (Connection c = getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setLong(1, id);
				ps.executeUpdate();
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	private static long getLastStatus(Connection c, long id) throws SQLException, DatabaseException {
		String sql = "SELECT id_status FROM process_status ps JOIN status s ON ps.id_status = s.id WHERE id_process = ? ORDER BY days DESC LIMIT 1";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("id_status");
				}
				throw new DatabaseException("Processo está em fase inicial");
			}
		}
	}

}
