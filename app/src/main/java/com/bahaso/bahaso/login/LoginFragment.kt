package com.bahaso.bahaso.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.utils.Oscillator.TAG
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bahaso.bahaso.BaseFragment
import com.bahaso.bahaso.core.session.Preferences
import com.bahaso.bahaso.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class LoginFragment : BaseFragment() {
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            btnLogin.setOnClickListener {
                attemptLogin()
            }
            btnToSignUp.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
            lupaPassword.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToLupaPasswordFragment()
                findNavController().navigate(action)
            }

        }
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun attemptLogin() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()
        if (email.equals("") || password.equals("")) {
            Toast.makeText(context, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT)
                .show()
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(Activity()) { task ->
                if (task.isSuccessful) {

                    Log.d(TAG, "signInWithEmail:success")
//                    Toast.makeText(context, "${auth.uid}", Toast.LENGTH_SHORT).show()

                    // Take userdata and navigate to home
                    saveUserDataToPreference(email)

                } else {
                    Toast.makeText(context, "Login gagal", Toast.LENGTH_SHORT).show()
                }

            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveUserDataToPreference(userEmail: String)  {
        val db = FirebaseFirestore.getInstance()
        val userDbRef = db.collection("users")
            .document(auth.currentUser!!.uid)

        userDbRef.get().addOnSuccessListener { data ->

            val name = data.getString("name") ?: ""
            val birth = data.getString("birth_date") ?: ""
            val gender = data.getString("gender") ?: ""

            val pref = Preferences(requireContext())

            pref.setName(name)
            pref.setBirth(birth)
            pref.setGender(gender)
            pref.setEmail(userEmail)

            Toast.makeText(context, "Login berhasil", Toast.LENGTH_SHORT).show()
            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            findNavController().navigate(action)

        }.addOnFailureListener {
            Toast.makeText(context, "Gagal mengambil data dari firebase", Toast.LENGTH_LONG)
                .show()
        }
    }
}