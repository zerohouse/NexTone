package game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Method {

	public static Context context;
	static Toast toast = null;
	private static int windowWidth;
	private static int windowHeight;

	public static RelativeLayout.LayoutParams getParams() {
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		return param;
	}

	public static void alert(String message) {
		if (toast != null) {
			toast.cancel();
		}
		toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void alert(int message) {
		toast = Toast.makeText(context, message + "", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static int dpToPx(int dp) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				r.getDisplayMetrics());
		return (int) px;
	}

	public static int resId(String filename) {
		int resId = context.getResources().getIdentifier(filename, "drawable",
				context.getPackageName());
		return resId;
	}

	@SuppressLint("NewApi")
	public static void setContext(Context con) {
		context = con;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		windowWidth = size.x;
		windowHeight = size.y;
	}

	public static int getWindowHeight() {
		return windowHeight;
	}
	
	public static int getWindowWidth() {
		return windowWidth;
	}

}
