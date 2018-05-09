package au.com.alzed.hashpass;


import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import android.widget.CheckBox;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * A login screen that offers login via email/password.
 */
public class Hashpass_main extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private EditText mMasterPassword;
    private EditText mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMasterPassword = (EditText) findViewById(R.id.masterPassword);
        mService = (EditText) findViewById(R.id.service);

        Button mGenerateButton = (Button) findViewById(R.id.generateButton);
        mGenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView genPass = (TextView) findViewById(R.id.generatedPassText);
                genPass.setVisibility(View.VISIBLE);

                String tokenPass = mMasterPassword.getText().toString() + "-" +  mService.getText().toString();
                String stringHash = new String(bytesToHex(getHash(tokenPass)).toLowerCase());
                mService.setText(bin2hex(getsHash(tokenPass)).toLowerCase());

                StringBuffer buffer = new StringBuffer(stringHash);
                buffer.replace(0, 1, "z");
                buffer.replace(3, 4, "H");
                buffer.replace(10, 11, String.valueOf(7));
                buffer.replace(15, 16, "!");

                stringHash = buffer.toString();

                genPass.setText(stringHash.substring(0, 16));

                CheckBox copyCheck = (CheckBox)findViewById(R.id.CopyCheck);
                if ( copyCheck.isChecked()) {

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("hashPassword", stringHash.substring(0, 16));
                    clipboard.setPrimaryClip(clip);
                }
            }
        });

    }

    public byte[] getHash(String password) {
        MessageDigest digest=null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //digest.reset();

        byte[] hashed = password.getBytes();

        for (int i = 0; i < 1; i++) {
            hashed = digest.digest(hashed);
            //hashed = bin2hex(hashed).getBytes();
            digest.reset();
        }

        return hashed;
    }

    public byte[] getsHash(String password) {
        MessageDigest digest=null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //digest.reset();

        byte[] hashed = password.getBytes();

        for (int i = 0; i < 2; i++) {
            hashed = digest.digest(hashed);
            //hashed = bin2hex(hashed).getBytes();
            digest.reset();
        }

        return hashed;
    }

    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
    }

    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }


}
