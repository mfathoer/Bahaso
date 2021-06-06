package com.bahaso.bahaso.theory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bahaso.bahaso.core.domain.Theory
import com.bahaso.bahaso.databinding.FragmentScreenSlideTheoryBinding
import com.bumptech.glide.Glide

class ScreenSlideTheoryFragment : Fragment() {

    private var _binding: FragmentScreenSlideTheoryBinding? = null

    private val binding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentScreenSlideTheoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val theory: Theory? = arguments?.getParcelable(EXTRA_DATA_THEORY)

        theory?.let {
            binding?.tvTextJava?.text = it.text
            binding?.tvTranslation?.text = it.translation
            binding?.imgTheory?.let { imageView -> Glide.with(this).load(it.imageDrawable).into(imageView) }
        }
    }

    companion object {
        private const val EXTRA_DATA_THEORY = "extra_data_theory"

        @JvmStatic
        fun newInstance(theory: Theory) =
            ScreenSlideTheoryFragment().apply {
                arguments = bundleOf(EXTRA_DATA_THEORY to theory)
            }
    }
}