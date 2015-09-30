package bikeblocker.bikeblocker.Control;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.User;
import bikeblocker.bikeblocker.R;

public class ViewUserActivity extends Activity {
    private String user_name; // selected user's username
    private TextView nameTextView;
    private TextView usernameTextView;
    private TextView creditsTextView;
    UserDAO userdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        creditsTextView = (TextView) findViewById(R.id.creditsTextView);

        Bundle extras = getIntent().getExtras();
        user_name = extras.getString(ListUsersActivity.USER_NAME);

        ImageButton deleteUserButton = (ImageButton) findViewById(R.id.deleteUserButton);
        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser();
            }
        });
    }

    //executes each time the Activity regains the focus (including the first time) after being partially or fully hidden
    @Override
    protected void onResume(){
        super.onResume();
        userdao = UserDAO.getInstance(ViewUserActivity.this);

        User user = userdao.selectUser(user_name);

        // fill TextViews with the retrieved data
        nameTextView.setText(user.getName());
        usernameTextView.setText(user.getUsername());
        creditsTextView.setText(Integer.toString(user.getCredits()));
    }

    // delete a contact
    private void deleteUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewUserActivity.this);

        builder.setTitle(R.string.confirmTitle);
        builder.setMessage(R.string.confirmMessage);

        // provide an OK button that deletes asynchronously while dismissing the dialog
        builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int button) {
                userdao = UserDAO.getInstance(ViewUserActivity.this);
                String username = usernameTextView.getText().toString();
                userdao.deleteUser(userdao.selectUser(username));
                Intent intent = new Intent(ViewUserActivity.this, ListUsersActivity.class);
                startActivity(intent);
            }
        } // end anonymous inner class that defines an instantiates the listener for positive button clicks

        ); // end listener ref declaration

        builder.setNegativeButton(R.string.button_cancel, null); //just dismiss dialog box (no delete)

        builder.show();
    } // end method deleteContact

}