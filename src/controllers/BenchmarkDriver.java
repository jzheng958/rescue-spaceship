package controllers;

import java.util.Random;

import models.Edge;
import models.GameState;
import models.Node;
import student.MySpaceship;

public class BenchmarkDriver extends Driver {
	public static final long SEED = 91;
	public static final int N_TRIALS = 40;

	public BenchmarkDriver(long seed) {
		super(seed, new MySpaceship());
	}

	@Override public void beginRescueStage() {}
	@Override public void beginReturnStage() {}
	@Override public void setCumulativeDistance(int d) {}
	@Override public void setNodeAndEdge(Node n, Edge e) {}
	@Override public void setTime(double t) {}
	@Override public void moveShipAlong(Edge e) {}
	@Override public void setHp(int hp) {}
	@Override public void setSpeed(double s) {}
	@Override public void grabSpeedUpgrade(Node n) {}

	public static void main(String[] args) {
		Random r = new Random(SEED);
		double sum = 0;

		for (int i = 0; i < N_TRIALS; i++) {
			long s = r.nextLong();
			Driver.shouldPrint = false;
			BenchmarkDriver b = new BenchmarkDriver(s);
			b.runGame();
			Driver.shouldPrint = true;
			GameState gs = b.getGameState();
			if (gs.getRescueSucceeded() && gs.getReturnSucceeded()) {
				sum += gs.getScore();
			} else {
				Driver.errPrintln("Your Spaceship failed for seed " + s);
				System.exit(1);
			}
		}

		Driver.outPrintln(sum / N_TRIALS + "");
		System.exit(0);
	}
}
