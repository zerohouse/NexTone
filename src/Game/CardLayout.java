package Game;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class CardLayout extends RelativeLayout {

	Context context;
	RelativeLayout.LayoutParams params;

	public CardLayout(Context context) {
		super(context);
		this.context = context;
		params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.width = Method.dpToPx(70);
		params.height = Method.dpToPx(100);
		setLayoutParams(params);
	}

}
