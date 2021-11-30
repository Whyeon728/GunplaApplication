package kr.ac.kumoh.s20191391.gunplaapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import kr.ac.kumoh.s20191391.gunplaapplication.databinding.ActivityMainBinding

//https://developer.android.com/training/volley/requestqueue

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val model: GunplaViewModel by viewModels()
    private lateinit var adapter: GunplaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = GunplaAdapter(model) { mechanic -> adapterOnClick(mechanic)}
        // 만들어준 어댑터를 리사이클러뷰에 적용
        binding.list.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = this@MainActivity.adapter //메인엑티비티의 this
        }

        // 라이브 데이터를 보고있다가 바뀌면 리스트를 다시 그리는 역할
        model.list.observe(this, {
            adapter.notifyDataSetChanged()
        })

        model.requestMechanic()
    }
    private fun adapterOnClick(mechanic: GunplaViewModel.Mechanic) {
        Toast.makeText(this, mechanic.model +" : "+ mechanic.armor, Toast.LENGTH_SHORT).show()
//        val uri = Uri.parse("https://www.youtube.com/results?search_query=${mechanic.model}")
//        val intent = Intent(Intent.ACTION_VIEW, uri)
//        startActivity(intent)
    }
}