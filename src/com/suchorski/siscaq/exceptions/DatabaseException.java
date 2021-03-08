package com.suchorski.siscaq.exceptions;

public class DatabaseException extends Exception {

	private static final long serialVersionUID = 744989823177445360L;

	public DatabaseException(String message) {
		super(parseMessage(message));
	}
	
	private static String parseMessage(String message) {
		if (message.matches(".+ CONSTRAINT `fk_return_process` FOREIGN KEY .+")) {
			return "Processo tem regressões e não pode ser excluído";
		}
		if (message.matches(".+ CONSTRAINT `fk_ps_process` FOREIGN KEY .+")) {
			return "Processo já está em andamento e não pode ser excluído";
		}
		if (message.matches(".+ CONSTRAINT `fk_process_planning` FOREIGN KEY .+")) {
			return "Planejamento contém processos e não pode ser excluído";
		}
		return message;
	}
	
}
