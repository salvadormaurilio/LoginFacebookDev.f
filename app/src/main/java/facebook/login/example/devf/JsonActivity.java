package facebook.login.example.devf;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);

        requestJson();
    }

    private void requestJson() {

        Session session = Session.getActiveSession();
        Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user,
                                            Response response) {
                        if (user != null) {
                            TextView textViewJson = (TextView) findViewById(R.id.textViewJson);

                            Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
                            String stringJson = prettyGson.toJson(user.getInnerJSONObject());
                            textViewJson.setText(stringJson);
                        } else {
                            Toast.makeText(JsonActivity.this, R.string.message_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).executeAsync();
    }


}
