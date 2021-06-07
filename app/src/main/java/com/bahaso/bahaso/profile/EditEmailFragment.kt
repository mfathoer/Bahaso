package com.bahaso.bahaso.profile

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bahaso.bahaso.BaseFragment
import com.bahaso.bahaso.MyApplication
import com.bahaso.bahaso.core.ViewModelFactory
import com.bahaso.bahaso.core.data.LoadResult
import com.bahaso.bahaso.core.session.Preferences
import com.bahaso.bahaso.databinding.FragmentEditEmailBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

import javax.inject.Inject

class EditEmailFragment : BaseFragment() {
    private var _binding: FragmentEditEmailBinding ? = null

    private val binding
        get() = _binding!!

    lateinit var oldEmail: String
    lateinit var newEmail: String
    lateinit var password: String

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: ProfileViewModel by viewModels {
        factory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentEditEmailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnEditEmail?.setOnClickListener{
            if(validate()){
                val pref = Preferences(requireContext())
                oldEmail = pref.email.toString()

                lifecycleScope.launch {
                    viewModel.changeEmail(oldEmail, newEmail, password).collect { loadResult ->
                        when (loadResult) {
                            is LoadResult.Success -> {
                                Toast.makeText(context,
                                    "Email Berhasil diubah",
                                    Toast.LENGTH_SHORT).show()
                                redirectToProfile()
                            }
                            is LoadResult.Error -> {
                                Toast.makeText(context, loadResult.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }

    fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    fun validate() : Boolean {
        newEmail = binding?.etEmail?.text.toString()
        password = binding?.etPassword?.text.toString()

        if(newEmail.isEmpty()){
            binding?.etEmail?.setError("Mohon masukkan Email")
            return false
        }

        if(password.isEmpty()){
            binding?.etPassword?.setError("Mohon masukkan password")
            return false
        }

        if(!newEmail.isValidEmail()){
            binding?.etEmail?.setError("Mohon masukkan email yang valid")
            return false
        }

        if(password.length < 8) {
            binding?.etPassword?.setError("Password harus memiliki panjang minimum 8 karakter")
            return false
        }

        return true
    }

    fun redirectToProfile(){
        val action = EditEmailFragmentDirections.actionEditEmailFragmentToProfileFragment()
        findNavController().navigate(action)
    }

}