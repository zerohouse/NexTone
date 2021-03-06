package components;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mylikenews.nextoneandroid.R;

public class ViewBinder extends TextView {
	int Int;
	RelativeLayout.LayoutParams params;
	Context context;
	ViewGroup parent;


	public ViewBinder(Context context, int val, ViewGroup parent) {
		super(context);
		this.context = context;
		this.parent = parent;
		setTextAppearance(context, R.style.myText);
		this.Int = val;
		initTextView(val + "");

	}

	public ViewBinder(Context context, int val, ViewGroup parent, boolean set) {
		super(context);
		this.context = context;
		this.parent = parent;
		this.Int = val;
		if (set==true){
			setTextAppearance(context, R.style.myText);
			
		}
		initTextView("");
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
