package id.my.okisulton.firebaseauth.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.firebase.database.FirebaseDatabase
import id.my.okisulton.firebaseauth.EditDataFragment
import id.my.okisulton.firebaseauth.R
import id.my.okisulton.firebaseauth.databinding.FragmentDialogBinding
import id.my.okisulton.firebaseauth.databinding.UpdateDialogBinding
import id.my.okisulton.firebaseauth.model.post.PersonsPost


/**
 *Created by osalimi on 01-04-2022.
 **/
class PersonsAdapter(
    val mCtx: Context,
    val layoutResId: Int,
    val listPersons : List<PersonsPost>,
    ) : ArrayAdapter<PersonsPost> (mCtx, layoutResId, listPersons)
{
    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val name = view.findViewById<TextView>(R.id.tvName)
        val address = view.findViewById<TextView>(R.id.tvAddress)
        val edit = view.findViewById<ImageView>(R.id.ivEdit)

        val persons = listPersons[position]

        name.text = "Name : ${persons.name}"
        address.text = "Address : ${persons.address}"
        edit.setOnClickListener {
            showEditDialog(persons)
        }

        return view
    }

    @SuppressLint("InflateParams")
    private fun showEditDialog(persons: PersonsPost) {

        val activity = mCtx as FragmentActivity
        val fm: FragmentManager = activity.supportFragmentManager

        val dialog = AlertDialog.Builder(mCtx)
        dialog.setCancelable(true)

        val inflater = LayoutInflater.from(mCtx)
        val newView = inflater.inflate(R.layout.update_dialog, null)
        dialog.setView(newView)
        dialog.setTitle("Update Data!")


        val etName = newView.findViewById<EditText>(R.id.editTextTextPersonName)
        val etAddress = newView.findViewById<EditText>(R.id.editTextTextAddress)

        etName.setText(persons.name)
        etAddress.setText(persons.address)

        dialog.setPositiveButton("Save") {_,_ ->
            // Initial DB
            val dbPersons = FirebaseDatabase.getInstance().getReference("Persons")

            val newName = etName.text.toString()
            val newAddress = etAddress.text.toString()

            if (newName.isEmpty()) {
                etName.error = "Name can't be null"
                etName.hasFocus()
                return@setPositiveButton
            }
            if (newAddress.isEmpty()) {
                etAddress.error = "Address can't be null"
                etAddress.hasFocus()
                return@setPositiveButton
            }

            // UPDATE DATA
            val updatePersons = PersonsPost(persons.id, newName, newAddress)
            persons.id?.let { dbPersons.child(it).setValue(updatePersons) }

            Toast.makeText(mCtx, "Update Success", Toast.LENGTH_SHORT).show()
        }

        dialog.setNegativeButton("cancel", null)

        dialog.show()

    }
}