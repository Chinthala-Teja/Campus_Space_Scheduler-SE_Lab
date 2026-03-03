package com.example.campus_space_scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class ClassroomListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_list);

        // This is a simplified example. In a real app, you'd use a RecyclerView.
        int[] cardIds = {R.id.card_elhc_201, R.id.card_elhc_302, R.id.card_elhc_401, R.id.card_eclc_b, R.id.card_eclc_j, R.id.card_nlhc_101};
        int[] textViewIds = {R.id.text_elhc_201, R.id.text_elhc_302, R.id.text_elhc_401, R.id.text_eclc_b, R.id.text_eclc_j, R.id.text_nlhc_101};

        for (int i = 0; i < cardIds.length; i++) {
            MaterialCardView card = findViewById(cardIds[i]);
            TextView textView = findViewById(textViewIds[i]);
            String roomName = textView.getText().toString();

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ClassroomListActivity.this, StudentRoomDashboardActivity.class);
                    intent.putExtra("SPACE_NAME", roomName);
                    intent.putExtra("ROLE", "student");
                    intent.putExtra("SPACE_TYPE", "Classroom");
                    startActivity(intent);
                }
            });
        }
    }
}
