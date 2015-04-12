package com.eugene.restapi_fatsecret;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity implements FatSecretSearchFragment.FragmentCallbacks {
    Fragment fatSecretSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fatSecretSearch = new FatSecretSearchFragment();
        if (fatSecretSearch != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new FatSecretSearchFragment()).commit();
    }

    @Override
    public void fromFragment() {

    }
}
