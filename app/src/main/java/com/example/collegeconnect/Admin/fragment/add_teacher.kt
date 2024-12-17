package com.example.collegeconnect.Admin.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Base64
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.Admin.Admin_dashboard
import com.example.collegeconnect.Login.Login_Activity
import com.example.collegeconnect.Models.Teacher
import com.example.collegeconnect.R
import com.example.collegeconnect.databinding.FragmentAddTeacherBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

class add_teacher : Fragment() {
    val auth= FirebaseAuth.getInstance()
    private lateinit var binding: FragmentAddTeacherBinding
    private val branchSubjectsMap = mapOf(
        "C.S.E.Engg." to listOf("Mathematics", "Physics", "Chemistry", "Engineering Drawing","Java","C.O.A.","Python","Software Engg.","Graphic Design","A.D.A."),
        "I.T.Engg." to listOf("Mathematics", "Physics", "Chemistry", "Engineering Drawing","Java","Python","Android","Graphic Design","C","C++"),
        "Electronics Engg." to listOf("Mathematics", "Physics", "Chemistry", "Engineering Drawing","Material Science", " Circuit Theory"," Practical Lab","Electrical Measurment Processing"),
        "E & C Engg." to listOf("Mathematics", "Physics", "Chemistry", "Engineering Drawing","C.C.N.","Electrical Lab","C.M.E.D.","Computer Programing","Digital Signal "),
        "Mechanical Engg." to listOf("Mathematics", "Physics", "Chemistry", "Engineering Drawing","Design Analysis"," Material Science","Fluid Mechanics","Workshop Processes"),
        "Civil Engg." to listOf("Mathematics", "Physics", "Chemistry", "Engineering Drawing","Engineering Geology","Structural Analysis","Mechanics Of Structure","A.C.E.P."),

        )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_teacher, container, false)

        setupToolbar()
//
        setupUI()
        setupNameValidation()
        setupSubmitButton()
        binding.etBranch.setOnClickListener {
            showBranchDropdown(it)
        }

        binding.ettSubject.setOnClickListener {
            val select_branch= getString(R.string.select_branchfirst)
            val selectedBranch = binding.etBranch.text.toString()
            if (selectedBranch.isNotEmpty()) {
                showSubjectDropdown(it, selectedBranch)
            } else {
                binding.tsubject.error=select_branch
            }
        }


        binding.ettgender.setOnClickListener {
            genderDropdown(it)
        }



        return binding.root
    }

    private fun setupToolbar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.apply {
            title = "Add Teacher"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back)
        }
    }

