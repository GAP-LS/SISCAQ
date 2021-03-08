package com.suchorski.siscaq.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.siscaq.exceptions.DatabaseException;
import com.suchorski.siscaq.models.Modality;

public class ModalityService extends AbstractService {
	
	public static Modality getById(Connection c, long id) throws SQLException, DatabaseException {
		String sql = "SELECT * FROM modality WHERE id = ?";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Modality(rs.getLong("id"), rs.getString("title"));
				}
				throw new DatabaseException("Modalidade não encontrada");
			}
		}
	}
	
	public static List<Modality> list() throws SQLException, DatabaseException {
		String sql = "SELECT * FROM modality ORDER BY title ASC";
		try (Connection c = getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				try (ResultSet rs = ps.executeQuery()) {
					List<Modality> modalitys = new ArrayList<Modality>();
					while (rs.next()) {
						modalitys.add(new Modality(rs.getLong("id"), rs.getString("title")));
					}
					if (modalitys.isEmpty()) {						
						throw new DatabaseException("Nenhuma modalidade foi encontrada");
					}
					return modalitys;
				}
			}
		}
	}

}
