package com.rlite.tweet;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.twitter.sdk.android.tweetcomposer.TweetComposer;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendTweetFragment extends Fragment {

    EditText msg;
    Button send;

    public SendTweetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_tweet, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        msg = (EditText) getActivity().findViewById(R.id.editText_message);
        send = (Button) getActivity().findViewById(R.id.btn_send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = msg.getText().toString();
                if (validate(message)) {
                    TweetComposer.Builder builder = new TweetComposer.Builder(getActivity())
                            .text(message);
                    builder.show();
                }
            }
        });

    }

    public boolean validate(String message)
    {

        if (message.length() == 0 || message.equals(" ") || message.isEmpty())
        {
            msg.setError("Message cannot be empty");
            return false;
        }

        return true;
    }
}
