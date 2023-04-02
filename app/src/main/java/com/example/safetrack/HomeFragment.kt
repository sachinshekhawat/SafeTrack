package com.example.safetrack

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*


class HomeFragment : Fragment() {

   private val listContacts:ArrayList<ContactModel> = ArrayList()
    lateinit var inviteAdapter: InviteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listMembers = listOf<MemberModel>(
            MemberModel("Sachin","B-232, Unnati vihar colony jaipur","80%","220M"),
            MemberModel("Komal","B-32, Unnati vihar colony jaipur","90%","340M"),
            MemberModel("Swati","B-32, Unnati vihar colony jaipur","63%","56M"),
            MemberModel("Rishu","B-232, Unnati vihar colony jaipur","68%","910M"),
            MemberModel("Siven","B-32, Unnati vihar colony jaipur","30%","450M")
        )

        val adapter = MemberAdapter(listMembers)

        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recycler_member)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


//        val listContacts = listOf<ContactModel>(
//            ContactModel("Gittu",9983496959),
//            ContactModel("Govind",8847592886),
//            ContactModel("Pradeep",9887333734),
//            ContactModel("Neeraj",9694214166),
//            ContactModel("Dada",7850019815),
//            ContactModel("Praveen",9837483988),
//            ContactModel("Hemang",8876775879),
//            ContactModel("Dada",9977348895)
//        )



        inviteAdapter = InviteAdapter(fetchContacts())

        fetchDatabaseContacts()

        CoroutineScope(Dispatchers.IO).launch {
            insertDatabaseContacts(fetchContacts())

        }


        val inviteRecycler = requireView().findViewById<RecyclerView>(R.id.recycler_invite)

        inviteRecycler.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        inviteRecycler.adapter = inviteAdapter
    }

    private  fun fetchDatabaseContacts() {
        val database = MyCommunityDB.getDatabase(requireContext())
        database.contactDao().getAllContacts().observe(viewLifecycleOwner){
            listContacts.clear()

            listContacts.addAll(it)
            inviteAdapter.notifyDataSetChanged()
        }
    }

    private suspend fun insertDatabaseContacts(listContacts: ArrayList<ContactModel>) {

        val database = MyCommunityDB.getDatabase(requireContext())
        database.contactDao().insertAll(listContacts)


    }

    @SuppressLint("Range")
    private fun fetchContacts(): ArrayList<ContactModel> {
        val cr = requireActivity().contentResolver
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null)

        val listContacts:ArrayList<ContactModel> = ArrayList()

        if(cursor != null && cursor.count>0){

            while(cursor != null && cursor.moveToNext()){
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if(hasPhoneNumber>0 ){
                    val pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?",
                        arrayOf(id),"")

                    if(pCur != null && pCur.count>0){

                        while(pCur!=null && pCur.moveToNext()){

                            val phoneNum =pCur.getString( pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            listContacts.add(ContactModel(name,phoneNum))
                        }
                        pCur.close()
                    }

                }
            }
            if(cursor!=null){
                cursor.close()
            }

        }
        return listContacts
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}