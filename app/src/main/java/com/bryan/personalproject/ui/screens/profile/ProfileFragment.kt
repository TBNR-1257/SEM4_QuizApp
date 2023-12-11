package com.bryan.personalproject.ui.screens.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bryan.personalproject.AuthActivity
import com.bryan.personalproject.MainActivity
import com.bryan.personalproject.R
import com.bryan.personalproject.databinding.FragmentProfileBinding
import com.bryan.personalproject.ui.screens.base.BaseFragment
import com.bryan.personalproject.ui.screens.profile.viewModel.ProfileViewModelImpl
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override val viewModel: ProfileViewModelImpl by viewModels()
    lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                viewModel.updateProfilePic(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }


    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)



        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }

        binding.icEditProfilePic.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

    }


    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)

        lifecycleScope.launch {
            viewModel.user.collect {
                binding.tvUsername.text = it.name
            }
        }


        lifecycleScope.launch {
            viewModel.finish.collect {
                val intent = Intent(requireContext(), AuthActivity::class.java)
                requireActivity().startActivity(intent)
                (requireActivity() as MainActivity).finish()
            }
        }

        lifecycleScope.launch {
            viewModel.profileUri.collect {
                Glide.with(requireContext())
                    .load(it)
                    .placeholder(R.drawable.ic_profile)
                    .into(binding.ivProfile)
            }
        }

    }


}