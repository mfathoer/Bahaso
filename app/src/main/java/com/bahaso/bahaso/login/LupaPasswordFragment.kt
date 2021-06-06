package com.bahaso.bahaso.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bahaso.bahaso.databinding.FragmentLoginBinding
import com.bahaso.bahaso.databinding.FragmentLupaPasswordBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class LupaPasswordFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentLupaPasswordBinding? = null
    private val binding: FragmentLupaPasswordBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLupaPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with (binding){
            btnKirim.setOnClickListener {
               reset_password()
            }
            btnLogin.setOnClickListener {
                val action = LupaPasswordFragmentDirections.actionLupaPasswordFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun reset_password() {
        val email = binding.editEmail.text.toString()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Link Reset Password Sudah Terkirim, Silahkan Cek Email Kalian", Toast.LENGTH_SHORT).show()
                }
            }
    }
}