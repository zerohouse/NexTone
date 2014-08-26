package Game;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewBinder extends TextView  {
	String string;
	int Int;
	RelativeLayout.LayoutParams params;
	Context context;
	ViewGroup parent;
 
	ViewBinder(Context context, String value, ViewGroup parent) {
		super(context);
		this.context = context;
		this.parent = parent;
		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		this.string = value;
		initTextView(params, value);
	}

	ViewBinder(Context context, int val, ViewGroup parent) {
		super(context);
		this.context = context;
		this.parent = parent;
		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		this.Int = val;
		initTextView(params, val + "");
		
	}
	
	ViewBinder(Context context, int val, ViewGroup parent, RelativeLayout.LayoutParams params) {
		super(context);
		this.context = context;
		this.Int = val;
		this.parent = parent;
		this.params = params;
		initTextView(params, val + "");
	}

	public void initTextView(RelativeLayout.LayoutParams params, String value) {
		setLayoutParams(params);
		setText(value);
		setTextSize(20);
		parent.addView(this);
	} 

	public void add(int amount) {
		Int += amount;
		setText(Int+"");
	}

	public int Int() {
		return Int;
	}

	public void setInt(int value) {
		Int = value;
		setText(Int+"");
	}
	
	public RelativeLayout.LayoutParams getParams(){
		return params;
	}
	
	public void setParams(RelativeLayout.LayoutParams params){
		setLayoutParams(params);
	}
}
