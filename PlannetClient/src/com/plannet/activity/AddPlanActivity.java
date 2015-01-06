package com.plannet.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.plannet.clientdb.PlanDAO;
import com.plannet.http.HttpRequest;
import com.plannet.others.GlobalVariables;
import com.plannet.others.Utilities;

public class AddPlanActivity extends Activity implements OnClickListener {
	private EditText titleEdit;
	private EditText summaryEdit;
	private Button button;

	private String[] response;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_plan);

		titleEdit = (EditText) findViewById(R.id.add_plan_title_edit);
		summaryEdit = (EditText) findViewById(R.id.add_plan_summary_edit);
		button = (Button) findViewById(R.id.add_plan_ok_button);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		final int cid = GlobalVariables.getCurrentPageCid();
		final String title = titleEdit.getText().toString();
		final String summary = summaryEdit.getText().toString();

		Log.e("add plan : ", cid + "   " + title + "   " + summary);

		if (title.isEmpty()) {
			Utilities.toastPopUp(this, "제목을 입력해주세요!");
			return;
		}

		int oldPid = (int) new PlanDAO(this).insert(cid, title, summary); // 반환값이 rowId인데 이게 바로 pid이다

		// Thread thread = new Thread() {
		// public void run() {
		// response = HttpRequest.PushPlan(cid, title, summary);
		// }
		// };
		// thread.start();
		//
		// try {
		// thread.join(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		//
		// // String result = response[0]; // 응답값에 따라 처리해주기
		// int newPid = Integer.parseInt(response[1]);
		// new PlanDAO(this).update(oldPid, newPid);

		Utilities.moveToAnotherActivity(this, MyPlanActivity.class);
	}
}