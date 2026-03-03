package com.example.campus_space_scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class LabListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_list);

        int[] cardIds = {R.id.card_nsl, R.id.card_ssl, R.id.card_bdl};
        int[] textViewIds = {R.id.text_nsl, R.id.text_ssl, R.id.text_bdl};

        boolean isFaculty = getIntent().hasExtra("IS_FACULTY");

        for (int i = 0; i < cardIds.length; i++) {
            MaterialCardView card = findViewById(cardIds[i]);
            TextView textView = findViewById(textViewIds[i]);
            String roomName = textView.getText().toString();

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if (isFaculty) {
                        intent = new Intent(LabListActivity.this, FacultyRoomDashboardActivity.class);
                        intent.putExtra("ROLE", "faculty");
                    } else {
                        intent = new Intent(LabListActivity.this, StudentRoomDashboardActivity.class);
                        intent.putExtra("ROLE", "student");
                    }
                    intent.putExtra("SPACE_NAME", roomName);
                    intent.putExtra("SPACE_TYPE", "Lab");
                    startActivity(intent);
                }
            });
        }
    }
}
