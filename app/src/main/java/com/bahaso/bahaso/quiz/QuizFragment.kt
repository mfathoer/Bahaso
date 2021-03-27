package com.bahaso.bahaso.quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bahaso.bahaso.MyApplication
import com.bahaso.bahaso.core.ViewModelFactory
import com.bahaso.bahaso.databinding.FragmentQuizBinding
import javax.inject.Inject


class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null

    private val binding
        get() = _binding!!

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: QuizViewModel by viewModels {
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
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}