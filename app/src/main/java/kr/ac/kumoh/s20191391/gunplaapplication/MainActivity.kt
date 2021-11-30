package kr.ac.kumoh.s20191391.gunplaapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import kr.ac.kumoh.s20191391.gunplaapplication.databinding.ActivityMainBinding

//https://developer.android.com/training/volley/requestqueue

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object { // 나중에 취소할때 큐태그에 해당하는 것들을 모두취소하기위해 지정
        const val QUEUE_TAG = "VolleyReQuest"
    }

    private lateinit var mQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //싱글톤 패턴 VolleyRequest Class 사용
        mQueue = VolleyRequest.getInstance(this.applicationContext).requestQueue

        requestMechanic()
    }

    //요청이 있을 시 onStop()이 호출 될때 요청들도 멈춰줘야하기 때문에
    override fun onStop() {
        super.onStop()
        mQueue.cancelAll(QUEUE_TAG) //큐태그를 지정한 모든요청을 정지
    }

    private fun requestMechanic() {
        // NOTE: 서버 주소는 본인의 서버 주소 사용할 것
        val url = "https://dbex-volley-server-epflg.run.goorm.io/gunpladb/mechanic"

        val request = JsonArrayRequest(
            Request.Method.GET, //GET 방식으로 해당 URL에 접속하겠다
           url,
            null, //성공했을때
            {
                binding.result.text = it.toString() //가지고온결과를 textView에 출력
            },
            //에러가 발생했을때
            {
                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
                binding.result.text = it.toString()
            }
        )

        request.tag = QUEUE_TAG
        mQueue.add(request)
    }

}