package com.example.project001

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project001.databinding.ActivityAdminBinding
import com.example.project001.databinding.RowBinding
import com.example.project001.models.FormModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    lateinit var firestoreUserAdapter: FirestoreRecyclerAdapter<FormModel, UsersViewholder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAdapter()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.admin_menu,menu)
        return super.onCreateOptionsMenu(menu)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.admin_logout){
            PreferenceManager.saveUser(this,"","")
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setUpAdapter() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        //        val adapter = ChatAdapter(requireContext(),chatList,this)
        val db = FirebaseFirestore.getInstance()
        db.collection(MyFirebaseUtils.FORMS).get().addOnSuccessListener {
            var list = arrayListOf<FormModel>()
            for(snp in it){
                var model = FormModel(fullName = snp.getString("fullName").toString(),
                fatherName = snp.getString("fatherName").toString(),
                contactNumber = snp.getString("contactNumber").toString(),
                email = snp.getString("email").toString(),
                country = snp.getString("country").toString(),
                state =  snp.getString("state").toString(),
                district = snp.getString("district").toString(),
                village = snp.getString("village").toString())
                list.add(model)
            }
            binding.recyclerView.adapter = MyListAdapter(this,list)
        }.addOnFailureListener{

        }

       /* val options: FirestoreRecyclerOptions<FormModel> =
            FirestoreRecyclerOptions.Builder<FormModel>()
                .setQuery(query, FormModel::class.java)
                .build()

        firestoreUserAdapter =
            object : FirestoreRecyclerAdapter<FormModel, UsersViewholder>(options) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewholder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val listItemBinding = RowBinding.inflate(layoutInflater, parent, false)
                    return UsersViewholder(listItemBinding)
                }

                override fun onBindViewHolder(
                    holder: UsersViewholder,
                    position: Int,
                    model: FormModel
                ) {
                    holder.binding.fulllName.text = model.fullName
                    holder.binding.fatherName.text = model.fatherName
                }

            }
*/



    }


    class UsersViewholder(val binding: RowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(documentSnapshot: FormModel) {
            Log.d(TAG, "bind: ${documentSnapshot.toString()}")
        }
    }
}

class MyListAdapter(adminActivity: AdminActivity,val list: ArrayList<FormModel>) :
    RecyclerView.Adapter<MyListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val binding = RowBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.fulllName.text = list[position].fullName
        holder.binding.fatherName.text = list[position].fatherName
    }


}
