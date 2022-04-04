package id.my.okisulton.firebaseauth.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase
import id.my.okisulton.firebaseauth.R
import id.my.okisulton.firebaseauth.model.post.PersonsPost

/**
 *Created by osalimi on 01-04-2022.
 **/
class PersonsAdapter(
    val mCtx: Context,
    val layoutResId: Int,
    val listPersons : List<PersonsPost>
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
        edit.setOnClickListener { showEditDialog(persons) }

        return view
    }

    @SuppressLint("InflateParams")
    private fun showEditDialog(persons: PersonsPost) {
        val builder = AlertDialog.Builder(mCtx)
        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.update_dialog, null)
        builder.setTitle("Edit Data")

        val etName = view.findViewById<EditText>(R.id.editTextTextPersonName)
        val etAddress = view.findViewById<EditText>(R.id.editTextTextAddress)
        val btnSave = view.findViewById<Button>(R.id.btnSave)

        etName.setText(persons.name)
        etAddress.setText(persons.address)

        builder.setView(view)
        builder.setPositiveButton("Update"){_,_ ->
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

            val updatePersons = PersonsPost(persons.id, newName, newAddress)

            persons.id?.let { dbPersons.child(it).setValue(updatePersons) }

            Toast.makeText(mCtx, "Update Success", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_ ->

        }

        val alertDialog = builder.create()
        alertDialog.show()
    }
}