package components;

import game.Method;

import com.mylikenews.nextoneandroid.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ClickableViewAccessibility")
public class ImageButton extends TextView {

	
	int background, down, cancel;
	RelativeLayout.LayoutParams params;
	
	public ImageButton(Context context, int back, int back2, String text) {
		super(context);

		setText(text);
		this.background = back;
		setBackgroundResource(background);
		setGravity(Gravity.CENTER);
		setTextAppearance(context, R.style.myBtnText);
	
		this.down = back2;
		
		params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		
		setLayoutParams(params);
		
		params.width = Method.dpToPx(100);
		params.height = Method.dpToPx(35);
		setOnTouchListener(new View.OnTouchListener() {
            
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		                 
		        switch (event.getAction()) {
		                 
		        case MotionEvent.ACTION_DOWN:
		            setBackgroundResource(down);         
		            break;
		            
		        case MotionEvent.ACTION_CANCEL:
		        	setBackgroundResource(background);   
		        	break;
		                     
		        case MotionEvent.ACTION_MOVE:
		        	setBackgroundResource(down);
		            break;
		                                     
		        case MotionEvent.ACTION_UP:
		        	setBackgroundResource(background);   
		            break;
		 
		        default:
		            break;
		        }
		                 
		        return false;
		    }
		});
	}

	public RelativeLayout.LayoutParams getParams() {
		return params;
		
	}

}
