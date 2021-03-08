package com.suchorski.siscaq.models;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.suchorski.siscaq.utils.SISCAQ;

public class Process {
	
	private long id;
	private Unity unity;
	private String description;
	private String nup;
	private String pam;
	private Date creationDate;
	private double value;
	private Date date;
	private String responsibleTr;
	private String info;
	private User responsible;
	private Type type;
	private Planning planning;
	private Modality modality;
	List<Status> status;
	
	public Process(long id, Unity unity, String description, String nup, String pam, Date creationDate, double value, Date date, String responsibleTr, String info, User responsible, Type type, Planning planning, Modality modality, List<Status> status) {
		this.id = id;
		this.unity = unity;
		this.description = description;
		this.nup = nup;
		this.pam = pam;
		this.creationDate = creationDate;
		this.value = value;
		this.date = date;
		this.responsibleTr = responsibleTr;
		this.info = info;
		this.responsible = responsible;
		this.type = type;
		this.planning = planning;
		this.modality = modality;
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Unity getUnity() {
		return unity;
	}
	
	public void setUnity(Unity unity) {
		this.unity = unity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getNup() {
		return nup;
	}
	
	public void setNup(String nup) {
		this.nup = nup;
	}
	
	public String getPam() {
		return pam;
	}
	
	public void setPam(String pam) {
		this.pam = pam;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getResponsibleTr() {
		return responsibleTr;
	}
	
	public void setResponsibleTr(String responsibleTr) {
		this.responsibleTr = responsibleTr;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
	
	public User getResponsible() {
		return responsible;
	}
	
	public void setResponsible(User responsible) {
		this.responsible = responsible;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public Planning getPlanning() {
		return planning;
	}
	
	public void setPlanning(Planning planning) {
		this.planning = planning;
	}
	public Modality getModality() {
		return modality;
	}
	
	public void setModality(Modality modality) {
		this.modality = modality;
	}
	
	public List<Status> getStatus() {
		return status;
	}
	
	public void setStatus(List<Status> status) {
		this.status = status;
	}
	
	public String getFormatedNup() {
		if (nup.length() == 17) {
			return nup.substring(0, 5) + "." + nup.substring(5, 11) + "/" + nup.substring(11, 15) + "-" + nup.substring(15, 17);
		}
		return nup;
	}
	
	public long getCountStatus() {
		return this.status.stream().filter(s -> s.getDate() != null).count();
	}

	public String bootstrapStatus(long days) {
		Date deadline = promptStatus(days);
		long diff = deadline.getTime() - (new java.util.Date()).getTime();
		long d = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		if (d >= 3) return "success";
		if (d > 0) return "warning";
		return "danger";
	}
	
	public Date promptStatus(long days) {
		return new Date(date.getTime() + days * SISCAQ.TIME.DAY_IN_MS);
	}
	
	public Date flexPromptStatus(long days, long lastDaysClosed, Date lastDateClosed, long firstDaysOpen) {
//		Date prompt = promptStatus(days);
		Date firstPromptOpen = promptStatus(firstDaysOpen);
		Date today = new Date((new java.util.Date()).getTime());
		if (firstPromptOpen.before(today)) {
//			Date lastClosedPrompt = new Date(lastDateClosed.getTime() + (days - lastDaysClosed) * SISCAQ.TIME.DAY_IN_MS);
//			if (lastClosedPrompt.before(today)) {
				return actualPromptStatus(days - firstDaysOpen);
//			}
//			return lastClosedPrompt;
		}		
		return promptStatus(days);		
	}
	
	public Date actualPromptStatus(long days) {
		return new Date((new java.util.Date()).getTime() + days * SISCAQ.TIME.DAY_IN_MS);
	}
	
	public String toCSV() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		StringBuilder sb = new StringBuilder();
		sb.append("\"" + unity.getInitials().replaceAll("\"", "\\\"") + "\",\"" + description.replaceAll("\"", "\\\"") + "\"," + getFormatedNup() + "," + sdf.format(date));
		for (Status s : status) {
			sb.append(",");
			if (s.getDate() == null) {
				sb.append("[" + sdf.format(promptStatus(s.getDays())) + "]");
			} else {
				sb.append(sdf.format(s.getDate()));
			}
		}
		sb.append("\n");
		return sb.toString();
	}
	
}
