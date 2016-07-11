package unegdevelop.paintfragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by Alex on 8/7/2016.
 */
public class TextBoxEdit extends AppCompatActivity{
    EditText textEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_textbox);

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