//    private fun setupBackPressedCallback() {
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                val fragmentManager = requireActivity().supportFragmentManager
//
//                    fragmentManager.beginTransaction()
//                        .replace(R.id.add_teacher, admin_home.newInstance())
//                        .commit()
//
//            }
//        })
//    }
    private fun showBranchDropdown(view: View) {
        val branches = branchSubjectsMap.keys.toList()


        val popupMenu = androidx.appcompat.widget.PopupMenu(requireContext(), view)
        val menu = popupMenu.menu
        branches.forEachIndexed { index, branch ->
            menu.add(0, index, index, branch)
        }


        popupMenu.setOnMenuItemClickListener { item ->
            val selectedBranch = branches[item.itemId]
            binding.etBranch.setText(selectedBranch)
            binding.ettSubject.text = null
            true
        }

        popupMenu.show()
    }

    private fun showSubjectDropdown(view: View, selectedBranch: String) {
        val subjects = branchSubjectsMap[selectedBranch] ?: emptyList()

        val popupMenu = androidx.appcompat.widget.PopupMenu(requireContext(), view)
        val menu = popupMenu.menu
        subjects.forEachIndexed { index, subject ->
            menu.add(0, index, index, subject)
        }


        popupMenu.setOnMenuItemClickListener { item ->
            val selectedSubject = subjects[item.itemId]
            binding.ettSubject.setText(selectedSubject)
            true
        }

        popupMenu.show()
    }

    private fun genderDropdown(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        val options = resources.getStringArray(R.array.gender)

        options.forEach { option ->
            popupMenu.menu.add(option)
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            binding.ettgender.setText(menuItem.title)
            binding.ettgender.error= null
            true
        }

        popupMenu.show()
    }

    private fun setupUI() {

        val noSpaceInputFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.toString().contains(" ")) {

                if (binding.ettname.hasFocus()) {
                    binding.tname.error = getString(R.string.space_name)
                } else if (binding.ettnumber.hasFocus()) {
                    binding.tnumber.error = getString(R.string.space_number)
                }
                else if (binding.ettexperience.hasFocus()){
                    binding.texperience.error= getString(R.string.space_experiance)
                }

                ""
            } else {
                null
            }
        }
        binding.ettname.filters = arrayOf(noSpaceInputFilter)
        binding.ettnumber.filters = arrayOf(noSpaceInputFilter)

        binding.ettname.addTextChangedListener(createTextWatcher(binding.ettname))
        binding.ettemail.addTextChangedListener(createEmailTextWatcher())
        binding.ettnumber.filters = arrayOf(InputFilter.LengthFilter(10))
        binding.ettnumber.addTextChangedListener(createTextWatcher(binding.ettnumber))
        binding.ettexperience.addTextChangedListener(createTextWatcher(binding.ettexperience))
        binding.ettexperience.filters = arrayOf(InputFilter.LengthFilter(2))
        binding.ettpassword.filters = arrayOf(InputFilter.LengthFilter(12))
        binding.ettpassword.addTextChangedListener(createPasswordTextWatcher())
        binding.ettconfirmpass.addTextChangedListener(createConfirmPasswordTextWatcher())

    }
    private fun createTextWatcher(textInputLayout: TextInputEditText): TextWatcher {

        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p: Editable?) {
                if (TextUtils.isEmpty(p.toString().trim())) {
                } else {
                    textInputLayout.error = null
                }
            }
        }
    }
    private fun createEmailTextWatcher(): TextWatcher {
        val valid_email = getString(R.string.valid_email)
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p: Editable?) {
                val email = p.toString().trim()
                if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.temail.error = valid_email
                } else {
                    binding.temail.error = null
                }
            }
        }
    }

    private fun createPasswordTextWatcher(): TextWatcher {
        val valid_password = getString(R.string.valid_pass)
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val pass = s.toString().trim()
                if (TextUtils.isEmpty(pass) || !isValidPassword(pass)) {
                    binding.tpassword.error = valid_password
                } else {
                    binding.tpassword.error = null
                }
            }



            private fun isValidPassword(password: String): Boolean {
                val passwordRegex =
                    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,12}\$"
                return password.matches(passwordRegex.toRegex())
            }
        }
    }

    private fun createConfirmPasswordTextWatcher(): TextWatcher {
        val match_pass = getString(R.string.match_pass)
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val confirmPass = s.toString().trim()
                val pass = binding.ettpassword.text.toString().trim()
                if (TextUtils.isEmpty(confirmPass) || confirmPass != pass) {
                    binding.ettconfirmpass.error = match_pass
                } else {
                    binding.ettconfirmpass.error = null
                }
            }
        }
    }

    private fun setupSubmitButton() {

        binding.tsubmitbtn.setOnClickListener{
            val username = binding.ettname.text.toString()
            val email = binding.ettemail.text.toString()
            val number = binding.ettnumber.text.toString()
            val branch =binding.etBranch.text.toString()
            val subject = binding.ettSubject.text.toString()
            val gender = binding.ettgender.text.toString()
            val experience = binding.ettexperience.text.toString()
            val password = binding.ettpassword.text.toString()
            if (areFieldsValid()) {
                registerTeacher(username, email, number,branch,subject, gender, experience, password)

            }
        }
    }


    private fun generateUniqueId(): String {
        return Random.nextInt(100000, 999999).toString()

    }



    private fun registerTeacher(username: String, email: String, number: String, branch: String, subject: String, gender: String, experience: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val uniqueId = generateUniqueId()
                    val database = FirebaseDatabase.getInstance().getReference("Teacher").child(uniqueId)
                    val user = Teacher(username, email, number, branch, subject, gender, experience, password, uniqueId)

                    database.setValue(user).addOnCompleteListener { dbTask ->
                        if (dbTask.isSuccessful) {

                            Toast.makeText(requireContext(), getString(R.string.registration_sucess), Toast.LENGTH_SHORT).show()
//                            openFragment(admin_home())
//                            val intent = Intent(requireContext(),Admin_dashboard::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                            startActivity(intent)
                            val fragmentManager = requireActivity().supportFragmentManager
                            fragmentManager.beginTransaction()
                                .replace(R.id.add_teacher, admin_home.newInstance())
                                .commit()
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.error_saving_user_data), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun openFragment(fragment: Fragment) {

            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment)

            fragmentTransaction.commit()


    }


    private fun setupNameValidation() {
        val nameEditText: TextInputEditText = binding.ettname

        nameEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.any { it.isDigit() }) {
                    binding.tname.error = getString(R.string.valid_name)


                    val filteredText = s.filter { !it.isDigit() }
                    nameEditText.setText(filteredText)

                    nameEditText.setSelection(filteredText.length)

                } else {
                    binding.tname.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }
    private fun encryptAES(eusername: String, key: ByteArray): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val secretKey: SecretKey = SecretKeySpec(key, "AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(eusername.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT) + ":" + Base64.encodeToString(iv, Base64.DEFAULT)

    }



    private fun isValidname(etname: String): Boolean {

        val nameRegex = "^[a-zA-Z]+$"
        return etname.matches(nameRegex.toRegex())
    }

    private fun areFieldsValid(): Boolean {
        val match_pass = getString(R.string.match_pass)
        val valid_email = getString(R.string.valid_email)
        val valid_password = getString(R.string.valid_pass)


        var isValid = true

//        for name
        val name = binding.ettname.text.toString().trim()
        if (TextUtils.isEmpty(name)) {
            binding.tname.error = getString(R.string.empty_name)

        } else if (!isValidname(name)) {
            binding.tname.error = getString(R.string.valid_name)
            isValid = false
        } else {
            binding.tname.error = null // Clear error if valid
        }


//        for email
        val email = binding.ettemail.text.toString()
        if (TextUtils.isEmpty(
                binding.ettemail.text.toString().trim()) ) {
            binding.temail.error = getString(R.string.empty_email)

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.ettemail.text.toString().trim()).matches()){
            binding.temail.error = valid_email

            isValid = false
        }
        else{
            binding.temail.error=null
        }


//        for contact
        if (TextUtils.isEmpty(binding.ettnumber.text.toString().trim())) {
            binding.tnumber.error = getString(R.string.empty_number)
            isValid = false
        }
        else{
            binding.tnumber.error=null
        }


//        for branch

        if(TextUtils.isEmpty(binding.etBranch.text.toString().trim())){
            binding.tbranch.error = getString(R.string.empty_branch)
            isValid= false
        }



        //        for subject
        if (binding.ettSubject.text.isNullOrEmpty()) {
            binding.tsubject.error = getString(R.string.empty_subject)
            isValid = false
        }
        else{
            binding.tsubject.error= null

        }





//        for gender
        if (binding.ettgender.text.isNullOrEmpty()) {
            binding.tgender.error = getString(R.string.empty_gender)
            isValid = false
        }
        else{
            binding.tgender.error =null

        }


//        for Experience

        if(TextUtils.isEmpty(binding.ettexperience.text.toString().trim())){
            binding.texperience.error = getString(R.string.empty_experience)
            isValid = false
        }




//        for password
        if (TextUtils.isEmpty(binding.ettpassword.text.toString().trim())) {
            binding.tpassword.error = getString(R.string.empty_pass)
            isValid = false
        }
        else if (!isValidPassword(binding.ettpassword.text.toString().trim())){
            binding.tpassword.error=valid_password

        }

        else{
            binding.ettpassword.error=null
        }


//        for confirm password
        if (TextUtils.isEmpty(binding.ettconfirmpass.text.toString().trim()) || binding.ettconfirmpass.text.toString().trim() != binding.ettpassword.text.toString().trim()) {
            binding.tconfirmpass.error = match_pass
            isValid = false
        }
        else{
            binding.tconfirmpass.error =null
        }



        return isValid
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,12}\$"
        return password.matches(passwordRegex.toRegex())
    }




    companion object {
        @JvmStatic
        fun newInstance() = add_teacher().apply {
            arguments = Bundle().apply {
                // Pass data if necessary
            }
        }
    }
}
