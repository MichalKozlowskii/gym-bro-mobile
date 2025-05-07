package com.example.gym_bro_mobile.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gym_bro_mobile.databinding.FragmentAuthBinding;
import com.example.gym_bro_mobile.viewmodel.AuthViewModel;

public class AuthFragment extends Fragment {

    private FragmentAuthBinding binding;
    private AuthViewModel authViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAuthBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            Log.d("LoginActivity", "Login button clicked");
            authViewModel.login(username, password, requireContext());
        });

        /*binding.btnRegister.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            authViewModel.register(username, password, requireContext());
        });

        binding.btnGitHub.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "GitHub login not implemented yet", Toast.LENGTH_SHORT).show();
        });*/

        authViewModel.getResultMessage().observe(getViewLifecycleOwner(), message -> {
            binding.tvResult.setVisibility(View.VISIBLE);
            binding.tvResult.setText(message);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

