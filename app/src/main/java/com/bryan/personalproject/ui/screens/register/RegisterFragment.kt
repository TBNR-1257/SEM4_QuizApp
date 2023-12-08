package com.bryan.personalproject.ui.screens.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bryan.personalproject.R
import com.bryan.personalproject.databinding.FragmentRegisterBinding
import com.bryan.personalproject.ui.screens.base.BaseFragment
import com.bryan.personalproject.ui.screens.register.viewModel.RegisterViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    override val viewModel: RegisterViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val roleOptions = resources.getStringArray(R.array.roles_array)
        val autoCompleteAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, roleOptions)

        binding.atvRole.setAdapter(autoCompleteAdapter)

        binding.atvRole.setOnItemClickListener { _, _, position, _ ->
            val selectedRole = roleOptions[position]
            // Now, you can use the selectedRole in your registration process
        }

        setupUiComponents(view)
        setupViewModelObserver(view)
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)

        binding.run {
            btnRegister.setOnClickListener {
                viewModel.register(
                    etName.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    etConfirmPass.text.toString(),
                    atvRole.text.toString()
                )
            }

            tvLogin.setOnClickListener {
                navController.popBackStack()
            }

        }
    }

    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)
        lifecycleScope.launch {
            viewModel.success.collect {
                val action = RegisterFragmentDirections.registerToLogin()
                navController.navigate(action)
            }
        }
    }

}