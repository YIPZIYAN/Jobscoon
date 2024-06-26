package com.example.assignment.auth

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.assignment.employee.EmployeeNavHost
import com.example.assignment.R
import com.example.assignment.databinding.FragmentLoginBinding
import com.example.assignment.employer.EmployerNavHost

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        binding.signUpBtn.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_loginFragment_to_signUpChooser)
        }

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.loginBtn.setOnClickListener {
            viewModel.submitLogin(
                binding.loginEmail.text.toString(),
                binding.loginPassword.text.toString()
            )
        }

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            if (it.success) {

                viewModel.isEmployer.observe(viewLifecycleOwner, Observer { isEmployer->
                    if (isEmployer == 1){
                        Log.d("EMPLOYER", "onActivityCreated: ")
                        val intent = Intent(requireActivity(), EmployerNavHost::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }else{
                        Log.d("EMPLOYEE", "onActivityCreated: ")
                        val intent = Intent(requireActivity(), EmployeeNavHost::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                })


            } else {
                Toast.makeText(requireContext(), it.errorMsg, Toast.LENGTH_LONG).show()

            }
        })


    }

}