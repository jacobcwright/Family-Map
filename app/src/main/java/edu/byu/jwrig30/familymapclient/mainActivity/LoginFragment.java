package edu.byu.jwrig30.familymapclient.mainActivity;

import static android.widget.Toast.makeText;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.jwrig30.familymapclient.R;
import edu.byu.jwrig30.familymapclient.tasks.LoginTask;
import edu.byu.jwrig30.familymapclient.tasks.RegisterTask;
import request.LoginRequest;
import request.RegisterRequest;


public class LoginFragment extends Fragment {

   private Listener listener;
    EditText host;
    EditText port;
    EditText username;
    EditText password ;
    EditText firstName;
    EditText lastName;
    EditText email;
    RadioButton male;
    RadioButton female;
    Button loginButton;
    Button registerButton ;
    public interface Listener{
        void notifyDone();
    }

    public void registerListener(Listener listener) {
        this.listener = listener;
    }

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Watcher watcher = new Watcher();

        // Set text fields & buttons & watchers
        host = view.findViewById(R.id.Host);
        host.addTextChangedListener(watcher);
        port = view.findViewById(R.id.Port);
        port.addTextChangedListener(watcher);
        username = view.findViewById(R.id.Username);
        username.addTextChangedListener(watcher);
        password = view.findViewById(R.id.Password);
        password.addTextChangedListener(watcher);
        firstName = view.findViewById(R.id.FirstName);
        firstName.addTextChangedListener(watcher);
        lastName = view.findViewById(R.id.LastName);
        lastName.addTextChangedListener(watcher);
        email = view.findViewById(R.id.Email);
        email.addTextChangedListener(watcher);
        male = view.findViewById(R.id.Male);
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Watcher().checkInputs();
            }
        });
        female = view.findViewById(R.id.Female);
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Watcher().checkInputs();
            }
        });
        loginButton = view.findViewById(R.id.LoginButton);
        loginButton.setEnabled(false);
        registerButton = view.findViewById(R.id.RegisterButton);
        registerButton.setEnabled(false);

        // create loginButton action
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()){
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        if(bundle.getBoolean("LoginResult")){
                            Context context = getContext();
                            CharSequence text = "Welcome " + bundle.getString("FirstName") + " " + bundle.getString("LastName");
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(context, text, duration).show();
                            if(listener != null) {
                                listener.notifyDone();
                            }
                        }
                        else{
                            Context context = getContext();
                            CharSequence text = "Invalid login";
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(context, text, duration).show();
                        }
                    }
                };
                LoginRequest request = new LoginRequest(username.getText().toString(), password.getText().toString());
                LoginTask task = new LoginTask(uiThreadMessageHandler, request, host.getText().toString(), port.getText().toString());
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);
            }
        });

        // create register button action
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()){
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        if(bundle.getBoolean("RegisterResult")){
                            Context context = getContext();
                            CharSequence text = "Welcome " + bundle.getString("FirstName") + " " + bundle.getString("LastName");
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(context, text, duration).show();
                            if(listener != null) {
                                listener.notifyDone();
                            }
                        }
                        else{
                            Context context = getContext();
                            CharSequence text = "Invalid register";
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(context, text, duration).show();
                        }
                    }
                };
                RegisterRequest request = new RegisterRequest(username.getText().toString(), password.getText().toString(),
                email.getText().toString(), firstName.getText().toString(), lastName.getText().toString(), (male.isChecked() ? "m" : "f"));
                RegisterTask task = new RegisterTask(uiThreadMessageHandler,request, host.getText().toString(), port.getText().toString());
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);
            }
        });

        return view;
    }


    private class Watcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkInputs();
        }

        @Override
        public void afterTextChanged(Editable s) {}

        public void checkInputs(){
            // enable login button?
            if(!TextUtils.isEmpty(host.getText()) &&
                    !TextUtils.isEmpty(port.getText()) &&
                    !TextUtils.isEmpty(username.getText()) &&
                    !TextUtils.isEmpty(password.getText())
            ){
                loginButton.setEnabled(true);
            }
            else{
                loginButton.setEnabled(false);
            }
            // enable register button?
            if(!TextUtils.isEmpty(host.getText()) &&
                    !TextUtils.isEmpty(port.getText()) &&
                    !TextUtils.isEmpty(username.getText()) &&
                    !TextUtils.isEmpty(password.getText()) &&
                    !TextUtils.isEmpty(firstName.getText()) &&
                    !TextUtils.isEmpty(lastName.getText()) &&
                    !TextUtils.isEmpty(email.getText()) &&
                    (male.isChecked() || female.isChecked())
            ){
                registerButton.setEnabled(true);
            }
            else{
                registerButton.setEnabled(false);
            }
        }
    }
}