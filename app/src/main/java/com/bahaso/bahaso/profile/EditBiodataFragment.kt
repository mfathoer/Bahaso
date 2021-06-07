package com.bahaso.bahaso.profile

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bahaso.bahaso.BaseFragment
import com.bahaso.bahaso.MyApplication
import com.bahaso.bahaso.R
import com.bahaso.bahaso.core.ViewModelFactory
import com.bahaso.bahaso.core.data.LoadResult
import com.bahaso.bahaso.core.session.Preferences
import com.bahaso.bahaso.databinding.FragmentEditBiodataBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EditBiodataFragment : BaseFragment() {
    private var _binding: FragmentEditBiodataBinding? = null
    private val cal: Calendar = Calendar.getInstance()
    private val binding
        get() = _binding

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: ProfileViewModel by viewModels {
        factory
    }

    lateinit var name: String
    lateinit var gender: String
    lateinit var birth: String
    lateinit var uid: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentEditBiodataBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = Preferences(requireContext())

        // Tanggal lahir
        val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

        binding?.etBirth?.setOnClickListener {
            DatePickerDialog(
                requireContext(), date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Gender
        val genderList = resources.getStringArray(R.array.gender_list)
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            genderList
        )
        binding?.etGender?.adapter = spinnerAdapter

        binding?.etGender?.onItemSelectedListener = object :
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
        // Set initial value ke form edit biodata
        binding?.etGender?.setSelection(spinnerAdapter.getPosition(pref.gender))
        binding?.etName?.setText(pref.name)
        binding?.etBirth?.setText(pref.birth)

        // Jika button edit dipencet
        binding?.btnEditBiodata?.setOnClickListener{
            name = binding?.etName?.text.toString()
            birth = binding?.etBirth?.text.toString()
            uid = Firebase.auth.uid.toString()
            if(validate(name, birth)){
                val data = hashMapOf(
                    "name" to name,
                    "birth_date" to birth,
                    "gender" to gender
                )

                lifecycleScope.launch{
                    viewModel.editDataFromFireStore(uid, data).collect{loadResult ->
                        when(loadResult){
                            is LoadResult.Success -> {
                                Toast.makeText(context, "Biodata Berhasil diubah", Toast.LENGTH_SHORT).show()

                                pref.setName(name)
                                pref.setBirth(birth)
                                pref.setGender(gender)

                                redirectToProfile()
                            }
                            is LoadResult.Error -> {
                                Toast.makeText(context, "Biodata gagal diubah", Toast.LENGTH_SHORT).show()

                                redirectToProfile()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun validate(name: String, birth: String) : Boolean {
        // Empty Check
        if (name.isEmpty()) {
            binding?.etName?.error = "Mohon masukkan nama anda"
            return false
        }

        if (birth.isEmpty()) {
            binding?.etBirth?.error = "Mohon masukkan tanggal lahir anda"
            return false
        }

        return true
    }

    private fun redirectToProfile() {
        val action = EditBiodataFragmentDirections.actionEditBiodataFragmentToProfileFragment()
        findNavController().navigate(action)
    }

    private fun updateLabel() {
        val myFormat = "MM/dd/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding?.etBirth?.text = sdf.format(cal.time)
    }
}