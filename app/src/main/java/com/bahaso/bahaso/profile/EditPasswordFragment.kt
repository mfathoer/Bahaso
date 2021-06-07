package com.bahaso.bahaso.profile

import android.content.Context
import android.os.Bundle
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
import com.bahaso.bahaso.databinding.FragmentEditPasswordBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditPasswordFragment : BaseFragment() {
    private var _binding: FragmentEditPasswordBinding ? = null

    private val binding
        get() = _binding!!

    lateinit var email: String
    lateinit var oldPassword: String
    lateinit var newPassword: String
    lateinit var newPassword2: String

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
        _binding = FragmentEditPasswordBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnEditPassword?.setOnClickListener{
           if(validate()){
               val pref = Preferences(requireContext())
               email = pref.email.toString()

               lifecycleScope.launch {
                   viewModel.changePassword(email, oldPassword, newPassword).collect { loadResult ->
                       when (loadResult) {
                           is LoadResult.Success -> {
                               Toast.makeText(context,
                                   "Password Berhasil diubah",
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

    fun validate() : Boolean {
        oldPassword = binding?.oldPassword.text.toString()
        newPassword = binding?.newPassword.text.toString()
        newPassword2 = binding?.newPassword2.text.toString()

        if(oldPassword.isEmpty()){
            binding?.oldPassword?.setError("Mohon masukkan password lama anda")
            return false
        }

        if(newPassword.isEmpty()){
            binding?.newPassword?.setError("Mohon masukkan password bar anda")
            return false
        }

        if(newPassword2.isEmpty()){
            binding?.newPassword2?.setError("Mohon ulangi password anda")
            return false
        }

        if(newPassword.length < 8) {
            binding?.newPassword?.setError("Password harus lebih dari 8 karakter")
            return false
        }

        if(newPassword2.length < 8) {
            binding?.newPassword2?.setError("Password harus lebih dari 8 karakter")
            return false
        }

        if(!newPassword.equals(newPassword2)){
            binding?.newPassword?.setError("Password tidak sama")
            return false
        }
        return true
    }

    fun redirectToProfile(){
        val action = EditPasswordFragmentDirections.actionEditPasswordFragmentToProfileFragment()
        findNavController().navigate(action)
    }

}