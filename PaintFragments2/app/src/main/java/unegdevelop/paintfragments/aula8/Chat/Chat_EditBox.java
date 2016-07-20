package unegdevelop.paintfragments.aula8.Chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import unegdevelop.paintfragments.R;

/**
 * Created by wuilkysb on 17/07/16.
 */
public class Chat_EditBox extends AppCompatActivity{
        EditText textEdit;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.chat_textbox);

            textEdit = (EditText) findViewById(R.id.editBox);

            this.setTitle("Ingresa Texto");
        }

        public void boxClicked(View view)
        {
            if (!textEdit.getText().toString().isEmpty()){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("text",textEdit.getText().toString());
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }

        }
}
