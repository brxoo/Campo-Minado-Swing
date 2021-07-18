package br.com.cod3r.cm.interfaces;

import br.com.cod3r.cm.enums.CampoEvento;
import br.com.cod3r.cm.modelo.Campo;

@FunctionalInterface
public interface CampoObservador {

	public void eventoOcorreu(Campo campo, CampoEvento evento);
}
