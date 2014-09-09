package components;

import com.mylikenews.nextoneandroid.R;

import game.Method;
import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IconBinder extends RelativeLayout {
	TextView title, description;
	ImageView image;
	Context context;
	String resource;

	public IconBinder(Context context) {
		super(context);
		this.context = context;
		image = new ImageView(context);
		RelativeLayout.LayoutParams imgparams = Method.getParams();
		addView(image);
		image.setLayoutParams(imgparams);
		imgparams.addRule(RelativeLayout.CENTER_IN_PARENT);

		title = new TextView(context);
		RelativeLayout.LayoutParams textparams = Method.getParams();
		title.setLayoutParams(textparams);
		textparams.addRule(RelativeLayout.CENTER_IN_PARENT);
		title.setBackgroundColor(Color.argb(100, 0, 0, 0));
		addView(title);

	}

	public IconBinder(Context context, String string) {
		super(context);
		this.context = context;
		image = new ImageView(context);
		RelativeLayout.LayoutParams imgparams = Method.getParams();
		addView(image);
		image.setLayoutParams(imgparams);
		imgparams.addRule(RelativeLayout.CENTER_IN_PARENT);

		title = new TextView(context);
		RelativeLayout.LayoutParams textparams = Method.getParams();
		title.setLayoutParams(textparams);
		textparams.addRule(RelativeLayout.CENTER_IN_PARENT);
		title.setBackgroundColor(Color.argb(100, 0, 0, 0));
		addView(title);
		setText(string);
		setIconResource(Method.resId(string));
		this.resource = string;
	}

	public void setText(String text) {
		// TODO Auto-generated method stub
		this.title.setText(text);
	}

	public void setIconResource(int resId) {
		image.setBackgroundResource(resId);
	}

	public void setTextAppearance(int resId) {
		title.setTextAppearance(context, resId);

	}

	public String resource() {
		return resource;
	}

	public void addDescription(String string) {
		description = new TextView(context);
		description.setText(string);
		RelativeLayout.LayoutParams desparams = Method.getParams();
		desparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		desparams.topMargin = Method.dpToPx(105);
		description.setTextAppearance(context, R.style.myBtnText);
		description.setBackgroundColor(Color.argb(150, 255, 255, 255));
		description.setLayoutParams(desparams);
		
		addView(description);
	}

}
