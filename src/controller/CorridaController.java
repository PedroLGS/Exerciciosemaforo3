package controller;

import java.util.concurrent.Semaphore;
import view.Principal;

public class CorridaController extends Thread {

	private int ThreadId;
	private Semaphore semaforoLargada;
	private Semaphore semaforoEscuderia;
	public static int carrosForaDaPista = 0;

	public CorridaController(int id, Semaphore semaforoLargada, Semaphore semaforoEscuderia) {
		this.ThreadId = id;
		this.semaforoLargada = semaforoLargada;
		this.semaforoEscuderia = semaforoEscuderia;
	}

	@Override
	public void run() {

		for (int i = 1; i < 3; i++) {
			try {
				semaforoLargada.acquire();
				CarroAndando(i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				semaforoLargada.release();
				System.out.println("O carro " + i + " da escuderia " + ThreadId + " saiu da pista");
				carrosForaDaPista++;
			}
		}
		if (carrosForaDaPista == 14) {
			Corrida();
		}
	}

	private void CarroAndando(int carro) {

		System.out.println("O carro " + carro + " da escuderia " + ThreadId + " entrou na pista");
		for (int i = 1; i < 4; i++) {
			int tempoVolta = (int) ((Math.random() * 180) + 60);
			try {
				sleep(tempoVolta * 30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("Escuderia: " + ThreadId + " Carro: " + carro + " Volta: " + i + " Tempo: "
					+ tempoVolta + " segundos");
			try {
				semaforoEscuderia.acquire();
				if (tempoVolta < Principal.valorVoltas[(2 * ThreadId) - carro]
						|| Principal.valorVoltas[(2 * ThreadId) - carro] == 0) {
					Principal.valorVoltas[(2 * ThreadId - 2 + carro) - 1] = tempoVolta;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				semaforoEscuderia.release();
			}

		}
	}

	public void Corrida() {
		int aux;
		String auxiliar;
		for (int i = 0; i < 13; i++) {
			for (int j = i + 1; j < 14; j++) {
				if (Principal.valorVoltas[i] > Principal.valorVoltas[j]) {
					aux = Principal.valorVoltas[i];
					Principal.valorVoltas[i] = Principal.valorVoltas[j];
					Principal.valorVoltas[j] = aux;
					auxiliar = Principal.textoVoltas[i];
					Principal.textoVoltas[i] = Principal.textoVoltas[j];
					Principal.textoVoltas[j] = auxiliar;
				}
			}
		}
		for (int i = 0; i < 14; i++) {
			System.out.println(
					"Posição " + (i + 1) + ": " + Principal.textoVoltas[i] + Principal.valorVoltas[i] + " segundos");
		}
	}

}