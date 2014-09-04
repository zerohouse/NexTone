package dek;

import java.io.Serializable;

public class Data implements Serializable {

	private static final long serialVersionUID = 5149399577212275632L;

	int id, sum;
	String herostring, dekstring, summary;

	public Data(int id, String herostring, String dekstring) {
		this.id = id;
		this.herostring = herostring;
		this.dekstring = dekstring;
		this.summary = herostring + "\n카드를 선택하지 않았습니다.";
		sum = 0;
	}

	public Data(int id, int sum, String summary, String herostring,
			String dekstring) {
		this.id = id;
		this.sum = sum;
		this.herostring = herostring;
		this.dekstring = dekstring;
		this.summary = summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSummary() {
		return summary;
	}

	public int getId() {
		return id;
	}

	public String getHerostring() {
		return herostring;
	}

	public void setHerostring(String herostring) {
		this.herostring = herostring;
	}

	public String getDekstring() {
		return dekstring;
	}

	public void setDekstring(String dekstring) {
		this.dekstring = dekstring;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}
}