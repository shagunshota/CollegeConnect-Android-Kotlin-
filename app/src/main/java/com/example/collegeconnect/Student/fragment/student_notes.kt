package com.example.collegeconnect.Student.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.collegeconnect.R
import com.example.collegeconnect.Student.View_notes
import com.example.collegeconnect.Student.student_profile
import com.example.collegeconnect.Student.student_settings
import com.example.collegeconnect.Student.student_upload
import com.example.collegeconnect.databinding.FragmentStudentNotesBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class student_notes : Fragment() {
    private lateinit var binding: FragmentStudentNotesBinding
    private val PICK_PDF_REQUEST = 1
    private var pdfUri: Uri? = null

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
    ): View? {
        checkNetworkStatus()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_notes, container, false)

        binding.attachpdf.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, PICK_PDF_REQUEST)
        }
        binding.etBranch.setOnClickListener {
            showBranchDropdown(it)
        }
        binding.etsemester.setOnClickListener {
            showSemesterDropdown(it)
        }
        binding.etUnit.setOnClickListener {
            showUnitDropdown(it)
        }
        binding.toolbar.setOnClickListener {
            showDropdownMenu(it)
        }
        binding.viewNotes.setOnClickListener{
            startActivity(Intent(requireContext(),View_notes::class.java))

        }

        binding.etsubject.setOnClickListener {
            val select_branch= getString(R.string.select_branchfirst)
            val selectedBranch = binding.etBranch.text.toString()
            if (selectedBranch.isNotEmpty()) {
                showSubjectDropdown(it, selectedBranch)
            } else {
                binding.ilsubject.error=select_branch
            }
        }
        binding.uploadbtn.setOnClickListener{
            if (areFieldsValid()){


            }
            else{
                Toast.makeText(requireContext(),getString(R.string.notes_upload_fail),Toast.LENGTH_SHORT).show()
            }
        }


        return binding.root
    }

    private fun uploadPdfToFirebase() {
        if (pdfUri == null) {
            Toast.makeText(requireContext(), "Please select a PDF first", Toast.LENGTH_SHORT).show()
            return
        }

        val storageRef = FirebaseStorage.getInstance().reference
        val pdfRef = storageRef.child("pdfs/${System.currentTimeMillis()}.pdf")

        pdfRef.putFile(pdfUri!!)
            .addOnSuccessListener {
                pdfRef.downloadUrl.addOnSuccessListener { uri ->
                    savePdfUrlToDatabase(uri.toString())
                }.addOnFailureListener { e ->
                    Log.e("Firebase", "Failed to get download URL: ${e.message}")
                    Toast.makeText(requireContext(), "Failed to get download URL", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Upload failed: ${e.message}")
                Toast.makeText(requireContext(), "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun savePdfUrlToDatabase(downloadUrl: String) {
        val databaseRef = FirebaseDatabase.getInstance().reference.child("pdfs")
        val pdfId = databaseRef.push().key

        pdfId?.let {
            databaseRef.child(it).setValue(downloadUrl)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "PDF URL saved to Realtime Database", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Log.e("Firebase", "Failed to save URL: ${exception.message}")
                    Toast.makeText(requireContext(), "Failed to save URL", Toast.LENGTH_SHORT).show()
                }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK) {
            pdfUri = data?.data
            pdfUri?.let { uri ->
                // Show a success message or display the file name
                val fileName = getFileName(uri)
                binding.fileName.text = fileName ?: "PDF Selected"
                Toast.makeText(requireContext(), "PDF Selected: $fileName", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(requireContext(), "Failed to select PDF", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    fileName = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        return fileName ?: uri.lastPathSegment
    }




    private fun areFieldsValid(): Boolean {


        var isValid = true


        val topic = binding.ettopic.text.toString().trim()
        if (TextUtils.isEmpty(topic)) {
            binding.iltopic.error = getString(R.string.empty_name)

        } else if (!isValidname(topic)) {
            binding.iltopic.error = getString(R.string.valid_name)
            isValid = false
        } else {
            binding.iltopic.error = null
        }




//        for Unit
        if (binding.etUnit.text.isNullOrEmpty()) {
            binding.ilUnit.error = getString(R.string.empty_gender)
            isValid = false
        }
        else{
            binding.ilUnit.error =null

        }

        //        for subject
        if (TextUtils.isEmpty(binding.etsubject.text.toString().trim())) {
            binding.ilsubject.error = getString(R.string.empty_subject)
            isValid = false
        }
        else{
            binding.ilsubject.error=null

        }



//        for semester
        if (binding.etsemester.text.isNullOrEmpty()) {
            binding.ilsemester.error = getString(R.string.empty_semester)
            isValid = false
        }
        else {
            binding.ilsemester.error = null
        }


        //  for Branch

        if (binding.etBranch.text.isNullOrEmpty()) {
            binding.ilBranch.error =getString(R.string.empty_branch)
            isValid = false
        }
        else{
            binding.ilBranch.error=null

        }


        if (pdfUri != null) {
            uploadPdfToFirebase()
        } else {
            Toast.makeText(requireContext(), "Please select a PDF first", Toast.LENGTH_SHORT).show()
        }

//        //   for pdf
//        if (binding.fileName.text.isNullOrEmpty()){
//            binding.pdferror.visibility = View.VISIBLE
//            binding.pdferror.error=getString(R.string.attach_Pdf)
//
//            isValid = false
//        }
//        else{
//            binding.pdferror.visibility = View.GONE
//            binding.pdferror.error=null
//        }




        return isValid
    }



    private fun isValidname(etname: String): Boolean {

        val nameRegex = "^[a-zA-Z]+$"
        return etname.matches(nameRegex.toRegex())
    }

    private fun checkNetworkStatus(): Boolean {
        if (!isNetworkConnected(requireContext())) {
            Snackbar.make(requireView(), getString(R.string.no_internet), Snackbar.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


    private fun showDropdownMenu(anchor: android.view.View) {
        val popupMenu = PopupMenu(requireContext(), anchor)
        popupMenu.menuInflater.inflate(R.menu.options, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {

                R.id.profile -> {
                    val intent = Intent(requireContext(), student_profile::class.java)
                    startActivity(intent)
                    true
                }
                R.id.settings -> {
                    val intent = Intent(requireContext(), student_settings::class.java)
                    startActivity(intent)
                    true
                }
                R.id.update_Profile -> {
                    val intent = Intent(requireContext(), student_upload::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()

    }

    private fun showSemesterDropdown(anchorView: View?) {
        val popupMenu = PopupMenu(requireContext(),anchorView)
        val  options = resources.getStringArray(R.array.select_sem)
        options.forEach { options ->
            popupMenu.menu.add(options)
        }
        popupMenu.setOnMenuItemClickListener { menuItem ->
            binding.etsemester.setText(menuItem.title)
            binding.etsemester.error= null
            true
        }
        popupMenu.show()
    }



    private fun showUnitDropdown(anchorView: View) {
        val popupMenu = PopupMenu(requireContext(), anchorView)
        val unitoptions = resources.getStringArray(R.array.unit)
            unitoptions.forEach { option ->
            popupMenu.menu.add(option)
        }
        popupMenu.setOnMenuItemClickListener { menuItem ->
            binding.etUnit.setText(menuItem.title)
            binding.etUnit.error = null
            true
        }
        popupMenu.show()
    }


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
            binding.etsubject.text = null
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
            binding.etsubject.setText(selectedSubject)
            true
        }
        popupMenu.show()
    }


//    private fun openFilePicker() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "application/pdf"
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        try {
//            startActivityForResult(Intent.createChooser(intent, "Select PDF"),PICK_PDF_REQUEST)
//        } catch (e: Exception) {
//            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK) {
//            data?.data?.let { uri ->
//                pdfUri = uri
//                val fileName = getFileName(uri)
//                binding.fileName.text = " $fileName"
//            }
//        }
//    }
//
//    private fun getFileName(uri: Uri): String? {
//        var fileName: String? = null
//        if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
//            val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
//            cursor?.use {
//                if (it.moveToFirst()) {
//                    fileName = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
//                }
//            }
//        }
//        return fileName ?: uri.lastPathSegment
//    }


    companion object {
        fun newInstance() =
            student_notes().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
