package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;

import br.com.cod3r.cm.enums.CampoEvento;
import br.com.cod3r.cm.interfaces.CampoObservador;

public class Campo {

	private final int linha;
	private final int coluna;
	private boolean campoAberto = false;
	private boolean campoMinado = false;
	private boolean campoMarcado = false;
	
	private List<Campo> vizinhos = new ArrayList<Campo>();
	private List<CampoObservador> observadores = new ArrayList<>();
	
	Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}
	
	public void registrarObservador(CampoObservador observador) {
		observadores.add(observador);
	}
	
	private void notificarObservadores(CampoEvento evento) {
		observadores.stream().forEach(o -> o.eventoOcorreu(this, evento));
	}
	
	boolean adicionarVizinho(Campo vizinho) {
		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(coluna - vizinho.coluna);
		int deltaGeral = deltaLinha + deltaColuna;
		
		if (deltaGeral == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else if (deltaGeral == 2 && diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else {
			return false;
		}

	}
	
	public void alternarMarcacao() {
		if (!campoAberto) {
			campoMarcado = !campoMarcado;
			
			if (campoMarcado) {
				notificarObservadores(CampoEvento.MARCAR);
			} else {
				notificarObservadores(CampoEvento.DESMARCAR);
			}
		}
	}
	
	public boolean abrir() {
		if (!campoAberto && !campoMarcado) {
			if (campoMinado) {
				notificarObservadores(CampoEvento.EXPLODIR);
				return true;
			}
			
			setCampoAberto(true);

			if (VizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir());
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	public boolean VizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.campoMinado);
	}
	
	void minar() {
		campoMinado = true;
	}
	
	public boolean isMinado() {
		return campoMinado;
	}
	
	public boolean isMarcado() {
		return campoMarcado;
	}
	
	
	public void setCampoAberto(boolean campoAberto) {
		this.campoAberto = campoAberto;
		
		if (campoAberto) {
			notificarObservadores(CampoEvento.ABRIR);
		}
	}

	public boolean isAberto() {
		return campoAberto;
	}
	
	public boolean isFechado() {
		return !isAberto();
	}
	
	public int getLinha() {
		return linha;
	}
	
	public int getColuna() {
		return coluna;
	}
	
	boolean objetivoAlcancado() {
		boolean campoDesvendado = !campoMinado && campoAberto;
		boolean campoProtegido = campoMinado && campoMarcado;
		return campoDesvendado || campoProtegido;
	}
	
	public int minasNaVizinhanca() {
		return (int) vizinhos.stream().filter(v -> v.campoMinado).count();
	}
	
	void reiniciarJogo() {
		campoAberto = false;
		campoMinado = false;
		campoMarcado = false;
		notificarObservadores(CampoEvento.REINICIAR);
	}
	
}
