package dek;

import game.Method;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mylikenews.nextoneandroid.R;

public class EachDek extends RelativeLayout {

	TextView title, description, remove, rename;
	ImageView background;
	Sql sql;
	Data data;
	Context context;

	@SuppressLint("NewApi")
	public EachDek(Context context, Data data, boolean needRemove) {
		super(context);
		this.data = data;
		this.context = context;
		sql = new Sql(context);

		background = new ImageView(context);
		RelativeLayout.LayoutParams backparam = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		background.setLayoutParams(backparam);
		background.setImageResource(Method.resId(data.getResource()));
		background.setAlpha((float) 0.4);
		addView(background);

		String[] tmp = data.getSummary().split("\n");
		title = new TextView(context);
		title.setGravity(Gravity.CENTER);
		title.setText(tmp[0]);
		title.setTextAppearance(context, R.style.myText);
		LayoutParams titleparams = Method.getParams();
		title.setLayoutParams(titleparams);
		titleparams.addRule(CENTER_HORIZONTAL);
		titleparams.topMargin = Method.dpToPx(50);

		addView(title);

		description = new TextView(context);
		description.setGravity(Gravity.CENTER);
		description.setText(tmp[1]);
		LayoutParams desparams = Method.getParams();
		description.setLayoutParams(desparams);

		desparams.addRule(CENTER_HORIZONTAL);
		desparams.topMargin = Method.dpToPx(85);

		addView(description);
		if (needRemove) {
			remove = new TextView(context);
			LayoutParams removeparams = Method.getParams();
			remove.setLayoutParams(removeparams);
			removeparams.addRule(ALIGN_PARENT_BOTTOM);
			removeparams.addRule(ALIGN_PARENT_RIGHT);
			remove.setText("삭제하기   ");
			remove.setId(125);
			addView(remove);

			rename = new TextView(context);
			LayoutParams renameparams = Method.getParams();
			rename.setLayoutParams(renameparams);
			renameparams.addRule(ALIGN_PARENT_BOTTOM);
			renameparams.addRule(LEFT_OF, remove.getId());
			rename.setText("이름바꾸기   ");
			addView(rename);
			rename.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					showRenameDialog();
				}
			});

		}

	}

	public void showRenameDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("덱이름을 정해주세요.");

		// Set up the input
		final EditText input = new EditText(context);
		// Specify the type of input expected; this, for example, sets the input
		// as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		input.setPrivateImeOptions("defaultInputmode=korean; ");
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("이걸로 하겠어!",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String name= input.getText().toString();
						data.setName(name);
						sql.update(data);
						title.setText(String.format("%s (%d/30)", name, data.getSum()));
					}
				});
		builder.setNegativeButton("나중에 다시!",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		
		builder.show();
	}

	public void setRemoveListener(OnClickListener del) {
		remove.setOnClickListener(del);
	}

	public Data getData() {
		return data;
	}

	public void setTitleColor(int color) {
		title.setTextColor(color);
	}

	public int getHeroType() {
		return data.getHeroType();
	}

}
