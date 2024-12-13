package com.example.collegeconnect.Teacher

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Base64
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.R
import com.example.collegeconnect.Student.student_upload.Users
import com.example.collegeconnect.databinding.ActivityTeacherUploadBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class teacher_upload : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityTeacherUploadBinding
    private val branchSubjectsMap = mapOf(
        "C.S.E.Engg." to listOf("Mathematics", "Physics", "Chemistry", "Engineering Drawing","Java","C.O.A.","Python","Software Engg.","Graphic Design","A.D.A."),
        "I.T.Engg." to listOf("Mathematics", "Physics", "Chemistry", "Engineering Drawing","Java","Python","Android","Graphic Design","C","C++"),
        "Electronics Engg." to listOf("Mathematics", "Physics", "Chemistry", "Engineering Drawing","Material Science", " Circuit Theory"," Practical Lab","Electrical Measurment Processing"),
        "E & C Engg." to listOf("Mathematics", "Physics", "Chemistry", "Engineering Drawing","C.C.N.","Electrical Lab","C.M.E.D.","Computer Programing","Digital Signal "),
        "Mechanical Engg." to listOf("Mathematics", "Physics", "Chemistry", "Engineering Drawing","Design Analysis"," Material Science","Fluid Mechanics","Workshop Processes"),
        "Civil Engg." to listOf("Mathematics", "Physics", "Chemistry", "Engineering Drawing","Engineering Geology","Structural Analysis","Mechanics Of Structure","A.C.E.P."),

        )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_teacher_upload)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_teacher_upload)
        changeStatusBarColor()
        binding.teachertoolbar.setNavigationOnClickListener {
            startActivity(Intent(this, Teacher_dashboard::class.java))

        }
        auth = FirebaseAuth.getInstance()

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setupUI()
        setupNameValidation()
        setupSubmitButton()
        binding.teacherregistration.etBranch.setOnClickListener {
            showBranchDropdown(it)
        }

        binding.teacherregistration.ettSubject.setOnClickListener {
            val select_branch= getString(R.string.select_branchfirst)
            val selectedBranch = binding.teacherregistration.etBranch.text.toString()
            if (selectedBranch.isNotEmpty()) {
                showSubjectDropdown(it, selectedBranch)
            } else {
                binding.teacherregistration.tsubject.error=select_branch
            }
        }


        binding.teacherregistration.ettgender.setOnClickListener {
            genderDropdown(it)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun changeStatusBarColor() {
        val window: Window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.lightblue)
    }

    private fun showBranchDropdown(view: View) {
        val branches = branchSubjectsMap.keys.toList()


        val popupMenu = androidx.appcompat.widget.PopupMenu(this, view)
        val menu = popupMenu.menu
        branches.forEachIndexed { index, branch ->
            menu.add(0, index, index, branch)
        }


        popupMenu.setOnMenuItemClickListener { item ->
            val selectedBranch = branches[item.itemId]
            binding.teacherregistration.etBranch.setText(selectedBranch)
            binding.teacherregistration.ettSubject.text = null
            true
        }

        popupMenu.show()
    }

    private fun showSubjectDropdown(view: View, selectedBranch: String) {
        val subjects = branchSubjectsMap[selectedBranch] ?: emptyList()

        val popupMenu = androidx.appcompat.widget.PopupMenu(this, view)
        val menu = popupMenu.menu
        subjects.forEachIndexed { index, subject ->
            menu.add(0, index, index, subject)
        }

        // Set listener to handle subject selection
        popupMenu.setOnMenuItemClickListener { item ->
            val selectedSubject = subjects[item.itemId]
            binding.teacherregistration.ettSubject.setText(selectedSubject)
            true
        }

        popupMenu.show()
    }

    private fun genderDropdown(view: View) {
        val popupMenu = PopupMenu(this, view)
        val options = resources.getStringArray(R.array.gender)

        options.forEach { option ->
            popupMenu.menu.add(option)
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            binding.teacherregistration.ettgender.setText(menuItem.title)
            binding.teacherregistration.ettgender.error= null
            true
        }

        popupMenu.show()
    }

    private fun setupUI() {

        val noSpaceInputFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.toString().contains(" ")) {

                if (binding.teacherregistration.ettname.hasFocus()) {
                    binding.teacherregistration.tname.error = getString(R.string.space_name)
                } else if (binding.teacherregistration.ettnumber.hasFocus()) {
                    binding.teacherregistration.tnumber.error = getString(R.string.space_number)
                }
                else if (binding.teacherregistration.ettexperience.hasFocus()){
                    binding.teacherregistration.texperience.error= getString(R.string.space_experiance)
                }

                ""
            } else {
                null
            }
        }
        binding.teacherregistration.ettname.filters = arrayOf(noSpaceInputFilter)
        binding.teacherregistration.ettnumber.filters = arrayOf(noSpaceInputFilter)

        binding.teacherregistration.ettname.addTextChangedListener(createTextWatcher(binding.teacherregistration.ettname))
        binding.teacherregistration.ettemail.addTextChangedListener(createEmailTextWatcher())
        binding.teacherregistration.ettnumber.filters = arrayOf(InputFilter.LengthFilter(10))
        binding.teacherregistration.ettnumber.addTextChangedListener(createTextWatcher(binding.teacherregistration.ettnumber))
        binding.teacherregistration.ettexperience.addTextChangedListener(createTextWatcher(binding.teacherregistration.ettexperience))
        binding.teacherregistration.ettexperience.filters = arrayOf(InputFilter.LengthFilter(2))

        binding.teacherregistration.ettpassword.filters = arrayOf(InputFilter.LengthFilter(12))
        binding.teacherregistration.ettpassword.addTextChangedListener(createPasswordTextWatcher())
        binding.teacherregistration.ettconfirmpass.addTextChangedListener(createConfirmPasswordTextWatcher())

    }
    private fun createTextWatcher(textInputLayout: TextInputEditText): TextWatcher {
        val required = getString(R.string.required)
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p: Editable?) {
                if (TextUtils.isEmpty(p.toString().trim())) {
//
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
                    binding.teacherregistration.temail.error = valid_email
                } else {
                    binding.teacherregistration.temail.error = null
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
                    binding.teacherregistration.tpassword.error = valid_password
                } else {
                    binding.teacherregistration.tpassword.error = null // Clear error when password is valid
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
                val pass = binding.teacherregistration.ettpassword.text.toString().trim()
                if (TextUtils.isEmpty(confirmPass) || confirmPass != pass) {
                    binding.teacherregistration.ettconfirmpass.error = match_pass
                } else {
                    binding.teacherregistration.ettconfirmpass.error = null
                }
            }
        }
    }

    private fun setupSubmitButton() {

        binding.teacherregistration.tsubmitbtn.setOnClickListener{
            val email = binding.teacherregistration.ettemail.text.toString()

            if (areFieldsValid()) {
                val updatedUser = Users(
                    username = binding.teacherregistration.ettname.text.toString(),
                    email = binding.teacherregistration.ettemail.text.toString(),
                    number = binding.teacherregistration.ettnumber.text.toString(),
                    branch =binding.teacherregistration.etBranch.text.toString(),
                    subject = binding.teacherregistration.ettSubject.text.toString(),
                    gender = binding.teacherregistration.ettgender.text.toString(),
                    experience = binding.teacherregistration.ettexperience.text.toString(),
                    password = binding.teacherregistration.ettpassword.text.toString()


                )
                updateTeacher(email,updatedUser)
            }
        }
    }

    private fun updateTeacher(email: String, updatedUser:Users) {
        val database = FirebaseDatabase.getInstance().getReference("Teacher")


        database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {

                        userSnapshot.ref.setValue(updatedUser).addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                Toast.makeText(this@teacher_upload, getString(R.string.data_updated), Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@teacher_upload, getString(R.string.fail_update), Toast.LENGTH_SHORT).show()
                            }
                        }
                        binding.teacherregistration.ettname.text?.clear()
                        binding.teacherregistration.ettemail.text?.clear()
                        binding.teacherregistration.ettnumber.text?.clear()
                        binding.teacherregistration.etBranch.text?.clear()
                        binding.teacherregistration.ettSubject.text?.clear()
                        binding.teacherregistration.ettgender.text?.clear()
                        binding.teacherregistration.ettexperience.text?.clear()
                        binding.teacherregistration.ettpassword.text?.clear()
                        binding.teacherregistration.ettconfirmpass.text?.clear()

                    }
                } else {
                    Toast.makeText(this@teacher_upload, getString(R.string.user_with_email), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@teacher_upload, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    data class Users(
        val username: String = " ",
        val email: String =" " ,
        val number: String =" " ,
        val branch : String =" ",
        val subject: String = " ",
        val gender: String= " ",
        val experience: String = " ",
        val password: String=" "
    ) {

    }



    private fun setupNameValidation() {
        val nameEditText: TextInputEditText = binding.teacherregistration.ettname

        nameEditText.addTextChangedListener(object : TextWatcher {


            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.any { it.isDigit() }) {
                    binding.teacherregistration.tname.error = getString(R.string.valid_name)


                    val filteredText = s.filter { !it.isDigit() }
                    nameEditText.setText(filteredText)

                    nameEditText.setSelection(filteredText.length)

                } else {
                    binding.teacherregistration.tname.error = null
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


        val name = binding.teacherregistration.ettname.text.toString().trim()
        if (TextUtils.isEmpty(name)) {
            binding.teacherregistration.tname.error = getString(R.string.empty_name)

        } else if (!isValidname(name)) {
            binding.teacherregistration.tname.error = getString(R.string.valid_name)
            isValid = false
        } else {
            binding.teacherregistration.tname.error = null
        }


        if (TextUtils.isEmpty(
                binding.teacherregistration.ettemail.text.toString().trim()) ) {
            binding.teacherregistration.temail.error = getString(R.string.empty_email)

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.teacherregistration.ettemail.text.toString().trim()).matches()){
            binding.teacherregistration.temail.error = valid_email

            isValid = false
        }
        else{
            binding.teacherregistration.temail.error=null
        }


        if (TextUtils.isEmpty(binding.teacherregistration.ettnumber.text.toString().trim())) {
            binding.teacherregistration.tnumber.error = getString(R.string.empty_number)
            isValid = false
        }
        else{
            binding.teacherregistration.tnumber.error=null
        }

        if(TextUtils.isEmpty(binding.teacherregistration.etBranch.text.toString().trim())){
            binding.teacherregistration.tbranch.error = getString(R.string.empty_branch)
            isValid= false
        }


        if (binding.teacherregistration.ettSubject.text.isNullOrEmpty()) {
            binding.teacherregistration.tsubject.error = getString(R.string.empty_subject)
            isValid = false
        }
        else{
            binding.teacherregistration.tsubject.error= null

        }



        if (binding.teacherregistration.ettgender.text.isNullOrEmpty()) {
            binding.teacherregistration.tgender.error = getString(R.string.empty_gender)
            isValid = false
        }
        else{
            binding.teacherregistration.tgender.error =null

        }



        if(TextUtils.isEmpty(binding.teacherregistration.ettexperience.text.toString().trim())){
            binding.teacherregistration.texperience.error = getString(R.string.empty_experience)
            isValid = false
        }



        if (TextUtils.isEmpty(binding.teacherregistration.ettpassword.text.toString().trim())) {
            binding.teacherregistration.tpassword.error = getString(R.string.empty_pass)
            isValid = false
        }
        else if (!isValidPassword(binding.teacherregistration.ettpassword.text.toString().trim())){
            binding.teacherregistration.tpassword.error=valid_password

        }

        else{
            binding.teacherregistration.ettpassword.error=null
        }



        if (TextUtils.isEmpty(binding.teacherregistration.ettconfirmpass.text.toString().trim()) || binding.teacherregistration.ettconfirmpass.text.toString().trim() != binding.teacherregistration.ettpassword.text.toString().trim()) {
            binding.teacherregistration.tconfirmpass.error = match_pass
            isValid = false
        }
        else{
            binding.teacherregistration.tconfirmpass.error =null
        }



        return isValid
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,12}\$"
        return password.matches(passwordRegex.toRegex())
    }

}