package com.bahaso.bahaso.profile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bahaso.bahaso.BaseFragment
import com.bahaso.bahaso.MyApplication
import com.bahaso.bahaso.core.ViewModelFactory
import com.bahaso.bahaso.core.session.Preferences
import com.bahaso.bahaso.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject


class ProfileFragment : BaseFragment() {
    private var _binding: FragmentProfileBinding? = null

    private val binding
        get() = _binding

    @Inject
    lateinit var factory: ViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = Preferences(requireContext())

        val user_id = Firebase.auth.currentUser?.uid.toString()

        Glide
            .with(this)
            .load(getURLProfile(user_id))
            .into(binding!!.fotoProfile)

        binding?.emailProfile?.text = pref.email
        binding?.namaProfile?.text = pref.name
        binding?.tanggalProfile?.text = pref.birth

        binding?.btnLogout?.setOnClickListener {
            pref.logout()
            Firebase.auth.signOut()

            val action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        binding?.btnEditBiodata?.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToEditBiodataFragment()
            findNavController().navigate(action)
        }

        binding?.btnEditPassword?.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToEditPasswordFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getURLProfile(user_id: String) : String {
        val builder: Uri.Builder = Uri.Builder()
        builder.scheme("https")
            .authority("robohash.org")
            .appendPath(user_id + ".png")
        return builder.build().toString()
    }
}