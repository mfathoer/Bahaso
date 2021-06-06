package com.bahaso.bahaso.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bahaso.bahaso.BaseFragment
import com.bahaso.bahaso.core.session.Preferences
import com.bahaso.bahaso.databinding.FragmentScoreBinding

class ScoreFragment : BaseFragment(){
    private var _binding: FragmentScoreBinding? = null

    private val binding get() = _binding

    private val arguments: ScoreFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScoreBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = Preferences(requireContext())
        binding?.scoreName?.text  = pref.name
        binding?.scoreGrade?.text = arguments.scoreArg.toString()

        binding!!.btnContinue.setOnClickListener {
            val action = ScoreFragmentDirections.actionScoreFragmentToHomeFragment().actionId
            findNavController().navigate(action)
        }

        binding!!.btnTryAgain.setOnClickListener {
            val action = ScoreFragmentDirections.actionScoreFragmentToQuizFragment()
            action.topic = arguments.topic
            findNavController().navigate(action)
        }

        }
    }

