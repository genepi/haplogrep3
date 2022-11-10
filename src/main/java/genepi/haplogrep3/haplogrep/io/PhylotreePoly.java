package genepi.haplogrep3.haplogrep.io;

public class PhylotreePoly implements Comparable<PhylotreePoly> {

	private String name;

	private double amount;

	public PhylotreePoly() {

	}

	public PhylotreePoly(String name, double amount) {
		super();
		this.name = name;
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public int compareTo(PhylotreePoly o) {
		if (this.getAmount() > o.getAmount()) {
			return -1;
		}
		if (this.getAmount() < o.getAmount()) {
			return 1;
		} else {
			return 0;
		}
	}

}
