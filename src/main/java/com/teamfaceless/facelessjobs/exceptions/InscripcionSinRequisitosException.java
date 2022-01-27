package com.teamfaceless.facelessjobs.exceptions;

import java.io.Serializable;

public class InscripcionSinRequisitosException extends Exception implements Serializable{


	private static final long serialVersionUID = 1L;

	public InscripcionSinRequisitosException(String mensaje) {
		super(mensaje);
	}
	
}
