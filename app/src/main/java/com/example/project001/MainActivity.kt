package com.example.project001

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project001.adapters.ConstituencyAdapter
import com.example.project001.adapters.CountryAdapter
import com.example.project001.adapters.DisctrictAdapter
import com.example.project001.adapters.StateAdapter
import com.example.project001.databinding.ActivityMainBinding
import com.example.project001.models.Country
import com.example.project001.models.Districts
import com.example.project001.models.FormModel
import com.example.project001.models.States
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson


class MainActivity : AppCompatActivity(), CountryAdapter.CountryInterface,
    StateAdapter.StateInterface, DisctrictAdapter.DistrictInterface,
    ConstituencyAdapter.ConstituencySelectInterface {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"
    lateinit var alertDialog: AlertDialog

    lateinit var alertAdaper: CountryAdapter
    lateinit var stateAdapter: StateAdapter
    lateinit var districtAdapter: DisctrictAdapter
    lateinit var constituencyAdapter: ConstituencyAdapter

    var statelist:List<States> = arrayListOf()
    var districtslist:List<Districts> = arrayListOf()
    var constituencylist:List<String> = arrayListOf()

    lateinit var country:Country


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        country = Gson().fromJson(Utils.json_data,Country::class.java)
        alertAdaper = CountryAdapter(this,country,this)
        Log.d(TAG, "onCreate: ${Gson().toJson(country)}")

        binding.btnSubmit.setOnClickListener {
            //Validate all fields

            //Submit data to fb

            pushFormToFirebase()
        }

        clickEvents()

    }

    private fun clickEvents() {

        binding.nri.setOnClickListener {
            binding.nri.background = resources.getDrawable(R.drawable.round)
            binding.nrv.setBackgroundColor(resources.getColor(R.color.white))
            binding.nri.setTextColor(resources.getColor(R.color.white))
            binding.nrv.setTextColor(resources.getColor(R.color.black))
            binding.nri.tag = "1"

        }
        binding.nrv.setOnClickListener {
            binding.nri.setBackgroundColor(resources.getColor(R.color.white))
            binding.nrv.background = resources.getDrawable(R.drawable.round)
            binding.nri.setTextColor(resources.getColor(R.color.black))
            binding.nrv.setTextColor(resources.getColor(R.color.white))
            binding.nri.tag = "0"
        }
        binding.etCountry.setOnClickListener {
            showCountry("Select country")
        }
        binding.etState.setOnClickListener {
            statelist = country.states

            showStates("Select State", statelist)
        }
        binding.etDistrict.setOnClickListener {
            showDistricts("Select District", districtslist)
        }
        binding.etConstituency.setOnClickListener {
            showConstituency("Select Constituency",constituencylist)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_menu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.user_logout){
            PreferenceManager.saveUser(this,"","")
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun pushFormToFirebase() {
        var category = ""
        if (binding.nri.tag == "1"){
            category = "NRI"
        }else{
            category = "NRV"
        }
        var model = FormModel(
            category = category,
            fullName = binding.etFullname.text.toString(),
            fatherName = binding.etFathername.text.toString(),
            aadharNumber = binding.etAadhar.text.toString(),
            contactNumber = binding.etContact.text.toString(),
            email = binding.etEmail.text.toString(),
            occupation = binding.etOccupation.text.toString(),
            country = binding.etCountry.text.toString(),
            state = binding.etState.text.toString(),
            district = binding.etDistrict.text.toString(),
            constituency = binding.etConstituency.text.toString(),
            mandal = binding.etMandal.text.toString(),
            village = binding.etVillage.text.toString(),
            modeOfContribution = binding.etMoc.text.toString(),
            remarks = binding.etRemarks.text.toString()
        )

        val db = FirebaseFirestore.getInstance()
        db.collection(MyFirebaseUtils.FORMS)
            .document()
            .set(model)
            .addOnSuccessListener { showToast("New Record Added")
                clearForm()}
            .addOnFailureListener { showToast("Error while saving data!") }
    }

    private fun clearForm() {
        binding.etFullname.setText("")
        binding.etFathername.setText("")
        binding.etContact.setText("")
        binding.etEmail.setText("")
        binding.etCountry.setText("")
        binding.etState.setText("")
        binding.etDistrict.setText("")
        binding.etVillage.setText("")
        binding.nri.background = resources.getDrawable(R.drawable.round)
        binding.nrv.setBackgroundColor(resources.getColor(R.color.white))
        binding.nri.setTextColor(resources.getColor(R.color.white))
        binding.nrv.setTextColor(resources.getColor(R.color.black))
        binding.nri.tag = "1"
        binding.etAadhar.setText("")
        binding.etOccupation.setText("")
        binding.etConstituency.setText("")
        binding.etMandal.setText("")
        binding.etMoc.setText("")
        binding.etRemarks.setText("")
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    private fun showCountry(heading: String) {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.alert_catgory_dialog, null)
        dialogBuilder.setView(dialogView)

        val alertName = dialogView.findViewById<TextView>(R.id.alertName)
        val closeBtn = dialogView.findViewById<ImageView>(R.id.closeBtn)
        alertName.text = heading
        val catRecycler = dialogView.findViewById<RecyclerView>(R.id.catRecycler)

        catRecycler.layoutManager = LinearLayoutManager(this)

        catRecycler.adapter = alertAdaper
        alertAdaper.notifyDataSetChanged()

        alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        closeBtn.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }
    private fun showStates(heading: String, states: List<States>) {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.alert_catgory_dialog, null)
        dialogBuilder.setView(dialogView)

        val alertName = dialogView.findViewById<TextView>(R.id.alertName)
        val closeBtn = dialogView.findViewById<ImageView>(R.id.closeBtn)
        alertName.text = heading
        val catRecycler = dialogView.findViewById<RecyclerView>(R.id.catRecycler)

        catRecycler.layoutManager = LinearLayoutManager(this)
//        alertAdaper.i = loadData()
//        alertAdaper.alerttype = "category"
//        alertAdaper = alertAdaper
        stateAdapter = StateAdapter(this,states,this)
        catRecycler.adapter = stateAdapter
        stateAdapter.notifyDataSetChanged()

        alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        closeBtn.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }

    private fun showDistricts(s: String, districtslist: List<Districts>) {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.alert_catgory_dialog, null)
        dialogBuilder.setView(dialogView)

        val alertName = dialogView.findViewById<TextView>(R.id.alertName)
        val closeBtn = dialogView.findViewById<ImageView>(R.id.closeBtn)
        alertName.text = s
        val catRecycler = dialogView.findViewById<RecyclerView>(R.id.catRecycler)

        catRecycler.layoutManager = LinearLayoutManager(this)
//        alertAdaper.i = loadData()
//        alertAdaper.alerttype = "category"
//        alertAdaper = alertAdaper
        districtAdapter = DisctrictAdapter(this,districtslist,this)
        catRecycler.adapter = districtAdapter
        districtAdapter.notifyDataSetChanged()

        alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        closeBtn.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }


    private fun showConstituency(s: String, constituencylist: List<String>) {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.alert_catgory_dialog, null)
        dialogBuilder.setView(dialogView)

        val alertName = dialogView.findViewById<TextView>(R.id.alertName)
        val closeBtn = dialogView.findViewById<ImageView>(R.id.closeBtn)
        alertName.text = s
        val catRecycler = dialogView.findViewById<RecyclerView>(R.id.catRecycler)

        catRecycler.layoutManager = LinearLayoutManager(this)
//        alertAdaper.i = loadData()
//        alertAdaper.alerttype = "category"
//        alertAdaper = alertAdaper
        constituencyAdapter = ConstituencyAdapter(this,constituencylist,this)
        catRecycler.adapter = constituencyAdapter
        constituencyAdapter.notifyDataSetChanged()

        alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        closeBtn.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }
    override fun onCountryClick(position: Int, states: List<States>) {
        binding.etCountry.setText("INDIA")
        binding.etState.setText(states[0].stateName)
        statelist = states
        alertDialog.dismiss()

    }
    override fun onStateClick(position: Int, states: List<Districts>) {
        binding.etState.setText(country.states[0].stateName)
        binding.etDistrict.setText(states[position].name)
        districtslist = states
        alertDialog.dismiss()
    }

    override fun onConstituencyClick(constituency: String) {
        binding.etConstituency.setText(constituency)
        alertDialog.dismiss()
    }

    override fun onDistrictClick(selectedDistricts: String,position: Int, states: List<String>) {
        binding.etDistrict.setText(selectedDistricts)
        binding.etConstituency.setText(states[0])
        constituencylist = states
        alertDialog.dismiss()
    }
}