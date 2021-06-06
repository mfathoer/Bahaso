package com.bahaso.bahaso.theory

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bahaso.bahaso.MyApplication
import com.bahaso.bahaso.R
import com.bahaso.bahaso.core.ViewModelFactory
import com.bahaso.bahaso.databinding.FragmentTheoryBinding
import javax.inject.Inject

class TheoryFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentTheoryBinding? = null

    private val binding
        get() = _binding

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: TheoryViewModel by viewModels {
        factory
    }

    private val args: TheoryFragmentArgs by navArgs()

    private lateinit var viewPagerAdapter: TheoryPagerAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTheoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnNextTheory?.setOnClickListener(this)
        binding?.btnTakeExam?.setOnClickListener(this)
        binding?.btnPrevTheory?.setOnClickListener(this)

        binding?.tvTopicTheory?.text = args.topic?.topic
        setupTheoryViewPager()
    }

    private fun setupTheoryViewPager() {
        viewModel.getDataTheory()
        viewPagerAdapter =
            TheoryPagerAdapter(childFragmentManager, lifecycle, viewModel.listDataTheory)

        binding?.theoryViewPager?.adapter = viewPagerAdapter

        binding?.theoryViewPager?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setViewPagerCurrentPage(position)

                if (position == 0) {
                    binding?.btnPrevTheory?.text = getString(R.string.back)
                } else {
                    binding?.btnPrevTheory?.text = getString(R.string.before)
                }

                if (viewModel.listDataTheory.size - 1 == position) {
                    showTakeExamButton(true)
                } else {
                    showTakeExamButton(false)
                }
            }
        })
    }

    private fun showTakeExamButton(state: Boolean) {
        if (state) {
            binding?.btnTakeExam?.visibility = View.VISIBLE
            binding?.btnNextTheory?.visibility = View.INVISIBLE
        } else {
            binding?.btnTakeExam?.visibility = View.INVISIBLE
            binding?.btnNextTheory?.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_prev_theory -> {
                if (binding?.theoryViewPager?.currentItem != 0) {
                    binding?.theoryViewPager?.currentItem = viewModel.viewPagerCurrentPage - 1
                } else {
                    activity?.onBackPressed()
                }
            }

            R.id.btn_next_theory -> {
                if (viewModel.listDataTheory.size - 1 != binding?.theoryViewPager?.currentItem) {
                    binding?.theoryViewPager?.currentItem = viewModel.viewPagerCurrentPage + 1
                }
            }

            R.id.btn_take_exam -> {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.apply {
                    setTitle(getString(R.string.are_you_sure))
                    setMessage(getString(R.string.take_exam_alert_dialog_message))
                    setPositiveButton(getString(R.string.start)) { _, _ ->
                        val action = TheoryFragmentDirections.actionTheoryFragmentToQuizFragment()
                        action.topic = args.topic
                        findNavController().navigate(action)
                    }
                    setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                    show()
                }
            }
        }
    }
}