DROP SCHEMA IF EXISTS siscaq;
CREATE SCHEMA siscaq DEFAULT CHARACTER SET utf8mb4;
USE siscaq;

CREATE TABLE level (
	id INT NOT NULL AUTO_INCREMENT,
	level INT NOT NULL,
	description VARCHAR(64) NOT NULL,
	CONSTRAINT pk_id PRIMARY KEY (id),
	CONSTRAINT uq_level UNIQUE (level)
) ENGINE=InnoDB;

CREATE TABLE user (
	id INT NOT NULL AUTO_INCREMENT,
	cpf CHAR(11) NOT NULL,
	display_name VARCHAR(128) NOT NULL DEFAULT '',
	creation_date DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
	id_level INT NOT NULL,
	CONSTRAINT pk_id PRIMARY KEY (id),
	CONSTRAINT uq_cpf UNIQUE (cpf),
	CONSTRAINT fk_user_level FOREIGN KEY (id_level) REFERENCES level(id)
) ENGINE=InnoDB;

CREATE TABLE status (
	id INT NOT NULL AUTO_INCREMENT,
    description VARCHAR(128) NOT NULL,
    days INT NOT NULL,
    CONSTRAINT pk_id PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE unity (
	id INT NOT NULL AUTO_INCREMENT,
	initials VARCHAR(16) NOT NULL,
	CONSTRAINT pk_id PRIMARY KEY (id),
	CONSTRAINT uq_initials UNIQUE (initials)
) ENGINE=InnoDB;

CREATE TABLE type (
	id INT NOT NULL AUTO_INCREMENT,
	title VARCHAR(64) NOT NULL,
	CONSTRAINT pk_id PRIMARY KEY (id),
    CONSTRAINT uq_title UNIQUE (title)
) ENGINE=InnoDB;

CREATE TABLE planning (
	id INT NOT NULL AUTO_INCREMENT,
	title VARCHAR(64) NOT NULL,
	creation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_id PRIMARY KEY (id),
    CONSTRAINT uq_planning_title UNIQUE (title)
) ENGINE=InnoDB;

CREATE TABLE modality (
	id INT NOT NULL AUTO_INCREMENT,
	title VARCHAR(64) NOT NULL,
	CONSTRAINT pk_id PRIMARY KEY (id),
    CONSTRAINT uq_modality_title UNIQUE (title)
) ENGINE=InnoDB;

CREATE TABLE process (
	id INT NOT NULL AUTO_INCREMENT,
	id_unity INT NOT NULL,
	description VARCHAR(512) NOT NULL,
	nup CHAR(17) DEFAULT '',
	pam VARCHAR(20) NOT NULL,
	creation_date DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
	value DOUBLE NOT NULL DEFAULT 0,
	date DATE NOT NULL,
	info VARCHAR(512),
	responsible_tr VARCHAR(64) NOT NULL,
	id_responsible INT NOT NULL,
	id_type INT NOT NULL,
	id_planning INT NOT NULL,
	id_modality INT NOT NULL,
	CONSTRAINT pk_id PRIMARY KEY (id),
	CONSTRAINT fk_process_unity FOREIGN KEY (id_unity) REFERENCES unity(id),
	CONSTRAINT fk_process_responsible FOREIGN KEY (id_responsible) REFERENCES user(id),
	CONSTRAINT fk_process_type FOREIGN KEY (id_type) REFERENCES type(id),
	CONSTRAINT fk_process_planning FOREIGN KEY (id_planning) REFERENCES planning(id),
	CONSTRAINT fk_process_modality FOREIGN KEY (id_modality) REFERENCES modality(id)
) ENGINE=InnoDB;

CREATE TABLE note (
	id INT NOT NULL AUTO_INCREMENT,
	note VARCHAR(256) NOT NULL,
	creation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	id_process INT NOT NULL,
	id_status INT NOT NULL,
	id_user INT NOT NULL,
	CONSTRAINT pk_id PRIMARY KEY (id),
	CONSTRAINT fk_note_process FOREIGN KEY (id_process) REFERENCES process(id) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT fk_note_status FOREIGN KEY (id_status) REFERENCES status(id),
	CONSTRAINT fk_note_user FOREIGN KEY (id_user) REFERENCES user(id)
) ENGINE=InnoDB;

CREATE TABLE process_status (
	id INT NOT NULL AUTO_INCREMENT,
	date DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
	id_process INT NOT NULL,
	id_status INT NOT NULL,
	CONSTRAINT pk_id PRIMARY KEY (id),
	CONSTRAINT uq_process_status UNIQUE (id_process, id_status),
    CONSTRAINT fk_ps_process FOREIGN KEY (id_process) REFERENCES process(id),
    CONSTRAINT fk_ps_status FOREIGN KEY (id_status) REFERENCES status(id)
) ENGINE=InnoDB;

CREATE TABLE regress (
	id INT NOT NULL AUTO_INCREMENT,
	id_process INT NOT NULL,
	id_status INT NOT NULL,
	id_user INT NOT NULL,
	date DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
	description VARCHAR(512) NOT NULL,
    CONSTRAINT pk_id PRIMARY KEY (id),
    CONSTRAINT fk_return_process FOREIGN KEY (id_process) REFERENCES process(id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_return_status FOREIGN KEY (id_status) REFERENCES status(id),
    CONSTRAINT fk_return_user FOREIGN KEY (id_user) REFERENCES user(id)
) ENGINE=InnoDB;

INSERT INTO level (level, description) VALUES
	(0, 'Usuário sem acesso'),
	(1, 'Leitor'),
	(4, 'Editor'),
	(8, 'Gerente'),
	(100, 'Desenvolvedor');

INSERT INTO user (cpf, id_level) VALUES ('11111111111', 5); -- CPF do administrador

INSERT INTO unity (initials) VALUES ('GAP-LS'), ('PAMA-LS'), ('CIAAR');

INSERT INTO tipo (titulo) VALUES ('Individual'), ('Concentrado');

INSERT INTO planning (title) VALUES ('Planejamento 2019');

INSERT INTO modality (title) VALUES ('Indefinido');

INSERT INTO status (days, description) VALUES
	(0, 'Recebmento (Doc. Iniciais)'), (1, 'PAM (Início DO)'), (16, 'PAM (Envio SCI)'), (26, 'PAG (Envio DO)'),
	(27, 'Edital (Início DO)'), (30, 'C. Edital (Receb. SCI)'), (32, 'C. Edital (Envio DO)'), (33, 'CJU (Envio)'),
	(48, 'CJU (Devolução)'), (50, 'NEA (Envio Setor)'), (52, 'NEA (Resposta Setor)'), (54, 'C. NEA (Envio SCI)'),
	(59, 'C. NEA (Envio DO)'), (60, 'Certame (Abertura)'), (68, 'Certame (Início)'), (88, 'Certame (Adjudicação)'),
	(98, 'Certame (Homologação)');