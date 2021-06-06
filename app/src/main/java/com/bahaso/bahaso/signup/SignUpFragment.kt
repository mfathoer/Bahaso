package com.bahaso.bahaso.signup

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bahaso.bahaso.BaseFragment
import com.bahaso.bahaso.MyApplication
import com.bahaso.bahaso.R
import com.bahaso.bahaso.core.ViewModelFactory
import com.bahaso.bahaso.core.session.Preferences
import com.bahaso.bahaso.databinding.FragmentSignUpBinding
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SignUpFragment : BaseFragment(), View.OnClickListener {

    private var _binding: FragmentSignUpBinding? = null

    private val binding
        get() = _binding!!

    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var name: String
    lateinit var password: String
    lateinit var email: String
    lateinit var gender: String
    lateinit var birth: String

    private val cal: Calendar = Calendar.getInstance()

    private val viewModel: SignUpViewModel by viewModels {
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
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener(this)
        binding.btnToLogin.setOnClickListener(this)

        viewModel.isLoading.observe(viewLifecycleOwner, { it ->
            showLoadingState(it)
        })

        // Move to Home
        viewModel.navigateToHome.observe(viewLifecycleOwner, {
            if (it) {
                val pref = Preferences(requireContext())
                pref.setName(name)
                pref.setBirth(birth)
                pref.setEmail(email)
                pref.setGender(gender)

                Log.d("SharedPreference", pref.email!!.toString())
                Log.d("SharedPreference", pref.name!!.toString())


                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeFragment())
            }
        })


        // Tanggal Lahir related
        val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

        binding.etBirth.setOnClickListener {
            DatePickerDialog(
                requireContext(), date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        /*Gender Related*/
        val genderList = resources.getStringArray(R.array.gender_list)
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            genderList
        )
        binding.etGender.adapter = spinnerAdapter

        binding.etGender.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                gender = genderList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun updateLabel() {
        val myFormat = "MM/dd/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.etBirth.text = sdf.format(cal.time)
    }

    /*Data related*/
    fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    fun validate(): Boolean {
        name = binding.etName.text.toString()
        email = binding.etEmail.text.toString()
        birth = binding.etBirth.text.toString()
        password = binding.etPass.text.toString()

        // Empty Check
        if (name.isEmpty()) {
            binding.etName.error = "Mohon masukkan nama anda"
            return false
        }

        if (birth.isEmpty()) {
            binding.etBirth.error = "Mohon masukkan tanggal lahir anda"
            return false
        }

        if (email.isEmpty()) {
            binding.etEmail.error = "Mohon masukkan email anda"
            return false
        }

        if (password.isEmpty()) {
            binding.etPass.error = "Mohon massukan passord anda"
            return false
        }

        // Format check

        if (!email.isValidEmail()) {
            binding.etEmail.error = "Email yang anda masukkan tidak valid"
            return false
        }

        if (password.length < 8) {
            binding.etPass.error = "Password yang anda masukkan kurang dari 8 karakter"
            return false
        }

        return true
    }


    private fun showLoadingState(state: Boolean) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnRegister -> {
                if (validate()) {
                    viewModel.signUp(name, birth, gender, email, password)
                }
            }

            R.id.btnToLogin -> {
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
            }
        }


    }
}