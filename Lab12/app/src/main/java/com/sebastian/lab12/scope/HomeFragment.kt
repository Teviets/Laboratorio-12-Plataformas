package com.sebastian.lab12.scope

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import coil.load
import com.sebastian.lab12.R
import com.sebastian.lab12.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.triggerStateFlow()
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        lifecycleScope.launchWhenCreated {
            mainViewModel.validAuthToken.collectLatest {
                verStatusMain(it)
            }
        }
        lifecycleScope.launchWhenCreated {
            homeViewModel.statusApp.collectLatest {
                verStatus(it)
            }
        }
    }

    private fun verStatusMain(it: Boolean) {
        when (it){
            true ->{
                // Mantiene la sesion
            }
            false ->{
                requireView().findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToLoginFragment()
                )
            }
        }

    }

    private fun verStatus(it: HomeViewModel.StatusApp) {
        when (it){
            is HomeViewModel.StatusApp.default ->{
                binding.apply {
                    binding.textView.text = getString(R.string.default_txt_in_TxtView)
                    binding.imageView.load(R.drawable.android)
                    imageView.visibility = View.VISIBLE
                    textView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
            is HomeViewModel.StatusApp.succes ->{
                binding.apply {
                    binding.textView.text = getString(R.string.success_txt_in_TxtView)
                    binding.imageView.load(R.drawable.succesimg)
                    imageView.visibility = View.VISIBLE
                    textView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
            is HomeViewModel.StatusApp.failure ->{
                binding.apply {
                    binding.textView.text = getString(R.string.failure_txt_in_TxtView)
                    binding.imageView.load(R.drawable.failureimg)
                    imageView.visibility = View.VISIBLE
                    textView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
            is HomeViewModel.StatusApp.Empty ->{
                binding.apply {
                    textView.text = getString(R.string.empty_txt_in_TxtView)
                    imageView.load(R.drawable.emptyimg)
                    imageView.visibility = View.VISIBLE
                    textView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE

                }
            }
            is HomeViewModel.StatusApp.loading ->{
                binding.apply {
                    imageView.visibility = View.GONE
                    textView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setListeners() {
        binding.DefaultButton.setOnClickListener {
            homeViewModel.carga()
            homeViewModel.reset()
        }
        binding.SuccesButton.setOnClickListener {
            homeViewModel.carga()
            homeViewModel.exito()
        }
        binding.FailureButton.setOnClickListener {
            homeViewModel.carga()
            homeViewModel.fallo()
        }
        binding.EmptyButton.setOnClickListener {
            homeViewModel.carga()
            homeViewModel.vacio()
        }
        binding.LogOutButton.setOnClickListener {
            mainViewModel.cerrarSesion()
            requireView().findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToLoginFragment()
            )
        }
        binding.SessionOnButton.setOnClickListener {
            mainViewModel.triggerStateFlow()
        }
    }

}