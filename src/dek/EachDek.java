package dek;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mylikenews.nextoneandroid.R;

public class EachDek extends LinearLayout {

	TextView title, description, remove;
	Sql sql;
	Data data;

	public EachDek(Context context, Data data, boolean needRemove) {
		super(context);
		this.data = data;
		setOrientation(VERTICAL);
		String[] tmp = data.getSummary().split("\n");
		title = new TextView(context);
		title.setGravity(Gravity.CENTER);
		title.setText(tmp[0]);
		title.setTextAppearance(context, R.style.myText);
		addView(title);

		description = new TextView(context);
		description.setGravity(Gravity.CENTER);
		description.setText(tmp[1]);
		addView(description);
		if (needRemove) {
			remove = new TextView(context);
			remove.setGravity(Gravity.RIGHT);
			remove.setText("덱 삭제하기   ");
			addView(remove);
		}

	}

	public void setRemoveListener(OnClickListener del) {
		remove.setOnClickListener(del);
	}

	public Data getData() {
		return data;
	}

}
