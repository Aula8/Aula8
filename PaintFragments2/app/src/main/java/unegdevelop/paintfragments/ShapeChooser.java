package unegdevelop.paintfragments;

/**
 * Created by Alex on 7/7/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ShapeChooser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shapechooser);

        this.setTitle("Elija una forma");
    }

    public void shapeClicked(View view){

        Intent returnIntent = new Intent();
        returnIntent.putExtra("shape",(String)view.getTag());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }
}
