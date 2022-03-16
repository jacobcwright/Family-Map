package edu.byu.jwrig30.familymapclient.mainActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.jwrig30.familymapclient.R;
import edu.byu.jwrig30.familymapclient.tasks.LoginTask;


public class LoginFragment extends Fragment {

   private Listener listener;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button doneButton = view.findViewById(R.id.LoginButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()){
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();

                    }

                };
                LoginTask task = new LoginTask(uiThreadMessageHandler);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);

                if(listener != null) {
                    listener.notifyDone();
                }
            }
        });

        return view;
    }
}