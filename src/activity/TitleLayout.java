package activity;

import com.example.coolweather.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TitleLayout extends LinearLayout {
	public TitleLayout(Context context,AttributeSet attrs){
		super(context,attrs);
		LayoutInflater.from(context).inflate(R.layout.title, this);
		Button titleBack=(Button)findViewById(R.id.change_city);
		titleBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Activity)getContext()).finish();
			}
		});
	}
}
