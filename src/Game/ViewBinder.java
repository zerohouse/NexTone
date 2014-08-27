package game;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mylikenews.nextoneandroid.R;

public class ViewBinder extends TextView {
	String string;
	int Int;
	RelativeLayout.LayoutParams params;
	Context context;
	ViewGroup parent;

	ViewBinder(Context context, String value, ViewGroup parent) {
		super(context);
		this.context = context;
		this.parent = parent;
		this.string = value;
		initTextView(value);
	}

	public ViewBinder(Context context, int val, ViewGroup parent) {
		super(context);
		this.context = context;
		this.parent = parent;
		setTextAppearance(context, R.style.myText);
		this.Int = val;
		initTextView(val + "");

	}

	public void initTextView(String value) {
		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		setLayoutParams(params);
		setText(value);

		parent.addView(this);
	}

	public void add(int amount) {
		Int += amount;
		setText(Int + "");
	}

	public int Int() {
		return Int;
	}

	public void setInt(int value) {
		Int = value;
		setText(Int + "");
	}

	public RelativeLayout.LayoutParams getParams() {
		return params;
	}

	public void setParams(RelativeLayout.LayoutParams params) {
		setLayoutParams(params);
	}
}
