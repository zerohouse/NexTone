package dek;

import java.io.Serializable;

public class Data implements Serializable {

	private static final long serialVersionUID = 5149399577212275632L;

	int id, sum;
	String herostring, dekstring, summary, name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Data(int id, String herostring, String dekstring) {
		this.id = id;
		this.herostring = herostring;
		this.dekstring = dekstring;
		this.summary = herostring + "\n카드를 선택하지 않았습니다.";
		this.name = "나만의 덱";
		sum = 0;
	}

	public Data(int id, int sum, String summary, String herostring,
			String dekstring, String name) {
		this.id = id;
		this.sum = sum;
		this.name = name;
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

	public int getHeroType() {
		String[] heroinfo = herostring.split(",");
		return Integer.parseInt(heroinfo[1]);
	}
	
	public String getResource() {
		String[] heroinfo = herostring.split(",");
		return heroinfo[0];
	}

	public void setId(int id) {
		this.id = id;
	}

}