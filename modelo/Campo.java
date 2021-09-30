package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;

import br.com.cod3r.cm.excecao.ExplosaoException;

public class Campo {
	
	private final int linha;
	private final int coluna;
	
	private boolean aberto; 
	private boolean minado;
	private boolean marcado; 

	private List<Campo> vizinhos = new ArrayList<>(); //um campo tem uma lista do tipo de campo, que define sua vizinhança
	
	Campo(int linha, int coluna){
		this.linha = linha;
		this.coluna = coluna;
	}
	
	boolean adicionarVizinho(Campo vizinho) {
		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(coluna - vizinho.coluna);
		int deltaGeral = deltaColuna + deltaLinha;
		
		if(deltaGeral == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		}else if (deltaGeral == 2 && diagonal){
			vizinhos.add(vizinho);
			return true;
		}else {
			return false;
		}
	}
	
	void alternarMarcacao() { //Alterna campo entre marcado/não marcado, em relação a ser um campo minado
		//Se o campo estiver marcado, não pode ser aberto
		if (!aberto) { // O campo precisa estar fechado
			marcado = !marcado; //se está marcado, desmarca...e vice-versa
		}
	}
	
	boolean abrir() {
		if (!aberto && !marcado) { //Para ser aberto, o campo precisa estar fechado e nao marcado
			aberto = true;
			
			if(minado) { //Se o campo for aberto e estiver minado, perde-se o jogo
				throw new ExplosaoException();
			}
			
			if(vizinhancaSegura()) {
				vizinhos.forEach(v->v.abrir());
			}
			return true;
		}else {
			return false;
		}
		}
		
	
	boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v->v.minado); //Se essa expressão der verdadeira, nenhum vizinho 
		//está minado
	}
	
	void minar() {
			minado = true;	
	}
	
	public boolean isMinado() {
		return minado;
	}
	
	public boolean isMarcado() {
		return marcado;
	}
	
	
	
	public boolean isAberto() {
		return aberto;
	}

	void setAberto(boolean aberto) {
		this.aberto = aberto;
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}
	
	boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		return desvendado || protegido;
	}
	
	long minasNaVizinhanca() {
		return vizinhos.stream().filter(v->v.minado).count();
	}
	
	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
	}
	
	public String toString() {
		if(marcado) {
			return "x";
		}else if(aberto && minado) {
			return "*";
		}else if(aberto && minasNaVizinhanca()>0) {
			return Long.toString(minasNaVizinhanca());
		}else if(aberto) {
			return " ";
		}else {
			return "?";
		}
	}
}
