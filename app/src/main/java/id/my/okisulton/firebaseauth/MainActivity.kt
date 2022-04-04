package id.my.okisulton.firebaseauth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import id.my.okisulton.firebaseauth.adapter.PersonsAdapter
import id.my.okisulton.firebaseauth.databinding.ActivityMainBinding
import id.my.okisulton.firebaseauth.model.post.PersonsPost

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() =  _binding!!

    private lateinit var ref : DatabaseReference
    private lateinit var listPersons : MutableList<PersonsPost>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listPersons = mutableListOf()

        ref = FirebaseDatabase.getInstance().getReference("Persons")

        getData()
    }

    override fun onStart() {
        super.onStart()
        setupListener()
    }

    private fun getData() {
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listPersons.clear()
                    for (x in snapshot.children) {
                        val person = x.getValue(PersonsPost::class.java)
                        if (person != null) {
                            listPersons.add(person)
                        }
                    }
                    val adapter = PersonsAdapter(applicationContext, R.layout.item_persons, listPersons)
                    binding.lvPersons.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun setupListener() {
        binding.btnSave.setOnClickListener { saveData() }
    }

    private fun saveData() {
        val name = binding.editTextTextPersonName.text.toString().trim()
        val address = binding.editTextTextAddress.text.toString().trim()
        if (name.isEmpty()) {
            binding.editTextTextPersonName.error = "Can't be null"
            return
        }
        if (address.isEmpty()) {
            binding.editTextTextAddress.error = "can't be null"
            return
        }

        val personId = ref.push().key
        val persons = PersonsPost(personId, name, address)

        if (personId != null) {
            ref.child(personId).setValue(persons).addOnCompleteListener{
                Toast.makeText(this, "Saving Success", Toast.LENGTH_SHORT).show()
            }
        }
    }
}