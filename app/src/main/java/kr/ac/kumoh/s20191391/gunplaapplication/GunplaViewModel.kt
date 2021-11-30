package kr.ac.kumoh.s20191391.gunplaapplication

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray
import org.json.JSONObject

class GunplaViewModel(application: Application): AndroidViewModel(application) {

    companion object { // 나중에 취소할때 큐태그에 해당하는 것들을 모두취소하기위해 지정
        const val QUEUE_TAG = "VolleyReQuest"
    }
    private lateinit var mQueue: RequestQueue

    data class Mechanic (
        val id: Int,
        val name: String,
        val model: String,
        val manufacturer: String,
        val armor: String,
        val height: Double,
        val weight: Double
    )

    val list = MutableLiveData<ArrayList<Mechanic>>() //라이브 데이터 처리; 변경가능한 데이터
    private val gunpla = ArrayList<Mechanic>() //실제로 데이터를 가지고 있음

    init {
        list.value = gunpla //뮤터블 데이터에 건플라 넣어줌
        //싱글톤 패턴 VolleyRequest Class 사용
        mQueue = VolleyRequest.getInstance(application).requestQueue
    }

    //요청이 있는 동안 onStop()이 호출 될때 요청들도 멈춰줘야하기 때문에
    override fun onCleared() {
        super.onCleared()
        mQueue.cancelAll(QUEUE_TAG) //큐태그를 지정한 모든요청을 정지
    }

    /*fun getGunpla(i: Int) {
        return gunpla[i]
    }*/
    fun getGunpla(i: Int) = gunpla[i]

    fun getSize() = gunpla.size

    fun requestMechanic() {
        // NOTE: 서버 주소는 본인의 서버 주소 사용할 것
        val url = "https://dbex-volley-server-epflg.run.goorm.io/gunpladb/mechanic"

        val request = JsonArrayRequest(
            Request.Method.GET, //GET 방식으로 해당 URL에 접속하겠다
            url,
            null, //성공했을때
            {
                //binding.result.text = it.toString() //가지고온결과를 textView에 출력
                //Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
                gunpla.clear() //중복되지 않게 클리어
                parseMechanicJSON(it)
                list.value = gunpla // 뮤터블리스트에 넘겨줌
            },
            //에러가 발생했을때
            {
                Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
                //binding.result.text = it.toString()
            }
        )

        request.tag = QUEUE_TAG
        mQueue.add(request)
    }
    //Json 파싱
    private fun parseMechanicJSON(items: JSONArray) {
        for (i in 0 until items.length()) {//JSONArray의 갯수만큼 반복
            val item: JSONObject = items.getJSONObject(i) //item에 해당 오브젝트 할당
            val id = item.getInt("id") // 해당 item에 대한 속성들 가져옴
            val name = item.getString("name")
            val model = item.getString("model")
            val manufacturer = item.getString("manufacturer")
            val armor = item.getString("armor")
            val height = item.getDouble("height")
            val weight = item.getDouble("weight")

            //ArrayList에 추가
            gunpla.add(Mechanic(id, name, model, manufacturer, armor,
                height, weight))
        }
    }
}