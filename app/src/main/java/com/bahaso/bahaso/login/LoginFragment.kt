package com.bahaso.bahaso.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.motion.utils.Oscillator.TAG
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bahaso.bahaso.BaseFragment
import com.bahaso.bahaso.R
import com.bahaso.bahaso.databinding.FragmentLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : BaseFragment() {
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {

            btnLogin.setOnClickListener {
                attemptlogin()

            }
            btnToSignUp.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
        }
        auth = Firebase.auth
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }
    private fun attemptlogin() {
        var email = binding.editEmail.text.toString()
        var password = binding.editPassword.text.toString()
        if(email.equals("") || password.equals("")) {
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(Activity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(context, "Login berhasil",
                        Toast.LENGTH_SHORT).show()
                    val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                    findNavController().navigate(action)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

            }
    }


}